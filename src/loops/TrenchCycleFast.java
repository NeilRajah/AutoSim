package loops;

import main.AutoSim;
import util.FieldPoints;

public class TrenchCycleFast extends CommandGroup {

	public TrenchCycleFast() {
		DriveLoop loop = AutoSim.driveLoop;
		
		//drive to loading
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ, 1, 12, 0, false));
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID, 1, 12, 9, false));
//			this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID2, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.LOADING_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 4)); //intaking
		
		//drive back to score
		this.add(new DriveDistance(loop, -100, 1, 6));
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID2, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.TRENCH_SHOT_BLUE, 1, 12, 9, false));
		this.add(new TurnAngle(loop, -50, 1, 6, true));
		this.add(new Wait(loop, 5)); //shooting
	}

}
