/**
 * PurePursuitController
 * Author: Neil Balaskandarajah
 * Created on: 17/05/2020
 * Controller for the Pure Pursuit algorithm
 */

package model.motion;

import java.util.ArrayList;

import org.knowm.xchart.XYChart;

import model.FieldPositioning;
import model.Point;
import model.Pose;
import util.PlotGenerator;
import util.Util;

public class PurePursuitController {
	//Attributes
	//Seek
	private double accTime; //time to accelerate linear output to maxVel
	private double turnConst; //for angular output calculations
	private double maxSpeed; //maximum linear output in ft/s

	//Arrive
	private double goalDist; //distance away from the final point to start decelerating
	private double endDist; //distance to be from the final point to be considered "done"
	private int goalIndex; //index of the current goal in the list

	//Pure Pursuit
	private double lookahead; //lookahead distance
	private Point[] goals; //array of goals to follow
	
	//Controller
	private boolean arrived; //whether or not the controller has arrived at the final point
	private boolean reverse; //whether the controller is following in reverse
	private Point goal; //current goal to follow
	private double speed; //linear speed output
	private double turn; //angular speed output
	private Pose robotPose; //pose of the robot
	
	//Rate Limiter
	private double prevTime; //for rate limiter
	private double lastSpeed; //last speed value
	private double robotSpeed; //speed from robot
	private double maxSpeedStep; //maximum amount the speed can increase
	ArrayList<Double> output = new ArrayList<Double>();
		
	/**
	 * Create a pure pursuit controller
	 */
	public PurePursuitController() {
		//set defaults
		this.arrived = false;
		this.reverse = false;
		this.speed = 0;
		this.turn = 0;
		this.turnConst = 0;
	} //end constructor

	/**
	 * Set the constants for the seek mode
	 * @param accTime Time for linear output to reach maximum
	 * @param turnConst Proportional gain on angle error
	 * @param maxSpeed Maximum linear output (-12 to 12)
	 * @param reverse Whether to drive through the points in reverse
	 */
	public void setSeekConstants(double accTime, double turnConst, double maxSpeed, boolean reverse) {
		this.accTime = accTime;
		this.turnConst = turnConst;
		this.maxSpeed = maxSpeed;
		this.reverse = reverse;
		this.lastSpeed = 0; //start at zero lastSpeed
		this.maxSpeedStep = Util.UPDATE_PERIOD * maxSpeed / accTime;
	} //end setSeekConstants
	
	/**
	 * Set the constants for the arrive mode
	 * @param goal Distance to be from goal before ramping
	 * @param end Distance to be from goal to be considered done
	 */
	public void setArriveConstants(double goal, double end) {
		this.goalDist = goal;
		this.endDist = end;
		this.goalIndex = 0; //start at the first point
	} //end setArriveConstants
	
	/**
	 * Set the constants for the pure pursuit mode
	 * @param lookahead Size of circle to lookahead
	 * @param goals
	 */
	public void setPurePursuitConstants(double lookahead, Point[] goals) {
		this.lookahead = lookahead;
		this.goals = goals;
		this.goal = goals[0]; //set the goal to be the first point
	} //end setPurePursuitConstants
	
	/**
	 * Get the linear output
	 * @return Linear output from controller
	 */
	public double getLinOut() {
		return speed;
	} //end getLinOut
	
	/**
	 * Get the angular output
	 * @return Angular output from controller
	 */
	public double getAngOut() {
		return turn;
	} //end getAngOut
	
	/**
	 * Return whether or not the controller has arrived at the goal
	 * @return True if close enough to goal, false if not
	 */
	public boolean isArrived() {
		return arrived;
	} //end isArrived
	
	/**
	 * Get the lookahead distance for the controller
	 * @return
	 */
	public double getLookahead() {
		return lookahead;
	} //end getLookahead
	
	/**
	 * Get the goal point
	 * @return Goal point the controller is tracking
	 */
	public Point getGoal() {
		return goal;
	} //end getGoal
	
	/**
	 * Calculate the outputs for the controller
	 * @param robotPose Pose of the robot (x,y,theta)
	 * @param robotSpeed Current linear speed of the robot
	 */
	public void calcOutputs(Pose robotPose, double robotSpeed) {
		//set the pose and current speed
		this.robotPose = robotPose;
		this.robotSpeed = robotSpeed;
		
		//calculate the arrived state
		if (FieldPositioning.dist(robotPose.getPoint(), goal) <= endDist) {
			goalIndex++;
			goal = goals[Math.min(goalIndex, goals.length-1)];
		}
		
		arrived = goalIndex == goals.length;
		
		//stop if the controller has arrived
		if (arrived) {
			//don't output if arrived
			this.robotSpeed = 0;
			this.turn = 0;
			
			/*
			XYChart c = PlotGenerator.buildChart("PPC Speed vs. Time", "Time (s)", "Output (V)");
			c.addSeries("Time", Util.scaleArray(Util.indexArray(output.size()), Util.UPDATE_PERIOD), 
						Util.doubleListToArray(output));
			PlotGenerator.displayChart(c);
			*/
			
		//pursue the goals
		} else {
			/*
			 * Done
			 * Seek with linSpeed = maxVel
			 * Seek and arrive to one point
			 * Seek and arrive with rateLimiter
			 * 
			 * To-Do
			 * Seek and arrive to multiple points
			 * Pursue multiple points
			 * 
			 */
			arrive();
			seek();			
		} //if
	} //end calcOutputs
	
	/**
	 * Seek the goal point
	 */
	private void seek() {
		//Distance to the target and the absoule angle of the target
		Point delta = Point.subtract(goal, robotPose.getPoint());
		//double dist = delta.getMag();
		double absAng = Math.atan2(delta.getX(), delta.getY());
		
		//Calculate the relative angles to the target
		double relAng = FieldPositioning.angleWrap(absAng - (robotPose.getHeading() - Math.PI/2));
		double relTurn = relAng - Math.PI/2;
		double relTurn2 = relTurn + 2*Math.PI;
		
		//choose the smaller of the two angles
		double twist = Util.minMag(relTurn, relTurn2);
		if (reverse)
			twist = FieldPositioning.angleWrap(twist + Math.PI); //flip angle if reverse
		
		//scale the speed based on the size of twist
		speed *= angleScale(twist); //slow down more the larger the relative angle is
		if (reverse)
			speed = -Math.abs(speed); //flip speed if reverse
		//rate limit speed
		if (Math.abs(speed) - Math.abs(lastSpeed) > 0) {
			speed = Math.min(speed, lastSpeed + maxSpeedStep);
			lastSpeed = speed;
		}
		
		//calculate the turn output
		turn = Math.copySign(turnConst * Math.abs(twist), twist);
		
		//set the last speed
		output.add(speed); //dbg
	} //end seek
	
	/**
	 * Get the scale factor based on the angle
	 * @param twist Relative angle in radians
	 * @return Scale factor from 0 to 1
	 */
	private double angleScale(double twist) {
		return -(Math.min(Math.PI/2, Math.abs(twist)) / (Math.PI/2)) + 1;
	} //end angleScale
	
	/**
	 * Arrive at the goal
	 */
	private void arrive() {
		//ramp down the speed based on how far from the final point it is
		if (FieldPositioning.isWithinBounds(goals[goals.length-1], robotPose.getPoint(), goalDist)) {
			double dist = FieldPositioning.dist(robotPose.getPoint(), goal);
			double scaleFactor = dist < endDist ? 0 : (dist-endDist) / (goalDist-endDist);
			speed = maxSpeed * scaleFactor;
			//arrived = dist < endDist;
			
		//set the goal to max vel if not arriving
		} else {
			speed = maxSpeed;
		} //if
	} //end arrive
	
	private void purePursuit() {
		
	}
} //end class