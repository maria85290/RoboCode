package NewRobot;

import java.io.Serializable;

/*
 * Permite enviar o nome dos robos.
 */

public class SendTeam implements Serializable {

    private static final long serialVersionUID = 1L;
    private String [] team;
    public String[] getTeam() {
        return team;
    }
    public SendTeam(String[] team ) {
        this.team = team;
    }
}