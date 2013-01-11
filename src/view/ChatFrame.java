package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;

public class ChatFrame extends JFrame {

	JPanel mainPanel;
	Client _client;
	String _idRoom;
	
	public String getIdRoom() {
		return _idRoom;
	}
	
	public ChatFrame(Client client, String login, String room, String idRoom) {
		this._client = client;
		this._idRoom = idRoom;
		
		mainPanel = new ChatPanel(client, idRoom);
	    this.setLocationRelativeTo(null);
	    this.setSize(800, 600);
	    this.setContentPane(mainPanel);               
	    this.setVisible(true);
	    this.setTitle("Nom d'utilisateur : " + login + ", Salle : " + room);
	    
	    this.addWindowListener(new ChatFrameListener());
	}
	
	public class ChatFrameListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			_client._openRooms.put(_idRoom, false);
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			_client._openRooms.put(_idRoom, false);
			_client.seDeconnecterChatroom(new Integer(_idRoom));
			
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		
	}
	
	
	
}
