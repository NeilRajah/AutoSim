/**
 * Widget
 * Author: Neil Balaskandarajah
 * Created on: 29/02/2020
 * Template for all widgets to be added to the UI
 */

package graphics.widgets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.JComponentFactory;
import util.Util.WIDGET_ID;

public abstract class Widget {
	//Attributes
	private double height; //height of widget in pixels
	private double width; //width of widget in pixels
	private boolean toggled; //whether widget is collapsed or not
	private String title; //title of the widget
	private WIDGET_ID id; //ID tag of the widget
	private JPanel component; //graphical component in widget
	private JPanel widgPanel; //panel for entire widget
	
	/**
	 * Create widget with a component
	 * @param component graphical component for widget
	 */
	protected Widget(WIDGET_ID id, JPanel component) {
		//set attributes
		this.component = component;
		this.id = id;
		
		//layout the view of the widget
		layoutWidg();
	} //end constructor
	
	protected abstract void initialize();
	
	/**
	 * Get the height of the widget 
	 * @return height in pixels
	 */
	public double getHeight() {
		return height;
	} //end getHeight
	
	/**
	 * Get the width of the widget
	 * @return width in pixels
	 */
	public double getWidth() {
		return width;
	} //end getWidth
	
	/**
	 * Get the ID of the Widget
	 * @return ID of the widget
	 */
	public WIDGET_ID getID() {
		return id;
	} //end getID
	
	/**
	 * Get the JPanel holding the widget
	 * @return
	 */
	public JPanel getComponent() {
		return component;
	} //end getComponent
	
	/**
	 * Return whether or not the widget is toggled
	 * @return toggle
	 */
	public boolean getToggled() {
		return toggled;
	} //end getToggled
	
	/**
	 * Toggle the state of the widget and update it
	 */
	public void toggle() {
		toggled = !toggled;
		update();
	} //end toggle
	
	/**
	 * Update the widget
	 */
	private void update() {
		//if toggled
		//	display JPanel
		//else
		//	do not
		
		component.setVisible(toggled);
	} //end update
	
	/**
	 * Layout the widget, including its component panel
	 */
	private void layoutWidg() {
		//set up widgPanel
		widgPanel = JComponentFactory.boxPanel(false);
		
		//add header (text bar with collapsible button)
		JLabel label = new JLabel("Widget");
	} //end layoutWidg
} //end class
