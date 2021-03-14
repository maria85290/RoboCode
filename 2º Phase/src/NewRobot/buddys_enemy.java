package NewRobot;

import robocode.HitRobotEvent;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;


public class buddys_enemy extends TeamRobot{

   /**
   Este robô apenas se dirige para o canto superior direito e permanece lá até o jogo terminar.
    **/
    public void run() {


        while(!(this.getX() == getBattleFieldWidth() && this.getY() == getBattleFieldHeight())){
            // Encostar os robos a parede
            goTo(getBattleFieldWidth(),getBattleFieldHeight());

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
        back(100);

        for(int i = 0; i<20; i++){
            doNothing();
        }
        turnRight(100);
        ahead(100);
    }
}
