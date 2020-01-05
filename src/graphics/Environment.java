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

import main.AutoSim;
import model.Point;
import model.Pose;
import util.FieldPoints;

public class Environment extends JComponent implements Component {
	//Attributes
	private int width; //width of the environment
	private int height; //height of the environment
	
	private BufferedImage field; //field image
	private UIBar bar; //user interface bar to update
	
	private ArrayList<Pose> poses;
	private int poseIndex;
	
	/**
	 * The environment the robot is simulated in
	 * int width - width of the component
	 * int height - height of the component
	 */
	public Environment(int width, int height) {
		super();
		
		//set the width and height of the component
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		//open field image
		try {
			field = ImageIO.read(getClass().getResource("/resources/2020Field.png"));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} //try-catch
		
		//reset values
		poseIndex = 0;
	} //end constructor
	
	//Pose
	
	public void addPoses(ArrayList<Pose> poses) {
		this.poses = poses;
	}
	
	public void incrementPoseIndex() {
		poseIndex++;
		repaint();
	}
	
	public void resetPoseIndex() {
		poseIndex = 0;
	}
	
	public void setPoseIndex(int index) {
		poseIndex = index;
	}
	
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
		g2.drawImage(field, 0, 0, null);
		
//		move origin to bottom left
//		g2.scale(1.0, 1.0);
//		g2.translate(0, 0);
		
		//stroke for lines
		g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		
//		g2.setColor(Color.blue);
//		g2.fillOval((int) (AutoSim.ppi * FieldPoints.MID_INITIATION.getY()), 
//					(int) (AutoSim.ppi * FieldPoints.MID_INITIATION.getX()), 50, 50);
//
//		g2.fillOval((int) (AutoSim.ppi * FieldPoints.GOAL_TEST.getY()), 
//					(int) (AutoSim.ppi * FieldPoints.GOAL_TEST.getX()), 50, 50);

		//draw all robots
//		for (Robot r : robots) {
//			Painter.drawRobot(g2, r); //draw the robot
//		} //loop
		if (poses != null) {
			Painter.drawPose(g2, poses.get(poseIndex));
		}
//		Painter.drawPose(g2, new Pose(FieldPoints.MID_INITIATION, Math.PI/2, Color.GREEN));
	} //end paintComponent
	
	//User Interaction
	
	/**
	 * Add a UI bar
	 * UIBar bar - bar to store for the environment to update
	 */
	public void addUIBar(UIBar bar) {
		this.bar = bar;
	} //end addUIBar
	
	/**
	 * Set the mouse coordinates for the UI to draw
	 * int x - x location of the mouse
	 * int y - y location of the mouse
	 */
	public void setCursorLocation(int x, int y) {
		bar.setCursorLocation(x, y);
	} //end setCursorLocation
} //end Environment
