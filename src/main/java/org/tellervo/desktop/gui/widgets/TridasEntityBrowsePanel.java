package org.tellervo.desktop.gui.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.TridasSelectEvent.TridasSelectType;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.ComboBoxFilterable;
import org.tellervo.desktop.ui.FilterableComboBoxModel;
import org.tellervo.desktop.util.ArrayListModel;
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
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import java.awt.Font;

public class TridasEntityBrowsePanel extends JPanel implements ItemListener{

	private static final long serialVersionUID = 1L;
	private ComboBoxFilterable cboObject;
	private JComboBox cboElement;
	private JComboBox cboSample;
	private JComboBox cboRadius;
	private JComboBox cboSeries;
	JLabel lblObject;
	JLabel lblElement;
	JLabel lblSample;
	JLabel lblRadius;
	JLabel lblSeries;
	
	protected FilterableComboBoxModel objModel = new FilterableComboBoxModel();
	protected ArrayListModel<TridasElement> elModel = new ArrayListModel<TridasElement>();
	protected ArrayListModel<TridasSample> sampModel = new ArrayListModel<TridasSample>();
	protected ArrayListModel<TridasRadius> radModel = new ArrayListModel<TridasRadius>();
	protected ArrayListModel<ITridasSeries> seriesModel = new ArrayListModel<ITridasSeries>();
	
	private EventListenerList tridasListeners = new EventListenerList();
	private static final Logger log = LoggerFactory.getLogger(TridasEntityBrowsePanel.class);
	private JButton btnSelect;
	private JLabel lblPrefix;
	private JButton btnReset;
	private final Class<? extends ITridas> expectedClass;
	private final EntitiesAccepted entitiesAccepted;
	
	
	public TridasEntityBrowsePanel() {
		
		expectedClass = TridasObject.class;
		entitiesAccepted = EntitiesAccepted.ALL;
		init();
		
		populateObjectCombo();
		setListenersEnabled(true);
		
	}
	
	public TridasEntityBrowsePanel(Class<? extends ITridas> expectedClass, EntitiesAccepted entitiesAccepted) {
		
		this.expectedClass = expectedClass;
		this.entitiesAccepted = entitiesAccepted;
		init();
		
		populateObjectCombo();
		setListenersEnabled(true);
		
	}
		
	/**
	 * Add a listener 
	 * 
	 * @param listener
	 */
	public void addTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.add(TridasSelectListener.class, listener);
	}
	
	private void init()
	{
		setLayout(new MigLayout("hidemode 3", "[right][::200px,grow,center][::200px,grow,center][::200px,grow,center][::200px,grow,center][::200px,grow,center][]", "[][top][grow]"));
		
		lblPrefix = new JLabel(App.getLabCodePrefix());
		add(lblPrefix, "cell 0 0,alignx trailing");
		
		cboObject = new ComboBoxFilterable();
		add(cboObject, "cell 1 0,growx");
		
        // Set models 
        cboObject.setModel(objModel);	
		
		cboElement = new JComboBox();
		add(cboElement, "cell 2 0,growx");
		cboElement.setModel(elModel);
		
		
		// Set renderers
		cboElement.setRenderer(new TridasListCellRenderer());
		
		
		// Temporary bodge so only populate combo (not free text) search works
		cboElement.setEditable(false);
		
		cboSample = new JComboBox();
		add(cboSample, "cell 3 0,growx");
		cboSample.setModel(sampModel);
		cboSample.setRenderer(new TridasListCellRenderer());
		cboSample.setEditable(false);
		
		cboRadius = new JComboBox();
		add(cboRadius, "cell 4 0,growx");
		cboRadius.setModel(radModel);
		
		cboSeries = new JComboBox();
		add(cboSeries, "cell 5 0,growx");
		
		btnSelect = new JButton("Select");
		add(btnSelect, "cell 6 0,growx");
		btnSelect.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				fireItemSelected();				
			}
			
		});
		
		lblObject = new JLabel("Object");
		lblObject.setFont(new Font("Dialog", Font.PLAIN, 8));
		add(lblObject, "cell 1 1,alignx center");
		
		lblElement = new JLabel("Element");
		lblElement.setFont(new Font("Dialog", Font.PLAIN, 8));
		add(lblElement, "cell 2 1,alignx center");
		
		lblSample = new JLabel("Sample");
		lblSample.setFont(new Font("Dialog", Font.PLAIN, 8));
		add(lblSample, "cell 3 1,alignx center");
		
		lblRadius = new JLabel("Radius");
		lblRadius.setFont(new Font("Dialog", Font.PLAIN, 8));
		add(lblRadius, "cell 4 1,alignx center");
		
		lblSeries = new JLabel("Series");
		lblSeries.setFont(new Font("Dialog", Font.PLAIN, 8));
		add(lblSeries, "cell 5 1,alignx center");
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				populateObjectCombo();	
				cboObject.setSelectedItem(null);
			}
			
		});
		add(btnReset, "cell 6 1,growx");

		setVisibilityBasedOnClasses();
	}

	
	private void setListenersEnabled(Boolean b)
	{	
		if(b)
		{
			cboObject.addItemListener(this);
			cboElement.addItemListener(this);
			cboSample.addItemListener(this);
			cboRadius.addItemListener(this);
		}
		else
		{
			cboObject.removeItemListener(this);
			cboElement.removeItemListener(this);
			cboSample.removeItemListener(this);
			cboRadius.removeItemListener(this);
			
		}
	}
	
	private void enableCombos(Class<? extends ITridas> clazz)
	{
		cboObject.setEnabled(false);
		
		if(clazz.equals(TridasObject.class) || clazz.equals(TridasObjectEx.class))
		{
			cboObject.setEnabled(true);
			cboElement.setEnabled(false);
			cboSample.setEnabled(false);
			cboRadius.setEnabled(false);
			cboSeries.setEnabled(false);
			cboElement.setSelectedItem(null);
			cboSample.setSelectedItem(null);
			cboRadius.setSelectedItem(null);
			cboSeries.setSelectedItem(null);
		}
		else if(clazz.equals(TridasElement.class))
		{
			cboObject.setEnabled(true);
			cboElement.setEnabled(true);
			cboSample.setEnabled(false);
			cboRadius.setEnabled(false);
			cboSeries.setEnabled(false);
			cboSample.setSelectedItem(null);
			cboRadius.setSelectedItem(null);
			cboSeries.setSelectedItem(null);
		}
		else if(clazz.equals(TridasSample.class))
		{
			cboObject.setEnabled(true);
			cboElement.setEnabled(true);
			cboSample.setEnabled(true);
			cboRadius.setEnabled(false);
			cboSeries.setEnabled(false);
			cboRadius.setSelectedItem(null);
			cboSeries.setSelectedItem(null);
		}		
		else if(clazz.equals(TridasRadius.class))
		{
			cboObject.setEnabled(true);
			cboElement.setEnabled(true);
			cboSample.setEnabled(true);
			cboRadius.setEnabled(true);	
			cboSeries.setEnabled(false);
			cboSeries.setSelectedItem(null);
		}	
		
		setVisibilityBasedOnClasses();

	}
	
	private void setVisibilityBasedOnClasses()
	{
		cboObject.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasObject.class));
		lblObject.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasObject.class));
		cboElement.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasElement.class));
		lblElement.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasElement.class));
		cboSample.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasSample.class));
		lblSample.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasSample.class));
		cboRadius.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasRadius.class));
		lblRadius.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasRadius.class));
		cboSeries.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasMeasurementSeries.class));
		lblSeries.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasMeasurementSeries.class));
	}
	
	/**
	 * Function useful for specifying if a combo should be visible or not.
	 * 
	 * @param clazz
	 * @return
	 */
	private Boolean isClassEqualOrMoreSeniorThanRequested(Class<? extends ITridas> clazz)
	{
		if((entitiesAccepted.equals(EntitiesAccepted.ALL)) || 
		   (entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_DOWN_TO_SERIES)))
		{
			return true;
		}
		else if((entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_ONLY)) || 
				(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT)))
		{
			return TridasUtils.getDepth(clazz)<=TridasUtils.getDepth(expectedClass);
		}
		
		return false;
		
	}
	
    private void populateObjectCombo()
    {
    	TridasObjectRenderer rend = new TridasObjectRenderer();
    	rend.setMaximumTitleLength(30);
    	rend.setHideTitles(true);
    	cboObject.setRenderer(rend);
    	objModel = new FilterableComboBoxModel();
    	objModel.addElements(App.tridasObjects.getObjectList());

    	cboObject.setModel(objModel);
    	cboObject.setSelectedItem(null);
    	enableCombos(TridasObject.class);
    }
    
    
    private void populateElementCombo()
    {
    	TridasObject obj = null;
    	
    	if (cboObject.getSelectedItem()!=null)
    	{
    		obj = (TridasObject) cboObject.getSelectedItem();
    	}
    	else
    	{
    		return;
    	}
    	    	    	
		// Find all elements for an object 
    	SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
    	param.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue().toString());

    	// we want an object return here, so we get a list of object->elements->samples when we use comprehensive
		EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting elements");
			return;
		}
		
		List<TridasElement> elList = resource.getAssociatedResult();
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(elList, numSorter);
		
		elModel.replaceContents(elList); 	
    	
		cboElement.setSelectedItem(null);
    	
     	cboElement.setModel(elModel);
    	log.debug("Element combo contains " + cboElement.getItemCount()+" items");
    	
    	enableCombos(TridasElement.class);
    }

    /**
     * Populate combo box with available samples
     */
    private void populateSampleCombo()
    {
    	TridasElement el = null;
    	
    	if (cboElement.getSelectedItem()!=null)
    	{
    		el = (TridasElement) cboElement.getSelectedItem();
    	}
    	else
    	{
    		return;
    	}
    	    
		// Find all samples for an element 
    	SearchParameters param = new SearchParameters(SearchReturnObject.SAMPLE);
    	param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, el.getIdentifier().getValue().toString());

    	// we want a sample return here
		EntitySearchResource<TridasSample> resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting samples");
			return;
		}
		
		List<TridasSample> sampList = resource.getAssociatedResult();
		
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		sampModel.replaceContents(sampList); 	
    	
		// Pick first in list
		cboSample.setSelectedItem(null);
    	
		enableCombos(TridasSample.class);
    }
    
    /**
     * Populate combo box with available radii
     */
    private void populateRadiusCombo()
    {
    	TridasSample smpl = null;
    	
    	if (cboSample.getSelectedItem()!=null)
    	{
    		smpl = (TridasSample) cboSample.getSelectedItem();
    	}
    	else
    	{
    		return;
    	}

		// Find all radii for a sample 
    	SearchParameters param = new SearchParameters(SearchReturnObject.RADIUS);
    	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, smpl.getIdentifier().getValue().toString());

    	// we want radii returned here
		EntitySearchResource<TridasRadius> resource = new EntitySearchResource<TridasRadius>(param, TridasRadius.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("oopsey doopsey.  Error getting radii");
			return;
		}
		
		List<TridasRadius> radiiList = resource.getAssociatedResult();
		
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		radModel.replaceContents(radiiList); 	
		cboRadius.setRenderer(new TridasListCellRenderer());
    	
		cboRadius.setSelectedItem(null);
		
		enableCombos(TridasRadius.class);
    	
    }
    
    private void populateSeriesCombo()
    {
    	
    	
    }
    
	/**
	 * Fire a selected entity event
	 * 
	 * @param event
	 */
	protected void fireTridasSelectListener(TridasSelectEvent event)
	{
	     Object[] listeners = tridasListeners.getListenerList();
	     // loop through each listener and pass on the event if needed
	     Integer numListeners = listeners.length;
	     for (int i = 0; i<numListeners; i+=2) 
	     {
	          if (listeners[i]==TridasSelectListener.class) 
	          {
	               // pass the event to the listeners event dispatch method
	                ((TridasSelectListener)listeners[i+1]).entitySelected(event);
	          }            
	     }

	}
    
	public void setSelectButtonVisible(Boolean b)
	{
		btnSelect.setVisible(b);
	}
	
	public void setResetButtonVisible(Boolean b)
	{
		btnReset.setVisible(b);
	}
	
	public void fireItemSelected()
	{
		ITridas entity = null;
		
		if(cboSeries.getSelectedItem()!=null)
		{
			entity = (ITridas) cboSeries.getSelectedItem();
		}
		else if(cboRadius.getSelectedItem()!=null)
		{
			entity = (ITridas) cboRadius.getSelectedItem();
		}
		else if(cboSample.getSelectedItem()!=null)
		{
			entity = (ITridas) cboSample.getSelectedItem();
		}
		else if(cboElement.getSelectedItem()!=null)
		{
			entity = (ITridas) cboElement.getSelectedItem();
		}
		else if(cboObject.getSelectedItem()!=null)
		{
			entity = (ITridas) cboObject.getSelectedItem();
		}
		else
		{
			return;
		}
		
		this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, entity, TridasSelectType.FORCED));
	}
    


	@Override
	public void itemStateChanged(ItemEvent event) {
		
		this.setListenersEnabled(false);
		if(event.getSource().equals(cboObject))
		{
			populateElementCombo();
		}
		else if(event.getSource().equals(cboElement))
		{
			populateSampleCombo();
		}
		else if(event.getSource().equals(cboSample))
		{
			populateRadiusCombo();
		}
		else if(event.getSource().equals(cboRadius))
		{
			populateSeriesCombo();
		}
		
		this.setListenersEnabled(true);
	}
	


}



