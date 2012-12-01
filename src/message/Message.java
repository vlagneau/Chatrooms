package message;

import java.util.StringTokenizer;

public class Message {
	private Integer _identificateur;
	private Integer _nature;
	private String _donnees;
	
	
	public Message(String message) {
		StringTokenizer tokenizer = new StringTokenizer(message, Header.DELIMITEUR_CHAR);
		
		if(tokenizer.countTokens() == 3){
			String id = tokenizer.nextToken();
			
			if(id != null && !id.equals("null")){
				_identificateur = new Integer(id);
			}
			else{
				_identificateur = -1;
			}
		}
		else{
			_identificateur = -1;
		}
		
		_nature = new Integer(tokenizer.nextToken());
		_donnees = new String(tokenizer.nextToken());
	}
	
	public Message(Integer identificateur, Integer nature, String donnees){
		_identificateur = identificateur;
		_nature = nature;
		_donnees = donnees;
	}


	@Override
	public String toString() {
		return Header.DELIMITEUR_CHAR + _identificateur + Header.DELIMITEUR_CHAR + _nature + Header.DELIMITEUR_CHAR + _donnees + Header.DELIMITEUR_CHAR + "\n";
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
			retour = _identificateur.toString();
			break;
		case Header.NATURE:
			retour = _nature.toString();
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
		int cpt = 0;
		
		if(_identificateur != null){
			cpt++;
		}
		if(_nature != null){
			cpt++;
		}
		if(_donnees != ""){
			cpt++;
		}
		
		return cpt;
	}
	
	public void modifierIdentificateur(Integer nouvelIdentificateur){
		if(nouvelIdentificateur != null){
			_identificateur = nouvelIdentificateur;
		}
	}
}
