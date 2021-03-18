package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;

public interface NavigationStrategy {

	Coordinate[] findPath(Coordinate start, Coordinate finish);
	ArrayList<NavigationPath> expand(NavigationPath path);
}
