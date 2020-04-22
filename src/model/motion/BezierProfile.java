package model.motion;

import java.util.ArrayList;

import model.FieldPositioning;
import model.Point;
import util.Util;

public class BezierProfile extends DriveProfile {
	//Attributes
	private BezierPath path; //path to follow
	private double trackWidth; //Half the width of robot base for offsetting paths in inches
	private double accDist; //distance to accelerate over in inches
	private double maxVel; //top linear speed in feet per second
	private int SIZE; //size of the first pass
	
	private JerkProfile linTraj; //linear velocity trajectory
	private double[] tVals; //t values for the profile points
	
	public BezierProfile(Point[] controlPts, double trackWidth, double accDist, double maxVel) {
		path = new BezierPath(controlPts);
		this.trackWidth = trackWidth;
		this.accDist = accDist;
		this.maxVel = maxVel;
		this.SIZE = BezierPath.HIGH_RES;
		
		computeConstants();
		fillProfiles();
	}
	
	public BezierProfile(double[][] controlPts, double trackWidth, double accDist, double maxVel) {
		this(FieldPositioning.pointsFromDoubles(controlPts), trackWidth, accDist, maxVel);
	}

	protected void computeConstants() {
		//first pass
		double[] distances = parameterizeByT();
		
		//trajectory
		double length = distances[distances.length - 1];
		linTraj = new JerkProfile(length, accDist, maxVel);
		this.size = linTraj.getSize();
		this.totalTime = linTraj.getTotalTime();
		
		//time parameterize
		tVals = parameterizeByD(distances);
	} 
	
	/**
	 * Split the curve up with evenly spaced t values
	 * @return Distance along the curve at each t value
	 */
	private double[] parameterizeByT() {
		double[] distances = new double[SIZE];
		double dist = 0;
		
		distances[0] = dist;
		
		for (int i = 1; i < SIZE; i++) {
			double t1 = (double) i / (double) SIZE;
			double t2 = (double) (i - 1) / (double) SIZE;
			dist += FieldPositioning.calcDistance(path.calcPoint(t1), path.calcPoint(t2));
			distances[i] = dist;
		} //loop
		
		return distances;
	} //end parameterizeByT

	/**
	 * Parameterize the curve by distance values
	 * @param distances Distance values from t parameterization
	 * @return Array of t values parameterized by distance
	 */
	private double[] parameterizeByD(double[] distances) {
		double[] tVals = new double[size];
		
		for (int i = 0; i < size; i++) {
			double dist = linTraj.getLeftTrajPoint(i)[0];
			int[] limits = Util.findSandwichedElements(distances, dist, 1E-6);
			
			double tLo = (double) limits[0] / SIZE;
			double tHi = (double) limits[1] / SIZE;
			double distLo = distances[limits[0]];
			double distHi = distances[limits[1]];
			
			tVals[i] = Util.interpolate(dist, tLo, distLo, tHi, distHi);
		} //loop
		
		return tVals;
	} //end paramaterizeByD
	
	protected void fillProfiles() {
		//center path and headings
		Point[] center = new Point[this.size];
		double[] headings = new double[this.size];
		
		for (int i = 0; i < this.size; i++) {
			center[i] = path.calcPoint(tVals[i]);
			headings[i] = path.calcHeading(tVals[i]);
		} //loop
		
		//left and right paths
		Point[] leftPts = new Point[center.length];
		Point[] rightPts = new Point[center.length];
		double r = trackWidth;
		
		for (int i = 0; i < size; i++) {
			//calc left and right sides
			double thetaL, thetaR; //angle offsets for left and right
			thetaL = Math.toRadians(headings[i] + 90);
			thetaR = Math.toRadians(headings[i] - 90);
			
			if (i == 0) { //swap values for first point
				double buffer = thetaL;
				thetaL = thetaR;
				thetaR = buffer;
			} //if			
			
			//left side
			double xL = center[i].getX() + r * Math.cos(thetaL); 
			double yL = center[i].getY() + r * Math.sin(thetaL); 
			leftPts[i] = new Point(xL, yL);

			//right side
			double xR = center[i].getX() + r * Math.cos(thetaR); 
			double yR = center[i].getY() + r * Math.sin(thetaR); 
			rightPts[i] = new Point(xR, yR);
		} //loop
		
		//calculate left and right side PVAs (A = 0)
		ArrayList<double[]> left = this.leftProfile;
		ArrayList<double[]> right = this.rightProfile;

		double leftPos = 0, rightPos = 0;
		left.add(new double[] {leftPos,0,0});
		right.add(new double[] {rightPos,0,0});
		double dt = Util.UPDATE_PERIOD;
		
		for (int i = 1; i < size; i++) {
			double[] leftPVA = new double[3];
			leftPVA[2] = 0;
			double leftDist = FieldPositioning.calcDistance(leftPts[i-1], leftPts[i]);
			leftPos += leftDist;
			double leftVel = leftDist / (dt * 12);
			this.leftProfile.add(new double[] {leftPos, leftVel, 0});
			
			double[] rightPVA = new double[3];
			rightPVA[2] = 0;
			double rightDist = FieldPositioning.calcDistance(rightPts[i-1], rightPts[i]);
			rightPos += rightDist;
			double rightVel = rightDist / (dt * 12);
			this.rightProfile.add(new double[] {rightPos, rightVel, 0});
		} //loop
	} //end fillProfiles
} //end class