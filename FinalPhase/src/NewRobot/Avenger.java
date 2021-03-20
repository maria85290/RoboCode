package NewRobot;


import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.io.IOException;
import java.util.Random;

import NewRobot.RobotColors;
import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

//Teammate
public class Avenger extends TeamRobot {

	private String nameL;
	public boolean isDead = false;
	public boolean startScanning = false;
    Position myPos;
	boolean guard = false;
	boolean inGuardPos = false;
	boolean movingForward;
	private String enemyName;
	int dist = 50; // distance to move when we're hit
	
	public void run() {
		while (true) {
			// Comportamento fastBoi
			if (!guard) {
				setAhead(40000);
				movingForward = true;
				setTurnRight(90);

				waitFor(new TurnCompleteCondition(this));

				setTurnLeft(180);
				waitFor(new TurnCompleteCondition(this));
				setTurnRight(180);
				waitFor(new TurnCompleteCondition(this));
			}
			// guard behaviour
			else {
				turnGunRight(10); // Scans automatically
			}
		}

	}

	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);

		if ((e.getName().equals(enemyName))) {
			System.out.println("Found enemy whos names is" + e.getName());
			if (!guard) {
				setTurnRight(e.getBearing());
				// if we've turned toward our enemy...
				if (Math.abs(getTurnRemaining()) < 10) {

					// move a little closer
					if (e.getDistance() > 200) {
						ahead(e.getDistance() / 2);
						fire(2);
					}
					// but not too close
					if (e.getDistance() < 100) {
						back(e.getDistance() * 2);
					}
				}
				// lock our radar onto our target
				setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
			} else {
				// Calculate exact location of the robot
				double absoluteBearing = getHeading() + e.getBearing();
				double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

				// If it's close enough, fire!
				if (Math.abs(bearingFromGun) <= 10) {
					turnGunRight(bearingFromGun);
					// We check gun heat here, because calling fire()
					// uses a turn, which could cause us to lose track
					// of the other robot.
					if (getGunHeat() == 0 && e.getDistance() < 50 && getEnergy() > 50) {
						fire(3);
					} // otherwise, fire 1.
					else {
						fire(1);
					}
				} // otherwise just set the gun to turn.
					// Note: This will have no effect until we call scan()
				else {
					turnGunRight(bearingFromGun);
				}
				// Generates another scan event if we see a robot.
				// We only need to call this if the gun (and therefore radar)
				// are not turning. Otherwise, scan is called automatically.
				if (bearingFromGun == 0) {
					scan();
				}
			}
		}
	}

	public void onMessageReceived(MessageEvent e) {
		if (e.getMessage() instanceof RobotColors) {

			RobotColors c = (RobotColors) e.getMessage();

			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		} else if (e.getMessage() instanceof SendName) {

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
		} else if (e.getMessage() instanceof Position) {
			System.out.println("Got Leader Position");
			myPos = (Position) (e.getMessage());

			guard = true;

			while (!(this.getX() == myPos.getX() && this.getY() == myPos.getY())) {
				goTo(myPos.getX(), myPos.getY());
			}
		} else if (e.getMessage().equals("Position")){
			try {
				System.out.println("Sending pos!");;
				sendMessage("NewRobot.CapAmerica", new SendPosition(this.getName(), new Position(getX(), getY())));
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		} else if (e.getMessage() instanceof String) {
			System.out.println("Got enemy Pos");
			enemyName = (String) (e.getMessage());

			startScanning = true;

		} else if (e.getMessage() instanceof SendGuardPos) {
			System.out.println("Im a guard!!");
			SendGuardPos guardPos = (SendGuardPos) e.getMessage();
			double x = guardPos.getPos().getX();
			double y = guardPos.getPos().getY();
			guard = true;
			while (inGuardPos == false) {
				goTo(x, y);
				if (getX() > x - 5 && getX() < x + 5 && getY() > y - 5 && getY() < y + 5){
					System.out.println("THERE ALREADY");
					inGuardPos = true;
				}
			}

		}
	}

	
	public void onHitRobot(HitRobotEvent e){
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
	 * onHitByBullet:  Turn perpendicular to the bullet, and move a bit.
	 */
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
