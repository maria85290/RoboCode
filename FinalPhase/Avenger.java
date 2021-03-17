package NewRobot.FinalPhase;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.io.IOException;

import NewRobot.PositionEnemy;
import NewRobot.RobotColors;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

//Teammate
public class Avenger extends TeamRobot {

    PositionEnemy p;
    private String nameL;
    public boolean isDead=false;
    public boolean startScanning=false;
    Position myPos;
    int time=0;
    
	public void run (){

        while(!startScanning){
            doNothing();
        }
        
        while (true) {
            turnGunRight(10); // Scans automatically
        }

    }

    public void onScannedRobot(ScannedRobotEvent e) {
        super.onScannedRobot(e);

        if((e.getName().equals(p.getName()))){

            System.out.println("Found enemy whos names is" + e.getName());

            double absoluteBearing = getHeading() + e.getBearing();
            double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

            double angle = Math.toRadians((getHeading() + e.getBearing()) % 360);

            // Calculate the coordinates of the robot
            double scannedX = getX() + Math.sin(angle) * e.getDistance();
            double scannedY = getY() + Math.cos(angle) * e.getDistance();

            turnGunRight(bearingFromGun);         
        }
    }



    public void onMessageReceived(MessageEvent e){
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
        } else if(e.getMessage() instanceof Position){
            System.out.println("Got Leader Position");
            myPos = (Position) (e.getMessage());

            while(!(this.getX() == myPos.getX() && this.getY() == myPos.getY())) {
                goTo(myPos.getX(), myPos.getY());
            }

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


}
