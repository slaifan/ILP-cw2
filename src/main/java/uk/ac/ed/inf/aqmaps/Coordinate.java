package uk.ac.ed.inf.aqmaps;

import java.util.Objects;

import com.mapbox.geojson.Point;

class Coordinate {
	private Double lng;
	private Double lat;
	
	Coordinate(Double lng, Double lat) {
		this.lng = lng;
		this.lat = lat;
	}
	
	public Double getLng() {
		return this.lng;
	}
	
	public Double getLat() {
		return this.lat;
	}
	
	// point that is at the specified distance and degree from object
	public Coordinate getPointAt(double radius, int degree) {
		var radianDegree = Math.toRadians(degree);
		
		double newX = radius * Math.cos(radianDegree);
		double newY = radius * Math.sin(radianDegree);
		
		var newLng = this.lng + newX;
		var newLat = this.lat + newY;
		
		return new Coordinate(newLng, newLat);
	}
	
	// angle between two points
	public double getAngle(Coordinate target) {
	    var angle = Math.toDegrees(Math.atan2(target.lat - lat, target.lng - lng));

	    if(angle < 0){
	        angle += 360;
	    }

	    return angle;
	}
	
	public String toString() {
		return "(" + this.lng + ", " + this.lat + ")";
	}
	
	public Point toMapBox() {
		return Point.fromLngLat(this.lng, this.lat);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.lng, this.lat);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Coordinate))
			return false;
		Coordinate other = (Coordinate) obj;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lng == null) {
			if (other.lng != null)
				return false;
		} else if (!lng.equals(other.lng))
			return false;
		return true;
	}
}
