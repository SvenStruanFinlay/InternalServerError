package stacs.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	ServerSocket socket = null;
	
	InputStream in = null;
	OutputStream out = null;
	
	public Server(int port) throws IOException {
		
		this.socket = new ServerSocket(port);
		
		while (true) {
			
			Socket connection = socket.accept();
			
			
		}
		
	}
	

	
	
	public static void main(String...strings) throws IOException {
		
		Server server = new Server(9696);
		
		
		
	}
	
}
