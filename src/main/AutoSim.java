/**
 * AutoSim
 * Author: Neil Balaskandarajah
 * Created on: 08/11/2019
 * Simulates the motion of a differential drive robot and its control algorithms
 */

package main;

import java.awt.Color;
import java.awt.Toolkit;

import commands.CommandGroup;
import commands.routines.EightCellAuto;
import graphics.Painter;
import graphics.Window;
import graphics.widgets.SpeedDisplay;
import graphics.widgets.SpeedDisplayWidget;
import model.DriveLoop;
import model.Gearbox;
import model.Motor;
import model.PIDController;
import model.Point;
import model.Robot;
import util.Util;
import util.Util.ROBOT_KEY;

public class AutoSim {
	//Constants
	private static Window w; //window to add components to
	
	//Screen dimensions in pixels for scaling
	public static final int SCREEN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width);
	public static final int SCREEN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height);
	
	//Pixels Per Inch (ppi), used for scaling to different screen resolutions
	public static final int ppi = (int) Math.floor(5.0 * (SCREEN_WIDTH/3840.0));
	
	//Command
	public static DriveLoop driveLoop;
	private static CommandGroup cg;
	
	/**
	 * Create a Window and launch the program
	 */
	public static void main(String[] args) {
		initialize(); //run the simulation
		
		//create the window and launch it
		w = new Window();
//		w.setDebug();
		addWidgets(); //add widgets to the widget hub
		w.launch();
		
		//add the command group and run it
		w.addCommandGroup(cg);		
		w.runAnimation();
	} //end main
	
	/**
	 * Initialize the program by creating the robot and the command group
	 */
	private static void initialize() {
		//create robot
		Gearbox gb = new Gearbox(12.82817, new Motor(Util.NEO), 2); //14ft/s 2 NEO gearbox each side
		Robot r = new Robot(6, 153, 30, 30, gb); //153lb 6" wheel dia 30"x30" chassis
		r.setXY(new Point(100,100));
		
		//set graphics parameters for drawing the robot
		Painter.robotLength = r.getLengthPixels();
		Painter.robotWidth = r.getWidthPixels();
		
		//create the feedback controllers and loop for the robot
		PIDController drivePID = new PIDController(Util.kP_DRIVE, Util.kI_DRIVE, Util.kD_DRIVE, r.getMaxLinSpeed());
		PIDController turnPID = new PIDController(Util.kP_TURN, Util.kI_TURN, Util.kD_TURN, r.getMaxLinSpeed());
		driveLoop = new DriveLoop(r, drivePID, turnPID);
		
		//create the command group
		cg = new EightCellAuto();
//		cg = new DriveToGoalDemo();
//		cg = new DriveToGoalTest();
//		cg = new CommandList(new DriveDistance(driveLoop, 100, 1, 12));
//		cg = new CommandList(new DriveToGoal(driveLoop, new Point(200,200), 1, 12, 0, false));
//		cg = new CommandList(new TimedVoltage(driveLoop, r.getMaxLinSpeed() * 0.5, 10));
//		cg = new CommandList(new MoveQuintic(driveLoop, FieldPoints.curve2));
//		cg = new CommandList(new Wait(driveLoop, 0.25));
		
		Util.println("Initialized");
	} //end initialize
	
	/**
	 * Add Widgets to the Widget Hub
	 */
	private static void addWidgets() {
		//Linear speed display
		SpeedDisplayWidget linSpd = new SpeedDisplayWidget(new SpeedDisplay(w.getHubWidth(), 
				w.getHubHeight() * 1/8, driveLoop.getRobot().getMaxLinSpeed()), ROBOT_KEY.LIN_VEL);
		linSpd.setColor(Color.GREEN);
		w.addWidget(linSpd);
		
		//Angular speed display
		SpeedDisplayWidget angSpd = new SpeedDisplayWidget(new SpeedDisplay(w.getHubWidth(), 
				w.getHubHeight() * 1/8, driveLoop.getRobot().getMaxAngSpeed()), ROBOT_KEY.ANG_VEL);
		angSpd.setColor(Color.ORANGE);
		w.addWidget(angSpd);
	} //end addWidgets
} //end AutoSim
