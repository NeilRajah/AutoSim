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
	 * Gearbox leftGearbox - left gearbox
	 * Gearbox rightGearbox - right gearbox
	 */
	public Robot(double wheelDia, double mass, double length, double width, Gearbox leftGearbox, Gearbox rightGearbox) {
		this.kMass = mass * Util.LBS_TO_KG; //convert to kg
		this.kLength = length * Util.INCHES_TO_METERS; //convert to m
		this.kWidth = width * Util.INCHES_TO_METERS; //convert to m
		this.leftGearbox = leftGearbox;
		this.rightGearbox = rightGearbox;
		
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
	} //end constructor
	
	public double getAveragePos() {
		return averagePos;
	}
	
	public double getAverageVel() {
		return (leftGearbox.getVel() + rightGearbox.getVel()) * 0.5 * kWheelRad / Util.INCHES_TO_METERS / 12;
	}

	public void update(double leftVoltage, double rightVoltage) {
		//calculate torque from each gearbox
		double leftTorque = leftGearbox.calcTorque(leftVoltage);
		double rightTorque = rightGearbox.calcTorque(rightVoltage);

		Util.println("Gearbox Torques:", leftTorque, rightTorque);
//		Util.println("Gearbox Torques:", leftGearbox.calcTorque(leftVoltage), rightGearbox.calcTorque(rightVoltage));
		
		//calculate each side's acceleration
		updateGearboxes(leftTorque, rightTorque);
//		Util.println("Post CGA:", leftGearbox.getAcc(), rightGearbox.getAcc());
		
		
		//update the pose of the robot
		updatePose(Util.UPDATE_PERIOD);
	} //end update
	
	/**
	 * Calculate the accelerations of each side of the drive
	 * double leftTorque - torque of the left gearbox
	 * double rightTorque - torque of the right gearbox
	 */
	private void updateGearboxes(double leftTorque, double rightTorque) {
		double A = (leftTorque + rightTorque) / (kMass * kWheelRad * kWheelRad);
		double B = ((rightTorque - leftTorque) * kPivotArm * kPivotArm) / (kMOI * kWheelRad * kWheelRad);
		
		Util.println("Acceleration Constants:", A, B);
//		Util.println("Robot Constants:", kPivotArm, kMOI, kWheelRad);
		
		leftGearbox.setAcc(A - B);
		rightGearbox.setAcc(A + B);
		
		//update the position and velocity of the gearbox
		leftGearbox.update(Util.UPDATE_PERIOD);
		Util.println("L Gearbox Kinematics:", leftGearbox.getPos(),
						leftGearbox.getVel(), leftGearbox.getAcc());
//		rightGearbox.update(Util.UPDATE_PERIOD);
		Util.println("R Gearbox Kinematics:", rightGearbox.getPos(),
				rightGearbox.getVel(), rightGearbox.getAcc());
	} //end setAccelerations
	
	private void updatePose(double dt) {
		//calculate the magnitude of displacement in time interval
		double leftDisp = leftGearbox.getPos(); //kWheelRad / Util.INCHES_TO_METERS
		double rightDisp = rightGearbox.getPos();
		double disp = (leftDisp + rightDisp) / 2;
		
		//calculate heading assuming constant angular acceleration
		double angAcc = (kWheelRad * (rightGearbox.getAcc() - leftGearbox.getAcc())) / (2 * kPivotArm);
		double angVel = angAcc * dt;
		double angDisp = angVel * dt + 0.5 * angAcc * dt * dt;
		
//		Util.println("Gearbox Differences:", (rightGearbox.getVel() - leftGearbox.getVel()));
		
		//update average position, heading and coordinates
		averagePos = disp * kWheelRad / Util.INCHES_TO_METERS;
		heading -= angDisp;
//		point.translate(disp, heading);
	}
} //end Robot
