package NewRobot.FinalPhase;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import NewRobot.RobotColors;
import robocode.DeathEvent;
import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

//TeamLeader
public class CapAmerica extends TeamRobot {
	private String[] teammates = new String[4];
	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move

	double energy = 200.0;
	private int num_team = 0;
	private ArrayList<String> teamsArray;
	private ArrayList<Position> cantos = new ArrayList<>();

	Position initial;
	Position guardCanto1;
	Position guardCanto2;

	boolean wallsMode = false;

	public void run() {
		cantos.add(new Position(18, 18));
		cantos.add(new Position(18, getBattleFieldHeight() - 18));
		cantos.add(new Position((getBattleFieldWidth() - 18), 18));
		cantos.add(new Position((getBattleFieldWidth() - 18), getBattleFieldHeight() - 18));

		RobotColors c = new RobotColors();

		c.bodyColor = Color.red;
		c.gunColor = Color.blue;
		c.radarColor = Color.white;
		c.scanColor = Color.orange;
		c.bulletColor = Color.red;

		// Set the color of this robot containing the RobotColors
		setBodyColor(c.bodyColor);
		setGunColor(c.gunColor);
		setRadarColor(c.radarColor);
		setScanColor(c.scanColor);
		setBulletColor(c.bulletColor);

		try {
			// Send RobotColors object to our entire team
			broadcastMessage(c);
			broadcastMessage(new SendName(this.getName()));
		} catch (IOException ignored) {}

		
		  while (num_team != 4) { doNothing();}
		 

		teamsArray = new ArrayList<>(Arrays.asList(teammates));

		Position atual = new Position(getX(), getY());
		// Escolher canto mais próximo e deslocar-se para lá
		int j = findClosestCorner(atual);
		initial = new Position(cantos.get(j).getX(), cantos.get(j).getY());
		goTo(initial.getX(), initial.getY());

		// mandar posições consoante sitio do lider
		// informar teammates mais próximos da sua posição (nesta primeira fase apenas enviar a 2)
		bodyGuardPositions(initial.getX(), initial.getY());

		try {
			System.out.print("Mandei ao " + teammates[0] + " o seguinte: " + guardCanto1.getX() + " " + guardCanto1.getY());
			sendMessage(teammates[0], guardCanto1);
			sendMessage(teammates[1], guardCanto2);
		} catch (IOException ignored) {}

		// scan por inimigos
		turnRight(360);

		while (!wallsMode) {
			turnRight(10);
			if (this.getEnergy() < 90 || teammates.length <= 2) {
				System.out.print("left alone");
				wallsMode = true;
			}
		}

		// Initialize moveAmount to the maximum possible for this battlefield.
		moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
		// Initialize peek to false
		peek = false;

		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of
		// getHeading() divided by 90.
		turnLeft(getHeading( ) % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnGunRight(90);
		turnRight(90);

		while (true) {
			// Look before we turn when ahead() completes.
			peek = true;
			// Move up the wall
			ahead(moveAmount);
			// Don't look now
			peek = false;
			// Turn to the next wall
			turnRight(90);
		}
	}

	/**
	 * onScannedRobot: Ver se é inimigo, enviar nome do inimigo
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);
		if(!teamsArray.contains(e.getName())) {
			try {
				// Send enemy name to teammates
				broadcastMessage(e.getName());
			} catch (IOException ex) {
				out.println("Unable to send order: ");
				ex.printStackTrace(out);
			}
		}
		if (wallsMode && !teamsArray.contains(e.getName())) {
			//TODO: Only fire if somewhat close 
			fire(2);
			// Note that scan is called automatically when the robot is moving.
			// By calling it manually here, we make sure we generate another scan event if
			// there's a robot on the next
			// wall, so that we do not start moving up it until it's gone.
			if (peek) {
				scan();
			}
		}
	}

	/**
	 * onHitRobot: Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		// If he's in front of us, set back up a bit.
		if (wallsMode) {
			if (e.getBearing() > -90 && e.getBearing() < 90) {
				back(100);
			} // else he's in back of us, so set ahead a bit.
			else {
				ahead(100);
			}
		}
		else {
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
	}

	// se capAmerica morrer passar liderança a outro teammate, de preferencia o que
	// tiver maior energia
	public void onDeath(DeathEvent event) {
		System.out.print("To be Implemented");
	}

	// Descobrir teammates
	public void onMessageReceived(MessageEvent e) {

		if (e.getMessage() instanceof SendName) {
			System.out.println("[MSG] Recebi nome de um TeamMate. ");
			SendName n = (SendName) e.getMessage();
			teammates[num_team] = n.getName();
			System.out.println(teammates[num_team]);
			num_team++;
		}
	}

	private void bodyGuardPositions(double x, double y) {

		// Leader (18,18)
		if (x == 18 && y == 18) {
			guardCanto1 = new Position(40, 90);
			guardCanto2 = new Position(90, 35);
		}
		// Leader (18,getBattleFieldHeight()-18), canto superior esquerdo
		else if (x == 18 && y == (getBattleFieldHeight() - 18)) {
			guardCanto1 = new Position(40, getBattleFieldHeight() - 90);
			guardCanto2 = new Position(90, getBattleFieldHeight() - 35);
		}
		// Leader (getBattleFieldWidth() - 18,18), canto inferior direito
		else if (x == (getBattleFieldWidth() - 18) && y == 18) {
			guardCanto1 = new Position(getBattleFieldWidth() - 90, 35);
			guardCanto2 = new Position(getBattleFieldWidth() - 40, 90);
		}
		// Leader ((getBattleFieldWidth() - 18), getBattleFieldHeight()- 18)), canto
		// superior direito
		else if (x == (getBattleFieldWidth() - 18) && y == (getBattleFieldHeight() - 18)) {
			guardCanto1 = new Position(getBattleFieldWidth() - 90, getBattleFieldHeight() - 35);
			guardCanto2 = new Position(getBattleFieldWidth() - 40, getBattleFieldHeight() - 90);
		}

	}

	private int findClosestCorner(Position atual) {
		double minDist = getDistance(cantos.get(0), atual);
		int j = 0;
		for (int i = 0; i < cantos.size(); i++) {
			if (getDistance(cantos.get(i), atual) < minDist) {
				minDist = getDistance(cantos.get(i), atual);
				j = i;
			}
		}
		return j;
	}

	// Calculate Distance
	public double getDistance(Position canto, Position atual) {
		return Math.sqrt(Math.pow(canto.getX() - atual.getX(), 2) + Math.pow(canto.getY() - atual.getY(), 2));
	}

	// Função de deslocação
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
