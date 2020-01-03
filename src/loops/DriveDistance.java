/**
 * DriveDistance
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Use PID control to drive a distance within a tolerance
 */
package loops;

import util.Util;

public class DriveDistance extends Command {
	private DriveLoop loop;
	
	private double distance;
	private double tolerance;
	private double topSpeed;
	private double angle;
	
	public DriveDistance(DriveLoop loop, double distance, double tolerance, double topSpeed) {
		this.loop = loop;
		
		this.distance = distance;
		this.tolerance = tolerance; 
		this.topSpeed = topSpeed;
	}

	@Override
	protected void initialize() {
		loop.setDriveDistance(distance, topSpeed, tolerance);
	}

	@Override
	protected void execute() {
		loop.onLoop();
		Util.println("DD:", distance, loop.getRobot().getAveragePos());
		Util.pause(500);
	}

	@Override
	protected void savePose() {
		
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
		Util.println("DriveDistance done:", distance, tolerance, topSpeed);
	}

}
