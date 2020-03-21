/**
 * Robot
 * Author: Neil Balaskandarajah
 * Created on: 28/12/2019
 * A simple physics model for a differential drive robot
 */

package model;

import java.awt.Color;
import java.util.HashMap;

import main.AutoSim;
import util.Util;
import util.Util.ROBOT_KEY;

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
	private String commandName; //name of the command
	private Point goalPoint; //used for graphics
	
	//Computed constants
	private double kWheelRad; //wheel radius in meters
	private double kMOI; //moment of inertia in kg*m^2
	private double kPivotArm; //distance from robot center to wheel in meters (used in torque calculations)
	private double fP; //constant used in gearbox acceleration calculation
	private double fM; //constant used in gearbox acceleration calculation
	private double maxLinSpeed; //top linear speed of the robot
	private double maxAngSpeed; //top angular speed of the robot
	
	/**
	 * Create a robot with base parameters
	 * double kWheelDia - wheel diameter in inches
	 * double kMass - mass of the robot in pounds
	 * double kLength - length of the robot in inches
	 * double kWidth - width of the robot in inches
	 * Gearbox gearbox - drive gearboxes
	 */
	public Robot(double wheelDia, double mass, double length, double width, Gearbox gearbox) {
		//set attributes in metric units
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
	 * Safely get a copy of this Robot
	 * @return - identical copy of this Robot 
	 */
	public Robot clone() {
		//convert the constants to imperial
		double wheelDia = kWheelRad * 2 / Util.INCHES_TO_METERS;
		double mass = kMass / Util.LBS_TO_KG;
		double length = kLength / Util.INCHES_TO_METERS;
		double width = kWidth / Util.INCHES_TO_METERS;
		
		//left gearbox identical to right gearbox, doesn't matter which is used
		return new Robot(wheelDia, mass, length, width, leftGearbox.clone());
	} //end clone
	
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
		
		//top speeds
		maxLinSpeed = (Math.PI * leftGearbox.getMotorParameters()[0] * kWheelRad)/
					(360 * leftGearbox.getGearRatio() * Util.INCHES_TO_METERS);
		maxAngSpeed = (24 * maxLinSpeed * Util.INCHES_TO_METERS) / (kWidth);
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
		color = Color.YELLOW;
		commandName = "";
	} //end reset
	
	/**
	 * Set the robot into waiting state
	 */
	public void waitState() {
		//reset speeds
		angularVel = 0;
		linearVel = 0;
		leftGearbox.zeroVel();
		rightGearbox.zeroVel();
		
		color = Color.yellow;
	} //end waitState
	
	//Kinematics
	
	/**
	 * Get the average distance travelled by robot
	 * @return average - average distance travelled by robot in inches
	 */
	public double getAveragePos() {
		return averagePos;
	} //end getAveragePos
	
	/**
	 * Get the linear velocity of the robot
	 * @return linearSpeed - linear speed of the robot in inches per second
	 */
	public double getLinearVel() {
		return linearVel;
	} //end getLinearVel
	
	/**
	 * Get the angular velocity of the robot
	 * @return angularSpeed - angular speed of the robot in radians per second
	 */
	public double getAngularVel() {
		return angularVel;
	} //end getAngularVel
	
	/**
	 * Get the max linear speed of the robot
	 * @return maxLinSpeed - top linear speed of the robot in ft/s
	 */
	public double getMaxLinSpeed() {
		return maxLinSpeed;
	} //end getMaxLinSpeed
	
	/**
	 * Get the max angular speed of the robot
	 * @return maxAngSpeed - top angular speed of the robot in rad/s
	 */
	public double getMaxAngSpeed() {
		return maxAngSpeed;
	} //end getMaxAngSpeed
	
	/**
	 * Get the position of the left gearbox
	 * @return - left gearbox position in radians
	 */
	public double getLeftPos() {
		return leftGearbox.getPos();
	} //end getLeftPos
	
	/**
	 * Get the position of the right gearbox
	 * @return - right gearbox position in radians
	 */
	public double getRightPos() {
		return rightGearbox.getPos();
	} //end getRightPos
	
	/**
	 * Check if the robot's speed is lower than a percent of its top speed
	 * @param percent - percent of the top speed to be under between -1 and 1
	 * @return - true if under, false if equal or over
	 */
	public boolean isSlowerThanPercent(double percent) {
		if (Math.abs(linearVel) < percent * maxLinSpeed) {
			return true;
		} else {
			return false;
		} //if
	} //end isSlowerThanPercent
	
	//Pose
	
	/**
	 * Get the coordinates of the robot
	 * @return - (x,y) values of the robot as a Point
	 */
	public Point getPoint() {
		return new Point(point.getX(), point.getY());
	} //end getPoint
	
	/**
	 * Get the x position of the robot
	 * @return - x position of robot
	 */
	public double getX() {
		return point.getX();
	} //end getX
	
	/**
	 * Get the y position of the robot
	 * @return - y position of robot
	 */
	public double getY() {
		return point.getY();
	} //end getY
	
	/**
	 * Set the (x,y) coordinates of the robot
	 * @param p - new (x,y) coordinates of the robot
	 */
	public void setXY(Point p) {
		point = new Point(p.getX(), p.getY());
	} //end setXY
	
	/**
	 * Get the pose of the robot
	 * @return - current pose of the robot
	 */
	public Pose getPose() {
		return new Pose(new Point(point.getX(), point.getY()), heading, 
						new Color(color.getRed(), color.getGreen(), color.getBlue()));	
	} //end getPose
	
	/**
	 * Get the heading of the robot
	 * @return heading - heading of the robot in radians
	 */
	public double getHeading() {
		return heading;
	} //end getHeading
	
	/**
	 * Set the heading of the robot
	 * @param heading - heading in radians for the robot to point at
	 */
	public void setHeading(double heading) {
		this.heading = heading;
	} //end setHeading
	
	/**
	 * Set the heading of the robot in degrees
	 * @param heading - heading in degrees to set the robot to
	 */
	public void setHeadingDegrees(double heading) {
		this.heading = Math.toRadians(heading);
	} //end setHeadingDegrees
	
	/**
	 * Get the yaw of the robot in degrees
	 * @param yaw - robot yaw in degrees
	 */
	public double getYaw() {
		yaw = Math.toDegrees(heading) % 360; //convert heading to degrees
		
		return yaw;
	} // end getYaw

	/**
	 * Set the yaw (and heading) of the robot
	 * @param yaw - yaw value to set the robot yaw to
	 */
	public void setYaw(double yaw) {
		this.yaw = yaw;
		this.heading = Math.toRadians(yaw);
	} //end setYaw

	//Dynamics
	
	/**
	 * Update the pose of the robot given voltages applied over an interval
	 * @param leftVoltage - voltage applied to left gearbox
	 * @param rightVoltage - voltage applied to right gearbox
	 */
	public void update(double leftVoltage, double rightVoltage) {
		//clamp the voltages between min and max voltage values
		leftVoltage = Util.clampNum(leftVoltage, -Util.MAX_VOLTAGE, Util.MAX_VOLTAGE);
		rightVoltage = Util.clampNum(rightVoltage, -Util.MAX_VOLTAGE, Util.MAX_VOLTAGE);
		
		//calculate force exerted by each gearbox on robot
		double leftForce = leftGearbox.calcTorque(leftVoltage) / kWheelRad;
		double rightForce = rightGearbox.calcTorque(rightVoltage) / kWheelRad;
		
		//calculate each side's acceleration 
		updateGearboxes(leftForce, rightForce);
		
		//update the speeds of the robot
		updateSpeeds();
		
		//update the pose of the robot
		updatePose();
		
		//update the graphics of the robot
		updateGraphics();
	} //end update
	
	/**
	 * Calculate the accelerations of each side of the drive
	 * @param leftTorque - torque of the left gearbox
	 * @param rightTorque - torque of the right gearbox
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
	
	//Graphics
	
	/**
	 * Get the color of the robot
	 * @return - color based on direction scaled to speed
	 */
	public Color getColor() {
		return color;
	} //end getColor
	
	/**
	 * Set the color of the robot
	 * @param c - new Color for the robot
	 */
	public void setColor(Color c) {
		this.color = c;
	} //end setColor
	
	/**
	 * Update the robot graphics
	 */
	private void updateGraphics() {
		updateColor();
	} //end updateGraphics
	
	/**
	 * Update the color of the robot
	 */
	private void updateColor() {
		//percentage of top speed
		double modifier = Math.min(1, Math.abs(linearVel) / maxLinSpeed);
		int val = 127 + (int) (128 * modifier);
		
		if (linearVel > 0) { //moving forward
			color = new Color(0, val, 0); //green
			
		} else if (linearVel < 0) { //reversing
			color = new Color(val, 0, 0); //red
			
		} else { //not moving
			color = Color.YELLOW;
		} //if
	} //end updateColor
	
	/**
	 * Get the width of the robot in pixels
	 * @return - width of robot in pixels
	 */
	public int getWidthPixels() {
		return (int) ((kWidth * AutoSim.PPI) / Util.INCHES_TO_METERS);
	} //end getWidthPixels
	
	/**
	 * Get the length of the robot in pixels
	 * @return - width of robot in pixels
	 */
	public int getLengthPixels() {
		return (int) ((kLength * AutoSim.PPI) / Util.INCHES_TO_METERS);
	} //end getLengthPixels
	
	/**
	 * Set the name of the Command the robot is running
	 * @param name name of Command robot is currently running
	 */
	public void setCommandName(String name) {
		this.commandName = name;
	} //end setCommandName
	
	/**
	 * Set the goal point to be drawn
	 * @param goal goal point Robot is attempting to reach
	 */
	public void setGoalPoint(Point goal) {
		this.goalPoint = goal;
	} //end setGoalPoint
	
	/**
	 * Get the data of the robot
	 * @return all robot data point in a HashMap
	 */
	public HashMap<ROBOT_KEY, Object> getData() {
		HashMap<ROBOT_KEY, Object> data = new HashMap<ROBOT_KEY, Object>();
		
		//add all keys and values
		data.put(ROBOT_KEY.AVG_POS, averagePos);
		data.put(ROBOT_KEY.ANG_VEL, angularVel);
		data.put(ROBOT_KEY.LIN_VEL, linearVel);
		data.put(ROBOT_KEY.HEADING, heading);
		data.put(ROBOT_KEY.YAW, yaw);
		data.put(ROBOT_KEY.POINT, point);
		data.put(ROBOT_KEY.COLOR, color);
		data.put(ROBOT_KEY.LEFT_POS, leftGearbox.getPos() * kWheelRad / Util.INCHES_TO_METERS);
		data.put(ROBOT_KEY.RIGHT_POS, rightGearbox.getPos() * kWheelRad / Util.INCHES_TO_METERS);
		data.put(ROBOT_KEY.LEFT_VEL, leftGearbox.getVel());
		data.put(ROBOT_KEY.RIGHT_VEL, rightGearbox.getVel());
		data.put(ROBOT_KEY.LEFT_ACC, leftGearbox.getAcc());
		data.put(ROBOT_KEY.RIGHT_ACC, rightGearbox.getAcc());
		data.put(ROBOT_KEY.LIN_ACC, (leftGearbox.getAcc() + rightGearbox.getAcc()) / 2);
		data.put(ROBOT_KEY.ANG_ACC, (kWheelRad / (2 * kPivotArm)) * (rightGearbox.getAcc() - leftGearbox.getAcc()));
		data.put(ROBOT_KEY.CURRENT_COMMAND, commandName);
		data.put(ROBOT_KEY.GOAL_POINT, goalPoint);
		
		return data;
	} //end getData
} //end Robot
