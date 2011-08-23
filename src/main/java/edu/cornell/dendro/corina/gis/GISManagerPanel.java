package edu.cornell.dendro.corina.gis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class GISManagerPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GISPanel gispanel;
	
	/**
	 * Create the panel.
	 * @param worldWindowGLCanvas 
	 */
	public GISManagerPanel(GISPanel panel) {
		
		this.gispanel = panel;
		setLayout(new MigLayout("", "[245px][grow]", "[25px][]"));
		
		JButton btnITRDB = new JButton("Add ITRDB site locations layer");
		
		btnITRDB.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				gispanel.addLayer(ITRDBMarkerLayerBuilder.createITRDBLayer());
				
			}
			
		});
		
		add(btnITRDB, "cell 1 0,alignx left,aligny top");
		
		JLabel lblAddLayer = new JLabel("Add layer:");
		add(lblAddLayer, "cell 0 1,alignx trailing");
		
		JComboBox cboAddType = new JComboBox();
		cboAddType.setModel(new DefaultComboBoxModel(new String[] {"All ITRDB sites", "All Corina objects", "All Corina elements for an object", "Specific Corina object", "Specific Corina element"}));
		add(cboAddType, "cell 1 1,growx");
		
		

		
		
	}

}
