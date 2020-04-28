package commands;

import model.DriveLoop;
import model.motion.BezierProfile;
import util.Util;

public class DriveCurveFollow extends Command {
	//Attributes
	private DriveLoop loop;
	private BezierProfile profile;
	private double tolerance;
	private int index;
	
	public DriveCurveFollow(DriveLoop loop, BezierProfile profile, double tolerance) {
		this.loop = loop;
		this.profile = profile;
		this.tolerance = tolerance;
		
		this.robot = loop.getRobot();
	}

	@Override
	protected void initialize() {
		this.loop.setCurveFollowingState(robot.getLeftPos(), robot.getRightPos());
		index = 0;
	}

	@Override
	protected void execute() {
		double time = index * Util.UPDATE_PERIOD;
		loop.updateCurveFollowingState(profile.getLeftTrajPoint(time), profile.getRightTrajPoint(time));
		loop.onLoop();
		index++;
	}

	@Override
	protected boolean isFinished() {
		return (index * Util.UPDATE_PERIOD) > profile.getTotalTime();
	}

	@Override
	protected void end() {
	}

	@Override
	protected void timedOut() {
		// TODO Auto-generated method stub

	}

}
