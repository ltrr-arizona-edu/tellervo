package org.tellervo.desktop.gui.hierarchy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.AbstractListModel;

import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.desktop.wsi.tellervo.resources.WSITagResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.WSITag;
import org.tellervo.schema.WSITag.AssignedTo;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasSample;

import net.miginfocom.swing.MigLayout;

public class AddRemoveWSITagDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JList lstTags;
	private DefaultListModel model;
	private boolean changed = false;
	private ArrayList<WSITag> removedTags = new ArrayList<WSITag>();
	private ITridasSeries series;
	
	/**
	 * Create the dialog.
	 */
	public AddRemoveWSITagDialog(ITridasSeries series) {
		
		this.series = series;
		

		if(series == null)
		{
			Alert.message("", "The series is null");
			dispose();
		}
		
		setupGUI();
		populateList();
	}

	private void populateList()
	{
		SearchParameters param = new SearchParameters(SearchReturnObject.TAG);
    	param.addSearchConstraint(SearchParameterName.SERIESID, SearchOperator.EQUALS, series.getIdentifier().getValue().toString());

		EntitySearchResource<WSITag> resource = new EntitySearchResource<WSITag>(param, WSITag.class);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("Failed to get tags for series");
			return;
		}
		
		List<WSITag> list =  resource.getAssociatedResult();
		
		if(list==null || list.size()==0)
		{
			Alert.message("", "This series has no tags to remove");
			dispose();
		}
		
		
		
		model = new DefaultListModel();
	
		
		for(WSITag tag : list)
		{
			model.addElement(tag);
		}
		
		lstTags.setModel(model);
	}
	
	private void setupGUI()
	{
		this.setModal(true);
		setTitle("Unassign Tags");
		this.setIconImage(Builder.getApplicationIcon());
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[436px,grow,fill][]", "[228px,top][228px,grow,fill]"));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 0 1 2,grow");
			{
				lstTags = new JList();
				
				scrollPane.setViewportView(lstTags);
				lstTags.setBackground(Color.WHITE);
				lstTags.setForeground(Color.BLACK);
				lstTags.setCellRenderer(new WSITagComboRenderer());
				
			}
		}
		{
			JButton btnAdd = new JButton("");
			btnAdd.setIcon(Builder.getIcon("edit_add.png", 16));
			btnAdd.addActionListener(this);
			btnAdd.setActionCommand("addTag");
			contentPanel.add(btnAdd, "flowy,cell 1 0");
		}
		{
			JButton btnRemove = new JButton("");
			btnRemove.setIcon(Builder.getIcon("cancel.png", 16));
			btnRemove.addActionListener(this);
			btnRemove.setActionCommand("removeTag");
			contentPanel.add(btnRemove, "cell 1 0");
		}
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
	}
	

	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK"))
		{
			commit();
			
			dispose();
			
		}
		else if (evt.getActionCommand().equals("Cancel"))
		{
			dispose();
		}
		else if (evt.getActionCommand().equals("removeTag"))
		{
			removeSelectedTag();
		}
		else if (evt.getActionCommand().equals("addTag"))
		{
			WSITag tag = WSITagNameDialog.addTagToSeries(this, series);
			
			if(tag!=null)
			{
				model.addElement(tag);
			}
		}
	}
	
	private void commit()
	{
		
		AssignedTo assignedto = new AssignedTo();
		
		ArrayList<AssignedTo.MeasurementSeries> mslist = new ArrayList<AssignedTo.MeasurementSeries>();
		AssignedTo.MeasurementSeries ms = new AssignedTo.MeasurementSeries();
		ms.setId(series.getIdentifier().getValue());
		mslist.add(ms);
		assignedto.setMeasurementSeries(mslist);
		
		for(WSITag tag : removedTags)
		{
		
			tag.setAssignedTo(assignedto);					
			
			// Create resource
			WSITagResource resource = new WSITagResource(tag, TellervoRequestType.UNASSIGN);
	
			// set up a dialog...
			TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(this, resource);
	
			resource.query();
			dialog.setVisible(true);
			if(!dialog.isSuccessful()) 
			{ 
				Alert.error("Error", dialog.getFailException().getMessage());
				return;
			}
		}

	}
	
	private void removeSelectedTag()
	{
		if(lstTags.getSelectedIndex()>=0)
		{
			int ind = lstTags.getSelectedIndex();
			removedTags.add((WSITag) model.get(ind));
			model.remove(ind);
		}
	}

	
	
	
	public static void showDialog(Component parent, ITridasSeries series)
	{
		
    	

		AddRemoveWSITagDialog dialog2 = new AddRemoveWSITagDialog(series);
		
		dialog2.setLocationRelativeTo(parent);
		dialog2.setVisible(true);
	}

	
	
}
