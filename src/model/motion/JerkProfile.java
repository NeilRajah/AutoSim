/**
 * Jerk Profile
 * Author: Neil Balaskandarajah
 * Created on: 13/01/2020
 * A 1-dimensional 5-segment motion profile
 */

package model.motion;

public class JerkProfile extends DriveProfile {
	
	public JerkProfile(double totalDist, double accDist, double maxVel) {
//		super(createProfile(totalDist, accDist, maxVel));
	}

	/**
	 * Compute all profile constants
	 */
	protected void computeConstants() {
		
	} //end computeConstants
	
	protected void fillProfiles() {
		// TODO Auto-generated method stub
		
	}
	
} //end JerkProfile
