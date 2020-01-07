/**
 * EightCellAuto
 * Author: Neil Balaskandarajah
 * Created on: 04/01/2020
 * Eight power cell autonomous through rendezvous zone
 */
package loops.routines;

import loops.CommandGroup;
import loops.DriveDistance;
import loops.DriveLoop;
import loops.DriveToGoal;
import loops.Wait;
import main.AutoSim;
import util.FieldPoints;

public class EightCellAuto extends CommandGroup {

	/**
	 * Create an eight cell auto routine
	 */
	public EightCellAuto() {
		//set the loop for the commands
		DriveLoop loop = AutoSim.driveLoop;
		
		//shoot balls
		this.add(new Wait(loop, 4));
		
		//pick close cells up
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_ONE, 1, 12, 6, true));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_TWO, 1, 6, 3, false));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_THREE, 1, 6, 3, false));
		
		//reverse and pick other cells up
		this.add(new DriveDistance(loop, -24, 1, 6));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_FOUR, 1, 6, 3, false));
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_POS_FIVE, 1, 6, 3, false));
		
		//drive to shot position and shoot
		this.add(new DriveToGoal(loop, FieldPoints.AUTO_SHOT, 1, 12, 6, true));
		this.add(new Wait(loop, 4));
	} //end constructor
} //end class
