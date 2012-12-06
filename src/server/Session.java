package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
	
	/**
	 * Lancement d'une session
	 * @param server serveur auquel est associ� la session
	 * @param chatter chatter identif� pour la session
	 * @param socket socket via laquelle va �changer la session avec le client
	 */
	public Session(Server server, Chatter chatter, Socket socket){
		_identifiant = _idSession;
		_idSession++;
		_server = server;
		_socket = socket;
		_chatter = chatter;
		
		// cr�ation des flux entrants et sortants
		try {
			_out = new PrintWriter(_socket.getOutputStream());
			_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		// lancement du thread d'�coute de la session
		new Thread(new ClientToSession()).start();
	}
	
	/**
	 * Fonction demandant au serveur d'identifier un User gr�ce aux donn�es issus d'un message
	 * de demande d'identification client
	 * @param donnees partie donn�es du message d'identification envoy� par le client
	 */
	private void demandeIdentification(String donnees){
		// d�coupage des informations des donn�es
		StringTokenizer stringTokenizer = new StringTokenizer(donnees, Header.DELIMITEUR_DONNES);
		String pseudo = stringTokenizer.nextToken();
		String password = stringTokenizer.nextToken();
		
		// idenfitication aupr�s du serveur, et cr�ation du message de retour vers le client
		boolean userFound = _server.findUser(pseudo, password);
		Message messageRetour = null;
		
		if(userFound){
			_chatter = new Chatter(pseudo);
			
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_IDENTIFICATION_OK, "-1");
		}
		else{
			messageRetour = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_IDENTIFICATION_KO, "-1");
		}
		
		// envoi du message
		envoyerMessage(messageRetour);
	}
	
	/**
	 * Fonction permettant d'envoyer un message dans le flux sortant de la socket (vers le client)
	 * @param message message � envoyer
	 */
	public void envoyerMessage(Message message){
		_out.write(message.toString());
		_out.flush();
	}
	
	/**
	 * Fonction permettant d'associer la session � une chatroom dont l'identifiant
	 * est pass� en param�tre
	 * @param idChatroom nom de la chatroom concern�e
	 */
	private void joindreChatroom(String idChatroom){
		Boolean reussite = Boolean.FALSE;
		
		// tentative de rejoindre une chatroom
		if(idChatroom != null && !idChatroom.equals("")){
			reussite = _server.joindreChatroom(this, new Integer(idChatroom));
		}
		
		// g�n�ration du message retourn� (r�ussite ou echec)
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
	 * Fonction permettant de d�connecter la session d'une chatroom dont le nom
	 * est pass� en param�tre
	 * @param idChatroom nom de la chatroom concern�e
	 */
	private void quitterChatroom(String idChatroom){
		Boolean reussite = Boolean.FALSE;
		
		// tentative de quitter la chatroom
		if(idChatroom != null && !idChatroom.equals("")){
			reussite = _server.quitterChatroom(this, new Integer(idChatroom));
		}
		
		// g�n�ration du message retourn� (r�ussite ou echec)
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
	 * Fonction permettant d'envoyer un message depuis le client vers le serveur (pour une chatroom pr�cise)
	 * @param texte texte � transmettre � la chatroom
	 */
	private void transmettreMessageTexte(String texte){
		if(texte != null && !texte.equals("")){
			// r�cup�ration de la chatroom concern�e par le message, ainsi que le texte � transmettre
			StringTokenizer tokenizer = new StringTokenizer(texte, Header.DELIMITEUR_CHATROOM);
			
			String identifiantChatroom = tokenizer.nextToken();
			String texteInitial = tokenizer.nextToken();
			
			// cr�ation du texte envoy� au serveur
			String nouveauTexte = _chatter.getPseudo() + " dit : " + texteInitial;
			Message messageEnvoye = new Message(_identifiant, Header.CODE_NATURE_TEXTE, nouveauTexte);
			
			// envoi du texte sur le serveur pour la chatroom concern�e
			_server.envoyerMessageChatroom(new Integer(identifiantChatroom), messageEnvoye);
		}
	}

	/**
	 * Thread d'�coute du client par la session (flux entrant dans le socket) 
	 */
	private class ClientToSession implements Runnable{
		@Override
		public void run() {
			// tant que le socket n'est pas ferm�, on �coute et on r�cup�re les messages du flux entrant (socket in)
			while(!_socket.isClosed()){
				try {
					// r�ception d'un message du client
					if(_in.ready()){
						String messageClient = _in.readLine();
						
						// d�coupe du message client
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
						
							// s'il s'agit d'une connexion � une chatroom
							case Header.CODE_NATURE_CONNEXION_CHATROOM:
								joindreChatroom(donneesMessageRecu);
								break;
							
							// s'il s'agit d'une d�connexion de chatroom
							case Header.CODE_NATURE_DECONNEXION_CHATROOM:
								quitterChatroom(donneesMessageRecu);
								break;	
								
							default:
								break;
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			System.out.println("sortie session");
		}
	}
}
