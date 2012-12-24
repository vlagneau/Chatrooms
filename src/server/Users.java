package server;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Users implements Serializable{
	private static final long serialVersionUID = -2151350248846300349L;
	private Set<User> _listeUsers;

	public Users() {
		_listeUsers = new HashSet<User>(0);
	}
	
	/**
	 * M�thode permettant d'ajouter un User � la classe
	 * @param user User devant �tre ajout�
	 */
	public void addUser(User user){
		_listeUsers.add(user);
	}
	
	/**
	 * Fonction permettant de savoir si un User est d�j� pr�sent dans la liste
	 * @param user User concern�
	 * @return TRUE si le User est dans la liste, FALSE sinon
	 */
	public boolean isRegistered(User user){
		return _listeUsers.contains(user);
	}
	
	/**
	 * Fonction d'affichage de la liste des users
	 */
	public void afficherUsers(){
		for (Iterator<User> iteratorUser = _listeUsers.iterator(); iteratorUser.hasNext();) {
			User userTemp = iteratorUser.next();
			
			System.out.println(userTemp.getPseudo());
		}
	}
}
