/**
 * StraightProfileTest
 * Author: Neil Balaskandarajah
 * Created on: 13/01/2020
 * Test the straight motion profile for a robot
 */

package loops.routines;

import loops.CommandGroup;
import loops.DriveDistance;
import loops.DriveLoop;
import loops.SetPose;
import main.AutoSim;

public class StraightProfileTest extends CommandGroup {

	public StraightProfileTest() {
		DriveLoop loop = AutoSim.driveLoop;
		
		this.add(new SetPose(loop, 100, 100, 0));
		this.add(new DriveDistance(loop, 100, 1, 12));
	} //end constructor

} //end class
