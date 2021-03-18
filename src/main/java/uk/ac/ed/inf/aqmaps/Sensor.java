package uk.ac.ed.inf.aqmaps;

import com.google.gson.Gson;
import com.mapbox.geojson.Feature;

public class Sensor {
	private String location;
	private double battery;
	private String reading;
	private What3Word word;
	private ReadingProperties readingProperties;

	public Coordinate getCoordinates() {
		if (word == null) {
			var jsonWord = WebServerConnection.getWords(location.split("\\."));
			this.word = new Gson().fromJson(jsonWord, What3Word.class);
		}
		return word.getCoordinates();
	}

	public Feature toMapBox(boolean visited) {
		if (word == null) {
			var jsonWord = WebServerConnection.getWords(location.split("\\."));
			this.word = new Gson().fromJson(jsonWord, What3Word.class);
		}
		var point = word.getCoordinates().toMapBox();
		var feature = Feature.fromGeometry(point);
		if (battery <= 10.0) {
			this.readingProperties = new ReadingProperties("unreliable");
		}
		if (!visited) {
			this.readingProperties = new ReadingProperties("-1"); // value outside range indicates not visited
//			System.out.println("not visited");
		}
		else {
			this.readingProperties = new ReadingProperties(reading);
		}
		feature.addStringProperty("rgb-string", readingProperties.getRGB());
		feature.addStringProperty("marker-color", readingProperties.getRGB());
		feature.addStringProperty("location", location);
		feature.addStringProperty("marker-size", ProjectConstants.MARKER_SIZE);
		feature.addStringProperty("marker-symbol", readingProperties.getSymbol());
		
		return feature;
	}

	public String getLocation() {
		return this.location;
	}
}
