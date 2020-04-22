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
	private double trackWidth; //half the width of the robot for path ofsetting
	
	//Calculated
	private double length; //total length of the curve
	private int[][] polyline; //polyline used for graphics
	private int[][] leftPoly; //polyline of left path
	private int[][] rightPoly; //polyline of right path
	private int numSegments; //number of segments curve is split up into
	private double[] headings; //headings at each point
	
	/*
	 * Paramaterize so each point has t, dist, heading, curvature/radius, and can lin int between them
	 */
	
	/**
	 * Create a quintic bezier path given an array of control points
	 * @param controlPts (x,y) control points
	 * @param numSegments Number of segments the curve is split up into
	 * @param trackWidth Half the width of robot for path ofsetting (inches)
	 */
	public BezierPath(Point[] controlPts, int numSegments, double trackWidth) {
		//set attributes
		this.controlPts = controlPts;
		this.numSegments = numSegments;
		this.trackWidth = trackWidth;
		
		//calculate constants
		computeConstants();
	} //end constructor
	
	/**
	 * Create a quintic bezier path given an array of point coordinates
	 * @param controlPts (x,y) control point coordinates in arrays
	 * @param numSegments Number of segments the curve is split up into 
	 * @param trackWidth Half the width of robot for path ofsetting (inches)
	 */
	public BezierPath(double[][] controlPts, int numSegments, double trackWidth) {
		//set attributes
		this.controlPts = new Point[6];
		this.numSegments = numSegments;
		this.trackWidth = trackWidth;
		
		//fill array
		for (int i = 0; i < controlPts.length; i++) {
			this.controlPts[i] = new Point(controlPts[i]);
		} //loop
		
		//calculate constants
		computeConstants();
	} //end constructor
	
	/**
	 * Default constructor that creates a blank curve
	 * @param numSegments Number of segments the curve is split up into 
	 * @param trackWidth Half the width of robot for path ofsetting (inches)
	 */
	public BezierPath(int numSegments, double trackWidth) {
		//set attributes
		this.controlPts = new Point[6];
		this.numSegments = numSegments;
		this.trackWidth = trackWidth;
		
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
		fillHeadings();
		computePolylines();
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
	private void computePolylines() {
		int[] x = new int[numSegments];
		int[] y = new int[numSegments];
		int[] xL = new int[numSegments];
		int[] yL = new int[numSegments];
		int[] xR = new int[numSegments];
		int[] yR = new int[numSegments];
		
		double t = 0;
		double r = trackWidth;
		
		for (int i = 0; i < numSegments; i++) {
			Point p = calcPoint(t);
			
			//flip x and y because of field config
			x[i] = (int) (p.getY() * (double) AutoSim.PPI); 
			y[i] = (int) (p.getX() * (double) AutoSim.PPI);
						
			//calc left and right sides
			double thetaL, thetaR; //angle offsets for left and right
			thetaL = headings[i] + 90;
			thetaR = headings[i] - 90;
			
			if (i == 0) { //swap values for first point
				double buffer = thetaL;
				thetaL = thetaR;
				thetaR = buffer;
			} //if
			
			//left side
			xL[i] = (int) (x[i] + r * Math.cos(Math.toRadians(thetaL)));
			yL[i] = (int) (y[i] + r * Math.sin(Math.toRadians(thetaL)));

			//right side
			xR[i] = (int) (x[i] + r * Math.cos(Math.toRadians(thetaR)));
			yR[i] = (int) (y[i] + r * Math.sin(Math.toRadians(thetaR)));		
			
			t += 1.0 / numSegments;
		} //loop
		
		polyline = new int[][]{x, y};
		leftPoly = new int[][] {xL, yL};
		rightPoly = new int[][] {xR, yR};
	} //end computePolyline
	
	/**
	 * Get the polyline for animation purposes
	 * @return X points and Y points of the curve
	 */
	public int[][] getPolyline() {
		return polyline;
	} //end getPolyline
	
	/**
	 * Get the left polyline for animation purposes
	 * @return (x,y) points for the left side curve
	 */
	public int[][] getLeftPolyline() {
		return leftPoly;
	} //end getLeftPolyline
	
	/**
	 * Get the right polyline for animation purposes
	 * @return (x,y) points for the right side curve
	 */
	public int[][] getRightPolyline() {
		return rightPoly;
	} //end getRightPolyline
	
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
	
	/**
	 * Fill all the headings into an array
	 */
	
	private void fillHeadings() {
		headings = new double[numSegments];
		double step = 1.0 / numSegments;
		
		for (int i = 0; i < headings.length; i++) {
			double t = i * step;
			headings[i] = calcHeading(t);
		} //loop
	} //end fillHeadings
	
	/**
	 * Get the heading at a t value from the array
	 * @param t Parametric t value from 0 to 1 inclusive 
	 * @return Heading at that t
	 */
	public double getHeading(double t) {
		return headings[Math.min(Math.max((int) (t * numSegments), 0), 1)];
	} //end getHeading
	
	/**
	 * Get the heading at an index i in the array
	 * @param i Index in the headings array
	 * @return Heading at index i
	 */
	public double getHeading(int i) {
		return headings[Math.min(Math.max(i, 0), 1)];
	} //end getHeading
} //end class
