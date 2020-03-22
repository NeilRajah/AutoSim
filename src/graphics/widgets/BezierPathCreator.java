/**
 * BezierPathCreator
 * Author: Neil Balaskandarajah
 * Created on: 16/03/2020
 * A user interface component for working with Bezier Paths
 */

package graphics.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import graphics.BoxButton;
import graphics.ButtonController;
import graphics.Painter;
import main.AutoSim;
import model.motion.QuinticBezierPath;
import util.JComponentUtil;

public class BezierPathCreator extends JPanel {
	//Attributes
	private int width; //width in pixels
	private int height; //height in pixels
	private QuinticBezierPath curve; //curve being manipulated
	private HashMap<String, JTextField> textBoxes; //text boxes for control points
	
	public BezierPathCreator(int width, int height) {
		super();
		
		this.width = width;
		this.height = height;
		textBoxes = new HashMap<String, JTextField>();
		
		layoutView();
		
//		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, AutoSim.PPI * 2));
//		this.setBorder(BorderFactory.createLineBorder(Color.RED, 10));

	}
	
	private void layoutView() {
		GridBagLayout gb = new GridBagLayout();
		this.setLayout(gb);
		
		layoutTopRow();
		layoutControlPointArea();
	}
	
	private void layoutTopRow() {
		//instead of returning, can pass in JPanel the top row is to be added to
	}
	
	private void layoutControlPointArea() {		
		//panel with gridbag layout
		//add toprow
		//add panel to this
		
		//panel and layout
		JPanel controlPointArea = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		controlPointArea.setLayout(gb);
		
		//top row
		JLabel title = new JLabel("Bezier Curve Creator", SwingConstants.CENTER);
		title.setFont(Painter.createFont(Painter.SF_UI_FONT, AutoSim.PPI * 10));
		title.setPreferredSize(new Dimension(width, height/10));
		
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
			
			//for every two text areas, add one button
			if (i % 2 == 0) {
				y++; //increment row position in grid
				
				//create button
				BoxButton button = new BoxButton(this.width / 5, this.height / 10, "P" + (y - 1), true, true); //P0, P1, ...
				button.setBackgroundColor(new Color(120, 130, 140));
				gb.setConstraints(button, JComponentUtil.createGBC(0, y, 0.25, 1));
				
				//add controller
				ButtonController btnCtrl = new ButtonController(button, System.out::println); //change this
				btnCtrl.setColors(Painter.BEZ_BTN_LIGHT, Painter.BEZ_BTN_DARK);
				button.addMouseListener(btnCtrl);
				
				//add button to panel
				controlPointArea.add(button);
			} //if
			
			//constrain to grid (x = 1, 2, 1, 2, 1 ...)
			gb.setConstraints(textBox, JComponentUtil.createGBC(i % 2 + 1, y, 0.375, 1));
			
			//add textbox controller
			TextBoxController boxController = new TextBoxController(textBox);
			textBox.addKeyListener(boxController);
			textBox.addFocusListener(boxController);
			
			//add to panel
			controlPointArea.add(textBox);
		} //loop
		
		//add all to panel
		this.add(controlPointArea);
	} //end layoutControlPointArea
} //end class
