/**
 * PathMouseController
 * Author: Neil Balaskandarajah
 * Created on: 09/04/2020
 * Mouse controller for creating a path
 */

package graphics.widgets;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.function.BiConsumer;

import graphics.GraphicBezierPath;
import graphics.Painter;
import graphics.components.BoxButton;
import main.AutoSim;
import model.FieldPositioning;
import model.Point;
import util.Util;

public class PathMouseController implements MouseListener, MouseMotionListener {
	//Attributes
	private BiConsumer<Integer, BoxButton.BUTTON_STATE> circleUpdate; //method run when updating circle
	private GraphicBezierPath path; //path with points
	
	/**
	 * Create a mouse controller with a method to update circles and the curve
	 * @param bc Method to update circles
	 * @param gbp Path with circles
	 */
	public PathMouseController(BiConsumer<Integer, BoxButton.BUTTON_STATE> bc, GraphicBezierPath gbp) {
		//set attributes
		this.circleUpdate = bc;
		this.path = gbp;
	} //end constructor

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Update the circles based on mouse position
	 * MouseEvent m Cursor information
	 */
	public void mouseMoved(MouseEvent m) {
		Point ms = getMouseCoordsInches(m);
		Circle[] circles = path.getCircles();
		
		for (int i = 0; i < circles.length; i++) {
			if (FieldPositioning.isWithinBounds(circles[i], ms, Painter.CIRCLE_RAD / (double) AutoSim.PPI)) {
				circles[i].setHovered();
			} else {
				circles[i].setDefault();
			} //if
		} //loop
	} //end mouseMoved

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
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
} //end class
