package stacs.net.client;

import java.io.ObjectInputStream;
import java.net.Socket;

import stacs.net.message.Message;

public class TcpClient extends Thread {

    @Override
    public void run() {
        Socket sock;
        try {
            sock = new Socket("localhost", 8055);
            ObjectInputStream is = new ObjectInputStream(sock.getInputStream());
            Object obj = is.readObject();
            Message msg = (Message) obj;
            msg.doThings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TcpClient().start();
    }
}
