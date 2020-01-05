package loops;

import main.AutoSim;
import model.Point;
import util.FieldPoints;

public class TestGroup extends CommandGroup {

	public TestGroup() {		
		DriveLoop driveLoop = AutoSim.driveLoop;
		driveLoop.getRobot().setXY(FieldPoints.MID_INITIATION);
		driveLoop.getRobot().setHeading(0);
//		driveLoop.getRobot().setYaw(30);
		
		this.add(new DriveToGoal(driveLoop, FieldPoints.GOAL_TEST, 1, 12, 0, false));
		this.add(new Wait(driveLoop, 0.75));
		this.add(new DriveToGoal(driveLoop, new Point(30,30), 1, 12, 0, true));
		this.add(new DriveToGoal(driveLoop, new Point(75,95), 1, 12, 0, false));
		this.add(new DriveToGoal(driveLoop, new Point(180,430), 1, 12, 0, false));
		this.add(new DriveToGoal(driveLoop, new Point(12, 630), 1, 12, 0, true));
		this.add(new DriveToGoal(driveLoop, new Point(300,60), 1, 12, 0, false));
//		this.add(new DriveToGoal(driveLoop, FieldPoints.ll2Plus, 1, 12, 0, false));
		
//		this.add(new DriveDistance(driveLoop, 75, 1, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new TurnAngle(driveLoop, 50, 0.5, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new DriveDistance(driveLoop, 100, 1, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new TurnAngle(driveLoop, 30, 1, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new DriveDistance(driveLoop, 20, 0.5, driveLoop.getRobot().getMaxLinSpeed()));
	}
}
