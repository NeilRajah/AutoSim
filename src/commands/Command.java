/*
 * Command
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Interface for running commands
 */
package commands;

import java.util.ArrayList;

import model.Pose;

public abstract class Command implements Runnable {
	//Attributes
	private boolean isRunning; //whether the command is running or not
	private boolean isTimedOut; //whether the command times out or not
	
	/**
	 * Runs once before command starts
	 */
	protected abstract void initialize();
	
	/**
	 * Runs while isFinished is false
	 */
	protected abstract void execute();
	
	/**
	 * Update values for animation purposes
	 */
	protected abstract void updateGraphics();
	
	/**
	 * Checks whether the command isFinished
	 */
	protected abstract boolean isFinished();
	
	/**
	 * Ends the command
	 */
	protected abstract void end();
	
	/**
	 * Runs a command until finished
	 */
	public void run() {
		//initialize the command
		isRunning = true;
		isTimedOut = false;
		this.initialize();
		double initTime = System.currentTimeMillis();
		
		//execute the command until it is finished
		while(!this.isFinished() && !this.isTimedOut) {
			isTimedOut = (System.currentTimeMillis() - initTime) > 5000;
			this.execute();
			this.updateGraphics();
		} //loop
		
		//end the command
		this.end();	
		isRunning = false;
		isTimedOut = false;
	} //end run
	
	/**
	 * Check if the command is running
	 * @return isRunning - whether the command is running or not
	 */
	public boolean isRunning() {
		return isRunning;
	} //end isRunning
	
	/**
	 * Get the list of poses 
	 */
	public abstract ArrayList<Pose> getPoses(); 
} //end class