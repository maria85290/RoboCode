package NewRobot;

import java.io.Serializable;

/**
 * Esta classe permite enviar uma confirmação.
 */

public class  Confirm  implements Serializable {

    private boolean bool = false;


    public boolean getI (){
        return this.bool;
    }

    public Confirm(boolean p) {
        this.bool = p;
    }
}
