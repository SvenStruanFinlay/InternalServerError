package stacs.net.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import stacs.client.RoomGen;
import stacs.main.Room;
import stacs.net.message.ChatMessage;

public class TcpServer extends Thread {

    Room rm = RoomGen.generateRoom();
    boolean running = true;

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(8055);

            while (running) {
                final Socket clientSocket = ss.accept();
                new Thread(() -> {
                    ObjectOutputStream os  = null;
                    try{ 
                        os = new ObjectOutputStream(clientSocket.getOutputStream());
                        os.writeObject(new ChatMessage("hello there"));
                        os.close();
                        clientSocket.close();
                    } catch(IOException e){
                        e.printStackTrace();
                    } 
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TcpServer().start();
    }
}
