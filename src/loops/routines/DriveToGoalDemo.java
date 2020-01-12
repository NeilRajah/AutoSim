/**
 * DriveToGoalDemo
 * Author: Neil Balaskandarajah
 * Created on: 07/01/2020
 * Demonstration for point to point driving
 */

package loops.routines;

import loops.CommandGroup;
import loops.DriveDistance;
import loops.DriveLoop;
import loops.DriveToGoal;
import loops.TurnAngle;
import main.AutoSim;
import model.Point;
import util.FieldPoints;
import util.Util;

public class DriveToGoalDemo extends CommandGroup {

	public DriveToGoalDemo() {
		DriveLoop loop = AutoSim.driveLoop; //get the main loop
		
		//Variables for the commands
		
		//X and Y values for ranges
		//height and width flipped because x and y are flipped
		double minX = 15, maxX = Util.FIELD_HEIGHT - minX;
		double minY = 15, maxY = Util.FIELD_WIDTH - minY;
		
		//topSpeed, minSpeed and reverse
		double topSpeed = loop.getRobot().getMaxLinSpeed();
		double minSpeed = 0;
		boolean reverse = false;
		
		//add 10 points to drive to
		for (int i = 0; i < 10; i++) {
			//create random x and y values to be within
			double x = Math.random() * (maxX - minX) + minX;
			double y = Math.random() * (maxY - minY) + minY;
			
			//set the speeds and reverse
			if (i == 0) { //first drive
				topSpeed = loop.getRobot().getMaxLinSpeed();
				minSpeed = 6;
				reverse = false;
				
			} else if (i == 9) { //last drive
				minSpeed = 0;
				reverse = false;
				
			} else { //other points
				topSpeed = Math.random() * (loop.getRobot().getMaxLinSpeed()/2) + loop.getRobot().getMaxLinSpeed()/2;
				reverse = Math.random() > 0.5;
				minSpeed = topSpeed/2;								
			} //if
			
			//add the command to the group
			add(new DriveToGoal(loop, new Point(x,y), 1, topSpeed, minSpeed, reverse));
		} //loop
	} //end constructor
} //end class
