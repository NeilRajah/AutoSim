/*
 * WidgetHub
 * Author: Neil Balaskandarajah
 * Created on: 01/03/2020
 * Hub for all the widgets 
 */

package graphics.widgets;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import graphics.Component;

public class WidgetHub extends JPanel implements Component {
	//Attributes
	private int height; //height of the hub
	private int width; //width of the hub
	
	public WidgetHub(int width, int height) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		
		//layout the view
		layoutView();
	} //end constructor

	/**
	 * Layout the view of the widget hub
	 */
	private void layoutView() {
		//add title bar
		JLabel title = new JLabel("Widget Hub");
		title.setForeground(Color.RED);
		title.setFont(title.getFont().deriveFont(40.0f));
		this.add(title);
		
//		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
	} //end layoutView
	
	/**
	 * Return the width of the widget hub
	 * @return width - width of component in pixels
	 */
	public int width() {
		return width;
	} //end width

	/**
	 * Return the height of the widget hub
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

} //end class
