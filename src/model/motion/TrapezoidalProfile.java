/**
 * TrapezoidalProfile
 * Author: Neil Balaskandarajah
 * Created on: 15/04/2020
 * Simple linear trapezoidal profile
 */

package model.motion;

import util.Util;

public class TrapezoidalProfile extends DriveProfile {
	//Attributes
	private double dT; //total distance in inches
	private double dA; //acceleration distance in inches
	private double dD; //deceleration distance in inches
	private double vM; //cruising velocity in ft/s
	private double tT; //total time
	private double tA; //acceleration time
	private double tD; //deceleration time
	private double acc; //acceleration constant 
	private double dec; //deceleration constant
	
	/**
	 * Create a profile given a total distance, acceleration distance and a max velocity
	 * @param totalDist
	 * @param accDist
	 * @param maxVel
	 */
	public TrapezoidalProfile(double totalDist, double accDist, double maxVel) {
		//set attributes
		this.dT = totalDist / 12; //convert to feet
		this.dA = accDist / 12; //feet
		this.dD = accDist / 12; //feet
		this.vM = maxVel;
		
		computeConstants();
		fillProfiles();
	} //end constructor

	/**
	 * Compute the profile constants
	 */
	protected void computeConstants() {
		//acceleration and deceleration
		this.acc = (vM * vM) / (2 * dA); //v^2 / 2d
		this.dec = -(vM * vM) / (2 * dD); //v^2 / 2d, negative for opposite direction
		
		//times
		this.tA = Math.sqrt((2 * dA) / (acc)); //sqrt(2d/a)
		this.tD = Math.sqrt((2 * dD) / (Math.abs(dec))); //sqrt(2d/a)
		double tC = (dT - dA - dD) / vM; //cruise dist / cruise vel
		this.tT = tA + tC + tD;
		
		//profile
		this.totalTime = tT;
	} //end computeConstants
	
	/**
	 * Fill the left and right sides using the trapezoidal motion algorithm
	 */
	protected void fillProfiles() {
		double p = 0; //position
		double v = 0; //velocity
		double a = 0; //acceleration
		double dt = Util.UPDATE_PERIOD; //time interval
		
		int loops = (int) (tT / dt); //number of timesteps
		
		//add first points
		this.leftProfile.add(new double[] {0,0, acc});
		this.rightProfile.add(new double[] {0,0, acc});
		
		for (int i = 1; i < loops; i++) {
			double t = i * dt;
			if (t < tA) { //accelerating
				a = acc;
				v += a * dt; //v = at
				p += v * dt + 0.5 * a * dt * dt; //p = vt + 0.5at^2
				
			} else if (t >= tA && t < (tT - tD)) { //cruising
				a = 0;
				v = vM;
				p += v * dt;
				
			} else { //decelerating
				a = dec;
				v += dec * dt;
				p += v * dt + 0.5 * a * dt * dt; //p = vt + 0.5at^2
			} //if
			
			//add trajectory points to lists
			this.leftProfile.add(new double[] {p,v,a});
			this.rightProfile.add(new double[] {p,v,a});
		} //loop
	} //end fillProfiles
} //end class