package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
	public final static int DEFAULT_PORT = 8080;
	
	private int _nbClients;
	private Set<Chatroom> _chatrooms;
	private Set<Session> _sessions;
	private Users _users;
	
	public void seConnecter(){
		// TODO methode se connecter
	}
	
	public void findUser(String pseudo, String password){
		// TODO méthode findUser
	}
	
	public void seJoindreAUneChatroom(Chatroom chatroom){
		// TODO méthode seJoindreAUneChatroom
	}
	
	public Server()
	{
		_nbClients = 0;
		_chatrooms = new HashSet<Chatroom>(0);
		_sessions = new HashSet<Session>(0);
		_users = new Users();
		
		
		try {
			ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
			
			while(serverSocket.isBound()){
				Socket test = serverSocket.accept();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String arg[])
	{
		Server s = new Server();
	}
}
