package NewRobot;
import robocode.*;
import robocode.util.Utils;
import standardOdometer.Odometer;


public class MyRobot extends AdvancedRobot{
	private Odometer standardOdometer = new Odometer ("isRacing", this);
	private MyOdometer odometer = new MyOdometer("isRacing",this);
	private boolean start = false;
	
	double actualY;
	double actualX;

	public void run() {		
		addCustomEvent(standardOdometer);
		addCustomEvent(odometer);
		
		System.out.println(calcAngle(18,18));
		setTurnGunLeft(calcAngle(18,18));
		ahead(100);
	}

	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("isRacing"))
			this.odometer.getRaceDistance();
		if (cd.getName().equals("isRacing"))
			this.standardOdometer.getRaceDistance();
	}
	
	private void goTo() {
		//ter atencao radar e arma ; aponta-los para (18,18) e ter angulos para essa posicao
		this.actualX = this.getX();
		this.actualY = this.getY();
	}
	
	private void rotate() {
		//Calcular angulos e apontar radar e arma para a posição

		
	}
	
	private double calcAngle(double destX, double destY) {
		//Reta para obter o angulo
		double distX = destX - actualX;
		double distY = destY - actualY;
		
		//Angulo da reta 90º até à reta que vai da origem do robo até ao ponto(18,18)
		double tempAngle = Math.atan2(distX, distY);
		
		System.out.println("Heading:" + this.getHeading());
		
		//Subtração entre temp angle e o angulo da frente do robo
		double rotationAngle = Utils.normalRelativeAngle(tempAngle- this.getHeading());
		
		
		return rotationAngle;
	}
		
}
