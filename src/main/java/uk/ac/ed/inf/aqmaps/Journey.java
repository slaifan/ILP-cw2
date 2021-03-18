package uk.ac.ed.inf.aqmaps;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;

// class which holds the flight of the drone on a given date
public class Journey {
	private int day;
	private int month;
	private int year;
	private ArrayList<Sensor> sensors;
	private HashMap<Coordinate, Sensor> sensorLocations;
	private Coordinate[] orderedJourney;
	private ArrayList<Coordinate> visited = new ArrayList<>();
	private int journeyLength;
	private Duration executionTime;
	private String movements;
	private String sensorReadings;
	private int visitedSensorsCount = 0;
	
	/// ONCE OBJECT IS CREATED IT IS NEVER MODIFIED (SEE REPORT)
	public Journey(int day, int month, int year, JourneyStrategy journeyStrategy, NavigationStrategy navigationStrategy, Coordinate start) {
		this.day = day;
		this.month = month;
		this.year = year;
		this.journeyLength = 0;
		parseSensors();
		sensorLocations();
		
		var sensorCoordinates = sensorLocations.keySet().toArray(new Coordinate[sensors.size()]);
		var dronePath = new Coordinate[sensorCoordinates.length + 1];
		dronePath[0] = start;
		for (int i = 1; i < dronePath.length; i++) {
			dronePath[i] = sensorCoordinates[i - 1];
		}
		
		var startTime = Instant.now(); // EXECUTION PERFORMANCE START
		this.orderedJourney = journeyStrategy.planJourney(dronePath);
		this.movements = recordJourneyPath(navigationStrategy);
		var endTime = Instant.now();// EXECUTION PERFORMANCE END

		this.executionTime = Duration.between(startTime, endTime);
		this.sensorReadings = recordReadings();
	}
	
	// get from server
	private void parseSensors() {
	    var jsonAirQuality = WebServerConnection.getAirQualityData(year, month, day);
	    if (jsonAirQuality == null) {
			System.out.println("could not get air quality data from server");
			return;
	    }
	    Type sensorsListType = new TypeToken<ArrayList<Sensor>>() {}.getType();
		sensors = new Gson().fromJson(jsonAirQuality, sensorsListType);
	}
	
	// useful for finding sensors from their coordinates later
	private void sensorLocations() {
		HashMap<Coordinate, Sensor> sensorLocations = new HashMap<>();
		for (int i = 0; i < sensors.size(); i++) {
			var sensor = sensors.get(i);
			var coords = sensor.getCoordinates();
			sensorLocations.put(coords, sensor);
		}
		this.sensorLocations = sensorLocations;
	}
	
	// fly from 1 sensor to the next and log the movement
	private String recordLocalNavigation(Coordinate start, Coordinate end, int nextSensorIndex, NavigationStrategy navigationStrategy) {
		var flightPath = new StringBuilder();
		var way = navigationStrategy.findPath(start, end);
		var sensorName = nextSensorIndex > 0 ? sensorLocations.get(end).getLocation() : "null";
		for (int j = 0; j < way.length - 1; j++) {
			journeyLength++;
			if (journeyLength > ProjectConstants.MAX_STEPS) {
				return flightPath.toString(); // if over 150 drone stops
			}
			var coordinate = way[j];
			var nxt = way[j + 1];
			var angle = (int) (Math.round(coordinate.getAngle(nxt)/ProjectConstants.ROTATION_ANGLE) * 10); // round angle to prevent annoying 89.999 degrees instead of 90 for example
			var scannedSensor = (j ==  way.length - 2) ? sensorName : "null";
			var movement = String.format("%s,%s,%s,%s,%s,%s,%s\n", journeyLength, coordinate.getLng(), coordinate.getLat(), angle, nxt.getLng(), nxt.getLat(), scannedSensor);
			flightPath.append(movement);
			visited.add(coordinate); // record that drone visited this node
		}
		var last = way[way.length - 1];
		visited.add(last);
		orderedJourney[nextSensorIndex % orderedJourney.length] = last;
		visitedSensorsCount++; // if went to next sensor successfully, we make sure to give it the right symbol, useful in visualising as MapBox in recordReadings()
		return flightPath.toString();
	}
	
	// start flying, record flight
	private String recordJourneyPath(NavigationStrategy navigationStrategy) {
		var flightPath = new StringBuilder();
		for (int i = 0; i < orderedJourney.length; i++) {
			if (journeyLength > ProjectConstants.MAX_STEPS) {
				return flightPath.toString();
			}
			flightPath.append(recordLocalNavigation(orderedJourney[i], orderedJourney[(i + 1) % orderedJourney.length], (i + 1) % orderedJourney.length, navigationStrategy));
		}
		return flightPath.toString();
	}
	
	// setup the JSON string
	private String recordReadings() {
		ArrayList<Feature> fl = new ArrayList<>();
		for (int i = 0; i < sensors.size(); i++) {
			var sensorFeature = sensors.get(i).toMapBox(i < visitedSensorsCount);
			fl.add(sensorFeature);
		}
		
		var flightPathAsMapboxPoints = visited.stream().map(Coordinate::toMapBox).collect(Collectors.toList());
		var linePath = (Geometry) LineString.fromLngLats(flightPathAsMapboxPoints);
		fl.add(Feature.fromGeometry(linePath));
		return FeatureCollection.fromFeatures(fl).toJson();
	}

	
	public Coordinate[] getOrderedJourney() {
		return this.orderedJourney;
	}
	
	public int getJourneyLength() {
		return this.journeyLength;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	public int getYear() {
		return this.year;
	}

	public String getReadings() {
		return this.sensorReadings;
	}
	
	public String getJourneyPath() {
		return this.movements;
	}

	public Duration getExecutionTime() {
		return executionTime;
	}

}
