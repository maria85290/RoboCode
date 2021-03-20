package NewRobot;

import java.io.Serializable;

public class SendRole implements Serializable {

    private static final long serialVersionUID = 1L;
    private String role;
    public String getRole() {
        return role;
    }
    public SendRole(String role) {
        this.role = role;
    }
}

