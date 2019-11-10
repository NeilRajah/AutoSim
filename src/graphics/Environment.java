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
	
	public double theta = 0;
	
	public Environment(int width, int height) {
		super();
		
		//set the width and height of the component
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
		repaint();
	} //end update
	
	public void setTheta(double newTheta) {
		theta = newTheta;
		update();
	}
	
	/**
	 * Draw the environment
	 * Graphics g - responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
	    
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));


		g2.translate(this.width/2, this.height/2);
		g2.rotate(theta);

		Painter.drawRobot(g2);
	} //end paintComponent
} //end Environment
