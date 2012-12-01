package message;

public class Header {
	public static final String DELIMITEUR_CHAR = "#";
	public static final String DELIMITEUR_DONNES = "%";
	
	public static final int IDENTIFIANT_SERVEUR = 0;
	
	public static final int CODE_NATURE_IDENTIFICATION = 10;
	public static final int CODE_NATURE_IDENTIFICATION_OK = 101;
	public static final int CODE_NATURE_IDENTIFICATION_KO = 102;
	
	public static final int CODE_NATURE_TEXTE = 20;
	
	public static final int CODE_NATURE_CONNEXION_CHATROOM = 30;
	public static final int CODE_NATURE_CONNEXION_CHATROOM_OK = 301;
	public static final int CODE_NATURE_CONNEXION_CHATROOM_KO = 302;
	
	public static final int CODE_NATURE_DECONNEXION_CHATROOM = 40;
	public static final int CODE_NATURE_DECONNEXION_CHATROOM_OK = 401;
	public static final int CODE_NATURE_DECONNEXION_CHATROOM_KO = 402;
	
	public static final int IDENTIFICATEUR = 0;
	public static final int NATURE = 1;
	public static final int DONNEES = 2;
	
	public static int convertCommand(String command){
		int retour = -1;

		switch (command) {
		default:
			break;
		}
		
		return retour;
	}
}
