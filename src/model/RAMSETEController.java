/**
 * RAMSETEController
 * Author: Neil Balaskandarajah
 * Created on: 02/05/2020
 * Nonlinear feedback controller for differential drive robot
 */

package model;

import util.Util;

public class RAMSETEController {
	//Attributes
	private double kBeta; //proportional constant
	private double kZeta; //damping constant [0,1]

	/**
	 * Create a RAMSETE controller with default constants
	 */
	public RAMSETEController() {
		this.kBeta = Util.kBETA;
		this.kZeta = Util.kZETA;
	} //end constructor

	/**
	 * Calculate the output of the controller
	 * @param current Current pose (x,y,theta)
	 * @param goal Goal pose (x,y,theta)
	 * @param goalLin Goal linear velocity (ft/s)
	 * @param goalAng Goal angular velocity (rad/s)
	 * @return New left and right wheel speeds
	 */
	public double[] calcWheelSpeeds(Pose current, Pose goal, double goalLin, double goalAng, double trackWidth) {
		//deltas
		double dx = goal.getX() - current.getX();
		double dy = goal.getY() - current.getY();
		double theta = current.getHeading();
		
		//errors
		double eX = dx * Math.cos(theta) + dy * Math.sin(theta);
		double eY = dx * -Math.sin(theta) + dy * Math.cos(theta);
		eY *= -1;
		eX *= 1;
		double eTheta = goal.getHeading() - current.getHeading();
		
		//k constant for output calculating (from stability function)
		double k = 2 * kZeta * Math.sqrt(Math.pow(goalAng, 2) + kBeta * Math.pow(goalLin, 2));
		double sinc = Util.fuzzyEquals(eTheta, 0, 0.01) ? 1.0 : Math.sin(eTheta) / eTheta;
		
		//outputs
		double v = goalLin * Math.cos(eTheta) + k * eY;
		double w = goalAng + k * eTheta + kBeta * goalLin * sinc * eX;
		
		//wheel speeds
		double left = (2 * v + w * trackWidth) / 2;
		double right = (2 * v - w * trackWidth) / 2;
		
		Util.println(k, sinc, v, w);
		
		return new double[] {left, right};
	} //end calcWheelSpeeds
	
} //end class