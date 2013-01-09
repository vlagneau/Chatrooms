package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import message.Header;
import message.Message;
import serialization.SerializationMain;

public class Server {
	public final static int DEFAULT_PORT = 8080;
	
	private static int identifiantChatroom = 0;
	
	private int _nbClients;
	private Map<Integer, Chatroom> _chatrooms;
	private Set<Session> _sessions;
	private Users _users;
	private ServerSocket _serverSocket;
	
	/**
	 * Lancement du serveur
	 */
	public Server()
	{
		_nbClients = 0;
		_chatrooms = SerializationMain.deserializationChatrooms();
		_sessions = new HashSet<Session>(0);
		_users = SerializationMain.deserializationUsers();
		
		if(_users == null){
			_users = new Users();
		}
		
		if(_chatrooms.size() == 0){
			creerChatroom("Attente");
		}
		else{
			identifiantChatroom = _chatrooms.size();
		}

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
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("SERVEUR : DESTRUCTEUR SERVEUR");
	}
	
	/**
	 * Fonction permettant d'arr�ter proprement le serveur. Il coupe les connexions, sauvegarde les users et chatrooms avant de s'�teindre
	 */
	public void arretServeur(){
		// deconnexions de toutes les sessions
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			sessionTemp.deconnexionServeur();
		}
		
//		_sessions.clear();
		
		// serialisation des chatrooms et users du serveur
		seralizationServer();
		
		// arr�t du serveur (thread + objet courant)
		arretThreadConnexion();
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction permettant de stopper le thread de reception de connexions
	 */
	private void arretThreadConnexion(){
		if (!_serverSocket.isClosed()) {
			try {
				_serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Fonction de sauvegarde des informations du serveur
	 */
	private void seralizationServer(){
		SerializationMain.serializationUsers(_users);
		
		Set<Chatroom> temp = new HashSet<Chatroom>(_chatrooms.values());
		SerializationMain.serializationChatrooms(temp);
	}
	
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
	 * Fonction permettant d'ajouter un user � la liste des users du serveur
	 * @param pseudo pseudo du user concern�
	 * @param password mot de pase du user concern�
	 */
	public void addUser(String pseudo, String password){
		User user = new User(pseudo, password);
		
		_users.addUser(user);
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
	public void creerChatroom(String nom) {
		Chatroom chatroom = new Chatroom(identifiantChatroom, nom);
		
		_chatrooms.put(identifiantChatroom, chatroom);
		
		identifiantChatroom++;
		
		miseAJourListeChatroomClients();
	}
	
	/**
	 * Fonction permettant de supprimer une chatroom du serveur
	 * @param idChatroom identifiant de la chatroom concern�e
	 */
	public void supprimerChatroom(Integer idChatroom) {
		Chatroom chatroom = _chatrooms.get(idChatroom);
		
		if(chatroom != null){
			_chatrooms.remove(idChatroom);
			
			chatroom.fermetureChatroom();
		}
		
		miseAJourListeChatroomClients();
	}
	
	/**
	 * Fonction permettant d'envoyer un message � toutes les sessions connect�es afin de leur indiquer
	 * l'ensemble des chatrooms disponibles sur le serveur
	 */
	public void miseAJourListeChatroomClients(){
		Message messageListe = obtenirMessageListeChatrooms();
		
		// envoi du message � toutes les sessions connect�es au serveur
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			sessionTemp.envoyerMessage(messageListe);			
		}
	}
	
	/**
	 * Fonction permettant d'obtenir le message contenant la liste des chatrooms ouvertes
	 * sur le serveur
	 * @return le message format� contenant la liste des chatrooms
	 */
	public Message obtenirMessageListeChatrooms(){
		StringBuffer listeChatrooms = new StringBuffer("");
		
		boolean premier = true;
		
		// cr�ation de la liste des chatrooms
		for (Iterator<Chatroom> iteratorChatrooms = _chatrooms.values().iterator(); iteratorChatrooms.hasNext();) {
			Chatroom chatroomTemp = iteratorChatrooms.next();

			if(!premier){
				listeChatrooms.append(Header.DELIMITEUR_CHATROOM);
			}
			else{
				premier = false;
			}
			
			listeChatrooms.append(chatroomTemp.get_id() + Header.DELIMITEUR_DONNES + chatroomTemp.get_nom());
		}
		
		// cr�ation du message transmettant la liste des chatrooms
		Message messageListe = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_LISTE_CHATROOMS, listeChatrooms.toString());
		
		return messageListe;
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
	
	public void suppressionSession(Session session){
		if(_sessions.contains(session)){
			_sessions.remove(session);
			
			_nbClients--;
		}
	}
	
	/**
	 * Fonction permettant de d�connecter une session de toutes les chatrooms auxquelles elle est connect�e
	 * @param session session concern�e
	 */
	public void quitterToutesChatroom(Session session) {
		Set<Chatroom> setChatroom = new HashSet<Chatroom>(_chatrooms.values());
		
		for (Iterator<Chatroom> iteratorChatroom = setChatroom.iterator(); iteratorChatroom.hasNext();) {
			Chatroom chatroom = iteratorChatroom.next();
			
			if(chatroom.isSessionConnected(session)){
				chatroom.deconnexion(session);
			}
			
		}
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
				} catch(SocketException se){
					// rien
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
//		s.seralizationServer();
	}
}
