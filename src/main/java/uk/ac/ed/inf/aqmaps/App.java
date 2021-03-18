package uk.ac.ed.inf.aqmaps;

/**
 * The main class of the program.
 *
 */
public class App 
{
	private static Coordinate start;
	
    public static void main(String[] args)
    {
        System.out.println("Program start");
        
        setup(args);
        
        NavigationStrategy navStrat = new AStarNavigationStrategy();
        JourneyStrategy journeyStrat = new TwoOptJourneyStrategy();
        
//      generateFiles(journeyStrat, navStrat); // kept in case you need to rerun
        
        if (args.length > 2) {
		    var day = Integer.parseInt(args[0]);
		    var month = Integer.parseInt(args[1]);
		    var year = Integer.parseInt(args[2]);
		    
		    var journey = new Journey(day, month, year, journeyStrat, navStrat, start);
			OutputWriter.writeFlightPathFile(journey);
			OutputWriter.writeReadingsFile(journey);
			System.out.println(journey.getExecutionTime());
        }
        else {
        	System.out.println("not enough arguments provided, please enter day, month, and year");
        }

//      recordPerformance(journeyStrat, navStrat); // curious about performance on your machine? ;-)
        
 
        System.out.println("ended");
    }
	
    // initial setup of project parameters
	private static void setup(String[] args) {
		if (args.length < 7) { // no arguments given so use default
			System.out.println("Not enough arguments provided, using default values for port and starting coordinates...");
			
	        WebServerConnection.setPort(80);
	        
	        start = new Coordinate(-3.188396, 55.944425);
//	        start = new Coordinate(-3.1878, 55.9444);
		}
		else {
			var port = Integer.parseInt(args[6]);
			WebServerConnection.setPort(port);
			
			var lng = Double.parseDouble(args[4]);
			var lat = Double.parseDouble(args[3]);
			start = new Coordinate(lng, lat);
		}
		
		LocationChecker.setup();
	}
	
	// record the performance of the chosen algorithms on all dates
	@SuppressWarnings("unused")
	private static void recordPerformance(JourneyStrategy journeyStrat, NavigationStrategy navStrat) {
	    var results = new StringBuilder();
	    results.append("\"date\",\"number of steps\",\"execution time\"\n");
	    for (int year = 2020; year < 2022; year++) {
			for (int month = 1; month < 13; month++) {
				for (int day = 1; day < 32; day++) {
					try {
						System.out.println(String.format("calculating journey %s-%s-%s", year, month, day));
						var journey = new Journey(day, month, year, journeyStrat, navStrat, start);
						var steps = journey.getJourneyLength();
						var time = journey.getExecutionTime();
						var result = String.format("\"%s-%s-%s\",\"%s\",\"%s\"\n", year, month, day, steps, time);
						System.out.println(result);
						results.append(result);
					} catch (NullPointerException e) {
						System.out.println(String.format("skipping %s-%s-%s", year, month, day));
					}
				}
			}
		}
      var performance = results.toString();
      OutputWriter.write(performance, "performance.csv");  
	}

	// generate the 24 output files
	@SuppressWarnings("unused")
	private static void generateFiles(JourneyStrategy journeyStrat, NavigationStrategy navStrat) {
			for (int month = 1; month < 13; month++) {
				System.out.println(String.format("calculating journey %s-%s-%s", 2020, month, month));
				var journey = new Journey(month, month, 2020, journeyStrat, navStrat, start);
				OutputWriter.writeFlightPathFile(journey);
				OutputWriter.writeReadingsFile(journey);
		}
	}
}
