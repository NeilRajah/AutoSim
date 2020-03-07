/*
 * WidgetHub
 * Author: Neil Balaskandarajah
 * Created on: 01/03/2020
 * Hub for all the widgets 
 */

package graphics.widgets;

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

import util.Util.ROBOT_KEY;
import util.Util.WIDGET_ID;

public class WidgetHub extends JComponent {
	//Attributes
	private int height; //height of the hub
	private int width; //width of the hub
	private HashMap<WIDGET_ID, Widget> widgets; //widgets and their IDs
	
	public WidgetHub(int width, int height) {
		super();
		
		//set attributes
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(width, height));
		widgets = new HashMap<WIDGET_ID, Widget>();
		
		//layout the view
		layoutView();
	} //end constructor

	/**
	 * Layout the view of the widget hub
	 */
	private void layoutView() {
		//set the layout manager of the hub
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); //vertical layout
		this.setAlignmentX(CENTER_ALIGNMENT);
		
		//add title bar
		JLabel title = new JLabel("Widget Hub");
		title.setForeground(Color.RED);
		title.setFont(title.getFont().deriveFont(40.0f));
		title.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
		title.setAlignmentX(CENTER_ALIGNMENT);
		this.add(title);
		
		//this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
	} //end layoutView
	
	/**
	 * Add a widget to the hub and to the map
	 * @param w Widget to add to the hub
	 */
	public void addWidget(Widget w) {
		this.add(w.getComponent());
		widgets.put(w.getID(), w);
	} //end addWidget
	
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
	 * Update the widget
	 */
	public void update(HashMap<ROBOT_KEY, Object> data) {	
		widgets.forEach((id, widget) -> {
			switch (id) {
				case SPEED_DISPLAY:
					SpeedDisplayWidget sd = (SpeedDisplayWidget) widget;
					sd.update(new double[] {(double) data.get(ROBOT_KEY.LIN_VEL)});
					break;
			}
		});
	} //end update

	/**
	 * Update a widget with given values
	 * @param widgetID identifier of the tag for the information to be given to
	 * @param values numerical values to be fed to the widget
	 */
	public void updateWidget(WIDGET_ID widgetID, double[] values) {
		
	} //end updateWidget
} //end class
