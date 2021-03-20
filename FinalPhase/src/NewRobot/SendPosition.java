package NewRobot;

import java.io.Serializable;

/*
 * Permite enviar o nome dos robos juntamente com a sua posiçao
 */

public class SendPosition implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private Position pos;

    public Position getPos() {
        return pos;
    }

    public SendPosition(String name, Position pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }
}
