package es.mcpworkshop.server.maps;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GoogleMapsApp {

  public static void main(String[] args) {
    SpringApplication.run(GoogleMapsApp.class, args);
  }

  @Bean
  public ToolCallbackProvider travelTools(TravelTimeCalculator travelTimeCalculator) {
    return MethodToolCallbackProvider.builder().toolObjects(travelTimeCalculator).build();
  }
}
