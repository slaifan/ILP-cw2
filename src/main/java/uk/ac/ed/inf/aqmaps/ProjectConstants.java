package uk.ac.ed.inf.aqmaps;

public class ProjectConstants {
	public static final double STEP_SIZE = 0.0003; // movement scale
	public static final double SCAN_RANGE = 0.0002; // in movement degrees
	public static final int MAX_STEPS = 150; // for drone
	public static final double ROTATION_ANGLE = 10; // in angular degrees
	static final double[] BOUNDARIES = {55.946233, 55.942617, (-3.192473), (-3.184319)}; // TOP, BOTTOM, LEFT, RIGHT
	public static final String MARKER_SIZE = "medium"; // for GeoJSON representation
}
