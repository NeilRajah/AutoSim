/**
 * Environment
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * The environment the robot acts in
 */
package graphics;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import model.Pose;

public class Environment extends JComponent implements Component {
	//Attributes
	//Configured
	private int width; //width of the environment
	private int height; //height of the environment
	
	//Elements
	private BufferedImage field; //field image
	private UIBar bar; //user interface bar to update
	
	//Updated
	private ArrayList<Pose> poses; //list of robot poses to draw
	private int poseIndex; //index in pose list of pose to draw
	
	/**
	 * The environment the robot is simulated in
	 * @param width - width of the component
	 * @param height - height of the component
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
	
	/**
	 * Add poses to be later drawn
	 * @param poses - list of poses to be drawn
	 */
	public void addPoses(ArrayList<Pose> poses) {
		this.poses = poses;
	} //end addPoses
	
	/**
	 * Increment the pose index by one and repaint the component
	 */
	public void incrementPoseIndex() {
		poseIndex++;
		repaint();
	} //end incrementPoseIndex
	
	/**
	 * Set the pose index to zero
	 */
	public void resetPoseIndex() {
		poseIndex = 0;
	} //end resetPoseIndex
	
	/**
	 * Set the pose index to a specified value
	 * @param index - index to set pose index to
	 */
	public void setPoseIndex(int index) {
		poseIndex = index;
	} //end setPoseIndex
	
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
	 * Draw the environment
	 * @param g - responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; //Graphics2D for better graphics
	    
		//draw the field image as the background
		g2.drawImage(field, 0, 0, null);
		
		//stroke for lines
		g2.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		
		//draw the current pose
		if (poses != null && !poses.isEmpty()) { //if the pose is not null or empty
			Painter.drawPose(g2, poses.get(poseIndex));
		} //if
	} //end paintComponent
	
	//User Interaction
	
	/**
	 * Add a UI bar
	 * @param bar - bar to store for the environment to update
	 */
	public void addUIBar(UIBar bar) {
		this.bar = bar;
	} //end addUIBar
	
	/**
	 * Set the mouse coordinates for the UI to draw
	 * @param x - x location of the mouse
	 * @param y - y location of the mouse
	 */
	public void setBarCursorLocation(int x, int y) {
		bar.setCursorLocation(x, y);
	} //end setBarCursorLocation
} //end Environment
