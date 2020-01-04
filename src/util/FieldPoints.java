/*
 * FieldPoints
 * Author: Neil Balaskandarajah
 * Created on: 02/01/2020
 * Static class holding all key points 
 */

package util;

import model.Point;

public class FieldPoints {
	//Points
	 //level 2
    public static final Point RIGHT_LEVEL_2 = new Point(207, 40);
    public static final Point LEFT_LEVEL_2 = new Point(117, 40);
    public static final Point ll2Plus = new Point(150, 140);

    //off platform
    public static final Point RIGHT_OFF_PLATFORM = new Point(207, 115);
    public static final Point LEFT_OFF_PLATFORM = new Point(117, 115);

    //rocket
    public static final Point RIGHT_CLOSE_ROCKET = new Point(283,165);
    public static final Point RIGHT_FAR_ROCKET = new Point(264,285);

    public static final Point RIGHT_ROCKET = new Point(312,230);
    public static final Point CLOSE_RIGHT_ROCKET_SCORE = new Point(299, 195);

    public static final Point LEFT_CLOSE_ROCKET = new Point(41,165);
    public static final Point LEFT_FAR_ROCKET = new Point(60, 295);
    public static final Point LEFT_ROCKET = new Point(12,230);
    public static final Point CLOSE_LEFT_ROCKET_SCORE = new Point(25, 195);

    //feeder
    public static final Point PRE_RIGHT_FEEDER = new Point(300, 60);
    public static final Point RIGHT_FEEDER = new Point(300, 0);

    public static final Point PRE_LEFT_FEEDER = new Point(24, 60);
    public static final Point LEFT_FEEDER = new Point(24, 18); //24,0

    //ejection distances
    public static final double CARGO_SHIP_EJECT_DIST = 6.5;
    public static final double ROCKET_EJECT_DIST = 3;

    //cargo ship
    public static final Point CLOSE_RIGHT_CARGO_PRE_SCORE = new Point(230, 260);
    public static final Point MID_RIGHT_CARGO_PRE_SCORE = new Point(230, 286);
    public static final Point PRE_CLOSE_RIGHT_CARGO = new Point(134, 235);
    public static final Point CLOSE_RIGHT_CARGO = new Point(176, 254);
    public static final Point MID_RIGHT_CARGO = new Point(176, 280);

    public static final Point CLOSE_LEFT_CARGO_PRE_SCORE = new Point(94, 260);
    public static final Point MID_LEFT_CARGO_PRE_SCORE = new Point(94, 286);
    public static final Point PRE_CLOSE_LEFT_CARGO = new Point(190, 235);
    public static final Point CLOSE_LEFT_CARGO = new Point(148, 254);
    public static final Point MID_LEFT_CARGO = new Point(148, 280);
} //end class