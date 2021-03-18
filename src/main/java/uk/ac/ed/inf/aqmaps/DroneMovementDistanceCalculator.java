package uk.ac.ed.inf.aqmaps;

public class DroneMovementDistanceCalculator extends DistanceCalculator {
	private NavigationStrategy navStrat;
	
	DroneMovementDistanceCalculator() {
		this.navStrat = new AStarNavigationStrategy();
	}
	
	@Override
	public Double getDistance(Coordinate p1, Coordinate p2) {
		return (double) this.navStrat.findPath(p1, p2).length;
	}
}