/**
 * EnvironmentKeyController
 * Author: Neil Balaskandarajah
 * Created on: 28/03/2020
 * Controller for the Environment to update a BezierPathCreator widget
 */

package graphics.widgets;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.function.BiConsumer;

public class EnvironmentKeyController implements KeyListener {	
	//Constants
	private final double DELTA = 2.0; //change in inches
	
	//Attributes
	private BiConsumer<Double, Double> keyUpdate; //method run on key update
	private double dx = 0; //change in x in inches
	private double dy = 0; //change in y in inches
	
	/**
	 * Create a path controller
	 * @param key
	 */
	public EnvironmentKeyController(BiConsumer<Double, Double> key) {
		//set attributes
		this.keyUpdate = key;
		
	} //end constructor
	
	/**
	 * Move the locked circle in the direction of the key press
	 */
	public void keyPressed(KeyEvent k) {
		if (k.getKeyCode() == KeyEvent.VK_LEFT) {
			dy = -DELTA;
			
		} else if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
			dy = DELTA;
		} //if
			
		if (k.getKeyCode() == KeyEvent.VK_UP) {
			dx = -DELTA;
			
		} else if (k.getKeyCode() == KeyEvent.VK_DOWN) {
			dx = DELTA;
		} //if
		
		keyUpdate.accept(dx, dy);
	} //end keyPressed

	/**
	 * Reset the deltas
	 */
	public void keyReleased(KeyEvent k) {
		if (k.getKeyCode() == KeyEvent.VK_LEFT || k.getKeyCode() == KeyEvent.VK_RIGHT) {
			dy = 0;
		} //if
			
		if (k.getKeyCode() == KeyEvent.VK_UP || k.getKeyCode() == KeyEvent.VK_DOWN) {
			dx = 0;
		} //if
	} //end keyReleased

	/*
	 * Unimplemented
	 */
	public void keyTyped(KeyEvent k) {}
} //end class
