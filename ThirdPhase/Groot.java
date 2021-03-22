package ThirdPhase;

import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("Duplicates")

public class Groot extends TeamRobot implements Droid {

	private String nameL;
	private ArrayList<String> teamsArray = new ArrayList<>();

	public void run() {

		//Set Robot Colors
		this.setColors(Color.BLUE,Color.RED,Color.RED,Color.WHITE,Color.ORANGE);

		while(true) {

			fire(3);

			Random r = new Random();
			int option = r.nextInt(2);  // To defined the rout is going to take.
			if(option==0){
				setTurnRight(270);
				ahead(500);
				fire(3);

			}else if (option == 1){
				setTurnLeft(180);
				ahead(400);
				//fire(3);
				setTurnRight(180);
				ahead(400);
				fire(3);
			}
			else {
				setTurnLeft(180);
				//fire(3);
				setTurnRight(180);
				back(400);
				fire(3);
			}
		}
	}

	public void onMessageReceived(MessageEvent e) {

		if (e.getMessage() instanceof Enemy) {

			System.out.println("[MSG] Got enemys name");

			Enemy en = (Enemy) e.getMessage();
			double dx = en.getX() - this.getX();
			double dy = en.getY() - this.getY();
			double alfa = Math.toDegrees(Math.atan2(dx, dy));

			turnRight(Utils.normalRelativeAngleDegrees(alfa - getGunHeading()));
			fire(3);
			ahead(100);

		}
		else if (e.getMessage() instanceof SendName) {

			System.out.println("[MSG] Got leaders name. ");
			SendName n = (SendName) e.getMessage();

			this.nameL = n.getName();

			// Respond with my own name
			try {
				sendMessage(this.nameL, new SendName(getName()));
			} catch (IOException a) {
				a.printStackTrace();
			}

		} else if(e.getMessage() instanceof ArrayList){

			teamsArray = (ArrayList) e.getMessage();
			teamsArray.add(nameL);
		}

	}

	public void onHitByBullet(HitByBulletEvent e) {

		Random r = new Random();
		int option = r.nextInt(2);
		if(option==1){
			setTurnRight(60);
			ahead(100);
		}else if (option == 0){
			setTurnLeft(60);
			ahead(100);
		}
		else {
			setTurnLeft(60);
			back(100);
		}
	}

	public void onHitWall(HitWallEvent e) {

		Random r = new Random();
		int strategy = r.nextInt(1);
		if(e.getBearing() > -90 && e.getBearing() <= 90){
			if(strategy==1){
				setTurnRight(45);
				back(100);
			}else{
				setTurnLeft(45);
				back(100);
			}

		}else{
			if(strategy==1){
				setTurnRight(45);
				ahead(100);
			}else{
				setTurnLeft(45);
				ahead(100);
			}
		}
	}

	public void onHitRobot(HitRobotEvent e) {

		//If on hit robot is not an enemy
		if(!teamsArray.contains(e.getName())){

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

}