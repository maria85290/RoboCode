package NewRobot;

public class DanceOrder implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private double x = 0.0;
	private double y = 0.0;
	private double rotation = 0.0;


	public DanceOrder(double x, double y, double rotation) {
		this.x = x;
		this.y = y;
		this.rotation = rotation;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRotation() {
		return rotation;
	}

}