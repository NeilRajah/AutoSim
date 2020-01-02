/**
 * Gearbox
 * Author: Neil Balaskandarajah
 * Created on: 28/12/2019
 * Simple physics model for a gearbox powered by DC motors
 */
package model;

public class Gearbox {
	//Attributes
	//Configured
	private double kGearRatio; //gear reduction of the gearbox 
	private Motor kMotor; //motor used in the gearbox
	private double kNumMotors; //number of motors in the gearbox
	
	//Calculated
	private double kTorque; //Nm per amp applied to the motor
	private double kPosition; //amount the motor has turned in radians
	private double kVelocity; //velocity of the motor in radians per second
	private double kAcceleration; //acceleration of the motor in radians per second^2
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
		kTorque = (numMotors * motor.getStallTorque()) / motor.getStallCurrent();
		
		//set the kinematics to zero
		reset();
	} //end constructor
	
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
	
	//Constants
	
	/**
	 * Get the torque constant of the gearbox
	 * return - gearbox stall torque divided by gearbox stall current
	 */
	public double getTorqueConstant() {
		return kTorque;
	} //end getTorqueConstant
	
	/**
	 * Get the voltage constant of the gearbox
	 * return - voltage constant of the motors used in the gearbox
	 */
	public double getVoltageConstant() {
		return kMotor.getVoltageConstant();
	} //end getVoltageConstant
	
	/**
	 * Get the electrical resistance in the gearbox
	 * return - resistance across the motors used in the gearbox
	 */
	public double getResistanceConstant() {
		return kMotor.getResistanceConstant();
	} //end getResistanceConstant
	
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
		double cVoltage = (kGearRatio * kTorque) / (kMotor.getResistanceConstant());
		double cVelocity = -(kGearRatio * kGearRatio * kTorque) / (kMotor.getResistanceConstant() * kMotor.getVoltageConstant());
		
		return cVoltage * voltage + cVelocity * kVelocity;
	} ///end calcTorque
	
	/**
	 * Update the position and velocity of the gearbox by assuming constant acceleration in a timestamp
	 * double dt - time interval of constant acceleration
	 */
	public void update(double acceleration, double dt) {
		this.kAcceleration = acceleration;
		this.kVelocity += this.kAcceleration * dt; //v2 = v1 + at
		this.kPosition += this.kVelocity * dt + 0.5 * this.kAcceleration * dt * dt; //constant vel in that time interval
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
