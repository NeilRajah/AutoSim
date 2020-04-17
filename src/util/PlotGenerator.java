/**
 * PlotGenerator
 * Author: Neil Balaskandarajah
 * Created on: 17/04/2020
 * Generate plots from (x,y) data
 */

package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import model.motion.DriveProfile;
import model.motion.TrapezoidalProfile;
import util.Util.ROBOT_KEY;

public class PlotGenerator {
	
	/**
	 * Run tests and generate plots
	 */
	public static void main(String[] args) {
		TrapezoidalProfile trap = new TrapezoidalProfile(100, 20, 12);
		displayChart(createLinearTrajChart(trap, "Trapezoidal Profile", 1024, 768));
	} //end main
	
	/**
	 * Create a profile trajectory plot with position, velocity and acceleration
	 * @param profile Profile to get data from
	 * @param title Plot title
	 */
	public static XYChart createLinearTrajChart(DriveProfile profile, String title, int width, int height) {		
		double[][] xy = getXYFromProfile(profile, 1);
//		xy[1] = Util.scaleArray(xy[1], 12); //convert from ft/s to in/s
		
		//create chart
		XYChart chart = buildChart(width, height, title, "Time (s)", "Velocity (ft/s)");
		chart.addSeries("Profile", xy[0], xy[1]);
		
		return chart;
	} //end createLinearTrajPlot
	
	/**
	 * Build a chart with the XYChartBuilder
	 * @param w Width in pixels
	 * @param h Height in pixels
	 * @param t Title
	 * @param x X axis title
	 * @param y Y axis title
	 * @return Chart configured with above parameters
	 */
	private static XYChart buildChart(int w, int h, String t, String x, String y) {
		return new XYChartBuilder().width(w).height(h).title(t).xAxisTitle(x).yAxisTitle(y).build();
	} //end buildChart
	
	/**
	 * Display a chart in a separate window
	 * @param c Chart to display
	 */
	public static void displayChart(XYChart c) {
		/* Display chart using image
		String filename = "displayChart";
		saveChartToFile(c, filename);
		
		try {
			Image img = ImageIO.read(new File(Util.UTIL_DIR + filename + ".png"));
			img = img.getScaledInstance(c.getWidth(), c.getHeight(), BufferedImage.SCALE_SMOOTH);
			ImageIcon icon = new ImageIcon(img);
			
			JFrame frame = new JFrame();
			
			JLabel comp = new JLabel();
			comp.setIcon(icon);
			frame.add(comp);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		} catch (IOException e) {
			Util.println("Could not display chart");
			e.printStackTrace();
		}
		*/
		
		new SwingWrapper(c).displayChart();
		//save to high-res and display
	} //end displayChart
	
	/**
	 * Save the chart to a file
	 * @param c Chart to save to file
	 * @param filename Name of file
	 */
	public static void saveChartToFile(XYChart c, String filename) {
		try {
			BitmapEncoder.saveBitmapWithDPI(c, Util.UTIL_DIR + filename, BitmapFormat.PNG, 100);
		} catch (IOException i) {
			i.printStackTrace();
		} //try-catch
	} //end saveChartToFile
	
	/**
	 * Get the (x,y) points from a chart from the profile
	 * @param profile Profile to get data from
	 * @param key Index in trajectory point array (0,1,2 -> position, velocity, acceleration)
	 * @return Array containing the x and y arrays
	 */
	private static double[][] getXYFromProfile(DriveProfile profile, int key) {
		//get (x,y) points for profile
		double[] x = new double[profile.getSize()];
		double[] y = new double[profile.getSize()];
		
		//velocity profile
		for (int i = 0; i < profile.getSize(); i++) {
			x[i] = i * Util.UPDATE_PERIOD;
			y[i] = profile.getLeftTrajPoint(i)[key];
		} //loop
		
		return new double[][] {x, y};
	} //end getXYFromProfile
	
	/**
	 * Get the (x,y) points for a chart from robot data
	 * @param data Robot data points
	 * @param key Key indicating what data point to get
	 * @return Array containing the x and y arrays
	 */
	public static double[][] getXYFromRobotData(ArrayList<HashMap<Util.ROBOT_KEY, Object>> data, ROBOT_KEY key) {
		double[] x = new double[data.size()];
		double[] y = new double[data.size()];
		
		for (int i = 0; i < data.size(); i++) {
			x[i] = i * Util.UPDATE_PERIOD;
			y[i] = (double) data.get(i).get(key);
		} //loop
		
		return new double[][] {x, y};
	} //end getXYFromRobotData
} //end class
