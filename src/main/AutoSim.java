/**
 * AutoSim
 * Author: Neil Balaskandarajah
 * Created on: 08/11/2019
 * Simulates the motion of a differential drive robot and its control algorithms
 */

package main;

import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import commands.CommandGroup;
import commands.routines.DriveToGoalDemo;
import graphics.Painter;
import graphics.Window;
import graphics.widgets.BezierPathCreator;
import graphics.widgets.BezierPathCreatorWidget;
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
	public static int SCREEN_WIDTH; 
	public static int SCREEN_HEIGHT;
	
	//Pixels Per Inch (ppi), used for scaling to different screen resolutions
	public static int PPI;
	
	//Whether the application is being displayed to the top screen
	public static boolean TOP_SCREEN;
	
	//DriveLoop instance to be controlled (change to getInstance()?)
	public static DriveLoop driveLoop;
	
	//CommandGroup to be run
	private static CommandGroup cg;
	
	/**
	 * Create a Window and launch the program
	 */
	public static void main(String[] args) {
		//initialize the program
		initializeScreen();
		initializeSimulation(); 
		
		//create the window 
		w = new Window(true); //true for debug, false for not
		addWidgets(); //add widgets to the widget hub
		
		//add the command group
		w.addCommandGroup(cg);
		
		//launch the application
		w.launch();
	} //end main
	
	/**
	 * Set the constants related to the screen
	 */
	private static void initializeScreen() {
		int screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
		//set the scaling constants
		if (screens > 1) {
			//monitor is 1080p
			SCREEN_WIDTH = 1920;
			SCREEN_HEIGHT = 1080;
			TOP_SCREEN = true;
		} else {
			//get monitor resolution
			SCREEN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width);
			SCREEN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height);
			TOP_SCREEN = false;
			//always chooses primary monitor's resolution
		} //if
		
		//5 pixels per inch on a 3840x2160 screen
		PPI = (int) Math.floor(5.0 * (SCREEN_WIDTH/3840.0));
	} //end initializeScreen
	
	/**
	 * Initialize the program by creating the robot and the command group
	 */
	private static void initializeSimulation() {
		//create robot
		Gearbox gb = new Gearbox(12.82817, new Motor(Util.NEO), 2); //14ft/s 2 NEO gearbox each side
		Robot r = new Robot(6, 153, 30, 30, gb); //153lb 6" wheel dia 30"x30" chassis
		
		//set graphics parameters for drawing the robot
		Painter.ROBOT_LENGTH = r.getLengthPixels();
		Painter.ROBOT_WIDTH = r.getWidthPixels();
		
		//create the feedback controllers and loop for the robot
		PIDController drivePID = new PIDController(Util.kP_DRIVE, Util.kI_DRIVE, Util.kD_DRIVE, r.getMaxLinSpeed());
		PIDController turnPID = new PIDController(Util.kP_TURN, Util.kI_TURN, Util.kD_TURN, r.getMaxLinSpeed());
		driveLoop = new DriveLoop(r, drivePID, turnPID);
		
		//create the command group
		cg = new DriveToGoalDemo();
//		cg = new CommandList(new DriveDistance(driveLoop, 100, 1, r.getMaxLinSpeed()));
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
		
		BezierPathCreatorWidget bezWidg = new BezierPathCreatorWidget(new BezierPathCreator(w.getHubWidth(), w.getHubHeight() * 1/2));
		w.addWidget(bezWidg);
	} //end addWidgets
} //end AutoSim
