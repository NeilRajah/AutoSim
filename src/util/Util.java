/**
 * Util
 * Author: Neil Balaskandarajah
 * Created on: 10/11/2019
 * Holds static utility methods
 */
package util;

public class Util {

	public static void println(String ... s) {
		for(String str : s) {
			System.out.print(s + " ");
		}
		System.out.println();
	} //end println
	
	public static void println(double ... d) {
		for(double dbl : d) {
			System.out.print(d + " ");
		}
		System.out.println();
	} //end println
} //end Util
