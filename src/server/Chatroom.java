package server;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
	
	/**
	 * Fonction permettant d'ajouter un nouvel utilisateur à la chatroom
	 * @param session session du nouvel utilisateur
	 */
	public void connexion(Session session){
		if(!_sessions.contains(session)){
			_sessions.add(session);
		}
	}
	
	/**
	 * Fonction permettant de retirer un utilisateur de la chatroom
	 * @param session session de l'utilisateur concerné
	 */
	public void deconnexion(Session session){
		if(_sessions.contains(session)){
			_sessions.remove(session);
		}
	}

	/**
	 * Fonction permettant de savoir si une Chatroom a bien un identifiant correspondant à celui passé en paramètre
	 * @param idChatroom identifiant recherché
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
	 * Fonction permettant d'envoyer un message à l'ensemble des sessions de la chatroom
	 * @param messageEnvoye message à transmettre
	 */
	public void transmettreMessage(Message messageEnvoye){
		for (Iterator<Session> iteratorSession = _sessions.iterator(); iteratorSession.hasNext();) {
			Session sessionTemp = iteratorSession.next();
			
			sessionTemp.envoyerMessage(messageEnvoye);
		}
	}
}
