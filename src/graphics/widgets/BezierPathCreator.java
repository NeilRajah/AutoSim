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
import graphics.GraphicBezierPath;
import graphics.Painter;
import graphics.components.BezierTextController;
import graphics.components.BoxButton;
import graphics.components.BoxButton.BUTTON_STATE;
import graphics.components.LockButtonController;
import main.AutoSim;
import util.JComponentUtil;

public class BezierPathCreator extends JPanel {
	//Attributes
	private int width; //width in pixels
	private int height; //height in pixels
	private GraphicBezierPath curve; //curve being manipulated
	private HashMap<String, JTextField> textBoxes; //text boxes for control points
	private BoxButton[] buttons; //buttons for control points
	
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
		this.textBoxes = new HashMap<String, JTextField>();
		this.buttons = new BoxButton[6];
		this.curve = new GraphicBezierPath();
		
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
			
			//for every two text areas, add one button
			if (i % 2 == 0) {
				y++; //increment row position in grid
				
				//bellow can be refactored into one method
				
				//create button and add to array
				BoxButton button = new BoxButton(this.width / 5, this.height / 10, "P" + (y - 1), true, true); //P0, P1, ...
				button.setColors(Painter.BEZ_BTN_LIGHT, Painter.BEZ_BTN_DARK);
				buttons[y-1] = button;
				gb.setConstraints(button, JComponentUtil.createGBC(0, y, 0.25, 1));
				
				//add graphics controller
				LockButtonController btnCtrl = new LockButtonController(button, this::requestToggle); 
				button.addMouseListener(btnCtrl);
				
				//add locking controller
				ControlCircleController circCtrl = new ControlCircleController(button, this::updateCircle);
				button.addMouseListener(circCtrl);
				
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
	 * Update a circle in the curve
	 * @param key Index of the circle in the array
	 * @param state State of the button
	 */
	public void updateCircle(int key, BoxButton.BUTTON_STATE state) {
		switch (state) {
			case DEFAULT:
				curve.setCircleDefault(key);
				break;
				
			case HOVER:
				curve.setCircleHover(key);
				break;
				
			case LOCK:
				curve.unlockAllCircles();
				curve.lockCircle(key);
				break;
		} //switch-case
	} //end updateCircle
	
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
		Circle[] circles = new Circle[6]; //new control point array
		
		//iterates in y,x,y,x,y ... pattern
		while (it.hasNext()) {
			loops++;
			
			//get entry
			Map.Entry<String, JTextField> entry = (Entry<String, JTextField>) it.next();	
			
			//catch formatting errors
			try {
				//set x and y values
				if (entry.getKey().contains("x")) {
					x = Double.parseDouble(entry.getValue().getText());
					
				} else if (entry.getKey().contains("y")) {
					y = Double.parseDouble(entry.getValue().getText());
				} //if
				
			} catch (NumberFormatException e) {
				x = 0;
				y = 0;
			} //try-catch
			
			//set the point
			if (loops % 2 == 0) {
				circles[loops / 2 - 1] = new Circle(x, y, Painter.BEZ_BTN_DARK);
			} //if
		} //while
		
		//set the control circles and the curve
		curve.setCircles(circles);
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
	 * @param circles Circles for the text boxes to show
	 */
	public void setCircles(Circle[] circles) {
		Iterator<Entry<String, JTextField>> it = textBoxes.entrySet().iterator(); //for looping through map
		int loops = 0; //number of elements visited
		
		//iterates in y,x,y,x,y ... pattern
		while (it.hasNext()) {
			//get entry
			Map.Entry<String, JTextField> entry = (Entry<String, JTextField>) it.next();	
			
			//set x and y values of control points to the textboxes
			if (entry.getKey().contains("x")) {
				entry.getValue().setText(Double.toString(circles[loops/2].getX()));
				
			} else if (entry.getKey().contains("y")) {
				entry.getValue().setText(Double.toString(circles[loops/2].getY()));
			} //if

			loops++;
		} //while
		
		//set the control circles and the curve
		curve.setCircles(circles);
		Environment.getInstance().setPath(curve);
	} //end setControlPoints
	
	/**
	 * Request a toggle for a button
	 * @param index Index of button in buttons array
	 */
	private void requestToggle(int index) {
		int lockIndex = getLockedButton();
		
		if (lockIndex == -1) { //no buttons locked
			//lock this
			buttons[index].setState(BUTTON_STATE.LOCK); 
		
		} else if (lockIndex != index) { //another button is locked
			//lock this, unlock that
			buttons[lockIndex].setState(BUTTON_STATE.DEFAULT);
			buttons[index].setState(BUTTON_STATE.LOCK); 
			
		} else if (lockIndex == index) { //this button is locked
 			buttons[index].setState(BUTTON_STATE.DEFAULT);
		} //if
	} //end requestToggle
	
	/**
	 * Get the index of the locked button
	 * @return index of locked button, -1 if none are locked
	 */
	private int getLockedButton() {
		int lockIndex = -1; //index of locked button
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].isLocked())
				lockIndex = i;
		} //loop
		
		return lockIndex;
	} //end getLockedButton
	
	/**
	 * Translate a circle in the x and y directions
	 * @param dx Change in x position
	 * @param dy Change in y position
	 */
	public void moveCircle(double dx, double dy) {
		int l = getLockedButton();
		
		//move the circle and update the textboxes
		if (l != -1) {
			curve.moveCircle(l, dx, dy);
			textBoxes.get("x".concat(Integer.toString(l))).setText(
					 	  Double.toString(curve.getCircles()[l].getX()));
			textBoxes.get("y".concat(Integer.toString(l))).setText(
				 	  Double.toString(curve.getCircles()[l].getY()));
		} //if
		
		updateControlPoints();
	} //end movePoint
} //end class
