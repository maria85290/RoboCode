package NewRobot;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import java.io.IOException;
import java.util.ArrayList;


public class buddys extends TeamRobot{

    private byte scanDirection = 1;
    private ArrayList<Position> positions = new ArrayList<>();
    private int num=0;
    private int time = 0;
    private Position initial;  // Posição para onde se deve dirigir no inicio
    private Position posF; // Posição para onde se dirige no final do percurso

    private  boolean receive = false;
    private  boolean start = false;
    private String nameL;


    /**
     * run:  Droid's default behavior
     */
    public void run() {

        System.out.println("[INICIO]");

        while(this.start!=true){
            doNothing();
        }

        // Ja todos estão na posição inicial. Recebem autorização para iniciar o percurso.
        if (this.start == true) {
            System.out.println("[MSG] Autorizado a fazer o trajeto");
            for (int i = 0; i < time; i++) {
                doNothing();
            }

            for (int j = 0; j < positions.size(); j++) {
                while (!(getX() == positions.get(j).getX() && getY() == positions.get(j).getY())) {
                    goTo(positions.get(j).getX(), positions.get(j).getY());
                }

            }
        }
        goTo(getX(), 18);  // Encosta a parede
        goTo(this.posF.getX(), this.posF.getY());

    }


    /**
     * onMessageReceived:  What to do when our leader sends a message
     */
    public void onMessageReceived(MessageEvent e) {


        // Recebem as coordenadas para onde deve ir.
        if (e.getMessage() instanceof Positions) {
            System.out.println("[MSG] Recebi posição");
            Positions p = (Positions) e.getMessage();
            this.positions = p.get();
            this.initial = p.getPosI();
            this.time = p.getTime();
            this.posF = p.getPosF();


            System.out.println("time: " + this.time + "---- Posição Inicial: " + this.initial);

            while (!(getX() == initial.getX() && getY() == initial.getY())) {
                goTo(initial.getX(), initial.getY());
            }

            // Se já estiver na posição inicial então manda msg ao lider.
            if (getX() == initial.getX() && getY() == initial.getY()) {
                try {
                    sendMessage(this.nameL, new Confirm(true));
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }



            // goTo(40,18);
        }


        // Set our colors
       else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();
            System.out.println("[MSG] Recebi cor");
            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }

       else if (e.getMessage() instanceof Confirm){
            System.out.println("[MSG] Recebi Confirmação para inciar a trajetoria");
            Confirm c = (Confirm) e.getMessage();
            this.start = c.getI();



        }

        else  if (e.getMessage() instanceof sendName) {
            System.out.println("[MSG] Recebi nome do Lider");
            sendName n = (sendName) e.getMessage();

            this.nameL = n.getName();

            // Enviar o nome do robot
            try {
                sendMessage(this.nameL, new sendName(getName()));
            } catch (IOException a) {
                a.printStackTrace();
            }
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



    public void onHitRobot(HitRobotEvent e) {
        System.out.println("Ups! Colision with another robot! " + e.getName());
        back(40);
        for (int i = 0; i<time/5; i++){
            doNothing();
        }
        turnRight(20);
        ahead(40);
    }
}
