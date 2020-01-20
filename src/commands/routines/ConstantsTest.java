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
	private double[] voltages;
	private double[] velocities;
	
	public ConstantsTest(DriveLoop loop) {
		//set attributes
		this.loop = loop;
		
		//constant for test length (currently 5 seconds)
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
			
			//print point values
			Util.tabPrint(volt, vel);
			System.out.println();
			
			//reset the robot
			loop.robot().reset();
		} //loop
		
		//regression
		//print out kV
	} //end calckV
	
	private void calckA() {
		
	}
} //end class
