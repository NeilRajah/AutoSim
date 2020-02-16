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
	private final int RESOLUTION = 100;
	
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
} //end class
