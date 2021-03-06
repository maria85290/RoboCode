package NewRobot;
import robocode.*;
import robocode.util.Utils;
import standardOdometer.Odometer;


public class MyRobot extends AdvancedRobot{
	private Odometer standardOdometer = new Odometer ("isRacing", this);
	private MyOdometer odometer = new MyOdometer("Racing",this);
	private boolean start = true;
	private boolean scanningObject = false;
	private int turns = 0;


	public void run() {
		addCustomEvent(standardOdometer);
		addCustomEvent(this.odometer);

		// para o colocar na origem
		turnRight(225 - getHeading());

		while(!(this.getX() == 18 && this.getY() == 18)){
			goTo(18 ,18);
		}

		//Make time so he gets to the starting point
		for (int i = 0; i < 100; i++) {
			doNothing(); // or perhaps scan();
		}
		
		odometer.startRace();
		this.turnLeft(this.getHeading());

		while(start){
			turnRight(1);
			this.scanningObject = true;
			if(this.turns >= 3) {
				System.out.println("Just passed the 3rd robot");
				this.scanningObject = false;
				goTo(18,18);
				start = false;
			}
		}
		System.out.println(this.odometer.totalDist());
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		super.onScannedRobot(e);
		if (scanningObject){
			turnLeft(e.getBearing()*1.7);
			ahead(e.getDistance()+ 18);
			setTurnRight(60);
			ahead(40);
			this.turns++;
		}
	}

	
	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("Racing"))
			this.odometer.getRobotDistance();
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
		turnRightRadians(turnAngle);


		if(targetAngle == turnAngle) {
			ahead(distance);
		} else {
			back(distance);
		}
	}
	
	public void onHitRobot(HitRobotEvent e) {
        setTurnLeft(10);
        back(15);
    }
}
