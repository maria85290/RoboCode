package NewRobot;
import robocode.Robot;
public class MyOdometer {
	public Robot robot;
	private double dist = 0.0;
	private boolean is_racing;
	private boolean finished;

	
	public MyOdometer (String name, Robot r) {
		this.robot = r;
		this.is_racing = false;
		this.finished = false;
	}
	
	public void getRaceDistance() {
		this.is_racing = true;
		//dist += getDistance()
	}
	
	public void endRace() {
		this.finished = true;
		this.is_racing = false;
	}
	
	public double getDistance(int x1, int x2, int y1, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
	}
	
	public String totalDist() {
		return "Race Distance = X pixels";
	}
}
