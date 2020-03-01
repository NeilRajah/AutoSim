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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import model.Pose;
import model.motion.QuinticBezierPath;

public class Environment extends JComponent implements Component {
	//Attributes
	//Configured
	private int width; //width of the environment
	private int height; //height of the environment
	
	//Elements
	private Image field; //field image
	private UIBar bar; //user interface bar to update
	
	//Updated
	private ArrayList<Pose> poses; //list of robot poses to draw
	private ArrayList<int[][]> curves; //points for the bezier path
	private int poseIndex; //index in pose list of pose to draw
	private int curveIndex; //index in curve list of curve to draw
	
	//Debug
	private boolean debug; //whether to display the field or not
	
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
			
			//scale the image for the machine's screen resolution
			field = field.getScaledInstance(width, height, BufferedImage.SCALE_FAST); 
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} //try-catch
		debug = false;
		
		//reset values
		poseIndex = 0;
		curveIndex = 0;
		
//		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
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
	 * Get the number of poses in the environment
	 * @return - number of poses in the poses list if the list is not null
	 */
	public int getNumPoses() {
		if (poses != null) {
			return poses.size();
		} 
		return 0;
	} //end getNumPoses
	
	/**
	 * Increment the pose index by one and repaint the component
	 */
	public void incrementPoseIndex() {
		poseIndex++;
		bar.setTime(poseIndex);
		repaint();
	} //end incrementPoseIndex
	
	/**
	 * Set the pose index to a specified value
	 * @param index - index to set pose index to
	 */
	public void setPoseIndex(int index) {
		poseIndex = index;
	} //end setPoseIndex
	
	//Curve
	
	/**
	 * Add curve to be used for graphics
	 * @param qbp - Bezier curve to be followed
	 */
	public void setCurves(ArrayList<int[][]> qbp) {
		curves = qbp;
	} //end setCurve
	
	/**
	 * Increment the curve index by one and repaint the component
	 */
	public void incrementCurveIndex() {
		curveIndex++;
		bar.setTime(poseIndex);
		repaint();
	} //end incrementPoseIndex
	
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
	 * Set the debug mode of the Environment (do not draw field if debug mode)
	 */
	public void setDebug() {
		debug = true;
	} //end setDebug
	
	/**
	 * Draw the environment
	 * @param g - responsible for drawing
	 */
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; //Graphics2D for better graphics
	    
		//draw the field image as the background
		if (!debug) {
			g2.drawImage(field, 0, 0, null);
		} else {
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(0, 0, width, height);
		} //if
		
		//stroke for lines
		g2.setStroke(new BasicStroke(10.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
		g2.setColor(Color.RED);
		
		//draw the current path
		if (curves != null) {
			g2.drawPolyline(curves.get(curveIndex)[0], curves.get(curveIndex)[1], QuinticBezierPath.RESOLUTION);
		} //if
		
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
