package graphics.widgets;

import java.awt.Color;

import util.JComponentFactory;
import util.Util.ROBOT_KEY;

public class SpeedDisplayWidget extends Widget {
	private SpeedDisplay sd;
	
	/**
	 * Create a SpeedDisplayWidget using a pre-existing component
	 * @param sd SpeedDisplay JComponent
	 * @param key value to get from data map and update with
	 */
	public SpeedDisplayWidget(SpeedDisplay sd, ROBOT_KEY key) {
		//create widget
		super(JComponentFactory.panelFromComponent(sd));
		
		//set attributes
		this.sd = sd;
		
		//set the keys to be used when updating
		this.keyArray = new ROBOT_KEY[] {key}; 
	} //end constructor
	
	/**
	 * Create a SpeedDisplayWidget
	 * @param width width of widget in pixels
	 * @param height height of widget in pixels
	 * @param maxVal maximum value to scale to
	 * @param key value to get from data map and update with
	 */
	public SpeedDisplayWidget(int width, int height, double maxVal, ROBOT_KEY key) {
		this(new SpeedDisplay(width, height, maxVal), key);
	} //end constructor
	
	/**
	 * Update the component
	 * @param values array of values used to update
	 */
	public void update(double[] values) {
		sd.update(values[0]);
	} //end update
	
	/**
	 * Set the color of the bar
	 * @param color color for the bar
	 */
	public void setColor(Color color) {
		sd.setColor(color);
	} //end setColor
} //end class