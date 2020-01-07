/**
 * Util
 * Author: Neil Balaskandarajah
 * Created on: 10/11/2019
 * Holds static utility methods and constants
 */
package util;

public class Util {
	//Constants
	public static final double INCHES_TO_METERS = 0.0254;
	public static final double LBS_TO_KG = 0.453592;
	public static final double UPDATE_PERIOD = 0.005; //5ms
	public static final double V_INTERCEPT = 0.206; //voltage required to overcome frictional losses
	public static final int FIELD_WIDTH = 648; //inches
	public static final int FIELD_HEIGHT = 324; //inches
	public static final double MAX_VOLTAGE = 12.0; //max voltage in Volts
	
	//PID constants
	//DrivePID constants
	public static final double kP_DRIVE = 0.32; 
	public static final double kI_DRIVE = 0.0;
	public static final double kD_DRIVE = 1.25;

	//TurnPID constants
	public static final double kP_TURN = 7.5;
	public static final double kI_TURN = 0.0;
	public static final double kD_TURN = 0.05;
	
	//Motors (values from https://motors.vex.com/)
	//Free Speed (RPM), Free Current (A), Stall Torque (Nm), Stall Current (A)
	public static final double[] NEO = new double[] {5880, 1.3, 3.36, 166}; 
	public static final double[] CIM = new double[] {5330, 2.7, 2.41, 131}; 
	public static final double[] MINI_CIM = new double[] {5840, 3, 1.41, 89}; 
	public static final double[] PRO_775 = new double[] {18730, 0.7, 0.71, 134};
	public static final double[] FALCON_500 = new double[] {6380, 1.5, 4.69, 257}; 
	
	//Calculations
	
	/**
	 * Clamp a number between two values
	 * @param num - number to clamp
	 * @param low - bottom value
	 * @param high - high value
	 * @return - value within [low, high]
	 */
	public static double clampNum(double num, double low, double high) {
		if (num > high) { //above limit
			return high;
		} else if (num < low) { //below limit
			return low;
		} else { //between limit
			return num;
		} //if
	} //end clampNum
	
	//Output
	
	/**
	 * Print a variable amount of strings to the console
	 * @param ... s - variable number of strings to print
	 */
	public static void println(String ... s) {
		for(String str : s) {
			System.out.print(str + " "); //print each with a space in between
		} //loop
		System.out.println();
	} //end println
	
	/**
	 * Print a variable amount of double to the console
	 * @param ... d - variable number of doubles to print
	 */
	public static void println(double ... d) {
		for(double dbl : d) {
			System.out.print(dbl + " "); //print each with a space in between
		} //loop
		System.out.println();
	} //end println
	
	/**
	 * Print a variable amount of double to the console with a message in front
	 * @param msg - message to print before numbers
	 * @param ... d - variable number of doubles to print
	 */
	public static void println(String msg, double ... d) {
		System.out.print(msg + " ");
		for (double dbl : d) {
			System.out.printf("%.4f ", dbl); //four decimal places
		} //loop
		System.out.println();
	} //end println
	
	/**
	 * Print a string with tabs at the end
	 * @param msg - string to print
	 */
	public static void tabPrint(String msg) {
		System.out.print(msg + "\t \t");
	} //end tabPrint
	
	/**
	 * Print a variable number of doubles with a tab after each one
	 * @param ... nums - variable number of doubles to print
	 */
	public static void tabPrint(double ... nums) {
		for (double num : nums) {
			System.out.printf("%.3f\t", num); //format to 3 digits
		} //loop
	} //end tabPritn
	
	/**
	 * Pauses the thread for a specified amount of time
	 * @param delay - time in milliseconds the pause the thread
	 */
	public static void pause(long delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException t) {
			t.printStackTrace();
		} //try-catch
	} //end pause
} //end Util
