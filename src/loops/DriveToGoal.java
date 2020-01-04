package loops;

import java.util.ArrayList;

import model.FieldPositioning;
import model.Point;
import model.Pose;
import util.Util;

public class DriveToGoal extends Command {
	private DriveLoop driveLoop;
	private ArrayList<Pose> poses;
	
	private Point goalPoint; //goal point to drive to
	private double initDist;
	private double scale;
	
	private double setpoint;
	private double goalAngle;
	private double tolerance;
	private double topSpeed;
	private double minSpeed;
	private boolean reverse;
	
	int counter = 0;
	
	/*
	 * Drive to a goal point using the P2P control scheme
	 * DriveLoop driveLoop - loop to control
	 * Point goal - goal point to drive to 
	 */
	public DriveToGoal(DriveLoop driveLoop, Point goal, double tolerance, double topSpeed, double minSpeed, boolean reverse) {
		this.driveLoop = driveLoop;
		this.poses = new ArrayList<Pose>();
		
		this.goalPoint = goal;
		this.tolerance = tolerance;
		this.topSpeed = topSpeed;
		this.minSpeed = minSpeed;
		this.reverse = reverse;
	} //end constructor

	@Override
	protected void initialize() {
		initDist = driveLoop.getRobot().getAveragePos();
		updateSetpoints();
		driveLoop.setDriveToGoal(setpoint, goalAngle, tolerance, topSpeed*scale, minSpeed, reverse);
	}

	@Override
	protected void execute() {	
		updateSetpoints();
		driveLoop.updateDriveToGoal(setpoint, goalAngle, tolerance, topSpeed*scale, minSpeed, reverse);
		driveLoop.onLoop();		
		
		counter++;
	}

	@Override
	protected void savePose() {
		poses.add(driveLoop.getRobot().getPose());
	}

	@Override
	protected boolean isFinished() {
		if (FieldPositioning.isWithinBounds(goalPoint, driveLoop.getRobot().getPoint(), tolerance)) {
			return true;
		} 
		else if (counter >= 5/Util.UPDATE_PERIOD) {
			return true;
		} 
		else {
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
	
	private void updateSetpoints() {
		double dist = FieldPositioning.calcDistance(driveLoop.getRobot().getPoint(), goalPoint);		
		
		if (dist >= 6) {
			//distance setpoint
			setpoint = driveLoop.getRobot().getAveragePos() + (reverse ? -dist : dist);
			
			//yaw setpoint
			double yaw = FieldPositioning.calcGoalYaw(driveLoop.getRobot().getPoint(), goalPoint);
			yaw = yaw + (reverse ? -Math.signum(yaw)*180 : 0);
			
			double deltaAngle = yaw - driveLoop.getRobot().getYaw();
			
			if (Math.abs(deltaAngle) >= (360 - Math.abs(deltaAngle))) {
				yaw = (Math.signum(driveLoop.getRobot().getYaw()* 180 - driveLoop.getRobot().getYaw())
								- (Math.signum(yaw)*180 - yaw));
			}
			goalAngle = yaw + driveLoop.getRobot().getHeading();
		}
		
		//scale
		double dA = Math.abs(goalAngle - driveLoop.getRobot().getHeading());
		scale = calcScale(dA);
		
		Util.println("Setpoints:", setpoint, goalAngle, topSpeed*scale);
	}
	
	/**
	 * Calculate the scalar value for the linear output based on the heading error
	 */
	private double calcScale(double dA) {
		
		//if difference is greater than 90
		if (dA > 90) {
			return 0; //do not move linearly, only turn
			
		} else {
			//scale linear output quadratically based on the angle error
			double coeff = (1 / Math.pow(90, 2));
			double output = coeff * Math.pow(dA - 90, 2);
			
			return output;
		} //if
	} //end calcScale

}
