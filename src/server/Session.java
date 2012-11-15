package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import message.Header;
import message.Message;

public class Session implements Runnable {
	private static int _idSession = 0;
	private Integer _identifiant;
	private Server _server;
	private Chatter _chatter;
	private PrintWriter _out;
	private BufferedReader _in;
	
	public Session(Server server, Chatter chatter, Socket socketServer){
		_identifiant = _idSession;
		_idSession++;
		_server = server;
		_chatter = chatter;
		
		try {
			_out = new PrintWriter(socketServer.getOutputStream());
			_in = new BufferedReader(new InputStreamReader(socketServer.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.run();
	}
	
	public void demandeIdentification(String pseudo, String password){
		// TODO méthode demandeIdentification
	}
	
	public void seJoindreAUneChatroom(Chatroom chatroom){
		// TODO méthode seJoindreAUneChatroom
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			String donnees = _in.readLine();
			
			String messageString = Header.DELIMITEUR_CHAR + _identifiant + Header.DELIMITEUR_CHAR + "TEXT" + Header.DELIMITEUR_CHAR + donnees + Header.DELIMITEUR_CHAR;
			
			Message message = new Message(messageString);
		}
	}
}
