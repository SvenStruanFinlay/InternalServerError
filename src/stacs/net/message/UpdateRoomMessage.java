package stacs.net.message;

import stacs.logic.entity.PlayerEntity;
import stacs.logic.room.Room;
import stacs.net.client.TcpClient;

public class UpdateRoomMessage extends Message {
    private static final long serialVersionUID = -5456984313060093334L;
    
    private Room room;
    private PlayerEntity player;
    
    public UpdateRoomMessage(Room room, PlayerEntity ent) {
        this.room = room;
        this.player = ent;
    }

    @Override
    public void clientRecieved(TcpClient tcpClient) {
        tcpClient.main.updateRoom(room, player);
    }
    
}
