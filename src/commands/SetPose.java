/**
 * SetPose
 * Author: Neil Balaskandarajah
 * Created on: 13/01/2020
 * Set the pose of the robot
 */
package commands;

import java.util.ArrayList;

import model.DriveLoop;
import model.Point;
import model.Pose;
import model.DriveLoop.STATE;

public class SetPose extends Command {
	//Attributes
	//General
	private DriveLoop loop; //drivetrain loop to update
	private ArrayList<Pose> poses; //list of poses to update for drawing
	
	/**
	 * Set the pose of the robot 
	 * @param driveLoop - drivetrain loop to update
	 * @param p - point to set (x,y) of robot to
	 * @param heading - heading to set robot to in degrees
	 */
	public SetPose(DriveLoop driveLoop, Point p, double heading) {
		//set attributes
		this.loop = driveLoop;
		poses = new ArrayList<Pose>();
		
		//set pose of robot
		loop.getRobotClone().setXY(p);
		loop.getRobotClone().setHeading(Math.toRadians(heading));
		loop.setState(STATE.WAITING);
		
		//set robot and name
		this.robot = loop.getRobot();
		this.name = "SetPose";
	} //end constructor
	
	/**
	 * Set the pose of the robot 
	 * @param driveLoop - drivetrain loop to update
	 * @param x - x value to set robot to
	 * @param y - y value to set robot to
	 * @param heading - heading to set robot to in degrees
	 */
	public SetPose(DriveLoop driveLoop, double x, double y, double heading) {
		//set attributes
		this.loop = driveLoop;
		poses = new ArrayList<Pose>();
		
		//set pose of robot
		loop.getRobot().setXY(new Point(x,y));
		loop.getRobot().setHeading(Math.toRadians(heading));
		loop.setState(STATE.WAITING);
	} //end constructor

	protected void initialize() {}
	
	protected void execute() {}

	/**
	 * Save the pose of the robot to the list
	 */
	protected void updateGraphics() {
		poses.add(loop.getRobot().getPose());
	} //end savePose

	/**
	 * Return true upon initialization
	 */
	protected boolean isFinished() {
		return true;
	} //end isFinished

	/**
	 * Run at the end of the command
	 */
	protected void end() {} 

	/**
	 * Get the list of poses
	 * @return poses - list of poses for animation
	 */
	public ArrayList<Pose> getPoses() {
		return poses;
	} //end getPoses

	@Override
	public ArrayList<int[][]> getCurves() {
		// TODO Auto-generated method stub
		return null;
	}
} //end class
