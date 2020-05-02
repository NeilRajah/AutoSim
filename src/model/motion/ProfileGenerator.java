/**
 * ProfileGenerator
 * Author: Neil Balaskandarajah
 * Created on: 30/04/2020
 * Save profiles to files to be read and played
 */

package model.motion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import model.Point;
import model.Pose;
import util.FieldPoints;
import util.Util;

public class ProfileGenerator {

	public static void main(String[] args) {
		double[][] curve = FieldPoints.niceLongCurve;
		long init = System.currentTimeMillis();
		BezierProfile bezTraj = new BezierProfile(curve, 30, 12 * 12, 200, 200);
		Util.println(System.currentTimeMillis() - init + "ms");
		String filename = "niceLongCurve";
//		Util.println("Vels file saved " + bezTraj.saveVelsToFile(filename));
		bezTraj.saveVelsToFile(filename);
		
		ArrayList<Pose> poses = posesFromVelsFile(filename, bezTraj);
		posesToFile(poses, filename);
	} //end main
	
	private static ArrayList<Pose> posesFromVelsFile(String filename, BezierProfile profile) {
		ArrayList<Pose> poses = new ArrayList<Pose>();
		
		try {
			Scanner s = new Scanner(new File(Util.UTIL_DIR + filename + ".vels"));
			
			//parameters for calculating displacement vector
			double width = profile.getTrackWidth();
			double heading = Math.toRadians(profile.getInitialHeading());
			Point start = profile.getStartPosition();
			double dt = Util.UPDATE_PERIOD;
			
			//add first pose
			poses.add(new Pose(start, heading));
			double x = start.getX();
			double y = start.getY();
			
			while (s.hasNext()) {
				s.next(); //skip time token
				double leftDisp = s.nextDouble() * dt * 12; //v * t, inches
				double rightDisp = s.nextDouble() * dt * 12; //v * t, inches
				
				//displacement vector
				double mag = (leftDisp + rightDisp) / 2;
				double angle = (rightDisp - leftDisp) / width;
				
				//create point for pose
				x += mag * Math.sin(heading);
				y += mag * Math.cos(heading);
				Point p = new Point(x, y);
				
				//add pose
				poses.add(new Pose(p, heading));

				//update heading
				heading += angle;
			} //loop
			
			s.close();
			
		} catch (FileNotFoundException e) {
			Util.println("Could not find " + filename);
		} //try-catch
		
		return poses;
	} //end posesFromVelsFile
	
	private static void posesToFile(ArrayList<Pose> poses, String filename) {
		try {
			PrintWriter pw = new PrintWriter(new File(Util.UTIL_DIR + filename + ".poses"));
			
			//loop through poses list, adding (x,y,theta) to file
			for (int i = 0; i < poses.size(); i++) {
				double x = poses.get(i).getPoint().getX();
				double y = poses.get(i).getPoint().getY();
				double heading = poses.get(i).getHeading();
								
				pw.write(String.format("%f %f %f\n", x, y, heading));
			} //loop
			
			pw.close();
			
		} catch (FileNotFoundException e) {
			Util.println("Could not find " + filename);
		} //try-catch
	} //end posesToFile
	
	public static ArrayList<Pose> posesFromFile(String filename) {
		ArrayList<Pose> poses = new ArrayList<Pose>();
		
		try {
			Scanner s = new Scanner(new File(Util.UTIL_DIR + filename + ".poses"));
			
			while(s.hasNext()) {
				
				double x = s.nextDouble();
				double y = s.nextDouble();
				double heading = s.nextDouble();
				
				poses.add(new Pose(new Point(x, y), heading));
			} //loop
			
			s.close();
			
		} catch (FileNotFoundException e) {
			Util.println("Could not find " + filename);
		} //try-catch
		
		return poses;
	} //end posesFromFile
} //end class 