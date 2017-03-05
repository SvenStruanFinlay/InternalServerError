package stacs.net.message;

import stacs.logic.entity.LivingEntity;
import stacs.logic.room.Room;
import stacs.net.client.TcpClient;

public class UpdateRoomMessage extends Message {
    private static final long serialVersionUID = -5456984313060093334L;
    
    private Room room;
    private LivingEntity player;
    private boolean requestTurns;
    
    public UpdateRoomMessage(Room room, LivingEntity ent, boolean requestTurns) {
        this.room = room;
        this.requestTurns = requestTurns;
        this.player = ent;
    }

    @Override
    public void clientRecieved(TcpClient tcpClient) {
        tcpClient.main.updateRoom(room, player, requestTurns);
    }
    
}
