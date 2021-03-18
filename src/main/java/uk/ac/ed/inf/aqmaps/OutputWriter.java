package uk.ac.ed.inf.aqmaps;

import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {
	public static void write(String content, String fileName) {
	    try {
	      FileWriter myWriter = new FileWriter(fileName);
	      myWriter.write(content);
	      myWriter.close();
	    } catch (IOException e) {
	      System.out.println("An error occurred while writing to " + fileName);
	      e.printStackTrace();
	    }
	  }
	
	// JSON output file
	public static void writeReadingsFile(Journey journey) {
		var dayStr = journey.getDay() < 10 ? "0" + journey.getDay() : "" + journey.getDay();
		var monthStr = journey.getMonth() < 10 ? "0" + journey.getMonth() : "" + journey.getMonth();
		String fileName = String.format("readings-%s-%s-%s.geojson", dayStr, monthStr, journey.getYear());
		write(journey.getReadings(), fileName);
	}
	
	// txt output file
	public static void writeFlightPathFile(Journey journey) {
		var dayStr = journey.getDay() < 10 ? "0" + journey.getDay() : "" + journey.getDay();
		var monthStr = journey.getMonth() < 10 ? "0" + journey.getMonth() : "" + journey.getMonth();
		var fileName = String.format("flightpath-%s-%s-%s.txt", dayStr, monthStr, journey.getYear());
		var journeyPath = journey.getJourneyPath();
		write(journeyPath, fileName);
	}
}
