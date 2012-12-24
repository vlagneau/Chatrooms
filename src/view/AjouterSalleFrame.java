package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;

public class AjouterSalleFrame extends JFrame {
	
	JPanel mainPanel;
	
	public AjouterSalleFrame(Client client) {
		mainPanel = new AjouterSallePanel(client, this);
		this.setTitle("Ajouter une nouvelle salle");
	    this.setSize(300, 130);
	    this.setLocationRelativeTo(null);               
	    this.setContentPane(mainPanel);               
	    this.setVisible(true);
	}
}
