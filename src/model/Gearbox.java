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
	
	//Calculated
	private double kTorque; //Nm per amp applied to the motor
	private double position; //amount the motor has turned in radians
	private double velocity; //velocity of the motor in radians per second
	private double acceleration; //acceleration of the motor in radians per second^2
	
	/**
	 * Create a gearbox with given parameters
	 * double kGearRatio - gear reduction of the gearbox
	 * Motor kMotor - motor used in the gearbox
	 * double kNumMotors - number of motors in the gearbox
	 */
	public Gearbox(double kGearRatio, Motor kMotor, double kNumMotors) {
		this.kGearRatio = kGearRatio;
		this.kMotor = kMotor;
		
		//calculate constants
		kTorque = (kNumMotors * kMotor.getStallTorque()) / kMotor.getStallCurrent();
		
		position = 0;
		velocity = 0;
		acceleration = 0;
	} //end constructor
	
	//Getters
	
	/**
	 * Get the torque constant of the gearbox
	 * return - gearbox stall torque divided by gearbox stall current
	 */
	public double getTorqueConstant() {
		return kTorque;
	} //end getTorqueConstant
	
	/**
	 * Get the gear ratio of the gearbox
	 * return kGearRatio - reduction of the gearbox
	 */
	public double getGearRatio() {
		return kGearRatio;
	} //end getGearRatio
	
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
	
	/**
	 * Get the position of the gearbox
	 * return position - position of the gearbox in radians
	 */
	public double getPos() {
		return position;
	} //end getPos
	
	/**
	 * Get the velocity of the gearbox
	 * return velocity - velocity of the gearbox in radians
	 */
	public double getVel() {
		return velocity;
	} //end getVel
	
	/**
	 * Get the acceleration of the gearbox
	 * return acceleration - acceleration of the gearbox in radians
	 */
	public double getAcc() {
		return acceleration;
	} //end getAcc
	
	/**
	 * Set the acceleration of the gearbox
	 * double acc - acceleration of the gearbox
	 */
	public void setAcc(double acc) {
		this.acceleration = acc;
	} //end setAcc
	
	//Dynamics
	
	/**
	 * Calculate the torque the motor provides given a specified voltage
	 * double voltage - voltage applied to the motor
	 * return - calculated torque based on voltage
	 */
	public double calcTorque(double voltage) {
		//torque is proportional to voltage applied and angular velocity of the motor
		double cVoltage = (kGearRatio * kTorque) / (kMotor.getResistanceConstant());
		double cVelocity = (kGearRatio * kGearRatio * kTorque) / (kMotor.getResistanceConstant() * kMotor.getVoltageConstant());
		
		return cVoltage * voltage + cVelocity * velocity;
	} ///end calcTorque
	
	/**
	 * Update the position and velocity of the gearbox by assuming constant acceleration in a timestamp
	 * double dt - time interval of constant acceleration
	 */
	public void update(double dt) {
		velocity += acceleration * dt; //v2 = v1 + at
		position += velocity * dt + 0.5 * acceleration * dt *dt; //d2 = d1 + vt + 0.5at^2
	} //end update
} //end Gearbox
