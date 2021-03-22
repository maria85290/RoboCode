package NewRobot;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import robocode.*;
import robocode.util.Utils;

import static robocode.util.Utils.normalRelativeAngleDegrees;

@SuppressWarnings("Duplicates")

public class CaptainAmerica extends TeamRobot{

    private ArrayList<String> teamsArray;
    private ArrayList<Position> cantos = new ArrayList<>();
    private ArrayList<String> enemies = new ArrayList<String>();

    private String[] teammates = new String[4];

    //to activate when it is in a corner (initial pos)
    private boolean start = false;
    private boolean peek;
    private boolean wallsMode = false;
    private boolean enemiesReady = false;
    private boolean isProbing = true;
    private boolean deaths = false;

    private double moveAmount;
    private int num_team = 0;
    private int n_enemy = 0;

    Position initial;
    Position guardCanto1;
    Position guardCanto2;



    public void run(){

        // Calculate corner positions for Leader
        cantos.add(new Position(18, 18));
        cantos.add(new Position(18, getBattleFieldHeight() - 18));
        cantos.add(new Position((getBattleFieldWidth() - 18), 18));
        cantos.add(new Position((getBattleFieldWidth() - 18), getBattleFieldHeight() - 18));

        // Set Robot Colors
        this.setColors(Color.BLUE,Color.RED,Color.RED,Color.WHITE,Color.ORANGE);

        try {
            // Send Leader's name to every robot on the battlefield
            broadcastMessage(new SendName(this.getName()));
        } catch (IOException ignored) {}

        // While the team isnt complete , wait
        while (num_team != 4){
            doNothing();
        }

        teamsArray = new ArrayList<>(Arrays.asList(teammates));

        // Leaders current position
        Position atual = new Position(getX(), getY());

        // Choose closest corner
        int j = findClosestCorner(atual);

        // Set initial position
        initial = new Position(cantos.get(j).getX(), cantos.get(j).getY());

        while (start == false) {
            goTo(initial.getX(), initial.getY());
        }

        // Set initial positions for the bodyguards
        bodyGuardPositions(initial.getX(), initial.getY());

        ArrayList<Integer> teamAvengIndex = new ArrayList<>();

        //Get indexes for the avengers teammates
        for(int y=0; y < 4; y ++){
            if(teammates[y].startsWith("NewRobot.Avenger")){
                teamAvengIndex.add(y);
            }
        }

        try {
            //Broadcast message of the teammates list
            broadcastMessage(teamsArray);

            //Send avengers positio for bodyguards mode
            sendMessage(teammates[teamAvengIndex.get(0)], guardCanto1);
            sendMessage(teammates[teamAvengIndex.get(1)], guardCanto2);
            System.out.println("Sent pos " +"(" + guardCanto1.getX() + "," + guardCanto1.getY() + ")" + "to " + teammates[teamAvengIndex.get(0)]);
            System.out.println("Sent pos " +"(" + guardCanto2.getX() + "," + guardCanto2.getY() + ")" + "to " + teammates[teamAvengIndex.get(1)]);

        } catch (IOException e) {
            e.printStackTrace();
        }


        // scan por inimigos
        turnRight(360);

        // Finished probing (360 loop looking for enemies)
        isProbing = false;
        System.out.println(enemies);

        //while the list of enemies != 5
        while(!enemiesReady){
            scan();
        }

        // Send respetive enemy name to teammate
        try {
            for(int i=0; i<4 ; i++){
                sendMessage(teamsArray.get(i),enemies.get(i));
                System.out.println("Send enemy name " + enemies.get(i) + " to teammate" + teamsArray.get(i) );
            }
        } catch (IOException ignored) {}

        //While protected (not in wallsMode)
        while (!wallsMode) {

            turnRight(10);

            // If energy lower than 90 or one of the Avengers death
            if ( this.getEnergy() < 90 || deaths == true) {

                System.out.print("Went rogue.");
                wallsMode = true;

                try {
                    //Send boolean to a Avengers informing i'm in wallsMode
                    sendMessage(teammates[teamAvengIndex.get(0)], true);
                    sendMessage(teammates[teamAvengIndex.get(1)], true);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Starting WallsMode

        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());

        peek = false;

        turnLeft(getHeading( ) % 90);

        ahead(moveAmount);

        peek = true;

        turnGunRight(90);
        turnRight(90);

        while (true) {

            // Look before we turn when ahead() completes.
            peek = true;
            // Move up the wall
            ahead(moveAmount);
            // Don't look now
            peek = false;
            // Turn to the next wall
            turnRight(90);
        }


    }


    public void onScannedRobot(ScannedRobotEvent e) {
        super.onScannedRobot(e);

        // If the enemy its not an teammate and the enemy isnt in the enemies list , add keep searching for enemies
        if(!teamsArray.contains(e.getName()) && !enemies.contains(e.getName()) && isProbing) {
            enemies.add(e.getName());
            n_enemy++;
            //When team complete ...
            if(n_enemy == 5){
                enemiesReady=true;
            }
        }

        // Enter wallsMode and the scanned robot isnt a teammate
        if (wallsMode && !teamsArray.contains(e.getName())) {

            //get the distance of the scanned robot
            double distance = e.getDistance();

            //Managing power according to distance to enemy
            if(distance > 800)
                fire(5);
            else if(distance > 600 && distance <= 800)
                fire(4);
            else if(distance > 400 && distance <= 600)
                fire(3);
            else if(distance > 200 && distance <= 400)
                fire(2);
            else if(distance < 200)
                fire(1);

            if (peek) {
                scan();
            }
        }

        // Send enemys positions to Droids
        if(!teamsArray.contains(e.getName())){

            double enemyBearing = this.getHeading() + e.getBearing();
            double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
            double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));
            try {
                broadcastMessage(new Enemy(e.getName(), enemyX, enemyY));
            } catch (IOException ex) {
                ex.printStackTrace(out);
            }
        }
    }


    public void onMessageReceived(MessageEvent e) {

        if (e.getMessage() instanceof SendName) {

            System.out.println("[MSG] Got teammate's name. ");
            SendName n = (SendName) e.getMessage();
            teammates[num_team] = n.getName();
            num_team++;

        }
    }

    public void onRobotDeath(RobotDeathEvent e){

        //On Avengers death
        if(e.getName().startsWith("NewRobot.Avenger")){

            System.out.println("Avenger died.");

            //Leader turns ON wallsMode.
            wallsMode=true;

            //Remove teammate from enemies list.
            for (int i = 0; i < teammates.length; i++)
            {
                if (teammates[i].equals(e.getName()))
                {
                    teammates[i] = "None";
                    deaths = true;
                    break;
                }
            }
        }
    }


    public void onHitByBullet(HitByBulletEvent e) {

        double absoluteBearing = getHeading() + e.getBearing();
        double bearingFromGun = normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        if(!teamsArray.contains(e.getName())){
            turnGunRight(bearingFromGun);
            fire(2);
        }

    }

    public void onHitRobot(HitRobotEvent e) {

        // If he's in front of us, set back up a bit.
        if (wallsMode) {
            if (e.getBearing() > -90 && e.getBearing() < 90) {
                back(100);
            } // else he's in back of us, so set ahead a bit.
            else {
                ahead(100);
            }
        }
        else {
            Random r = new Random();
            int goRandom = r.nextInt(1);
            //move away randomly
            //he is behind us so set back a bit
            if(e.getBearing() > -90 && e.getBearing() <= 90){
                if(goRandom==1){
                    setTurnRight(45);
                    back(150);
                }else{
                    setTurnLeft(45);
                    back(150);
                }
            }else{
                if(goRandom==1){
                    setTurnRight(45);
                    ahead(150);
                }else{
                    setTurnLeft(45);
                    ahead(150);
                }
            }
        }
    }

    private void bodyGuardPositions(double x, double y) {

        // Leader (18,18)
        if (x == 18 && y == 18) {
            guardCanto1 = new Position(40, 90);
            guardCanto2 = new Position(90, 35);
        }
        // Leader (18,getBattleFieldHeight()-18), upper left corner
        else if (x == 18 && y == (getBattleFieldHeight() - 18)) {
            guardCanto1 = new Position(40, getBattleFieldHeight() - 90);
            guardCanto2 = new Position(90, getBattleFieldHeight() - 35);
        }
        // Leader (getBattleFieldWidth() - 18,18), lower right corner
        else if (x == (getBattleFieldWidth() - 18) && y == 18) {
            guardCanto1 = new Position(getBattleFieldWidth() - 90, 35);
            guardCanto2 = new Position(getBattleFieldWidth() - 40, 90);
        }
        // Leader ((getBattleFieldWidth() - 18), getBattleFieldHeight()- 18)), upper right corner
        else if (x == (getBattleFieldWidth() - 18) && y == (getBattleFieldHeight() - 18)) {
            guardCanto1 = new Position(getBattleFieldWidth() - 90, getBattleFieldHeight() - 35);
            guardCanto2 = new Position(getBattleFieldWidth() - 40, getBattleFieldHeight() - 90);
        }

    }

    private int findClosestCorner(Position atual) {
        double minDist = getDistance(cantos.get(0), atual);
        int j = 0;
        for (int i = 0; i < cantos.size(); i++) {
            if (getDistance(cantos.get(i), atual) < minDist) {
                minDist = getDistance(cantos.get(i), atual);
                j = i;
            }
        }
        return j;
    }

    // Calculate Distance
    public double getDistance(Position canto, Position atual) {
        return Math.sqrt(Math.pow(canto.getX() - atual.getX(), 2) + Math.pow(canto.getY() - atual.getY(), 2));
    }

    // Movement method
    private void goTo(double x, double y) {
        double initialX = x;
        double initialY = y;

        x -= getX();
        y -= getY();
        double angleToTarget = Math.atan2(x, y);

        double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());

        double distance = Math.hypot(x, y);

        /* This is a simple method of performing set front as back */
        double turnAngle = Math.atan(Math.tan(targetAngle));
        turnRightRadians(turnAngle);

        if (targetAngle == turnAngle) {
            ahead(distance);
        } else {
            back(distance);
        }
        System.out.println("FLAG " + start + " " + x + ' ' + getX() + " " + y + ' ' + getY());
        if (getX() >= initialX - 5 && getX() <= initialX + 5 && getY() >= initialY - 5 && getY() <= initialY + 5){
            start = true;
        }
    }

}
