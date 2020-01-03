package util;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import model.Gearbox;
import model.Motor;
import model.Point;
import model.Robot;

class ModelTest {
	static Gearbox gb;
	static Robot r;
	
	@BeforeAll
	public static void setUp() {
		gb = new Gearbox(8.5521, new Motor(Util.NEO), 2); //NEO
		r = new Robot(4, 153, 30, 30, gb);
	}
	
	@AfterEach
	public void reset() {
		r.reset();
	}
	
	@Test
	void gearboxTorqueTest() {
		double torque = gb.calcTorque(12);
		
		double correctValue = 57.47;
		assertEquals("Torque should be " + correctValue + ", calculated to be " + torque, correctValue, torque, 1);
	}
	
	@Test
	void displacementTest() { 
		double t = 0.00;
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12, 12);
			t += Util.UPDATE_PERIOD;
		}
		double correctDisplacement = 128.6;
		assertEquals(correctDisplacement, r.getAveragePos(), 1);
	}
	
	@Test
	void poseTestX() {
		double t = 0.00;
		
		r.setHeading(2);
		
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12,12);
			t += Util.UPDATE_PERIOD;
		}
		
		Point correctPoint = new Point(-53.5, 116.9);
		assertEquals(correctPoint.getX(), r.getPoint().getX(), 1);
	}
	
	@Test
	void poseTestY() {
		double t = 0.00;
		
		r.setHeading(2);
		
		while (t <= 1.0 + Util.UPDATE_PERIOD) {
			r.update(12,12);
			t += Util.UPDATE_PERIOD;
		}

		Point correctPoint = new Point(-53.5, 116.9);
		assertEquals(correctPoint.getY(), r.getPoint().getY(), 1);
	}
	
	@Test
	void headingTest() {
		double t = 0.00;
		while (t <= 1.00 + Util.UPDATE_PERIOD) {
			r.update(-12, 12);
			t += Util.UPDATE_PERIOD;
		}
		double correctHeading = 510;
		assertEquals(correctHeading, Math.toDegrees(r.getHeading()), 1);
	}
	
	
	@Test
	void speedTest() {
		double t = 0.00;
		while (t <= 1.00 + Util.UPDATE_PERIOD) {
			r.update(12, 12);
			t += Util.UPDATE_PERIOD;
		}
		double correctSpeed = 12;
		assertEquals(correctSpeed, r.getLinearVel(), 1);
	}
	
	@Test
	void topLinSpeedTest() {
		double correctTopSpeed = 12;
		assertEquals(correctTopSpeed, r.getMaxLinSpeed(), 1);
	}
	
	@Test
	void topAngSpeedTest() {
		double correctTopSpeed = 9.6;
		assertEquals(correctTopSpeed, r.getMaxAngSpeed(), 1);
	}
}
