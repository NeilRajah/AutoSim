/**
 * ControlCircleController
 * Author: Neil Balaskandarajah
 * Created on: 31/03/2020
 * Controller for changing the control circle state based on user mouse actions
 */

package graphics.widgets;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.BiConsumer;

import graphics.components.BoxButton;

public class ControlCircleController implements MouseListener {
	//Attributes
	private int key; //index of the control circle being updated
	private BiConsumer<Integer, BUTTON_STATE> updateCircle; //method that updates circle
	private boolean locked; //whether the circle is locked or not
	
	//Possible actions
	public static enum BUTTON_STATE {
		DEFAULT,
		HOVER,
		LOCK
	} //end enum
	
	/**
	 * Create a circle controller to update a button
	 * @param button Point button
	 * @param method Method to run that updates the circle
	 */
	public ControlCircleController(BoxButton button, BiConsumer<Integer, BUTTON_STATE> method) {
		//set attributes
		this.updateCircle = method;
		
		//get key from button text
		this.key = Integer.parseInt(button.getText().substring(1)); //P0 -> 0, P1 -> 1, ...
		
		//start with the button not locked
		this.locked = false;
	} //end constructor

	/**
	 * Set the state of the button to be locked or hovered based on user clicks
	 */
	public void mouseClicked(MouseEvent m) {
		locked = !locked;
		
		if (locked)
			updateCircle.accept(key, BUTTON_STATE.LOCK);
		else
			updateCircle.accept(key, BUTTON_STATE.HOVER);
	} //end mouseClicked

	/**
	 * Set the control circle to hover
	 */
	public void mouseEntered(MouseEvent m) {
		if (!locked)
			updateCircle.accept(key, BUTTON_STATE.HOVER);
	} //end mouseEntered

	/**
	 * Set the control circle to default
	 */
	public void mouseExited(MouseEvent m) {
		if (!locked)
			updateCircle.accept(key, BUTTON_STATE.DEFAULT);		
	} //end mouseExited

	/*
	 * Unimplemented
	 */
	public void mousePressed(MouseEvent m) {}

	/*
	 * Unimplemented
	 */
	public void mouseReleased(MouseEvent m) {}
} //end class
