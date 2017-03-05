package stacs.logic.item;

import java.awt.Image;
import java.io.Serializable;

public abstract class Item implements Serializable {
    private static final long serialVersionUID = -109531769726562759L;
    
    private static int ID = 0;
    public final int id = ID++;
    
    public abstract Image getImage();
    public abstract String getName();
}
