package server;

import java.io.Serializable;


public class User extends Chatter implements Serializable{
	private static final long serialVersionUID = 358622370398815350L;
	private String _password;

	public User(String pseudo, String password) {
		super(pseudo);
		this._password = password;
	}

	public User() {
		super();
	}

	@Override
	public int hashCode() {
		return super.hashCode() + _password.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		boolean retour = false;
		
		if (this == obj){
			retour = true;
		}
		else{
			User other = (User) obj;
			
			if(other != null){
				if(_password == null && other._password == null){
					if((_pseudo == null && other._pseudo == null) || _pseudo.equals(other._pseudo)){
						retour = true;
					}
				}
				else if(_pseudo == null && other._pseudo == null){
					if((_password == null && other._password == null) || _password.equals(other._password)){
						retour = true;
					}
				}
				else if(_password.equals(other._password) && _pseudo.equals(other._pseudo)){
					retour = true;
				}
			}
		}
		
		return retour;
	}

	@Override
	public String toString() {
		return _pseudo + " - " + _password;
	}
}
