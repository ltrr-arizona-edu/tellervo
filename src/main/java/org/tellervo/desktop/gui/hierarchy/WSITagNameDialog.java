package org.tellervo.desktop.gui.hierarchy;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSITag;
import org.tellervo.schema.WSITag.AssignedTo;
import org.tridas.interfaces.ITridasSeries;

public class WSITagNameDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox tagField;
	private WSITag tag;
	private boolean changed = false;
	private ArrayList<String> existingPersonalTagStrings = new ArrayList<String>();
	private ArrayList<WSITag> existingPersonalTags = new ArrayList<WSITag>();
	private ArrayList<String> existingSharedTagStrings = new ArrayList<String>();
	private ArrayList<WSITag> existingSharedTags = new ArrayList<WSITag>();
	private JRadioButton radPersonal;
	private JRadioButton radShared;
	private DefaultComboBoxModel model ;
	private static final String COMMIT_ACTION = "commit";
	private boolean autocomplete = false;
	
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public WSITagNameDialog(WSITag tag) {
		
		setupGUI(tag);		
	}
	
	public WSITagNameDialog(WSITag tag, Boolean autocomplete) {

		this.autocomplete = autocomplete; 
		setupGUI(tag);
	}
	
	private void populateExistingTagLists()
	{
		
		SearchParameters search = new SearchParameters(SearchReturnObject.TAG);
		search.addSearchForAll();
		
		EntitySearchResource<WSITag> searchResource = new EntitySearchResource<WSITag>(search, WSITag.class);
		
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(this, searchResource);
		searchResource.query();
		dlg.setVisible(true);
			
		if(!dlg.isSuccessful()) 
		{
			// Search failed
			return;
		} 
		else 
		{
			// Search successful
			List<WSITag> foundEntities = (List<WSITag>) searchResource.getAssociatedResult();
			
			for(WSITag tag : foundEntities)
			{
				if(tag.isSetOwnerid())
				{
			        existingPersonalTagStrings.add(tag.getValue());
			        existingPersonalTags.add(tag);

				}
				else
				{
					existingSharedTagStrings.add(tag.getValue());
					existingSharedTags.add(tag);

				}

			}
		}
	}
	
	private void setupGUI(WSITag tag)
	{
		this.tag =tag;
		
		
		if(autocomplete)
		{
			populateExistingTagLists();
		}
		
		
		this.setTitle("Series Tag");
		this.setIconImage(Builder.getApplicationIcon());
		this.setModal(true);
		setBounds(100, 100, 450, 145);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow,right]", "[21.00][][]"));
		{
			model = new DefaultComboBoxModel();
			
			tagField = new JComboBox(model);
			tagField.setEditable(true);
			AutoCompleteDecorator.decorate(tagField);				
			contentPanel.add(tagField, "cell 0 1,growx");
		}

		{
			radPersonal = new JRadioButton("Personal");
			radPersonal.setSelected(true);
			radPersonal.setActionCommand("TagTypeSelected");
			radPersonal.addActionListener(this);
			contentPanel.add(radPersonal, "flowx,cell 0 2");
		}
		radShared = new JRadioButton("Shared");
		radShared.setActionCommand("TagTypeSelected");
		radShared.addActionListener(this);
		contentPanel.add(radShared, "cell 0 2,alignx right");
		ButtonGroup group = new ButtonGroup();

		group.add(radPersonal);

		group.add(radShared);
		
		radShared.setEnabled(App.isAdmin);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);

				buttonPane.add(cancelButton);
			}
		}
		
		

		setupAutocomplete();
		if(tag!=null)
		{
			tagField.setSelectedItem(tag.getValue());
			
			if(tag.isSetOwnerid())
			{
				this.radPersonal.setSelected(true);
			}
			else
			{
				this.radShared.setSelected(true);
			}
			
			((JTextField)tagField.getEditor().getEditorComponent()).setCaretPosition(tag.getValue().length()-1);

		}
		
		tagField.requestFocusInWindow();
		
	}
		
	private void setupAutocomplete()
	{
		if(!autocomplete) return;
		
		if(this.radPersonal.isSelected())
		{
			model = new DefaultComboBoxModel(this.existingPersonalTagStrings.toArray(new String[existingPersonalTagStrings.size()]));
		}
		else
		{
			model = new DefaultComboBoxModel(this.existingSharedTagStrings.toArray(new String[existingSharedTagStrings.size()]));
		}
		
		this.tagField.setModel(model);
		this.tagField.setSelectedItem(null);
		
	}
	
	private void generateTag()
	{
		String tagtext = this.tagField.getSelectedItem().toString();
		if(tagtext.trim().length()==0) 
		{
			this.tag = null;
			return;
		}
		
		if(this.tag==null)
		{
			this.tag = new WSITag();
		}
		
		this.tag.setValue(this.tagField.getSelectedItem().toString());
				
		if(this.radPersonal.isSelected())
		{
			this.tag.setOwnerid(App.currentUser.getId());
			
		}
		else
		{
			this.tag.setOwnerid(null);
		}
	}
	
	public WSITag getWSITag()
	{
		return this.tag;
		
	}



	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK"))
		{
			changed= true;
			generateTag();
			this.setVisible(false);
		}
		else if (evt.getActionCommand().equals("Cancel"))
		{
			dispose();
		}
		else if (evt.getActionCommand().equals("TagTypeSelected"))
		{
			this.setupAutocomplete();
		}
	}
	
	public boolean isChanged()
	{
		return changed;
	}
	
	/**
	 * Show dialog for tagging the specified series
	 * 
	 * @param series
	 */
	public static WSITag addTagToSeries(Component parent, ITridasSeries series)
	{
		ArrayList<ITridasSeries> seriesList = new ArrayList<ITridasSeries>();
		seriesList.add(series);
		
		return addTagToSeries(parent, seriesList);
		
		
	}
	/**
	 * Show dialog for tagging the specified list of series
	 * 
	 * @param series
	 */
	public static WSITag addTagToSeries(Component parent, List<ITridasSeries> seriesList)
	{
		if(seriesList==null) return null;
		
		
		WSITagNameDialog dialog = new WSITagNameDialog(null, true);
		
		dialog.setLocationRelativeTo(parent);
		dialog.setVisible(true);
		
		if(!dialog.isChanged()) return null;
		
		WSITag newtag = dialog.getWSITag();
	
		if(newtag==null) return null;
		
		AssignedTo at = new AssignedTo();
		ArrayList<AssignedTo.MeasurementSeries> msl = new ArrayList<AssignedTo.MeasurementSeries>();
		
		for(ITridasSeries tms : seriesList)
		{
			AssignedTo.MeasurementSeries n = new AssignedTo.MeasurementSeries();
			n.setId(tms.getIdentifier().getValue());
			msl.add(n);
		}
		
		at.setMeasurementSeries(msl);
		newtag.setAssignedTo(at);
		
		EntityResource<WSITag> resource = new EntityResource<WSITag>(newtag, TellervoRequestType.CREATE, WSITag.class);
		
		// set up a dialog...
		TellervoResourceAccessDialog dialog2 = TellervoResourceAccessDialog.forWindow(null, resource);
	
		resource.query();
		dialog2.setVisible(true);
		if(!dialog2.isSuccessful()) 
		{ 
			Alert.error("Error", dialog2.getFailException().getMessage());
		}
		
		return newtag;
		
	}

	


}
