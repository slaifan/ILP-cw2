package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;

public class NavigationPath implements Comparable<NavigationPath> {
	private ArrayList<Coordinate> steps;
	private DistanceCalculator distanceCalculator = DistanceCalculatorFactory.getDefault("heuristic");
	Coordinate last; // to speed up getting heuristic cost
	Coordinate target;
	
	public NavigationPath(Coordinate target, ArrayList<Coordinate> coords) {
		this.target = target;
		this.steps = coords;
		this.last = coords.get(coords.size() - 1);
	}
	
	public NavigationPath(Coordinate target) {
		this.target = target;
		this.steps = new ArrayList<>();
	}
	
	public ArrayList<Coordinate> getPathSteps() {
		return steps;
	}

	public void add(Coordinate coord) {
		this.getPathSteps().add(coord);
		this.last = coord;
	}
	
	public Double getPathCost() {
		Double realCost = 0.0;
		if (steps.size() > 0) {
			realCost = (double) steps.size() * ProjectConstants.STEP_SIZE;
		}
		return realCost + getDistanceToTarget(); // f(x) + h(x)
	}
	
	public Double getDistanceToTarget() {
		return distanceCalculator.getDistance(this.last, this.target); // h(x) heuristic calculation to target
	}
	
	
	public String toString() {
		StringBuilder bldr = new StringBuilder();
		for (Coordinate coordinate : this.getPathSteps()) {
			bldr.append(coordinate.toString());
			bldr.append(" -> ");
		}
		return bldr.toString();
	}

	@Override
	public int compareTo(NavigationPath otherPath) {
		var thisPathLength = this.getPathCost();
		var otherPathLength = ((NavigationPath)otherPath).getPathCost() ;
		if (thisPathLength == otherPathLength)
			return 0;
        return thisPathLength - otherPathLength < 0 ? (-1) : 1;

	}
}