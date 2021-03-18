package uk.ac.ed.inf.aqmaps;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Polygon;

public class Building {
	private List<Coordinate> coordinates = new ArrayList<>();

	public Building(Feature buildingFeature) {
		var points = ((Polygon) buildingFeature.geometry()).coordinates().get(0);
		for (int i = 0; i < points.size(); i++) {
			var point = points.get(i);
			var lng = point.longitude();
			var lat = point.latitude();
			this.coordinates.add(new Coordinate(lng, lat));
		}
	}

	// checks if a given line passes our object
	public boolean passedBy(Line2D path) {
		for (int i = 0; i < coordinates.size() - 1; i++) {
			var p1 = coordinates.get(i);
			var p2 = coordinates.get(i + 1);
			Line2D buildingEdge = new Line2D.Double(p1.getLng(), p1.getLat(), p2.getLng(), p2.getLat()); // x1, y1, x2, y2 
			if (buildingEdge.intersectsLine(path)) {
				return true;
			}
		}
		return false;
	}

}
