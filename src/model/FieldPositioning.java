/**
 * FieldPositioning
 * Author: Neil
 * Created on: 02/01/2020
 * Utility methods related to field relative positioning
 */
package model;

public class FieldPositioning {

	/**
	 * Calculate the goal yaw setpoint based on the current and goal positions
	 * @param current - the (x,y) position of the robot currently
	 * @param goal - the desired (x,y) position of the robot
	 * @return the yaw setpoint for the robot to face the goal point in degrees
	 */
	public static double calcGoalYaw(Point current, Point goal) {
		double dx = goal.getX() - current.getX();
		double dy = goal.getY() - current.getY();
		
		//if goal point lies on x or y axis (dx or dy equal to zero)
		if(dy == 0) { //if no change in y
			return dx > 0 ? 90 : -90;
		} else if (dx == 0) { //if no change in x
			return dy > 0 ? 0 : 180;
		} else {
			if (dy < 0) { //point is behind you
				if (dx > 0) {
					return 90 - Math.toDegrees(Math.atan(dy/dx)); //behind and right
				} else if (dx < 0) {
					return -90 - Math.toDegrees(Math.atan(dy/dx)); //behind and left
				} //if
			} //if
		} //if
		return Math.toDegrees(Math.atan2(dx,dy)); //anywhere else
	} //end calcGoalYaw

	/**
	 * Calculate the distance between two points using the Pythagorean theorem
	 * @param p1 - first point
	 * @param p2 - second point
	 * @return - absolute distance between the two points
	 */
	public static double calcDistance(Point p1, Point p2) {
		return Math.hypot(p2.getX() - p1.getX(), p2.getY() - p1.getY());
	} //endd calcDistance
	
	/**
	 * Calculate the angle between two points in radians from the x-axis
	 * @param p1 - first point
	 * @param p2 - second point
	 * @return - angle between the two point in radians from the x-axis
	 */
	public static double calcAngleRad(Point p1, Point p2) {
		//calculate deltas
		double deltaX = p2.getX() - p1.getX();
		double deltaY = p2.getY() - p1.getY();
		
		if (deltaX == 0) {
			return deltaY > 0 ? 0 : Math.PI;
		} else if (deltaY == 0) {
			return deltaX > 0 ? Math.PI/2 : -Math.PI/2;
		} //if
		return Math.atan(deltaY/deltaX); //anywhere else 
	} //end calcAngleRad
	
	/**
	 * Calculate the angle between two points in degrees from the x-axis
	 * @param p1 - first point
	 * @param p2 - second point
	 * @return - angle between the two point in degrees from the x-axis
	 */
	public static double calcAngleDeg(Point p1, Point p2) {
		return Math.toDegrees(calcAngleRad(p1, p2));
	} //end calcAngleDeg
	
	/**
	 * Check if two points are close enough to another within a range
	 * @param target - target point to be close to
	 * @param current - point to check closeness of
	 * @param range - how close current point can be to target point
	 */
	public static boolean isWithinBounds(Point target, Point current, double range) {
		//calculate deltas
		double dx = Math.abs(target.getX() - current.getX());
		double dy = Math.abs(target.getY() - current.getY());
		
		return dx < range && dy < range;
	} //end isWithinBounds
	
	/**
	 * Flip the x and y values of curve points
	 * @param curve - curve to flip
	 * @return - curve with its x and y values flipped
	 */
	public static double[][] flipCurve(double[][] curve) {
		for (int i = 0; i < curve.length; i++) {
			double buffer = curve[i][0];
			curve[i][0] = curve[i][1];
			curve[i][1] = buffer;
		} //loop
		
		return curve;
	} //end flipCurve
} //end class
