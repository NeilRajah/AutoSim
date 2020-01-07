/**
 * TrenchCycleLow
 * Author: Neil Balaskandarajah
 * Created on: 04/01/2020
 * Low trench cycle right out of auto
 */

package loops.routines;

import loops.CommandGroup;
import loops.DriveDistance;
import loops.DriveLoop;
import loops.DriveToGoal;
import loops.Wait;
import main.AutoSim;
import util.FieldPoints;

public class TrenchCycleLow extends CommandGroup {

	/**
	 * Create a low trench cycle
	 */
	public TrenchCycleLow() {
		//set the loop for the commands
		DriveLoop loop = AutoSim.driveLoop;
		
		//drive to the loading zone
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID, 1, 9, 3, false));
		this.add(new DriveToGoal(loop, FieldPoints.LOADING_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 4)); //intake the power cells
		
		//drive back under panel and score
		this.add(new DriveDistance(loop, -50, 6, 12));
		this.add(new DriveToGoal(loop, FieldPoints.CONTROL_PANEL_BLUE, 1, 12, 6, false));
		this.add(new DriveToGoal(loop, FieldPoints.TRENCH_SHOT_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 5)); //shoot the cell
	} //end constructor
} //end class
