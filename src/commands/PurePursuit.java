/**
 * PurePursuit
 * Author: Neil Balaskandarajah
 * Created on: 17/05/2020
 * Follow a set of goals using the pure pursuit algorithm
 */

package commands;

import model.DriveLoop;
import model.Point;
import model.DriveLoop.STATE;
import model.motion.PurePursuitController;

public class PurePursuit extends Command {
	//Attributes
	private DriveLoop loop; //drivetrain state machine to control
	private Point[] goals;
	private PurePursuitController ppc;
	
	public PurePursuit(DriveLoop loop, PurePursuitController ppc) {
		//set attributes
		this.loop = loop;
		this.ppc = ppc;
		this.robot = loop.getRobot();
	}

	@Override
	protected void initialize() {
		loop.setPurePursuitController(ppc);
		loop.setState(STATE.PURE_PURSUIT);
		
		this.robot.setLookahead(ppc.getLookahead());
		this.robot.setGoalPoint(ppc.getGoal());
	}

	@Override
	protected void execute() {
		loop.updatePurePursuitState(loop.getRobot().getPose());
		loop.onLoop();
	}

	@Override
	protected boolean isFinished() {
		return ppc.isArrived();
	}

	@Override
	protected void end() {
		ppc.writeToFile();
	}

	@Override
	protected void timedOut() {
		// TODO Auto-generated method stub

	}

}
