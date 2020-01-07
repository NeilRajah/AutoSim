/**
 * Pose
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Pose of the robot for animation
 */
package model;

import java.awt.Color;

public class Pose {
	//Attributes
	//Configured
	private Point point; //coordinates of robot
	private double heading; //heading of robot
	private Color color; //robot color

	/**
	 * Create a pose instance with a point, heading and color
	 * @param point - (x,y) of robot
	 * @param heading - heading of robot in radians
	 * @param color - RGB color of robot
	 */
	public Pose(Point point, double heading, Color color) {
		this.point = point;
		this.heading = heading;
		this.color = color;
	} //end constructor
	
	/**
	 * Get the coordinates of the robot
	 * @return - (x,y) position of robot
	 */
	public Point getPoint() {
		return new Point(point.getX(), point.getY());
	} //end getPoint
	
	/**
	 * Get the heading of the robot
	 * @return heading - heading of robot in radians
	 */
	public double getHeading() {
		return heading;
	} //end getHeading
	
	/**
	 * Get the color of the robot
	 * @return color - RGB color of robot
	 */
	public Color getColor() {
		return color;
	} //end getColor
	
	/**
	 * Set the color of the pose
	 * @param clr - color of the pose
	 */
	public void setColor(Color clr) {
		this.color = clr;
	} //end setColor
} //end class
