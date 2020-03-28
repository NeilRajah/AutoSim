/**
 * BezierPathCreator
 * Author: Neil Balaskandarajah
 * Created on: 16/03/2020
 * A user interface component for working with Bezier Paths
 */

package graphics.widgets;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import graphics.Environment;
import graphics.Painter;
import graphics.components.BezierTextController;
import graphics.components.BoxButton;
import graphics.components.ButtonController;
import main.AutoSim;
import model.Point;
import model.motion.QuinticBezierPath;
import util.JComponentUtil;
import util.Util;

public class BezierPathCreator extends JPanel {
	//Attributes
	private int width; //width in pixels
	private int height; //height in pixels
	private QuinticBezierPath curve; //curve being manipulated
	private HashMap<String, JTextField> textBoxes; //text boxes for control points
	
	/**
	 * Create a BezierPathCreator with a width and a height
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public BezierPathCreator(int width, int height) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		textBoxes = new HashMap<String, JTextField>();
		this.curve = new QuinticBezierPath(QuinticBezierPath.FAST_RES);
		
		//layout all components
		layoutView();
	} //end constructor
	
	/**
	 * Lay out the view of the BezierPathCreator
	 */
	private void layoutView() {
		//set the layout for the entire panel
		GridBagLayout gb = new GridBagLayout();
		this.setLayout(gb);
		
		//layout each of the components
		//layoutTopRow();
		layoutControlPointArea();
		
		//update curve points
		updateControlPoints();
	} //end layoutView
	
	/**
	 * Layout the area containing all the control points
	 */
	private void layoutControlPointArea() {	
		//panel and layout
		JPanel controlPointArea = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		controlPointArea.setLayout(gb);
		
		//top row
		JLabel title = new JLabel("Bezier Curve Creator", SwingConstants.CENTER);
		title.setFont(Painter.createFont(Painter.SF_UI_FONT, AutoSim.PPI * 10));
		title.setPreferredSize(new Dimension(width, height/10));
		
		//constraint for the label
		GridBagConstraints topRowConstraint = JComponentUtil.createGBC(0, 0);
		topRowConstraint.gridwidth = 3;
		gb.setConstraints(title, topRowConstraint);
		controlPointArea.add(title);
		
		//add buttons and text boxes
		int y = 0;
		for (int i = 0; i < 12; i++) {
			//create textbox and add to HashMap
			String key = (i % 2 == 0 ? "x" : "y") + Integer.toString(i/2 % 6); //x0, y0, x1, ...
			JTextField textBox = JComponentUtil.textField(key, this.width / 6, this.height / 10, AutoSim.PPI * 4);
			textBoxes.put(key, textBox); //add to map
			
			//add textbox controller
			BezierTextController boxController = new BezierTextController(textBox, this);
			textBox.addKeyListener(boxController);
			textBox.addFocusListener(boxController);

			textBox.setText(Integer.toString(i * 10)); //CHANGE to load from file (if file exist, demo path, etc.)
			
			//for every two text areas, add one button
			if (i % 2 == 0) {
				y++; //increment row position in grid
				
				//create button
				BoxButton button = new BoxButton(this.width / 5, this.height / 10, "P" + (y - 1), true, true); //P0, P1, ...
				gb.setConstraints(button, JComponentUtil.createGBC(0, y, 0.25, 1));
				
				//add controller
				ButtonController btnCtrl = new ButtonController(button, System.out::println); //change this to locking
				btnCtrl.setColors(Painter.BEZ_BTN_LIGHT, Painter.BEZ_BTN_DARK);
				button.addMouseListener(btnCtrl);
				
				//add button to panel
				controlPointArea.add(button);
			} //if
			
			//constrain to grid (x = 1, 2, 1, 2, 1 ...)
			gb.setConstraints(textBox, JComponentUtil.createGBC(i % 2 + 1, y, 0.375, 1));
			
			//add to panel
			controlPointArea.add(textBox);
		} //loop
		
		//add all to panel
		this.add(controlPointArea);
	} //end layoutControlPointArea
	
	/**
	 * Set a coordinate value in the curvecurve
	 * @param key Key designating the value to be changed
	 * @param value Value to update
	 */
	public void setCurveCoordinate(String key, double value) {
		//set the coordinate
		curve.setCoordinate(key, value);
		
		//send to environment
		if (allBoxesValid()) {
			Environment.getInstance().setPath(curve);
		} //if
	} //end setCurveCoordinate
	
	/**
	 * Update the control points of the curve
	 */
	public void updateControlPoints() {
		Iterator<Entry<String, JTextField>> it = textBoxes.entrySet().iterator(); //for looping through map
		int loops = 0; //number of loops
		double x = 0; //x value of point
		double y = 0; //y value of point
		Point[] points = new Point[6]; //new control point array
		
		//iterates in y,x,y,x,y ... pattern
		while (it.hasNext()) {
			loops++;
			
			//get entry
			Map.Entry<String, JTextField> entry = (Entry<String, JTextField>) it.next();	
			
			//set x and y values
			if (entry.getKey().contains("x")) {
				x = Double.parseDouble(entry.getValue().getText());
				
			} else if (entry.getKey().contains("y")) {
				y = Double.parseDouble(entry.getValue().getText());
			} //if
			
			//set the point
			if (loops % 2 == 0) {
				points[loops / 2 - 1] = new Point(x, y);
			} //if
		} //while
		
		//set the control points and the curve
		curve.setControlPoints(points);
		Environment.getInstance().setPath(curve);
	} //end updateControlPoints
	
	/**
	 * Loop through all the boxes, checking if they are all valid
	 * @return If all the boxes are non-empty
	 */
	private boolean allBoxesValid() {
		Iterator<Entry<String, JTextField>> it = textBoxes.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry<String, JTextField> entry = (Entry<String, JTextField>) it.next();
			
			if (entry.getValue().getText().equals("")) {
				return false;
			} //if
		} //else
		
		return true;
	} //end allBoxesValid
	
	/**
	 * Set the control points to the boxes
	 * @param controlPoints Points for the text boxes to show
	 */
	public void setControlPoints(Point[] controlPoints) {
		Iterator<Entry<String, JTextField>> it = textBoxes.entrySet().iterator(); //for looping through map
		int loops = 0; //number of elements visited
		
		//iterates in y,x,y,x,y ... pattern
		while (it.hasNext()) {
			//get entry
			Map.Entry<String, JTextField> entry = (Entry<String, JTextField>) it.next();	
			
			//set x and y values of control points to textbox
			if (entry.getKey().contains("x")) {
				entry.getValue().setText(Double.toString(controlPoints[loops/2].getX()));
				
			} else if (entry.getKey().contains("y")) {
				entry.getValue().setText(Double.toString(controlPoints[loops/2].getY()));
			} //if

			loops++;
		} //while
		

		//set the control points and the curve
		curve.setControlPoints(controlPoints);
		Environment.getInstance().setPath(curve);
	} //end setControlPoints
} //end class
