package server;

public class Chatter{
	protected String _pseudo;

	// ------------------ GETTERS / SETTERS -----------------------
	public String getPseudo() {
		return _pseudo;
	}

	public void setPseudo(String pseudo) {
		this._pseudo = pseudo;
	}

	// ------------------ METHODES --------------------------------
	public Chatter(String pseudo) {
		this._pseudo = pseudo;
	}
	
	public Chatter() {
		this._pseudo = "invité";
	}

	@Override
	public String toString() {
		return _pseudo;
	}

	@Override
	public int hashCode() {
		return _pseudo.hashCode();
	}
}
