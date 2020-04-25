package model.motion;

import java.util.ArrayList;

import model.FieldPositioning;
import model.Point;
import util.Util;

public class BezierProfile extends DriveProfile {
	//Attributes
	private BezierPath path; //path to follow
	private double maxVel; //maximum velocity robot can reach in in/s
	private double maxAcc; //maximum acceleration robot can reach in in/s^2
	private double maxDec; //maximum deceleration robot can reach in in/s^2
	
	private final int SIZE = 500;; //number of pieces the path is split into
	private final double STEP = 1.0 / SIZE; //inverse of size
	
	private double totalLength; //arclength of the entire path
	private Point[] evenPoints; //list of points in the path evenly spaced by distance
	
	private double[] centerVel; //center velocity of the robot
	
	/**
	 * Create a profile to follow a Bezier curve while respecting kinematic robot constraints
	 * @param controlPts Control points of the Bezier curve
	 * @param maxVel Maximum reachable robot velocity in in/s
	 * @param maxAcc Maximum reachable robot acceleration in in/s^2
	 * @param maxDec Maximum reachable robot deceleration in in/s^2
	 */
	public BezierProfile(Point[] controlPts, double maxVel, double maxAcc, double maxDec) {
		path = new BezierPath(controlPts);
		this.maxVel = maxVel;
		this.maxAcc = maxAcc;
		this.maxDec = maxDec;
		
		computeConstants();
		fillProfiles();
	} //end constructor

	/**
	 * Compute the center path trajectory
	 */
	protected void computeConstants() {
		/*
		 * DONE:
		 * Parametrically space
		 * Space by distance
		 * 
		 * TO-DO:
		 * Curvature constraint
		 * Acceleration constraint
		 * Deceleration constraint
		 */
		
		/*
		 * Create a list of points with even t value spacing. This is later used to split the
		 * curve up into segments of equal length.
		 */
		double[] tDistances = parameterizeByT();
		this.totalLength = tDistances[tDistances.length - 1]; //last distance is length of curve
		//t value is index/size, tDistances[index] is distance along path at that t value
		
		/*
		 * Create the evenly spaced points list by linearly interpolating between the points based on the
		 * distance along the curve.
		 */
		parameterizeByD(tDistances);
		
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
		double distStep = 1.0 / this.totalLength;
				
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
			evenPoints[i] = path.calcPoint(Util.interpolate(distStep, t1, d1, t2, d2));
		} //loop
	} //end paramaterizeByD
	
	private void applyCurvatureConstraint() {
		
	}
	
	private void applyAccelerationConstraint() {
		
	}
	
	private void applyDecelerationConstraint() {
		
	}
		
	protected void fillProfiles() {
		/*
		 * Offset path
		 * Offset velocities
		 * Time
		 */
	} //end fillProfiles
} //end class