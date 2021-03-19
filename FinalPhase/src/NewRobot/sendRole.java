package NewRobot;

import java.io.Serializable;

public class sendRole implements Serializable {

    private static final long serialVersionUID = 1L;
    private String role;
    public String getRole() {
        return role;
    }
    public sendRole(String role) {
        this.role = role;
    }
}

