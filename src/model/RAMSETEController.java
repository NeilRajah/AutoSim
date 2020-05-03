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

		
	
} //end class