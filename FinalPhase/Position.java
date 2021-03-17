package NewRobot.FinalPhase;

import java.io.Serializable;

public class Position implements Serializable {
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    private double x;

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double y;

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
