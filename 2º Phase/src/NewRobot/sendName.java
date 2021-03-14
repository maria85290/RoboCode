package NewRobot;

import java.io.Serializable;

/**
 * Permite enviar o nome dos robos.
 */
public class sendName implements Serializable{
    private String name;
    public String getName() {
            return name;
        }




        public sendName(String name) {
            this.name = name;
        }
  }

