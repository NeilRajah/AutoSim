package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import util.Util;

class ModelTest {
	
	@Test
	void gearboxTorqueTest() {
		Gearbox gb = new Gearbox(120, new Motor(2.41, 131, 5330, 2.7), 2);
		double torque = gb.calcTorque(12);
		
		double correctValue = 578.4;
		assertEquals("Torque should be " + correctValue + ", calculated to be " + torque, correctValue, torque, 1);
	}
	
	@Test
	void displacementTest() {
		Gearbox gb = new Gearbox(8.2554, new Motor(2.6, 105, 5676, 1.8), 2);
		Robot r = new Robot(4, 153, 30, 30, gb, gb);
		double t = 0.00;
		while (t < 1.0) {
			r.update(12,12);
			System.out.println(t +" "+ r.getAveragePos() + "\n");
			t += Util.UPDATE_PERIOD;
		}
		double correctDisplacement = 125;
		assertEquals(correctDisplacement, r.getAveragePos(), 1);
	}

}
