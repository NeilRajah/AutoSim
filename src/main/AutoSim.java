/**
 * AutoSim
 * Author: Neil Balaskandarajah
 * Created on: 08/11/2019
 * Simulates the motion of a differential drive robot and its control algorithms
 */

package main;

import java.awt.Toolkit;

import graphics.Window;
import model.Gearbox;
import model.Motor;
import model.Robot;
import util.Util;

public class AutoSim {
	//Constants
	//Screen dimensions in pixels for scaling
	public static final int SCREEN_WIDTH = (int) (Toolkit.getDefaultToolkit().getScreenSize().width);
	public static final int SCREEN_HEIGHT = (int) (Toolkit.getDefaultToolkit().getScreenSize().height);
	
	//Pixels Per Inch (ppi), used for scaling to different screen resolutions
	public static final int ppi = (int) Math.ceil(5.0 * (SCREEN_WIDTH/3840.0));
	
	/**
	 * Create a Window and launch the program
	 */
	public static void main(String[] args) {
		Gearbox gb = new Gearbox(8.5521, new Motor(Util.NEO), 2);
		Robot r = new Robot(4, 153, 30, 30, gb);
		
		Window w = new Window();
		w.addRobot(r);
		w.launch();
	} //end main

} //end AutoSim
