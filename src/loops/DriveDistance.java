/**
 * DriveDistance
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Use PID control to drive a distance within a tolerance
 */
package loops;

import java.awt.Color;
import java.util.ArrayList;

import model.Point;
import model.Pose;
import util.FieldPoints;

public class DriveDistance extends Command {
	private DriveLoop loop;
	private ArrayList<Pose> poses;
	
	private double distance;
	private double tolerance;
	private double topSpeed;
	private double angle;
	
	public DriveDistance(DriveLoop loop, double distance, double tolerance, double topSpeed) {
		this.loop = loop;
		
		this.distance = distance;
		this.tolerance = tolerance; 
		this.topSpeed = topSpeed;
		poses = new ArrayList<Pose>();
//		poses.add(new Pose(new Point(162, 20), 0, Color.green));
	}

	@Override
	protected void initialize() {
		loop.setDriveDistance(distance, topSpeed, tolerance);
//		loop.getRobot().setHeadingDegrees(-45);
	}

	@Override
	protected void execute() {
		loop.onLoop();
//		Util.tabPrint(distance, loop.getRobot().getAveragePos());
//		System.out.println();
//		Util.pause(5);
	}

	@Override
	protected void savePose() {
		poses.add(loop.getRobot().getPose());
//		poses.add(new Pose(new Point(162, 20), 0, Color.green));
	}

	@Override
	protected boolean isFinished() {
		if (loop.isDrivePIDAtTarget() && loop.isRobotSlowerThanPercent(0.05)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void end() {
//		Util.println("DriveDistance done:", distance, tolerance, topSpeed);
	}

	@Override
	public ArrayList<Pose> getPoses() {
		return poses;
	}

}
