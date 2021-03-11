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
	private boolean pos1 = false;
	private boolean pos2 = false;
	private boolean pos3 = false;
	private boolean pos4 = false;

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
			} catch (IOException ignored) {}
			

				// Posição central
			while(!(this.getX() == getBattleFieldWidth()/2 && this.getY() == getBattleFieldHeight()/2)) {
				goTo(getBattleFieldWidth() / 2, getBattleFieldHeight() / 2);
			}

			//rotate robot to the south
			double angleToTarget = Math.atan2(0,this.getY());

			double targetAngle = angleToTarget - getHeadingRadians();

			/* This is a simple method of performing set front as back */
			double turnAngle = Math.atan(Math.tan(targetAngle));
			turnRightRadians(turnAngle);

			try {
				double x = getX();
				double y = getY();
				double rotation = 30;

				String[] teammates = getTeammates();
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
				for (int i = 0; i < 30; i++) {
					doNothing();
				}

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

