/**
 * Window
 * @author Neil Balaskandarajah
 * Created on: 08/11/2019
 * Main window for the application
 */
package graphics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import commands.Command;
import commands.CommandGroup;
import graphics.widgets.Widget;
import graphics.widgets.WidgetHub;
import main.AutoSim;
import util.JComponentUtil;
import util.Util;
import util.Util.ROBOT_KEY;

public class Window extends JFrame {
	//Attributes
	private JPanel mainPanel; //main panel for display
	private Environment env; //environment
	private UIBar bar; //user interface bar
	private WidgetHub widgetHub; //hub for all the widgets
	private int height; //height of window in pixels
	private int width; //width of window in pixels
	private boolean debug; //whether the window is for debugging or not
	
	/**
	 * Create a window
	 * @param debug Whether the window is for debugging
	 */
	public Window(boolean debug) {
		super();
		
		this.debug = debug;
		layoutView();
	} //end constructor
	
	/**
	 * Layout the window, add all components
	 */
	private void layoutView() {
		//main panel that holds all components
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		
		//set up respective components
		setUpEnvironment();
		setUpWidgetHub();
		setUpUIBar();
		setUpStartButton();
	} //end layoutView
	
	/**
	 * Set up the Environment
	 */
	private void setUpEnvironment() {
		//environment where robot acts in
		//convert from inches to pixels
		width = AutoSim.PPI * Util.FIELD_WIDTH; 
		height = AutoSim.PPI * Util.FIELD_HEIGHT;
		
		//change window shape depending on whether it is in debug or not
		if (debug) {
			env = new Environment(height, height); //square
			env.setDebug();
		} else {
			env = new Environment(width, height); //long field
		} //if
		
		//add to grid
		GridBagConstraints envGBC = JComponentUtil.createGBC(0, 0);
		envGBC.gridwidth = 2;
		mainPanel.add(env, envGBC);
		
		//add controllers
		EnvironmentController envCtrl = new EnvironmentController(env);
		env.addMouseMotionListener(envCtrl);
		
		//set focus
		env.setFocusable(true);
	} //end setUpEnvironment
	
	/**
	 * Set up the Widget Hub
	 */
	private void setUpWidgetHub() {
		//add Widget Hub
		widgetHub = new WidgetHub(width * 1/6, env.height() + height/15);
		GridBagConstraints widgGridBag = JComponentUtil.createGBC(2, 0);
		widgGridBag.gridheight = 2;
		mainPanel.add(widgetHub, widgGridBag);
	} //end setUpWidgetHub
	
	/**
	 * Set up the UI bar
	 */
	private void setUpUIBar() {
		bar = new UIBar((int) (env.width() * 0.8), height / 15);
		mainPanel.add(bar, JComponentUtil.createGBC(0, 1));
		
		//add UI bar to environment
		env.addUIBar(bar);
	} //end setUpUIBar
	
	/**
	 * Set up the start button
	 */
	private void setUpStartButton() {
		BoxButton start = new BoxButton((int) (env.width() * 0.2), bar.height(), "Start", true, true);
		
		ButtonController startCtrl = new ButtonController(start, this::runAnimation); 
		startCtrl.setColors(Painter.BEZ_BTN_LIGHT, Painter.BEZ_BTN_DARK);
		start.addMouseListener(startCtrl);
		
		mainPanel.add(start, JComponentUtil.createGBC(1, 1));
	} //end setUpStartButton
	
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
		if (AutoSim.TOP_SCREEN) {
			this.setLocation(AutoSim.SCREEN_WIDTH/2, -AutoSim.SCREEN_HEIGHT + 100);
		} else {
			this.setLocation(AutoSim.SCREEN_WIDTH/2 - this.getWidth()/2, AutoSim.SCREEN_HEIGHT/2 - this.getHeight()/2);
		}
		this.setVisible(true);
		env.update();
		
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
		env.incrementPoseIndex();
		env.update();
	} //end addCommandGroup
	
	/**
	 * Run the animation
	 */
	public void runAnimation() {
		//loop for running simulation
		Runnable loop = () -> {
			//slight delay before running
			Util.pause(250);
			Util.println("Starting loop");
			Util.println("Number of poses:", env.getNumPoses());
			Util.println("Total time:", env.getNumPoses() * Util.UPDATE_PERIOD);
			
			//loop through all poses every 5 milliseconds
			for (int i = 1; i < env.getNumPoses(); i++) {
				HashMap<ROBOT_KEY, Object> data = env.getDataPoint(i);
				
				env.incrementPoseIndex(); //draw the next pose
				widgetHub.update(data); //update all widgets
				bar.setCommandName((String) data.get(ROBOT_KEY.CURRENT_COMMAND)); //name of the command being run
				
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
