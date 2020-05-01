/**
 * ProfileGenerator
 * Author: Neil Balaskandarajah
 * Created on: 30/04/2020
 * Save profiles to files to be read and played
 */

package model.motion;

import util.FieldPoints;
import util.Util;

public class ProfileGenerator {

	public static void main(String[] args) {
		double[][] curve = FieldPoints.niceLongCurve;
		long init = System.currentTimeMillis();
		BezierProfile bezTraj = new BezierProfile(curve, 30, 12 * 12, 200, 200);
		Util.println(System.currentTimeMillis() - init + "ms");
		Util.println(bezTraj.saveVelsToFile("niceLongCurve"));
	} //end main
} //end class