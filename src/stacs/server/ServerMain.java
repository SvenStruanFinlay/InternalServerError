package stacs.server;

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
        new TcpServer(world).start();
        world.runServerLoop();
    }
}
