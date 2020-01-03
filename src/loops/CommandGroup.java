/**
 * CommandGroup
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * A group of commands to create routines
 */
package loops;

import java.util.ArrayList;

public abstract class CommandGroup {
	//Attributes
	private ArrayList<Command> commands; //all commands to be run
	private boolean isRunning = false; //whether the command is running
	
	/*
	 * Create a command group
	 */
	public CommandGroup() {
		initialize();
	} //end constructor
	
	/*
	 * Initialize the command group by creating the command array
	 */
	private void initialize() {
		commands = new ArrayList<Command>();
	} //end initialize
	
	/*
	 * Add a command to the list
	 * Command c - command to be added
	 */
	public void add(Command c) {
		commands.add(c);
	} //end add
	
	/*
	 * Runs the entire command group
	 */
	public void start() {
		isRunning = true;
//		boolean commandRunning = false;
		
		for (int i = 0; i < commands.size(); i++) {
//			if (!commandRunning) {
//				commands.get(i).run();
//				commandRunning = true;
//				
//			} else {
//				if (commands.get(i).isFinished()) {
//					commandRunning = false;
//				} //if
//			} //if
			
			commands.get(i).run();
			
//			if (commands.get(i).isRunning()) {
//				if (commands.get(i).isFinished()) {
//					continue;
//				}
//			} else {
//				commands.get(i).run();
//			}
		} //loop
		
		isRunning = false;
	} //end start
	
	/*
	 * Return whether the command is running or not
	 */
	public boolean isRunning() {
		return isRunning;
	} //end isRunning
} //end class
