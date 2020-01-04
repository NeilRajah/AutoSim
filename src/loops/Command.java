/*
 * Command
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Interface for running commands
 */
package loops;

import java.util.ArrayList;

import model.Pose;

public abstract class Command implements Runnable {
	//Attributes
	private boolean isRunning; //whether the command is running or not
	
	/*
	 * Runs once before command starts
	 */
	protected abstract void initialize();
	
	/*
	 * Runs while isFinished is false
	 */
	protected abstract void execute();
	
	/*
	 * Save the pose of the robot for animation purposes
	 */
	protected abstract void savePose();
	
	/*
	 * Checks whether the command isFinished
	 */
	protected abstract boolean isFinished();
	
	/*
	 * Ends the command
	 */
	protected abstract void end();
	
	/*
	 * Runs a command until finished
	 */
	public void run() {
		isRunning = true;
		this.initialize();
		
		while(!this.isFinished()) {
			this.execute();
			this.savePose();
		} //loop
		
		this.end();	
		isRunning = false;
	} //end run
	
	/*
	 * Check if the command is running
	 */
	public boolean isRunning() {
		return isRunning;
	} //end isRunning
	
	/*
	 * Get the list of poses 
	 */
	public abstract ArrayList<Pose> getPoses(); 
} //end class