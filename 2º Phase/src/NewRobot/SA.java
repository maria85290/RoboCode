package NewRobot;

import robocode.HitByBulletEvent;
import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class SA extends TeamRobot {
    private ArrayList<Position> positions = new ArrayList<>();
    private int destX;
    private int destY;
    private int x;
    private int y;
    private  String lastRobot;
    private boolean ready = false;
    private int num_ready = 0;
    private  String[] teammates= new String[4];
    private int num_team = 0;
    /**
     * run:  Leader's default behavior
     */
    public void run() {

        System.out.println(this.getName() + " (1)");
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


        // Povoar o array posição
        Position pos = new Position(100, 100);
        Position pos1 = new Position(300, 100);
        Position pos2 = new Position(300, 280);
        Position pos3 = new Position(100, 280);
        Position pos4 = new Position(100, 450);
        Position pos5 = new Position(300, 450);

        Position pos6 = new Position(450, 100);
        Position pos7 = new Position(550, 450);
        Position pos8 = new Position(650, 100);

        // Adiciona posições pela ordem inversa.
        positions.add(0, pos8);
        positions.add(0, pos7);
        positions.add(0, pos6);
        positions.add(0, pos5);
        positions.add(0, pos4);
        positions.add(0, pos3);
        positions.add(0, pos2);
        positions.add(0, pos1);
        positions.add(0, pos);

        System.out.println(positions);


        // Posições de inicio dos 4 robos:
        Position robot1 = new Position(90, 18);
        Position robot2 = new Position(18, 90 );
        Position robot3 = new Position(190, 18);
        Position robot4 = new Position(18, 190);

        // Tempos de espera
        int t1 = 60;
        int t2 = 130;
        int t3 = 190;
        int t4 = 250;


        try {
            // Send RobotColors object to our entire team
            broadcastMessage(c);
            broadcastMessage(new sendName(this.getName()));
        } catch (IOException ignored) {
        }

        // Espera ate receber o nome de todos os elemntos da equipa
        while (num_team!=4){
            doNothing();
        }


        try {
            sendMessage(teammates[0], new Positions(positions, t1, robot1, new Position(110,18)));
            System.out.println("Coordenadas enviadas ao" + teammates[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sendMessage(teammates[1], new Positions(positions, t2, robot2, new Position(170,18)));
            System.out.println("Coordenadas enviadas ao" +teammates[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            sendMessage(teammates[2], new Positions(positions, t3, robot3, new Position(230,18)));
            System.out.println("Coordenadas enviadas ao" + teammates[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
            try {
                sendMessage(teammates[3], new Positions(positions, t4, robot4, new Position(290,18)));
                System.out.println("Coordenadas enviadas ao" + teammates[3]);
            } catch (IOException e) {
                e.printStackTrace();
            }


        while (ready==false){
            doNothing();
        }
        if (ready == true) {

            System.out.println("A iniciar o percurso");
            //for (int a = 0; a < 100; a++){
            for (int j = 1; j < positions.size(); j++) {
                while (!(getX() == positions.get(j).getX() && getY() == positions.get(j).getY())) {
                    goTo(positions.get(j).getX(), positions.get(j).getY());
                }
            }
        }
        goTo(getX(), 18);  // Encosta a parede
        goTo(50,18);
    }


    public void onMessageReceived(MessageEvent e) {

        if (e.getMessage() instanceof Confirm){
            Confirm c = (Confirm) e.getMessage();
            num_ready = num_ready +1;
            System.out.println("Recebi uma nova confirmação de posição: " + num_ready);
            if (num_ready == 4){
                this.ready = true;

                while (!(getX() == positions.get(0).getX() && getY() == positions.get(0).getY())) {
                    goTo(positions.get(0).getX(), positions.get(0).getY());
                }
                try {
                    broadcastMessage(new Confirm(true));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                System.out.println("Estão todos a postos: " + num_ready);
            }
        }
        else  if (e.getMessage() instanceof sendName) {
            System.out.println("[MSG] Recebi nome de um TeamMate");
            sendName n = (sendName) e.getMessage();
            teammates[num_team]=n.getName();
            System.out.println(teammates[num_team]);
            num_team ++;
        }

    }


    private void goTo(double x, double y) {

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
    }


    public void onHitRobot(HitRobotEvent e) {
        System.out.println("Ups! Colision with another robot! " + e.getName());
        back(50);
        turnLeft(60);
        ahead(50);
    }


    public void onPaint(Graphics2D g) {
        g.setColor(Color.RED);
        int initialX = (int) Math.round(getBattleFieldWidth() / 5);
        int initialY = (int) Math.round(getBattleFieldHeight() / 10);


        int pos1X = (int) Math.round(getBattleFieldWidth() / 5);
        int pos1Y = (int) Math.round(getBattleFieldHeight() / 10);

        // Desenhar o S
        g.drawLine(100, 100,300,100);
        g.drawLine(300, 100,300,280);
        g.drawLine(300, 280,100,280);
        g.drawLine(100, 280,100,450);
        g.drawLine(100, 450,300,450);

        // Desenhar o A

        g.drawLine(450, 100,550,450);
        g.drawLine(550, 450,650,100);
        g.drawLine(470, 250,630,250);

    }


}