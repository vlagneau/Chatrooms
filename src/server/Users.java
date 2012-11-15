package server;

import java.util.HashSet;
import java.util.Set;

public class Users {
	private Set<User> _listeUsers;

	public Users() {
		_listeUsers = new HashSet<User>(0);
	}
	
	/**
	 * Méthode permettant d'ajouter un User à la classe
	 * @param user User devant être ajouté
	 */
	public void addUser(User user){
		_listeUsers.add(user);
	}
	
	/**
	 * Fonction permettant de savoir si un User est déjà présent dans la liste
	 * @param user User concerné
	 * @return TRUE si le User est dans la liste, FALSE sinon
	 */
	public boolean isRegistered(User user){
		return _listeUsers.contains(user);
	}
	
	public void findUser(String pseudo, String password){
		// TODO méthode findUser
	}
}
