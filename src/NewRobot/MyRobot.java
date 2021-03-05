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

		//setTurnGunRight(calcAngle(18,18));
		turnRight(calcAngle(18,18));
		ahead(100);
	}

	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("isRacing"))
			this.odometer.getRaceDistance();
		if (cd.getName().equals("isRacing"))
			this.standardOdometer.getRaceDistance();
	}

	private void goTo(double x, double y){
		x = getX();
		y = getY();

		/* Calculate the angle to the target position */
		double angleToTarget = Math.atan2(x, y);

		/* Calculate the turn required get there */
		double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());

		/*
		 * The Java Hypot method is a quick way of getting the length
		 * of a vector. Which in this case is also the distance between
		 * our robot and the target location.
		 */
		double distance = Math.hypot(x, y);

		/* This is a simple method of performing set front as back */
		double turnAngle = Math.atan(Math.tan(targetAngle));
		setTurnRightRadians(turnAngle);
		if(targetAngle == turnAngle) {
			setAhead(distance);
		} else {
			setBack(distance);
		}
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

		System.out.println("Atan angle: " + tempAngle);
		System.out.println("Heading: " + this.getHeading());

		//Subtração entre temp angle e o angulo da frente do robo
		double rotationAngle = Utils.normalRelativeAngle(tempAngle- this.getHeading());

		System.out.println("Rotation: " + rotationAngle);
		return rotationAngle;
	}

}
