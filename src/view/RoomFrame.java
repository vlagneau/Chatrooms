package view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;

public class RoomFrame extends JFrame {
	JPanel mainPanel;
	
	Client _client;
	
	public RoomFrame(Client client, String login) {
		mainPanel = new RoomPanel(client, login);
		_client = client;
		
		
		this.setTitle("Salles, " + login);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(220, 240);
	    this.setLocationRelativeTo(null);               
	    this.setContentPane(mainPanel);               
	    this.setVisible(true);	
	}
	
	public class RoomFrameListener implements WindowListener {

		@Override
		public void windowActivated(WindowEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent arg0) {
			
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			_client.arreterClient();
			
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
