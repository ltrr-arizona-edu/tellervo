package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.components.table.CurationStatusComboBox;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSICurationEvent;
import org.tridas.schema.TridasSample;

import net.miginfocom.swing.MigLayout;

public class CurationStatusSetterDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private CurationStatusComboBox cboCurationStatus;
	private JTextPane txtNotes;
	private ArrayList<TridasSample> samples;
	
	/**
	 * Create the dialog.
	 */
	public CurationStatusSetterDialog(ArrayList<TridasSample> samples) throws Exception{
		
		if(samples==null || samples.size()==0)
		{
			throw new Exception("CurationStatusSetterDialog requires samples");
		}
		
		
		this.samples = samples;
		setupGui();
		
		
	}
	
	private void setupGui()
	{
		setTitle("Curation Status");
		setIconImage(Builder.getApplicationIcon());
		
		setBounds(100, 100, 446, 223);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][grow]", "[][grow]"));
		{
			JLabel lblNewCurationStatus = new JLabel("New curation status:");
			contentPanel.add(lblNewCurationStatus, "cell 0 0,alignx trailing");
		}
		{
			cboCurationStatus = new CurationStatusComboBox();
			contentPanel.add(cboCurationStatus, "cell 1 0,growx");
		}
		{
			JLabel lblNotes = new JLabel("Notes:");
			contentPanel.add(lblNotes, "cell 0 1,aligny top");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 1 1,grow");
			{
				txtNotes = new JTextPane();
				scrollPane.setViewportView(txtNotes);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOK = new JButton("OK");
				btnOK.setActionCommand("OK");
				btnOK.addActionListener(this);
				buttonPane.add(btnOK);
				getRootPane().setDefaultButton(btnOK);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPane.add(btnCancel);
			}
		}
	}
	
	private void commitChanges()
	{
	    int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to alter the curation status of these samples?", "Are you sure?", JOptionPane.YES_NO_CANCEL_OPTION);
		
	    if(result == JOptionPane.YES_OPTION)
	    {
	    	for(TridasSample s : this.samples)
	    	{
	    		WSICurationEvent curationEvent = new WSICurationEvent();
	    		curationEvent.setSample(s);
	    		curationEvent.setNotes(this.txtNotes.getText());
	    		curationEvent.setStatus((CurationStatus) this.cboCurationStatus.getSelectedItem());
	    		
	    		// Create resource
				EntityResource<WSICurationEvent> resource2 = new EntityResource<WSICurationEvent>(curationEvent, TellervoRequestType.CREATE, WSICurationEvent.class);
				TellervoResourceAccessDialog dialog2 = TellervoResourceAccessDialog.forWindow(this, resource2);

				resource2.query();
				dialog2.setVisible(true);

				if(!dialog2.isSuccessful()) 
				{ 
					Alert.error("Error", dialog2.getFailException().getMessage());
					return;
				}			
				
				dialog2.dispose();
	    		
	    	}
	    }
	    else
	    {
	    	return;
	    }
		
		this.dispose();
		return;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().contentEquals("Cancel"))
		{
			this.dispose();
			return;
		}
		
		if(e.getActionCommand().contentEquals("OK"))
		{
			
			commitChanges();
			
		}
	}

}
