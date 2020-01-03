/**
 * DriveLoop
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2019
 * State machine for the drivetrain
 */
package loops;

import java.awt.Color;

import model.FieldPositioning;
import model.PIDController;
import model.Point;
import model.Robot;

public class DriveLoop {
	//Attributes
	//Configured
	private Robot robot; //robot controlled by loop
	private PIDController drivePID; //PID controller for driving
	private PIDController turnPID; //PID controller for turning
	
	//Updated
	private STATE state; //state the robot is in
	
	private Point goalPoint; //point to drive to
	private double goalDist; //distance to drive
	private double tolerance; //epsilon to be within
	private double topSpeed; //max allowable speed
	private double minSpeed; //min allowable speed
	private double goalAngle; //angle to turn to
	private double goalYaw; //yaw to turn to
	
	//States
	public static enum STATE {
		WAITING, 		//initial
		DRIVE_TO_GOAL,	//point to point driving
		DRIVE_DISTANCE, //PID distance driving
		TURN_ANGLE, 	//PID angle turning
		FINISHED 		//finished command group
	} //end enum
	
	/**
	 * Create a loop with a robot
	 * Robot robot - robot to be controlled by the loop
	 * PIDController drivePID - PID controller for driving
	 * PIDController turnPID - PID controller for turning
	 */
	public DriveLoop(Robot robot, PIDController drivePID, PIDController turnPID) {
		//set attributes
		this.robot = robot;
		this.drivePID = new PIDController(drivePID.getP(), drivePID.getI(), drivePID.getD(), robot.getMaxLinSpeed());
		this.turnPID = new PIDController(turnPID.getP(), turnPID.getI(), turnPID.getD(), robot.getMaxLinSpeed());
		
		//default cases
		state = STATE.WAITING;
		goalPoint = robot.getPoint();
		goalDist = 0;
		goalAngle = 0;
		goalYaw = 0;
		minSpeed = 0;
		topSpeed = 0;
		tolerance = 0;
	} //end constructor

	//States
	
	/**
	 * Get the state the robot is in
	 * return state - state the robot is in
	 */
	public STATE getState() {
		return state;
	} //end getState
	
	/**
	 * Set the state of the robot
	 * STATE state - new state of the robot
	 */
	public void setState(STATE state) {
		this.state = state;
	} //end setState
	
	/**
	 * Run commands based on the state the robot is in
	 */
	public void onLoop() {
		switch (state) {
			//waiting state
			case WAITING:
				robot.setColor(Color.YELLOW);
				break;
				
			//driving to goal
			case DRIVE_TO_GOAL:
				//hypotenuse and angle calcs
				//pid
				driveToGoalLoop();
				break;
				
			//drive a distance
			case DRIVE_DISTANCE:
				//pid
				driveDistanceLoop();
				break;
				
			//turn an angle
			case TURN_ANGLE:
				//pid
				turnAngleLoop();
				break;
				
			//finished
			case FINISHED:
				robot.setColor(Color.BLUE);
				break;
		} //switch-case
	} //end onLoop
	
	public Robot getRobot() {
		return robot;
	}
	
	//controller
	public boolean isDrivePIDAtTarget() {
		return drivePID.isDone();
	}
	
	public boolean isTurnPIDAtTarget() {
		return turnPID.isDone();
	}
	
	public boolean isRobotSlowerThanPercent(double percent) {
		return robot.isSlowerThanPercent(percent); 
	}
	
	//drive distance
	
	public void setDriveDistance(double distance, double topSpeed, double tolerance) {
		this.goalDist = distance + robot.getAveragePos();
		this.topSpeed = topSpeed;
		this.tolerance = tolerance;
		this.goalAngle = robot.getHeading();
		
		this.state = STATE.DRIVE_DISTANCE;
	}
	
	private void driveDistanceLoop() {
		double driveOut = drivePID.calcRegulatedPID(goalDist, robot.getAveragePos(), tolerance, topSpeed, minSpeed);
		double turnOut = turnPID.calcPID(goalAngle, robot.getHeading(), 1);
		
		robot.update(driveOut + turnOut, driveOut - turnOut);
	}
	
	//turn angle
	
	public void setTurnAngle(double angle, double tolerance) {
		this.goalAngle = angle;
		this.tolerance = tolerance;
		
		this.state = STATE.TURN_ANGLE;
	}
	
	private void turnAngleLoop() {
		double turnOut = turnPID.calcPID(goalAngle, robot.getHeading(), 1);
		
		robot.update(turnOut, -turnOut);
	}
	
	//drive to goal
	
	public void setDriveToGoal(Point goal, double epsilon, double topSpeed, double minSpeed, boolean reverse) {
		this.goalDist = FieldPositioning.calcDistance(goal, robot.getPoint());
		this.tolerance = epsilon;
		this.topSpeed = topSpeed;
		this.minSpeed = minSpeed;
		
		this.state = STATE.DRIVE_TO_GOAL;
	}
	
	private void driveToGoalLoop() {
		double driveOut = drivePID.calcRegulatedPID(goalDist, robot.getAveragePos(), tolerance, topSpeed, minSpeed);
		double turnOut = turnPID.calcPID(goalAngle, robot.getHeading(), 1);
		
		robot.update(driveOut + turnOut, driveOut - turnOut);
	}
	
} //end class
