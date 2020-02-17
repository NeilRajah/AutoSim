/**
 * DriveDistance
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Use PID control to drive a distance within a tolerance
 */

package commands;

import java.util.ArrayList;

import model.DriveLoop;
import model.Pose;
import util.Util;

public class DriveDistance extends Command {
	//Attributes
	//General
	private DriveLoop loop; //drivetrain loop to update
	private ArrayList<Pose> poses; //list of poses to update for drawing
	
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
		
		//initialize poses list
		poses = new ArrayList<Pose>();
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
	 * Save the pose of the robot to the list
	 */
	protected void updateGraphics() {
		poses.add(loop.getRobot().getPose());
	} //end savePose

	/**
	 * Return whether the robot is moving slowly within the target or not
	 * @return - true if it is, false if it is not
	 */
	protected boolean isFinished() {
		if (loop.isDrivePIDAtTarget() && loop.isRobotSlowerThanPercent(Util.kP_DRIVE * tolerance)) {
			return true;
		} else {
			return false;
		} //if
	} //end isFinished

	/**
	 * Run at the end of the command
	 */
	protected void end() {} 

	/**
	 * Get the list of poses
	 * @return poses - list of poses for animation
	 */
	public ArrayList<Pose> getPoses() {
		return poses;
	} //end getPoses

	@Override
	public ArrayList<int[][]> getCurves() {
		// TODO Auto-generated method stub
		return null;
	}
} //end class
