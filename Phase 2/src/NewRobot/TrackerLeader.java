package NewRobot;

import robocode.*;
import robocode.util.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static robocode.util.Utils.normalRelativeAngleDegrees;

@SuppressWarnings("Duplicates")

public class TrackerLeader extends TeamRobot {

    PositionEnemy p;
    double energy = 200.0;
    private int num_team = 0;
    private  String[] teammates= new String[4];
    private ArrayList<String> teamsArray;
    int j=0;
    private boolean finishedProbing = false;
    String nome;
    double bearing;
    double heading;
    double distance;


    public void run(){

        PositionTracker pos1 = new PositionTracker(((getBattleFieldWidth()/2) - 150),getBattleFieldHeight() - 50, 20); //60

        PositionTracker pos2 = new PositionTracker(((getBattleFieldWidth()/2) - 70), getBattleFieldHeight()-100,60); //130
        PositionTracker pos3 = new PositionTracker(((getBattleFieldWidth()/2) + 70), getBattleFieldHeight()-100, 100); //190

        PositionTracker pos4 = new PositionTracker(((getBattleFieldWidth()/2) +150), getBattleFieldHeight() -50, 130); //250


        /*
        Prepare robot colors
        */
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
            broadcastMessage(new SendName(this.getName()));
        } catch (IOException ignored) {}

/*        try {
            sendMessage(teammates[0],p1);
            sendMessage(teammates[1],p2);
            sendMessage(teammates[2],p3);
            sendMessage(teammates[3],p4);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        while(!(this.getX() == getBattleFieldWidth()/2 && this.getY() == getBattleFieldHeight() -18)) {
            goTo(getBattleFieldWidth()/2, getBattleFieldHeight() -18);
        }

        while (num_team!=4){
            doNothing();
        }

        try {
            sendMessage(teammates[0],pos1);

            sendMessage(teammates[1],pos2);

            sendMessage(teammates[2],pos3);

            sendMessage(teammates[3],pos4);

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Scaning the battlefield
        turnRight(360);
        finishedProbing = true;

        while(true){
            turnRight(10);
        }



    }


    public void onScannedRobot(ScannedRobotEvent e) {
        super.onScannedRobot(e);

        teamsArray = new ArrayList<>(Arrays.asList(teammates));

        if(e.getEnergy() < energy && (!teamsArray.contains(e.getName()))){
            energy=e.getEnergy();
            nome = e.getName();
            heading = e.getHeading();
            bearing=e.getBearing();
            distance = e.getDistance();
        }

        if(finishedProbing){

            double angle = Math.toRadians((heading + bearing) % 360);

            // Calculate the coordinates of the robot
            double scannedX = getX() + Math.sin(angle) * distance;
            double scannedY = getY() + Math.cos(angle) * distance;

            System.out.println("Atacar o " + e.getName() + " na posicao " + "(" + scannedX + "," + scannedY + ")");

            p = new PositionEnemy(scannedX,scannedY, energy,nome);

            for(int i=0; i <4 ; i++){
                try {
                    sendMessage(teammates[i],p);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void onMessageReceived(MessageEvent e){

        if (e.getMessage() instanceof SendName) {

            System.out.println("[MSG] Recebi nome de um TeamMate. ");
            SendName n = (SendName) e.getMessage();
            teammates[num_team] = n.getName();
            System.out.println(teammates[num_team]);
            num_team++;

        }
    }

    public void onRobotDeath(RobotDeathEvent e) {
        if(e.getName().equals(p.getName())){
            energy=200.0;
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        setTurnLeft(10);
        back(40);
    }

    public void onHitRobot(HitRobotEvent e) {
        Random r = new Random();
        int rand = r.nextInt(2);
        if(rand==1){
            setTurnLeft(10);
            back(15);
        }
        else {back(20); setTurnRight(15); }
    }
/*
    public void onHitWall(HitWallEvent e) {
        turnLeft(180);
    }*/

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
