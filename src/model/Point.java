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
	private double x; //x position of the point
	private double y; //y position of the point
	
	/**
	 * Create an (x,y) point
	 * @param x - x-value of the point
	 * @param y - y-value of the point
	 */
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	} //end constructor
	
	/**
	 * Create an (x,y) point
	 * @param xy - (x,y) coordinates for the point
	 */
	public Point(double[] xy) {
		this.x = xy[0];
		this.y = xy[1];
	} //end constructor

	/**
	 * Get the x value of the point
	 * @return x - x value of the point
	 */
	public double getX() {
		return x;
	} //end getX
	
	/**
	 * Set the x value of the point
	 * @param x - new x value for the point
	 */
	public void setX(double x) {
		this.x = x;
	} //end setX
	
	/**
	 * Get the y value of the point
	 * @return y - y value of the point
	 */
	public double getY() {
		return y;
	} //end getY
	
	/**
	 * Set the y value of the point
	 * @param y - new y value for the point
	 */
	public void setY(double y) {
		this.y = y;
	} //end setY
	
	/**
	 * Get the x and y values in (x,y) format
	 * @return string representation of the point
	 */
	public String getString() {
		return String.format("(%.3f,%.3f)", x, y);
	} //end getString
	
	/**
	 * Translate the point a given magnitude at a given angle
	 * @param magnitude Distance to translate the point 
	 * @param angle Angle to translate the point at in radians
	 */
	public void translate(double magnitude, double angle) {
		//sin and cos flipped because x and y of field are flipped
		this.x += magnitude * Math.sin(angle); 
		this.y += magnitude * Math.cos(angle);
	} //end translate
	
	/**
	 * Safe copy of this point
	 * @return Clone of this point
	 */
	public Point clone() {
		return new Point(x, y);
	} //end clone
} //end Point