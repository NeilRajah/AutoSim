/**
 * TrenchCycleLong
 * Author: Neil Balaskandarajah
 * Created on: 04/01/2020
 * Long trench cycle right out of auto
 */

package commands.routines;

import commands.CommandGroup;
import commands.DriveDistance;
import commands.DriveToGoal;
import commands.Wait;
import main.AutoSim;
import model.DriveLoop;
import util.FieldPoints;

public class TrenchCycleLong extends CommandGroup {

	/**
	 * Create a long trench cycle routine
	 */
	public TrenchCycleLong() {
		//set the loop for the commands
		DriveLoop loop = AutoSim.driveLoop;
		
		//drive to loading
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ, 1, 12, 0, false));
		this.add(new DriveToGoal(loop, FieldPoints.RDVZ_MID, 1, 12, 9, false));
		this.add(new DriveToGoal(loop, FieldPoints.LOADING_BLUE, 1, 12, 0, false));
		this.add(new Wait(loop, 4)); //intaking
		
		//drive back to score
		this.add(new DriveDistance(loop, -30, 1, 6));
		this.add(new DriveToGoal(loop, FieldPoints.TARGET_BLUE, 1, 12, 9, false));
		this.add(new Wait(loop, 5)); //shooting
	} //end constructor
} //end class
