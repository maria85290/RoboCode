package NewRobot;
import robocode.*;
import robocode.util.Utils;
import standardOdometer.Odometer;
public class MyRobot extends AdvancedRobot{
	private Odometer standardOdometer = new Odometer ("isRacing", this);
	private MyOdometer odometer = new MyOdometer("isRacing",this);
	private boolean start = false;
	
	double atualY;
	double atualX;

	public void run() {		
		addCustomEvent(standardOdometer);
	}

	public void onCustomEvent (CustomEvent ev) {
		Condition cd = ev.getCondition();
		if (cd.getName().equals("isRacing"))
			this.odometer.getRaceDistance();
	}
	
	private void goTo() {
		//ter atencao radar e arma ; aponta-los para (18,18) e ter angulos para essa posicao
		this.atualX = this.getX();
		this.atualY = this.getY();
	}
	
	private void rotate() {
		//Calcular angulos e apontar radar e arma para a posição
		double initialAngle = this.getHeading();
		
	}
	

	
}
