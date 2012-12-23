package message;

public class Header {
	public static final String DELIMITEUR_CHAR = "#";
	public static final String DELIMITEUR_DONNES = "%";
	public static final String DELIMITEUR_CHATROOM = "§";
	
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
	
	public static final int CODE_NATURE_DECONNEXION_SERVEUR = 50;
	public static final int CODE_NATURE_DECONNEXION_SERVEUR_OK = 501;
	public static final int CODE_NATURE_DECONNEXION_SERVEUR_KO = 502;
	
	public static final int CODE_NATURE_LISTE_CHATROOMS = 60;
	
	public static final int CODE_NATURE_LISTE_USERS_CHATROOMS = 70;
	
	public static final int CODE_NATURE_CREATION_CHATROOM = 80;
	public static final int CODE_NATURE_CREATION_CHATROOM_OK = 801;
	public static final int CODE_NATURE_CREATION_CHATROOM_KO = 802;
	
	public static final int CODE_NATURE_SUPPRESSION_CHATROOM = 90;
	public static final int CODE_NATURE_SUPPRESSION_CHATROOM_OK = 901;
	public static final int CODE_NATURE_SUPPRESSION_CHATROOM_KO = 902;

	public static final int CODE_NATURE_INSCRIPTION = 0;
	
	public static final int IDENTIFICATEUR = 0;
	public static final int NATURE = 1;
	public static final int DONNEES = 2;
}
