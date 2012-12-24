package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import message.Header;

import view.LoginPanel.LoginListener;

import client.Client;

public class ChatPanel extends JPanel {
	
	JTextArea messages_TA;
	JList clients_L;
	DefaultListModel clients;

    // Variables declaration - do not modify
    private static javax.swing.JTextArea Chat_TA;
    private javax.swing.JButton Envoyer_B;
    private javax.swing.JButton Rejoindre_B;
    private javax.swing.JTextField Message_TF;
    private static javax.swing.JList Rooms_L;
    private javax.swing.JLabel Rooms_Label;
    private static javax.swing.JList Users_L;
    private javax.swing.JLabel Users_Label;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration
    
    Client _client;
    Integer _idRoom;
    
	
	public ChatPanel(Client client, String idRoom) {
		this._client = client;
		this._idRoom = new Integer(idRoom);
		initComponents();
		
		miseAjourUsers(_client._users);
		
	}
	
	private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Chat_TA = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        Users_L = new javax.swing.JList();
        Message_TF = new javax.swing.JTextField();
        Envoyer_B = new javax.swing.JButton("Envoyer");
        Users_Label = new javax.swing.JLabel();

        Envoyer_B.addActionListener(new ChatListener());
        Message_TF.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){ 
					(new ChatListener()).actionPerformed(null);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}

			
		});
        
        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Chat_TA.setColumns(20);
        Chat_TA.setEditable(false);
        Chat_TA.setRows(5);
        Chat_TA.setName("Chat_TA"); // NOI18N
        jScrollPane1.setViewportView(Chat_TA);

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        Users_L.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        Users_L.setName("Users_L"); // NOI18N
        jScrollPane2.setViewportView(Users_L);

        Message_TF.setName("Message_TF"); // NOI18N

        Envoyer_B.setName("Envoyer_B"); // NOI18N


        Users_Label.setName("Users_Label"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Message_TF, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 436, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Users_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                    .addComponent(Envoyer_B, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Users_Label)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 483, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Message_TF, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(Envoyer_B, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
    }
	
	/**
	 * Fonction qui met à jour le TEXTAREA de la discussion
	 * @param headerState
	 * @param message
	 */
	public static void getMessageFromClient(int headerState, String message) {
		Chat_TA.setText(Chat_TA.getText() + message);
		Chat_TA.validate();
	}
	
	/**
	 * Fonction qui met à jour la liste des users de la JList
	 * @param users
	 */
	public static void miseAjourUsers(final Set<String> users) {
		DefaultListModel list = new DefaultListModel();
		for (String user : users) {
			list.addElement(user);
		}
		if(Users_L != null) {
			Users_L.setModel(list);
			Users_L.validate();
		}
	}
	


	public class ChatListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			//Envoie du texte à la session si l'on clique sur Envoyer
	    	 _client.envoyerTexte(_idRoom, Message_TF.getText());
	    	 Message_TF.setText("");
	    	 Message_TF.validate();
		}
	}
	
	
}
