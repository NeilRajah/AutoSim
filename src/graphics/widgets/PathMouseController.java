/**
 * PathMouseController
 * Author: Neil Balaskandarajah
 * Created on: 09/04/2020
 * Mouse controller for creating a path
 */

package graphics.widgets;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.function.BiConsumer;

import util.Util;
import graphics.Environment;
import graphics.GraphicBezierPath;
import graphics.Painter;
import graphics.components.BoxButton;
import graphics.components.BoxButton.BUTTON_STATE;
import main.AutoSim;
import model.FieldPositioning;
import model.Point;

public class PathMouseController implements MouseListener, MouseMotionListener, MouseWheelListener {
	//Attributes
	private BiConsumer<Integer, BUTTON_STATE> circleUpdate; //method run when updating circle
	private BiConsumer<Integer, BUTTON_STATE> buttonUpdate; //method run to update button
	private GraphicBezierPath path; //path with points
	private int currentCircIndex; //index for the current circle
	
	/**
	 * Create a mouse controller with a method to update circles and the curve
	 * @param cu Method to update circles
	 * @param bu Method to update buttons
	 * @param gbp Path with circles
	 */
	public PathMouseController(BiConsumer<Integer, BUTTON_STATE> cu, BiConsumer<Integer, BUTTON_STATE> bu, 
								GraphicBezierPath gbp) {
		//set attributes
		this.circleUpdate = cu;
		this.buttonUpdate = bu;
		this.path = gbp;
		this.currentCircIndex = 0;
	} //end constructor

	/**
	 * Update the circles based on mouse position
	 * @param m Cursor information
	 */
	public void mouseMoved(MouseEvent m) {
		Point ms = getMouseCoordsInches(m);
		Circle[] circles = path.getCircles();
		
		//only search through points if over a circle
		for (int i = 0; i < circles.length; i++) {
			Circle c = circles[i]; //current circle
			//if mouse is within a circle
			boolean over = FieldPositioning.isWithinBounds(c, ms, Painter.CIRCLE_RAD / (double) AutoSim.PPI); 
			currentCircIndex = over ? i : -1;
			
			if (over) {
				//set to hover
				Environment.getInstance().setCursor(new Cursor(Cursor.HAND_CURSOR)); //hand cursor
				circleUpdate.accept(currentCircIndex, BUTTON_STATE.HOVER);
				buttonUpdate.accept(currentCircIndex, BUTTON_STATE.HOVER);

				break; //no need to loop through rest of points
				
			} else {
				//set to default
				Environment.getInstance().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); //regular cursor
				//refactor to if statement

				circleUpdate.accept(i, BUTTON_STATE.DEFAULT);
				buttonUpdate.accept(i, BUTTON_STATE.DEFAULT);
			} //if
		} //loop
	} //end mouseMoved

	/**
	 * Update the current circle on press
	 * @param m Cursor information
	 */
	public void mousePressed(MouseEvent m) {
		//if over a circle
		if (currentCircIndex != -1) {
//			path.getCircles()[currentCircIndex].setLocked();
			circleUpdate.accept(currentCircIndex, BUTTON_STATE.LOCK);
			buttonUpdate.accept(currentCircIndex, BUTTON_STATE.LOCK);
		} //if
	} //end mousePressed

	/**
	 * Update the current circle on release
	 * @param m Cursor information
	 */
	public void mouseReleased(MouseEvent m) {
		//if over a circle
		if (currentCircIndex != -1 && !path.getCircles()[currentCircIndex].isLocked()) {
			circleUpdate.accept(currentCircIndex, BUTTON_STATE.HOVER);
			buttonUpdate.accept(currentCircIndex, BUTTON_STATE.HOVER);
		} //if
	} //end mouseReleased
	
	
	public void mouseDragged(MouseEvent m) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Get the mouse coordinates in real space
	 * @param m MouseEvent with cursor information
	 * @return (x,y) mouse location in inches
	 */
	private Point getMouseCoordsInches(MouseEvent m) {
		return new Point(m.getY() / (double) AutoSim.PPI, m.getX() / (double) AutoSim.PPI);
	} //end getMouseCoordsInches
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent m) {
		
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		// TODO Auto-generated method stub

	}
	
	/*
	 * Unimplemented
	 */
	public void mouseEntered(MouseEvent m) {}

	/*
	 * Unimplemented
	 */
	public void mouseExited(MouseEvent m) {}
} //end class
