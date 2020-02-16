/**
 * QuinticBezierPath
 * Author: Neil Balaskandarajah
 * Created on: 16/02/2020
 * Path object for a bezier curve
 */

package model.motion;

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
		this.controlPts = new Point[5];
		
		//fill array
		for (int i = 0; i < 5; i++) {
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
		for (int i = 0; i < 5; i++) {
			sum += Util.FIVENOMIAL_CONSTANTS[i] * Math.pow(1 - t, 5 - i) * Math.pow(t, i);
			sumX += controlPts[i].getX() * sum;
			sumY += controlPts[i].getY() * sum;
		} //loop
		
		return new Point(sumX, sumY);
	} //end calcPoint
	
	/**
	 * Calculate the heading at the curve of a given t value
	 * @param t - t value of the curve
	 * return - heading at t
	 */
	public double calcHeading(double t) {
		if (t <= EPSILON) {
			return FieldPositioning.calcAngleRad(calcPoint(EPSILON), calcPoint(0));
		} else if (t >= 1 - EPSILON) {
			return FieldPositioning.calcAngleRad(calcPoint(1), calcPoint(1 - EPSILON));
		} //if
		return FieldPositioning.calcAngleRad(calcPoint(t + EPSILON), calcPoint(t - EPSILON));
	} //end getHeading
	
	/**
	 * Get the polyline for animation purposes
	 * return - x points and y points of the curve
	 */
	public int[][] getPolyline() {
		int[] x = new int[RESOLUTION];
		int[] y = new int[RESOLUTION];
		
		double t = 0;
		
		for (int i = 0; i < RESOLUTION; i++) {
			Point p = calcPoint(t);
			
			x[i] = (int) p.getX();
			y[i] = (int) p.getY();
			
			t += 1.0 / RESOLUTION;
		} //loop
		
		return new int[][]{x, y};
	} //end getPolyline
} //end class
