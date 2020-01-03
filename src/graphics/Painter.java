/**
 * Painter
 * @author Neil Balaskandarajah
 * Created on: 10/11/2019
 * Holds all drawing methods for any graphics
 */
package graphics;

import java.awt.Color;
import java.awt.Graphics2D;

import main.AutoSim;
import model.Robot;

public class Painter {
	
	/**
	 * Draw the robot to the screen
	 * Graphics2D g2 - used for drawing
	 * Robot r - robot to draw
	 */
	public static void drawRobot(Graphics2D g2, Robot r) {
		//translate to center of roboot
		g2.translate(r.getPoint().getX()*AutoSim.ppi, r.getPoint().getY()*AutoSim.ppi);
		g2.rotate(Math.toRadians(0));
		
		//draw body of robot
		g2.setColor(r.getColor());
		int length = r.getLengthPixels();
		int width = r.getWidthPixels();
		int cornerRad = AutoSim.ppi * 6;
		g2.fillRoundRect(-width/2, -length/2, width, length, cornerRad, cornerRad);
		
		//draw back-end indicator
		g2.fillRoundRect(-width/2, -length/2, width, cornerRad, cornerRad/4, cornerRad/4);
		
		//draw wheels of robot
		g2.setColor(new Color(70, 70, 70));
		int wheelWidth = (int) (2 * AutoSim.ppi);
		int wheelLength = 8 * AutoSim.ppi;
		int wheelOffset = length/8;
		g2.fillRect(-width/2, length/2 - wheelLength - wheelOffset, wheelWidth, wheelLength); //top left
		g2.fillRect(width/2 - wheelWidth, length/2 - wheelLength - wheelOffset, wheelWidth, wheelLength); //top right
		g2.fillRect(-width/2, -length/2 + wheelOffset, wheelWidth, wheelLength); //bottom left
		g2.fillRect(width/2 - wheelWidth, -length/2 + wheelOffset, wheelWidth, wheelLength); //bottom right
	} //end drawRobot
	
	/**
	 * 
	 * Graphics2D g2 - used for drawing
	 * String str - string to draw
	 * int x - x position to draw to
	 * int y - y position to draw to
	 */
	public static void drawString(Graphics2D g2, String str, int x, int y) {
		g2.scale(1.0, -1.0);
		g2.drawString(str, x, -y);
		g2.scale(1.0, -1.0);
	} //end drawString
} //end Painter 
