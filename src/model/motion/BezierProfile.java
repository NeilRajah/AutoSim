package model.motion;

import java.util.ArrayList;

import model.FieldPositioning;
import model.Point;
import util.PlotGenerator;
import util.Util;

public class BezierProfile extends DriveProfile {
	//Attributes
	private BezierPath path; //path to follow
	private double trackWidth; //width of the robot wheelbase in inches
	private double maxVel; //maximum velocity robot can reach in in/s
	private double maxAcc; //maximum acceleration robot can reach in in/s^2
	private double maxDec; //maximum deceleration robot can reach in in/s^2
	
	private final int SIZE = 500;; //number of pieces the path is split into
	private final double STEP = 1.0 / SIZE; //inverse of size
	
	private double totalLength; //arclength of the entire path
	private double[] tVals; //t values for the points evenly spaced along the path
	private Point[] evenPoints; //list of points in the path evenly spaced by distance
	
	private double[] centerRadius; //radius the center of the robot drives at in inches
	private double[] centerVel; //center velocity of the robot in inches per second
	
	/**
	 * Create a profile to follow a Bezier curve while respecting kinematic robot constraints
	 * @param controlPts Control points of the Bezier curve
	 * @param trackWidth Width of the robot wheelbase in inches
	 * @param maxVel Maximum reachable robot velocity in in/s
	 * @param maxAcc Maximum reachable robot acceleration in in/s^2
	 * @param maxDec Maximum reachable robot deceleration in in/s^2
	 */
	public BezierProfile(Point[] controlPts, double trackWidth, double maxVel, double maxAcc, double maxDec) {
		//set attributes
		path = new BezierPath(controlPts);
		this.trackWidth = trackWidth;
		this.maxVel = maxVel;
		this.maxAcc = maxAcc;
		this.maxDec = maxDec;
		
		//create profile
		computeConstants();
		fillProfiles();
	} //end constructor
	
	/**
	 * Create a profile to follow a Bezier curve while respecting kinematic robot constraints
	 * @param controlPts Control points of the Bezier curve
	 * @param trackWidth Width of the robot wheelbase in inches
	 * @param maxVel Maximum reachable robot velocity in in/s
	 * @param maxAcc Maximum reachable robot acceleration in in/s^2
	 * @param maxDec Maximum reachable robot deceleration in in/s^2
	 */
	public BezierProfile(double[][] controlPts, double trackWidth, double maxVel, double maxAcc, double maxDec) {
		this(FieldPositioning.pointsFromDoubles(controlPts), trackWidth, maxVel, maxAcc, maxDec);
	} //end constructor

	/**
	 * Compute the center path trajectory
	 */
	protected void computeConstants() {
		/*
		 * Create a list of points with even t value spacing. This is later used to split the
		 * curve up into segments of equal length.
		 */
		double[] tDistances = parameterizeByT();
		this.totalLength = tDistances[tDistances.length - 1]; //last distance is length of curve
		//t value is index/size, tDistances[index] is distance along path at that t value
		
		//parameterize by T test
		ArrayList<String> tDistanceData = new ArrayList<String>();
		for (int i = 0; i < tDistances.length; i++) {
			double a = i * STEP;
			double b = tDistances[i];
			double c = i == 0 ? 0 : tDistances[i] - tDistances[i-1];
			String line = String.format("%.3f %.3f %.3f%n", a, b, c);
			tDistanceData.add(line);
		} 
		Util.println("tDistanceData " + Util.saveListToFile(tDistanceData, "tDistanceData"));
		
		/*
		 * Create the evenly spaced points list by linearly interpolating between the points based on the
		 * distance along the curve.
		 */
		parameterizeByD(tDistances);
		
		//parameterize by d test
		ArrayList<String> dDistanceData = new ArrayList<String>();
		for (int i = 0; i < SIZE; i++) {
			double a = tVals[i];
			double b = i == 0 ? 0 : FieldPositioning.calcDistance(evenPoints[i], evenPoints[i-1]);
			dDistanceData.add(String.format("%.3f %.3f\n", a, b));
		}
		Util.println("dDistanceData " + Util.saveListToFile(dDistanceData, "dDistanceData"));
		
		/*
		 * Constrain the center velocity of the robot by the path's curvature. 
		 */
		applyCurvatureConstraint();
		
		//curvate constraint test
		ArrayList<String> curvatureData = new ArrayList<String>();
		for (int i = 0; i < centerVel.length; i++) {
			curvatureData.add(String.format("%.3f\n", centerVel[i]));
		} 
		Util.println("curvatureData " + Util.saveListToFile(curvatureData, "curvatureData"));
		
		/*
		 * Constrain the center velocity of the robot by the maximum acceleration with a forward pass
		 * of the center velocity list.
		 */
		applyAccelerationConstraint();
		
		//acceleration constraint test
		ArrayList<String> accelerationData = new ArrayList<String>();
		for (int i = 0; i < centerVel.length; i++) {
			accelerationData.add(String.format("%.3f\n", centerVel[i]));
		} 
		Util.println("accelerationData " + Util.saveListToFile(accelerationData, "accelerationData"));
		
		/*
		 * Constrain the center velocity of the robot by the maximum deceleration with a backward pass
		 * of the center velocity list.
		 */
		applyDecelerationConstraint();
		
		//deceleration constraint test
		ArrayList<String> decelerationData = new ArrayList<String>();
		for (int i = 0; i < centerVel.length; i++) {
			decelerationData.add(String.format("%.3f\n", centerVel[i]));
		} 
		Util.println("decelerationData " + Util.saveListToFile(decelerationData, "decelerationData"));
		
		//display charts
		PlotGenerator.displayChart(PlotGenerator.createChartFromList(1920, 1080, "decelerationData", 
									"i", "Velocity (ft/s)", decelerationData));
		PlotGenerator.displayChart(PlotGenerator.createChartFromList(1920, 1080, "accelerationData", 
									"i", "Velocity (ft/s)", accelerationData));
		PlotGenerator.displayChart(PlotGenerator.createChartFromList(1920, 1080, "curvatureData", 
									"i", "Velocity (ft/s)", curvatureData));
	} //end computeConstants
	
	/**
	 * Split the curve up with evenly spaced t values
	 * @return Distance along the curve at each t value
	 */
	private double[] parameterizeByT() {
		double t = 0; //parametric value
		double dist = 0; 
		double[] distances = new double[SIZE];
		
		distances[0] = 0; //start with zero distance
		
		for (int i = 1; i < SIZE; i++) {
			//cumulatively sum the distance
			dist += FieldPositioning.calcDistance(path.calcPoint(t), path.calcPoint(t - STEP));
			distances[i] = dist;
			
			t += 1.0 / SIZE;
		} //loop
		
		return distances;
	} //end parameterizeByT

	/**
	 * Parameterize the curve by distance values
	 * @param tDistances Distance values from t parameterization
	 * @return Array of t values parameterized by distance
	 */
	private void parameterizeByD(double[] tDistances) {
		double dist = 0;
		double distStep = this.totalLength / SIZE;
				
		//initialize the t values array
		tVals = new double[SIZE];
		tVals[0] = 0;
		
		//initialize the points array
		this.evenPoints = new Point[SIZE];
		this.evenPoints[0] = path.getControlPoints()[0];
		
		for (int i = 1; i < SIZE; i++) {
			dist += distStep; //increase the distance
			
			//index of the distance in tDistances just below dist (bottom of the sandwich)
			int k = Util.findSandwichedElements(tDistances, dist, 1E-3)[0];
			
			//linear interpolation
			double t2 = (k+1) * STEP;
			double t1 = k * STEP;
			double d2 = tDistances[k+1];
			double d1 = tDistances[k];
			
			//add the point at the interpolated t value to the curve
			tVals[i] = Util.interpolate(dist, t1, d1, t2, d2);
			evenPoints[i] = path.calcPoint(tVals[i]);
		} //loop
	} //end paramaterizeByD
	
	/**
	 * Apply the curvature constraint to the robot's center velocity
	 */
	private void applyCurvatureConstraint() {
		//initialize the center velocity list
		centerVel = new double[SIZE];
		
		//get the center path radii
		calcRadii();
		
		//fill the center velocities
		for (int i = 0; i < SIZE; i++) {
			centerVel[i] = (maxVel * centerRadius[i]) / (centerRadius[i] + trackWidth / 2);
		} //loop
	} //end applyCurvatureConstraint
	
	/**
	 * Calculate the center radius along the spline
	 */
	private void calcRadii() {
		/*
		 * Potentially refactor this to the calculus method
		 * dx, dy, ddx, ddy
		 * Use 1 / curvature formula
		 */
		centerRadius = new double[SIZE];
		
		for (int i = 0; i < SIZE; i++) {
			//radius is inverse of curvature
			centerRadius[i] = path.calcRadius(tVals[i]);
		} //loop		
	} //end calcRadii
	
	/**
	 * Constrain the center velocity with the maximum acceleration value
	 */
	private void applyAccelerationConstraint() {
		//distance between points
		double distStep = this.totalLength / SIZE;
		
		//start at initial speed of zero
		centerVel[0] = 0;
		
		//calculate the new center velocity at each point, skipping the first one
		for (int i = 1; i < centerVel.length; i++) {
			double velFromAcc = Math.sqrt(Math.pow(centerVel[i-1], 2) + 2 * maxAcc * distStep); //sqrt(v^2 + 2ad)
			centerVel[i] = Math.min(velFromAcc, centerVel[i]);
		} //loop
	} //end applyAccelerationConstraints
	
	/**
	 * Constrain the center velocity with the maximum deceleration value
	 */
	private void applyDecelerationConstraint() {
		//distance between points
		double distStep = this.totalLength / SIZE;
		
		//start at final speed of zero
		centerVel[centerVel.length - 1] = 0;
		
		//calculate the new center velocity looping backwards, skipping the first one
		for (int i = SIZE - 2; i >= 0; i--) {
			double velFromAcc = Math.sqrt(Math.pow(centerVel[i+1], 2) + 2 * maxDec * distStep); //sqrt(v^2 + 2ad)
			centerVel[i] = Math.min(velFromAcc, centerVel[i]);
		} //loop
	} //end applyDecelerationConstraint
		
	protected void fillProfiles() {
		/*
		 * TO-DO:
		 * Offset path
		 * Offset velocities
		 * Time
		 */
	} //end fillProfiles
} //end class