package loops;

import java.awt.Color;
import java.util.ArrayList;

import loops.DriveLoop.STATE;
import model.Pose;
import util.Util;

public class Wait extends Command {
	private DriveLoop driveLoop;
	private ArrayList<Pose> poses;
	
	private double waitTime;
	private int counter;
	private int frames;
	
	public Wait(DriveLoop driveLoop, double waitTime) {
		this.driveLoop = driveLoop;
		poses = new ArrayList<Pose>();
		
		this.waitTime = waitTime;
	}

	@Override
	protected void initialize() {
		frames = (int) (waitTime / Util.UPDATE_PERIOD);
		driveLoop.setState(STATE.WAITING);
	}

	@Override
	protected void execute() {
		counter++;
	}

	@Override
	protected void savePose() {
		// TODO Auto-generated method stub
		Pose pose = driveLoop.getRobot().getPose();
		pose.setColor(Color.yellow);
		poses.add(pose);
	}

	@Override
	protected boolean isFinished() {
		if (counter > frames) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<Pose> getPoses() {
		return poses;
	}

}
