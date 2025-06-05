package es.mcpworkshop.server.maps;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.TravelMode;
import java.io.IOException;

public class TravelTimeCalculator {
    private final GeoApiContext context;

    /**
     * Creates a TravelTimeCalculator using an API key for authentication.
     * 
     * @param apiKey The Google Maps API key
     */
    public TravelTimeCalculator(String apiKey) {
        this.context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }



    public TravelTimeResult getTravelTime(String origin, String destination, TravelMode travelMode) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(context)
                    .origin(origin)
                    .destination(destination)
                    .mode(travelMode)
                    .await();

            if (result.routes != null && result.routes.length > 0) {
                DirectionsRoute route = result.routes[0];
                if (route.legs != null && route.legs.length > 0) {
                    Duration duration = route.legs[0].duration;
                    Duration durationInTraffic = route.legs[0].durationInTraffic;

                    return new TravelTimeResult(
                            duration.humanReadable,
                            duration.inSeconds,
                            durationInTraffic != null ? durationInTraffic.humanReadable : null,
                            durationInTraffic != null ? durationInTraffic.inSeconds : null,
                            origin,
                            destination,
                            travelMode.toString()
                    );
                }
            }

            throw new RuntimeException("No route found between origin and destination");

        } catch (Exception e) {
            throw new RuntimeException("Error calculating travel time: " + e.getMessage(), e);
        }
    }

    public void shutdown() {
        context.shutdown();
    }

    public static class TravelTimeResult {
        private final String durationText;
        private final long durationSeconds;
        private final String durationInTrafficText;
        private final Long durationInTrafficSeconds;
        private final String origin;
        private final String destination;
        private final String travelMode;

        public TravelTimeResult(String durationText, long durationSeconds, 
                              String durationInTrafficText, Long durationInTrafficSeconds,
                              String origin, String destination, String travelMode) {
            this.durationText = durationText;
            this.durationSeconds = durationSeconds;
            this.durationInTrafficText = durationInTrafficText;
            this.durationInTrafficSeconds = durationInTrafficSeconds;
            this.origin = origin;
            this.destination = destination;
            this.travelMode = travelMode;
        }

        public String getDurationText() { return durationText; }
        public long getDurationSeconds() { return durationSeconds; }
        public String getDurationInTrafficText() { return durationInTrafficText; }
        public Long getDurationInTrafficSeconds() { return durationInTrafficSeconds; }
        public String getOrigin() { return origin; }
        public String getDestination() { return destination; }
        public String getTravelMode() { return travelMode; }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Travel from: ").append(origin).append("\n");
            sb.append("Travel to: ").append(destination).append("\n");
            sb.append("Travel mode: ").append(travelMode).append("\n");
            sb.append("Duration: ").append(durationText).append(" (").append(durationSeconds).append(" seconds)\n");
            if (durationInTrafficText != null) {
                sb.append("Duration in traffic: ").append(durationInTrafficText)
                  .append(" (").append(durationInTrafficSeconds).append(" seconds)\n");
            }
            return sb.toString();
        }
    }
}
