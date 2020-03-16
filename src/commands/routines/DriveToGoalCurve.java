/**
 * DriveToGoalCurve
 * Author: Neil Balaskandarajah
 * Created on: 2020/03/15
 * Follow a curve using point-to-point driving
 */

package commands.routines;

import commands.CommandGroup;
import commands.DriveToGoal;
import model.DriveLoop;
import model.FieldPositioning;
import model.Point;
import model.motion.QuinticBezierPath;
import util.Util;

public class DriveToGoalCurve extends CommandGroup {
	
	public DriveToGoalCurve(DriveLoop loop, QuinticBezierPath curve) {
		loop.getRobot().setXY(curve.calcPoint(0));
		loop.getRobot().setHeadingDegrees(curve.calcHeading(0) + 180);
		
		double maxVel = loop.getRobot().getMaxLinSpeed();
		
		Point lastPoint = curve.calcPoint(0);
		for (double t = 0; t <= 1.0; t += 0.01) {
			Point goal = curve.calcPoint(t);
//			double scale = FieldPositioning.calcDistance(lastPoint, goal) / curve.getLength();
//			double scale = 0.8 / Math.abs(curve.calcCurvature(t));
//			Util.println(scale);
			double scale = t <= 0.5 ? 5 * t : 1 - 5 * t;
//			double minScale;
			
			this.add(new DriveToGoal(loop, goal, 1, maxVel * scale, 0.75 * maxVel * scale, false));
			lastPoint = goal;
		}
		
		this.addCurve(curve.getPolyline());
	}
	
	public DriveToGoalCurve(DriveLoop loop, double[][] controlPoints) {
		this(loop, new QuinticBezierPath(controlPoints));
	}
	
	public DriveToGoalCurve(DriveLoop loop, Point[] controlPoints) {
		this(loop, new QuinticBezierPath(controlPoints));
	}
} //end class
