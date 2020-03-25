/**
 * MoveQuintic
 * Author: Neil Balaskandarajah
 * Created on: 16/02/2020
 * Move the robot through a quintic bezier path
 */
package commands;

import java.util.ArrayList;

import model.DriveLoop;
import model.FieldPositioning;
import model.Point;
import model.motion.QuinticBezierPath;

public class MoveQuintic extends Command {
	// Attributes
	// General
	private DriveLoop loop; // drivetrain loop to update
	
	private double start = 0.0;
	private double end = 1.0;
	private double step = 0.001;
	
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
		this.curve = new QuinticBezierPath(curvePts, QuinticBezierPath.HIGH_RES); //quintic bezier path
		
		//update t value
		t = start;
		
		//set robot and name
		this.robot = loop.getRobot();
		
		//set curve
		ArrayList<int[][]> curvePolyline = new ArrayList<int[][]>();
		curvePolyline.add(curve.getPolyline());
		this.curves = curvePolyline;
	} //end constructor
	
	/**
	 * Move the robot through a Quintic Bezier Path at even t-intervals
	 * @param loop - drivetrain loop to update
	 * @param controlPts - control points for the curve
	 */
	public MoveQuintic(DriveLoop loop, double[][] controlPts) {		
		this(loop, FieldPositioning.pointsFromDoubles(controlPts));
	} //end constructor

	@Override
	protected void initialize() {}

	@Override
	protected void execute() {
		loop.getRobot().setXY(curve.calcPoint(t));
		loop.getRobot().setYaw(curve.calcHeading(t));
		
//		Point p = curve.calcPoint(t);
//		Util.tabPrint(t, p.getX(), p.getY(), curve.calcHeading(t));
//		System.out.println();
		
		t += step;
	} //end execute

	/**
	 * Finish the command when the t value is equal to the end value within tolerance
	 */
	protected boolean isFinished() {
		return Math.abs(t - end) < step;
	} //end isFinished

	/**
	 * Run at the end of the command
	 */
	protected void end() {}
	
	/**
	 * Run if the command times out
	 */
	protected void timedOut() {} 
} //end class
