package stacs.net.message;

import stacs.main.Room;

public class UpdateRoomMessage extends Message {
    private static final long serialVersionUID = -5456984313060093334L;

    
    private Room room;
    
    public UpdateRoomMessage(Room room) {
        this.room = room;
    }
    
    @Override
    public void doThings() {
        System.out.println("got room");
    }

}
