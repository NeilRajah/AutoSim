/**
 * Window
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * Main window for the application
 */
package graphics;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
	private JPanel mainPanel; //main panel for display
	private int width; //width of window
	private int height; //height of window
	
	/**
	 * Create a window
	 * @param width Width of the window in pixels
	 * @param height Height of the window in pixels
	 */
	public Window(int width, int height) {
		super();
		
		this.width = width;
		this.height = height;
		
		layoutView();
	} //end constructor
	
	/**
	 * Layout the window, add all components
	 */
	private void layoutView() {
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(width, height));
		
		Environment env = new Environment(width, height);
		mainPanel.add(env);
	} //end layoutView
	
	/**
	 * Configure and launch the window
	 */
	public void launch() {
		this.setContentPane(mainPanel);
		this.setUndecorated(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false);
		this.setLocation(600, 200);
		this.setVisible(true);
	} //end launch

} //end Window
