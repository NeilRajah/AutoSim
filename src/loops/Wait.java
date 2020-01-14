/**
 * Wait
 * Author: Neil Balaskandarajah
 * Created on: 04/01/2020
 * Have the robot wait at its current position for an amount of time
 */
package loops;

import java.awt.Color;
import java.util.ArrayList;

import loops.DriveLoop.STATE;
import model.Pose;
import util.Util;

public class Wait extends Command {
	//Attributes
	//General
	private DriveLoop loop; //drivetrain loop to update
	private ArrayList<Pose> poses; //list of poses to update for drawing
		
	//Specific
	private double waitTime; //time to wait in seconds
	private int counter; //counter for knowing when to end command
	private int updates; //number of updates to run before finished 
	
	/**
	 * Have the robot wait at its position in seconds
	 * @param driveLoop - drivetrain loop to update
	 * @param waitTime - time to wait in seconds
	 */
	public Wait(DriveLoop driveLoop, double waitTime) {
		//set attributes
		this.loop = driveLoop;
		this.waitTime = waitTime;
		
		poses = new ArrayList<Pose>();
	} //end constructor

	/**
	 * Initialize the command by setting the state and the number of updates
	 */
	protected void initialize() {
		updates = (int) (waitTime / Util.UPDATE_PERIOD);
		loop.setState(STATE.WAITING);
	} //end initialize

	/**
	 * Execute the command by incrementing the counter
	 */
	protected void execute() {
		counter++;
	} //end execute

	/*
	 * Save the current pose to the list
	 */
	protected void savePose() {
		Pose pose = loop.robot().getPose();
		pose.setColor(Color.yellow);
		poses.add(pose);
	} //end savePose
 
	/*
	 * Return whether the time in seconds has elapsed
	 * @return - true if time has elapsed, false if not
	 */
	protected boolean isFinished() {
		if (counter > updates) {
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
} //end class
