package NewRobot;

import java.awt.Color;
import java.io.IOException;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

public class TeamLider extends TeamRobot {
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
			
			
			// Initialize moveAmount to the maximum possible for this battlefield.
			moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
			// Initialize peek to false
			peek = false;

			// turnLeft to face a wall.
			// getHeading() % 90 means the remainder of
			// getHeading() divided by 90.
			turnLeft(getHeading() % 90);
			ahead(moveAmount);
			
			// Turn the gun to turn right 90 degrees.
			peek = true;
			turnGunRight(90);
			turnRight(90);

			while (true) {
				peek = true;
				// Move up the wall
				ahead(moveAmount);
				// Don't look now
				peek = false;

				// Turn to the next wall
				turnRight(90);
				
				turnRadarRight(1000);
			}
		}

		/**
		 * onScannedRobot:  What to do when you see another robot
		 */
		public void onScannedRobot(ScannedRobotEvent e) {
			
			// Don't fire on teammates
			if (!isTeammate(e.getName())) {
				
				double enemyBearing = this.getHeading() + e.getBearing();
				// Calculate enemy's position
				double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
				double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
				
			
				try {
					// Send enemy position to teammates
					broadcastMessage(new Enemy(e.getName(),enemyX, enemyY, e.getEnergy(),e.getBearing(), e.getDistance()));
					// Note that scan is called automatically when the robot is moving.
					// By calling it manually here, we make sure we generate another scan event if there's a robot on the next
					// wall, so that we do not start moving up it until it's gone.
					
				} catch (IOException ex) {
					out.println("Unable to send order: ");
					ex.printStackTrace(out); 
				}
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

