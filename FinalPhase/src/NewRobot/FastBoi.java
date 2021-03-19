package NewRobot;

import robocode.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FastBoi extends TeamRobot {
	boolean movingForward;
	private String enemyName;
	String[] teammates;
	private String nameL;
	private boolean startScanning = false;
	private  boolean in_attack = false; // quando tem uma alvo fica a True;

	/**
	 * run: Crazy's main run function
	 */
	public void run() {

		// Loop forever
		while (true) {
			// Tell the game we will want to move ahead 40000 -- some large number
			setAhead(40000);
			movingForward = true;
			// Tell the game we will want to turn right 90
			setTurnRight(90);
			// At this point, we have indicated to the game that *when we do something*,
			// we will want to move ahead and turn right. That's what "set" means.
			// It is important to realize we have not done anything yet!
			// In order to actually move, we'll want to call a method that
			// takes real time, such as waitFor.
			// waitFor actually starts the action -- we start moving and turning.
			// It will not return until we have finished turning.
			waitFor(new TurnCompleteCondition(this));
			// Note: We are still moving ahead now, but the turn is complete.
			// Now we'll turn the other way...
			setTurnLeft(180);
			// ... and wait for the turn to finish ...
			waitFor(new TurnCompleteCondition(this));
			// ... then the other way ...
			setTurnRight(180);
			// .. and wait for that turn to finish.
			waitFor(new TurnCompleteCondition(this));
			// then back to the top to do it all again
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {

		System.out.println(e.getName() + " found enemy ");
		setTurnRight(e.getBearing());
		// if we've turned toward our enemy...
		if (Math.abs(getTurnRemaining()) < 10) {
			// move a little closer
			if (e.getDistance() > 200) {
				setAhead(e.getDistance() / 2);
			}
			// but not too close
			if (e.getDistance() < 100) {
				setBack(e.getDistance() * 2);
			}
		}
		if (e.getEnergy() > 16) {
			fire(3);
		} else if (e.getEnergy() > 10) {
			fire(2);
		} else if (e.getEnergy() > 4) {
			fire(1);
		} else if (e.getEnergy() > 2) {
			fire(.5);
		} else if (e.getEnergy() > .4) {
			fire(.1);
		}
		// lock our radar onto our target
		setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());

	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof RobotColors) {

			RobotColors c = (RobotColors) e.getMessage();

			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}
		else if (e.getMessage() instanceof SendName) {

			System.out.println("[MSG] Recebi nome do Lider. ");
			SendName n = (SendName) e.getMessage();

			this.nameL = n.getName();

			// Enviar o nome do robot
			try {
				sendMessage(this.nameL, new SendName(getName()));
				System.out.println("Enviei o meu nome. ");
			} catch (IOException a) {
				a.printStackTrace();
			}

			// Recebeu o nome de um inimigo para atacar;
		} else if (e.getMessage() instanceof String) {
			enemyName = (String) (e.getMessage());
			System.out.println("Got enemy Name: " + enemyName);
			in_attack = true;
			startScanning = true;
		}
	}

	/**
	 * onHitWall: Handle collision with wall.
	 */
	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}

	/**
	 * reverseDirection: Switch from ahead to back & vice versa
	 */
	public void reverseDirection() {
		if (movingForward) {
			setBack(40000);
			movingForward = false;
		} else {
			setAhead(40000);
			movingForward = true;
		}
	}


	/**
	 * onHitRobot: Back up!
	 */
	public void onHitRobot(HitRobotEvent e) {
		setTurnLeft(10);
		back(15);
	}

	public void onRobotDeath(RobotDeathEvent e) {
		setTurnRadarRight(1000);
	}


}
