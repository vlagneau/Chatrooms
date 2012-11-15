package message;

import java.util.StringTokenizer;

public class Message {
	private String _identificateur;
	private String _nature;
	private String _donnees;
	
	
	public Message(String message) {
		StringTokenizer tokenizer = new StringTokenizer(message, Header.DELIMITEUR_CHAR);
		
		_identificateur = new String(tokenizer.nextToken());
		_nature = new String(tokenizer.nextToken());
		_donnees = new String(tokenizer.nextToken());
	}


	@Override
	public String toString() {
		return Header.DELIMITEUR_CHAR + _identificateur + Header.DELIMITEUR_CHAR + _nature + Header.DELIMITEUR_CHAR + _donnees + Header.DELIMITEUR_CHAR;
	}
	
	/**
	 * Fonction retournant le champ dont le nombre est passé en paramètre
	 * @param fieldNumber valeur entre 0 et le nombre de données du message
	 * @return une chaine correspondant au champ dont le nombre est passé en paramètre
	 */
	public String getField(int fieldNumber){
		String retour = null;
		switch (fieldNumber) {
		case Header.IDENTIFICATEUR:
			retour = _identificateur;
			break;
		case Header.NATURE:
			retour = _nature;
			break;
		case Header.DONNEES:
			retour = _donnees;
			break;
		default:
			break;
		}
		
		return retour;
	}
	
	/**
	 * Fonction retournant le nombre de champs du message
	 * @return le nombre de champs sous forme d'integer
	 */
	public int getNbFields(){
		return 3;
	}
}
