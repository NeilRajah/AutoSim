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
	 * @return the yaw setpoint for the robot to face the goal point
	 */
	public static double calcGoalYaw(Point current, Point goal) {
		double dx = goal.getX() - current.getX();
		double dy = goal.getY() - current.getY();
		
		double goalYaw = 0;
		
		//if goal point lies on x or y axis (dx or dy equal to zero)
		if(dy == 0) { //if no change in y
			if(dx > 0) {
				goalYaw = 90; //dead right
			} else if (dx < 0) {
				goalYaw = -90; //dead left
			} //if
		} else if (dx == 0) { //if no change in x
			if(dy > 0) {
				goalYaw = 0; //dead ahead
			} else if (dy < 0) {
				goalYaw = 180; //dead behind
			} //if
		} else {
			if (dy < 0) { //point is behind you
				if (dx > 0) {
					goalYaw = 90 - Math.toDegrees(Math.atan(dy/dx)); //behind and right
				} else if (dx < 0) {
					goalYaw = -90 - Math.toDegrees(Math.atan(dy/dx)); //behind and left
				} //if
			} else { //anywhere else
				goalYaw = Math.toDegrees(Math.atan2(dx,dy));
			} //if
		} //if
		
		return goalYaw;
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
		double theta = 0;
		
		if (deltaX == 0) {
			if (deltaY > 0) //point above
				theta = 0;
			else if (deltaY < 0) //point below
				theta = 180;
		} else if (deltaY == 0) {
			if (deltaX > 0) //point to right
				theta = 90;
			else if (deltaX < 0)
				theta = -90; //point to left
		} else {
			theta = Math.atan(deltaY/deltaX); //anywhere else 
		} //if
		
		return theta;
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
		
		if (dx < range && dy < range) {
			return true;
		} else {
			return false;
		} //if
	} //end isWithinBounds
} //end class
