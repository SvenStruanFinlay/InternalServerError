package stacs.server;

import stacs.alexa.Server;
import stacs.net.server.TcpServer;
import stacs.test.RoomGen;

public class ServerMain {
    
    ServerWorld world = new ServerWorld();
    
    public static void main(String[] args) {
        ServerMain main = new ServerMain();
        main.run();
    }
    
    public void run() {
        RoomGen.generateWorld(world);
        new Thread(new Server()).start();
        new TcpServer(world).start();
        world.runServerLoop();
    }
}
