/**
 * Window
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * Main window for the application
 */
package graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import commands.Command;
import commands.CommandGroup;
import graphics.widgets.Widget;
import graphics.widgets.WidgetHub;
import main.AutoSim;
import util.Util;
import util.Util.WIDGET_ID;

public class Window extends JFrame {
	//Attributes
	private JPanel mainPanel; //main panel for display
	private Environment env; //environment
	private UIBar bar; //user interface bar
	private WidgetHub widgetHub; //hub for all the widgets
	
	/**
	 * Create a window
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
		
		//add Widget Hub
		widgetHub = new WidgetHub(width * 1/6, env.height() + height/15);
		GridBagConstraints widgGridBag = ComponentUtil.createGBC(1, 0);
		widgGridBag.gridheight = 2;
		mainPanel.add(widgetHub, widgGridBag);
		
		//UI Bar
		bar = new UIBar(width, height / 15);
		mainPanel.add(bar, ComponentUtil.createGBC(0, 1));
		
		//add UI bar to environment
		env.addUIBar(bar);
		
		//add controllers
		EnvironmentController envCtrl = new EnvironmentController(env);
		env.addMouseMotionListener(envCtrl);
	} //end layoutView
	
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
		this.setResizable(false); 
		this.setLocation(10, 10);
		this.setVisible(true);
	} //end launch
	
	/**
	 * Set the debug mode of the Environment
	 */
	public void setDebug() {
		env.setDebug();
	} //end setDebug
	
	/**
	 * Add a Command to be animated
	 * @param c - Command to be animated
	 */
	public void addCommand(Command c) {
		c.run();
		env.addPoses(c.getPoses());
		env.setCurves(c.getCurves());
	} //end addCommand
	
	/**
	 * Add a CommandGroup to be animated
	 * @param cg - CommandGroup to be animated
	 */
	public void addCommandGroup(CommandGroup cg) {
		cg.run();
		env.addPoses(cg.getPoses());
		env.setCurves(cg.getCurves());
	} //end addCommandGroup
	
	/**
	 * Run the animation
	 */
	public void runAnimation() {
		//loop for running simulation
		Runnable loop = () -> {
			//1 second delay from open to simulation launch
			Util.pause(1000);
			
			//loop through all poses every 5 milliseconds
			for (int i = 1; i < env.getNumPoses(); i++) {
				env.incrementPoseIndex();
				Util.pause((int) (Util.UPDATE_PERIOD * 1000));
				
			} //loop
			System.out.println("ran");
		};
		
		//create and run the thread
		Thread t = new Thread(loop);
		t.start();
	} //end runAnimation
	
	/**
	 * Add a widget to the hub
	 * @param p - widget to add to the hub
	 */
	public void addWidget(WIDGET_ID id, JPanel panel) {
//		widgetHub.addWidget(new Widget(id, panel));
	} //end addWidget
	
	/**
	 * Add a widget to the hub
	 * @param c - widget to add to the hub
	 */
	public void addWidget(JComponent component) {
		JPanel panel = new JPanel();
		panel.add(component);
		
//		widgetHub.addWidget(panel);
	} //end addWidget
} //end Window
