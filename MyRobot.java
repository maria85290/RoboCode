package NewRobot;
import robocode.*; 
import standardOdometer.Odometer;
public class MyRobot extends AdvancedRobot{
	private Odometer odometer = new Odometer("isRacing",this);
	
	public void run() {
		addCustomEvent(odometer);
	}

	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("isRacing"))
			this.odometer.getRaceDistance();
	}
}
