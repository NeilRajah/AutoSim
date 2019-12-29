/**
 * Window
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * Main window for the application
 */
package graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.AutoSim;
import util.Util;

public class Window extends JFrame {
	private JPanel mainPanel; //main panel for display
	
	/**
	 * Create a window
	 * @param width Width of the window in pixels
	 * @param height Height of the window in pixels
	 */
	public Window() {
		super();
		
		layoutView();
	} //end constructor
	
	/**
	 * Layout the window, add all components
	 */
	private void layoutView() {
		//main panel that holds all components
		mainPanel = new JPanel();
//		mainPanel.setLayout(new GridBagLayout());
		
		//environment where robot acts in
//		System.out.println(AutoSim.screenWidth +" "+ AutoSim.screenHeight +" "+ (AutoSim.ppi * AutoSim.fieldWidth));
		int width = AutoSim.ppi * AutoSim.fieldWidth;
		int height = AutoSim.ppi * AutoSim.fieldHeight;
//		int width = 324, height = 324;
		Util.println(width, height, AutoSim.ppi);
		Environment env = new Environment(width, height);
//		GridBagConstraints envGBC = createGBC(0,0, 1, 0.9);
//		mainPanel.add(env, envGBC);
		mainPanel.add(env);
		
		//text field (UI placeholder)
		JTextField text = new JTextField();
		GridBagConstraints textGBC = createGBC(0,1, 1, 0.1);
//		mainPanel.add(text, textGBC);
	} //end layoutView
	
	/**
	 * Create a GridBagConstraints object based on parameters for the layout
	 * int gridx - x position to contrain to
	 * int gridy - y position to constrain to
	 * double weightx - x weight to contrain to
	 * double weighty - y weight to constrain to
	 * return gbc - GridBagConstraints with parameters
	 */
	private GridBagConstraints createGBC(int gridx, int gridy, double weightx, double weighty) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.weightx = weightx;
		gbc.weighty = weighty;
		gbc.fill = GridBagConstraints.BOTH; //default to filling entire cell
		
		return gbc;
	} //end createGBC
	
	/**
	 * Configure and launch the window
	 */
	public void launch() {
		//configure frame and set it to visible
		this.setTitle("AutoSim");
		this.setContentPane(mainPanel);
		this.setUndecorated(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false); //scale window components
		this.setLocation(0, 0); //change to center to screen
		this.setVisible(true);
	} //end launch

} //end Window
