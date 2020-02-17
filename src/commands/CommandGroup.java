/**
 * CommandGroup
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * A group of commands to create routines
 */
package commands;

import java.util.ArrayList;

import model.Pose;
import util.Util;

public abstract class CommandGroup {
	//Attributes
	private ArrayList<Command> commands; //all commands to be run
	private boolean isRunning = false; //whether the command is running
	private ArrayList<Pose> poses; //poses of the robot
	private ArrayList<int[][]> curves; //curves the robot follows
	
	/**
	 * Create a command group
	 */
	public CommandGroup() {
		initialize();
	} //end constructor
	
	/**
	 * Initialize the command group by creating the command and pose lists
	 */
	private void initialize() {
		commands = new ArrayList<Command>();
		poses = new ArrayList<Pose>();
		curves = new ArrayList<int[][]>();
	} //end initialize
	
	/**
	 * Add a command to the list
	 * @param c - command to be added
	 */
	public void add(Command c) {
		commands.add(c);
	} //end add
	
	/**
	 * Runs the entire command group
	 */
	public void run() {
		isRunning = true;
		
		//run each command and add its poses to the total list
		for (int i = 0; i < commands.size(); i++) {
			commands.get(i).run();
			poses.addAll(commands.get(i).getPoses());
			curves.addAll(commands.get(i).getCurves());
		} //loop
		
		isRunning = false;
	} //end start
	
	/**
	 * Return whether the command is running or not
	 * @return isRunning - whether the command is running or not
	 */
	public boolean isRunning() {
		return isRunning;
	} //end isRunning
	
	/**
	 * Get the poses from the command group 
	 * @return poses - all poses from each command
	 */
	public ArrayList<Pose> getPoses() {
		return poses;
	} //end getPoses
	
	/**
	 * Get the curve from the command group 
	 * @return curve - all curves from each command
	 */
	public ArrayList<int[][]> getCurves() {
		return curves;
	} //end getPoses
} //end class
