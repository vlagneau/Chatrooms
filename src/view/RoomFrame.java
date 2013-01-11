package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.Client;

public class RoomFrame extends JFrame {
	JPanel mainPanel;
	
	Client _client;
	JMenuBar bar;
	JMenuItem deco;
	JMenuItem quitter;
	
	public static Set<ChatFrame> openRooms;
	
	
	public RoomFrame(Client client, String login) {
		openRooms = new HashSet<>();
		mainPanel = new RoomPanel(client, login, this);
		_client = client;
		
		bar = new JMenuBar();
		deco = new JMenuItem("Déconnecter");
		quitter = new JMenuItem("Quitter");
		
		deco.addActionListener(new RoomFrameMenuListener());
		quitter.addActionListener(new RoomFrameMenuListener());
		
		bar.add(deco);
		bar.add(quitter);
		
		
		this.setJMenuBar(bar);
		this.setTitle("Salles, " + login);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(220, 260);
	    this.setLocationRelativeTo(null);               
	    this.setContentPane(mainPanel);               
	    this.setVisible(true);	
	}
	public void FermerSalles() {
		for(JFrame frame : openRooms) {
			frame.dispose();
		}
	}
	
	public static void FermerSalle(String id) {
		for(ChatFrame frame : openRooms) {
			if(frame.getIdRoom().equals(id)) {
				frame.dispose();
				JOptionPane.showMessageDialog(null,"Fermeture de la chatroom","Fermeture", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	public class RoomFrameMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(arg0.getSource() == deco) {
				_client.deconnexionClient();
				FermerSalles();
				dispose();
				
				new LoginFrame();
			}
			else {
				_client.arreterClient();
				FermerSalles();
				System.exit(0);
			}
			
		}
		
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
