package ThirdPhase;

public class Enemy implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name = null;
	private double x = 0.0;
	private double y = 0.0;
	
	public Enemy(String name, double x, double y) {
		this.name=name;
		this.x = x;
		this.y = y;
	}
	
	public String getName(){
		return name;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public void setX(double x){
		this.x=x;
	}
	
	public void setY(double y){
		this.y=y;
	}
}