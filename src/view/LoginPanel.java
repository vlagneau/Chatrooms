package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import message.Header;

import server.Server;
import view.ChatPanel.ChatListener;
import client.Client;

public class LoginPanel extends JPanel {
	
    private javax.swing.JButton Connexion_B;
    private javax.swing.JButton Inscription_B;
    private javax.swing.JLabel Login_L;
    private static javax.swing.JTextField Login_TF;
    private javax.swing.JLabel Password_L;
    private javax.swing.JPasswordField Password_TF;
    
	static JFrame parent;
	
	static Client client;
	Socket socket;

	/**
	 * Constructor
	 */
	public LoginPanel(JFrame parent) {
		initComponents(parent);
		
		try {
		     this.socket = new Socket(InetAddress.getLocalHost(),Server.DEFAULT_PORT);
		     
		     if(this.socket.isConnected()){
		    	client = new Client(this.socket);
		}
		     
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
    private void initComponents(JFrame parent) {

    	this.parent = parent;
    	
        Login_L = new javax.swing.JLabel();
        Login_TF = new javax.swing.JTextField();
        Password_L = new javax.swing.JLabel();
        Password_TF = new javax.swing.JPasswordField();
        Connexion_B = new javax.swing.JButton();
        Inscription_B = new javax.swing.JButton("S'inscrire");

        Login_L.setText("Nom d'utilisateur");

        Password_L.setText("Mot de passe");


        Connexion_B.setText("Se connecter");
        Connexion_B.addActionListener(new LoginListener());
        
        Inscription_B.addActionListener(new LoginListener());
        
//        Password_TF.addKeyListener(new KeyListener(){
//
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
//					(new LoginListener()).actionPerformed(null);
//				}
//			}
//
//			@Override
//			public void keyReleased(KeyEvent e) {
//			}
//
//			@Override
//			public void keyTyped(KeyEvent e) {
//			}
//
//			
//		});


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Inscription_B)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Connexion_B))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Password_L)
                        .addGap(18, 18, 18)
                        .addComponent(Password_TF))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Login_L)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Login_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Login_L)
                    .addComponent(Login_TF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Password_L)
                    .addComponent(Password_TF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Connexion_B)
                    .addComponent(Inscription_B))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
	
	public class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == Connexion_B) {
				String loginString = Login_TF.getText();
				String passwordString = Password_TF.getText();
				if(!loginString.isEmpty() && !passwordString.isEmpty())
					client.seConnecter(loginString, passwordString);
			}
			else {
				new InscriptionFrame(client);
			}
			

		}
		
	}
	
	/**
	 * Fonction qui vérifie si redirige vers les Salles si le login/mdp est OK
	 * @param headerState
	 * @param message
	 */
	public static void getMessageFromClient(int headerState, String message) {
		
		//On affiche l'état de la connexion
		JOptionPane.showMessageDialog(null,message,"Connexion", JOptionPane.INFORMATION_MESSAGE);
		
		//Si c'est ok on lance la chatroom
		if(headerState == Header.CODE_NATURE_IDENTIFICATION_OK) {
			
			RoomFrame room = new RoomFrame(client, Login_TF.getText());
			parent.setVisible(false);
			
		}
	}
}
