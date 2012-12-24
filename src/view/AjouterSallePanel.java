package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import client.Client;

public class AjouterSallePanel extends JPanel {
	
    private javax.swing.JButton Annuler_B;
    private javax.swing.JButton Confirmer_B;
    private javax.swing.JLabel Salle_L;
    private javax.swing.JTextField Salle_TF;
    
    private Client _client;
    private JFrame _parent;
	
	public AjouterSallePanel(Client client, JFrame parent) {
		
		_client = client;
		_parent = parent;
		initComponents();
	}
	
	public class AjouterSalleListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			//si on clique sur Confirmer alors on vérifie que le champs n'est pas vide et qu'il n'existe pas une autre chatroom du même nom et on la crée
			if(e.getSource() == Confirmer_B) {
				
				String nom = Salle_TF.getText();
				if(!nom.isEmpty()) {
					
					if(!_client._rooms.containsKey(nom)) {
						_client.creationChatroom(nom);
						_parent.dispose();
					}
					else {
						JOptionPane.showMessageDialog(null,"Salle déjà existante","Nom de salle existante", JOptionPane.ERROR_MESSAGE);
					}
					
				}
				else {
					JOptionPane.showMessageDialog(null,"Nom de salle vide","Nom de salle incorrecte", JOptionPane.ERROR_MESSAGE);
				}
			}
			//Si on clique sur Annuler alors on quitte la JFRAME
			if(e.getSource() == Annuler_B) {
				_parent.dispose();
			}
			
		}
		
	}
	
    private void initComponents() {

        Salle_TF = new javax.swing.JTextField();
        Salle_L = new javax.swing.JLabel();
        Confirmer_B = new javax.swing.JButton();
        Annuler_B = new javax.swing.JButton();

        Salle_L.setText("Salle");

        Confirmer_B.setText("Confirmer");
       
        Annuler_B.setText("Annuler");
        
        Confirmer_B.addActionListener(new AjouterSalleListener());
        Annuler_B.addActionListener(new AjouterSalleListener());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Salle_L, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Annuler_B)
                        .addGap(18, 18, 18)
                        .addComponent(Confirmer_B))
                    .addComponent(Salle_TF))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Salle_TF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Salle_L))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Confirmer_B)
                    .addComponent(Annuler_B))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }
}
