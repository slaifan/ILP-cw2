package uk.ac.ed.inf.aqmaps;



public class TwoOptJourneyStrategy implements JourneyStrategy {
	private static final int ITERATION_LIMIT = 800;
	DistanceCalculator distanceCalculator = DistanceCalculatorFactory.getDefault("journey");	
	
	// adapted from:
	// https://www.technical-recipes.com/2017/applying-the-2-opt-algorithm-to-traveling-salesman-problems-in-java/
	public Coordinate[] planJourney(Coordinate[] tour) {
	    int improve = 0;
	    Coordinate[] newTour = tour.clone();
//	    System.out.println("starting distance = " + distanceCalculator.getTourDistance(tour));
	    while (improve < ITERATION_LIMIT)
	    {
	        double best_distance = distanceCalculator.getTourDistance(tour);
	 
	        for (int i = 1; i < tour.length - 1; i++ ) 
	        {
	            for (int k = i + 1; k < tour.length; k++) 
	            {
	                TwoOptSwap( i, k, tour, newTour);
	                double new_distance = distanceCalculator.getTourDistance(newTour);
	 
	                if ( new_distance < best_distance ) 
	                {
	                    // Improvement found so reset
	                    improve = 0;
	                                                 
	                    tour = newTour.clone();
	                         
	                    best_distance = new_distance;
//	                    System.out.println("new distance found " + best_distance + " by swapping " + i + " and " + k + " th elements");
	                }
	            }
	        }
	        improve ++;
	    }
	    return tour;
	}
		
	private void TwoOptSwap(int i, int k, Coordinate[] tour,  Coordinate[] newTour) {
		for ( int c = 0; c <= i - 1; ++c ) {
	        newTour[c] = tour[c];
	    }
	    // take route[i] to route[k] and add them in reverse order to new_route
	    int dec = 0;
	    for ( int c = i; c <= k; ++c ) {
	        newTour[c] = tour[k - dec];
	        dec++;
	    }
	    for ( int c = k + 1; c < tour.length; ++c ) {
	    	newTour[c] = tour[c];
	    }
	}

}
