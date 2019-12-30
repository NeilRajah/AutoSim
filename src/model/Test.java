package model;

public class Test {

	public static void main(String[] args) {
		Gearbox gb = new Gearbox(120, new Motor(2.41, 131, 5330, 2.7), 2);
		Robot r = new Robot(4, 150, 30, 30, gb, gb);
		
		Runnable applyVoltage = () -> {
			double t = 0.0;
			while (t < 10.0) {
				System.out.println(r.getAveragePos());
				r.update(12, 12);
				try {
					Thread.sleep(5);
				} catch (Exception e) {
					
				}
				
				t += 0.005;
			}
		};
		
		Thread t = new Thread(applyVoltage);
		t.start();
	}
	
	public static double cube(double x) {
		return x*x*x;
	}

}
