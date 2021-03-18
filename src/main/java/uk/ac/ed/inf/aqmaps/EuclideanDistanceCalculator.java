package uk.ac.ed.inf.aqmaps;

public class EuclideanDistanceCalculator extends DistanceCalculator {

	@Override
	public Double getDistance(Coordinate p1, Coordinate p2) {
		var y = p1.getLng() - p2.getLng();
		var x = p1.getLat() - p2.getLat();
		var squared = Math.pow(y, 2) + Math.pow(x, 2); // (y1 - y2)^2 + (x1 - x2)^2
		return Math.sqrt(squared);
	}
}
