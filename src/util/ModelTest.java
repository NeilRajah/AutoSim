/**
 * ModelTest
 * Author: Neil Balaskandarajah
 * Created on: 25/12/2019
 * Test cases to make sure the model works
 */

package util;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import commands.DriveDistance;
import commands.DriveToGoal;
import commands.routines.ConstantsTest;
import main.AutoSim;
import model.DriveLoop;
import model.FieldPositioning;
import model.Gearbox;
import model.Motor;
import model.PIDController;
import model.Point;
import model.Robot;

class ModelTest {
	//Attributes
	private static Gearbox gb; //drive gearbox
	private static Robot r; //robot 
	private static DriveLoop driveLoop; //loop controlling robot
	
	@BeforeAll
	/**
	 * Set up the suite by creating the robot
	 */
	private static void setUp() {
		gb = new Gearbox(8.5521, new Motor(Util.NEO), 2); //2 NEO 12ft/s drive
		r = new Robot(4, 153, 30, 30, gb); //4" wheel dia 153lb 30"x30" 
		PIDController drivePID = new PIDController(Util.kP_DRIVE, Util.kI_DRIVE, Util.kD_DRIVE, r.getMaxLinSpeed());
		PIDController turnPID = new PIDController(Util.kP_TURN, Util.kI_TURN, Util.kD_TURN, r.getMaxLinSpeed());
		driveLoop = new DriveLoop(r, drivePID, turnPID);
		r.setXY(new Point(0,0));
	} //end setUp
	
	@AfterEach
	/**
	 * Reset the suite after each test by resetting the robot values and returning to origin
	 */
	public void reset() {
		r.reset();
		r.setXY(new Point(0,0));
	} //end reset
	
	@Test
	/**
	 * Ensure the torque produced by the robot is correct
	 */
	void gearboxTorqueTest() {
		double torque = gb.calcTorque(12);
		
		double correctValue = 57.47; //calculated from model spreadsheet
		assertEquals("Torque should be " + correctValue + ", calculated to be " + torque, correctValue, torque, 1);
	} //end gearboxTorqueTest
	
	@Test
	/**
	 * Ensure the robot displaces the correct amount
	 */
	void displacementTest() { 
		double t = 0.00;
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12, 12);
			t += Util.UPDATE_PERIOD;
		} //loop
		double correctDisplacement = 128.6; //calculated from model spreadsheet
		assertEquals(correctDisplacement, r.getAveragePos(), 1);
	} //end displacementTest
	
	@Test
	/**
	 * Ensure the x direction of the pose is correct
	 */
	void poseTestX() {
		double t = 0.00;
		
		r.setHeading(2);
		
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12,12);
			t += Util.UPDATE_PERIOD;
		} //loop
		
		Point correctPoint = new Point(116.9, -53.5); //(r, theta) = (128.6, 2)
		assertEquals(correctPoint.getX(), r.getPoint().getX(), 1);
	} //end poseTestX
	
	@Test
	/**
	 * Ensure the y direction of the pose is correct
	 */
	void poseTestY() {
		double t = 0.00;
		
		r.setHeading(2);
		
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12,12);
			t += Util.UPDATE_PERIOD;
		} //loop

		Point correctPoint = new Point(116.9, -53.5); //(r, theta) = (128.6, 2)
		assertEquals(correctPoint.getY(), r.getPoint().getY(), 1);
	} //end poseTestY
	
	@Test
	/**
	 * Ensure the robot turns properly
	 */
	void headingTest() {
		double t = 0.00;
		while (t <= 1.00 + Util.UPDATE_PERIOD) {
			r.update(-12, 12);
			t += Util.UPDATE_PERIOD;
		} //loop
		double correctHeading = 510; //calculated from model spreadsheet
		assertEquals(correctHeading, Math.toDegrees(r.getHeading()), 1);
	} //end headingTest
	
	
	@Test
	/**
	 * Ensure the robot accelerates properly
	 */
	void speedTest() {
		double t = 0.00;
		while (t <= 1.00 + Util.UPDATE_PERIOD) {
			r.update(12, 12);
			t += Util.UPDATE_PERIOD;
		} //loop
		double correctSpeed = 12; //calculated from model spreadsheet
		assertEquals(correctSpeed, r.getLinearVel(), 1);
	} //end speedTest
	
	@Test
	/**
	 * Ensure the top linear speed of the robot is calculated correctly
	 */
	void topLinSpeedTest() {
		double correctTopSpeed = 12; //calculated from model spreadsheet
		assertEquals(correctTopSpeed, r.getMaxLinSpeed(), 1);
	} //end topLinSpeedTest
	
	@Test
	/**
	 * Ensure the top angular speed of the robot is calculated correctly
	 */
	void topAngSpeedTest() {
		double correctTopSpeed = 9.6; //calculated from model spreadsheet
		assertEquals(correctTopSpeed, r.getMaxAngSpeed(), 1);
	} //end topAngTest
	
	@Test
	/**
	 * Ensure the distance the robot drives when controlled by PID is correct
	 */
	void driveDistanceTest() {
		DriveDistance dd = new DriveDistance(driveLoop, 100, 1, 12);
		dd.run();
		assertEquals(100, r.getAveragePos(), 1);
	} //end driveDistanceTest
	
	@Test
	/**
	 * Ensure the robot calculates its yaw correctly
	 */
	void yawTest() {
		r.setHeadingDegrees(10);
		assertEquals(10, r.getYaw(), 1);
	} //end yawTest
	
	@Test
	/**
	 * Ensure the robot moves to a goal point correctly
	 */
	void driveToGoalTest() {
		DriveToGoal d2g = new DriveToGoal(driveLoop, FieldPoints.AUTO_POS_TWO, 1, 12, 0, false);
		d2g.run();
		assertEquals(1, FieldPositioning.calcDistance(FieldPoints.AUTO_POS_TWO, driveLoop.robot().getPoint()), 1);
	} //end driveToGoalTest
	
	@Test
	/**
	 * Calculate the constants
	 */
	void constantsTest() {
		ConstantsTest ct = new ConstantsTest(driveLoop);
		ct.execute();
	} //end ConstantsTest
} //end class
