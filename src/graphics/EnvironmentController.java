/**
 * EnvironmentController
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2020
 * Controller for the Environment
 */
package graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class EnvironmentController implements MouseMotionListener {
	//Attributes
	private Environment env; //Environment instance to update
	
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
	 * @param m - MouseEvent generated by Swing
	 */
	public void mouseMoved(MouseEvent m) {
		env.setBarCursorLocation(m.getX(), m.getY());
	} //end mouseMoved

	//Required by interface
	public void mouseDragged(MouseEvent arg0) {}
} //end class
