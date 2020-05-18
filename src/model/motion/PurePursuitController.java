/**
 * PurePursuitController
 * Author: Neil Balaskandarajah
 * Created on: 17/05/2020
 * Controller for the Pure Pursuit algorithm
 */

package model.motion;

import java.util.ArrayList;

import model.FieldPositioning;
import model.Point;
import model.Pose;
import util.Util;

public class PurePursuitController {
	//Attributes
	//Seek
	private double accTime; //time to accelerate linear output to maxVel
	private double turnConst; //for angular output calculations
	private double maxVel; //maximum linear output in ft/s

	//Arrive
	private double goalDist; //distance away from the final point to start decelerating
	private double endDist; //distance to be from the final point to be considered "done"

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
	private double prevTime; //for rate limiter
	
	private ArrayList<String> twists;
	
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
		
		twists = new ArrayList<String>();
	} //end constructor

	public void setSeekConstants(double accTime, double turnConst, double maxVel) {
		this.accTime = accTime;
		this.turnConst = turnConst;
		this.maxVel = maxVel;
	}
	
	public void setArriveConstants(double goal, double end) {
		this.goalDist = goal;
		this.endDist = end;
	}
	
	public void setPurePursuitConstants(double lookahead, Point[] goals) {
		this.lookahead = lookahead;
		this.goals = goals;
		this.goal = goals[0];
	}
	
	public double getLinOut() {
		return speed;
	}
	
	public double getAngOut() {
		return turn;
	}
	
	public boolean isArrived() {
		return arrived;
	}
	
	public double getLookahead() {
		return lookahead;
	}
	
	public Point getGoal() {
		return goal;
	}
	
	public void calcOutputs(Pose robot) {
		this.robotPose = robot;
		arrived = FieldPositioning.dist(robot.getPoint(), goal) <= endDist;
		
		if (arrived) {
			speed = 0;
			turn = 0;
		} else {
			/*
			 * Seek with linSpeed = maxVel
			 * Seek and arrive with rateLimiter
			 * Seek and arrive to one point
			 * Seek and arrive to multiple points
			 * Pursue multiple points
			 */
			speed = maxVel;
			seek();
			
//			purePursuit();
		}
	}
	
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
		twists.add(String.format("%.3f %.3f %.3f %.3f\n", Math.toDegrees(robotPose.getHeading()), 
					Math.toDegrees(relTurn), Math.toDegrees(relTurn2), Math.toDegrees(twist)));
		if (reverse)
			twist = FieldPositioning.angleWrap(twist + Math.PI);
		
		//scale the speed based on the size of twist
		speed *= angleScale(twist);
		if (reverse)
			speed = -Math.abs(speed);
		
		//calculate the turn output
		turn = -Math.copySign(turnConst * Math.abs(twist), twist);
	}
	
	private double angleScale(double twist) {
		return -(Math.min(Math.PI/2, Math.abs(twist)) / (Math.PI/2)) + 1;
	}	
	
	private void arrive() {
		
	}
	
	private void purePursuit() {
		
	}
	
	public void writeToFile() {
		Util.saveListToFile(twists, "twists");
	}
} //end class