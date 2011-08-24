package edu.cornell.dendro.corina.gis;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import edu.cornell.dendro.corina.ui.Builder;

public class AddGISDataDialog extends JDialog implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame parent;
	private GISPanel wwMapPanel;
	
	/**
	 * Create the panel.
	 * @param worldWindowGLCanvas 
	 */
	public AddGISDataDialog(GISPanel wwMapPanel, JFrame parent) {
		
		this.setIconImage(Builder.getApplicationIcon());
		this.setModal(true);
		
		
		this.wwMapPanel = wwMapPanel;
		this.parent = parent;
		getContentPane().setLayout(new MigLayout("", "[65.00px][grow]", "[][][grow][]"));
		
		JLabel lblAddLayer = new JLabel("Add:");
		getContentPane().add(lblAddLayer, "cell 0 0,alignx trailing");
		
		JComboBox cboAddType = new JComboBox();
		cboAddType.setModel(new DefaultComboBoxModel(new String[] {"All ITRDB sites", "All Corina objects", "All Corina elements for an object", "Specific Corina object", "Specific Corina element", "ESRI Shapefile"}));
		getContentPane().add(cboAddType, "cell 1 0,growx");
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, "cell 0 3 2 1,alignx right,growy");
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setActionCommand("add");
		btnAdd.addActionListener(this);
		panel_1.add(btnAdd);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("cancel");
		btnCancel.addActionListener(this);
		panel_1.add(btnCancel);
		
		pack();
		this.setLocationRelativeTo(parent);
		
		

		
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getActionCommand().equals("add"))
		{
			try{
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				wwMapPanel.addLayer(ITRDBMarkerLayerBuilder.createITRDBLayer());
			} finally {
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			dispose();
		}
		else if (event.getActionCommand().equals("cancel"))
		{
			dispose();
		}
		
	}

}
