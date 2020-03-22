/**
 * Painter
 * @author Neil Balaskandarajah
 * Created on: 10/11/2019
 * Holds all drawing methods for any graphics
 */
package graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;

import main.AutoSim;
import model.Point;
import model.Pose;

public class Painter {
	//Painting constants
	public static int ROBOT_LENGTH = 0; //length of robot in pixels
	public static int ROBOT_WIDTH = 0; //width of robot in pixels
	public static final int CORNER_RAD = AutoSim.PPI * 6; //corner radius
	
	//Fonts
	public static final String SF_UI_FONT = "src/resources/SF-UI-Display-Light.ttf";
	public static final String OXYGEN_FONT = "src/resources/Oxygen-Regular.ttf";
	
	//Colors
	public static final Color BEZ_BTN_LIGHT = new Color(120, 130, 140);
	public static final Color BEZ_BTN_DARK = new Color(110, 120, 130);
	
	/**
	 * Draw a string to the screen
	 * @param g2 - used for drawing
	 * @param str - string to draw
	 * @param x - x position to draw to
	 * @param y - y position to draw to
	 */
	public static void drawFlippedString(Graphics2D g2, String str, int x, int y) {
		g2.scale(1.0, -1.0);
		g2.drawString(str, x, -y);
		g2.scale(1.0, -1.0);
	} //end drawString
	
	/**
	 * Draw a robot pose to the screen
	 * @param g2 object for drawing
	 * @param p pose to draw
	 */
	public static void drawPose(Graphics2D g2, Pose p) {
		
		//translate to center of robot, robot to its heading
		g2.translate(p.getPoint().getY()*AutoSim.PPI, p.getPoint().getX()*AutoSim.PPI);
		g2.rotate(p.getHeading());
		
		//draw body of robot
		g2.setColor(p.getColor());
		g2.fillRoundRect(-ROBOT_LENGTH/2, -ROBOT_WIDTH/2, ROBOT_LENGTH, ROBOT_WIDTH, CORNER_RAD, CORNER_RAD);
		
		//draw back-end indicator (straight box so only round edges at front)
		g2.fillRoundRect(-ROBOT_LENGTH/2, -ROBOT_WIDTH/2, CORNER_RAD, ROBOT_WIDTH, CORNER_RAD/4, CORNER_RAD/4);
		
//		g2.translate(-p.getPoint().getY()*AutoSim.ppi, -p.getPoint().getX()*AutoSim.ppi);
//		g2.rotate(-p.getHeading());
//		g2.dispose();
//		g2 = originalG2;
	} //end drawPose
	
	/**
	 * Draw the grid to the environment
	 * @param g2 object for drawing
	 * @param width width of the environment
	 * @param height height of the environment
	 * @param step increments for the grid lines
	 */
	public static void drawGrid(Graphics2D g2, int width, int height, int step) {
		//draw vertical lines
		for (int x = step; x < width; x += step) {
			g2.drawLine(x, 0, x, height);
		} //loop
		
		//draw horizontal lines
		for (int y = step; y < height; y += step) {
			g2.drawLine(0, y, width, y);
		} //loop
	} //end drawGrid
	
	public static void drawPoint(Graphics2D g2, Point p, int rad) {
		rad *= AutoSim.PPI;
		int x = (int) (p.getX() * AutoSim.PPI);
		int y = (int) (p.getY() * AutoSim.PPI);
		
		g2.fillOval((int) (y - rad/2.0), (int) (x - rad/2.0), rad, rad);
	} //end drawPoint
	
	public static void drawLine(Graphics2D g2, Point start, Point end) {
		int x1 = (int) (start.getX() * AutoSim.PPI);
		int y1 = (int) (start.getY() * AutoSim.PPI);
		int x2 = (int) (end.getX() * AutoSim.PPI);
		int y2 = (int) (end.getY() * AutoSim.PPI);
		
		g2.drawLine(y1, x1, y2, x2);
	} //end drawLine
	
	/**
	 * Set the font of a Graphics2D object
	 * @param g2 Used for drawing
	 * @param filename Directory of the font file
	 * @param fontSize Value to scale pixels per inch by
	 */
	public static Font createFont(String filename, int fontSize) {
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, fontSize); //default font
		
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File(filename));
			f = f.deriveFont((float) fontSize);
		} catch (Exception e) {} //try-catch
		
		return f;
	} //end createFont
} //end Painter 
