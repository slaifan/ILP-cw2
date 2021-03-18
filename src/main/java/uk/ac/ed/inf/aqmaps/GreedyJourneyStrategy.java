package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.HashSet;

public class GreedyJourneyStrategy implements JourneyStrategy {
	DistanceCalculator distanceCalculator = DistanceCalculatorFactory.getDefault("journey");
	
	public Coordinate[] planJourney(Coordinate[] tour) {
		Double[][] distances = distanceCalculator.getAllDistances(tour);
		var greedyTour = findMinRoute(distances);
		var result = new Coordinate[greedyTour.length];
		for (int i = 0; i < greedyTour.length; i++) {
			var idx = greedyTour[i];
//			System.out.println(i + " " + idx);
			result[i] = tour[idx];
		}
		return result;
	}
	
	
	// returns indices
	private Integer[] findMinRoute(Double[][] tsp)
    {
		HashSet<Integer> visited = new HashSet<>();
		var path = new ArrayList<Integer>();
		path.add(0);
		visited.add(0);
		while (visited.size() < tsp.length) {
			var lastPoint = path.get(path.size() - 1);
			var min = Double.MAX_VALUE;
			var minIdx = lastPoint;
			for (int i = 0; i < tsp[lastPoint].length; i++) {
				if (i != lastPoint && !visited.contains(i) && tsp[lastPoint][i] < min) {
					min = tsp[lastPoint][i];
					minIdx = i;
				}
			}
			path.add(minIdx);
			visited.add(minIdx);
		}
		return path.toArray(new Integer[path.size()]);
    }
}
