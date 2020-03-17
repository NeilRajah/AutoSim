/**
 * BezierPathCreator
 * Author: Neil Balaskandarajah
 * Created on: 16/03/2020
 * A user interface component for working with Bezier Paths
 */

package graphics.widgets;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import model.motion.QuinticBezierPath;
import util.JComponentFactory;

public class BezierPathCreator extends JPanel {
	//Attributes
	private int width; //width in pixels
	private int height; //height in pixels
	private QuinticBezierPath curve; //curve being manipulated
	private HashMap<String, JTextArea> textBoxes; //text boxes for control points
	
	public BezierPathCreator(int width, int height) {
		super();
		
		this.width = width;
		this.height = height;
		
		layoutView();
		
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
	}
	
	private void layoutView() {
		layoutControlPointArea();
	}
	
	private JPanel layoutTopRow() {
		JPanel topRow = JComponentFactory.boxPanel(true);
		
		return topRow;
	}
	
	private void layoutControlPointArea() {
		//loop with modulus to give names
		//i % 2 == 0 -> x, == 1 -> y; n = (i % 6) + 1; loop 12 times
		
		//panel with gridbag layout
		//add toprow
		//add panel to this
	}
} //end class
