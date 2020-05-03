/**
 * DriveCurveFollow
 * Author: Neil Balaskandarajah
 * Created on: 23/04/2020
 * Follow a bezier curve in the CurveFollowing state
 */

package commands;

import model.DriveLoop;
import model.motion.BezierProfile;
import util.Util;

public class DriveCurveFollow extends Command {
	//Attributes
	private DriveLoop loop; //drivetrain state machine
	private BezierProfile profile; //profile generated from curve
	private double tolerance; //distance to be within of endpoint to be considered done
	private int index; //current index in trajectory
	
	/**
	 * Create a command to follow a curve
	 * @param loop Drivetrain state machine
	 * @param profile Profile generated from curve
	 * @param tolerance Distance to be relative to endpoint to be considered done
	 */
	public DriveCurveFollow(DriveLoop loop, BezierProfile profile, double tolerance) {
		//set attributes
		this.loop = loop;
		this.profile = profile;
		this.tolerance = tolerance;
		
		//for superclass
		this.robot = loop.getRobot();
	} //end constructor

	/**
	 * Set the state for the first time and zero the index
	 */
	protected void initialize() {
		this.loop.setCurveFollowingState();
		index = 0;
	} //end initialize

	/**
	 * Update the curve following state and run the loop
	 */
	protected void execute() {
		double time = index * Util.UPDATE_PERIOD;
//		loop.updateCurveFollowingState();
		loop.onLoop();
		index++;
	} //end execute

	/**
	 * Finish the command when time is up
	 *   change to be based on wheel distance
	 */
	protected boolean isFinished() {
		return (index * Util.UPDATE_PERIOD) > profile.getTotalTime();
	} //end isFinished

	/**
	 * Run when the command ends
	 */
	protected void end() {
		
	} //end end

	/**
	 * Run if the command times out
	 */
	protected void timedOut() {

	} //end timedOut
} //end class