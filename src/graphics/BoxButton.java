package graphics;
/*
 * BoxButton
 * Author: Neil Balaskandarajah
 * Created on: 21/03/2020
 * A custom and more aesthetically pleasing button/text box
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;

import javax.swing.JComponent;

import main.AutoSim;
import util.Util;

public class BoxButton extends JComponent {
	//Attributes
	private String text; //text for the box
	
	private Color backgroundColor; //box background color
	private Color textColor; //text color
	private Font f; //Font for text
	private float fontSize; //font size in pixels
	private int cornerRad; //radius of the corners in pixels
	private boolean border; //whether OBOX has border
	
	/*
	 * Create an OBox with a width, height and text inside
	 * int width - width of the box
	 * int height - height of the box
	 * String text - text in the box
	 */
	public BoxButton(int width, int height, String text) {
		super();
		
		this.text = text;
		
		//set the size of the box
		this.setPreferredSize(new Dimension(width, height));
		
		//set the font size
		fontSize = Util.FONT_SIZE;	
		
		//regular Oxygen font
		f = Util.getFileFont(Painter.OXYGEN_FONT);
		
		//set the corner radius and colors
		cornerRad = AutoSim.PPI * 10;		
		backgroundColor = Color.BLACK;
		textColor = Color.WHITE;
	} //end constructor
	
	/*
	 * Create an OBox with a width, height and text inside
	 * int width - width of the box
	 * int height - height of the box
	 * String text - text in the box
	 * boolean straight - whether the box has straight corners or not
	 * boolean border - whether the box has a border or not
	 */
	public BoxButton(int width, int height, String text, boolean straight, boolean border) {
		this(width, height, text);
		
		if (straight) {
			cornerRad = 0;
		} else {
			cornerRad = 60;
		} //if
	} //end constructor
	
	/*
	 * Draw the box to the screen
	 * Graphics g - object responsible for drawing 
	 */
	public void paintComponent(Graphics g) {
		//draw border
		int wall;
		if (border) {
			g.setColor(backgroundColor.darker());
			g.fillRoundRect(AutoSim.PPI, 0, this.getWidth(), this.getHeight(), cornerRad, cornerRad);
			wall = AutoSim.PPI;
		} else {
			wall = 0;
		} //if
		
		//draw the inside
		g.setColor(backgroundColor);
		g.fillRoundRect(AutoSim.PPI, wall, this.getWidth(), this.getHeight(), cornerRad, cornerRad);
		
		//set the text color
		g.setColor(textColor);

		//set the font
		g.setFont(f.deriveFont(fontSize));
		FontMetrics fm = g.getFontMetrics();
		
		//set the font to use antialiasing or not
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		//draw the text centered on the button
		g.drawString(text, (this.getWidth() / 2) - (fm.stringWidth(text) / 2), 
							(this.getHeight() / 2) + (fm.getAscent() / 2) - (int) (Util.FONT_SIZE * 0.12));
	} //end paintComponent
	
	/*
	 * Set the background color of the box
	 * Color c - background color for the box
	 */
	public void setBackgroundColor(Color c) {
		backgroundColor = c;
	} //end setBackgroundColor

	/*
	 * Get the text to the box
	 * return text - text of the box
	 */
	public String getText() {
		return text;
	} //end getText
	
	/*
	 * Set the text to the box
	 * String text - text to set the box to
	 */
	public void setText(String text) {
		this.text = text;
	} //end setText
	
	/*
	 * Update the box
	 */
	public void update() {
		repaint();
	} //end update
	
	/*
	 * Set the font size of the box
	 * float fontSize - new font size
	 */
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	} //end setFontSize
	
	/*
	 * Set the corner radius of the box
	 * int cornerRad - corner radius
	 */
	public void setCornerRadius(int cornerRad) {
		this.cornerRad = cornerRad;
	} //end setCornerRadius

	/*
	 * Set the text color of the box
	 * Color color - new text color
	 */
	public void setTextColor(Color color) {
		this.textColor = color;
	} //end setTextColor
} //end class