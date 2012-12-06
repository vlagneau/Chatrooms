package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import message.Message;

public class Server {
	public final static int DEFAULT_PORT = 8080;
	
	private static int identifiantChatroom = 0;
	
	private int _nbClients;
	private Map<Integer, Chatroom> _chatrooms;
	private Set<Session> _sessions;
	private Users _users;
	private ServerSocket _serverSocket;
	
	/**
	 * Fonction permttant de trouver  si un User identifi� par le couple pseudo / password
	 * est pr�sent dans la base des User du serveur
	 * @param pseudo pseudo du User
	 * @param password password du User
	 * @return TRUE si le User est pr�sent dans la base des Users de serveur, FALSE sinon
	 */
	public boolean findUser(String pseudo, String password){
		Boolean retour = Boolean.FALSE;
		
		User user = new User(pseudo, password);
		retour = _users.isRegistered(user);
		
		return retour;
	}
	
	/**
	 * Fonction permettant � une session de rejoindre une chatroom
	 * @param session session concern�e
	 * @param idChatroom identifiant de la chatroom concern�e
	 * @return FALSE si la session n'a pas pu joindre la chatroom, TRUE sinon
	 */
	public boolean joindreChatroom(Session session, Integer idChatroom){
		Boolean retour = Boolean.FALSE;
		
		Chatroom chatroom = trouverChatroom(idChatroom);
		
		if(chatroom != null){
			chatroom.connexion(session);
			retour = Boolean.TRUE;
		}
		
		return retour;
	}
	
	/**
	 * Fonction permettant � une session de quitter une chatroom � laquelle elle est connect�e
	 * @param session session concern�e
	 * @param idChatroom identifiant de la chatroom concern�e
	 * @return FALSE si la session n'a pas se d�connecter de la chatroom, TRUE sinon
	 */
	public boolean quitterChatroom(Session session, Integer idChatroom){
		Boolean retour = Boolean.FALSE;
		
		Chatroom chatroom = trouverChatroom(idChatroom);
		
		if(chatroom != null){
			chatroom.deconnexion(session);
			retour = Boolean.TRUE;
		}
		
		return retour;
	}
	
	/**
	 * Fonction permettant de trouver une chatroom dans la liste des chatrooms du serveur � partir de son identifiant
	 * @param idChatroom identifiant de la chatroom concern�
	 * @return NULL si la chatroom n'est pas trouv�e, la Chatroom concern�e sinon
	 */
	private Chatroom trouverChatroom(Integer idChatroom) {
		Chatroom retour = null;
		
		if(_chatrooms.containsKey(idChatroom)){
			retour = _chatrooms.get(idChatroom);
		}
		
		return retour;
	}

	/**
	 * Lancement du serveur
	 */
	public Server()
	{
		_nbClients = 0;
		_chatrooms = new HashMap<Integer, Chatroom>(0);
		_sessions = new HashSet<Session>(0);
		_users = chargerUsers();
		
		creerChatroom("Attente");
		
		try {
			_serverSocket = new ServerSocket(DEFAULT_PORT);
			
			System.out.println("SERVER : Serveur lanc�");
			
			AcceptorThread acceptor = new AcceptorThread(this);
			new Thread(acceptor).start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction permettant de charger la liste des users connus du serveur
	 * @return la liste des users sous forme de Users
	 */
	private Users chargerUsers() {
		Users retour = new Users();
		
		retour.addUser(new User("lagneau", "azer"));
		retour.addUser(new User("mougamad", "azer"));
		
		return retour;
	}

	/**
	 * Fonction permettant de cr�er une chatroom sur le serveur
	 * @param nom nom de la chatroom
	 */
	private void creerChatroom(String nom) {
		Chatroom chatroom = new Chatroom(identifiantChatroom, nom);
		
		_chatrooms.put(identifiantChatroom, chatroom);
		
		identifiantChatroom++;
	}

	/**
	 * Classe priv�e thread�e permettant d'accepter les connections sur le serveur
	 * et de mettre � jour le nombre de clients
	 */
	private class AcceptorThread implements Runnable{
		private Server _server;
		
		public AcceptorThread(Server _server) {
			super();
			this._server = _server;
		}

		@Override
		public void run() {
			// tant que le serveur de socket n'est pas ferm�, on accepte de nouveaux sockets
			while(!_serverSocket.isClosed()){
				try {
					System.out.println("SERVER : Attente connexion");
					Socket socket = _serverSocket.accept();
					
					System.out.println("SERVER : connection realis�e");
					
					if(socket.isBound()){
						_nbClients++;
						
						Session session = new Session(_server, new Chatter(), socket);
						_sessions.add(session);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Fonction permettant d'envoyer un message � une chatroom dont l'identifiant est pass�
	 * en param�tres
	 * @param idChatroom identifiant de la chatroom concern�e
	 * @param messageEnvoye message � transmettre � la chatroom
	 */
	public void envoyerMessageChatroom(Integer idChatroom, Message messageEnvoye) {
		// on trouve la chatroom concern�e et on lui fait transmettre le message
		if(_chatrooms.containsKey(idChatroom)){
			Chatroom chatroom = _chatrooms.get(idChatroom);
			
			chatroom.transmettreMessage(messageEnvoye);
		}
	}
	
	public static void main(String arg[])
	{
		Server s = new Server();
	}
}
