package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

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
		Gearbox gb = new Gearbox(120, new Motor(2.41, 131, 5330, 2.7), 2);
		Robot r = new Robot(4, 150, 30, 30, gb, gb);
		
		Runnable applyVoltage = () -> {
			double t = 0.0;
			while (t < 5.0) {
				r.update(12, 12);
				try {
					Thread.sleep(5);
				} catch (Exception e) {}
				
				t += 0.005;
			}
			double correctDisplacement = 100;
			assertEquals(correctDisplacement, r.getAveragePos(), 1);
		};
		
		Thread t = new Thread(applyVoltage);
		t.start();
	}

}
