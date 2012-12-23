package server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import message.Header;
import message.Message;

public class Chatroom {
	private int _id;
	private Set<Session> _sessions;
	private String _nom;
	
	public Chatroom(Integer id, String nom){
		_sessions = new HashSet<Session>(0);
		_nom = nom;
		_id = id;
	}
	
	public int get_id() {
		return _id;
	}

	public String get_nom() {
		return _nom;
	}

	/**
	 * Fonction permettant d'ajouter un nouvel utilisateur � la chatroom
	 * @param session session du nouvel utilisateur
	 */
	public void connexion(Session session){
		if(!_sessions.contains(session)){
			_sessions.add(session);
		}
		
		transmettreListeUsersATous();
	}
	
	/**
	 * Fonction permettant de retirer un utilisateur de la chatroom
	 * @param session session de l'utilisateur concern�
	 */
	public void deconnexion(Session session){
		if(_sessions.contains(session)){
			_sessions.remove(session);
		}
		
		transmettreListeUsersATous();
	}

	/**
	 * Fonction permettant de savoir si une Chatroom a bien un identifiant correspondant � celui pass� en param�tre
	 * @param idChatroom identifiant recherch�
	 * @return TRUE si c'est le cas, FALSE sinon
	 */
	public boolean hasId(Integer idChatroom) {
		boolean retour = false;
		
		if(this._id == idChatroom){
			retour = true;
		}
		
		return retour;
	}
	
	/**
	 * Fonction permettant d'envoyer un message � l'ensemble des sessions de la chatroom
	 * @param messageEnvoye message � transmettre
	 */
	public void transmettreMessage(Message messageEnvoye){
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			sessionTemp.envoyerMessage(messageEnvoye);
		}
	}
	
	/**
	 * Fonction permettant de fermer proprement une chatroom en d�connectant l'ensemble des sessions
	 * qui lui sont associ�es
	 */
	public void fermetureChatroom(){
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			deconnexion(sessionTemp);
		}
	}
	
	/**
	 * Fonction permettant de savoir si une chatroom a une session de connect�e
	 * @param session session concern�e
	 */
	public boolean isSessionConnected(Session session){
		return _sessions.contains(session);
	}
	
	/**
	 * Fonction permettant d'envoyer � toutes les sessions connect�es � la chatroom
	 * la liste des users connect�s dans la chatroom
	 */
	private void transmettreListeUsersATous(){
		List<String> listeUsers = new ArrayList<String>(0);
		
		// r�cup�ration de l'ensemble des users connect�s
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			Chatter chatterTemp = sessionTemp.get_chatter();
			
			listeUsers.add(chatterTemp.getPseudo());
		}

		// cr�ation du message envoy� aux clients
		StringBuffer stringEnvoye = new StringBuffer("");
		boolean premier = true;
		
		for (Iterator<String> iteratorUsers = listeUsers.iterator(); iteratorUsers.hasNext();) {
			String stringUserTemp = (String) iteratorUsers.next();
			
			if(!premier){
				stringEnvoye.append(Header.DELIMITEUR_DONNES + stringUserTemp);
			}
			else{
				premier = false;
				stringEnvoye.append(stringUserTemp);
			}
		}
		
		Message messageEnvoye = new Message(Header.IDENTIFIANT_SERVEUR, Header.CODE_NATURE_LISTE_USERS_CHATROOMS, stringEnvoye.toString());
		
		// envoi du message � toutes les sessions connect�es � la chatroom
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			sessionTemp.envoyerMessage(messageEnvoye);
			
		}
	}
}
