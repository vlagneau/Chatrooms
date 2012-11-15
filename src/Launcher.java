import message.Message;
import server.User;
import server.Users;


public class Launcher {

	public static void main(String arg []){
		Message msg = new Message("#0#TEXT#comment ca va?");
		
		String s= msg.toString();
		String donnees = msg.getField(2);
		int nb_fields = msg.getNbFields();
		System.out.println(nb_fields); // affiche 3
		System.out.println(s); // affiche #0#TEXT#comment ca va ?#
		System.out.println(donnees); // affiche : comment ca va ?
	}
}
