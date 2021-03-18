package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BestFirstNavigationStrategy implements NavigationStrategy {
	DistanceCalculator distanceCalculator = DistanceCalculatorFactory.getDefault("navigation");
	
	@Override
	public Coordinate[] findPath(Coordinate start, Coordinate finish) {
//		System.out.println("started findPath(" + start + ", " + finish + ")");
		PriorityQueue<NavigationPath> paths = new PriorityQueue<>();
		var initialPath = new NavigationPath(finish);
		initialPath.add(start);
		paths.add(initialPath);
		var path = paths.peek();
		while (!paths.isEmpty()) {
//			System.out.println("there are now " + paths.size() + " paths to explore");
			path = paths.poll();

			if (distanceCalculator.getDistance(path.last, path.target) <= ProjectConstants.SCAN_RANGE) {
//				System.out.println("found");
				var pathAsArray = path.getPathSteps().toArray(new Coordinate[path.getPathSteps().size()]);
				return pathAsArray;
			}
			var nextSteps = expand(path);
			for (NavigationPath step : nextSteps) {
//				System.out.println(step);
					paths.add(step);
			}
		}
		var pathAsArray = path.getPathSteps().toArray(new Coordinate[path.getPathSteps().size()]);
		return pathAsArray;
	}
	
	public ArrayList<NavigationPath> expand(NavigationPath path) {
		var nextSteps = new ArrayList<NavigationPath>();
//		System.out.println("expand() started");
		for (int degree = 0; degree < 360; degree+= ProjectConstants.ROTATION_ANGLE) {
//			System.out.println("degree = " + degree);
			Coordinate newCoord = path.last.getPointAt(ProjectConstants.STEP_SIZE, degree);
			var steps = path.getPathSteps();
			if (LocationChecker.isValidPath(path.last, newCoord) && !steps.subList(0, steps.size() - 1).contains(path.last)) {
				ArrayList<Coordinate> newPath = (ArrayList<Coordinate>) steps.clone();
				newPath.add(newCoord);
				nextSteps.add(new NavigationPath(path.target, newPath));
			}
		}
		var result = new ArrayList<NavigationPath>();
		try {
			result.add(Collections.min(nextSteps));
		}
		catch (NoSuchElementException e) {
			System.out.println("Algorithm was unable to find a path");
			System.exit(2);
			return null;
		}
		return result;
	}
}
