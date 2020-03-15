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
import util.Util.ROBOT_KEY;
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
		
		Util.println("Window launched");
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
		env.setPoses(c.getPoses());
		env.setCurves(c.getCurves());
		env.setData(c.getData());
	} //end addCommand
	
	/**
	 * Add a CommandGroup to be animated
	 * @param cg - CommandGroup to be animated
	 */
	public void addCommandGroup(CommandGroup cg) {
		cg.run();
		env.setPoses(cg.getPoses());
		env.setCurves(cg.getCurves());
		env.setData(cg.getData());
	} //end addCommandGroup
	
	/**
	 * Run the animation
	 */
	public void runAnimation() {
		//loop for running simulation
		Runnable loop = () -> {
			//1 second delay from open to simulation launch
			Util.pause(1000);
			Util.println("Starting loop");
			Util.println("Number of poses:", env.getNumPoses());
			Util.println("Total time:", env.getNumPoses() * Util.UPDATE_PERIOD);
			
			//loop through all poses every 5 milliseconds
			for (int i = 1; i < env.getNumPoses(); i++) {
				env.incrementPoseIndex(); //draw the next pose
				widgetHub.update(env.getDataPoint(i)); //update all widgets
				bar.setCommandName((String) env.getDataPoint(i).get(ROBOT_KEY.CURRENT_COMMAND)); //name of the command being run
				
				Util.pause(Util.ANIMATION_PERIOD);
			} //loop
			
			Util.println("ran");
		};
		
		//create and run the thread
		Thread t = new Thread(loop);
		t.start();
		Util.println("Thread Started");
	} //end runAnimation
	
	//Widgets
	
	/**
	 * Add a widget to the hub
	 * @param w - Widget to add to the hub
	 */
	public void addWidget(Widget w) {
		widgetHub.addWidget(w);
	} //end addWidget
	
	/**
	 * Get the width of the widget hub
	 * @return width of widget hub in pixels
	 */
	public int getHubWidth() {
		return widgetHub.width();
	} //end getHubWidth
	
	/**
	 * Get the height of the widget hub
	 * @return height of the widget hub in pixels
	 */
	public int getHubHeight() {
		return widgetHub.height();
	} //end getHubHeight
} //end Window
