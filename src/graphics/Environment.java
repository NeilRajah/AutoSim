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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import model.Robot;

public class Environment extends JComponent implements Component {
	//Attributes
	private int width; //width of the environment
	private int height; //height of the environment
	private ArrayList<Robot> robots; //robots
	
	//Constants
	private BufferedImage FIELD; //field image
	
	public Environment(int width, int height) {
		super();
		
		//set the width and height of the component
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		//open field image
		try {
			FIELD = ImageIO.read(getClass().getResource("/resources/2019Field.jpeg"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} //try-catch
		
		//initialize list of robots
		robots = new ArrayList<Robot>();
	} //end constructor
	
	//Robot
	/**
	 * Add a robot to the list
	 * Robot r - robot to add to the list
	 */
	public void addRobot(Robot r) {
		robots.add(r);
	} //end addRobot
	
	//Graphics
	
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
	
	/**
	 * Draw the environment
	 * Graphics g - responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; //Graphics2D for better graphics
	    
		//white background, will be replaced by field or grid
//		g2.setColor(Color.white);
//		g2.fillRect(0, 0, width, height);
		g2.drawImage(FIELD, 0, 0, null);
		
		//move origin to bottom left
		g2.scale(1.0, -1.0);
		g2.translate(0, -height);
		
		//stroke for lines
		g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));

		//draw elements
		for (Robot r : robots) {
			Painter.drawRobot(g2, r); //draw the robot
		}
	} //end paintComponent
} //end Environment
