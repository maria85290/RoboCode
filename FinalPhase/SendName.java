package NewRobot.FinalPhase;

import java.io.Serializable;

/*
 * Permite enviar o nome dos robos.
 */

public class SendName implements Serializable {
	private String name;
    public String getName() {
            return name;
        }
        public SendName(String name) {
            this.name = name;
        }
  }

