/**
 * QuinticBezierPath
 * Author: Neil Balaskandarajah
 * Created on: 16/02/2020
 * Path object for a bezier curve
 */

package model.motion;

import main.AutoSim;
import model.FieldPositioning;
import model.Point;
import util.Util;

public class QuinticBezierPath {
	//Constants
	public static final int RESOLUTION = 1000;
	private final double EPSILON = 5E-4;
	
	//Attributes
	//Configured
	private Point[] controlPts; //curve control points
	
	//Calculated
	private double length; //total length of the curve
	private int[][] polyline; //polyline used for graphics
	
	/*
	 * Paramaterize so each point has t, dist, heading, curvature/radius, and can lin int between them
	 */
	
	/**
	 * Create a quintic bezier path given an array of control points
	 * @param controlPts - (x,y) control points
	 */
	public QuinticBezierPath(Point[] controlPts) {
		//set attributes
		this.controlPts = controlPts;
		
		//calculate constants
		computeConstants();
	} //end constructor
	
	/**
	 * Create a quintic bezier path given an array of point coordinates
	 * @param controlPts - (x,y) control point coordinates in arrays
	 */
	public QuinticBezierPath(double[][] controlPts) {
		//set attributes
		this.controlPts = new Point[6];
		
		//fill array
		for (int i = 0; i <= 5; i++) {
			this.controlPts[i] = new Point(controlPts[i]);
		} //loop
		
		//calculate constants
		computeConstants();
	} //end constructor
	
	//Attributes
	
	/**
	 * Return the length of the path
	 * @return length - numerically calculated path length
	 */
	public double getLength() {
		return length;
	} //end getLength
	
	/**
	 * Compute constants of the path
	 */
	private void computeConstants() {
		computeLength();
		computePolyline();
	} //end computeConstants

	/**
	 * Compute the length of the path
	 */
	private void computeLength() {
		//loop through t between 0 and 1, += 1/res, calc distances between points
		length = 0;
		double t = 0;
		double stepSize = 1.0 / RESOLUTION;
		
		for (int i = 1; i < RESOLUTION; i++) {
			t += stepSize;
			length += FieldPositioning.calcDistance(calcPoint(t), calcPoint(t - stepSize));
		} //loop
	} //end computeLength
	
	/**
	 * Calculate the (x,y) point value for a given t
	 * @param t - t value of the curve
	 * return - (x,y) point value for t
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
	 * Calculate the heading at the curve of a given t value
	 * @param t - t value of the curve
	 * @return - heading at t in degrees
	 */
	public double calcHeading(double t) {
		if (t <= EPSILON) {
			return FieldPositioning.calcGoalYaw(calcPoint(EPSILON), calcPoint(0));
		} else if (t >= 1 - EPSILON) {
			return FieldPositioning.calcGoalYaw(calcPoint(1 - EPSILON), calcPoint(1));
		} //if
		return FieldPositioning.calcGoalYaw(calcPoint(t - EPSILON), calcPoint(t + EPSILON));
	} //end getHeading
	
	public double calcCurvature(double t) {
		t = t <= EPSILON ? EPSILON : t >= (1 - EPSILON) ? 1 - EPSILON : t;
		Point p1 = calcPoint(t - EPSILON);
		Point p2 = calcPoint(t + EPSILON);
		
		return Math.toRadians(calcHeading(t)) / 
				(FieldPositioning.calcDistance(p1, p2));
	}
	
	/**
	 * Computes the set of (x,y) points for drawing the curve later
	 */
	private void computePolyline() {
		int[] x = new int[RESOLUTION];
		int[] y = new int[RESOLUTION];
		
		double t = 0;
		
		for (int i = 0; i < RESOLUTION; i++) {
			Point p = calcPoint(t);
			
			//flip x and y because of field config
			x[i] = (int) (p.getY() * (double) AutoSim.PPI); 
			y[i] = (int) (p.getX() * (double) AutoSim.PPI);
			
			t += 1.0 / RESOLUTION;
		} //loop
		
		polyline = new int[][]{x, y};
	} //end computePolyline
	
	/**
	 * Get the polyline for animation purposes
	 * return - x points and y points of the curve
	 */
	public int[][] getPolyline() {
		return polyline;
	} //end getPolyline
	
	/**
	 * Create an array of Points from a 2D array of points
	 * @param controlPts - 2D array of points containing (x,y) of control points
	 * @return control points as Point objects
	 */
	public static Point[] pointsFromDoubles(double[][] controlPts) {
		//turn double[][] to point array 
		Point[] curvePts = new Point[6];
		for (int i = 0; i <= 5; i++) {
			curvePts[i] = new Point(controlPts[i]);
		} //loop
		
		return curvePts;
	} //end pointsFromDoubles
} //end class
