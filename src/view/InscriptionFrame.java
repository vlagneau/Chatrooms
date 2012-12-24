package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import client.Client;

public class InscriptionFrame extends JFrame {
	JPanel mainPanel;
	public InscriptionFrame(Client client) {
		mainPanel = new InscriptionPanel(this, client);
		this.setTitle("Inscription");
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(280, 180);
	    this.setLocationRelativeTo(null);               
	    this.setContentPane(mainPanel);               
	    this.setVisible(true);
	}
}
