package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class AStarNavigationStrategy implements NavigationStrategy {
	DistanceCalculator distanceCalculator = DistanceCalculatorFactory.getDefault("navigation");
	
	public Coordinate[] findPath(Coordinate start, Coordinate finish) {
		PriorityQueue<NavigationPath> paths = new PriorityQueue<>();
		var initial = new ArrayList<Coordinate>();
		initial.add(start);
		var firstPath = new NavigationPath(finish, initial);
		paths.add(firstPath);
		while (!paths.isEmpty()) {
			var path = paths.poll();
			if (path.getDistanceToTarget() <= ProjectConstants.SCAN_RANGE 
					&& path.getPathSteps().size() > 1) { // check that drone moved
				return path.getPathSteps().toArray(new Coordinate[path.getPathSteps().size()]);
			}
			ArrayList<NavigationPath> nextSteps;
			int stepsToTarget = (int) (path.getDistanceToTarget() / ProjectConstants.STEP_SIZE);
			if (stepsToTarget > 5) {
				var n = (18 / stepsToTarget) + 1; // the further away we are from target the less paths we explore
				nextSteps = expand(n, path);
			}
			else { // too small to care
				nextSteps = expand(path);
			}
			for (NavigationPath step : nextSteps) {
					paths.add(step);
			}
		}
		return null; // no path found to target
	}
	
	// take best few paths only
	public ArrayList<NavigationPath> expand(int toTake, NavigationPath path) {
		var nextSteps = expand(path);
		Collections.sort(nextSteps);
		var result = new ArrayList<NavigationPath>();
		for (int i = 0; i < toTake; i++) {
			result.add(nextSteps.get(i));
		}
		return result;
	}

	// generate all possible next steps
	public ArrayList<NavigationPath> expand(NavigationPath path) {
		var nextSteps = new ArrayList<NavigationPath>();
		for (int degree = 0; degree < 360; degree+= ProjectConstants.ROTATION_ANGLE) {
			Coordinate newCoord = path.last.getPointAt(ProjectConstants.STEP_SIZE, degree);
			if (LocationChecker.isValidPath(path.last, newCoord)) {
				@SuppressWarnings("unchecked")
				ArrayList<Coordinate> newPath = (ArrayList<Coordinate>) path.getPathSteps().clone();
				newPath.add(newCoord);
				nextSteps.add(new NavigationPath(path.target, newPath));
			}
		}
		return nextSteps;
	}
}
