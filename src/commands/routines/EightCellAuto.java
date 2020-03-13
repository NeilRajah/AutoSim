/**
 * EightCellAuto
 * Author: Neil Balaskandarajah
 * Created on: 04/01/2020
 * Eight power cell autonomous through rendezvous zone
 */
package commands.routines;

import commands.CommandGroup;
import commands.DriveDistance;
import commands.DriveToGoal;
import commands.SetPose;
import commands.Wait;
import main.AutoSim;
import model.DriveLoop;
import util.FieldPoints;
import util.Util;

public class EightCellAuto extends CommandGroup {

	/**
	 * Create an eight cell auto routine
	 */
	public EightCellAuto() {
		//set the loop for the commands
		DriveLoop loop = AutoSim.driveLoop;
		
		//shoot balls
//		this.add(new Wait(loop, 4));
		this.add(new SetPose(loop, 130, 130, 0));
		
		//pick close cells up
		add(new DriveToGoal(loop, FieldPoints.AUTO_POS_ONE, 1, 12, 6, true));
		add(new DriveToGoal(loop, FieldPoints.AUTO_POS_TWO, 1, 6, 3, false));
		add(new DriveToGoal(loop, FieldPoints.AUTO_POS_THREE, 1, 6, 1, false));
		
		//reverse and pick other cells up
//		add(new DriveDistance(loop, -24, 1, 6));
//		add(new DriveToGoal(loop, FieldPoints.AUTO_POS_FOUR, 1, 6, 3, false));
//		add(new DriveToGoal(loop, FieldPoints.AUTO_POS_FIVE, 1, 6, 3, false));
		
//		drive to shot position and shoot
//		add(new DriveToGoal(loop, FieldPoints.AUTO_SHOT, 1, 12, 6, true));
//		this.add(new Wait(loop, 4));
		
		Util.println("Added all commands");
	} //end constructor
} //end class
