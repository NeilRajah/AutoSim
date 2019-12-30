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
			System.out.print(dbl + " ");
		}
		System.out.println();
	}
} //end Util
