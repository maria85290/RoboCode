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
	boolean movingForward;
	private String enemyName;
	private String role;
	private boolean ready = false;
	public int quadrant = 0;  // variavel dos fastBoi
	int dist = 50; // distance to move when we're hit
	
	public void run() {
		while(ready==false){doNothing();}
		while (true) {

			/**
			 * IDEIA: O Lider envia o quadrante onde ele deve estar e envia tb a lista com so membros daa equipa.
			 * O lider tb envia a cada um a sua role.
			 * O lider vai para o respetivo quadrante. (se o lider esta no 1º quadrante, entáo casa fast boi vai estar no quadrantes 2 e 3)
			 * O fastboi fas scna dos inimigos que estao nesse quadrante e procura disparar sobre eles.
			 * Ataca-o ate ele morrer
			 *
			 */
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

		if (!guard) {

			System.out.println("Found enemy whos names is" + e.getName());

			double absoluteBearing = getHeading() + e.getBearing();
			double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

			double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);

			// Calculate the coordinates of the robot
			double scannedX = getX() + Math.sin(angle) * e.getDistance();
			double scannedY = getY() + Math.cos(angle) * e.getDistance();


			turnGunRight(bearingFromGun);

			System.out.println("MEio:" + getBattleFieldWidth() / 2);
			System.out.println("X:" + scannedX);


			//3º quadrante
			if (scannedX <= (getBattleFieldWidth() / 2) && scannedY <= (getBattleFieldHeight() / 2) && getX() <= (getBattleFieldWidth() / 2) && getY() <= (getBattleFieldHeight() / 2)) {
				System.out.println("Enemy " + e.getName() + "found!");
				if (e.getEnergy() > 16) {
					fire(3);
				} else if (e.getEnergy() > 10) {
					fire(2);
					System.out.println("Fire2");
				} else if (e.getEnergy() > 4) {
					fire(1);
					System.out.println("Fire3");
				} else if (e.getEnergy() > 2) {
					fire(.5);
					System.out.println("Fire4");
				} else if (e.getEnergy() > .4) {
					fire(.1);
					System.out.println("Fire5");
				}
				setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
			}
			//4º quadrante
			else if (scannedX >= (getBattleFieldWidth() / 2) && scannedY <= (getBattleFieldHeight() / 2) && getX() >= (getBattleFieldWidth() / 2) && getY() <= (getBattleFieldHeight() / 2)) {
				System.out.println("Enemy " + e.getName() + "found!");
				if (e.getEnergy() > 16) {
					fire(3);
				} else if (e.getEnergy() > 10) {
					fire(2);
					System.out.println("Fire2");
				} else if (e.getEnergy() > 4) {
					fire(1);
					System.out.println("Fire3");
				} else if (e.getEnergy() > 2) {
					fire(.5);
					System.out.println("Fire4");
				} else if (e.getEnergy() > .4) {
					fire(.1);
					System.out.println("Fire5");
				}
				setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
			}
			//1º quadrante
			else if (scannedX >= (getBattleFieldWidth() / 2) && scannedY >= (getBattleFieldHeight() / 2) && getX() >= (getBattleFieldWidth() / 2) && getY() >= (getBattleFieldHeight() / 2)) {
				System.out.println("Enemy " + e.getName() + "found!");
				if (e.getEnergy() > 16) {
					fire(3);
				} else if (e.getEnergy() > 10) {
					fire(2);
					System.out.println("Fire2");
				} else if (e.getEnergy() > 4) {
					fire(1);
					System.out.println("Fire3");
				} else if (e.getEnergy() > 2) {
					fire(.5);
					System.out.println("Fire4");
				} else if (e.getEnergy() > .4) {
					fire(.1);
					System.out.println("Fire5");
				}
				setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
			}
			//2º quadrante
			else if (scannedX <= (getBattleFieldWidth() / 2) && scannedY >= (getBattleFieldHeight() / 2) && getX() <= (getBattleFieldWidth() / 2) && getY() >= (getBattleFieldHeight() / 2)) {
				System.out.println("Enemy " + e.getName() + "found!");
				if (e.getEnergy() > 16) {
					fire(3);
				} else if (e.getEnergy() > 10) {
					fire(2);
					System.out.println("Fire2");
				} else if (e.getEnergy() > 4) {
					fire(1);
					System.out.println("Fire3");
				} else if (e.getEnergy() > 2) {
					fire(.5);
					System.out.println("Fire4");
				} else if (e.getEnergy() > .4) {
					fire(.1);
					System.out.println("Fire5");
				}
				setTurnRadarRight(getHeading() - getRadarHeading() + e.getBearing());
			}

		} else {


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

			while (!(this.getX() == myPos.getX() && this.getY() == myPos.getY())) {
				goTo(myPos.getX(), myPos.getY());
			}
		}

		// Recebe os membros da sua equipa para imprdir disparos para a sua propria equipa
		else if (e.getMessage() instanceof sendTeam) {
			System.out.println("Got Team");
			sendTeam t = (sendTeam) (e.getMessage());

			String [] team = t.getTeam();
			team[4] = nameL; // adicionar o lider
			System.out.println("TEAM: " + team[0] + " " + team[1] + " "+ team[2] + " " + team[3] + " " +  team[4]);
			ready = true;

		} else if (e.getMessage() instanceof String) {

			enemyName = (String) (e.getMessage());
			System.out.println("Got enemy Name: " + enemyName);
			startScanning = true;


			// Recebem a sua função.
		} else if (e.getMessage() instanceof sendRole) {
			System.out.println("A VER A FUNÇÃO");
			sendRole r = (sendRole) e.getMessage();
			this.role = r.getRole();

			System.out.println("My Role : " + this.role);

			if (role.startsWith("fastBoi")){

				guard = false;
			}
			else{
				guard = true;
			}
			startScanning = true;

		}

		// Os fastBois recebem do seu lider o quadrante onde devem atuar.
		else if (e.getMessage() instanceof sendQuadrant) {

			sendQuadrant q = (sendQuadrant) e.getMessage();
			this.quadrant = q.getQuadrant();

			System.out.println("My quadrant : " + this.quadrant);

			// Vai para o quadrante respetivo
			if (this.quadrant == 1){
				goTo(getBattleFieldWidth() - getBattleFieldWidth()/4, getBattleFieldHeight() - getBattleFieldHeight()/4);
			}
			else if (this.quadrant == 2){
				goTo(getBattleFieldWidth()/4, getBattleFieldHeight() - getBattleFieldHeight()/4);
			}
			else if (this.quadrant == 3){
				goTo(getBattleFieldWidth()/4,  getBattleFieldHeight()/4);
			}
			else if (this.quadrant == 3){
				goTo(getBattleFieldWidth() - getBattleFieldWidth()/4,  getBattleFieldHeight()/4);
			}

			}
	}

	
	public void onHitRobot(HitRobotEvent e){
        Random r = new Random();
        int goRandom = r.nextInt(1);
        //move away randomly
        //he is behind us so set back a bit
        if(e.getBearing() > -90 && e.getBearing() <= 90){
            if(goRandom==1){
                setTurnRight(45);
                back(150);
            }else{
                setTurnLeft(45);
                back(150);
            }
        }else{
            if(goRandom==1){
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
