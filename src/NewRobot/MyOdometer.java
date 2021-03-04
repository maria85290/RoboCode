package NewRobot;
import java.text.DecimalFormat;

import robocode.Condition;
import robocode.Robot;

public class MyOdometer extends Condition{
	public Robot robot;
	private double dist = 0.0;
	private boolean is_racing;
	private boolean finished;

	private double x;
	private double y;
	private double newX;
	private double newY;
	
	public MyOdometer (String name, Robot r) {
		super(name);
		this.robot = r;
		this.is_racing = false;
		this.finished = false;
	}
	
	public void startRace() {
		x = robot.getX();
		y= robot.getY();
		newX = robot.getX();
		newY = robot.getY();
		
		this.is_racing = true;
	}
	
	public void getRaceDistance() {
		if (is_racing) {
			newX = robot.getX();
			newY = robot.getY();
			
			dist += getDistance(x, newX, y, newY);
			
			x = newX;
			y = newY;
		}
	}
	
	public void endRace() {
		finished = true;
		is_racing = false;
	}
	
	public double getDistance(double x, double newX, double y, double newY) {
        return Math.sqrt(Math.pow(newX - x, 2) + Math.pow(newY - y, 2));
	}
	
	public String totalDist() {
		endRace();
		return "Race Distance: " + (new DecimalFormat("#.##")).format(dist) + "pixels";
	}

	@Override
	public boolean test() {
		//this.robot.setDebugProperty("Racing", String.valueOf(this.is_racing));
        //this.robot.setDebugProperty("RoundFinished", String.valueOf(this.finished));
        //return robot.getTime()!=0;
		return false;
	}
}
