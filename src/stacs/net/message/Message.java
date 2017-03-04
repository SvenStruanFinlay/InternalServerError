package stacs.net.message;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = 6132180955645887083L;

    public abstract void doThings();
}
