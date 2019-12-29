/**
 * Point
 * Author: Neil
 * Created on: 28/12/2019
 * Class to define the (x,y) positions on the field
 */
package model;

public class Point {
	//Attributes
	//Configured
	private double x, y; //x and y position of the point
	
	/**
	 * Create an (x,y) point
	 * double x - x-value of the point
	 * double y - y-value of the point
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	} //end constructor

	/**
	 * Get the x value of the point
	 * return x - x value of the point
	 */
	public double getX() {
		return x;
	} //end getX
	
	/**
	 * Set the x value of the point
	 * double x - new x value for the point
	 */
	public void setX(double x) {
		this.x = x;
	} //end setX
	
	/**
	 * Get the y value of the point
	 * return y - y value of the point
	 */
	public double getY() {
		return y;
	} //end getY
	
	/**
	 * Set the y value of the point
	 * double y - new y value for the point
	 */
	public void setY(double y) {
		this.y = y;
	} //end setY
	
	/**
	 * Calculate the distance between two points using the Pythagorean theorem
	 * Point p1 - first point
	 * Point p2 - second point
	 * return - absolute distance between the two points
	 */
	public static double calcDistance(Point p1, Point p2) {
		return Math.hypot(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	} //return calcDistance
	
	/**
	 * Calculate the angle between two points in radians from the x-axis
	 * Point p1 - first point
	 * Point p2 - second point
	 * return - angle between the two point in radians from the x-axis
	 */
	public static double calcAngleRad(Point p1, Point p2) {
		double deltaX = p2.getX() - p1.getX();
		double deltaY = p2.getY() - p1.getY();
		double theta = 0;
		
		if (deltaX == 0) {
			if (deltaY > 0)
				theta = 0;
			else if (deltaY < 0)
				theta = 180;
		} else if (deltaY == 0) {
			if (deltaX > 0) 
				theta = 90;
			else if (deltaX < 0)
				theta = -90;
		} else {
			theta = Math.atan(deltaY/deltaX); 
		} //if
		
		return theta;
	} //end calcAngleRad
	
	/**
	 * Calculate the angle between two points in degrees from the x-axis
	 * Point p1 - first point
	 * Point p2 - second point
	 * return - angle between the two point in degrees from the x-axis
	 */
	public static double calcAngleDeg(Point p1, Point p2) {
		return Math.toDegrees(calcAngleRad(p1, p2));
	} //end calcAngleDeg
	
	/**
	 * Check if two points are close enough to another within a range
	 * Point target - target point to be close to
	 * Point current - point to check closeness of
	 * double range - how close current point can be to target point
	 */
	public static boolean isWithinBounds(Point target, Point current, double range) {
		double xDiff = Math.abs(target.getX() - current.getX());
		double yDiff = Math.abs(target.getY() - current.getY());
		
		if (xDiff < range && yDiff < range) {
			return true;
		} else {
			return false;
		} //if
	} //end isWithinBounds
} //end Point