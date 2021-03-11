package NewRobot;

import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class JustinTimb extends TeamRobot implements Droid {
    private double offsetX;
    private double offsetY;
    private int turns;
    private ArrayList<Position> positions = new ArrayList<>();
    /**
     * run:  Droid's default behavior
     */
    public void run() {

        System.out.println("Droid ready.");
        System.out.println(this.getName());
        offsetX = getBattleFieldWidth() / 3;
        offsetY = getBattleFieldHeight() / 3;
        initializePositions();
        for (int i = 0; i < positions.size(); i++){
            System.out.println(positions.get(i));
        }

    }

    /**
     * onMessageReceived:  What to do when our leader sends a message
     */
    public void onMessageReceived(MessageEvent e) {
        // Fire at a point
        if (e.getMessage() instanceof DanceOrder) {
            for(int i = 0; i < 20; i++){
                doNothing();
            }

            DanceOrder o = (DanceOrder) e.getMessage();

            System.out.println(o.getX() + ' ' + o.getY());

            //get angle do dance
            double danceAngle = o.getRotation();

            Random rand = new Random();

            int pos = rand.nextInt(4);


            goTo(o.getX(), o.getY());

            //rotate robot to the south
            double angleToTarget = Math.atan2(0,this.getY());

            double targetAngle = angleToTarget - getHeadingRadians();

            /* This is a simple method of performing set front as back */
            double turnAngle = Math.atan(Math.tan(targetAngle));
            turnRightRadians(turnAngle);

            //Dance like the win dance
            for (int i = 0; i < 50; i++) {
                turnRight(danceAngle);
                turnLeft(danceAngle);
            }

        } // Set our colors
        else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }

    }

    public double getDestX(int turns){
        if(turns == 1 || turns == 2) {
            return offsetX;
        }
        else {
            return offsetX * 2;
        }
    }

    public double getDestY(int turns){
        if(turns == 1 || turns == 4) {
            return offsetY;
        }
        else {
            return offsetY * 2;
        }
    }

    public Position getDest(){
        if(Math.abs(this.getX() - offsetX) < Math.abs(this.getX() - 2*offsetX) && Math.abs(this.getY() - offsetY) < Math.abs(this.getY() - 2*offsetY)) {
            return new Position(offsetX, offsetY);
        }
        else if (Math.abs(this.getX() - offsetX) < Math.abs(this.getX() - 2*offsetX) && Math.abs(this.getY() - offsetY) > Math.abs(this.getY() - 2*offsetY)) {
            return new Position(offsetX, offsetY * 2);
        }

        else if (Math.abs(this.getX() - offsetX) > Math.abs(this.getX() - 2*offsetX) && Math.abs(this.getY() - offsetY) > Math.abs(this.getY() - 2*offsetY)) {
            return new Position(offsetX * 2, offsetY * 2);
        }
        else {
            return new Position(offsetX * 2, offsetY);
        }
    }

    public void initializePositions(){
        for(int i = 1; i < 5; i++) {
            Position pos = new Position(getDestX(i), getDestY(i));
            positions.add(pos);
        }
    }

    public void goTo(double newX, double newY) {
        double dx = newX - this.getX();
        double dy = newY - this.getY();
        // Calculate angle to face the robot's body to go to the destination
        double theta = Math.toDegrees(Math.atan2(dx, dy));

        // Turn body to the destination angle
        turnRight(Utils.normalRelativeAngleDegrees(theta - getGunHeading()));

        //calculate distance to move and go
        double distance = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        ahead(distance);


    }

    public Position getDestPos(){
        if(turns == 0) {
            return new Position(offsetX, offsetY);
        }
        else if(turns == 1) {
            return new Position(offsetX, offsetY * 2);
        }
        else if (turns == 2) {
            return new Position(offsetX * 2, offsetY * 2);
        }
        else {
            return new Position(offsetX * 2, offsetY);
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        System.out.println("Ups! Colision with another robot! " + e.getName());
        setTurnLeft(10);
        back(100);

    }
}
