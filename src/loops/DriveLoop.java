/**
 * DriveLoop
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2019
 * State machine for the drivetrain
 */
package loops;

import java.awt.Color;

import model.Point;
import model.Robot;

public class DriveLoop {
	//Attributes
	//Configured
	private Robot robot; //robot controlled by loop
	
	//Updated
	private STATE state; //state the robot is in
	private Point goalPoint; //point to drive to
	private double goalDist; //distance to drive
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
	 */
	public DriveLoop(Robot robot) {
		this.robot = robot;
		
		state = STATE.WAITING;
		goalPoint = robot.getPoint();
		goalDist = 0;
		goalAngle = 0;
		goalYaw = 0;
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
				break;
				
			//drive a distance
			case DRIVE_DISTANCE:
				//pid
				break;
				
			//turn an angle
			case TURN_ANGLE:
				//pid
				break;
				
			//finished
			case FINISHED:
				robot.setColor(Color.BLUE);
				break;
		} //switch-case
	} //end onLoop
	
} //end class
