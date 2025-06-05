# Google Maps Travel Time Calculator

A Java application using Maven and Java 21 to calculate travel time to destinations using the Google Maps API with API key authentication.

## Authentication Setup

This application supports API key authentication (recommended) and OAuth2 authentication (legacy). Choose one of the following methods:

### Option 1: API Key (Recommended)

1. **Create an API Key:**
   - Go to the [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the "Directions API"
   - Go to "APIs & Services" > "Credentials"
   - Create an API key
   - Restrict the API key to the Directions API for better security

2. **Set Environment Variable:**
   ```bash
   export GOOGLE_MAPS_API_KEY=your_api_key_here
   ```

### Option 2: OAuth2 Service Account (Legacy)

1. **Create a Service Account:**
   - Go to the [Google Cloud Console](https://console.cloud.google.com/)
   - Create a new project or select an existing one
   - Enable the "Directions API"
   - Go to "IAM & Admin" > "Service Accounts"
   - Create a new service account
   - Download the JSON key file

2. **Set Environment Variable:**
   ```bash
   export GOOGLE_APPLICATION_CREDENTIALS=/path/to/your/service-account-key.json
   ```



## Build the Project

```bash
mvn clean compile
```

## Usage

Run the application with origin and destination:

```bash
mvn exec:java -Dexec.mainClass="es.mcpworkshop.server.maps.GoogleMapsApp" -Dexec.args="'New York, NY' 'Los Angeles, CA'"
```

Or compile and run directly:

```bash
mvn clean compile
java -cp target/classes es.mcpworkshop.server.maps.GoogleMapsApp "New York, NY" "Los Angeles, CA"
```

### Travel Modes

You can specify different travel modes:

```bash
# Driving (default)
java -cp target/classes es.mcpworkshop.server.maps.GoogleMapsApp "Times Square, NYC" "Central Park, NYC" DRIVING

# Walking
java -cp target/classes es.mcpworkshop.server.maps.GoogleMapsApp "Times Square, NYC" "Central Park, NYC" WALKING

# Bicycling
java -cp target/classes es.mcpworkshop.server.maps.GoogleMapsApp "Times Square, NYC" "Central Park, NYC" BICYCLING

# Public Transit
java -cp target/classes es.mcpworkshop.server.maps.GoogleMapsApp "Times Square, NYC" "Central Park, NYC" TRANSIT
```

## Example Output

```
Calculating travel time using API key...

Travel from: New York, NY
Travel to: Los Angeles, CA
Travel mode: DRIVING
Duration: 1 day 16 hours (144000 seconds)
Duration in traffic: 1 day 18 hours (151200 seconds)
```

## API Features

The application provides:
- Travel time calculation between any two locations
- Support for different travel modes (driving, walking, bicycling, transit)
- Duration with and without traffic (when available)
- Human-readable and seconds format for durations

## Authentication Flow

The application follows this priority order:
1. **API Key** (via `GOOGLE_MAPS_API_KEY`) - Recommended

## Requirements

- Java 21
- Maven 3.6+
- Google Cloud Project with Directions API enabled
- Google Maps API Key (recommended) or OAuth2 credentials (legacy)
