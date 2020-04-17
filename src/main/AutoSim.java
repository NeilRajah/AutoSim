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

import org.knowm.xchart.XYChart;

import commands.CommandGroup;
import commands.CommandList;
import commands.DriveClosedLoopLinearProfile;
import commands.DriveDistance;
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
import model.motion.TrapezoidalProfile;
import util.PlotGenerator;
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
	
	//Trajectory to follow
	private static TrapezoidalProfile trap;
	
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
		
		//add the command group and plot data
		w.addCommandGroup(cg);
		new Thread(AutoSim::plotData).run(); //run in parallel to speed things up
		
		//launch the application
		w.launch();
			
	} //end main
	
	/**
	 * Set the constants related to the screen
	 */
	private static void initializeScreen() {
		int screens = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
//		screens = 0;
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
		Gearbox gb = new Gearbox(Gearbox.ratioFromTopSpeed(Util.NEO, 4, 12), new Motor(Util.NEO), 2); //12ft/s 4 NEO
		Robot r = new Robot(4, 120, 30, 30, gb); //120lb 4" wheel dia 30"x30" chassis
		r.setXY(new Point(30,30));
		
		//set graphics parameters for drawing the robot
		Painter.ROBOT_LENGTH = r.getLengthPixels();
		Painter.ROBOT_WIDTH = r.getWidthPixels();
		
		//create the feedback controllers and loop for the robot
		PIDController drivePID = new PIDController(Util.kP_DRIVE, Util.kI_DRIVE, Util.kD_DRIVE, r.getMaxLinSpeed());
		PIDController turnPID = new PIDController(Util.kP_TURN, Util.kI_TURN, Util.kD_TURN, r.getMaxLinSpeed());
		driveLoop = new DriveLoop(r, drivePID, turnPID);
		driveLoop.setFFValues(Util.kV_EMPIR, Util.kA_EMPIR); //need better values
		
		//create the command group
		trap = new TrapezoidalProfile(150, 30, 12);
		cg = new CommandList(new DriveClosedLoopLinearProfile(driveLoop, trap, 1)); 
//		cg = new CommandList(new DriveDistance(driveLoop, 100, 1, 12));
	} //end initialize
	
	/**
	 * Add Widgets to the Widget Hub
	 */
	private static void addWidgets() {
		//Linear speed display
		SpeedDisplayWidget linSpd = new SpeedDisplayWidget(new SpeedDisplay(w.getHubWidth(), 
			w.getHubHeight() * 1/8, driveLoop.getRobot().getMaxLinSpeed()), ROBOT_KEY.LIN_VEL);
		linSpd.setColors(Color.GREEN, Color.RED);
		w.addWidget(linSpd);		
		
		//Angular speed display
		SpeedDisplayWidget angSpd = new SpeedDisplayWidget(new SpeedDisplay(w.getHubWidth(), 
			w.getHubHeight() * 1/8, driveLoop.getRobot().getMaxAngSpeed()), ROBOT_KEY.ANG_VEL);
		angSpd.setColors(Color.ORANGE, Color.BLUE);
		w.addWidget(angSpd);
		
		//bezier path creator widget
//		BezierPathCreatorWidget bezWidg = new BezierPathCreatorWidget(new BezierPathCreator(w.getHubWidth(), w.getHubHeight() * 1/2));
//		bezWidg.setControlPoints("src//main//niceLongCurve.crv");
//		w.addWidget(bezWidg);
	} //end addWidgets

	/**
	 * Plot the robot data to a separate window
	 */
	private static void plotData() {
		XYChart chart = PlotGenerator.createLinearTrajChart(trap, "Profile vs. Robot", 1920, 1080);
		
		double[][] robotSeries = PlotGenerator.getXYFromRobotData(cg.getData(), ROBOT_KEY.LIN_VEL);
		chart.addSeries("Robot", robotSeries[0], robotSeries[1]);
		PlotGenerator.displayChart(chart);
	} //end plotData
} //end AutoSim
