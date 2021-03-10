package NewRobot;

public class Enemy implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private double x = 0.0;
	private double y = 0.0;
	private String name;

	public Enemy(String name, double x, double y) {
		this.x = x;
		this.y = y;
		this.name = name;
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
}