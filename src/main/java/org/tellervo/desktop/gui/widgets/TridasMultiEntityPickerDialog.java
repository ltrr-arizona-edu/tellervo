package org.tellervo.desktop.gui.widgets;

import java.awt.Window;
import java.util.ArrayList;

import javax.swing.JDialog;

import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;

public class TridasMultiEntityPickerDialog extends JDialog implements TridasSelectListener{
	public TridasMultiEntityPickerDialog(Window parent, String title,
			Class<? extends ITridas> clazz) {
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new MigLayout("", "[grow][76px]", "[25px]"));
		
		tblTotal = new JLabel("");
		panel.add(tblTotal, "cell 0 0");
		
		btnOK = new JButton("OK");
		panel.add(btnOK, "flowx,cell 1 0,alignx left,aligny top");
		
		btnCancel = new JButton("Cancel");
		panel.add(btnCancel, "cell 1 0");
		
		setTitle("Entity Picker");
		TridasEntityPickerPanel pickerpanel = new TridasEntityPickerPanel(this, clazz, EntitiesAccepted.SPECIFIED_ENTITY_ONLY);
		getContentPane().add(pickerpanel, BorderLayout.CENTER);
		setIconImage(Builder.getApplicationIcon());

        setTitle(title);
        setLocationRelativeTo(parent);
        pack();
    
        pickerpanel.addTridasSelectListener(this);
        pickerpanel.setShutdownOnSelect(false);
        setModal(true);
        
        
        setVisible(true); // blocks until user brings dialog down...
	}

	private static final long serialVersionUID = 1L;
	private ArrayList<ITridas> entities= new ArrayList<ITridas>();
	private JLabel tblTotal;
	private JButton btnOK;
	private JButton btnCancel;




	@Override
	public void entitySelected(TridasSelectEvent event) {
		try {
			ArrayList<ITridas> entitylist = event.getEntityList();
			entities.addAll(entitylist);
			
		} catch (Exception e) {

			
			
		}
		
		
	}
	

}
