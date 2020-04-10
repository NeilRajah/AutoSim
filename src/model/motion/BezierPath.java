/**
 * BezierPath
 * Author: Neil Balaskandarajah
 * Created on: 16/02/2020
 * Path object for a bezier curve
 */

package model.motion;

import main.AutoSim;
import model.FieldPositioning;
import model.Point;
import util.Util;

public class BezierPath {
	//Constants
	public static final int HIGH_RES = 1000;
	public static final int FAST_RES = 100;
	private final double EPSILON = 5E-4;
	
	//Attributes
	//Configured
	private Point[] controlPts; //curve control points
	
	//Calculated
	private double length; //total length of the curve
	private int[][] polyline; //polyline used for graphics
	private int numSegments; //number of segments curve is split up into
	
	/*
	 * Paramaterize so each point has t, dist, heading, curvature/radius, and can lin int between them
	 */
	
	/**
	 * Create a quintic bezier path given an array of control points
	 * @param controlPts - (x,y) control points
	 * @param numSegments Number of segments the curve is split up into
	 */
	public BezierPath(Point[] controlPts, int numSegments) {
		//set attributes
		this.controlPts = controlPts;
		this.numSegments = numSegments;
		
		//calculate constants
		computeConstants();
	} //end constructor
	
	/**
	 * Create a quintic bezier path given an array of point coordinates
	 * @param controlPts (x,y) control point coordinates in arrays
	 * @param numSegments Number of segments the curve is split up into 
	 */
	public BezierPath(double[][] controlPts, int numSegments) {
		//set attributes
		this.controlPts = new Point[6];
		this.numSegments = numSegments;
		
		//fill array
		for (int i = 0; i < controlPts.length; i++) {
			this.controlPts[i] = new Point(controlPts[i]);
		} //loop
		
		//calculate constants
		computeConstants();
	} //end constructor
	
	/**
	 * Default constructor that creates a blank curve
	 */
	public BezierPath(int numSegments) {
		//set attributes
		this.controlPts = new Point[6];
		this.numSegments = numSegments;
		
		//fill array with blank points
		for (int i = 0; i < controlPts.length; i++) {
			this.controlPts[i] = new Point(0, 0);
		} //loop
		
		//calculate the constants
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
	 * Update the curve 
	 */
	public void update() {
		computeConstants();
	} //end update
	
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
		
		update();
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
		
		//update the curve
		update();
	} //end setCoordinate
	
	/**
	 * Compute the length of the path
	 */
	private void computeLength() {
		//loop through t between 0 and 1, += 1/res, calc distances between points
		length = 0;
		double t = 0;
		double stepSize = 1.0 / numSegments;
		
		for (int i = 1; i < numSegments; i++) {
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
	
	/**
	 * Calculate the curvature at a given t
	 * @param t t value to calculate the curvature
	 * @return curvature at the t value
	 */
	public double calcCurvature(double t) {
		t = t <= EPSILON ? EPSILON : t >= (1 - EPSILON) ? 1 - EPSILON : t;
		Point p1 = calcPoint(t - EPSILON);
		Point p2 = calcPoint(t + EPSILON);
		
		return Math.toRadians(calcHeading(t)) / 
				(FieldPositioning.calcDistance(p1, p2));
	} //end calcCurvature
	
	/**
	 * Computes the set of (x,y) points for drawing the curve later
	 */
	private void computePolyline() {
		int[] x = new int[numSegments];
		int[] y = new int[numSegments];
		
		double t = 0;
		
		for (int i = 0; i < numSegments; i++) {
			Point p = calcPoint(t);
			
			//flip x and y because of field config
			x[i] = (int) (p.getY() * (double) AutoSim.PPI); 
			y[i] = (int) (p.getX() * (double) AutoSim.PPI);
			
			t += 1.0 / numSegments;
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
} //end class