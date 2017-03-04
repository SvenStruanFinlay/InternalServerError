package stacs.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import stacs.client.ClientMain;
import stacs.logic.turn.NextTurnAction;
import stacs.net.message.JoinMessage;
import stacs.net.message.Message;
import stacs.net.message.NextActionMessage;

public class TcpClient extends Thread {

    public ClientMain main;
    
    ObjectOutputStream os;
    ObjectInputStream is;
    
    public TcpClient(ClientMain main){
        this.main = main;
    }
    
    public void joinGame(String name){
        try {
            os.writeObject(new JoinMessage(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void sendAction(NextTurnAction action){
        try {
            os.reset();
            os.writeObject(new NextActionMessage(action));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void run() {
        Socket sock;
        try {
            sock = new Socket("localhost", 8055);
            os = new ObjectOutputStream(sock.getOutputStream());
            is = new ObjectInputStream(sock.getInputStream());
            
            while(true){
                Object obj = is.readObject();
                Message msg = (Message) obj;
                System.out.println("Got message " + obj.getClass().getSimpleName());
                msg.clientRecieved(this);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
