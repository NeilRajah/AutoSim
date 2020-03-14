/**
 * DriveToGoalTest
 * Author: Neil Balaskandarajah
 * Created on: 14/03/2020
 * Test command group for DriveToGoal
 */

package commands.routines;

import java.util.ArrayList;

import commands.CommandGroup;
import commands.DriveToGoal;
import commands.SetPose;
import commands.Wait;
import main.AutoSim;
import model.DriveLoop;
import util.Util;;

public class DriveToGoalTest extends CommandGroup {
	//Attributes
	private DriveLoop loop;
	private double startX;
	private double startY;
	
	/**
	 * All test cases
	 */
	public DriveToGoalTest() {
		loop = AutoSim.driveLoop; //get the main loop
		
		//set the starting (x,y) position of the robot
		startX = Util.FIELD_HEIGHT / 2;
//		startX = 50;
		startY = Util.FIELD_WIDTH / 2;
		double dist = 75;
		
		//set the robot to its starting pose (heading = 0)
//		loop.getRobot().setXY(new Point(0, 0));
		resetRobot();
//		this.add(new SetPose(loop, 50, 50));
//		this.add(new Wait(loop, 0.25));
//		this.add(new SetPose(loop, 250, 50));
//		this.add(new Wait(loop, 0.25));
		
		//List of all tests
		ArrayList<DriveToGoal> tests = new ArrayList<DriveToGoal>();
		
		//Forward Straight ahead
		tests.add(new DriveToGoal(loop, startX, startY + dist, 1, 12, 2, false));
		
		//Forward Straight behind
		DriveToGoal test2 = new DriveToGoal(loop, startX, startY - dist, 1, 12, 2, false);
		test2.setTimeout(2.00);
		tests.add(test2);
		
		//Forward Left
		tests.add(new DriveToGoal(loop, startX - dist, startY, 1, 12, 2, false));
		
		//Forward Right
		tests.add(new DriveToGoal(loop, startX + dist, startY, 1, 12, 2, false));
		
		//Forward Straight ahead
		tests.add(new DriveToGoal(loop, startX, startY + dist, 1, 12, 2, true));
		
		//Forward Straight behind
		tests.add(new DriveToGoal(loop, startX, startY - dist, 1, 12, 2, true));
		
		//Forward Left
		tests.add(new DriveToGoal(loop, startX - dist, startY, 1, 12, 2, true));
		
		//Forward Right
		tests.add(new DriveToGoal(loop, startX + dist, startY, 1, 12, 2, true));
		
		//Forward + + 
		tests.add(new DriveToGoal(loop, startX + dist, startY + dist, 1, 12, 2, false));
		
		//Forward + -
		tests.add(new DriveToGoal(loop, startX + dist, startY - dist, 1, 12, 2, false));
		
		//Forward - -
		tests.add(new DriveToGoal(loop, startX - dist, startY - dist, 1, 12, 2, false));

		//Forward - +
		tests.add(new DriveToGoal(loop, startX - dist, startY + dist, 1, 12, 2, false));
		
		//Reverse + + 
		tests.add(new DriveToGoal(loop, startX + dist, startY + dist, 1, 12, 2, true));
		
		//Reverse + -
		tests.add(new DriveToGoal(loop, startX + dist, startY - dist, 1, 12, 2, true));
		
		//Reverse - -
		tests.add(new DriveToGoal(loop, startX - dist, startY - dist, 1, 12, 2, true));

		//Reverse - +
		tests.add(new DriveToGoal(loop, startX - dist, startY + dist, 1, 12, 2, true));
		
		//add all tests to the command group
		for (DriveToGoal test : tests) {
			this.add(test);
			resetRobot();
		} //loop		
		
//		this.add(test2);
//		this.add(new Wait(loop, 1.0));
	} //end constructor
	
	/**
	 * Reset the robot to a starting configuration before each test
	 */
	private void resetRobot() {
		this.add(new Wait(loop, 0.5));
		this.add(new SetPose(loop, startX, startY));
		this.add(new Wait(loop, 0.5));
	} //end resetRobot
} //end class
