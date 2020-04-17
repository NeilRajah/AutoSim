/**
 * DriveLoop
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2019
 * State machine for the drivetrain
 */
package model;

import java.awt.Color;

import util.Util;

public class DriveLoop {
	//Attributes
	//Configured
	private Robot robot; //robot controlled by loop
	private PIDController drivePID; //PID controller for driving
	private PIDController turnPID; //PID controller for turning
	
	//Updated
	private STATE state; //state the robot is in
	private double goalDist; 	//distance to drive
	private double tolerance; 	//epsilon to be within
	private double topSpeed; 	//max allowable speed
	private double minSpeed; 	//min allowable speed
	private double goalAngle; 	//angle to turn to
	private double kV; //feedforward velocity constant
	private double kA; //feedforward acceleration constant
	
	private double[] leftPVA; //position, velocity and acceleration
	private double[] rightPVA; 
	
	//States the robot can be in
	public static enum STATE {
		WAITING, 		//initial
		DRIVE_TO_GOAL,	//point to point driving
		DRIVE_DISTANCE, //PID distance driving
		TURN_ANGLE, 	//PID angle turning
		OPEN_LOOP_PROFILE, //FF profile following
		CLOSED_LOOP_PROFILE //FF + PID profile following
	} //end enum
	
	/**
	 * Create a loop with a robot
	 * @param robot - robot to be controlled by the loop
	 * @param drivePID - PID controller for driving
	 * @param turnPID - PID controller for turning
	 */
	public DriveLoop(Robot robot, PIDController drivePID, PIDController turnPID) {
		//set attributes
		this.robot = robot;
		this.drivePID = new PIDController(drivePID.getP(), drivePID.getI(), drivePID.getD(), robot.getMaxLinSpeed());
		this.turnPID = new PIDController(turnPID.getP(), turnPID.getI(), turnPID.getD(), robot.getMaxLinSpeed());
		
		//default cases
		state = STATE.WAITING;
		goalDist = 0;
		goalAngle = 0;
		minSpeed = 0;
		topSpeed = 0;
		tolerance = 0;
	} //end constructor
	
	//Configure Constants
	
	/**
	 * Set the feedforward values for the robot
	 * @param kV Velocity feedforward (V / ft/s)
	 * @param kA Acceleration feedforward (V / ft/s^2)
	 */
	public void setFFValues(double kV, double kA) {
		this.kV = kV;
		this.kA = kA;
	} //end setFFValues
	
	//States
	
	/**
	 * Get the state the robot is in
	 * @return state - state the robot is in
	 */
	public STATE getState() {
		return state;
	} //end getState
	
	/**
	 * Set the state of the robot
	 * @return state - new state of the robot
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
				
			//follow a profile with open loop controls
			case OPEN_LOOP_PROFILE:
				//FF
				openLoopProfileLoop();
				break;
		} //switch-case
	} //end onLoop
	
	/**
	 * Get a copy of the robot being controlled by the loop
	 * @return - copy of robot being controlled
	 */
	public Robot getRobotClone() {
		return robot.clone();
	} //end getRobot
	
	/**
	 * Get the robot being controlled by the loop
	 * @return robot - robot being controlled by loop
	 */
	public Robot getRobot() {
		return robot;
	} //end robot
	
	/**
	 * Get whether or not the PID controller for driving is at its target
	 * @return - isDone of drivePID
	 */
	public boolean isDrivePIDAtTarget() {
		return drivePID.isDone();
	} //end isDrivePIDAtTarget
	
	/**
	 * Get whether or not the PID controller for turning is at its target
	 * @return - isDone of turnPID
	 */
	public boolean isTurnPIDAtTarget() {
		return turnPID.isDone();
	} //end isTurnPIDAtTarget
	
	/**
	 * Get whether or not the robot being controlled is moving slower than a percent of its top speed
	 * @param percent - percent of its top speed between -1 and 1
	 * @return - isSlowerThanPercent of robot
	 */
	public boolean isRobotSlowerThanPercent(double percent) {
		return robot.isSlowerThanPercent(percent); 
	} //end isRobotSlowerThanPercent
	
	//DriveDistance state
	
	/**
	 * Set the loop to the DriveDistance state
	 * @param distance - distance from current position to drive to in inches
	 * @param topSpeed - top speed to be below in ft/s
	 * @param tolerance - range to be within in inches
	 */
	public void setDriveDistanceState(double distance, double topSpeed, double tolerance) {
		//configure loop parameters
		this.goalDist = distance + robot.getAveragePos();
		this.topSpeed = topSpeed;
		this.minSpeed = 0;
		this.tolerance = tolerance;
		this.goalAngle = robot.getHeading();
		
		//set the state
		this.state = STATE.DRIVE_DISTANCE;
		
		//reset the PID controllers
		drivePID.reset();
		turnPID.reset();
	} //end setDriveDistanceState
	
	/**
	 * Drive the robot with PID control to a distance target below a top speed until it gets to its target
	 */
	private void driveDistanceLoop() {
		//calculate feedback control outputs
		double driveOut = drivePID.calcRegulatedPID(goalDist, robot.getAveragePos(), tolerance, topSpeed, minSpeed);
		double turnOut = turnPID.calcPID(goalAngle, robot.getHeading(), 1);
		
		//set respective sides
		robot.update(driveOut - turnOut, driveOut + turnOut);
	} //end driveDistanceLoop
	
	//TurnAngle state
	
	/**
	 * Set the loop to the TurnAngle state
	 * @param angle - angle for the robot to turn to in radians
	 * @param topSpeed - top speed to be below in ft/s
	 * @param tolerance - tolerance to be within in radians
	 * @param relative - whether or not angle is relative to the robot's current heading or the zero
	 */
	public void setTurnAngleState(double angle, double topSpeed, double tolerance, boolean relative) {
		//configure loop parameters
		this.goalAngle = angle + (relative ? robot.getHeading() : 0);
		this.topSpeed = topSpeed;
		this.tolerance = tolerance;
		
		//set state
		this.state = STATE.TURN_ANGLE;
		
		//reset the PID controllers
		drivePID.reset();
		turnPID.reset();
	} //end setTurnAngleState
	
	/**
	 * Turn the robot with PID control to a distance target below a top speed until it gets to its target
	 */
	private void turnAngleLoop() {
		//calculate controller output
		double turnOut = turnPID.calcRegulatedPID(goalAngle, robot.getHeading(), tolerance, topSpeed, 0);
		
		//set respective sides
		robot.update(-turnOut, turnOut);
	} //end turnAngleLoop
	
	//DriveToGoal state
	
	/**
	 * Set the loop to the DriveToGoal state
	 * @param dist - distance to drive to in inches
	 * @param angle - angle to drive to in radians
	 * @param range - distance from goal point to be within in inches
	 * @param topSpeed - top speed to be under in ft/s
	 * @param minSpeed - minimum speed to be over in ft/s
	 * @param reverse - whether to drive to the goal point in reverse or not
	 */
	public void setDriveToGoalState(double dist, double angle, double range, double topSpeed, double minSpeed, boolean reverse) {
		//update the parameters
		updateDriveToGoalState(dist, angle, range, topSpeed, minSpeed, reverse);
		
		//reset the controllers
		drivePID.reset();
		turnPID.reset();
	} //end setDriveToGoalState
	
	/**
	 * Update the DriveToGoal state parameters
	 * @param dist - distance to drive to in inches
	 * @param angle - angle to drive to in radians
	 * @param range - distance from goal point to be within in inches
	 * @param topSpeed - top speed to be under in ft/s
	 * @param minSpeed - minimum speed to be over in ft/s
	 * @param reverse - whether to drive to the goal point in reverse or not
	 */
	public void updateDriveToGoalState(double dist, double yaw, double range, double topSpeed, double minSpeed, boolean reverse) {
		//configure loop parameters
		this.goalDist = dist;
		this.goalAngle = yaw;
		this.tolerance = range;
		this.topSpeed = topSpeed;
		this.minSpeed = minSpeed;
		
		//set state
		this.state = STATE.DRIVE_TO_GOAL;
	} //end updateDriveToGoalState
	
	/**
	 * Drive the robot to a goal point using the P2P control scheme
	 */
	private void driveToGoalLoop() {
		//calculate controller outputs
		double driveOut = drivePID.calcRegulatedPID(goalDist, robot.getAveragePos(), tolerance, topSpeed, minSpeed);
		double turnOut = turnPID.calcPID(goalAngle, robot.getHeading(), Math.toRadians(1));
		
		//set respective sides
		robot.update(driveOut - turnOut, driveOut + turnOut);
	} //end driveToGoalLoop	

	/**
	 * Set the state machine into the open loop profile state
	 */
	public void setOpenLoopProfileState() {
		//set the state
		this.state = STATE.OPEN_LOOP_PROFILE;
	} //end setOpenLoopProfileState
	
	/**
	 * Update the left and right goal velocities
	 * @param leftVel Left goal velocity in ft/s
	 * @param rightVel Right goal velocity in ft/s
	 */
	public void updateOpenLoopProfileState(double[] left, double[] right) {
		this.leftPVA = left;
		this.rightPVA = right;
	} //end updateOpenLoopProfileState
	
	/**
	 * Set the robot output based on left and right goal velocities
	 */
	private void openLoopProfileLoop() {
		double leftOut = kV * leftPVA[1] + kA * leftPVA[2];
		double rightOut = kV * rightPVA[1] + kA * rightPVA[2];
		
		robot.update(leftOut, rightOut);
	} //end openLoopProfileLoop
} //end class
