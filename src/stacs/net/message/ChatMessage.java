package stacs.net.message;

import stacs.net.client.TcpClient;
import stacs.net.server.ServerClientThread;

public class ChatMessage extends Message {
    private static final long serialVersionUID = 2045613638081294354L;

    public final String msg;
    
    public ChatMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public void clientRecieved(TcpClient tcpClient) {
        tcpClient.main.addMessage(msg);
    }

    @Override
    public void serverRecieved(ServerClientThread thread) {
        thread.server.world.sendMessage(this);
    }
    
    
}
