package model;

public class Test {

	public static void main(String[] args) {
		Ball ball = new Ball();
		
		System.out.println(ball.getRadius());
		ball.setRadius(20.0);
		System.out.println(ball.getRadius());
	}

}

class Ball {
	private double radius;
	
	public Ball() {
		radius = 0;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
}
