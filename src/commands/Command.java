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
	private ArrayList<Pose> poses = new ArrayList<Pose>(); //list of robot poses
	protected ArrayList<int[][]> curves = new ArrayList<int[][]>(); //list of curves
	protected Robot robot; //robot being commanded
	protected String name; //name of the command for debugging
	
	/**
	 * Runs once before command starts
	 */
	protected abstract void initialize();
	
	/**
	 * Runs while isFinished is false
	 */
	protected abstract void execute();
	
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
			poses.add(robot.getPose());
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
	 * @return list of poses from robot
	 */
	public ArrayList<Pose> getPoses() {
		return poses;
	} //end getPoses
	
	/**
	 * Get the curve
	 * @return - list of curves added from command
	 */
	public ArrayList<int[][]> getCurves() {
		return curves;
	} //end getCurves
	
	/**
	 * Get the data points of the robot at each pose
	 * @return list of HashMaps of data points
	 */
	public ArrayList<HashMap<ROBOT_KEY, Object>> getData() {
		return data;
	} //end data
	
	/**
	 * Get the name of the command
	 * @return String name of the command
	 */
	public String getName() {
		return name;
	} //end getName
} //end class