package NewRobot;

import java.io.Serializable;

public class PositionEnemy implements Serializable {

    private double x;
    private double y;
    private double enemyEnergy;
    private String enemyName;

    public PositionEnemy(double x, double y, double e, String nome) {
        this.x = x;
        this.y = y;
        this.enemyEnergy = e;
        this.enemyName = nome;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getEnergy(){
        return enemyEnergy;
    }

    public String getName(){
        return enemyName;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setEnergy(double n) {
        this.enemyEnergy = n;
    }

    public void setName(String n) {
        this.enemyName = n;
    }

}
