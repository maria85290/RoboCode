package NewRobot;

import javafx.geometry.Pos;
import robocode.Droid;
import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import java.io.IOException;
import java.util.Random;

public class JustinTimb extends TeamRobot implements Droid {
    private double offsetX;
    private double offsetY;
    /**
     * run:  Droid's default behavior
     */
    public void run() {
        offsetX = getBattleFieldWidth() / 3;
        offsetY = getBattleFieldHeight() / 3;


        System.out.println("Droid ready.");

    }

    /**
     * onMessageReceived:  What to do when our leader sends a message
     */
    public void onMessageReceived(MessageEvent e) {
        System.out.println("Message: " + e.getClass().getName());
        // Fire at a point
        if (e.getMessage() instanceof DanceOrder) {
            System.out.println("Dance order");

            DanceOrder o = (DanceOrder) e.getMessage();

            System.out.println(o.getX() + ' ' + o.getY());

            //get angle do dance
            double danceAngle = o.getRotation();


            while(this.getX() != o.getX() && this.getY() != o.getY()){
                goTo(o.getX(), o.getY());
            }

            if (this.getHeading() > 180) {
                this.turnRight(360 - this.getHeading() - danceAngle/2);
            }
            else {
                this.turnLeft(this.getHeading() + danceAngle/2);
            }


            while(this.getX() != o.getX() && this.getY() != o.getY()){
                goTo(o.getX(), o.getY());
            }

            for(int i = 0; i < 20; i++){
                doNothing();
            }

            DanceOrder order = new DanceOrder(this.getX(), this.getY(), o.getRotation());
            try {
                //inform team lider the this robot is ready to dance
                sendMessage("NewRobot.NSYNC", order);

            } catch (IOException ioException) {
                ioException.printStackTrace();
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

        else if (e.getMessage() instanceof Double || e.getMessage() instanceof Integer) {
            System.out.println("I'm going to dance!");
            double rotation = (Double) e.getMessage();
            dance(rotation);

            goTo(getNewPos().getX(), getNewPos().getY());
            turnRight(this.getHeading());
            try {
                sendMessage("NewRobot.NSYNC","ready");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

        }

    }

    public void dance(double danceAngle) {
        //Dance like the win dance
        for (int i = 0; i < 10; i++) {
            turnRight(danceAngle);
            turnLeft(danceAngle);
        }
    }

    public Position getNewPos(){

        double x = Math.round(this.getX());
        double y = Math.round(this.getY());
        double roundedOffsetX = Math.round(offsetX);
        double roundedOffsetY = Math.round(offsetY);
        System.out.println("X: " + x);
        System.out.println("Y: " + y);
        System.out.println("offsetX: " + roundedOffsetX);
        System.out.println("offsetY: " + roundedOffsetY);
        System.out.println("offsetX2: " + roundedOffsetX * 2);
        System.out.println("offsetY2: " + roundedOffsetY * 2);

        Position newPos = new Position();

        Position pos1 = new Position(roundedOffsetX, roundedOffsetY);
        Position pos2 = new Position(getBattleFieldWidth() / 6, getBattleFieldHeight() / 2);
        Position pos3 = new Position(roundedOffsetX, roundedOffsetY * 2);
        Position pos4 = new Position(getBattleFieldWidth() / 2, getBattleFieldHeight() * 5 / 6);
        Position pos5 = new Position(roundedOffsetX * 2, roundedOffsetY * 2);
        Position pos6 = new Position(getBattleFieldWidth() * 5 / 6, getBattleFieldHeight() * 2);
        Position pos7 = new Position(roundedOffsetX * 2, roundedOffsetY);
        Position pos78= new Position(getBattleFieldWidth() / 2, getBattleFieldHeight() / 6);

        if (x == roundedOffsetX && y == roundedOffsetY) {
            System.out.println("First pos");
            newPos.setX(this.getX());
            newPos.setY(getBattleFieldHeight()/2);
        }
        else if (x == roundedOffsetX && y == roundedOffsetY * 2){
            System.out.println("Second pos");
            newPos.setX(getBattleFieldWidth()/2);
            newPos.setY(5*getBattleFieldHeight()/6);
        }

        else if (x >= roundedOffsetX-1 * 2 && y == roundedOffsetY * 2){
            System.out.println("Third pos");
            newPos.setX(this.getX());
            newPos.setY(getBattleFieldHeight()/2);
        }

        else {
            System.out.println("Last pos");
            newPos.setX(getBattleFieldWidth() / 2);
            newPos.setY(getBattleFieldHeight() / 6);
        }
        return newPos;
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

        if(this.getX() != newX && this.getY() != newY){
            turnLeft(40);
            back(20);
        }
    }

    public void onHitRobot(HitRobotEvent e) {
        //System.out.println("Ups! Colision with another robot! " + e.getName());
        turnLeft(10);
        back(100);

    }
}
