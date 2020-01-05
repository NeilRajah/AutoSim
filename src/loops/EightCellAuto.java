package loops;

import main.AutoSim;
import util.FieldPoints;

public class EightCellAuto extends CommandGroup {

	public EightCellAuto() {
		DriveLoop loop = AutoSim.driveLoop;
		
		this.add(new Wait(loop, 4));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_ONE, 1, 12, 6, true));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_TWO, 1, 6, 3, false));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_THREE, 1, 6, 3, false));
		this.add(new DriveDistance(loop, -24, 1, 6));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_FOUR, 1, 6, 3, false));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_FIVE, 1, 6, 3, false));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_SHOT, 1, 12, 6, true));
		this.add(new Wait(loop, 4));
	}

}
