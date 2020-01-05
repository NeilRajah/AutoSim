package loops;

import main.AutoSim;
import util.FieldPoints;

public class TrenchCycleLow extends CommandGroup {

	public TrenchCycleLow() {
		DriveLoop loop = AutoSim.driveLoop;
//		for (int i = 0; i < 4; i++) {
		//load
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID, 1, 9, 3, false));
//		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID2, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.LOADING_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 4));
		
		//drive back under panel and score
		this.add(new DriveDistance(loop, -50, 6, 12));
		this.add(new DriveToGoal(loop, FieldPoints.CONTROL_PANEL_BLUE, 1, 12, 6, false));
		this.add(new DriveToGoal(loop, FieldPoints.TRENCH_SHOT_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 5));
		
//		}		
	}

}
