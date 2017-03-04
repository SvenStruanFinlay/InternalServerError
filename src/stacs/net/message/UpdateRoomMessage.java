package stacs.net.message;

import stacs.logic.room.Room;
import stacs.net.client.TcpClient;

public class UpdateRoomMessage extends Message {
    private static final long serialVersionUID = -5456984313060093334L;
    
    private Room room;
    
    public UpdateRoomMessage(Room room) {
        this.room = room;
    }

    @Override
    public void clientRecieved(TcpClient tcpClient) {
        tcpClient.main.updateRoom(room);
    }
    
}
