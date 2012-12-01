package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import message.Message;

public class Server {
	public final static int DEFAULT_PORT = 8080;
	
	private static int identifiantChatroom = 0;
	
	private int _nbClients;
	private Set<Chatroom> _chatrooms;
	private Set<Session> _sessions;
	private Users _users;
	private ServerSocket _serverSocket;
	
	/**
	 * Fonction permttant de trouver  si un User identifié par le couple pseudo / password
	 * est présent dans la base des User du serveur
	 * @param pseudo pseudo du User
	 * @param password password du User
	 * @return TRUE si le User est présent dans la base des Users de serveur, FALSE sinon
	 */
	public boolean findUser(String pseudo, String password){
		Boolean retour = Boolean.FALSE;
		
		User user = new User(pseudo, password);
		retour = _users.isRegistered(user);
		
		return retour;
	}
	
	/**
	 * Fonction permettant à une session de rejoindre une chatroom
	 * @param session session concernée
	 * @param idChatroom identifiant de la chatroom concernée
	 * @return FALSE si la session n'a pas pu joindre la chatroom, TRUE sinon
	 */
	public boolean joindreChatroom(Session session, Integer idChatroom){
		// TODO méthode seJoindreAUneChatroom
		Boolean retour = Boolean.FALSE;
		
		Chatroom chatroom = trouverChatroom(idChatroom);
		
		if(chatroom != null){
			chatroom.connexion(session);
			retour = Boolean.TRUE;
		}
		
		return retour;
	}
	
	/**
	 * Fonction permettant à une session de quitter une chatroom à laquelle elle est connectée
	 * @param session session concernée
	 * @param idChatroom identifiant de la chatroom concernée
	 * @return FALSE si la session n'a pas se déconnecter de la chatroom, TRUE sinon
	 */
	public boolean quitterChatroom(Session session, Integer idChatroom){
		// TODO méthode seJoindreAUneChatroom
		Boolean retour = Boolean.FALSE;
		
		Chatroom chatroom = trouverChatroom(idChatroom);
		
		if(chatroom != null){
			chatroom.deconnexion(session);
			retour = Boolean.TRUE;
		}
		
		return retour;
	}
	
	/**
	 * Fonction permettant de trouver une chatroom dans la liste des chatrooms du serveur à partir de son identifiant
	 * @param idChatroom identifiant de la chatroom concerné
	 * @return NULL si la chatroom n'est pas trouvée, la Chatroom concernée sinon
	 */
	private Chatroom trouverChatroom(Integer idChatroom) {
		Chatroom retour = null;
		
		boolean trouve = false;
		Iterator<Chatroom> iteratorChatrooms = _chatrooms.iterator();
		
		while(!trouve && iteratorChatrooms.hasNext()){
			Chatroom chatroomTemp = iteratorChatrooms.next();
			
			if(chatroomTemp.hasId(idChatroom)){
				trouve = true;
				retour = chatroomTemp;
			}
		}
		
		return retour;
	}

	/**
	 * Lancement du serveur
	 */
	public Server()
	{
		_nbClients = 0;
		_chatrooms = new HashSet<Chatroom>(0);
		_sessions = new HashSet<Session>(0);
		_users = chargerUsers();
		
		creerChatroom("test");
		
		try {
			_serverSocket = new ServerSocket(DEFAULT_PORT);
			
			System.out.println("SERVER : Serveur lancé");
			
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
	 * Fonction permettant de créer une chatroom sur le serveur
	 * @param nom nom de la chatroom
	 */
	private void creerChatroom(String nom) {
		Chatroom chatroom = new Chatroom(identifiantChatroom, nom);
		
		_chatrooms.add(chatroom);
		
		identifiantChatroom++;
	}

	/**
	 * Classe privée threadée permettant d'accepter les connections sur le serveur
	 * et de mettre à jour le nombre de clients
	 */
	private class AcceptorThread implements Runnable{
		private Server _server;
		
		public AcceptorThread(Server _server) {
			super();
			this._server = _server;
		}

		@Override
		public void run() {
			// tant que le serveur de socket n'est pas fermé, on accepte de nouveaux sockets
			while(!_serverSocket.isClosed()){
				try {
					System.out.println("SERVER : Attente connexion");
					Socket socket = _serverSocket.accept();
					
					System.out.println("SERVER : connection realisée");
					
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
	
	public static void main(String arg[])
	{
		Server s = new Server();
	}

	
	public void envoyerMessageChatroom(Message messageEnvoye) {
		// TODO Auto-generated method stub
		for (Iterator<Session> iterator = _sessions.iterator(); iterator.hasNext();) {
			Session session = iterator.next();
			
			session.envoyerMessage(messageEnvoye);
			
		}
	}
}
