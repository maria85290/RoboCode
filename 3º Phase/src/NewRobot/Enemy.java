package NewRobot;


public class Enemy  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private double x = 0.0;
	private double y = 0.0;
	private String name;
	private double energy = 0.0;
	private double bearing = 0.0;
	private double distance = 0.0;


	public Enemy(String name, double x, double y, double energy, double bearing, double distance) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.energy = energy;
		this.bearing = bearing;
		this.distance =distance;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String getName() {
		return name;
	}

	public double getEnergy() {
		return energy;
	}

	public double getBearing() {
		return bearing;
	}

	public double getDistance() {
		return distance;
	}

}