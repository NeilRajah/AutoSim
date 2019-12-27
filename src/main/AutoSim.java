package main;

import java.awt.Toolkit;

import graphics.Window;

/**
 * AutoSim
 * Author: Neil Balaskandarajah
 * Created on: 08/11/2019
 * Simulates the motion of a differential drive robot and its control algorithms
 */
public class AutoSim {
	//Constants
	//Screen dimensions in pixels for scaling
	public static final int screenWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width);
	public static final int screenHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().height);
	public static final double scaleFactor = screenWidth/3840.0;
	
	//Pixels Per Inch (ppi), used for scaling to different screen resolutions
	public static final int ppi = (int) Math.ceil(5.0 * (screenWidth/3840.0));
	
	//Field dimensions (inches)
	public static final int fieldWidth = 324;
	public static final int fieldHeight = 324;
	
	/**
	 * Create a Window and launch the program
	 */
	public static void main(String[] args) {
		System.setProperty("sun.java2d.uiScale", "1.0");
		System.out.println(Toolkit.getDefaultToolkit().getScreenResolution());
		
		//will later make choosing between vertical and horizontal an option from Java icon
		boolean vertical = false; 
		
		if (vertical) { //portrait orientation
			double goldenRatio = 1.61803398875;
			int windowHeight = (int) (screenHeight * 0.8);
			Window w = new Window((int) (windowHeight/goldenRatio), windowHeight);
			w.launch();
			
		} else { //landscape orientation
			Window w = new Window(screenWidth/2, screenHeight/2);
			w.launch();
		}
	} //end main

} //end AutoSim
