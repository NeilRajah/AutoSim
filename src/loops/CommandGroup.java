/**
 * CommandGroup
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * A group of commands to create routines
 */
package loops;

import java.util.ArrayList;

import model.Pose;
import util.Util;

public abstract class CommandGroup {
	//Attributes
	private ArrayList<Command> commands; //all commands to be run
	private boolean isRunning = false; //whether the command is running
	private ArrayList<Pose> poses; //poses of the robot
	
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
	public void start() {
		isRunning = true;
		
		Util.println("CG:", commands.size()); //print number of commands in the group
		
		//run each command and add its poses to the total list
		for (int i = 0; i < commands.size(); i++) {
			commands.get(i).run();
			poses.addAll(commands.get(i).getPoses());
		} //loop
		
		isRunning = false;
		
		Util.println("TIME:", poses.size() * Util.UPDATE_PERIOD); //print total time for command group
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
} //end class
