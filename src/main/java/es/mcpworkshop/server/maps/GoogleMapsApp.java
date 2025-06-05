package es.mcpworkshop.server.maps;

import com.google.maps.model.TravelMode;

public class GoogleMapsApp {

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -cp target/classes es.mcpworkshop.server.maps.GoogleMapsApp <origin> <destination> [travel_mode]");
            System.out.println("Travel modes: DRIVING (default), WALKING, BICYCLING, TRANSIT");
            System.out.println();
            System.out.println("Authentication Options:");
            System.out.println(" API Key: Set GOOGLE_MAPS_API_KEY environment variable");
            System.exit(1);
        }

        String origin = args[0];
        String destination = args[1];
        TravelMode travelMode = TravelMode.DRIVING;

        if (args.length > 2) {
            try {
                travelMode = TravelMode.valueOf(args[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid travel mode: " + args[2]);
                System.err.println("Valid modes: DRIVING, WALKING, BICYCLING, TRANSIT");
                System.exit(1);
            }
        }

        TravelTimeCalculator calculator = null;

        try {
            // Try API key authentication first
            String apiKey = System.getenv("GOOGLE_MAPS_API_KEY");
            if (apiKey != null && !apiKey.isEmpty()) {
                calculator = new TravelTimeCalculator(apiKey);
                System.out.println("Calculating travel time using API key...\n");

                TravelTimeCalculator.TravelTimeResult result = calculator.getTravelTime(
                    origin, destination, travelMode
                );

                System.out.println(result.getDurationText());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        } finally {
            if (calculator != null) {
                calculator.shutdown();
            }
        }
    }
}
