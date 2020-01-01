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
	
	//Calculated
	private double kWheelRad; //wheel radius in meters
	private double kMOI; //moment of inertia in kg*m^2
	private double kPivotArm; //distance from robot center to wheel in meters (used in torque calculations)
	
	//Kinematics
	private double averagePos; //position of robot
	private double heading; //heading of the robot in egrees
	private double yaw; //yaw of the robot
	
	private double angularSpeed; //angular speed of the robot
	private double linearSpeed; // translational speed of robot
	
	private Point point; //point representing robot's position on the field
	
	//Graphics
	private Color color; //color of the robot
	
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
		this.leftGearbox = gearbox;
		this.rightGearbox = gearbox;
		
		//Calculate constants
		//convert radius to meters
		kWheelRad = wheelDia/2 * Util.INCHES_TO_METERS;
		
		//robot simplified to rectangular slab rotating about axis through center perpendicular to surface
		kMOI = kMass * (kLength * kLength + kWidth * kWidth) / 12;
		
		//half the width of the robot
		kPivotArm = kWidth/2;
		
		//set values to zero
		averagePos = 0;
		heading = 0;
		point = new Point(0,0);
		angularSpeed = 0;
	} //end constructor
	
	public void reset() {
		averagePos = 0;
		heading = 0;
		point = new Point(0,0);
		leftGearbox.reset();
		rightGearbox.reset();
		angularSpeed = 0;
	}
	
	public double getAveragePos() {
		return averagePos;
	}
	
	public double getAverageVel() {
		return (leftGearbox.getVel() + rightGearbox.getVel()) * 0.5 * kWheelRad / Util.INCHES_TO_METERS / 12;
	}
	
	public Point getPoint() {
		return point;
	}
	
	public double getHeading() {
		return heading;
	}
	
	//testing
	public Gearbox getLeftGearbox() {
		return leftGearbox;
	}
	
	public Gearbox getRightGearbox() {
		return rightGearbox;
	}

	public void update(double leftVoltage, double rightVoltage) {//calculate torque from each gearbox
		double leftTorque = leftGearbox.calcTorque(leftVoltage);
		double rightTorque = rightGearbox.calcTorque(rightVoltage);
		
		//calculate each side's acceleration
		updateGearboxes(leftTorque, rightTorque);
		
		//update the pose of the robot
		updateHeading(leftTorque, rightTorque, Util.UPDATE_PERIOD);
		updatePose(Util.UPDATE_PERIOD);
	} //end update
	
	/**
	 * Calculate the accelerations of each side of the drive
	 * double leftTorque - torque of the left gearbox
	 * double rightTorque - torque of the right gearbox
	 */
	private void updateGearboxes(double leftTorque, double rightTorque) {
		double S = 1 / (kMass * kWheelRad * kWheelRad);
		double D = (kPivotArm * kPivotArm) / (kMOI * kWheelRad * kWheelRad);
		
		double sum = (leftTorque + rightTorque);
		double difference = (rightTorque - leftTorque);
		
		double leftAcc = (S * sum) - (D * difference);
		double rightAcc = (S * sum) + (D * difference);	
		
//		Util.println("Left and right accelerations sent to gearboxes:", leftAcc, rightAcc);
		
		//update the position and velocity of the gearbox
		leftGearbox.update(leftAcc, Util.UPDATE_PERIOD);
		rightGearbox.update(rightAcc, Util.UPDATE_PERIOD);
	} //end updateGearboxes
	
	private void updateHeading(double leftTorque, double rightTorque, double dt) {
		angularSpeed = (kWheelRad / (2 * kPivotArm)) * (rightGearbox.getVel() - leftGearbox.getVel());
		double angDisp = angularSpeed * dt;
		
		heading += angDisp;
	}
	
	private void updatePose(double dt) {
		//calculate the magnitude of displacement in time interval
		double leftDisp = leftGearbox.getPos(); //kWheelRad / Util.INCHES_TO_METERS
		double rightDisp = rightGearbox.getPos();
		double disp = (leftDisp + rightDisp) / 2;
		
		//update average position and coordinates
		double newPos = disp * kWheelRad / Util.INCHES_TO_METERS;
		point.translate(newPos - averagePos, heading);
		averagePos = newPos;
	}
} //end Robot
