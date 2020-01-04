/**
 * Window
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * Main window for the application
 */
package graphics;

import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import main.AutoSim;
import model.Pose;
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
		int width = AutoSim.ppi * Util.FIELD_WIDTH; //convert from inches to pixels
		int height = AutoSim.ppi * Util.FIELD_HEIGHT;
		env = new Environment(width, height);
		mainPanel.add(env, ComponentUtil.createGBC(0, 0));
		
		//UI Bar
		bar = new UIBar(width, height / 15);
		mainPanel.add(bar, ComponentUtil.createGBC(0, 1));
		
		//add UI bar to environment
		env.addUIBar(bar);
		
		//add controllers
		EnvironmentController envCtrl = new EnvironmentController(env);
		env.addMouseMotionListener(envCtrl);
	} //end layoutView
	
	public void addPoses(ArrayList<Pose> poses) {
		env.addPoses(poses);
	}
	
	public void incrementPoseIndex() {
		env.incrementPoseIndex();
	}
	
	/**
	 * Configure and launch the window
	 */
	public void launch() {
		//configure frame and set it to visible
		this.setTitle("AutoSim by Neil Balaskandarajah");
		this.setContentPane(mainPanel);
		this.setUndecorated(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setResizable(false); //scale window components
		this.setLocation(600, 50); //change to center of screen
		this.setVisible(true);
	} //end launch

} //end Window
