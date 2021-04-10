package NewRobot;

import java.io.Serializable;

public class PositionTracker implements Serializable {

    private double x;
    private double y;
    private int time;

    public PositionTracker(double x, double y, int t) {
        this.x = x;
        this.y = y;
        this.time=t;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getTime(){
        return time;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setTime(int t){
        this.time=t;
    }

}
