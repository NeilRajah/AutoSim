/**
 * DriveDistance
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Use PID control to drive a distance within a tolerance
 */

package commands;

import model.DriveLoop;
import util.Util;

public class DriveDistance extends Command {
	//Attributes
	//General
	private DriveLoop loop; //drivetrain loop to update
	
	//Specific
	private double distance; //distance to drive in inches
	private double tolerance; //tolerance to be within in inche
	private double topSpeed; //top speed to drive at in inches
	
	/**
	 * Drive the robot a specified distance within a tolerance and below a top speed
	 * @param loop - drivetrain loop to update
	 * @param distance - distance to drive in inches
	 * @param tolerance - tolerance to be within in inches
	 * @param topSpeed - top speed to drive at in inches
	 */
	public DriveDistance(DriveLoop loop, double distance, double tolerance, double topSpeed) {
		//set attributes
		this.loop = loop;
		this.distance = distance;
		this.tolerance = tolerance; 
		this.topSpeed = topSpeed;
		
		//set robot and name
		this.robot = loop.getRobot();
		this.name = "DriveDistance";
	} //end constructor

	/**
	 * Initialize the command by setting the state machine state and corresponding values
	 */
	protected void initialize() {
		loop.setDriveDistanceState(distance + tolerance, topSpeed, tolerance);
	} //end initialize

	/**
	 * Execute the command by running the state machine
	 */
	protected void execute() {
		loop.onLoop();
	} //end onLoop

	/**
	 * Return whether the robot is moving slowly within the target or not
	 * @return - true if it is, false if it is not
	 */
	protected boolean isFinished() {
		return loop.isDrivePIDAtTarget() && 
				loop.isRobotSlowerThanPercent(Util.kP_DRIVE * tolerance);
	} //end isFinished

	/**
	 * Run at the end of the command
	 */
	protected void end() {} 
} //end class
