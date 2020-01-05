/**
 * EnvironmentController
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2020
 * Controller for the Environment
 */
package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import util.Util;

public class EnvironmentController implements MouseMotionListener {
	//Attributes
	Environment env;
	
	/**
	 * Create an environment controller with an environment
	 * Environment env - environment to get information from 
	 */
	public EnvironmentController(Environment env) {
		super();
		
		//set attributes
		this.env = env;
	} //end constructor

	/**
	 * Send the coordinates of the mouse to the UI bar
	 */
	public void mouseMoved(MouseEvent m) {
		env.setCursorLocation(m.getX(), m.getY());
	} //end mouseMoved

	public void mouseDragged(MouseEvent arg0) {}
} //end class
