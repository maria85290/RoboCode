package NewRobot;

import java.io.Serializable;
import java.util.ArrayList;

public class Positions implements Serializable {

    private ArrayList<Position> p = new ArrayList<>();
    private Position posI;  // posição inicial para onde o robot deve ir;
    private int time;
    private  Position posF; // Posição final onde o robot deve ficar;

    public double getX(int x) {
        return p.get(x).getX();
    }
    public double getY(int x) {
        return p.get(x).getY();
    }

    public Position getPosI(){
        return  posI;
    }

    public Position getPosF(){
        return  posF;
    }

    public ArrayList get(){
        return p;
    }

    public int getTime(){
        return time;
    }

    public Positions(ArrayList a, int time, Position posI, Position posF) {
        this.p = a;
        this.time = time;
        this.posI=posI;
        this.posF = posF;
    }
}
