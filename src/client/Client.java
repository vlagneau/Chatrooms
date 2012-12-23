package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import message.Header;
import message.Message;
import server.Server;

public class Client {
	private Socket _socket;
	private PrintWriter _out;
	private Thread _threadEcoute;

	/**
	 * Cr�ation du client, lancement du thread d'�coute
	 * @param _socket socket � partir duquel le client va communiquer
	 */
	public Client(Socket _socket) {
		super();
		this._socket = _socket;
		
		// si le socket est connect� au serveur, on lance le thread d'�coute et on ouvre le flux d'envoi
		if(_socket.isConnected()){
			try {
				_out = new PrintWriter(_socket.getOutputStream());
				
				_threadEcoute = new Thread(new SessionToClient(this));
				_threadEcoute.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fonction permettant de tenter de se connecter au serveur gr�ce � un couple pseudo / password
	 * @param pseudo
	 * @param password
	 */
	public void seConnecter(String pseudo, String password){
		if(_socket.isConnected()){
			String donnees = pseudo + Header.DELIMITEUR_DONNES + password;
			Message messageConnexion = new Message(null, Header.CODE_NATURE_IDENTIFICATION, donnees);
			
			// envoi du message de connexion
			envoyerMessage(messageConnexion);
		}
	}
	
	public void envoyerInscription(String pseudo, String password){
		if(_socket.isConnected()){
			String donnees = pseudo + Header.DELIMITEUR_DONNES + password;
			Message messageConnexion = new Message(null, Header.CODE_NATURE_INSCRIPTION, donnees);
			
			// envoi du message de connexion
			envoyerMessage(messageConnexion);
		}
	}
	
	/**
	 * Fonction permettant d'envoyer un message texte dans le flux de sortie (vers la session) pour une chatroom
	 * dont l'identifiant � �t� pass� en param�tre
	 * @param idChatroom identifiant de la chatroom dans laquelle envoyer le message
	 * @param message message texte � envoyer
	 */
	public void envoyerTexte(Integer idChatroom, String message){
		if(_socket.isConnected() && idChatroom != null){
			String nouveauMessage = idChatroom + Header.DELIMITEUR_CHATROOM + message;
			
			Message messageEnvoye = new Message(null, Header.CODE_NATURE_TEXTE, nouveauMessage);
			
			// envoi du message
			envoyerMessage(messageEnvoye);
		}
	}
	
	/**
	 * Fonction permettant au client d'acc�der � la chatroom dont l'identifiant est
	 * pass� en param�tre
	 * @param idChatroom nom de la chatroom
	 */
	public void seConnecterChatroom(Integer idChatroom){
		if(_socket.isConnected()){
			Message messageEnvoye = new Message(null, Header.CODE_NATURE_CONNEXION_CHATROOM, idChatroom.toString());
			
			envoyerMessage(messageEnvoye);
		}
	}
	
	/**
	 * Fonction permettant au client de se d�connecter de la chatroom dont l'identifiant est
	 * pass� en param�tre
	 * @param idChatroom nom de la chatroom
	 */
	public void seDeconnecterChatroom(Integer idChatroom){
		if(_socket.isConnected()){
			Message messageEnvoye = new Message(null, Header.CODE_NATURE_DECONNEXION_CHATROOM, idChatroom.toString());
			
			envoyerMessage(messageEnvoye);
		}
	}
	
	/**
	 * Fonction permettant d'envoyer un message dans le flux de sortie du socket (vers la session)
	 * @param message message � envoyer
	 */
	private void envoyerMessage(Message message){
		_out.write(message.toString());
		_out.flush();
	}
	
	/**
	 * Fonction permettant de cr�er une chatroom sur le serveur
	 * @param nomChatroom nom de la chatroom � cr�er
	 */
	public void creationChatroom(String nomChatroom){
		if(_socket.isConnected()){
			Message messageEnvoye = new Message(null, Header.CODE_NATURE_CREATION_CHATROOM, nomChatroom);
			
			envoyerMessage(messageEnvoye);
		}
	}
	
	/**
	 * Fonction permettant de quitter une chatroom � laquelle le serveur est connect�
	 * @param idChatroom identifiant de chatroom
	 */
	public void suppressionChatroom(String idChatroom){
		if(_socket.isConnected()){
			Message messageEnvoye = new Message(null, Header.CODE_NATURE_SUPPRESSION_CHATROOM, idChatroom);
			
			envoyerMessage(messageEnvoye);
		}
	}
	
	/**
	 * Fonction permettant d'arr�ter le client proprement en fermant notamment le socket
	 */
	public void arreterClient(){
		try {
//			deconnexionClient();
//			finClient();
			_socket.close();
			
			System.out.println("Arr�t du client");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Fonction permettant de d�connecter le client (retour � une session non connect�e au serveur)
	 */
	public void deconnexionClient(){
		if(_socket.isConnected()){
			Message messageDeconnexion = new Message(null, Header.CODE_NATURE_DECONNEXION_SERVEUR, "-1");
			
			envoyerMessage(messageDeconnexion);
		}
	}
	
	/**
	 * Thread d'�coute du flux entrant du socket (messages issus de la session)
	 */
	private class SessionToClient implements Runnable{
		private BufferedReader _in;
		
		// cr�ation du thread, r�cup�ration du flux entrant du socket
		public SessionToClient(Client client){
			super();
			
			try {
				_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			// tant que le socket n'est pas ferm�, on lit les messages
			while(!_socket.isClosed()){
				try {
					if(_in.ready()){
						String test = _in.readLine();
						
						Message messageRecu = new Message(test);
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_TEXTE)){
							System.out.println(messageRecu.getField(Header.DONNEES));
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_CONNEXION_CHATROOM_OK)){
							System.out.println("Connexion � la chatroom OK");
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_DECONNEXION_CHATROOM_OK)){
							System.out.println("D�connexion � la chatroom OK");
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_CREATION_CHATROOM_OK)){
							System.out.println("Cr�ation de la chatroom OK");
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_CREATION_CHATROOM_KO)){
							System.out.println("Cr�ation de la chatroom KO");
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_SUPPRESSION_CHATROOM_OK)){
							System.out.println("Suppression de la chatroom OK");
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_SUPPRESSION_CHATROOM_KO)){
							System.out.println("Suppression de la chatroom KO");
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_LISTE_CHATROOMS)){
							System.out.println(messageRecu.getField(Header.DONNEES));
						}
						
						if(messageRecu.getField(Header.NATURE).equals("" + Header.CODE_NATURE_LISTE_USERS_CHATROOMS)){
							System.out.println(messageRecu.getField(Header.DONNEES));
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		Socket socket;

		try {
		
			socket = new Socket(InetAddress.getLocalHost(),Server.DEFAULT_PORT);
//			InetAddress test = InetAddress.getByName("192.168.4.138");
//			
//			socket = new Socket(test,Server.DEFAULT_PORT); 
			
		     if(socket.isConnected()){
		    	Client client = new Client(socket);
		    	socket = null;
//		    	client.envoyerInscription("toto", "azer"); 
		    	client.seConnecter("lagneau", "azer");
		    	
		    	client.seConnecterChatroom(0);
//		    	client.seDeconnecterChatroom(0);
		    	
		    	client.creationChatroom("test");
		    	client.creationChatroom("test2");
		    	
		    	client.suppressionChatroom("1");
		    	
		    	client.envoyerTexte(0, "Coucou!");
		    	
//		    	client.arreterClient();
		     }
		     
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
