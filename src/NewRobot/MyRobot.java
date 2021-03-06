package NewRobot;
import robocode.*;
import robocode.util.Utils;
import standardOdometer.Odometer;


public class MyRobot extends AdvancedRobot{
	private Odometer standardOdometer = new Odometer ("isRacing", this);
	private MyOdometer odometer = new MyOdometer("isRacing",this);
	private boolean start = false;
	private boolean isRacing = false;
	private boolean scanningObject = false;
	private int turns = 0;

	//double actualY;
	//double actualX;

	public void run() {

//        setAdjustGunForRobotTurn(true);
//        setAdjustRadarForGunTurn(true);

		addCustomEvent(standardOdometer);
		addCustomEvent(this.odometer);

		// para o colocar na origem
		turnRight(225 - getHeading());


		while(!(this.getX() == 18 && this.getY() == 18)){
			goTo(18 ,18);
		}

		//Make time so he gets to the starting point
		for (int i = 0; i < 90; i++) {
			doNothing(); // or perhaps scan();
		}

		this.turnLeft(this.getHeading());

		while(true){
			//
			//this.turnLeft(this.getHeading());
			turnRight(1);
			this.scanningObject = true;
			//setTurnRadarRight(360);
			//execute();
			if(this.turns >= 3) {
				System.out.println("Just passed the 3rd robot");
				this.scanningObject = false;
				goTo(18,18);
			}
		}

	}
	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);

		if (scanningObject){
			turnLeft(e.getBearing()*1.5);
			ahead(e.getDistance()+18);
			setTurnRight(60);
			ahead(40);
			this.turns++;


		}
	}



	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("isRacing"))
			this.odometer.getRaceDistance();
		if (cd.getName().equals("isRacing"))
			this.standardOdometer.getRaceDistance();
	}

	private void goToStartingPosition(double x, double y){
		goTo(x ,y);


	}

	private void goTo(double x, double y){

		x -= getX();
		y -= getY();

		double angleToTarget = Math.atan2(x, y);

		double targetAngle = Utils.normalRelativeAngle(angleToTarget - getHeadingRadians());


		double distance = Math.hypot(x, y);

		/* This is a simple method of performing set front as back */
		double turnAngle = Math.atan(Math.tan(targetAngle));
		System.out.println("START: " + this.start);
		turnRightRadians(turnAngle);
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
			ahead(distance);
		} else {
			back(distance);
		}


	}

	private void rotate() {
		//Calcular angulos e apontar radar e arma para a posição


	}



}
