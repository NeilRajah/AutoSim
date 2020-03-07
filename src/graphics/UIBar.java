/**
 * UIBar
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2020
 * Bar on bottom of window to help user interact with program 
 */
package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import main.AutoSim;
import util.Util;

public class UIBar extends JComponent {
	//Attributes
	//Configured
	private int width; //component width
	private int height; //component height
	
	//Updated
	private String cursorLoc; //x,y location of cursor in inches
	private String time; //time from start in seconds
	private float fontSize; //font size
	
	/**
	 * Create a bar for user interaction
	 * @param width - width of the component
	 * @param height - height of the component
	 */
	public UIBar(int width, int height) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		//update constants
		fontSize = (float) (height / 1.75);
		cursorLoc = "(x,y): 0 0";
		time = "Time: 0.000";
		
//		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
		
		//draw default information
		repaint();
	} //end constructor

	//Graphics

	/**
	 * Return the width of the environment
	 * @return width - width of component in pixels
	 */
	public int width() {
		return width;
	} //end width

	/**
	 * Return the height of the environment
	 * @return height - height of component in pixels
	 */
	public int height() {
		return height;		
	} //end height
	
	/**
	 * Update the environment
	 */
	public void update() {	
		repaint();
	} //end update
	
	/**
	 * Update the time since the animation started
	 * @param index - number of frames elapsed in animation
	 */
	public void setTime(int index) {
		//each frame is one update period long
		time = String.format("Time: %.3f", index * Util.UPDATE_PERIOD); 	
		repaint();
	} //end setTime
	
	/**
	 * Update the cursor location
	 * @param x - x position of cursor
	 * @param y - y position of cursor
	 */
	public void setCursorLocation(int x, int y) {
		//x and y values in inches
		double xVal = (double) y / AutoSim.ppi;
		double yVal = (double) x / AutoSim.ppi;
		
		cursorLoc = String.format("(x,y): %.1f %.1f", xVal, yVal);
		repaint();
	} //end setCursorLocation
	
	/**
	 * Draw the information to the bar
	 * @param g - used for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; //g2 for better drawing
		
		setFont(g2); //set the font
		
		//move origin to bottom left
		g2.scale(1.0, -1.0);
		g2.translate(0, -height);
		
		//set the background
		g2.setColor(Color.white);
		g2.fillRect(0, 0, width, height);
		
		//draw text
		g2.setColor(Color.black);
		Painter.drawFlippedString(g2, cursorLoc, 0, height/10);
		Painter.drawFlippedString(g2, time, 1000, height/10);
	} //end paintComponent
	
	/**
	 * Set the font of the component
	 * @param g2 - used for drawing
	 */
	private void setFont(Graphics2D g2) {
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, (int) fontSize); //default font
		
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/SF-UI-Display-Light.ttf"));
			f = f.deriveFont(fontSize);
		} catch (Exception e) {} //try-catch
		
		g2.setFont(f);
	} //end setFont
} //end class