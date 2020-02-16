/*
 * FieldPoints
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2020
 * Static class holding all key points 
 */

package util;

import model.Point;

public class FieldPoints {
	//Curves
	public static final Point[] curve1 = new Point[] {
		new Point(7.3, 3.3),
		new Point(4.7, 11.9),
		new Point(32.3, 3.5),
		new Point(16.4, 25.2),
		new Point(24.2, 37.6),
		new Point(38.3, 24.8)
	};
	
	public static double[][] curve = new double[][] {
		{5.4, 3.7},
		{149.4, 5},
		{170.6, 74.9},
		{22, 36.4},
		{27.6, 113.3},
		{177.6, 109.5}
	};
	
	//Points
	public static final Point MID_INITIATION = new Point(162, 522);
	
	//climbing points
	public static final Point MID_BLUE = new Point(202, 344);
	public static final Point MID_RED = new Point(118, 301);
	
	//loading zones
	public static final Point LOADING_BLUE = new Point(222, 15);
	public static final Point LOADING_RED = new Point(100,634);
	
	//target zones
	public static final Point TARGET_BLUE = new Point(231, 630);
	public static final Point TARGET_RED = new Point(94, 15);
	
	//in front of control panel (would run sequence from here)
	public static final Point CONTROL_PANEL_BLUE = new Point(293, 214);
	public static final Point CONTROL_PANEL_RED = new Point(25, 433);
	
	//trench shot point (would run sequence from here)
	public static final Point TRENCH_SHOT_BLUE = new Point(293, 414);
	public static final Point TRENCH_SHOT_RED = new Point(25, 233);
	
	//points for moving through rendesvouz zone
	public static final Point RDVZ = new Point(233, 440);
	public static final Point RDVZ_MID = new Point(232, 314);
	public static final Point RDVZ_MID2 = new Point(222, 264);
	
	//positions for getting balls in auto through rendezvouz zone
	public static final Point AUTO_POS_ONE = new Point(138, 430);
	public static final Point AUTO_POS_TWO = new Point(146, 398);
	public static final Point AUTO_POS_THREE = new Point(178, 411);
	public static final Point AUTO_POS_FOUR = new Point(200, 405);
	public static final Point AUTO_POS_FIVE = new Point(212, 370);
	
	//auto shot position
	public static final Point AUTO_SHOT = new Point(248,522);
} //end class