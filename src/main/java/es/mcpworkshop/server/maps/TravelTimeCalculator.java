package es.mcpworkshop.server.maps;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TravelTimeCalculator implements DisposableBean {

  private final GeoApiContext context;

  private static final Logger LOG = LoggerFactory.getLogger(TravelTimeCalculator.class);

  public TravelTimeCalculator(@Value("${google_maps.api_key}") String apiKey) {
    LOG.info("INITIALIZING API CONTEXT");
    this.context = new GeoApiContext.Builder().apiKey(apiKey).build();
    LOG.info("API Key: {}", apiKey.substring(0, 5));
  }

  @Override
  public void destroy() throws Exception {
    this.context.shutdown();
    LOG.info("SHUTTING DOWN API CONTEXT");
  }

  @Tool(
      description =
          """
                          You are a google maps travel assistant, and your job is to return the time to travel
                           from origin city to destination city.
                          """)
  public String getTravelTime(
      @ToolParam(description = "The origin city") String origin,
      @ToolParam(description = "The target destination city") String destination) {
    LOG.info("Getting travel time from {} to {}", origin, destination);
    try {
      DirectionsApiRequest request =
          DirectionsApi.newRequest(context)
              .origin(origin)
              .destination(destination)
              .mode(TravelMode.DRIVING);

      DirectionsResult result = request.await();

      if (result != null && result.routes != null && result.routes.length > 0) {
        LOG.info("Found at least {} routes", result.routes.length);
        DirectionsRoute route = result.routes[0];
        if (route.legs != null && route.legs.length > 0) {
          Duration duration = route.legs[0].duration;
          Duration durationInTraffic = route.legs[0].durationInTraffic;

          TravelTimeResult travelTimeResult =
              new TravelTimeResult(
                  duration.humanReadable,
                  duration.inSeconds,
                  durationInTraffic != null ? durationInTraffic.humanReadable : null,
                  durationInTraffic != null ? durationInTraffic.inSeconds : null,
                  origin,
                  destination,
                  TravelMode.DRIVING.toString());
          LOG.info(
              "Travel time from {} to {} takes {}",
              origin,
              destination,
              travelTimeResult.durationText());
          return travelTimeResult.durationText();
        }
      }

      throw new RuntimeException("No route found between origin and destination");

    } catch (Exception e) {
      LOG.error("Could not make it", e);
      throw new RuntimeException("Error calculating travel time: " + e.getMessage(), e);
    }
  }

  record TravelTimeResult(
      String durationText,
      long durationSeconds,
      String durationInTrafficText,
      Long durationInTrafficSeconds,
      String origin,
      String destination,
      String travelMode) {}
}
