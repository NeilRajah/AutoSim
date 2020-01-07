/**
 * Painter
 * @author Neil Balaskandarajah
 * Created on: 10/11/2019
 * Holds all drawing methods for any graphics
 */
package graphics;

import java.awt.Graphics2D;

import main.AutoSim;
import model.Pose;

public class Painter {
	//Values
	public static int robotLength = 0; //length of robot in pixels
	public static int robotWidth = 0; //width of robot in pixels
	public static int cornerRad = AutoSim.ppi * 6; //corner radius
	
	/**
	 * Draw a string to the screen
	 * @param g2 - used for drawing
	 * @param str - string to draw
	 * @param x - x position to draw to
	 * @param y - y position to draw to
	 */
	public static void drawString(Graphics2D g2, String str, int x, int y) {
		g2.drawString(str, x, -y);
	} //end drawString
	
	/**
	 * Draw a robot pose to the screen
	 * @param g2 - object for drawing
	 * @param p - pose to draw
	 */
	public static void drawPose(Graphics2D g2, Pose p) {
		//translate to center of robot, robot to its heading
		g2.translate(p.getPoint().getY()*AutoSim.ppi, p.getPoint().getX()*AutoSim.ppi);
		g2.rotate(p.getHeading());
		
		//draw body of robot
		g2.setColor(p.getColor());
		g2.fillRoundRect(-robotLength/2, -robotWidth/2, robotLength, robotWidth, cornerRad, cornerRad);
		
		//draw back-end indicator (straight box so only round edges at front)
		g2.fillRoundRect(-robotLength/2, -robotWidth/2, cornerRad, robotWidth, cornerRad/4, cornerRad/4);
	} //end drawPose
} //end Painter 
