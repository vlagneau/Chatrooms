package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoginFrame extends JFrame {
	
	JPanel mainPanel;
	public LoginFrame() {
		mainPanel = new LoginPanel(this);
		this.setTitle("Login");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setSize(280, 180);
	    this.setLocationRelativeTo(null);               
	    this.setContentPane(mainPanel);               
	    this.setVisible(true);
	}
	
}
