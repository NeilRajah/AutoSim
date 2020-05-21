/**
 * DriveLoop
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2019
 * State machine for the drivetrain
 */
package model;

import java.awt.Color;

import model.motion.PurePursuitController;
import util.Util;

public class DriveLoop {
	//Attributes
	private Robot robot; //robot controlled by loop
	private PIDController drivePID; //PID controller for driving
	private PIDController turnPID; //PID controller for turning
	private RAMSETEController ramsete; //RAMSETE controller for pose tracking
	private PurePursuitController ppc; //Pure Pursuit controller
	
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
	
	private Pose goalPose; //goal pose (x,y,theta)
	private double goalLinVel; //goal lin vel ft/s
	private double goalAngVel; //goal ang vel ft/s

	//States the robot can be in
	public static enum STATE {
		WAITING, 		//initial
		DRIVE_TO_GOAL,	//point to point driving
		DRIVE_DISTANCE, //PID distance driving
		TURN_ANGLE, 	//PID angle turning
		OPEN_LOOP_PROFILE, //FF profile following
		CLOSED_LOOP_LINEAR_PROFILE, //FF + PID profile following
		CURVE_FOLLOWING, //PIDFF curve following
		PURE_PURSUIT //pure pursuit algorithm
	} //end enum
	
	/**
	 * Create a loop with a robot
	 * @param robot Robot to be controlled by the loop
	 * @param drivePID PID controller for driving
	 * @param turnPID PID controller for turning
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
	
	/**
	 * Set the Pure Pursuit Controller for the loop
	 * @param ppc Configured Pure Pursuit Controller
	 */
	public void setPurePursuitController(PurePursuitController ppc) {
		this.ppc = ppc;
	} //end setPurePursuitController
	
	/**
	 * Get the Pure Pursuit controller
	 * @return Pure Pursuit controller used for path following
	 */
	public PurePursuitController getPurePursuitController() {
		return ppc;
	} //end getPurePursuitController
	
	//States
	
	/**
	 * Get the state the robot is in
	 * @return state State the robot is currently in
	 */
	public STATE getState() {
		return state;
	} //end getState
	
	/**
	 * Set the state of the robot
	 * @return state New state of the robot
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
				
			case CLOSED_LOOP_LINEAR_PROFILE:
				//PID + FF
				closedLoopLinearProfileLoop();
				break;
				
			case CURVE_FOLLOWING:
				curveFollowingLoop();
				break;
				
			case PURE_PURSUIT:
				purePursuitLoop();
				break;
		} //switch-case
	} //end onLoop
		
	/**
	 * Get the robot being controlled by the loop
	 * @return robot Robot being controlled by loop
	 */
	public Robot getRobot() {
		return robot;
	} //end robot
	
	/**
	 * Get whether or not the PID controller for driving is at its target
	 * @return isDone of drivePID
	 */
	public boolean isDrivePIDAtTarget() {
		return drivePID.isDone();
	} //end isDrivePIDAtTarget
	
	/**
	 * Get whether or not the PID controller for turning is at its target
	 * @return isDone of turnPID
	 */
	public boolean isTurnPIDAtTarget() {
		return turnPID.isDone();
	} //end isTurnPIDAtTarget
	
	/**
	 * Get whether or not the robot being controlled is moving slower than a percent of its top speed
	 * @param percent Percent of its top speed between -1 and 1
	 * @return isSlowerThanPercent of robot
	 */
	public boolean isRobotSlowerThanPercent(double percent) {
		return robot.isSlowerThanPercent(percent); 
	} //end isRobotSlowerThanPercent
	
	/**
	 * Return if the robot is slower than a velocity (ft/s)
	 * @param vel Velocity to be slower than
	 * @return True if slower, false if not
	 */
	public boolean isRobotSlowerThanVel(double vel) {
		return robot.isSlowerThanPercent(Math.abs(vel) / robot.getMaxLinSpeed());
	} //end isRobotSlowerThanVel
	
	//DriveDistance state
	
	/**
	 * Set the loop to the DriveDistance state
	 * @param distance Distance from current position to drive to in inches
	 * @param topSpeed Top speed to be below in ft/s
	 * @param tolerance Range to be within goal distance in inches
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
	 * @param angle Angle for the robot to turn to in radians
	 * @param topSpeed Top speed to be below in ft/s
	 * @param tolerance Tolerance to be within in radians
	 * @param relative Whether or not angle is relative to the robot's current heading or the zero
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
	 * @param dist Distance to drive to in inches
	 * @param angle Angle to drive to in radians
	 * @param range Distance from goal point to be within in inches
	 * @param topSpeed Top speed to be under in ft/s
	 * @param minSpeed Minimum speed to be over in ft/s
	 * @param reverse Whether to drive to the goal point in reverse or not
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
	 * @param dist Distance to drive to in inches
	 * @param angle Angle to drive to in radians
	 * @param range Distance from goal point to be within in inches
	 * @param topSpeed Top speed to be under in ft/s
	 * @param minSpeed Minimum speed to be over in ft/s
	 * @param reverse Whether to drive to the goal point in reverse or not
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

	/**
	 * Set the state of the loop to closed loop profile
	 * @param tolerance Distance tolerance
	 * @param totalDist Distance setpoint
	 * @param initPos Initial position
	 */
	public void setClosedLoopLinearProfileState(double tolerance, double totalDist, double initPos) {
		this.state = STATE.CLOSED_LOOP_LINEAR_PROFILE;
		this.tolerance = tolerance;
		this.goalDist = totalDist;
		this.drivePID.setInitPos(initPos);
	} //end set state
	
	/**
	 * Update the left and right goals
	 * @param left Left position, velocity and acceleration
	 * @param right Right position, velocity and acceleration
	 */
	public void updateClosedLoopLinearProfileState(double[] left, double[] right) {
		this.leftPVA = left;
		this.rightPVA = right;
	} //end updateClosedLoopLinearProfileState
	
	/**
	 * Set the output based on the goal position, velocity and acceleration
	 */
	private void closedLoopLinearProfileLoop() {
		//can choose from left or right, both are same
		double pos = leftPVA[0] + this.drivePID.getInitPos(); //position robot needs to be at
		double vel = leftPVA[1];
		double acc = leftPVA[2];
		
		double output = drivePID.calcDVPID(pos, robot.getAveragePos(), vel, tolerance) + calcFFOutput(vel, acc);
		
		robot.update(output, output);
	} //end closedLinearProfileLoop
	
	/**
	 * Set the state machine into the curve following state
	 */
	public void setCurveFollowingState() {
		this.state = STATE.CURVE_FOLLOWING;
	} //end setCurveFollowingState
	
	/**
	 * Update the left and right goals
	 * @param left Left position, velocity and acceleration
	 * @param right Right position, velocity and acceleration
	 */
	public void updateCurveFollowingState(Pose goal, double goalLin, double goalAng) {
		this.goalPose = goal;
		this.goalLinVel = goalLin;
		this.goalAngVel = goalAng;
	} //end updateCurveFollowingState
	
	/**
	 * Follow the wheel velocity setpoints using FF
	 */
	private void curveFollowingLoop() {
		double[] outputs = ramsete.calcWheelSpeeds(robot.getPose(), goalPose, goalLinVel, goalAngVel, robot.getWidthInches());
		
		double leftOut = Util.kP_DRIVE * outputs[0];
		double rightOut = Util.kP_DRIVE * outputs[1];
		
		robot.update(leftOut, rightOut);
	} //end curveFollowingLoop
	
	/**
	 * Calculate the feedforward output based on velocity and acceleration
	 * @param vel Goal velocity
	 * @param acc Goal acceleration
	 * @return Output using feedforward constants determined through characterization
	 */
	private double calcFFOutput(double vel, double acc) {
		return kV * vel + kA * acc;
	} //end calcFFOutput
	
	//Pure Pursuit
	
	/**
	 * Update the pure pursuit state
	 * @param robotPose Current pose of the robot
	 * @param robotSpeed Current linear speed of the robot
	 */
	public void updatePurePursuitState(Pose robotPose, double robotSpeed) {
		ppc.calcOutputs(robotPose, robotSpeed);
	} //end updatePurePursuitState
	
	/**
	 * Set the outputs based on the pure pursuit controller
	 */
	private void purePursuitLoop() {
		double speed = ppc.getLinOut();
		double turn = ppc.getAngOut();
		robot.update(speed - turn, speed + turn);
	} //end purePursuitLoop
} //end class