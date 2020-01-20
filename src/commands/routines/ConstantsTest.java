/**
 * ConstantsTest
 * Author: Neil Balaskandarajah
 * Created on: 01/20/2020
 * Run the robot at various voltages to determine useful constants
 */
package commands.routines;

import java.util.ArrayList;

import commands.Command;
import commands.TimedVoltage;
import model.DriveLoop;
import model.Point;
import util.Util;

public class ConstantsTest {
	//Attributes
	private DriveLoop loop; //drivetrain loop to update
	private ArrayList<Point> points; //voltage-velocity points
	
	public ConstantsTest(DriveLoop loop) {
		//set attributes
		this.loop = loop;
		points = new ArrayList<Point>(); 
	} //end constructor
	
	/**
	 * Run the tests and print out the constants
	 */
	public void execute() {
		calckV();
		calckA();
	} //end execute
	
	private void calckV() {
		for(double volt = 0; volt < Util.MAX_VOLTAGE; volt += 0.1) {
			//run the robot at a voltage for 5 seconds
			Command c = new TimedVoltage(loop, volt, 5);
			c.run();
			
			//save the velocities and the voltages
			double vel = loop.robot().getLinearVel();
			points.add(new Point(volt, vel));
			
			//print point values
			Util.tabPrint(volt, vel);
			System.out.println();
			
			//reset the robot
			loop.robot().reset();
		} //loop
	} //end calckV
	
	private void calckA() {
		
	}
} //end class
