package model.motion;

import java.util.ArrayList;
import java.util.Calendar;

import org.knowm.xchart.XYChart;

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
	
	private double totalLength; //arclength of the entire path in inches
	private double[] tVals; //t values for the points evenly spaced along the path
	private Point[] evenPoints; //list of points in the path evenly spaced by distance
	
	private double[] centerRadius; //radius the center of the robot drives at in inches
	private double[] centerVel; //center velocity of the robot in inches per second
	
	private double[] headings; //headings at the evenly spaced points in the path in degrees
	private double[] leftRadius; //radius of left side of path in inches
	private double[] rightRadius; //radius of right side of path in inches
	
	private double[] times; //time at each point in curve
	private double[] leftVel; //left wheel velocities in inches per second
	private double[] rightVel; //right wheel velocities in inches per second
	private double[] leftPos; //left wheel position in inches
	private double[] rightPos; //right wheel position in inches
	private double[] leftAcc; //left wheel acceleration in in/s^2
	private double[] rightAcc; //right wheel acceleration in in/s^2
	
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
		this.size = SIZE;
		
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
		
		/*
		 * Create the evenly spaced points list by linearly interpolating between the points based on the
		 * distance along the curve. The curve radius (inverse of curvature) and heading (degrees) is
		 * also calculated at this point.
		 */
		parameterizeByD(tDistances);
		calcRadii();
		calcHeadings();
		
		/*
		 * Constrain the center velocity of the robot by the path's curvature. 
		 */
		applyCurvatureConstraint();
		
		/*
		 * Constrain the center velocity of the robot by the maximum acceleration with a forward pass
		 * of the center velocity list.
		 */
		applyAccelerationConstraint();
		
		/*
		 * Constrain the center velocity of the robot by the maximum deceleration with a backward pass
		 * of the center velocity list.
		 */
		applyDecelerationConstraint();
		/*
		//deceleration constraint test
		ArrayList<String> decelerationData = new ArrayList<String>();
		for (int i = 0; i < centerVel.length; i++) {
			decelerationData.add(String.format("%.3f\n", centerVel[i]));
		} 
		Util.println("decelerationData " + Util.saveListToFile(decelerationData, "decelerationData"));
		
		//display charts
		PlotGenerator.displayChart(PlotGenerator.createChartFromList(1920, 1080, "decelerationData", 
									"i", "Velocity (ft/s)", decelerationData));
		*/
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
		
		for (int i = 0; i < centerRadius.length; i++) {
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
			centerVel[i] = Math.min(velFromAcc, centerVel[i]); //minimum of this new constraint and old value
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
			centerVel[i] = Math.min(velFromAcc, centerVel[i]); //minimum of this new constraint and old value
		} //loop
	} //end applyDecelerationConstraint
		
	/**
	 * Fill the trajectory points for the left and right side of the profile
	 */
	protected void fillProfiles() {
		//calculate outer radii for left and right velocity offsetting
		calcOuterRadii();
		
		//calculate left and right velocities
		calcWheelVelocities();
		
		//calculate the time at each step of the trajectory
		fillTimes();
		
		//calculate left and right wheel positions
		calcWheelPositions();
		
		//calculate left and right wheel accelerations
		calcWheelAccelerations();
		
		//fill the left and right side trajectory points (just velocity setpoints)
		for (int i = 0; i < SIZE; i++) {
			this.leftProfile.add(new double[] {leftPos[i], leftVel[i] / 12, leftAcc[i]});
			this.rightProfile.add(new double[] {rightPos[i], rightVel[i] / 12, rightAcc[i]});
		} //loop
		
		Util.println(this.totalTime);
	} //end fillProfiles
	
	/**
	 * Fill the array of headings for the robot to follow
	 */
	private void calcHeadings() {
		//first calc 
		headings = new double[SIZE];
		
		for (int i = 0; i < headings.length; i++) {
			headings[i] = path.calcHeading(tVals[i]);
		} //loop
	} //end calcHeadings
	
	/**
	 * Represent the change in heading as an int (1 if negative, -1 if positive, 0 if none)
	 * @return Array of values for deciding which wheel is outer
	 */
	private int[] calcDeltaHeadings() {
		int[] dTheta = new int[SIZE];
		
		dTheta[0] = 0; //no change at first point
		
		for (int i = 1; i < dTheta.length; i++) {
			double dHeading = headings[i] - headings[i-1];
			dTheta[i] = dHeading > 0 ? -1 : dHeading < 0 ? 1 : 0;
		} //loop
		
		return dTheta;
	} //end calcDeltaHeadings
	
	/**
	 * Calculate the radius of the left and right paths
	 */
	private void calcOuterRadii() {
		int[] dTheta = calcDeltaHeadings();
		double offset = trackWidth / 2;
		leftRadius = new double[SIZE];
		rightRadius = new double[SIZE];
		
		for (int i = 0; i < SIZE; i++) {
			if (dTheta[i] == 1) { //turning right, left is outer
				leftRadius[i] = centerRadius[i] + offset;
				rightRadius[i] = centerRadius[i] - offset;
				
			} else if (dTheta[i] == -1) { //turning left, right is outer
				leftRadius[i] = centerRadius[i] - offset;
				rightRadius[i] = centerRadius[i] + offset;
				
			} else { //zero, no change in heading
				leftRadius[i] = centerRadius[i];
				rightRadius[i] = centerRadius[i];
			} //if
		} //loop
	} //end calcOuterRadii
	
	/**
	 * Calculate the wheel velocities in inches per second
	 */
	private void calcWheelVelocities() {
		leftVel = new double[SIZE];
		rightVel = new double[SIZE];
		
		for (int i = 0; i < SIZE; i++) {
			leftVel[i] = (centerVel[i] / centerRadius[i]) * leftRadius[i];
			rightVel[i] = (centerVel[i] / centerRadius[i]) * rightRadius[i];
		} //loop
		
		/*
		Util.println("created chart");
		XYChart c = PlotGenerator.buildChart(1920, 1080, "Wheel Velocities", "index", "Velocity (ft/s)");
		c.addSeries("left", leftVel);
		c.addSeries("right", rightVel);
		PlotGenerator.displayChart(c);
		*/
	} //end calcWheelVelocities
	
	/**
	 * Calculate the time at each step and set the total time
	 */
	private void fillTimes() {
		times = new double[SIZE];
		double distStep = this.totalLength / SIZE;
		double time = 0;
		times[0] = 0;
		
		for (int i = 1; i < times.length; i++) {
			time += centerVel[i] == 0 ? 0 : distStep / centerVel[i]; //don't step forward in time if not moving
			times[i] = time;
		} //loop
		
		this.totalTime = time;
		
		/*
		Util.println("created chart");
		XYChart c = PlotGenerator.buildChart("Times", "Index", "Time (s)");
		c.addSeries("times", times);
		PlotGenerator.displayChart(c);
		*/
	} //end fillTimes
	
	/**
	 * Calculate the wheel positions in inches
	 */
	private void calcWheelPositions() {
		leftPos = new double[SIZE];
		rightPos = new double[SIZE];
		double left = 0;
		double right = 0;
		
		leftPos[0] = 0;
		rightPos[0] = 0;
		
		for (int i = 1; i < SIZE; i++) {
			double dt = times[i] - times[i-1];
			left += leftVel[i] * dt;
			leftPos[i] = left;
			
			right += rightVel[i] * dt;
			rightPos[i] = right;
		} //loop
		
		Util.saveDoubleArrayToFile(leftPos, "leftPos");
		Util.saveDoubleArrayToFile(rightPos, "rightPos");
	} //end calcWheelPositions
	
	/**
	 * Calculate the wheel accelerations in in/s^2
	 */
	public void calcWheelAccelerations() {
		leftAcc = new double[SIZE];
		rightAcc = new double[SIZE];
		
		leftAcc[0] = 0;
		rightAcc[0] = 0;
		
		for (int i = 1; i < SIZE; i++) {
			double dt = times[i] - times[i-1];
			leftAcc[i] = (leftVel[i] - leftVel[i-1]) / dt;
			rightAcc[i] = (rightVel[i] - rightVel[i-1]) / dt;
		} //loop
	} //end calcWheelAccelerations
	
	//Getters

	/**
	 * Get the left trajectory point at this time
	 * @param time Time in seconds to get trajectory point
	 * @return Left position, velocity, and acceleration at that time
	 */
	public double[] getLeftTrajPoint(double time) {
		int i = Util.findSandwichedElements(times, time, 1E-3)[0];
		return leftProfile.get(i);
	} //end getLeftTrajPoint
	
	/**
	 * Get the right trajectory point at this time
	 * @param time Time in seconds to get trajectory point
	 * @return Right position, velocity, and acceleration at that time
	 */
	public double[] getRightTrajPoint(double time) {
		int i = Util.findSandwichedElements(times, time, 1E-3)[0];
		return rightProfile.get(i);
	} //end getRightTrajPoint
	
	/**
	 * Get the size of the profile
	 * @return Number of steps in the profile
	 */
	public int getSize() {
		return SIZE;
	} //end getSize
} //end class