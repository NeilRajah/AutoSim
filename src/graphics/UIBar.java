/**
 * UIBar
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2020
 * Bar on bottom of window to help user interact with program 
 */
package graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.JComponent;

public class UIBar extends JComponent implements Component {
	//Attributes
	//Configured
	private int width; //component width
	private int height; //component height
	
	//Updated
	private String cursorLoc; //x,y location of cursor in inches
	private int fontSize; //font size
	
	public UIBar(int width, int height) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		//update constants
		fontSize = 120;
	}

	@Override
	public int width() {
		return width;
	}

	@Override
	public int height() {
		return height;
	}

	@Override
	public void update() {
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		setFont(g2);
		
		g2.scale(1.0, -1.0);
		g2.translate(0, -height);
		
		g2.setColor(Color.BLUE);
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(Color.WHITE);
		g2.scale(1.0, -1.0);
		g2.drawString("robot", 0, -5);
		g2.scale(1.0, -1.0);
	}
	
	private void setFont(Graphics2D g2) {
		Font f = new Font(Font.SANS_SERIF, Font.PLAIN, (int) fontSize);
		try {
			f = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/SF-UI-Display-Light.otf"));
			f = f.deriveFont(fontSize);
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("no font 4 u");
//			JOptionPane.showMessageDialog(null, "no font bro");
		}
		g2.setFont(f);
	}

}
