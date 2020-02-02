/**
 * JerkProfile
 * Author: Neil Balaskandarajah
 * Created on: 13/01/2020
 * A 1-dimensional 5-segment motion profile
 */

package model.motion;

import model.DriveLoop;
import model.Robot;
import util.Util;

public class JerkProfile {
	//Attributes
	//Configured
	private DriveLoop loop;
	private Robot robot;
	private double totalDist;
	private double accDist;
	private double decDist;
	
	//Computed
	private double kA;
	private double kV;
	
	public JerkProfile(DriveLoop loop, double totalDist, double accDist, double decDist) {
		/* 
		 * Error if:
		 * accDist > totalDist
		 * decDist > totalDist
		 * accDist + decDist > totalDist
		 */
		this.robot = loop.getRobot();
		
		computeConstants();
	}
	
	//constructor with ramp dist, sets accDist and decDist to be the same
	//error if rampDist > totalDist
	
	private void computeConstants() {
		
	}
	
	
} //end JerkProfile
