/**
 * GraphicBezierPath
 * Author: Neil Balaskandarajah
 * Created on: 31/03/2020
 * Bezier path for graphical features
 */

package graphics;

import graphics.components.BoxButton.BUTTON_STATE;
import graphics.widgets.Circle;
import model.Point;
import model.motion.BezierPath;

public class GraphicBezierPath extends BezierPath {
	//Attributes
	private Circle[] circles; //control points
	
	/**
	 * Create a bezier path with circles
	 * @param circles Control points as circles
	 */
	public GraphicBezierPath(Circle[] circles) {
		super(circles, BezierPath.FAST_RES);
		
		this.circles = circles;
	} //end constructor
	
	/**
	 * Create a blank path with high resolution
	 */
	public GraphicBezierPath() {
		super(BezierPath.FAST_RES);
	} //end constructor
	
	/**
	 * Create an array of Circles from an array of Points
	 * @param points Points to make the circles from
	 * @return Array of Circles with a default color
	 */
	public static Circle[] circlesFromPoints(Point[] points) {
		Circle[] circles = new Circle[6];
		
		for (int i = 0; i < 6; i++) {
			circles[i] = new Circle(points[i], Painter.BEZ_BTN_DARK);
		} //loop
		
		return circles;
	} //end circlesFromPoints
	
	/**
	 * Create an array of Circles from an array of Points
	 * @param coords (x,y) values to make the circles from
	 * @return Array of Circles with a default color
	 */
	public static Circle[] circlesFromCoordinates(double[][] coords) {
		Circle[] circles = new Circle[6];
		
		for (int i = 0; i < 6; i++) {
			circles[i] = new Circle(coords[i][0], coords[i][1], Painter.BEZ_BTN_DARK);
		} //loop
		
		return circles;
	} //end circlesFromCoordinates

	/**
	 * Return the path's circles
	 * @return Circles representing the (x,y) points
	 */
	public Circle[] getCircles() {
		return circles;
	} //end getCircles
	
	/**
	 * Set the circles for the curve
	 * @param circles Circle control points of the curve
	 */
	public void setCircles(Circle[] circles) {
		this.circles = circles;
		this.setControlPoints(circles);
	} //end setCircles
	
	/**
	 * Set a circle in the array to be hovered over
	 * @param i Index of the circle
	 */
	public void setCircleHover(int i) {
		circles[i].setHovered();
	} //end setCircleHover
	
	/**
	 * Set a circle in the array to be locked
	 * @param i Index of the circle
	 */
	public void lockCircle(int i) {
		circles[i].setLocked();
	} //end lockCircle
	
	/**
	 * Set a circle in the array to be returned to default
	 * @param i Index of the circle
	 */
	public void setCircleDefault(int i) {
		circles[i].setDefault();
	} //end setCircleDefault
	
	/**
	 * Check if a circle is locke in the curve
	 * @return Whether a control circle is locked or not
	 */
	public boolean circleIsLocked() {
		for (int i = 0; i < circles.length; i++) {
			if (circles[i].isLocked()) {
				return true;
			} //if
		} //loop
		
		return false;
	} //end circleIsLocked
	
	/**
	 * Unlock all circles in the curve
	 */
	public void unlockAllCircles() {
		for (int i = 0; i < circles.length; i++) {
			circles[i].setDefault();
		} //loop
	} //end unlockAllCircles
	
	/**
	 * Translate a circle in the x and y directions
	 * @param i Index of the circle in the array
	 * @param dx Change in x position
	 * @param dy Change in y position
	 */
	public void moveCircle(int i, double dx, double dy) {
		circles[i].setX(circles[i].getX() + dx);
		circles[i].setY(circles[i].getY() + dy);
	} //end moveCircle

	/**
	 * Get the index of the locked circle
	 * @return index of locked circle, -1 if none are locked
	 */
	private int getLockedCircleIndex() {
		//extend Lockable interface/extend Lockable for Circles, buttons (share common methods)
		
		int lockIndex = -1; //index of locked circle
		for (int i = 0; i < circles.length; i++) {
			if (circles[i].isLocked())
				lockIndex = i;
		} //loop
		
		return lockIndex;
	} //end getLockedButton
	
	/**
	 * Request to lock a circle
	 * @param index Index of the circle requested
	 */
	public void requestCircleLock(int index) {
		int lockIndex = getLockedCircleIndex();
		
		if (lockIndex == -1) { //no buttons locked
			//lock this
			circles[index].setState(BUTTON_STATE.LOCK); 
		
		} else if (lockIndex != index) { //another button is locked
			//lock this, unlock that
			circles[lockIndex].setState(BUTTON_STATE.DEFAULT);
			circles[index].setState(BUTTON_STATE.LOCK); 
			
		} else if (lockIndex == index) { //this button is locked
			circles[index].setState(BUTTON_STATE.DEFAULT);
		} //if
	} //end requestCircleLock
} //end class
