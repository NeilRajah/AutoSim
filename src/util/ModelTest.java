/**
 * ModelTest
 * Author: Neil Balaskandarajah
 * Created on: 25/12/2019
 * Test cases to make sure the model works
 */

package util;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import commands.Command;
import commands.DriveDistance;
import commands.DriveToGoal;
import commands.TimedVoltage;
import commands.routines.ConstantsTest;
import graphics.components.BoxButton.BUTTON_STATE;
import graphics.widgets.Circle;
import model.DriveLoop;
import model.FieldPositioning;
import model.Gearbox;
import model.Motor;
import model.PIDController;
import model.Point;
import model.Robot;
import model.motion.BezierPath;
import model.motion.TrapezoidalProfile;
import util.Util.ROBOT_KEY;

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
		r.setHeadingDegrees(425);
		assertEquals(65, r.getYaw(), 1);
	} //end yawTest
	
	@Test
	/**
	 * Ensure the robot moves to a goal point correctly
	 */
	void driveToGoalTest() {
		DriveToGoal d2g = new DriveToGoal(driveLoop, FieldPoints.AUTO_POS_TWO, 1, 12, 0, false);
		d2g.run();
		assertEquals(1, FieldPositioning.calcDistance(FieldPoints.AUTO_POS_TWO, driveLoop.getRobot().getPoint()), 1);
	} //end driveToGoalTest
	
	@Test
	/**
	 * Calculate the constants
	 */
	void constantsTest() {
		ConstantsTest ct = new ConstantsTest(driveLoop);
		ct.execute();
		double correctSlope = 0.9908;
		assertEquals(correctSlope, Util.kV_EMPIR, 1E-2);
	} //end ConstantsTest
	
	@Test
	/**
	 * Check if the length of the path is correct
	 */
	void pathLengthTest() {
		BezierPath testPath = new BezierPath(new Point[] {
    		new Point(7.3, 3.3),
    		new Point(4.7, 11.9),
    		new Point(32.3, 3.5),
    		new Point(16.4, 25.2),
    		new Point(24.2, 37.6),
    		new Point(38.3, 24.8)
		}, BezierPath.FAST_RES);
		double correctLength = 46.5;
		assertEquals(correctLength, testPath.getLength(), 1);
	} //end pathLengthsTest
	
	@Test
	/**
	 * Check if the path point calculated at a t value is correct
	 */
	void pathPointTest() {
		BezierPath testPath = new BezierPath(FieldPoints.curve, BezierPath.FAST_RES);
		Point correctPoint = new Point(5.4, 3.7);
		assertEquals(0.0, FieldPositioning.calcDistance(correctPoint, testPath.calcPoint(0)), 0.1);
	} //end pathPointTest
	
	@Test
	/**
	 * Path heading test (dx = 0, dy > 0)
	 */
	void pathHeadingTest1() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,1);
		double correctHeading = 0;
		
		assertEquals(correctHeading, FieldPositioning.calcGoalYaw(p1, p2), 1E-3);
	} //end pathHeadingTest1
	
	@Test
	/**
	 * Path heading test (dy = 0, dx > 0)
	 */
	void pathHeadingTest2() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(1,0);
		double correctHeading = 90;
		
		assertEquals(correctHeading, FieldPositioning.calcGoalYaw(p1, p2), 1E-3);
	} //end pathHeadingTest2
	
	@Test
	/**
	 * Path heading test (dx = 0, dy < 0)
	 */
	void pathHeadingTest3() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(0,-1);
		double correctHeading = 180;
		
		assertEquals(correctHeading, FieldPositioning.calcGoalYaw(p1, p2), 1E-3);
	} //end pathHeadingTest3
	
	@Test
	/**
	 * Path heading test (dy = 0, dx < 0)
	 */
	void pathHeadingTest4() {
		Point p1 = new Point(0,0);
		Point p2 = new Point(-1,0);
		double correctHeading = -90;
		
		assertEquals(correctHeading, FieldPositioning.calcGoalYaw(p1, p2), 1E-3);
	} //end pathHeadingTest4
	
	@Test
	/**
	 * Linear interpolation test
	 */
	void linearInterpolationTest() {
		assertEquals(1.17608, Util.interpolate(212, 1.1555, 200, 1.1898, 220), 1E-3);
	} //end linearInterpolationTest
	
	@Test
	/**
	 * Test getting data HashMap from the robot
	 */
	void robotDataTest() {
		Command c = new TimedVoltage(driveLoop, 6.0, 2);
		c.run();
		
		double dataPoint = (double) driveLoop.getRobot().getData().get(ROBOT_KEY.AVG_POS);
		assertEquals(135, dataPoint, 1);
	} //end robotDataTest
	
	@Test
	/**
	 * Test the upper limits of regulated clamping
	 */
	void regulatedClampUpperLimitTest() {
		assertEquals(-1, Util.regulatedClamp(-1.2, 0.2, 1), 0E-3);
	} //end regulatedClampingTest
	
	@Test
	/**
	 * Test the lower limits of regulated clamping
	 */
	void regulatedClampingLowerLimitTest() {
		assertEquals(-0.2, Util.regulatedClamp(-0.1, 0.2, 1), 0E-3);
	} //end regulatedClampingTest
	
	@Test
	/**
	 * Test the default case of regulated clamping
	 */
	void regulatedClampingTest() {
		assertEquals(-0.6, Util.regulatedClamp(-0.6, 0.2, 1), 0E-3);
	} //end regulatedClampingTest
	
	@Test
	/**
	 * Test the number parsing utility function
	 */
	void numberParseTest() {
		assertEquals(0.7125, Util.stringToNum("0.7125"), 0E-2);
	} //end numberParseTest
	
	@Test
	/**
	 * Test the functionality of getting the Circle's state
	 */
	void circleStateTest() {
		Circle c = new Circle(20, 20, Color.GREEN);
		c.setHovered();
		try {
			assertEquals(true, c.getState().equals(BUTTON_STATE.HOVER));
		} catch (AssertionError a) {
			System.out.println("Expected: HOVERED, Actual: " + c.getState());
			throw a;
		} //try-catch
	} //end circleStateTest
	
	@Test
	/**
	 * Test the functionality of the trapezoidal profile
	 */
	void trapProfileTest() {
		TrapezoidalProfile tp = new TrapezoidalProfile(100, 24, 12);
		
		/* Print positions and velocities
		 for (double t = 0; t < tp.getTotalTime(); t += Util.UPDATE_PERIOD) {
			double[] l = tp.getLeftTrajPoint(t);
			double[] r = tp.getRightTrajPoint(t);
			Util.tabPrint(t, l[0], r[0], l[1], r[1], l[2], r[2]);
			System.out.println();
		} //loop
		*/		
		
		//open chart plot
		//PlotGenerator.displayChart(PlotGenerator.createLinearTrajChart(tp, "trapProfileTest", 1920, 1080, 1));
		
		assertEquals(100.0, tp.getLeftTrajPoint(tp.getTotalTime())[0], 1);
	} //end trapProfileTest
	
	@Test
	/**
	 * Testing time calculations for linear trajectories
	 */
	void linearTimeCalculationTest() {
		double maxVel = 144; //inches
		double accDist = 24; //inches
		
		double trajAcc = Math.pow(maxVel, 2) / (2 * accDist);
		double t1 = Math.sqrt((2 * accDist) / trajAcc);
		
		double t2 = (2 * accDist) / maxVel;
		
		assertEquals(t1, t2, 0.05);
	} //end linearTimeCalculationTest
} //end class
