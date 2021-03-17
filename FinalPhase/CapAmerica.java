package NewRobot.FinalPhase;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import NewRobot.PositionTracker;
import NewRobot.sendName;
import robocode.MessageEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.util.Utils;

//TeamLeader
public class CapAmerica extends TeamRobot {
    private  String[] teammates= new String[4];
    double energy = 200.0;
    private int num_team = 0;
    private ArrayList<String> teamsArray;
    private ArrayList<Position> cantos = new ArrayList<>();
    Position initial;
    
    
	public void run() {

		cantos.add(new Position(18,18));
		cantos.add(new Position(18, getBattleFieldHeight()-18)); 
		cantos.add(new Position((getBattleFieldWidth() - 18), 18)); 
		cantos.add(new Position((getBattleFieldWidth() - 18), getBattleFieldHeight()- 18)); 
        
        
		// Prepare RobotColors object
		RobotColors c = new RobotColors();

		c.bodyColor = Color.red;
		c.gunColor = Color.blue;
		c.radarColor = Color.red;
		c.scanColor = Color.white;
		c.bulletColor = Color.red;

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
		} catch (IOException ignored) {
			System.err.println("Exception Ocurred!");
		}
		
	    Position atual = new Position(getX(), getY());
		// Escolher canto mais próximo e deslocar-se para lá
	    int j = findClosestCorner(atual);
		initial = new Position (cantos.get(j).getX(),cantos.get(j).getY());
		goTo(cantos.get(j).getX(),cantos.get(j).getY());
		
		
		Position guardOne = new Position (initial.getX(), initial.getY());
	
		//informar teammates mais próximos da sua posição (nesta primeira fase apenas enviar a 2)
		try {
			sendMessage(teamsArray.get(0), initial); 
			sendMessage(teamsArray.get(1), initial); 
		} catch (IOException ignored) {}					
			
		//scan por inimigos
        turnRight(360);
		while (true) {
            turnRight(10);
		}
	}

	private int findClosestCorner(Position atual) {
		double minDist = getDistance(cantos.get(0),atual);			
		int j = 0;
		for (int i = 0; i < cantos.size(); i++) {
			if (getDistance(cantos.get(i),atual) < minDist) {
				minDist = getDistance(cantos.get(i),atual);
				j = i;
			}
		}
		return j;
	}

	/**
	 * onScannedRobot:  Ver se é inimigo, enviar nome do inimigo
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);

        teamsArray = new ArrayList<>(Arrays.asList(teammates));
        
		// Don't fire on teammates
		if (teamsArray.contains(e.getName())) {
			return;
		}
		
		try {
			// Send enemy name to teammates
			broadcastMessage(e.getName());
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}
	}

	
	//Descobrir teammates
    public void onMessageReceived(MessageEvent e){

        if (e.getMessage() instanceof SendName) {
            System.out.println("[MSG] Recebi nome de um TeamMate. ");
            sendName n = (sendName) e.getMessage();
            teammates[num_team] = n.getName();
            System.out.println(teammates[num_team]);
            num_team++;
        }
    }
	
    //Calculate Distance
    public double getDistance(Position canto, Position atual) {
        return Math.sqrt(Math.pow(canto.getX() - atual.getX(), 2) + Math.pow(canto.getY() - atual.getY(), 2));
	}
	
    /**
     * //Calculate Distance
	    public double getDistance(double x, double newX, double y, double newY) {
	        return Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2));
		}
     */
    
    //Função de deslocação
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
