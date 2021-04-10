package NewRobot;

import java.awt.Color;
import java.io.IOException;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class NSYNC extends TeamRobot {
	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move
	private double offsetX;
	private double offsetY;
	private int msgReceived = 0;
	private double rotationV = 30;
	private boolean pos1 = false;
	private boolean pos2 = false;
	private boolean pos3 = false;
	private boolean pos4 = false;

	private  String[] teammates= new String[4];   // array com o nome dos membros da equipa
	private int num_team = 0;  // numero de robos que ja enviaram o seu nome ao lider


	/**
	 * run:  Leader's default behavior
	 */
	public void run() {
		offsetX = getBattleFieldWidth() / 3;
		offsetY = getBattleFieldHeight() / 3;
		// Prepare RobotColors object
		RobotColors c = new RobotColors();

		c.bodyColor = Color.yellow;
		c.gunColor = Color.white;
		c.radarColor = Color.red;
		c.scanColor = Color.blue;
		c.bulletColor = Color.green;

		// Set the color of this robot containing the RobotColors
		setBodyColor(c.bodyColor);
		setGunColor(c.gunColor);
		setRadarColor(c.radarColor);
		setScanColor(c.scanColor);
		setBulletColor(c.bulletColor);

		try {
			// Send RobotColors object to our entire team
			broadcastMessage(c);

			// Envia o seu nome aos robôs.
			broadcastMessage(new SendName(this.getName()));
		} catch (IOException ignored) {}

		// Espera ate receber o nome de todos os elemntos da equipa
		while (num_team!=4){
			doNothing();
		}

		// Posição central
		while(!(this.getX() == getBattleFieldWidth()/2 && this.getY() == getBattleFieldHeight()/2)) {
			goTo(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2);
		}

		double x = getX();
		double y = getY();
		double rotation = 30;

		try {


			if (this.getHeading() > 180) {
				this.turnRight(360 - this.getHeading() - rotation/2);
			}
			else {
				this.turnLeft(this.getHeading() + rotation/2);
			}

			int pos = 0;
			for (String member : teammates) {
				if (pos == 0) {
					sendMessage(member, new DanceOrder(offsetX, offsetY,rotation));
				}
				else if (pos == 1) {
					sendMessage(member, new DanceOrder(offsetX, offsetY * 2,rotation));
				}
				else if (pos == 2) {
					sendMessage(member, new DanceOrder(offsetX * 2, offsetY * 2,rotation));
				}
				else {
					sendMessage(member, new DanceOrder(offsetX * 2, offsetY,rotation));
				}
				pos++;
			}
			//wait for other the droids to get in their correct positions
			for (int i = 0; i < 80; i++) {
				doNothing();
			}

			while(!(this.getX() == getBattleFieldWidth()/2 && this.getY() == getBattleFieldHeight()/2)) {
				goTo(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2);
			}

			if (this.getHeading() > 180) {
				this.turnRight(360 - this.getHeading() - rotation/2);
			}
			else {
				this.turnLeft(this.getHeading() + rotation/2);
			}



		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}

	}

	/**
	 * onMessageReceived:  What to do when our leader sends a message
	 */
	public void onMessageReceived(MessageEvent e) {
		// if robot has danced already, it is going to the next position
		if (e.getMessage() instanceof DanceOrder) {
			DanceOrder o = (DanceOrder) e.getMessage();
			msgReceived++;
			System.out.println(e.getSender() + " is ready! " + msgReceived);

			if (msgReceived == 4) {
				msgReceived = 0;
				for (String member : teammates) {
					try {
						System.out.println("Sending to " + member);
						sendMessage(member, o.getRotation());
					} catch (IOException ioException) {
						System.out.println("Here " + member);
						ioException.printStackTrace();
					}
				}
				dance(o.getRotation());

			}


		}
		else if (e.getMessage().equals("ready")) {
			msgReceived++;
			System.out.println(e.getSender() + " is ready! " + msgReceived);

			if (msgReceived == 4) {
				msgReceived = 0;
				for (String member : teammates) {
					try {
						System.out.println("Sending to " + member);
						sendMessage(member, rotationV);
					} catch (IOException ioException) {
						System.out.println("Here " + member);
						ioException.printStackTrace();
					}
				}
				dance(rotationV);

			}
		}

		// Recebe os nomes dos membros da equipa.
		else  if (e.getMessage() instanceof SendName) {
			SendName n = (SendName) e.getMessage();
			System.out.println("[MSG] Recebi nome de um TeamMate: " + n.getName());
			teammates[num_team]=n.getName();
			System.out.println(teammates[num_team]);
			num_team ++;
		}
	}

	private void goTo(double x, double y){

		x -= getX();
		y -= getY();

		double angleToTarget = Math.atan2(x, y);

		double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());


		double distance = Math.hypot(x, y);

		/* This is a simple method of performing set front as back */
		double turnAngle = Math.atan(Math.tan(targetAngle));
		turnRightRadians(turnAngle);


		if(targetAngle == turnAngle) {
			ahead(distance);
		} else {
			back(distance);
		}
	}

	public void dance(double rotation){
		//Dance like the win dance
		for (int i = 0; i < 10; i++) {
			turnRight(rotation);
			turnLeft(rotation);
		}
	}



	/**
	 * onHitByBullet:  Turn perpendicular to bullet path
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(90 - e.getBearing());
	}

	/**
	 * onHitRobot:  Move away a bit.
	 */
	public void onHitRobot(HitRobotEvent e) {
		// If he's in front of us, set back up a bit.
		turnLeft(10);
		if (e.getBearing() > -90 && e.getBearing() < 90) {
			back(100);
		} // else he's in back of us, so set ahead a bit.
		else {
			ahead(100);
		}
	}
}

