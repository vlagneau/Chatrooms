package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import server.Server;

public class Client {
	public static void main(String[] zero) {
		
		Socket socket;

		try {
		
		     socket = new Socket(InetAddress.getLocalHost(),Server.DEFAULT_PORT);
		     socket.close();

		}catch (UnknownHostException e) {
			
			e.printStackTrace();
		}catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
