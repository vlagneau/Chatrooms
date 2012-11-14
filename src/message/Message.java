package message;

import java.util.StringTokenizer;

public class Message {
	public static final String DELIMITEUR_CHAR = "#";
	
	private String _identificateur;
	private String _nature;
	private String _donnees;
	
	
	public Message(String message) {
		StringTokenizer tokenizer = new StringTokenizer(message, Message.DELIMITEUR_CHAR);
		
		_identificateur = new String(tokenizer.nextToken());
		_nature = new String(tokenizer.nextToken());
		_donnees = new String(tokenizer.nextToken());
	}
	
	
}
