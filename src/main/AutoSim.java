/**
 * AutoSim
 * Author: Neil Balaskandarajah
 * Created on: 08/11/2019
 * Simulates the motion of a differential drive robot and its control algorithms
 */

package main;

import java.awt.Toolkit;

import graphics.Painter;
import graphics.Window;
import loops.Command;
import loops.CommandGroup;
import loops.DriveDistance;
import loops.DriveLoop;
import loops.routines.DriveToGoalDemo;
import model.Gearbox;
import model.Motor;
import model.PIDController;
import model.Robot;
import util.FieldPoints;
import util.Util;

public class AutoSim {
	//Constants
	//Screen dimensions in pixels for scaling
	public static final int SCREEN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width);
	public static final int SCREEN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height);
	
	//Pixels Per Inch (ppi), used for scaling to different screen resolutions
	public static final int ppi = (int) Math.floor(5.0 * (SCREEN_WIDTH/3840.0));
	
	//Command
	public static DriveLoop driveLoop;
	private static Command c;
	private static CommandGroup cg;
	
	/**
	 * Create a Window and launch the program
	 */
	public static void main(String[] args) {
		runSimulation(); //run the simulation
		
		//create the window and launch it
		Window w = new Window();
		w.launch();
		
		//add the command group and run it
		w.addCommandGroup(cg);
		w.runAnimation();
	} //end main
	
	/**
	 * Initialize the program by creating the robot and the command group
	 */
	private static void runSimulation() {
		//create robot
		Gearbox gb = new Gearbox(12.82817, new Motor(Util.NEO), 2); //14ft/s 2 NEO gearbox each side
		Robot r = new Robot(6, 153, 30, 30, gb); //153lb 6" wheel dia 30"x30" chassis
		r.setXY(FieldPoints.AUTO_SHOT); //starting (x,y) position
		r.setHeadingDegrees(180);
		
		//set graphics parameters for drawing the robot
		Painter.robotLength = r.getLengthPixels();
		Painter.robotWidth = r.getWidthPixels();
		
		//create the feedback controllers and loop for the robot
		PIDController drivePID = new PIDController(Util.kP_DRIVE, Util.kI_DRIVE, Util.kD_DRIVE, r.getMaxLinSpeed());
		PIDController turnPID = new PIDController(Util.kP_TURN, Util.kI_TURN, Util.kD_TURN, r.getMaxLinSpeed());
		driveLoop = new DriveLoop(r, drivePID, turnPID);
		
		//create the command group
//		cg = new TrenchCycleLow();
//		cg = new TrenchCycleFast();
//		cg = new TrenchCycleLong();
//		cg = new EightCellAuto(); 
		cg = new DriveToGoalDemo();
		
		//creating a single command
//		c = new DriveDistance(driveLoop, 100, 20, 12);
//		c.run();
	} //end initialize
} //end AutoSim
