/**
 * GraphicBezierPath
 * Author: Neil Balaskandarajah
 * Created on: 31/03/2020
 * Bezier path for graphical features
 */

package graphics;

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
	 * Return the path's circles
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
	
} //end class
