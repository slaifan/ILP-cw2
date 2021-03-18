package uk.ac.ed.inf.aqmaps;

import java.awt.geom.Line2D;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

public class LocationChecker {
	
	private static List<Building> noFlyAreas = new ArrayList<Building>();
	
	// Initialise the no fly zones
	public static void setup() {
		if (noFlyAreas.size() == 0) {
			String buildingsJson = WebServerConnection.getBuildings();
			if (buildingsJson == null)
				return;
			var buildingsMapbox = FeatureCollection.fromJson(buildingsJson);
			for (Feature buildingFeature : buildingsMapbox.features()) {
				var b = new Building(buildingFeature);
				noFlyAreas.add(b);
			}
		}
	}
	
	public static boolean isValidPath(Coordinate p1, Coordinate p2) {
		return isWithinBounds(p2) && !isColliding(p1, p2);
	}
	
	// checks if the path between p1 and p2 collides with any buildings
	private static boolean isColliding(Coordinate p1, Coordinate p2) {
        Line2D path = new Line2D.Double(p1.getLng(), p1.getLat(), p2.getLng(), p2.getLat());
        for (Building building : noFlyAreas) {
			if (building.passedBy(path)) {
//				System.out.println("collision detected for point (" + path.getX1() + ", " + path.getY1() + ")");
				return true;
			}
		}
		return false;
	}

	// can be moved to Coordinate as well, I'm not sure which is more intuitive
	private static boolean isWithinBounds(Coordinate p) {
		var lng = p.getLng();
		var lat = p.getLat();
		var isValidHorizontal = lng >= ProjectConstants.BOUNDARIES[2] && lng <= ProjectConstants.BOUNDARIES[3];
		var isValidVertical = lat >= ProjectConstants.BOUNDARIES[1] && lat <= ProjectConstants.BOUNDARIES[0];
		
		return isValidHorizontal && isValidVertical;
	}

}
