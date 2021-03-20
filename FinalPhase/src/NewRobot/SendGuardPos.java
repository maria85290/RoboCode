package NewRobot;

import java.io.Serializable;

public class SendGuardPos implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private Position pos;

    public Position getPos() {
        return pos;
    }

    public SendGuardPos(String name, Position pos) {
        this.name = name;
        this.pos = pos;
    }

    public String getName() {

        return name;
    }

}
