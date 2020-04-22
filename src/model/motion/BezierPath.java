/**
 * BezierPath
 * Author: Neil Balaskandarajah
 * Created on: 16/02/2020
 * Path object for a bezier curve
 */

package model.motion;

import model.FieldPositioning;
import model.Point;
import util.FieldPoints;
import util.Util;

public class BezierPath {
	//Constants
	public static final int HIGH_RES = 1000;
	public static final int FAST_RES = 100;
	private final double EPSILON = 5E-4;
	
	//Attributes
	//Configured
	private Point[] controlPts; //curve control points
	protected double trackWidth; //half the width of the robot for path ofsetting
	
	//Calculated
	protected int numSegments; //number of segments curve is split up into
	
	/*
	 * Paramaterize so each point has t, dist, heading, curvature/radius, and can lin int between them
	 */
	
	/**
	 * Create a quintic bezier path given an array of control points
	 * @param controlPts (x,y) control points
	 * @param numSegments Number of segments the curve is split up into
	 * @param trackWidth Half the width of robot for path ofsetting (inches)
	 */
	public BezierPath(Point[] controlPts) {
		//set attributes
		this.controlPts = controlPts;
	} //end constructor
	
	/**
	 * Create a quintic bezier path given an array of point coordinates
	 * @param controlPts (x,y) control point coordinates in arrays
	 * @param numSegments Number of segments the curve is split up into 
	 * @param trackWidth Half the width of robot for path ofsetting (inches)
	 */
	public BezierPath(double[][] controlPts) {
		this(FieldPositioning.pointsFromDoubles(controlPts));
	} //end constructor
	
	/**
	 * Default constructor that creates a blank curve
	 * @param numSegments Number of segments the curve is split up into 
	 * @param trackWidth Half the width of robot for path ofsetting (inches)
	 */
	public BezierPath() {
		this(FieldPoints.empty);
	} //end constructor
	
	//Attributes
	
	/**
	 * Get the control points
	 * @return Control points for the curve
	 */
	public Point[] getControlPoints() {
		return controlPts;
	} //end getControlPoints
	
	/**
	 * Set the points and update the curve
	 * @param points
	 */
	public void setControlPoints(Point[] points) {
		this.controlPts = points;
	} //end setControlPoints
	
	/**
	 * Set the x or y value for one point
	 * @param key Key designating the value to be changed
	 * @param value Value to update
	 */
	public void setCoordinate(String key, double value) {
		//update the value of the point
		int pointIndex = Integer.parseInt(key.substring(1));
		if (key.charAt(0) == 'x') {
			this.controlPts[pointIndex].setX(value); //char at index 1 is index in point array
		} else if (key.charAt(0) == 'y') {
			this.controlPts[pointIndex].setY(value);
		} //if
	} //end setCoordinate
		
	/**
	 * Calculate the (x,y) point value for a given t
	 * @param t Parametric t value of the curve from 0 to 1 inclusive
	 * @return (x,y) point value for t
	 */
	public Point calcPoint(double t) {
		//running sums
		double sum = 0, sumX = 0, sumY = 0;
		
		//loop through and get contribution from each control point
		for (int i = 0; i <= 5; i++) {
			sum = Util.FIVENOMIAL_CONSTANTS[i] * Math.pow(1 - t, 5 - i) * Math.pow(t, i);
			sumX += controlPts[i].getX() * sum;
			sumY += controlPts[i].getY() * sum;
		} //loop
		
		return new Point(sumX, sumY);
	} //end calcPoint
	
	
	/**
	 * Calculate the curvature at a given t
	 * @param t Parametric t value to calculate the curvature
	 * @return curvature at the t value
	 */
	public double calcCurvature(double t) {
		t = t <= EPSILON ? EPSILON : t >= (1 - EPSILON) ? 1 - EPSILON : t;
		Point p1 = calcPoint(t - EPSILON);
		Point p2 = calcPoint(t + EPSILON);
		
		return Math.toRadians(calcHeading(t)) / 
				(FieldPositioning.calcDistance(p1, p2));
	} //end calcCurvature
	
	//Heading
	
	/**
	 * Calculate the heading at the curve of a given t value
	 * @param t Parametric t value of the curve
	 * @return Heading at t in degrees
	 */
	public double calcHeading(double t) {
		if (t <= EPSILON) {
			return FieldPositioning.calcGoalYaw(calcPoint(EPSILON), calcPoint(0));
		} else if (t >= 1 - EPSILON) {
			return FieldPositioning.calcGoalYaw(calcPoint(1 - EPSILON), calcPoint(1));
		} //if
		return FieldPositioning.calcGoalYaw(calcPoint(t - EPSILON), calcPoint(t + EPSILON));
	} //end getHeading
} //end class
