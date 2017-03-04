package stacs.net.message;

import java.io.Serializable;

import stacs.net.client.TcpClient;
import stacs.net.server.ServerClientThread;

public abstract class Message implements Serializable {
    private static final long serialVersionUID = 6132180955645887083L;

    public void clientRecieved(TcpClient tcpClient){
        throw new RuntimeException("not suppoerted " + this);
    }
    
    public void serverRecieved(ServerClientThread thread){
        throw new RuntimeException("not suppoerted " + this);
    }
}
