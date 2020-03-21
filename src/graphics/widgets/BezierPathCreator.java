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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import graphics.BoxButton;
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
		this.setBorder(BorderFactory.createLineBorder(Color.RED, 10));

	}
	
	private void layoutView() {
		layoutControlPointArea();
	}
	
	private void topRow() {
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
		title.setFont(title.getFont().deriveFont((float) (AutoSim.PPI * 8)));
		title.setPreferredSize(new Dimension(width, height/10));
		
		GridBagConstraints topRowConstraint = JComponentUtil.createGBC(0, 0);
		topRowConstraint.gridwidth = 3;
		gb.setConstraints(title, topRowConstraint);
		controlPointArea.add(title);
		
		//text areas
		int y = 0;
		for (int i = 0; i < 12; i++) {
			//add to HashMap
			String key = (i % 2 == 0 ? "x" : "y") + Integer.toString(i % 6 / 2); //x0, y0, x1, ...
			JTextField textBox = JComponentUtil.textField(this.width / 6, this.height / 10);
			textBoxes.put(key, textBox);
			
			//for every two text areas, add one button
			if (i % 2 == 0) {
				y++;
//				JPanel button = new JPanel();
				BoxButton btn = new BoxButton(this.width / 5, this.height / 10, "P" + (y - 1), true, true); //P0, P1, ...
//				JTextField btn = JComponentUtil.textField(200, 200);
//				button.add(btn);
//				btn.setBorder(BorderFactory.createLineBorder(Color.black));
//				btn.setPreferredSize(btn.getSize());
				gb.setConstraints(btn, JComponentUtil.createGBC(0, y, 0.25, 1));
				btn.setBorder(BorderFactory.createEmptyBorder(AutoSim.PPI, AutoSim.PPI, AutoSim.PPI, AutoSim.PPI));
				controlPointArea.add(btn);
				
				//button not changing size, needs to be smaller
			} //if
			
			//constrain to grid (x = 1, 2, 1, 2, 1 ...)
			gb.setConstraints(textBox, JComponentUtil.createGBC(i % 2 + 1, y, 0.375, 1));
			
			//center the text
			textBox.setText(key);
			textBox.setHorizontalAlignment(SwingConstants.CENTER); //center text
			textBox.setFont(Painter.createFont(Painter.OXYGEN_FONT, AutoSim.PPI * 10));
			
			textBox.setBorder(BorderFactory.createLineBorder(Color.black, (int) (AutoSim.PPI * 0.2)));
			//extra paddding
//			textBox.setBorder(BorderFactory.createEmptyBorder(AutoSim.PPI, AutoSim.PPI, AutoSim.PPI, AutoSim.PPI));
			
			//		add controllers		//
			
			//add to panel
			controlPointArea.add(textBox);
		} //loop
		
		//add all to panel
		this.add(controlPointArea);
	} //end layoutControlPointArea
} //end class
