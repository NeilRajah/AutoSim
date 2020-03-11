/*
 * SpeedDisplay
 * Created by: Neil Balaskandarajah
 * Created on: 03/02/2020
 * A simple display of an object's speed
 */
package graphics.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

import util.Util.WIDGET_ID;

public class SpeedDisplay extends JComponent {
	//Attributes
	private JComponent display; //JComponent for the widget
	private double speed; //speed of object
	private double maxSpeed; //speed cap
	private Color color; //color of the slider
	
	/*
	 * Create a new speed display with a width, height and max speed
	 * int width - width of the component in pixels
	 * int height - height of the component in pixels
	 * int maxSpeed - max speed of the object in units
	 */
	public SpeedDisplay(int width, int height, double maxSpeed) {
		this.setPreferredSize(new Dimension(width, height));
		this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.maxSpeed = maxSpeed;
		
		this.color = Color.GREEN;
	} //end constructor
	
	/*
	 * Set the speed of the object and redraw the component
	 * double newSpeed - new speed of the object
	 */
	public void update(double newSpeed) {
		speed = Math.abs(newSpeed);
		repaint();
	} //end setSpeed
	
	/*
	 * Set the color of the display
	 * Color clr - color to set the velocity slider of the display
	 */
	public void setColor(Color clr) {
		this.color = clr;
	} //end setColor
	
	/*
	 * Visually represent the speed of the object
	 * Graphics g - Java AWT object responsible of drawing the component
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		int drawHeight = this.getHeight() - (int) ((speed/maxSpeed) * this.getHeight());
		
		//background
		g2.setBackground(color);
		g2.clearRect(0, 0, this.getWidth(), this.getHeight());
		
		//clear
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, this.getWidth(), drawHeight);
	} //end paintComponent
} //end class
