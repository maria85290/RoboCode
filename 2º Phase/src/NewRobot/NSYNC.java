package NewRobot;

import java.awt.Color;
import java.io.IOException;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

public class NSYNC extends TeamRobot {
	boolean peek; // Don't turn if there's a robot there
	double moveAmount; // How much to move

		/**
		 * run:  Leader's default behavior
		 */
		public void run() {
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
			} catch (IOException ignored) {}
			

				// Posição central
			while(!(this.getX() == getBattleFieldWidth()/2 && this.getY() == getBattleFieldHeight()/2)) {
				goTo(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2);
			}

			// Ficar virado para baixo.
			turnLeft(180);

			try {
				double x = getX();
				double y = getY();
				double rotation = 30;


				broadcastMessage(new DanceOrder(x,y,rotation));

				//Dance like the win dance
				for (int i = 0; i < 50; i++) {
					turnRight(rotation);
					turnLeft(rotation);
				}


			} catch (IOException ex) {
				out.println("Unable to send order: ");
				ex.printStackTrace(out);
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
			if (e.getBearing() > -90 && e.getBearing() < 90) {
				back(100);
			} // else he's in back of us, so set ahead a bit.
			else {
				ahead(100);
			}
		}
	}

