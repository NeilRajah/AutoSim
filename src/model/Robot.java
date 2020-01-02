/**
 * Robot
 * Author: Neil Balaskandarajah
 * Created on: 28/12/2019
 * A simple physics model for a differential drive robot
 */

package model;

import java.awt.Color;

import util.Util;

public class Robot {
	//Attributes
	//Configured
	private double kMass; //mass of the robot in pounds
	private double kLength; //length of the robot in inches
	private double kWidth; //width of the robot in inches
	private Gearbox leftGearbox; //left gearbox
	private Gearbox rightGearbox; //right gearbox
	
	//Kinematics
	private double averagePos; //position of robot
	private double angularVel; //angular speed of the robot
	private double linearVel; // translational speed of robot
	
	//Pose
	private double heading; //heading of the robot in degrees
	private double yaw; //yaw of the robot
	private Point point; //point representing robot's position on the field
	
	//Graphics
	private Color color; //color of the robot
	
	//Computed constants
	private double kWheelRad; //wheel radius in meters
	private double kMOI; //moment of inertia in kg*m^2
	private double kPivotArm; //distance from robot center to wheel in meters (used in torque calculations)
	private double fP; //constant used in gearbox acceleration calculation
	private double fM; //constant used in gearbox acceleration calculation
	
	/**
	 * Create a robot with base parameters
	 * double kWheelDia - wheel diameter in inches
	 * double kMass - mass of the robot in pounds
	 * double kLength - length of the robot in inches
	 * double kWidth - width of the robot in inches
	 * Gearbox gearbox - drive gearboxes
	 */
	public Robot(double wheelDia, double mass, double length, double width, Gearbox gearbox) {
		this.kMass = mass * Util.LBS_TO_KG; //convert to kg
		this.kLength = length * Util.INCHES_TO_METERS; //convert to m
		this.kWidth = width * Util.INCHES_TO_METERS; //convert to m
		this.kWheelRad = wheelDia/2 * Util.INCHES_TO_METERS; //convert radius to m
		
		//create gearboxes
		this.leftGearbox = new Gearbox(gearbox.getGearRatio(), new Motor(gearbox.getMotorParameters()),gearbox.getNumMotors());
		this.rightGearbox = new Gearbox(gearbox.getGearRatio(), new Motor(gearbox.getMotorParameters()),gearbox.getNumMotors());
		
		//compute constants
		computeConstants();
		
		//set values to zero
		reset();
	} //end constructor
	
	/**
	 * Compute constants of the robot
	 */
	private void computeConstants() {
		//robot simplified to rectangular slab rotating about axis through center perpendicular to surface
		kMOI = kMass * (kLength * kLength + kWidth * kWidth) / 12;
		
		//half the width of the robot
		kPivotArm = kWidth/2;
		
		//constants used in force calculations
		fM = (1/kMass) - (kPivotArm * kPivotArm)/kMOI;
		fP = (1/kMass) + (kPivotArm * kPivotArm)/kMOI;
	} //end computeConstants
	
	/**
	 * Reset the gearboxes, pose and kinematics of the robot
	 */
	public void reset() {
		averagePos = 0;
		heading = 0;
		point = new Point(0,0);
		leftGearbox.reset();
		rightGearbox.reset();
		angularVel = 0;
		linearVel = 0;
	} //end reset
	
	//Kinematics
	
	/**
	 * Get the average distance travelled by robot
	 * return average - average distance travelled by robot in inches
	 */
	public double getAveragePos() {
		return averagePos;
	} //end getAveragePos
	
	/**
	 * Get the linear velocity of the robot
	 * return linearSpeed - linear speed of the robot in inches per second
	 */
	public double getLinearVel() {
		return linearVel;
	} //end getLinearVel
	
	/**
	 * Get the angular velocity of the robot
	 * return angularSpeed - angular speed of the robot in radians per second
	 */
	public double getAngularVel() {
		return angularVel;
	} //end getAngularVel
	
	//Pose
	
	/**
	 * Get the coordinates of the robot
	 * return - (x,y) values of the robot
	 */
	public Point getPoint() {
		return new Point(point.getX(), point.getY());
	} //end getPoint
	
	/**
	 * Get the heading of the robot
	 * return heading - heading of the robot in radians
	 */
	public double getHeading() {
		return heading;
	} //end getHeading
	
	/**
	 * Set the heading of the robot
	 * double heading - heading in radians for the robot to point at
	 */
	public void setHeading(double heading) {
		this.heading = heading;
	} //end setHeading

	//Dynamics
	
	/**
	 * Update the pose of the robot given voltages applied over an interval
	 * @param leftVoltage - voltage applied to left gearbox
	 * @param rightVoltage - voltage applied to right gearbox
	 */
	public void update(double leftVoltage, double rightVoltage) {
		//calculate force exerted by each gearbox on robot
		double leftForce = leftGearbox.calcTorque(leftVoltage) / kWheelRad;
		double rightForce = rightGearbox.calcTorque(rightVoltage) / kWheelRad;
		
		//calculate each side's acceleration 
		updateGearboxes(leftForce, rightForce);
		
		//update the speeds of the robot
		updateSpeeds();
		
		//update the pose of the robot
		updatePose();
	} //end update
	
	/**
	 * Calculate the accelerations of each side of the drive
	 * double leftTorque - torque of the left gearbox
	 * double rightTorque - torque of the right gearbox
	 */
	private void updateGearboxes(double leftForce, double rightForce) {
		//calculate accelerations using forces
		double leftAcc = (fP * leftForce + fM * rightForce) / kWheelRad; //convert from m/s^2 to rad/s^2
		double rightAcc = (fM * leftForce + fP * rightForce) / kWheelRad;
				
		//update the position and velocity of the gearbox
		leftGearbox.update(leftAcc);
		rightGearbox.update(rightAcc);
	} //end updateGearboxes
	
	/**
	 * Calculate the angular velocity of the robot and update its heading
	 */
	private void updateSpeeds() {
		//angular velocity based on left, right wheel velocities and robot width
		angularVel = (kWheelRad / (2 * kPivotArm)) * (rightGearbox.getVel() - leftGearbox.getVel());
		
		//linear speed is average of two velocities
		linearVel = (rightGearbox.getVel() + leftGearbox.getVel()) / 12;
	} //end updateHeading
	
	/**
	 * Update the pose of the robot
	 */
	private void updatePose() {
		//calculate the magnitude of displacement in time interval
		double leftDisp = leftGearbox.getPos(); 
		double rightDisp = rightGearbox.getPos();
		double disp = (leftDisp + rightDisp) / 2;
		
		//update average position and coordinates
		double newPos = disp * kWheelRad / Util.INCHES_TO_METERS;
		point.translate(newPos - averagePos, heading);
		averagePos = newPos;
		
		//update heading
		heading += angularVel * Util.UPDATE_PERIOD;
	} //end update pose
} //end Robot
