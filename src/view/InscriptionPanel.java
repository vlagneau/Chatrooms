package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import server.Server;
import view.ChatPanel.ChatListener;
import client.Client;

public class InscriptionPanel extends JPanel{
    private javax.swing.JButton Inscription_B;
    private javax.swing.JLabel Login_L;
    private javax.swing.JTextField Login_TF;
    private javax.swing.JLabel Password_L;
    private javax.swing.JTextField Password_TF;
    private JFrame _parent;
    
    private Client _client;
    
    
	/**
	 * Constructor
	 */
	public InscriptionPanel(JFrame parent, Client client) {
		initComponents(parent);
		this._client = client;
		this._parent = parent;
		
	}
	
	public class InscriptionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String login = Login_TF.getText();
			String mdp = Password_TF.getText();
			boolean trouve = false;
			
			for (String user : _client._users) {
				if(login.equals(user)) {
					trouve = true;
					break;
				}					
			}
			
			if(!login.isEmpty() && !mdp.isEmpty() && !trouve) {
				_client.envoyerInscription(login, mdp);
				_parent.dispose();
			}
			else if (trouve) {
				JOptionPane.showMessageDialog(null,"Utilisateur déjà existant","Utilisateur existant", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		
	}
	
	private void initComponents(JFrame parent) {

		this._parent = parent;
		
        Login_L = new javax.swing.JLabel();
        Login_TF = new javax.swing.JTextField();
        Password_L = new javax.swing.JLabel();
        Password_TF = new javax.swing.JTextField();
        Inscription_B = new javax.swing.JButton("Valider");

        Inscription_B.addActionListener(new InscriptionListener());
        
        Login_L.setText("Nom d'utilisateur");

        Password_L.setText("Mot de passe");
        
        Password_L.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
					(new InscriptionListener()).actionPerformed(null);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			
		});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Password_L)
                        .addGap(18, 18, 18)
                        .addComponent(Password_TF))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(Login_L)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Login_TF, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Inscription_B)
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Inscription_B)
                .addContainerGap(18, Short.MAX_VALUE))
        );
    }
}
