package NewRobot;


import robocode.*;
import robocode.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static robocode.util.Utils.normalRelativeAngleDegrees;

@SuppressWarnings("Duplicates")

public class TrackerSquad extends TeamRobot{

    PositionEnemy p;
    private String nameL;
    public boolean isDead=false;
    public boolean startScanning=false;
    PositionTracker myPos;
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

            System.out.println("MEio:" + getBattleFieldWidth()/2);
            System.out.println("X:" + scannedX);


            //Esqueda
            if(scannedX <= (getBattleFieldWidth()/2) && getX() <= (getBattleFieldWidth()/2)){
                System.out.println("in");
                if (e.getEnergy() > 16 ) {
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
            }
            //direta
            else if(scannedX > (getBattleFieldWidth()/2) && getX() > (getBattleFieldWidth()/2)){
                System.out.println("in");
                if (e.getEnergy() > 16 ) {
                    System.out.println("Fire1");
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
            }

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

        } else  if (e.getMessage() instanceof SendName) {

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
        } else if (e.getMessage() instanceof PositionEnemy){

            System.out.println(" Got enemy's position!");
            p = (PositionEnemy) (e.getMessage());

            startScanning = true;
            System.out.println("Nome do inimigo:" + p.getName());

        } else if(e.getMessage() instanceof PositionTracker){
            System.out.println(" Got my position!");
            myPos = (PositionTracker) (e.getMessage());
            time = myPos.getTime();

            for(int i=0; i< time; i++){
                doNothing();
            }

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


    public void onDeath(DeathEvent event){
        isDead = true;
    }

/*    public void onHitByBullet(HitByBulletEvent e) {
        setTurnLeft(10);
        back(40);
    }*/

    public void onHitRobot(HitRobotEvent e) {
        back(70);
        ahead(70);

    }
/*
    public void onHitWall(HitWallEvent e) {
        turnLeft(180);\

              if (rand == 1) {
            setTurnLeft(10);
            ahead(40);
            setTurnLeft(10);
            ahead(40);
            setTurnLeft(10);
            ahead(40);

                   Random r = new Random();
        int rand = r.nextInt(2);

            setTurnRight(40);
            ahead(40);
            setTurnRight(40);
            ahead(40);
            setTurnRight(40);
            ahead(40);
        } else {
    }*/
    }
