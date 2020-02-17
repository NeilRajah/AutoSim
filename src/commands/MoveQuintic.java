/**
 * MoveQuintic
 * Author: Neil Balaskandarajah
 * Created on: 16/02/2020
 * Move the robot through a quintic bezier path
 */
package commands;

import java.util.ArrayList;

import model.DriveLoop;
import model.Point;
import model.Pose;
import model.motion.QuinticBezierPath;
import util.Util;

public class MoveQuintic extends Command {
	// Attributes
	// General
	private DriveLoop loop; // drivetrain loop to update
	private ArrayList<Pose> poses; // list of poses to update for drawing
	private ArrayList<int[][]> curves; // list of curves to draw
	
	//Configured
	private QuinticBezierPath curve; //quintic bezier curve
	
	//Updated
	private double t; //t value for the current point
	
	/**
	 * Move the robot through a Quintic Bezier Path at even t-intervals
	 * @param loop - drivetrain loop to update
	 * @param curvePts - control points for the curve
	 */
	public MoveQuintic(DriveLoop loop, Point[] curvePts) {
		//set attributes
		this.loop = loop; //state machine to control the robot
		this.curve = new QuinticBezierPath(curvePts); //quintic bezier path
		
		//update poses
		this.poses = new ArrayList<Pose>();
		
		//update t value
		t = 0;
	} //end constructor
	
	/**
	 * Move the robot through a Quintic Bezier Path at even t-intervals
	 * @param loop - drivetrain loop to update
	 * @param curvePts - control points for the curve
	 */
	public MoveQuintic(DriveLoop loop, double[][] curvePts) {
		//set attributes
		this.loop = loop; //state machine to control the robot
		this.curve = new QuinticBezierPath(curvePts); //quintic bezier path
		
		//update poses
		this.poses = new ArrayList<Pose>();
		this.curves = new ArrayList<int[][]>();
		
		//update t value
		t = 0;
	} //end constructor

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		loop.getRobot().setXY(curve.calcPoint(t));
		loop.getRobot().setHeading(Math.PI/2 - curve.calcHeading(t));
		
//		Point p = curve.calcPoint(t);
//		Util.tabPrint(t, p.getX(), p.getY(), curve.calcHeading(t));
//		System.out.println();
		
		t += 2.0 / QuinticBezierPath.RESOLUTION;
	} //end execute

	@Override
	protected void updateGraphics() {
		poses.add(loop.getRobot().getPose());
		
//		Pose p = loop.getRobot().getPose();
//		Util.println(t, p.getPoint().getX(), p.getPoint().getY(), p.getHeading());
	}

	@Override
	protected boolean isFinished() {
		return Math.abs(t - 1.0) < 1.0 / QuinticBezierPath.RESOLUTION;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Pose> getPoses() {
		return poses;
	}
	
	@Override
	public ArrayList<int[][]> getCurves() {
		curves.add(curve.getPolyline());
		return curves;
	}

}
