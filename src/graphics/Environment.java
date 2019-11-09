/**
 * Environment
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * The environment the robot acts in
 */
package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class Environment extends JComponent implements Component {
	//Attributes
	private int width; //width of the environment
	private int height; //height of the environment
	
	public Environment(int width, int height) {
		super();
		
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
	} //end constructor
	
	@Override
	/**
	 * Return the width of the environment
	 */
	public int width() {
		return width;
	} //end width

	@Override
	/**
	 * Return the height of the environment
	 */
	public int height() {
		return height;		
	} //end height

	@Override
	/**
	 * Update the environment
	 */
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Draw the environment
	 * Graphics g - responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(10));
		
		g2.drawLine(0, 0, width, height);
	} //end paintComponent

} //end Environment
