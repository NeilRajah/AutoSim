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
	public static final int FIELD_WIDTH = 324; 
	public static final int FIELD_HEIGHT = 324;
	
	//Motors (values from https://motors.vex.com/)
	public static final double[] NEO = new double[] {5880, 1.3, 3.36, 166}; //
	public static final double[] CIM = new double[] {5330, 2.7, 2.41, 131}; //
	public static final double[] MINI_CIM = new double[] {5840, 3, 1.41, 89};  //
	public static final double[] PRO_775 = new double[] {18730, 0.7, 0.71, 134}; //
	public static final double[] FALCON_500 = new double[] {6380, 1.5, 4.69, 257}; //
	
	//Calculations
	/*
	 * Clamp a number between two values
	 * double num - number to clamp
	 * double low - bottom value
	 * double high - high value
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
	
	public static void println(String ... s) {
		for(String str : s) {
			System.out.print(s + " ");
		}
		System.out.println();
	} //end println
	
	public static void println(double ... d) {
		for(double dbl : d) {
			System.out.print(dbl + " ");
		}
		System.out.println();
	} //end println
	
	public static void println(String msg, double ... d) {
		System.out.print(msg + " ");
		for (double dbl : d) {
			System.out.printf("%.4f ", dbl);
		}
		System.out.println();
	}
	
	public static void tabPrint(String msg) {
		System.out.print(msg + "\t \t");
	}
	
	public static void tabPrint(double ... nums) {
		for (double num : nums) {
			System.out.printf("%.3f\t", num);
		}
	}
} //end Util
