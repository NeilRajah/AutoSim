/**
 * Motor
 * Author: Neil Balaskandarajah
 * Created on: 28/12/2019
 * Physics model for a standard DC motor
 */
package model;

import util.Util;

public class Motor {
	//Constants
	public static final double MAX_VOLTAGE = 12.0; //max voltage in Volts
	
	//Attributes
	//Configured
	private double kStallTorque; //stall torque in Nm
	private double kStallCurrent; //stall current in Amps
	private double kFreeSpeed; //free speed in RPM
	private double kFreeCurrent; //free current in Amps
	
	//Calculated
	private double kResistance; //resistance across the motor
	private double kVoltage; //radians/second per volt applied to the motor

	/**
	 * Create a motor with given parameters
	 * double kStallTorque - stall torque in Nm
	 * double kStallCurrent - stall current in Amps
	 * double kFreeSpeed - free speed in RPM
	 * double kFreeCurrent - free current in Amps
	 */
	public Motor(double stallTorque, double stallCurrent, double freeSpeed, double freeCurrent) {
		this.kStallTorque = stallTorque;
		this.kStallCurrent = stallCurrent;
		this.kFreeSpeed = freeSpeed;
		this.kFreeCurrent = freeCurrent;
		
		//calculated values
		//resistance across is total voltage divided by stall current
		kResistance = MAX_VOLTAGE/kStallCurrent;
		
		//angular velocity of the motor per volt applied
		kVoltage = (kFreeSpeed * (Math.PI/30.0)) / (MAX_VOLTAGE - (kResistance * kFreeCurrent) + Util.V_INTERCEPT);
	} //end constructor

	/**
	 * Get the stall torque of the motor
	 * return kStallTorque - stall torque of the motor in Nm
	 */
	public double getStallTorque() {
		return kStallTorque;
	} //end getStallTorque
	
	/**
	 * Get the stall current of the motor
	 * return kStallCurrent - stall current of the motor in Amps
	 */
	public double getStallCurrent() {
		return kStallCurrent;
	} //end getStallCurrent
	
	/** MAY REMOVE
	 * Get the free speed of the motor 
	 * return kFreeSpeed - free speed of the motor in radians/second
	 */
	public double getFreeSpeed() {
		return kFreeSpeed;
	} //end getFreeSpeed
	
	/** MAY REMOVE
	 * Get the free current of the motor
	 * return kFreeCurrent - free current of the motor in Amps
	 */
	public double getFreeCurrent() {
		return kFreeCurrent;
	} //end getFreeCurrent
	
	/**
	 * Get the resistance constant of the motor
	 * return - resistance across the motor in Ohms
	 */
	public double getResistanceConstant() {
		return kResistance;
	} //end getFreeCurrent
	
	/**
	 * Get the voltage constant of the motor
	 * return - radians/second per volt applied to the motor
	 */
	public double getVoltageConstant() {
		return kVoltage;
	} //end getFreeCurrent
} //end Motor
