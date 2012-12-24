package serialization;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import server.Chatroom;
import server.Users;

public class SerializationMain {
	/**
	 * Fonction permettant de serialiser un objet Users correspondant � la liste
	 * des users du serveur
	 * @param users users � s�rialiser
	 */
	public static void serializationUsers(Users users){
		try {
			// ouverture du flux de sortie
			FileOutputStream fos = new FileOutputStream("users.ser");

			// cr�ation du flux objet vers le fichier
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			try {
				// s�rialisation
				oos.writeObject(users); 
				oos.flush();
			} finally {
				// fermeture des flux
				oos.close();
				fos.close();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Fonction permettant de deserialiser le fichier de serialisation des users du serveur
	 * @return l'objet Users correspondant � celui serialis�
	 */
	public static Users deserializationUsers(){
		Users users = null;
		
		try {
			// ouverture du flux d'entr�e du fichier
			FileInputStream fis = new FileInputStream("users.ser");
			// cr�ation du flux objet
			ObjectInputStream ois = new ObjectInputStream(fis);
			try {	
				// d�s�rialisation
				users = (Users) ois.readObject(); 
			} finally {
				// fermeture des flux
				ois.close();
				fis.close();
			}
		} catch(IOException ioe) {
//			ioe.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
//			cnfe.printStackTrace();
		}

		return users;
	}
	
	/**
	 * Fonction permettant de s�rialiser un set de chatrooms
	 * @param setChatroom Set de chatrooms � s�rialiser
	 */
	public static void serializationChatrooms(Set<Chatroom> setChatroom){
		try {
			// ouverture du flux de sortie
			FileOutputStream fos = new FileOutputStream("chatrooms.ser");

			// cr�ation du flux objet
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			try {
				// s�rialisation
				oos.writeObject(setChatroom); 
				oos.flush();
			} finally {
				// fermeture des flux
				oos.close();
				fos.close();
			}
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Fonction retournant la liste des chatrooms d�serialis�es sous la forme utilis�e par 
	 * le serveur
	 * @return une map id, chatrooms correspondant � celle exploit�e par le serveur
	 */
	public static Map<Integer, Chatroom> deserializationChatrooms(){
		Map<Integer, Chatroom> retour = new HashMap<Integer, Chatroom>(0);
		
		Set<Chatroom> setChatrooms = null;
		
		try {
			// ouverture du flux d'entr�e
			FileInputStream fis = new FileInputStream("chatrooms.ser");
			// cr�ation du flux objet
			ObjectInputStream ois= new ObjectInputStream(fis);
			try {	
				// d�s�rialisation
				setChatrooms = (Set<Chatroom>) ois.readObject(); 
			} finally {
				// fermeture des flux
				try {
					ois.close();
				} finally {
					fis.close();
				}
			}
		} catch(IOException ioe) {
//			ioe.printStackTrace();
		} catch(ClassNotFoundException cnfe) {
//			cnfe.printStackTrace();
		}
		
		
		if(setChatrooms != null) {
			for (Iterator<Chatroom> iterator = setChatrooms.iterator(); iterator.hasNext();) {
				Chatroom chatroom = iterator.next();
				chatroom.initSessions();
				retour.put(chatroom.get_id(), chatroom);				
			}
		}
		
		return retour;
	}
}	
