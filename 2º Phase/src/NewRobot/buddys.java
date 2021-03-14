package NewRobot;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Esta classe implementa um buddy que será um membro da equipa S.A.
 */

public class buddys extends TeamRobot{

    private ArrayList<Position> positions = new ArrayList<>(); // array com as posições que devem seguir.
    private int time = 0;     // tempo de espera até começar a rota.
    private Position initial;  // Posição para onde se deve dirigir no inicio.
    private Position posF; // Posição para onde se dirige no final do percurso.

    private  boolean start = false;   // quando está a true significa que o robot teve autoriação do lider para começar
    private String nameL;  // Nome do lider da equipa


    public void run() {

        System.out.println("[INICIO]");

        while(this.start!=true){
            doNothing();
        }

        // Ja todos estão na posição inicial.
        // Recebem autorização para iniciar o percurso.

        if (this.start == true) {
            System.out.println("[MSG] Autorizado a fazer o trajeto");

            // Para não iniciarem todos ao mesmo tempo. Espera time iterações.
            for (int i = 0; i < time; i++) {
                doNothing();
            }

            // Começa a percorrer o trajeto enviado pelo lider.
            for (int j = 0; j < positions.size(); j++) {
                while (!(getX() == positions.get(j).getX() && getY() == positions.get(j).getY())) {
                    goTo(positions.get(j).getX(), positions.get(j).getY());
                }

            }
        }
        goTo(getX(), 18);  // Encosta a parede

        //Vai para a posição final.
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

       // Recebe confirmação para iniciar a trajetoria.

       else if (e.getMessage() instanceof Confirm){
            System.out.println("[MSG] Recebi Confirmação para inciar a trajetoria");
            Confirm c = (Confirm) e.getMessage();
            this.start = c.getI();



        }

       // Recebe o nome do seu lider
        else  if (e.getMessage() instanceof sendName) {
            System.out.println("[MSG] Recebi nome do Lider");
            sendName n = (sendName) e.getMessage();

            this.nameL = n.getName();

            // Enviar o seu nome ao lider.
            try {
                sendMessage(this.nameL, new sendName(getName()));
            } catch (IOException a) {
                a.printStackTrace();
            }
        }

    }


    /**
     * Função que desloca o robot para a posição (newx, newy)
     */

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


    /**
     * Permite definir o comportamento do robot quando choca.
     */

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
