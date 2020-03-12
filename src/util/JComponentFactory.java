/**
 * JComponentFactory
 * Author: Neil Balaskandarajah
 * Created on: 28/02/2020
 * Use pre-configured Swing components with ease
 */

package util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;

public class JComponentFactory {
	// PANELS//

	/**
	 * Creates a panel with a specified box layout
	 * @param horizontal whether the panel is horizontal or not
	 */
	public static JPanel boxPanel(boolean horizontal) {
		JPanel panel = new JPanel();
		BoxLayout box;

		// set to horizontal or vertical
		if (horizontal) {
			box = new BoxLayout(panel, BoxLayout.X_AXIS);
		} else {
			box = new BoxLayout(panel, BoxLayout.Y_AXIS);
		} // if

		panel.setLayout(box);

		return panel;
	} // end boxPanel

	/**
	 * Creates a panel with a border layout
	 */
	public static JPanel borderPanel() {
		JPanel panel = new JPanel();

		// set to border layout
		BorderLayout border = new BorderLayout();
		panel.setLayout(border);

		return panel;
	} // end borderPanel

	/**
	 * Create a panel with a grid layout 
	 * @param rows - number of rows in the grid 
	 * @param cols - number of columns in the grid
	 */
	public static JPanel gridPanel(int rows, int cols) {
		JPanel panel = new JPanel();
		GridLayout grid = new GridLayout(rows, cols, 5, 5);
		panel.setLayout(grid);

		return panel;
	} // end gridPanel
	
	/**
	 * Create a JPanel holding a JComponent
	 * @param component JComponent to be put in JPanel
	 * @return vanilla JPanel containing component
	 */
	public static JPanel panelFromComponent(JComponent component) {
		JPanel panel = new JPanel();
		panel.add(component);
		return panel;
	} //end panelFromComponent

	// WIDGETS//
	/**
	 * Create a checkbox of a standard size 
	 * @param label - component label
	 */
	public static JCheckBox checkBox(String label) {
		JCheckBox checkBox = new JCheckBox(label);
		checkBox.setPreferredSize(new Dimension(75, 25));

		return checkBox;
	} // end checkBox

	/**
	 * Create a slider of a standard size 
	 * @param min - left slider value 
	 * @param max - right slider value
	 */
	public static JSlider slider(int min, int max) {
		JSlider slider = new JSlider(min, max);
		slider.setPreferredSize(new Dimension(200, 25));
		slider.setValue(min);

		return slider;
	} // end slider

	/**
	 * Creates a radio button with a label and standard size 
	 * @param label - component label
	 */
	public static JRadioButton radioButton(String label) {
		JRadioButton radio = new JRadioButton(label);
		radio.setPreferredSize(new Dimension(110, 25));

		return radio;
	} // end radioButton

	/**
	 * Create a text area
	 */
	public static JTextArea textArea() {
		JTextArea text = new JTextArea();
		text.setPreferredSize(new Dimension(900, 50));

		return text;
	} // end textArea

	/**
	 * Create a button with a title of standard size 
	 * @param label - component label
	 */
	public static JButton button(String label) {
		JButton button = new JButton(label);
		button.setPreferredSize(new Dimension(80, 50));

		return button;
	} // end button

	/**
	 * Return a partially configured frame 
	 * @param name - window title
	 */
	public static JFrame frame(String name) {
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setFocusable(true);

		return frame;
	} // end frame
} // end class