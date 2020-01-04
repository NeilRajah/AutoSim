package loops;

import java.util.ArrayList;

import model.Pose;
import util.Util;

public class TurnAngle extends Command {
	private DriveLoop loop;
	private ArrayList<Pose> poses;
	
	private double tolerance;
	private double topSpeed;
	private double angle;
	double initTime = System.currentTimeMillis();
	
	public TurnAngle(DriveLoop loop, double angle, double tolerance, double topSpeed) {
		this.loop = loop;
		
		this.angle = Math.toRadians(angle);
		this.tolerance = Math.toRadians(tolerance);
		this.topSpeed = topSpeed;
		poses = new ArrayList<Pose>();
	}

	@Override
	protected void initialize() {
		loop.setTurnAngle(angle, topSpeed, tolerance);
	}

	@Override
	protected void execute() {
		loop.onLoop();
//		Util.println(angle, loop.getRobot().getHeading());
	}

	@Override
	protected void savePose() {
		poses.add(loop.getRobot().getPose());
	}

	@Override
	protected boolean isFinished() {
		if (loop.isTurnPIDAtTarget() && loop.isRobotSlowerThanPercent(0.05)) {
			System.out.println("TurnAngle isFinished():" + 
								loop.isTurnPIDAtTarget() +" "+ 
								loop.isRobotSlowerThanPercent(0.05));
			return true;
		} else if (System.currentTimeMillis() - initTime > 3000) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
//		System.out.println("donezies");
	}

	@Override
	public ArrayList<Pose> getPoses() {
		return poses;
	}

}
