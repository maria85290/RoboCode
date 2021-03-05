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

		if (getHeading() >= 45){
			turnRight(225 - 180 -getHeading());
		}
		else{
			turnRight(225 - 180 -getHeading());
		}

		goTo(18,18);

		//turnRight(360-getHeading());

		this.start = true;

	}

	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("isRacing"))
			this.odometer.getRaceDistance();
		if (cd.getName().equals("isRacing"))
			this.standardOdometer.getRaceDistance();
	}

	private void goTo(double x, double y){

		x -= getX();
		y -= getY();

		double angleToTarget = Math.atan2(x, y);

		double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());


		double distance = Math.hypot(x, y);

		/* This is a simple method of performing set front as back */
		double turnAngle = Math.atan(Math.tan(targetAngle));

		setTurnRightRadians(turnAngle);
	/*
	Fazer isso é o mesmo que fazer o passo de baixo.
		if (targetAngle<Math.PI/2 && -Math.PI/2<targetAngle){
			setAhead(distance);
		}
		else{
			setBack(distance);
		}

	 */

		if(targetAngle == turnAngle) {
			setAhead(distance);
		} else {
			setBack(distance);
		}


	}

	private void rotate() {
		//Calcular angulos e apontar radar e arma para a posição


	}



}
