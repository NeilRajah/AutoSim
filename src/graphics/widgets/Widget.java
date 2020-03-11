/**
 * Widget
 * Author: Neil Balaskandarajah
 * Created on: 29/02/2020
 * Template for all widgets to be added to the UI
 */

package graphics.widgets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Util.WIDGET_ID;

public abstract class Widget {
	//Attributes
	private boolean toggled; //whether widget is collapsed or not
	private WIDGET_ID id; //ID tag of the widget
	private JPanel panel; //graphical component in widget
	
	/**
	 * Create widget with a component
	 * @param panel graphical component for widget
	 */
	public Widget(WIDGET_ID id, JPanel panel) {
		//set attributes
		this.panel = panel;
		this.id = id;
		
		//layout the view of the widget
		layoutWidg();
	} //end constructor
	
	public abstract void update(double[] values);
	
	/**
	 * Get the height of the widget 
	 * @return height in pixels
	 */
	public double getHeight() {
		return panel.getHeight();
	} //end getHeight
	
	/**
	 * Get the width of the widget
	 * @return width in pixels
	 */
	public double getWidth() {
		return panel.getWidth();
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
		return panel;
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
	} //end toggle
	
	/**
	 * Layout the widget, including its component panel
	 */
	private void layoutWidg() {
		//set up widgPanel
//		widgPanel = JComponentFactory.boxPanel(false);
		
		//add header (text bar with collapsible button)
		JLabel label = new JLabel("Widget");
	} //end layoutWidg
} //end class
