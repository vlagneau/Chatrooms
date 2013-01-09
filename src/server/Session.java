package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.StringTokenizer;

import message.Header;
import message.Message;

public class Session {
	private static int _idSession = 1;
	private Integer _identifiant;
	private Socket _socket;
	private Chatter _chatter;
	private PrintWriter _out;
	private BufferedReader _in;
	private Server _server;
	private Boolean _isConnected;

	public Chatter get_chatter() {
		return _chatter;
	}
	
	/**
	 * Lancement d'une session
	 * @param server serveur auquel est associé la session
	 * @param chatter chatter identifé pour la session
	 * @param socket socket via laquelle va échanger la session avec le client
	 */
	public Session(Server server, Chatter chatter, Socket socket){
		_identifiant = _idSession;
		_idSession++;
		_server = server;
		_socket = socket;
		_chatter = chatter;
		_isConnected = Boolean.FALSE;
		
		// création des flux entrants et sortants
		try {
			_out = new PrintWriter(_socket.getOutputStream());
			
			envoyerMessage(_server.obtenirMessageListeChatrooms());
			
			_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		// lancement du thread d'écoute de la session
		new Thread(new ClientToSession()).start();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("SESSION : DESTRUCTEUR SESSION");
	}

	/**
	 * Fonction demandant au serveur d'identifier un User grâce aux données issus d'un message
	 * de demande d'identification client
	 * @param donnees partie données du message d'identification envoyé par le client
	 */
	private void demandeIdentification(String donnees){
		// découpage des informations des données
		StringTokenizer stringTokenizer = new StringTokenizer(donnees, Header.DELIMITEUR_DONNES);
		String pseudo = stringTokenizer.nextToken();
		String password = stringTokenizer.nextToken();
		
		// idenfitication auprès du serveur, et création du message de retour vers le client
		boolean userFound = _server.findUser(pseudo, password);
		Message messageRetour = null;
		
		if(userFound){
			_chatter = new Chatter(pseudo);
			_isConnected = Boolean.TRUE;
			
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_IDENTIFICATION_OK, "-1");
		}
		else{
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_IDENTIFICATION_KO, "-1");
		}
		
		// envoi du message
		envoyerMessage(messageRetour);
	}
	
	/**
	 * Fonction permettant d'ajouter un user à la liste des users du serveur grâce aux données
	 * du message d'inscription reçu par la session
	 * @param donnees données à partir desquelles seront extraites le pseudo et le mot de passe
	 */
	private void inscriptionUser(String donnees) {
		// découpage des informations des données
		StringTokenizer stringTokenizer = new StringTokenizer(donnees, Header.DELIMITEUR_DONNES);
		String pseudo = stringTokenizer.nextToken();
		String password = stringTokenizer.nextToken();
		
		_server.addUser(pseudo, password);
	}
	
	/**
	 * Fonction permettant de déconnecter l'utilisateur du serveur et des chatrooms dans lequel il
	 * était connecté
	 */
	public void deconnexionServeur(){
		if(_isConnected){
			_chatter = new Chatter();
			_isConnected = Boolean.FALSE;
			
			_server.quitterToutesChatroom(this);
		}
	}
	
	/**
	 * Fonction permettant d'envoyer un message dans le flux sortant de la socket (vers le client)
	 * @param message message à envoyer
	 */
	public void envoyerMessage(Message message){
		_out.write(message.toString());
		_out.flush();
	}
	
	/**
	 * Fonction permettant d'associer la session à une chatroom dont l'identifiant
	 * est passé en paramètre
	 * @param idChatroom nom de la chatroom concernée
	 */
	private void joindreChatroom(String idChatroom){
		Boolean reussite = Boolean.FALSE;
		
		// tentative de rejoindre une chatroom
		if(idChatroom != null && !idChatroom.equals("")){
			reussite = _server.joindreChatroom(this, new Integer(idChatroom));
		}
		
		// génération du message retourné (réussite ou echec)
		Message messageRetour = null;
		
		if(reussite){
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_CONNEXION_CHATROOM_OK, "-1");
		}
		else{
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_CONNEXION_CHATROOM_KO, "-1");
		}
		
		envoyerMessage(messageRetour);
	}
	
	/**
	 * Fonction permettant de déconnecter la session d'une chatroom dont le nom
	 * est passé en paramètre
	 * @param idChatroom nom de la chatroom concernée
	 */
	private void quitterChatroom(String idChatroom){
		Boolean reussite = Boolean.FALSE;
		
		// tentative de quitter la chatroom
		if(idChatroom != null && !idChatroom.equals("")){
			reussite = _server.quitterChatroom(this, new Integer(idChatroom));
		}
		
		// génération du message retourné (réussite ou echec)
		Message messageRetour = null;
		
		if(reussite){
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_DECONNEXION_CHATROOM_OK, "-1");
		}
		else{
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_DECONNEXION_CHATROOM_KO, "-1");
		}
		
		envoyerMessage(messageRetour);
	}
	
	/**
	 * Fonction permettant à un client de créer une chatroom
	 * @param nomChatroom nom de la chatroom à créer
	 */
	private void creerChatroom(String nomChatroom){
		Boolean reussite = Boolean.FALSE;
		
		if(nomChatroom != null && !nomChatroom.equals("")){
			_server.creerChatroom(nomChatroom);
			reussite = Boolean.TRUE;
		}
		
		// génération du message retourné (réussite ou echec)
		Message messageRetour = null;

		if(reussite){
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_CREATION_CHATROOM_OK, "-1");
		}
		else{
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_CREATION_CHATROOM_KO, "-1");
		}

		envoyerMessage(messageRetour);
	}
	
	/**
	 * Fonction permettant à un client de supprimer une chatroom
	 * @param idChatroom identifiant de la chatroom à créer
	 */
	private void supprimerChatroom(String idChatroom){
		Boolean reussite = Boolean.FALSE;
		
		if(idChatroom != null && !idChatroom.equals("")){
			_server.supprimerChatroom(new Integer(idChatroom));
			reussite = Boolean.TRUE;
		}
		
		// génération du message retourné (réussite ou echec)
		Message messageRetour = null;

		if(reussite){
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_SUPPRESSION_CHATROOM_OK, "-1");
		}
		else{
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_SUPPRESSION_CHATROOM_KO, "-1");
		}

		envoyerMessage(messageRetour);
	}
	
	/**
	 * Fonction permettant d'indiquer au serveur que le client s'arrête et que la session est supprimée
	 */
	private void terminerSession() {
		_server.suppressionSession(this);
		try {
			_socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction permettant d'envoyer un message depuis le client vers le serveur (pour une chatroom précise)
	 * @param texte texte à transmettre à la chatroom
	 */
	private void transmettreMessageTexte(String texte){
		if(texte != null && !texte.equals("")){
			// récupération de la chatroom concernée par le message, ainsi que le texte à transmettre
			StringTokenizer tokenizer = new StringTokenizer(texte, Header.DELIMITEUR_CHATROOM);
			
			String identifiantChatroom = tokenizer.nextToken();
			String texteInitial = tokenizer.nextToken();
			
			// création du texte envoyé au serveur
			String nouveauTexte = _chatter.getPseudo() + " dit : " + texteInitial;
			Message messageEnvoye = new Message(_identifiant, Header.CODE_NATURE_TEXTE, nouveauTexte);
			
			// envoi du texte sur le serveur pour la chatroom concernée
			_server.envoyerMessageChatroom(new Integer(identifiantChatroom), messageEnvoye);
		}
	}

	/**
	 * Thread d'écoute du client par la session (flux entrant dans le socket) 
	 */
	private class ClientToSession implements Runnable{
		@Override
		public void run() {
			String messageClient = null;
			
			// tant que le socket n'est pas fermé, on écoute et on récupère les messages du flux entrant (socket in)
			try {
				// réception d'un message du client

				while(!_socket.isClosed() && (messageClient = _in.readLine()) != null){

					// découpe du message client
					Message messageRecu = new Message(messageClient);

					Integer natureMessageRecu = new Integer(messageRecu.getField(Header.NATURE));
					String donneesMessageRecu = messageRecu.getField(Header.DONNEES);

					switch (natureMessageRecu) {
					// s'il s'agit d'un message texte
					case Header.CODE_NATURE_TEXTE:
						transmettreMessageTexte(donneesMessageRecu);
						break;

						// s'il s'agit d'un message d'identification
					case Header.CODE_NATURE_IDENTIFICATION:
						demandeIdentification(donneesMessageRecu);
						break;

						// s'il s'agit d'un message de déconnexion du chatter
					case Header.CODE_NATURE_DECONNEXION_SERVEUR:
						deconnexionServeur();
						break;

						// s'il s'agit d'une connexion à une chatroom
					case Header.CODE_NATURE_CONNEXION_CHATROOM:
						joindreChatroom(donneesMessageRecu);
						break;

						// s'il s'agit d'une déconnexion de chatroom
					case Header.CODE_NATURE_DECONNEXION_CHATROOM:
						quitterChatroom(donneesMessageRecu);
						break;	

						// s'il s'agit d'une déconnexion de chatroom
					case Header.CODE_NATURE_CREATION_CHATROOM:
						creerChatroom(donneesMessageRecu);
						break;	

						// s'il s'agit d'une déconnexion de chatroom
					case Header.CODE_NATURE_SUPPRESSION_CHATROOM:
						supprimerChatroom(donneesMessageRecu);
						break;

						// s'il s'agit d'une déconnexion de chatroom
					case Header.CODE_NATURE_INSCRIPTION:
						inscriptionUser(donneesMessageRecu);
						break;	
						
						// s'il s'agit d'un ordre d'extinction du serveur
					case Header.CODE_NATURE_EXTINCTION_SERVEUR:
						_server.arretServeur();
						break;
						
					default:
						break;
					}
				}
			} catch(SocketException se){
				//se.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(_isConnected){
				deconnexionServeur();
			}

			terminerSession();
			
			System.out.println("SESSION : session terminée");
		}
	}
}
