/**
 * PIDController
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2019
 * A simple PID controller that assumes regular loop intervals
 */
package model;

import util.Util;

public class PIDController {
	//Attributes
	//Configured
	private double kP; //proportionality constant
	private double kI; //integral constant
	private double kD; //derivative constant
	private double topSpeed; //max velocity of the robot

	//Calculated
	private double errorSum; //sum of all errors
	private double lastError; //previous error
	private boolean atTarget; //whether within epsilon bounds

	/**
	 * Create a PID controller with gains
	 * @param p Proportionality constant
	 * @param i Integral constant
	 * @param d Derivative constant
	 * @param topSpeed Top speed the controller can output
	 */
	public PIDController(double p, double i, double d, double topSpeed) {
		//set attributes
		kP = p;
		kI = i;
		kD = d;
		this.topSpeed = topSpeed;
		
		errorSum = 0; // initialize errorSum to 0
		lastError = 0; // initialize lastError to 0
	} //end constructor

	//Attributes
	
	/**
	 * Return whether the error is within the epsilon bounds or not
	 * @return atTarget - whether controller is at target
	 */
	public boolean isDone() {
		return atTarget;
	} //end isDone

	/**
	 * Get the proportionality constant of the controller
	 * @return kP - proportionality constant
	 */
	public double getP() {
		return kP;
	} //end getP

	/**
	 * Get the integral constant of the controller
	 * @return kI - integral constant
	 */
	public double getI() {
		return kI;
	} //end getI

	/**
	 * Get the derivative constant of the controller
	 * @return kD - derivative constant
	 */
	public double getD() {
		return kD;
	} //end getD
	
	/**
	 * Reset the controller for the next set of calculations
	 */
	public void reset() {
		errorSum = 0;
		lastError = 0;
		atTarget = false;
	} //end reset
	
	//Calculations
	
	/**
	 * Calculate the PID output based on the setpoint, current value and tolerance
	 * @param setpoint - setpoint to reach
	 * @param current - current value
	 * @param epsilon - range the controller can be in to be considered "at goal"
	 * @return - calculated PID output in volts
	 */
	public double calcPID(double setpoint, double current, double epsilon) {		
		//calculate error
		double error = setpoint - current;
		
		//update atTarget
		atTarget = Math.abs(error) <= epsilon;
		
		//proportional output
		double pOut = kP * error;
		
		//integral output
		errorSum += error;
		double iOut = kI * errorSum;
		
		//derivative output
		double dOut = 0;
		if (lastError != 0)
			dOut = kD * (error - lastError);
		lastError = error;
		
		//output is sum of each constant's output
		return pOut + iOut + dOut;
	} //end calcPID
	
	/**
	 * Calculate a regulated PID output based on setpoint, current value and tolerance
	 * @param setpoint setpoint to reach
	 * @param current current value
	 * @param epsilon range the controller can be in to be considered "at goal"
	 * @param topSpeed magnitude of top of speed limit
	 * @param minSpeed magnitude of bottom of speed limit
	 * @return - regulated PID output in volts
	 */
	public double calcRegulatedPID(double setpoint, double current, double epsilon, double topSpeed, double minSpeed) {
		double output = calcPID(setpoint, current, epsilon);
		double topLimit = 12 * (Math.abs(topSpeed) / this.topSpeed);
		double botLimit = 12 * (Math.abs(minSpeed) / this.topSpeed);
		
		return Util.regulatedClamp(output, botLimit, topLimit);
	} //end calcRegulatedPID
} //end class
