/**
 * BezierPathCreatorWidget
 * Author: Neil Balaskandarajah
 * Created on: 17/03/2020
 * Widget for bezier path creation
 */

package graphics.widgets;

import model.FieldPositioning;
import model.Point;

public class BezierPathCreatorWidget extends Widget {
	//Attributes
	private BezierPathCreator bpc;
	
	/**
	 * Create a widget containing a BezierPathCreator
	 * @param panel Panel containing the BezierPathCreator
	 */
	public BezierPathCreatorWidget(BezierPathCreator bpc) {
		super(bpc);
		
		this.bpc = bpc;
	} //end constructor

	/**
	 * Set the control points of the path 
	 * @param points Control points of path 
	 */
	public void setControlPoints(Point[] points) {
		bpc.setControlPoints(points);
	} //end setControlPoints
	
	/**
	 * Set the control points of the path 
	 * @param points Control points of path 
	 */
	public void setControlPoints(double[][] points) {
		bpc.setControlPoints(FieldPositioning.pointsFromDoubles(points));
	} //end setControlPoints
	
	/**
	 * Update the widget given update values
	 * @param values Values given from the update
	 */
	public void update(double[] values) {
	
	} //end update

} //end class
