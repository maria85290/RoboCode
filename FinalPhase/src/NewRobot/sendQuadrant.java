package NewRobot;


import java.io.Serializable;

/*
 * Permite enviar o nome dos robos.
 */

public class sendQuadrant implements Serializable {

    private static final long serialVersionUID = 1L;
    private int quadrant;
    public int getQuadrant() {
        return quadrant;
    }
    public sendQuadrant(int x) {
        this.quadrant = x;
    }
}

