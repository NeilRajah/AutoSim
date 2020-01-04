package loops;

import main.AutoSim;
import util.FieldPoints;

public class TestGroup extends CommandGroup {

	public TestGroup() {		
		DriveLoop driveLoop = AutoSim.driveLoop;
		
//		this.add(new DriveToGoal(driveLoop, FieldPoints.RIGHT_FAR_ROCKET, 1, 12, 0, false));
		this.add(new DriveToGoal(driveLoop, FieldPoints.ll2Plus, 1, 12, 0, false));
		
//		this.add(new DriveDistance(driveLoop, 75, 1, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new TurnAngle(driveLoop, 50, 0.5, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new DriveDistance(driveLoop, 100, 1, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new TurnAngle(driveLoop, 30, 1, driveLoop.getRobot().getMaxLinSpeed()));
//		this.add(new DriveDistance(driveLoop, 20, 0.5, driveLoop.getRobot().getMaxLinSpeed()));
	}
}
