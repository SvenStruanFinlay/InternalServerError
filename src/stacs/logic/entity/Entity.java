package stacs.logic.entity;

import java.awt.Image;
import java.io.Serializable;

import stacs.client.DrawingUtils;
import stacs.logic.room.Room;
import stacs.logic.room.Square;
import stacs.logic.turn.NullAction;
import stacs.server.ServerWorld;

public abstract class Entity implements Serializable {
    private static final long serialVersionUID = 8882148407207576417L;

    private static int ID = 0;
    public final int id = ID++;
    public Square currentSquare;
    public boolean interpolate = true;

    public String getDisplayName() {
        return null;
    }

    public void startNextTurn(ServerWorld world) {
        world.nextActionMap.put(this, new NullAction());
    }

    public Image getSprite(int tick) {
        return null;
    }

    public double getInterpolatedY(double prog, double scale, double transy, Square lastSquare) {
        int yy = DrawingUtils.transformY(currentSquare.x, 1 - currentSquare.getH(), currentSquare.y, scale, transy);

        int ly = yy;

        if (lastSquare != null && interpolate) {
            double hh2 = lastSquare.getH();
            ly = DrawingUtils.transformY(lastSquare.x, 1 - hh2, lastSquare.y, scale, transy);
        }

        if (prog < 1) {
            yy = (int) (prog * yy + (1 - prog) * ly);
        }
        return yy;

    }

    public double getInterpolatedX(double prog, double scale, double transx, Square lastSquare) {
        int xx = DrawingUtils.transformX(currentSquare.x, 1 - currentSquare.getH(), currentSquare.y, scale, transx);

        int lx = xx;

        if (lastSquare != null && interpolate) {
            double hh2 = lastSquare.getH();
            lx = DrawingUtils.transformX(lastSquare.x, 1 - hh2, lastSquare.y, scale, transx);
        }

        if (prog < 1) {
            xx = (int) (prog * xx + (1 - prog) * lx);
        }
        return xx;
    }
}
