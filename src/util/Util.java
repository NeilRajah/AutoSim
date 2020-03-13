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
	public static final double LOOKAHEAD_DIST = 6; //additional distance to look ahead
	
	//PID constants
	//DrivePID constants
	public static final double kP_DRIVE = 0.32; 
	public static final double kI_DRIVE = 0.0;
	public static final double kD_DRIVE = 1.25;

	//TurnPID constants
	public static final double kP_TURN = 7.5;
	public static final double kI_TURN = 0.0;
	public static final double kD_TURN = 0.05;
	
	//Motion Profiling Constants (calculated with model)
	public static final double kV_MODEL = 0.182; //ft/s -> V is 0.0833333
	public static final double kA_MODEL = 0.0203;
	
	//Motion Profiling Constants (calculated empirically)
	//initialized to negative value for detecting errors in not assigning it
	public static double kV_EMPIR = -1; //voltage -> ft/s
	public static double kA_EMPIR = -1; //voltage -> ft/s^2
	
	//Motors (values from https://motors.vex.com/)
	//Free Speed (RPM), Free Current (A), Stall Torque (Nm), Stall Current (A)
	public static final double[] NEO = new double[] {5880, 1.3, 3.36, 166}; 
	public static final double[] CIM = new double[] {5330, 2.7, 2.41, 131}; 
	public static final double[] MINI_CIM = new double[] {5840, 3, 1.41, 89}; 
	public static final double[] PRO_775 = new double[] {18730, 0.7, 0.71, 134};
	public static final double[] FALCON_500 = new double[] {6380, 1.5, 4.69, 257}; 
	
	//Path Constants
	public static final int[] FIVENOMIAL_CONSTANTS = {1,5,10,10,5,1};
	
	//Widget IDs
	public static enum WIDGET_ID {
		SPEED_DISPLAY
	} //end enum
	
	//Robot HashMap Keys
	public static enum ROBOT_KEY {
		AVG_POS, 	
		LIN_VEL, 	
		ANG_VEL, 	
		HEADING, 	
		YAW,	 	
		POINT,		
		COLOR,		
		LEFT_POS,	
		RIGHT_POS,
		LEFT_VEL,
		RIGHT_VEL,
		LEFT_ACC,
		RIGHT_ACC,
		LIN_ACC,
		ANG_ACC,
		STATE
	} //end enum
	
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
	
	/**
	 * Calculate a slope of best fit for equally sized double arrays
	 * Math based on videos by Eugene O'Loughlin:
	 * https://youtu.be/2SCg8Kuh0tE
	 * https://youtu.be/GhrxgbQnEEU
	 * @param x - x values in data set
	 * @param y - y values in data set
	 * @return slope calculated using linear regression
	 */
	public static double regressedSlope(double[] x, double[] y) {
		//throw error if arrays are not same length
		if (x.length != y.length) {
			String errorMsg = "Arrays must be same size!\n";
			errorMsg += "x Array Size: " + x.length + "\n";
			errorMsg += "y Array Size: " + y.length + "\n";
			
			throw new IllegalArgumentException(errorMsg);
		} //if
		
		//compute average of each array
		double xSum = 0.0, ySum = 0.0;
		
		for (int i = 0; i < x.length; i++) {
			xSum += x[i];
			ySum += y[i];
		} //loop
		
		double xAvg = xSum / x.length;
		double yAvg = ySum / y.length;
		Util.println(xAvg, yAvg);
		
		//create deviation arrays 
		double[] xDevs = new double[x.length];
		double[] yDevs = new double[y.length];
		
		for (int i = 0; i < x.length; i++) {
			xDevs[i] = x[i] - xAvg;
			yDevs[i] = y[i] - yAvg;
			//calculations for r and sx,sy here?
		} //loop
		
		//compute Pearson's Coefficient
		double rNum = 0.0;
		double xDevSqr = 0.0, yDevSqr = 0.0;
		
		for (int i = 0; i < x.length; i++) {
			rNum += xDevs[i] * yDevs[i];
			xDevSqr += Math.pow(xDevs[i], 2);
			yDevSqr += Math.pow(yDevs[i], 2);
		} //loop
		
		double r = rNum / Math.sqrt(xDevSqr * yDevSqr);
		
		//compute standard deviations
		double xStdDevNum = 0.0, yStdDevNum = 0.0;
		
		for (int i = 0; i < x.length; i++) {
			xStdDevNum += Math.pow(xDevs[i], 2);
			yStdDevNum += Math.pow(yDevs[i], 2);
		} //loop
		
		double xStdDev = Math.sqrt(xStdDevNum / (x.length - 1));
		double yStdDev = Math.sqrt(yStdDevNum / (y.length - 1));
		
		return r * (yStdDev / xStdDev);
	} //end regressedSlope
	
	/**
	 * Linearly interpolate between two points
	 * @param y - input value to find x value of
	 * @param x1 - bottom x value
	 * @param y1 - bottom y value
	 * @param x2 - top x value
	 * @param y2 - top y value
	 * @return - corresponding linearly interpolated x value of y
	 */
	public static double interpolate(double y, double x1, double y1, double x2, double y2) {
		return ((y - y1) * (x2 - x1)) / (y2 - y1) + x1;
	} //end interpolate
	
	/**
	 * Flip the x and y values of curve points
	 * @param curve - curve to flip
	 * @return - curve with its x and y values flipped
	 */
	public static double[][] flipCurve(double[][] curve) {
		for (int i = 0; i < curve.length; i++) {
			double buffer = curve[i][0];
			curve[i][0] = curve[i][1];
			curve[i][1] = buffer;
		} //loop
		
		return curve;
	} //end flipCurve
	
	//Output
	
	/**
	 * Get the information of the caller method
	 */
	private static void printCallerInfo() {
		StackTraceElement stack = Thread.currentThread().getStackTrace()[3];
		String methodName = stack.getMethodName();
		String className = stack.getClassName();
		
		System.out.print("[" + className + "." + methodName + "()] ");
	} //end println
	
	/**
	 * Print a single string to the console 
	 * @param s message to be printed
	 */
	public static void println(String s) {
		printCallerInfo();
		System.out.println(s);
	} //end println
	
	/**
	 * Print a variable amount of strings to the console
	 * @param ... s - variable number of strings to print
	 */
	public static void println(String ... s) {
		printCallerInfo();
		
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
		printCallerInfo();
		
		for(double dbl : d) {
			System.out.printf("%.4f ", dbl); //four decimal places
		} //loop
		System.out.println();
	} //end println
	
	/**
	 * Print a variable amount of double to the console with a message in front
	 * @param msg - message to print before numbers
	 * @param ... d - variable number of doubles to print
	 */
	public static void println(String msg, double ... d) {
		printCallerInfo();
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
