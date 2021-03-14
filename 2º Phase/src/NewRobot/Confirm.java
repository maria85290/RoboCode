package NewRobot;

import java.io.Serializable;

public class  Confirm  implements Serializable {

    private boolean bool = false;


    public boolean getI (){
        return this.bool;
    }

    public Confirm(boolean p) {
        this.bool = p;
    }
}
