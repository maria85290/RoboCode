package NewRobot;


import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import robocode.*;
import robocode.util.Utils;

@SuppressWarnings("Duplicates")


//Teammate
public class Avenger extends TeamRobot {

	private String nameL;
	private String enemyName;

	private boolean afterGuard = false;
	private boolean startScanning = false;
	private boolean guard = false;
	private boolean movingForward;

	//Bodyguard position
    Position myPos;

	// distance to move when we're hit
	int dist = 50;

	//Teammates list
	private ArrayList<String> teamsArray = new ArrayList<>();

	public void run() {

		//Set Robot Colors
		this.setColors(Color.BLUE,Color.RED,Color.RED,Color.WHITE,Color.ORANGE);

		while (true) {
				turnGunRight(10); // Scans automatically
		}

	}

	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);

		//If the enemy scanned is the enemy sent from Leader CapAmerica
		if ((e.getName().equals(enemyName))) {

			System.out.println("Got enemys name" + e.getName());

			double absoluteBearing = getHeading() + e.getBearing();
			double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

			turnGunRight(bearingFromGun);

			//get the distance of the scanned robot
			manageFirePower(e);

		}

		if(!teamsArray.contains(e.getName())){

			System.out.println("Scanned enemy name: " + e.getName());

			//If Avneger is in guard mode
			if(guard) {

				double absoluteBearing = getHeading() + e.getBearing();
				double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

				turnGunRight(bearingFromGun);
				manageFirePower(e);

				//If Avenger went rogue : after Leader turns into walls mode
			} else if(afterGuard){

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
					//get the distance of the scanned robot
				manageFirePower(e);

				// lock our radar onto our target
				setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());

			}
		}

	}

	private void manageFirePower(ScannedRobotEvent e) {
		//get the distance of the scanned robot
		double distance = e.getDistance();

		//Managing power according to distance to enemy
		if (distance > 800)
			fire(5);
		else if (distance > 600 && distance <= 800)
			fire(4);
		else if (distance > 400 && distance <= 600)
			fire(3);
		else if (distance > 200 && distance <= 400)
			fire(2);
		else if (distance < 200)
			fire(1);
	}

	public void onMessageReceived(MessageEvent e) {

		if (e.getMessage() instanceof SendName) {

			System.out.println("[MSG] Got Leaders name. ");

			SendName n = (SendName) e.getMessage();

			this.nameL = n.getName();

			// Respond with my own name
			try {
				sendMessage(this.nameL, new SendName(getName()));
			} catch (IOException a) {
				a.printStackTrace();
			}

		} else if (e.getMessage() instanceof Position) {

			System.out.println("[MSG] Got my bodyguard position.");

			myPos = (Position) (e.getMessage());

			//Guard mode ON
			guard = true;

			while (!(this.getX() == myPos.getX() && this.getY() == myPos.getY())) {
				goTo(myPos.getX(), myPos.getY());
			}

		} else if (e.getMessage() instanceof String) {

			System.out.println("[MSG] Got enemy's name.");

			enemyName = (String) (e.getMessage());

			//Start looking for my enemy
			startScanning = true;

		// Receives flag to know when leader is in WallsMode
		} else if(e.getMessage() instanceof Boolean){

			guard=false;
			afterGuard = true;

		//Got my teammate's name. My name is included on the list.
		} else if(e.getMessage() instanceof ArrayList){

			teamsArray = (ArrayList) e.getMessage();

			//Adds the name of the leader to my teammates
			teamsArray.add(nameL);
		}
	}

	
	public void onHitRobot(HitRobotEvent e){

		//If on guard mode, get back into bodyguard position.
		if(guard){

			while(!(this.getX() == myPos.getX() && this.getY() == myPos.getY())) {
				goTo(myPos.getX(), myPos.getY());
			}

		}else {
			Random r = new Random();
			int goRandom = r.nextInt(1);
			//move away randomly
			//he is behind us so set back a bit
			if(e.getBearing() > -90 && e.getBearing() <= 90){
				if(goRandom == 1){
					setTurnRight(45);
					back(150);
				}else{
					setTurnLeft(45);
					back(150);
				}
			}else{
				if(goRandom == 1){
					setTurnRight(45);
					ahead(150);
				}else{
					setTurnLeft(45);
					ahead(150);
				}
			}
		}
    }

	public void onRobotDeath(RobotDeathEvent e){

		//If my initial enemy dies, start scanning
		if(e.getName().equals(enemyName)){
			scan();
		}
	}

	public void onHitWall(HitWallEvent e) {
		reverseDirection();
	}

	public void reverseDirection() {
		if (movingForward) {
			setBack(40000);
			movingForward = false;
		} else {
			setAhead(40000);
			movingForward = true;
		}
	}

	public void onHitByBullet(HitByBulletEvent e) {
		if(!guard) {
			turnRight(normalRelativeAngleDegrees(90 - (getHeading() - e.getHeading())));
	
			ahead(dist);
			dist *= -1;
			scan();
		}
	}

	
	private void goTo(double x, double y) {

		x -= getX();
		y -= getY();

		double angleToTarget = Math.atan2(x, y);

		double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());

		double distance = Math.hypot(x, y);

		/* This is a simple method of performing set front as back */
		double turnAngle = Math.atan(Math.tan(targetAngle));
		turnRightRadians(turnAngle);

		if (targetAngle == turnAngle) {
			ahead(distance);
		} else {
			back(distance);
		}
	}

}
