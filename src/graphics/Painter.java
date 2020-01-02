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

public class Painter {
	
	public static void drawRobot(Graphics2D g2) {
		g2.translate(500, 500);
		
		g2.setColor(Color.GREEN);
		int rectWidth = AutoSim.ppi * 30;
		g2.fillRoundRect(-rectWidth/2, -rectWidth/2, rectWidth, rectWidth, 10, 10);
		
		g2.setColor(Color.black);
		int wheelWidth = 5;
		int wheelLength = 30;
		g2.fillRoundRect(-rectWidth/2 + (rectWidth - wheelLength)/2, -rectWidth/2, wheelLength, wheelWidth, 2, 2);
		g2.fillRoundRect(-rectWidth/2 + (rectWidth - wheelLength)/2, rectWidth/2 - wheelWidth, wheelLength, wheelWidth, 2, 2);

		g2.setColor(Color.BLACK);
		int r = rectWidth/2 + 15;
		g2.drawLine(0, 0, r, 0);
		
		int rHead = 10;
		g2.translate(r, 0);
		g2.rotate(Math.toRadians(90));
		g2.drawLine(0, 0, rHead, rHead);
		g2.rotate(Math.toRadians(90));
		g2.drawLine(0, 0, rHead, rHead);
	}
} //end Painter
