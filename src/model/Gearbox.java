/**
 * Gearbox
 * Author: Neil Balaskandarajah
 * Created on: 28/12/2019
 * Simple physics model for a gearbox powered by DC motors
 */
package model;

import util.Util;

public class Gearbox {
	//Attributes
	//Configured
	private double kGearRatio; //gear reduction of the gearbox 
	private Motor kMotor; //motor used in the gearbox
	private double kNumMotors; //number of motors in the gearbox
	
	//Calculated
	private double kPosition; //amount the motor has turned in radians
	private double kVelocity; //velocity of the motor in radians per second
	private double kAcceleration; //acceleration of the motor in radians per second^2
	
	//Computed constants
	private double cVoltage; //constant proportional to voltage in torque calculations
	private double cVelocity; //constant proportional to velocity in torque calculations
	
	/**
	 * Create a gearbox with given parameters
	 * double kGearRatio - gear reduction of the gearbox
	 * Motor kMotor - motor used in the gearbox
	 * double kNumMotors - number of motors in the gearbox
	 */
	public Gearbox(double gearRatio, Motor motor, double numMotors) {
		this.kGearRatio = gearRatio;
		this.kMotor = motor;
		this.kNumMotors = numMotors;
		
		//calculate constants
		computeConstants();
		
		//set the kinematics to zero
		reset();
	} //end constructor
	
	/**
	 * Compute constants of the gearbox
	 */
	private void computeConstants() {
		//constants used in torque calculation
		cVoltage = (kGearRatio * kMotor.getTorqueConstant() * kNumMotors) / (kMotor.getResistanceConstant());
		cVelocity = -(kGearRatio * kGearRatio * kMotor.getTorqueConstant() * kNumMotors) / 
						(kMotor.getResistanceConstant() * kMotor.getVoltageConstant());
	} //end computeConstants
	
	//Attributes
	/**
	 * Get the gear ratio of the gearbox
	 * return kGearRatio - reduction of the gearbox
	 */
	public double getGearRatio() {
		return kGearRatio;
	} //end getGearRatio
	
	/**
	 * Get the number of motors in the gearbox
	 * return kNumMotors - number of motors in the gearbox
	 */
	public double getNumMotors() {
		return kNumMotors;
	} //end getNumMotors
	
	/**
	 * Get the parameter array for the motors in the gearbox
	 * return - array of motor parameters
	 */
	public double[] getMotorParameters() {
		return kMotor.getParameters();
	} //end getMotorName
	
	//Kinematics
	
	/**
	 * Get the position of the gearbox
	 * return position - position of the gearbox in radians
	 */
	public double getPos() {
		return kPosition;
	} //end getPos
	
	/**
	 * Get the velocity of the gearbox
	 * return velocity - velocity of the gearbox in radians
	 */
	public double getVel() {
		return kVelocity;
	} //end getVel
	
	/**
	 * Zero the velocity of the gearbox
	 */
	public void zeroVel() {
		kVelocity = 0;
	} //end zeroVel
	
	/**
	 * Get the acceleration of the gearbox
	 * return acceleration - acceleration of the gearbox in radians
	 */
	public double getAcc() {
		return kAcceleration;
	} //end getAcc
	
	//Dynamics
	
	/**
	 * Calculate the torque the motor provides given a specified voltage
	 * double voltage - voltage applied to the motor
	 * return - calculated torque based on voltage
	 */
	public double calcTorque(double voltage) {
		//torque is proportional to voltage applied and angular velocity of the motor		
		return cVoltage * voltage + cVelocity * kVelocity;
	} ///end calcTorque
	
	/**
	 * Update the position and velocity of the gearbox by assuming constant acceleration in a timestamp
	 * double acceleration - acceleration of the gearbox for this timestamp
	 */
	public void update(double acceleration) {
		this.kAcceleration = acceleration; //save the acceleration to memory
		
		//v2 = v1 + at
		this.kVelocity += this.kAcceleration * Util.UPDATE_PERIOD; 
		
		//d2 = d1 + vt + 0.5at^2
		this.kPosition += this.kVelocity * Util.UPDATE_PERIOD + 0.5 * this.kAcceleration * Util.UPDATE_PERIOD * Util.UPDATE_PERIOD;
	} //end update
	
	/**
	 * Reset the kinematics of the gearbox
	 */
	public void reset() {
		kPosition = 0;
		kVelocity = 0;
		kAcceleration = 0;
	} //end reset
} //end Gearbox
