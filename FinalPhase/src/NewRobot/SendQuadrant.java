package NewRobot;


import java.io.Serializable;

/*
 * Permite enviar o nome dos robos.
 */

public class SendQuadrant implements Serializable {

    private static final long serialVersionUID = 1L;
    private int quadrant;
    public int getQuadrant() {
        return quadrant;
    }
    public SendQuadrant(int x) {
        this.quadrant = x;
    }
}