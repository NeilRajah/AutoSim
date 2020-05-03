/**
 * RAMSETECommand
 * Author: Neil Balaskandarajah
 * Created on: 03/05/2020
 * Drive to a goal pose using the RAMSETE controller
 */

package commands;

import model.DriveLoop;
import model.FieldPositioning;
import model.Pose;
import util.Util;

public class RAMSETECommand extends Command {
	//Attributes
	private DriveLoop loop;
	private Pose goal;
	private double goalVel;
	private double goalAng;
	
	public RAMSETECommand(DriveLoop loop, Pose goal, double goalVel, double goalAng) {
		this.loop = loop;
		this.robot = loop.getRobot();
		
		this.goal = goal;
		this.goalVel = goalVel;
		this.goalAng = goalAng;
	} //end constructor

	public RAMSETECommand(DriveLoop loop, Pose goal) {
		this(loop, goal, 0, 0);
	} //end constructor
	
	@Override
	protected void initialize() {
		loop.setCurveFollowingState();
		robot.setGoalPoint(goal.getPoint());
	}

	@Override
	protected void execute() {
		loop.updateCurveFollowingState(goal, goalVel, goalAng);
		loop.onLoop();
	}

	@Override
	protected boolean isFinished() {
		return FieldPositioning.isWithinBounds(goal.getPoint(), robot.getPoint(), 1) 
				&& Util.fuzzyEquals(robot.getHeading(), goal.getHeading(), 1);
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void timedOut() {
		// TODO Auto-generated method stub

	}

}
