package uk.ac.ed.inf.aqmaps;
/**
 * 
 * Abstract class, implementations define how to calculate distance between two points.
 *
 */
abstract class DistanceCalculator {
	public abstract Double getDistance(Coordinate p1, Coordinate p2);
	
	public Double[][] getAllDistances(Coordinate[] coords) {
		var distances = new Double[coords.length][coords.length];
		for (int i = 0; i < distances.length; i++) {
			for (int j = i; j < distances[i].length; j++) {
				var dist = this.getDistance(coords[i], coords[j]);
				distances[i][j] = dist;
				distances[j][i] = dist;
			}
		}
		return distances;
	}

	public Double getTotalDistance(Coordinate[] coords) {
		var sum = 0.0;
		for (int i = 0; i < coords.length - 1; i++) {
			sum += this.getDistance(coords[i], coords[i + 1]);
		}
		return sum;
	}
	
	// all distances and back to start
	public Double getTourDistance(Coordinate[] coords) {
		var lastToFirst = this.getDistance(coords[coords.length - 1], coords[0]); // to close the circuit
		return this.getTotalDistance(coords) + lastToFirst;
	}
}
