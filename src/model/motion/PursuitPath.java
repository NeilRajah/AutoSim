/**
 * PursuitPath
 * Author: Neil Balaskandarajah
 * Created on: 10/05/2020
 * Path object for Pure Pursuit tracking
 */

package model.motion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import model.FieldPositioning;
import model.Point;
import util.Util;

public class PursuitPath {
	//Constants
	private final double DIST_STEP = 1.0; //spacing between points
	
	//Attributes
	private BezierPath path;
	private double trackWidth;
	private double maxVel;
	private double acc;
	private double dec;
	
	private Point[] points;
	private double totalLength;
	private double[] distAlongPath;
	private double[] radius;
	private double[] vel;
	
	public PursuitPath(Point[] controlPts, double trackWidth, double maxVel, double acc, double dec) {
		//set attributes
		this.path = new BezierPath(controlPts);
		this.trackWidth = trackWidth;
		this.maxVel = maxVel;
		this.acc = acc;
		this.dec = dec;
		
		//create the necessary arrays 
		createPath();
	}
	
	public PursuitPath(Point[] controlPts, double trackWidth, double maxVel, double acc) {
		this(controlPts, trackWidth, maxVel, acc, -acc);
	}
	
	public PursuitPath(double[][] controlPts, double trackWidth, double maxVel, double acc, double dec) {
		this(FieldPositioning.pointsFromDoubles(controlPts), trackWidth, maxVel, acc, dec);
	}
	
	public PursuitPath(double[][] controlPts, double trackWidth, double maxVel, double acc) {
		this(FieldPositioning.pointsFromDoubles(controlPts), trackWidth, maxVel, acc, -acc);
	}
	
	public PursuitPath(Point[] points, double[] distAlongPath, double[] radius, double[] vel) {
		//Set the values directly
		this.points = points;
		this.distAlongPath = distAlongPath;
		this.radius = radius;
		this.vel = vel;
	}
	
	private void createPath() {
		/*
		 * Done:
		 * parameterize by t
		 * parameterize by d (stepSize ~1")
		 * radius at all points
		 * write to file
		 * read from file
		 * curvature constraint
		 * 
		 * To-Do:
		 * acceleration constraint
		 * deceleration constraint
		 */
		
		//---------------------------------
		
		/*
		 * Create a list of points with even t value spacing. This is later used to split the
		 * curve up into segments of equal length.
		 */
		double[] tDistances = parameterizeByT();
		
		/*
		 * Create the evenly spaced points list by linearly interpolating between the points based on the
		 * distance along the curve. The curve radius (inverse of curvature) and heading (degrees) is
		 * also calculated at this point.
		 */
		parameterizeByD(tDistances);
		
		/**
		 * Calculate the radius of travel between adjacent points. Beginning and end segments
		 * are assumed to be straight (ie. very large radius) such that the curvature value 
		 * (C = 1/r) is very small.
		 */
		calcRadii();
		
		/*
		 * Constrain the center velocity of the robot by the path's curvature. 
		 */
		applyCurvatureConstraint();
		
		/*
		 * Constrain the center velocity of the robot by the maximum acceleration with a forward pass
		 * of the center velocity list.
		 */
//		applyAccelerationConstraint();
		
		/*
		 * Constrain the center velocity of the robot by the maximum deceleration with a backward pass
		 * of the center velocity list.
		 */
//		applyDecelerationConstraint();
	}
	
	private double[] parameterizeByT() {
		final int size = 2000;
		final double step = 1.0/size;
		
		double t = 0; //parametric value
		double dist = 0; 
		double[] distances = new double[size];
		
		distances[0] = 0; //start with zero distance
		
		for (int i = 1; i < size; i++) {
			t += step;
			//cumulatively sum the distance
			dist += FieldPositioning.calcDistance(path.calcPoint(t), path.calcPoint(t - step));
			distances[i] = dist;
		} //loop
		
		this.totalLength = distances[distances.length-1];
		return distances;
	}
	
	/*
	 * Might be able to get away with not creating tVals
	 */
	private void parameterizeByD(double[] tDistances) {
		final int size = (int) Math.ceil(this.totalLength / DIST_STEP);
		final double step = 1.0/size;
		double dist = 0;
				
		//initialize the t values array
		double[] tVals = new double[size];
		tVals[0] = 0;
		
		//initialize the points array
		this.points = new Point[size];
		this.points[0] = path.getControlPoints()[0]; //first point of path object
		
		//initialize the distance array
		this.distAlongPath = new double[size];
		distAlongPath[0] = 0; //start at zero units
		
		for (int i = 1; i < size; i++) {			
			//index of the distance in tDistances just below dist (bottom of the sandwich)
			int k = Util.findSandwichedElements(tDistances, (double) i * DIST_STEP, 1E-3)[0];
			
			//linear interpolation
			double t2 = (k+1) * step;
			double t1 = k * step;
			double d2 = tDistances[k+1];
			double d1 = tDistances[k];
			
			//add the point at the interpolated t value to the curve
			tVals[i] = Util.interpolate(dist, t1, d1, t2, d2);
			points[i] = path.calcPoint(tVals[i]);
			
			//add the distance to the distances array
			dist += FieldPositioning.calcDistance(points[i-1], points[i]);
			distAlongPath[i] = dist;
		} //loop
	}
	
	private void calcRadii() {
		radius = new double[points.length];
		
		//first and last segments have max radius
		radius[0] = 100000;
		radius[radius.length-1] = 100000;
		
		//calculate radii at each point
		for (int i = 1; i < radius.length-1; i++) {
			radius[i] = FieldPositioning.calcRadius(points[i-1], points[i], points[i+1]);
		} //loop
	}
	
	private void applyCurvatureConstraint() {
		//create the velocities array
		vel = new double[points.length];
		
		//fill the center velocities
		for (int i = 0; i < points.length; i++) {
			vel[i] = (maxVel * radius[i]) / (radius[i] + trackWidth/2);
		} //loop
	}
	
	private void applyAccelerationConstraint() {
		vel[0] = 0; //start at zero initial velocity
		
		//calculate the new center velocity at each point, skipping the first one
		for (int i = 1; i < vel.length; i++) {
			double velFromAcc = Math.sqrt(Math.pow(vel[i-1], 2) + 2 * acc * DIST_STEP); //sqrt(v^2 + 2ad)
			vel[i] = Math.min(velFromAcc, vel[i]); //minimum of this new constraint and old value
		} //loop
	}
	
	private void applyDecelerationConstraint() {
		//start at final speed of zero
		vel[vel.length - 1] = 0;
		
		//calculate the new center velocity looping backwards, skipping the first one
		for (int i = points.length - 2; i >= 0; i--) {
			double velFromAcc = Math.sqrt(Math.pow(vel[i+1], 2) + 2 * dec * DIST_STEP); //sqrt(v^2 + 2ad)
			vel[i] = Math.min(velFromAcc, vel[i]); //minimum of this new constraint and old value
		} //loop
	}
	
	//Getters
	
	//File
	
	public boolean writeToFile(String filename) {
		try {
			filename = Util.UTIL_DIR + filename + ".prstpath";
			PrintWriter pw = new PrintWriter(new File(filename));
			
			//header of file is size of path
			pw.println(points.length);
			
			//(x,y), distAlongPath, radius, velocity
			for (int i = 0; i < points.length; i++) {
				//x y distAlongPath radius vel
				pw.println(String.format("%.3f %.3f %.3f %.3f %.3f", 
							points[i].getX(), points[i].getY(), //(x,y) point
							distAlongPath[i], //based off index because evenly spaced
							radius[i], //radius
							vel[i])); //velocity
			} //loop
			
			pw.close();
			
			return true;
			
		} catch (FileNotFoundException f) {
			Util.println("Could not find", filename);
			return false;
		}
	}
	
	public static PursuitPath createFromFile(String filename) {
		try {
			Scanner s = new Scanner(new File(Util.UTIL_DIR + filename + ".prstPath"));
			final int size = Integer.parseInt(s.nextLine()); //size from first line
			
			//Values for path
			Point[] points = new Point[size];
			double[] distAlongPath = new double[size];
			double[] radius = new double[size];
			double[] vel = new double[size];
			
			//fill the above arrays
			for (int i = 0; i < size; i++) {
				points[i] = new Point(s.nextDouble(), s.nextDouble());
				distAlongPath[i] = s.nextDouble();
				radius[i] = s.nextDouble();
				vel[i] = s.nextDouble();
			} //loop
			
			//close the Scanner
			s.close();
					
			return new PursuitPath(points, distAlongPath, radius, vel);
		
		} catch (FileNotFoundException f) {
			Util.println("Could not find", filename);			
			return null;
		
		} catch (NumberFormatException n) {
			Util.println("Issue parsing", filename); 
			//more detailed explanation here?
			return null;
		} //try-catch
	} 
} //end class
