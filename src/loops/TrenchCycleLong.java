package loops;

import main.AutoSim;
import util.FieldPoints;

public class TrenchCycleLong extends CommandGroup {

	public TrenchCycleLong() {
		DriveLoop loop = AutoSim.driveLoop;
		
		//drive to loading
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ, 1, 12, 0, false));
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID, 1, 12, 9, false));
//			this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID2, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.LOADING_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 4)); //intaking
		
		//drive back to score
		this.add(new DriveDistance(loop, -30, 1, 6));
//		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID2, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.TARGET_BLUE, 1, 12, 9, false));
//		this.add(new TurnAngle(loop, -50, 1, 6, true));
		this.add(new Wait(loop, 5)); //shooting
	}

}
