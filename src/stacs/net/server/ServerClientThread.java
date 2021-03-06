package stacs.net.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import stacs.logic.entity.PlayerEntity;
import stacs.logic.room.Room;
import stacs.net.message.ChatMessage;
import stacs.net.message.Message;
import stacs.net.message.UpdateRoomMessage;

public class ServerClientThread implements Runnable {

    private Socket clientSocket;
    public PlayerEntity player = null;

    public TcpServer server;

    ObjectOutputStream os = null;
    ObjectInputStream is = null;

    public ServerClientThread(Socket clientSocket, TcpServer server) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public void doUpdate(Room room, boolean turn) throws IOException {
        os.reset();
        os.writeObject(new UpdateRoomMessage(room, player, turn));
        os.flush();
    }

    @Override
    public void run() {
        try {
            try {
                os = new ObjectOutputStream(clientSocket.getOutputStream());
                is = new ObjectInputStream(clientSocket.getInputStream());

                while (!clientSocket.isClosed()) {
                    Object obj = is.readObject();
                    Message msg = (Message) obj;
                    System.out.println("Got message " + obj.getClass().getSimpleName());
                    msg.serverRecieved(this);
                }

                os.close();
                clientSocket.close();
            } catch (IOException e) {
                if(player != null)
                    server.world.remove(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(ChatMessage message) throws IOException {
        os.reset();
        os.writeObject(message);
        os.flush();
    }

}
