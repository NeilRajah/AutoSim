/**
 * Environment
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * The environment the robot acts in
 */
package graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

import main.AutoSim;
import model.Point;
import model.Pose;
import model.motion.QuinticBezierPath;
import util.Util;
import util.Util.ROBOT_KEY;

public class Environment extends JComponent {
	//Attributes
	//Singleton Instance
	private static Environment mInstance; //single instance of the Environment
	
	//Configured
	private int width; //width of the environment
	private int height; //height of the environment
	
	//Elements
	private Image field; //field image
	private UIBar bar; //user interface bar to update
	
	//Updated
	private ArrayList<Pose> poses; //list of robot poses to draw
	private ArrayList<HashMap<ROBOT_KEY, Object>> data; //data from the robot
	private int poseIndex; //index in pose list of pose to draw
	private int curveIndex; //index in curve list of curve to draw
	private boolean debug; //whether to display the field or not
	private boolean simulating; //true when the animation is running
	
	//Curves
	private ArrayList<int[][]> curves; //points for the bezier path
	private Point[] controlPoints; //control points for path	
	
	/**
	 * The environment the robot is simulated in
	 */
	private Environment() {
		super();
		
		//reset values
		debug = false;
		poseIndex = -1;
		curveIndex = 0;
		simulating = false;
		
		//add border
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, AutoSim.PPI * 2));
	} //end constructor
	
	/**
	 * Get the single instance of the Environment
	 * @return Single instance of the environment
	 */
	public static Environment getInstance() {
		if (mInstance == null)
			mInstance = new Environment();
		
		return mInstance;
	} //end getInstance
	
	/**
	 * Set the size of the Environment
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void setSize(int width, int height) {
		//set the width and height of the component
		mInstance.width = width;
		mInstance.height = height;
		mInstance.setPreferredSize(new Dimension(width, height));
		
		//open field image
		try {
			field = ImageIO.read(getClass().getResource("/resources/2020Field.png"));
			
			//scale the image for the machine's screen resolution
			field = field.getScaledInstance(width, height, BufferedImage.SCALE_FAST); 
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		} //try-catch
	} //end setSize
	
	//Simulation
	
	/**
	 * Set the status of the simulation
	 * @param isSimulating Whether or not the simulation is running
	 */
	public void setSimulating(boolean isSimulating) {
		this.simulating = isSimulating;
	} //end setSimulating
	
	//Pose
	
	/**
	 * Add poses to be later drawn
	 * @param poses - list of poses to be drawn
	 */
	public void setPoses(ArrayList<Pose> poses) {
		this.poses = poses;
	} //end addPoses
	
	/**
	 * Get the number of poses in the environment
	 * @return - number of poses in the poses list if the list is not null
	 */
	public int getNumPoses() {
		if (poses != null) {
			return poses.size();
		} //if
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
	 * @param paths Bezier curve to be followed
	 */
	public void setCurves(ArrayList<int[][]> paths) {
		curves = paths;
	} //end setCurve
	
	/**
	 * Increment the curve index by one and repaint the component
	 */
	public void incrementCurveIndex() {
		curveIndex++;
		bar.setTime(poseIndex);
		repaint();
	} //end incrementPoseIndex
	
	/**
	 * Set the curve to be drawn (intended for widget use)
	 * @param path Curve to be drawn
	 */
	public void setPath(QuinticBezierPath path) {
		//clear the list (if it isn't null) and add the curve
		if (curves != null)
			curves.clear();
		else
			curves = new ArrayList<int[][]>();
		
		//add the points and update the environment
		curves.add(path.getPolyline());
		this.controlPoints = path.getControlPoints();
		update();
		Util.println(curves.size());
	} //end setCurve
	
	//Data
	
	/**
	 * Set the data for the simulation
	 * @param data list of robot data points at each timestamp
	 */
	public void setData(ArrayList<HashMap<ROBOT_KEY, Object>> data) {
		this.data = data;
	} //end setData
	
	/**
	 * Get the data point at an index
	 * @param index list index to get data point at
	 * @return data point at index
	 */
	public HashMap<ROBOT_KEY, Object> getDataPoint(int index) {
		return data.get(index);
	} //end getDataPoint
	
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
			
			g2.setColor(Color.black);
			Painter.drawGrid(g2, width, height, 12 * AutoSim.PPI); //12 inches
		} //if
		g2.setStroke(new BasicStroke((float) (AutoSim.PPI * 2), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		
		//draw the current path
		if (curves != null && !curves.isEmpty()) {
			//stroke for lines
			g2 = (Graphics2D) g;
			g2.setColor(Color.WHITE);
			
			//draw the curve
			g2.drawPolyline(curves.get(curveIndex)[0], curves.get(curveIndex)[1], curves.get(curveIndex)[0].length);
			
			//draw the control points
			if (curves.size() == 1) {	
				//control points
				g2.setColor(new Color(230, 230, 230));
				for (int i = 0; i < 6; i++) {
					Painter.drawPoint(g2, controlPoints[i]);
				} //loop
				
				//tangents
				Painter.setTransparency(g2, 0.8);
				Painter.drawLine(g2, controlPoints[0], controlPoints[1]);
				Painter.drawLine(g2, controlPoints[4], controlPoints[5]);
				Painter.setTransparency(g2, 1.0);
			} //if
		} else {
			Util.println("didn't draw curve");
			Util.println(curves.size());
		}

		//draw the current pose
		AffineTransform oldTransform = g2.getTransform();
		if (poses != null && !poses.isEmpty()) { //if the pose is not null or empty
			Painter.drawPose(g2, poses.get(poseIndex));
		} //if
		g2.setTransform(oldTransform);
		
		//draw the goal point if not the first step
		if (poseIndex > 0 && (data.get(poseIndex).get(ROBOT_KEY.GOAL_POINT) != null)) {
			//drawing values
			g2.setColor(Color.GRAY);
			Point goal = (Point) data.get(poseIndex).get(ROBOT_KEY.GOAL_POINT);
			Point robot = poses.get(poseIndex).getPoint();
			
			//points to draw and line between them
			Painter.drawPoint(g2, goal);
			Painter.drawPoint(g2, robot);
			Painter.drawLine(g2, goal, robot);
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
