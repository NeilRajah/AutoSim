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
	public static int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	public static int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	public static void main(String[] args) {
		Window w = new Window(width/2, height/2);
		w.launch();
	} //end main

} //end AutoSim
