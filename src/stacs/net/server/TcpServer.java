package stacs.net.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import stacs.logic.room.Room;
import stacs.net.message.ChatMessage;
import stacs.server.ServerWorld;
import stacs.test.RoomGen;

public class TcpServer extends Thread {

    public final ServerWorld world;
    
    public TcpServer(ServerWorld world) {
        this.world = world;
    }
    
    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8055);

            while (true) {
                final Socket clientSocket = ss.accept();
                new Thread(new ServerClientThread(clientSocket, this)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
