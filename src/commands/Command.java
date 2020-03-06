/*
 * Command
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Interface for running commands
 */
package commands;

import java.util.ArrayList;
import java.util.HashMap;

import model.Pose;
import model.Robot;
import util.Util.ROBOT_KEY;

public abstract class Command implements Runnable {
	//Attributes
	private boolean isRunning; //whether the command is running or not
	private boolean isTimedOut; //whether the command times out or not
	private ArrayList<HashMap<ROBOT_KEY, Object>> data = new ArrayList<HashMap<ROBOT_KEY, Object>>(); //robot data
	protected Robot robot; //robot being commanded
	
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
			data.add(robot.getData());
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
	
	/**
	 * Get the curve
	 */
	public abstract ArrayList<int[][]> getCurves();
	
	/**
	 * Get the data points of the robot at each pose
	 * @return list of HashMaps of data points
	 */
	public ArrayList<HashMap<ROBOT_KEY, Object>> getData() {
		return data;
	} //end data
} //end class