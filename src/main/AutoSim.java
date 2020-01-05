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
import loops.DriveLoop;
import loops.EightCellAuto;
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
	public static final int ppi = (int) Math.ceil(5.0 * (SCREEN_WIDTH/3840.0));
	
	//Command
	public static DriveLoop driveLoop;
	private static Command c;
	private static CommandGroup cg;
	
	/**
	 * Create a Window and launch the program
	 */
	public static void main(String[] args) {
		initialize();
		
		Window w = new Window();
//		w.addRobot(r);
		w.addPoses(cg.getPoses());
		w.launch();
		
		Runnable loop = () -> {
			System.out.println("pausing");
			Util.pause(1000);
			System.out.println("running");
			for (int i = 1; i < cg.getPoses().size(); i++) {
				w.incrementPoseIndex();
				Util.pause((int) (Util.UPDATE_PERIOD * 1000));
			}
			System.out.println("ran");
		};
		
		Thread t = new Thread(loop);
		t.start();
	} //end main
	
	private static void initialize() {
		Gearbox gb = new Gearbox(12.82817, new Motor(Util.NEO), 2);
		Robot r = new Robot(6, 153, 30, 30, gb);
		r.setXY(FieldPoints.AUTO_SHOT);
		Painter.length = r.getLengthPixels();
		Painter.width = r.getWidthPixels();
		
		PIDController drivePID = new PIDController(Util.kP_DRIVE, Util.kI_DRIVE, Util.kD_DRIVE, r.getMaxLinSpeed());
		PIDController turnPID = new PIDController(Util.kP_TURN, Util.kI_TURN, Util.kD_TURN, r.getMaxLinSpeed());
		driveLoop = new DriveLoop(r, drivePID, turnPID);
		
//		c = new DriveDistance(driveLoop, 100, 1, 12);
//		c = new TurnAngle(driveLoop, 45, 0.5, 12);
		
//		c.run();
		
//		cg = new TrenchCycleLow();
//		cg = new TrenchCycleFast();
//		cg = new TrenchCycleLong();
		cg = new EightCellAuto();
		cg.start();
	}
} //end AutoSim
