package uk.ac.ed.inf.aqmaps;

public class DistanceCalculatorFactory {

	// returns the default calculator that other objects ask for, can be extended to be overloaded with enums if need arises
	public static DistanceCalculator getDefault(String calculatorType) {
		if (calculatorType == "optimalJourney") { 
			return new DroneMovementDistanceCalculator();
		}
		// add different cases if you want to use different distance calculation methods for different components
		
		return new EuclideanDistanceCalculator();
	}
}
