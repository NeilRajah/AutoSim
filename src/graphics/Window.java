/**
 * Window
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * Main window for the application
 */
package graphics;

import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.AutoSim;
import model.Robot;
import util.Util;

public class Window extends JFrame {
	private JPanel mainPanel; //main panel for display
	private Environment env; //environment
	private UIBar bar; //user interface bar
	
	/**
	 * Create a window
	 * Robot r - robot to be simulated
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
		mainPanel.setLayout(new GridBagLayout());
		
		//environment where robot acts in
//		System.out.println(AutoSim.screenWidth +" "+ AutoSim.screenHeight +" "+ (AutoSim.ppi * AutoSim.fieldWidth));
		int width = AutoSim.ppi * Util.FIELD_WIDTH;
		int height = AutoSim.ppi * Util.FIELD_HEIGHT;
//		int width = 324, height = 324;
		Util.println(width, height, AutoSim.ppi);
		env = new Environment(width, height);
//		GridBagConstraints envGBC = createGBC(0,0, 1, 1);
//		mainPanel.add(env, envGBC);
//		mainPanel.add(env, ComponentUtil.createGBC(0, 0));
//		mainPanel.add(env);
		
		//UI Bar		
		Util.println(width, height / 10);
		bar = new UIBar(width, height / 10);
//		GridBagConstraints barGBC = createGBC(0,1, 1, 1);
		mainPanel.add(bar, ComponentUtil.createGBC(1, 0));
//		JTextField text = new JTextField();
//		GridBagConstraints textGBC = createGBC(0,1, 1, 0.1);
//		mainPanel.add(text, textGBC);
	} //end layoutView
	
	
	
	/**
	 * Add a robot to the environment
	 * Robot r - robot to add to environment
	 */
	public void addRobot(Robot r) {
		env.addRobot(r);
	} //end addRobot
	
	/**
	 * Configure and launch the window
	 */
	public void launch() {
		//configure frame and set it to visible
		this.setTitle("AutoSim");
		this.setContentPane(mainPanel);
		this.setUndecorated(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.pack();
		this.setSize(2000, 2000);
		this.setResizable(false); //scale window components
		this.setLocation(600, 50); //change to center of screen
		this.setVisible(true);
	} //end launch

} //end Window
