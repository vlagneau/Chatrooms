package server;

import java.util.HashSet;
import java.util.Set;

public class Chatroom {
	private Set<Session> _sessions;
	private String _nom;
	
	public Chatroom(String nom){
		_sessions = new HashSet<Session>(0);
		_nom = nom;
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
}
