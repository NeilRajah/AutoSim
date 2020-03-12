/**
 * DriveToGoal
 * Author: Neil Balaskandarajah
 * Created on: 03/01/2020
 * Drive the robot to an (x,y) position on the field
 */
package commands;

import model.DriveLoop;
import model.FieldPositioning;
import model.Point;
import util.Util;

public class DriveToGoal extends Command {
	// Attributes
	// General
	private DriveLoop loop; // drivetrain loop to update

	// Specific
	private Point goalPoint; // goal point to drive to
	private double setpoint; // distance to drive to in inches
	private double goalAngle; // angle to drive to in radians
	private double tolerance; // tolerance to be within in inches
	private double topSpeed; // top speed of robot in ft/s
	private double minSpeed; // min speed of robot in ft/s
	private boolean reverse; // whether the robot is driving backwards or not
	private double scale; // amount to scale the top speed by
	private double lookahead; // additional distance to add onto setpoint to cruise to minSpeed

	/**
	 * Drive to a goal point using the P2P control scheme
	 * 
	 * @param driveLoop  - drivetrain loop to update
	 * @param goal - goal point to drive to
	 * @param tolerance - tolerance to be within in inches
	 * @param topSpeed - top speed of the robot in ft/s
	 * @param minSpeed - min speed of the robot in ft/s
	 * @param reverse - whether the robot is driving backwards or not
	 */
	public DriveToGoal(DriveLoop driveLoop, Point goal, double tolerance, double topSpeed, double minSpeed,
			boolean reverse) {
		// set attributes
		this.loop = driveLoop;
		this.goalPoint = goal;
		this.tolerance = tolerance;
		this.topSpeed = topSpeed;
		this.minSpeed = minSpeed;
		this.reverse = reverse;

		// calculate the lookahead distance
		this.lookahead = (Util.MAX_VOLTAGE * minSpeed) / (topSpeed * Util.kP_DRIVE) + Util.LOOKAHEAD_DIST;
		this.lookahead *= reverse ? -1 : 1;
		
		//set robot
		this.robot = loop.getRobot();
	} // end constructor

	/**
	 * Initialize the command by updating the setpoints and setting the state
	 * machine state
	 */
	protected void initialize() {
		updateSetpoints();
		loop.setDriveToGoalState(setpoint, goalAngle, tolerance, topSpeed * scale, minSpeed, reverse);
	} // end initialize

	/**
	 * Execute the command by updating the state machine and running it
	 */
	protected void execute() {
		updateSetpoints();
		loop.updateDriveToGoalState(setpoint, goalAngle, tolerance, topSpeed * scale, minSpeed, reverse);
		loop.onLoop();
	} // end execute

	/**
	 * Return whether or not the robot is within the target point tolerance circle
	 * @return - true if within, false if not
	 */
	protected boolean isFinished() {
		return FieldPositioning.isWithinBounds(goalPoint, loop.getRobot().getPoint(), tolerance);
	} // end isFinished

	/**
	 * Run at the end of the command
	 */
	protected void end() {}
	
	//Helper Methods

	/**
	 * Update the state machine setpoints based on the robot's current position
	 */
	private void updateSetpoints() {
		// distance from robot to goal point
		double dist = FieldPositioning.calcDistance(loop.getRobot().getPoint(), goalPoint);

		// update distance and angle setpoints if more than half a foot away
		if (dist >= 6) {
			// distance setpoint
			setpoint = loop.getRobot().getAveragePos() + (reverse ? -dist : dist) + lookahead;

			// angle setpoint
			goalAngle = Math.toRadians(calcDeltaAngle()) + loop.getRobot().getHeading();
		} // if

		// scale for top speed
		double dA = Math.abs(Math.toDegrees(goalAngle - loop.getRobot().getHeading()));
		scale = calcScale(dA);
	} // end updateSetpoints

	/**
	 * Calculate the scalar value for the linear output based on the heading error
	 * 
	 * @param dA
	 *            - magnitude of angle change in degrees
	 * @return output - value to scale linear top speed by
	 */
	private double calcScale(double dA) {
		if (dA > 90) { // if difference is greater than 90
			return 0; // do not move linearly, only turn

		} else {
			// scale linear output quadratically based on the angle error
			double coeff = (1 / Math.pow(90, 2));
			double output = coeff * Math.pow(dA - 90, 2);

			return output;
		} // if
	} // end calcScale

	/**
	 * Calculate the angle change required to face point based on the angle from the
	 * robot to the point
	 * 
	 * @return - required angle change in degrees
	 */
	private double calcDeltaAngle() {
		double pointYaw = FieldPositioning.calcGoalYaw(loop.getRobot().getPoint(), goalPoint); // calculate the yaw to
																								// the point
		pointYaw += (reverse ? -Math.signum(pointYaw) * 180 : 0); // add on 180 in the opposite direction if going in
																	// reverse

		double deltaAngle = pointYaw - loop.getRobot().getYaw(); // yaw difference between point yaw and robot yaw

		if (Math.abs(deltaAngle) >= (360 - Math.abs(deltaAngle))) { // if angle change not smallest possible
			return (Math.signum(loop.getRobot().getYaw() * 180 - loop.getRobot().getYaw())
					- (Math.signum(pointYaw) * 180 - pointYaw));

		} else { // if it is smallest value
			return deltaAngle;
		} // if
	} // end calcDeltaAngle
} // end class
