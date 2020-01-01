package model;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import util.Util;

class ModelTest {
	static Gearbox gb;
	static Robot r;
	
	@BeforeAll
	public static void setUp() {
		gb = new Gearbox(8.2554, new Motor(2.6, 105, 5676, 1.8), 2); //NEO
//		gb = new Gearbox(6.6447, new Motor(2.41, 131, 5330, 2.7), 2); //CIM
		r = new Robot(4, 153, 30, 30, gb);
	}
	
	@AfterEach
	public void reset() {
		r.reset();
		System.out.println();
	}
	
//	@Test
	void gearboxTorqueTest() {
		double torque = gb.calcTorque(12);
		
		double correctValue = 42.928;
		assertEquals("Torque should be " + correctValue + ", calculated to be " + torque, correctValue, torque, 1);
	}
	
	@Test
	void displacementTest() { 
		double t = 0.00;
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			Gearbox lgb = r.getLeftGearbox();
			Gearbox rgb = r.getRightGearbox();
			r.update(12, 12);
			Util.println("", t, lgb.getPos(), lgb.getVel(), lgb.getAcc(), rgb.getPos(), rgb.getVel(), rgb.getAcc());
			t += Util.UPDATE_PERIOD;
		}
		double correctDisplacement = 123;
		assertEquals(correctDisplacement, r.getAveragePos(), 1);
	}
	
//	@Test
	void poseTest() {
		double t = 0.00;
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12,12);
			t += Util.UPDATE_PERIOD;
		}
		Point correctPoint = new Point(0,123);
		assertEquals(correctPoint.getY(), r.getPoint().getY(), 1);
	}
	
//	@Test
	void headingTest() {
		double t = 0.00;
		while (t <= 0.01 + Util.UPDATE_PERIOD) {
			Gearbox lgb = r.getLeftGearbox();
			Gearbox rgb = r.getRightGearbox();
			r.update(-12, 12);
			Util.println("Time: " + t +" | ", lgb.getVel(), rgb.getVel(), lgb.getAcc(), rgb.getAcc());
			t += Util.UPDATE_PERIOD;
			System.out.println();
		}
		double correctHeading = 500;
		assertEquals(correctHeading, Math.toDegrees(r.getHeading()), 1);
	}
	
//	@Test
	void signTest() {
		for (double t = 0; t <= 1.00; t += Util.UPDATE_PERIOD) {
			
		}
	}

}
