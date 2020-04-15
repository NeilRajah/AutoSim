/**
 * BezierPathCreatorWidget
 * Author: Neil Balaskandarajah
 * Created on: 17/03/2020
 * Widget for bezier path creation
 */

package graphics.widgets;

import java.io.File;
import java.util.Scanner;

import graphics.Environment;
import graphics.GraphicBezierPath;
import model.Point;
import util.FieldPoints;
import util.Util;

public class BezierPathCreatorWidget extends Widget {
	//Attributes
	private BezierPathCreator bpc; //path creator JComponent
	
	/**
	 * Create a widget containing a BezierPathCreator
	 * @param panel Panel containing the BezierPathCreator
	 */
	public BezierPathCreatorWidget(BezierPathCreator bpc) {
		super(bpc);
		
		//set attributes
		this.bpc = bpc;
		
		//add the controllers
		registerControllers();
	} //end constructor

	/**
	 * Add the key and mouse controllers
	 */
	public void registerControllers() {
		//key controller
		PathKeyController pkc = new PathKeyController(bpc);
		Environment.getInstance().addKeyListener(pkc);
		Environment.getInstance().addMouseListener(pkc);
		
		//mouse controller
		PathMouseController pmc = new PathMouseController(bpc);
		Environment.getInstance().addMouseListener(pmc);
		Environment.getInstance().addMouseMotionListener(pmc);
	} //end registerControllers
	
	/**
	 * Set the control points of the path 
	 * @param points Control points of path 
	 */
	public void setControlPoints(Point[] points) {
		bpc.setCircles(GraphicBezierPath.circlesFromPoints(points));
	} //end setControlPoints
	
	/**
	 * Set the control points of the path 
	 * @param points Control points of path 
	 */
	public void setControlPoints(double[][] points) {
		bpc.setCircles(GraphicBezierPath.circlesFromCoordinates(points));
	} //end setControlPoints
	
	/**
	 * Set the control points of the path
	 * @param filename File containing control points
	 */
	public void setControlPoints(String filename) {
		try {
			Scanner s = new Scanner(new File(filename));
			
			while (s.hasNextLine()) {
				String line = s.nextLine();
				System.out.println(line);
			}
			
			s.close();
		} catch (Exception e) {
			//message window
			Util.println("Unable to open file");
			e.printStackTrace();
			setControlPoints(FieldPoints.curve);
		}
	} //end setControlPoints
	
	/**
	 * Update the widget given update values
	 * @param values Values given from the update
	 */
	public void update(double[] values) {
	
	} //end update
} //end class
