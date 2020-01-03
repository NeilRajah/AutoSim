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
	 * @param g2
	 */
	public static void drawRobot(Graphics2D g2, Robot r) {
		//translate to center of roboot
		g2.translate(r.getPoint().getX()*AutoSim.ppi, r.getPoint().getY()*AutoSim.ppi);
		
		g2.setColor(r.getColor());
		int rectWidth = AutoSim.ppi * 30;
		int cornerRad = AutoSim.ppi * 6;
		g2.fillRoundRect(-rectWidth/2, -rectWidth/2, rectWidth, rectWidth, cornerRad, cornerRad);
		
		g2.setColor(Color.black);
		int wheelWidth = 5;
		int wheelLength = 30;
		g2.fillRoundRect(-rectWidth/2 + (rectWidth - wheelLength)/2, -rectWidth/2, wheelLength, wheelWidth, 5, 5);
		g2.fillRoundRect(-rectWidth/2 + (rectWidth - wheelLength)/2, rectWidth/2 - wheelWidth, wheelLength, wheelWidth, 5, 5);

		g2.setColor(Color.BLACK);
		int rad = rectWidth/2 + 15;
		g2.drawLine(0, 0, rad, 0);
		
		int rHead = 10;
		g2.translate(rad, 0);
		g2.rotate(Math.toRadians(90));
		g2.drawLine(0, 0, rHead, rHead);
		g2.rotate(Math.toRadians(90));
		g2.drawLine(0, 0, rHead, rHead);
	} //end drawRobot
} //end Painter
