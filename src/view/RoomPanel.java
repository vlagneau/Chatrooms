package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import server.Chatroom;

import client.Client;

public class RoomPanel extends JPanel {
	
    private javax.swing.JButton Ajouter_B;
    private static javax.swing.JList Salles_L;
    private javax.swing.JButton Supprimer_B;
    private javax.swing.JScrollPane jScrollPane1;
    
    private RoomFrame _parent;
    
    private Client _client;
    private String _login;
    
	public RoomPanel(final Client client, final String login, RoomFrame parent) {
		initComponents();
		_client = client;
		_login = login;
		_parent = parent;

		miseAjourChatrooms(client._rooms.keySet());
	}
	
	/*
	 * Fonction qui met à jour la JList des salles
	 */
	public static void miseAjourChatrooms(final Set<String> rooms) {
		DefaultListModel list = new DefaultListModel();
		for (String room : rooms) {
			list.addElement(room);
		}
		if(Salles_L != null) {
			Salles_L.setModel(list);
			Salles_L.validate();
		}
	}
	
	public class RoomListener implements ActionListener, MouseListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//Si l'on clique sur Ajouter alors on  lance la vue de création de chatroom
			if(e.getSource() == Ajouter_B) {
				
				new AjouterSalleFrame(_client);
			}
			//Si l'on clique sur Supprimer alors on  on supprime la chatroom
			if(e.getSource() == Supprimer_B) {
				
	            String room = Salles_L.getSelectedValue().toString();
	            if(!room.isEmpty() && !room.equals("Attente")) {
		            String id = _client._rooms.get(room);
					_client.suppressionChatroom(id);
					//RoomFrame.FermerSalle(id);
	            }
			}
			
		}

		@Override
		/**
		 * Evenement sur le clique d'un élement d'une salle
		 */
		public void mouseClicked(MouseEvent evt) {
			if(evt.getSource() == Salles_L) {
				JList list = (JList)evt.getSource();
		        //Si on double click
		        if (evt.getClickCount() == 2) {
		            String room = list.getSelectedValue().toString();
		            
		            String id = _client._rooms.get(room);
		            //on vérifie que l'on a pas ouverte la même salle
		            if(!_client._openRooms.containsKey(id)) {
		            	_client.seConnecterChatroom(new Integer(id));
						ChatFrame chat = new ChatFrame(_client, _login, room, id);
						_client._openRooms.put(id, true);
						_parent.openRooms.add(chat);
		            }
		            if(_client._openRooms.containsKey(id)) {
		            	if(!_client._openRooms.get(id)) {
		            		_client.seConnecterChatroom(new Integer(id));
							ChatFrame chat = new ChatFrame(_client, _login, room, id);
							_client._openRooms.put(id, true);
							_parent.openRooms.add(chat);
		            	}
		            }
		        }
			}
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Salles_L = new javax.swing.JList();
        Ajouter_B = new javax.swing.JButton("Ajouter");
        Supprimer_B = new javax.swing.JButton("Supprimer");

        setName("Form"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        Salles_L.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        Salles_L.setName("Salles_L"); // NOI18N
        jScrollPane1.setViewportView(Salles_L);

        Ajouter_B.setName("Ajouter_B"); // NOI18N

        Supprimer_B.setName("Supprimer_B"); // NOI18N
        
		Salles_L.addMouseListener(new RoomListener());
		Ajouter_B.addActionListener(new RoomListener());
		Supprimer_B.addActionListener(new RoomListener());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Ajouter_B)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addComponent(Supprimer_B)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Ajouter_B)
                    .addComponent(Supprimer_B))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
}
