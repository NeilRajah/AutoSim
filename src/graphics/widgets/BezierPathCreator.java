/**
 * BezierPathCreator
 * Author: Neil Balaskandarajah
 * Created on: 16/03/2020
 * A user interface component for working with Bezier Paths
 */

package graphics.widgets;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import graphics.BoxButton;
import main.AutoSim;
import model.motion.QuinticBezierPath;
import util.JComponentUtil;

public class BezierPathCreator extends JPanel {
	//Attributes
	private int width; //width in pixels
	private int height; //height in pixels
	private QuinticBezierPath curve; //curve being manipulated
	private HashMap<String, JTextPane> textBoxes; //text boxes for control points
	
	public BezierPathCreator(int width, int height) {
		super();
		
		this.width = width;
		this.height = height;
		textBoxes = new HashMap<String, JTextPane>();
		
		layoutView();
		
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, AutoSim.PPI * 2));
	}
	
	private void layoutView() {
		layoutControlPointArea();
	}
	
	private JPanel topRow() {
		//instead of returning, can pass in JPanel the top row is to be added to
		JPanel topRow = JComponentUtil.boxPanel(true);
		
		JLabel title = new JLabel("Bezier Curve Creator");
		title.setFont(title.getFont().deriveFont((float) (AutoSim.PPI * 8)));
		topRow.add(title);
		
		return topRow;
	}
	
	private void layoutControlPointArea() {
		//loop with modulus to give names
		//i % 2 == 0 -> x, == 1 -> y; n = (i % 6) + 1; loop 12 times
		
		//panel with gridbag layout
		//add toprow
		//add panel to this
		
		//panel and layout
		JPanel controlPointArea = new JPanel();
		GridBagLayout gb = new GridBagLayout();
		controlPointArea.setLayout(gb);
		
		//top row
		JPanel topRow = topRow();
		gb.setConstraints(topRow, JComponentUtil.createGBC(0, 0));
		controlPointArea.add(topRow);
		
		//text areas
		int y = 0;
		for (int i = 0; i < 12; i++) {
			//add to HashMap
			String key = (i % 2 == 0 ? "x" : "y") + Integer.toString(i % 6 / 2); //x0, y0, x1, ...
			JTextPane textBox = JComponentUtil.textPane(this.width / 6, this.height / 10);
			textBoxes.put(key, textBox);
			
			//change y height for the buttons (every two text areas, add a button)
			if (i % 2 == 0) {
				y++;
//				JPanel button = new JPanel();
				BoxButton btn = new BoxButton(this.width / 6, this.height / 10, "P" + i % 6, true, true); //P0, P1, ...
//				button.add(btn);
//				btn.setBorder(BorderFactory.createLineBorder(Color.black));
				btn.setPreferredSize(btn.getSize());
				gb.setConstraints(btn, JComponentUtil.createGBC(0, y));
				btn.setBorder(BorderFactory.createEmptyBorder(AutoSim.PPI, AutoSim.PPI, AutoSim.PPI, AutoSim.PPI));
				controlPointArea.add(btn);
			} //if
			
			//constrain to grid (x = 1, 2, 1, 2, 1 ...)
			gb.setConstraints(textBox, JComponentUtil.createGBC(i % 2 + 1, y));
			
			//center the text
			textBox.setText(key);
			SimpleAttributeSet attribs = new SimpleAttributeSet();
			StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
			textBox.setParagraphAttributes(attribs, true);
			
//			textBox.setBorder(BorderFactory.createLineBorder(Color.black));
			//extra paddding
			textBox.setBorder(BorderFactory.createEmptyBorder(AutoSim.PPI, AutoSim.PPI, AutoSim.PPI, AutoSim.PPI));
			
			//	add controller	//
			
			//add to panel
			controlPointArea.add(textBox);
		} //loop
		
		//add all to panel
		this.add(controlPointArea);
	}
} //end class
