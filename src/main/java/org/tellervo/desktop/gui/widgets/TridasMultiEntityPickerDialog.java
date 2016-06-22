package org.tellervo.desktop.gui.widgets;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.curation.BoxCuration;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.TridasManipUtil;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import java.awt.BorderLayout;

public class TridasMultiEntityPickerDialog extends JDialog implements TridasSelectListener, ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JLabel tblTotal;
	private JButton btnOK;
	private JButton btnCancel;
	private JPanel panelList;
	private JList<ITridas> lstEntities;
	private DefaultListModel<ITridas> lstModel;
	
	private JScrollPane scrollPane;
	private Class<? extends ITridas> clazz;
	private final static Logger log = LoggerFactory.getLogger(BoxCuration.class);
	private JLabel lblEntitiesSelected;
	private JButton btnRemove;

	
	public TridasMultiEntityPickerDialog(Window parent, String title,
			Class<? extends ITridas> clazz) {
		getContentPane().setLayout(new MigLayout("", "[309.00px,grow][]", "[73.00px][][94.00,grow][37px]"));
		
		this.clazz = clazz; 
		
		lblEntitiesSelected = new JLabel("Selected:");
		getContentPane().add(lblEntitiesSelected, "cell 0 1");
		
		panelList = new JPanel();
		getContentPane().add(panelList, "cell 0 2,grow");
		panelList.setLayout(new BorderLayout(0, 0));
		
		scrollPane = new JScrollPane();
		panelList.add(scrollPane);
		
		lstEntities = new JList<ITridas>();
		lstEntities.setCellRenderer(new TridasListCellRenderer());
		lstModel = new DefaultListModel<ITridas>();
		lstEntities.setModel(lstModel);
		scrollPane.setViewportView(lstEntities);
		
		btnRemove = new JButton("");
		btnRemove.setIcon(Builder.getIcon("cancel.png", 16));
		btnRemove.addActionListener(this);
		btnRemove.setActionCommand("RemoveEntity");
		getContentPane().add(btnRemove, "cell 1 2,aligny top");
		
		JPanel panelButtons = new JPanel();
		getContentPane().add(panelButtons, "cell 0 3 2 1,growx,aligny top");
		panelButtons.setLayout(new MigLayout("", "[grow][76px]", "[25px]"));
		
		tblTotal = new JLabel("");
		panelButtons.add(tblTotal, "cell 0 0");
		
		btnOK = new JButton("OK");
		btnOK.setActionCommand("OK");
		btnOK.addActionListener(this);
		
		panelButtons.add(btnOK, "flowx,cell 1 0,alignx left,aligny top");
		
		btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panelButtons.add(btnCancel, "cell 1 0");
		
		setTitle("Entity Picker");
		TridasEntityPickerPanel pickerpanel = new TridasEntityPickerPanel(this, clazz, EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT);
		getContentPane().add(pickerpanel, "cell 0 0 2 1,alignx left,growy");
		setIconImage(Builder.getApplicationIcon());

        setTitle(title);
        setLocationRelativeTo(parent);
        pack();
    
        pickerpanel.addTridasSelectListener(this);
        pickerpanel.setShutdownOnSelect(false);
        setModal(true);
        
        
        setVisible(true); // blocks until user brings dialog down...
	}


	public ArrayList<ITridas> getEntities() {
		
		ArrayList<ITridas> items = new ArrayList<ITridas>();
		
		for(int i=0; i<lstModel.getSize(); i++)
		{
			ITridas item = (ITridas) lstModel.getElementAt(i);
			items.add(item);
		}
		
		return items;
	}
	
	@Override
	public void entitySelected(TridasSelectEvent event) {
		try {
			ITridas entity = event.getEntity();
			
			SearchReturnObject returnObject;
			if(clazz.equals(TridasObject.class))
			{
				returnObject = SearchReturnObject.OBJECT;
			}
			else if(clazz.equals(TridasElement.class))
			{
				returnObject = SearchReturnObject.ELEMENT;
			}
			else if(clazz.equals(TridasSample.class))
			{
				returnObject = SearchReturnObject.SAMPLE;
			}
			else
			{
				log.error("Unsupported class type");
				return;
			}
			
			
			Collection<ITridas> entityList = getFullTridasRepresentations(entity, returnObject);
				
			if(entityList==null || entityList.size()==0)
			{
				log.warn("No entities returned");
			}
			else
			{	
				for(ITridas item : entityList)
				{
					lstModel.addElement(item);
				}
			}
			
			
		} catch (Exception e) {
			
			log.error("Error getting full representation");
			e.printStackTrace();

		}
		
		
	}
	
	
	private Collection<ITridas> getFullTridasRepresentations(ITridas entity, SearchReturnObject returnClass)
	{
		
		// Set return type to samples
    	SearchParameters param = new SearchParameters(returnClass);
    	
    	// Set search parameters
    	SearchParameterName paramName;
		if (entity instanceof TridasObject)
		{
			paramName = SearchParameterName.OBJECTID;
		}
		else if (entity instanceof TridasElement)
		{
			paramName = SearchParameterName.ELEMENTID;
		}
		else if (entity instanceof TridasSample)
		{
			paramName = SearchParameterName.SAMPLEID;
		}
		else
		{
			log.error("Unsupported class type");
    		return null;
		}
    	param.addSearchConstraint(paramName, SearchOperator.EQUALS, entity.getIdentifier().getValue());

    	// we want a sample returned here
    	EntitySearchResource resource;
    	if(returnClass.equals(SearchReturnObject.OBJECT))
    	{
    		resource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
    	}
    	else if(returnClass.equals(SearchReturnObject.ELEMENT))
    	{
    		resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
    	}
    	else if(returnClass.equals(SearchReturnObject.SAMPLE))
    	{
    		resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
    	}
    	else
    	{
    		log.error("Unsupported return class");
    		return null;
    	}
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.SUMMARY);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting samples");
			return null;
		}
		
			
		List<TridasSample> returnList = (List<TridasSample>) resource.getAssociatedResult();
		if (returnList.size()==0) 
		{
			return null;
		}
		
	
		
		return new ArrayList(returnList);
	}




	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK"))
		{
			setVisible(false);
		}
		else if(evt.getActionCommand().equals("Cancel"))
		{
			lstModel.clear();
			setVisible(false);
		}
		else if (evt.getActionCommand().equals("RemoveEntity"))
		{
	        int index = lstEntities.getSelectedIndices().length - 1;

	        while (lstEntities.getSelectedIndices().length != 0) {
	            lstModel.removeElementAt(lstEntities.getSelectedIndices()[index--]);
	        }
		}
		
	}




}
