/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.JWindow;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.desktop.gui.hierarchy.TridasTreeViewPanel;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.ArrayListModel;
import org.tellervo.desktop.util.labels.ui.TridasListCellRenderer;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


/**
 * This is a dialog that asks the user to specify a TRiDaS entity by scanning a barcode or typing 
 * a lab code.  
 * 
 * @author pwb48
 *
 */
public class TridasEntityChooser extends JDialog implements ActionListener, TridasSelectListener{
	
	private final static Logger log = LoggerFactory.getLogger(TridasEntityChooser.class);
	private static final long serialVersionUID = 3079094155424090769L;
	private CorinaCodePanel codePanel;
	private final Class<? extends ITridas> expectedClass;
	private final EntitiesAccepted entitiesAccepted;
	private JButton okButton;
	private JButton cancelButton;
	private ITridas entity;
	private JTextPane lblWarning;
	private Window parent;
	private JRadioButton radLabcode;
	private JRadioButton radHierarchy;
	private JPanel panelLabcode;
	private JPanel panelHierarchy;
	private JComboBox cboObject;
	private JComboBox cboElement;
	private JComboBox cboSample;
	private JComboBox cboRadius;
	private JComboBox cboSeries;	
	private JLabel lblObject;
	private JLabel lblElement;
	private JLabel lblSample;
	private JLabel lblRadius;
	private JLabel lblSeries;
	private Integer retryCount = 0;

	protected ArrayListModel<TridasObject> objModel;
	protected ArrayListModel<TridasElement> elModel;
	protected ArrayListModel<TridasSample> sampModel;
	protected ArrayListModel<TridasRadius> radiusModel;
	protected ArrayListModel<TridasMeasurementSeries> seriesModel;
	private JButton btnSelectO;
	private JButton btnSelectE;
	private JButton btnSelectS;
	private JButton btnSelectR;

	
	public static enum EntitiesAccepted {
		SPECIFIED_ENTITY_ONLY,
		SPECIFIED_ENTITY_UP_TO_PROJECT,
		SPECIFIED_ENTITY_DOWN_TO_SERIES,
		ALL
	}
	
	/**
	 * Create the dialog.
	 * @wbp.parser.constructor
	 */
	public TridasEntityChooser(Window parent){
		super(parent);
		this.parent = parent;
		this.setModal(true);
		entitiesAccepted = EntitiesAccepted.ALL;
		expectedClass = TridasObject.class;
		setupGui();

	}
	
	/**
	 * Standard constructor. Specify the type of class you are expecting to get back from the user and
	 * whether you'll accept this type only, all entities higher or lower in hierarchy, or all classes
	 * at all. 
	 * 
	 * @param parent
	 * @param expectedClazz
	 * @param ea
	 */
	public TridasEntityChooser(Window parent, Class<? extends ITridas> expectedClazz, EntitiesAccepted ea){

		super(parent);
		this.parent = parent;
		this.setModal(true);


		entitiesAccepted = ea;
		expectedClass = expectedClazz;
		setupGui();
	}
		

	/**
	 * Get the entity specified by the user
	 * 
	 * @return
	 */
	public ITridas getEntity()
	{		
		return entity;
	}
	
	/**
	 * Display panels depending on radio button selection
	 */
	private void setPanelToView()
	{
		if(radLabcode.isSelected())
		{
			panelLabcode.setVisible(true);
			panelHierarchy.setVisible(false);
		}
		else
		{
			panelLabcode.setVisible(false);
			panelHierarchy.setVisible(true);

			// Hide/show combos depending on what level of entity we are returning
			btnSelectO.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasElement.class));
			cboElement.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasElement.class));
			lblElement.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasElement.class));
			
			btnSelectE.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasSample.class));
			cboSample.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasSample.class));
			lblSample.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasSample.class));
			
			btnSelectS.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasRadius.class));
			cboRadius.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasRadius.class));
			lblRadius.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasRadius.class));
			
			btnSelectR.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasMeasurementSeries.class));
			lblSeries.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasMeasurementSeries.class));
			cboSeries.setVisible(isClassEqualOrMoreSeniorThanRequested(TridasMeasurementSeries.class));

		}
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
	
	
	/**
	 * Set up the GUI components
	 */
	private void setupGui(){

		setBounds(100, 100, 450, 300);
		this.getRootPane().setLayout(new BorderLayout());
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			this.getRootPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		{
			JPanel panel = new JPanel();
			this.getRootPane().add(panel, BorderLayout.CENTER);
			panel.setLayout(new MigLayout("", "[140px:140.00px:140px][400px:400px:800px,grow]", "[][25.00:25.00:25.00][25.00:25.00:25.00][grow]"));
			{
				radLabcode = new JRadioButton("Pick using barcode or labcode");
				radLabcode.setSelected(true);
				radLabcode.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setPanelToView();
						
					}
					
				});
				panel.add(radLabcode, "cell 1 1");
			}
			{
				radHierarchy = new JRadioButton("Pick from hierarchy");
				panel.add(radHierarchy, "cell 1 2");
				radHierarchy.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent arg0) {
						setPanelToView();
						
					}
					
				});
			}
			{
				JLayeredPane layeredPane = new JLayeredPane();
				panel.add(layeredPane, "cell 1 3,grow");
				layeredPane.setLayout(new BorderLayout(0, 0));
				panelLabcode = new JPanel();
				layeredPane.add(panelLabcode, BorderLayout.NORTH);
				panelLabcode.setLayout(new MigLayout("", "[252px,grow]", "[21.00px]"));
				codePanel = new CorinaCodePanel(this);
				panelLabcode.add(codePanel, "cell 0 0,grow");
				
				codePanel.addTridasSelectListener(this);
				codePanel.setFocus();
				{
					panelHierarchy = new JPanel();
					layeredPane.add(panelHierarchy, BorderLayout.CENTER);
					panelHierarchy.setLayout(new MigLayout("", "[right][grow][]", "[][][][][]"));
					{
						lblObject = new JLabel("Object:");
						panelHierarchy.add(lblObject, "cell 0 0,alignx trailing");
					}
					{
						cboObject = new JComboBox();
				    	//cboObject.setModel(objModel);
				    	cboObject.addActionListener(this);
						populateObjectCombo();
						panelHierarchy.add(cboObject, "cell 1 0,growx");
					}
					{
						btnSelectO = new JButton("Select");
						btnSelectO.addActionListener(this);
						panelHierarchy.add(btnSelectO, "cell 2 0");
					}
					{
						lblElement = new JLabel("Element:");
						panelHierarchy.add(lblElement, "cell 0 1,alignx trailing");
					}
					{
						cboElement = new JComboBox();
						cboElement.setEnabled(false);
						//cboElement.setModel(elModel);
						cboElement.addActionListener(this);
						cboElement.setRenderer(new TridasListCellRenderer());
						panelHierarchy.add(cboElement, "cell 1 1,growx");
					}
					{
						btnSelectE = new JButton("Select");
						btnSelectE.setEnabled(false);
						btnSelectE.addActionListener(this);
						panelHierarchy.add(btnSelectE, "cell 2 1");
					}
					{
						lblSample = new JLabel("Sample:");
						panelHierarchy.add(lblSample, "cell 0 2,alignx trailing");
					}
					{
						cboSample = new JComboBox();
						//cboSample.setModel(sampModel);
						cboSample.setRenderer(new TridasListCellRenderer());
						cboSample.setEnabled(false);
						panelHierarchy.add(cboSample, "cell 1 2,growx");
					}
					{
						btnSelectS = new JButton("Select");
						btnSelectS.setEnabled(false);
						btnSelectS.addActionListener(this);
						panelHierarchy.add(btnSelectS, "cell 2 2");
					}
					{
						lblRadius = new JLabel("Radius:");
						panelHierarchy.add(lblRadius, "cell 0 3,alignx trailing");
					}
					{
						cboRadius = new JComboBox();
						//cboRadius.setModel(radiusModel);
						cboRadius.setRenderer(new TridasListCellRenderer());
						cboRadius.setEnabled(false);
						panelHierarchy.add(cboRadius, "cell 1 3,growx");
					}
					{
						btnSelectR = new JButton("Select");
						btnSelectR.setEnabled(false);
						btnSelectR.addActionListener(this);
						panelHierarchy.add(btnSelectR, "cell 2 3");
					}
					{
						lblSeries = new JLabel("Series:");
						panelHierarchy.add(lblSeries, "cell 0 4,alignx trailing");
					}
					{
						cboSeries = new JComboBox();
						//cboSeries.setModel(seriesModel);
						cboSeries.setRenderer(new TridasListCellRenderer());
						cboSeries.setEnabled(false);
						cboSeries.addActionListener(this);
						panelHierarchy.add(cboSeries, "cell 1 4,growx");
					}
				}
			}
			{
				JPanel panelIcon = new JPanel();
				panelIcon.setBorder(null);
				panel.add(panelIcon, "cell 0 0 1 4,alignx center,aligny top");
				{
					JLabel lblIcon = new JLabel("");
					lblIcon.setIcon(Builder.getIcon("barcode.png", 128));
					panelIcon.add(lblIcon);
				}
			}
			{
				lblWarning = new JTextPane();
				lblWarning.setEditable(false);
				lblWarning.setVisible(false);
				lblWarning.setForeground(Color.RED);
				lblWarning.setBackground(null);
				lblWarning.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
				panel.add(lblWarning, "cell 1 0");
			}
		}
		
		// Put radio buttons in a group
		ButtonGroup group = new ButtonGroup();
		group.add(radLabcode);
		group.add(radHierarchy);
		setPanelToView();
		
		
		this.setLocationRelativeTo(parent);
		
		this.radHierarchy.setSelected(true);
		this.pack();
		this.radLabcode.setSelected(true);

	}

	
	/**
	 * Set the entity to return from the hierarchy combo boxes
	 */
	private void setEntityFromHierarchy()
	{
		if(this.cboSeries.isEnabled())
		{
			if(cboSeries.getSelectedItem()!=null)
			{
				entity = (ITridas) cboSeries.getSelectedItem();
				return;
			}
		}
		
		if(this.cboRadius.isEnabled())
		{
			if(cboRadius.getSelectedItem()!=null)
			{
				entity = (ITridas) cboRadius.getSelectedItem();
				return;
			}
		}
		
		if(this.cboSample.isEnabled())
		{
			if(cboSample.getSelectedItem()!=null)
			{
				entity = (ITridas) cboSample.getSelectedItem();
				return;
			}
		}
		
		if(this.cboElement.isEnabled())
		{
			if(cboElement.getSelectedItem()!=null)
			{
				entity = (ITridas) cboElement.getSelectedItem();
				return;
			}
		}
		
		if(this.cboObject.isEnabled())
		{
			if(cboObject.getSelectedItem()!=null)
			{
				entity = (ITridas) cboObject.getSelectedItem();
				return;
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel"))
		{
			entity = null;
			dispose();
		}
		else if (e.getActionCommand().equals("OK"))
		{
			if(this.radLabcode.isSelected())
			{
				// Entity selected from lab/barcode
				codePanel.processLabCode();
			}
			else
			{
				// Entity specified by combo boxes
				setEntityFromHierarchy();
				returnEntity();
			}
		}
		else if (e.getSource().equals(btnSelectO))
		{
			// Object selected so populate element combo
			if(cboElement.isVisible()) 
			{
				cboElement.setEnabled(true);
				btnSelectE.setEnabled(true);
				populateElementCombo();
			}
		}
		else if (e.getSource().equals(btnSelectE))
		{
			// Element selected so populate sample combo
			if(cboSample.isVisible()) 
			{
				cboSample.setEnabled(true);
				btnSelectS.setEnabled(true);
				populateSampleCombo();
			}
		}
		else if (e.getSource().equals(btnSelectS))
		{
			// Sample selected so populate radius combo
			if(cboRadius.isVisible()) 
			{
				cboRadius.setEnabled(true);
				btnSelectR.setEnabled(true);
				populateRadiusCombo();
			}
		}
		else if (e.getSource().equals(cboObject))
		{
			log.debug("cboObject clicked");
			log.debug("Combo has "+cboObject.getItemCount() + " items");
		}
		else if (e.getSource().equals(cboElement))
		{
			log.debug("cboElement clicked");
			log.debug("Combo has "+cboElement.getItemCount() + " items");
		}
		
	}
	
	
	/**
	 * Show the TridasEntityChooser dialog. Returns a Tridas entity
	 * specified by the user, an this can be of any type.
	 *  
	 * @param parent
	 * @param title
	 * @return
	 */
	public static ITridas showDialog(JWindow parent, String title)
	{
		
        TridasEntityChooser dialog = new TridasEntityChooser(parent);

        dialog.setTitle(title);
        dialog.setVisible(true); // blocks until user brings dialog down...

        return dialog.getEntity();

	}
	
	/**
	 * Show the TridasEntityChooser dialog.  Dialog returns a Tridas entity
	 * of a type specified by the clazz and acceptableType parameters
	 * 
	 * @param parent
	 * @param title
	 * @param clazz
	 * @param acceptabletype
	 * @return
	 */
	public static ITridas showDialog(Window parent, String title,
			Class<? extends ITridas> clazz, EntitiesAccepted acceptabletype)
	{
		
        TridasEntityChooser dialog = new TridasEntityChooser(parent, clazz, acceptabletype);
        dialog.setTitle(title);

        
        dialog.setVisible(true); // blocks until user brings dialog down...
       	dialog.setLocationRelativeTo(parent);

        return dialog.getEntity();

	}


	@Override
	public void entitySelected(TridasSelectEvent event) {
		
		try {
			ArrayList<ITridas> entitylist = event.getEntityList();
			if(entitylist.size()==0)
			{
				setWarning("No records match");
				return;
			}
			else if(entitylist.size()>1)
			{
				setWarning("Ambiguous - multiple matches");
				return;
			}
			
			
			entity = event.getEntity();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		returnEntity();
	}
			
	
	private void returnEntity()
	{

			if(checkEntityIsValid(entity))
			{
				dispose();
			}
			else
			{
				if(entity!=null)
				{
					@SuppressWarnings("unchecked")
					String givenClassName = TridasTreeViewPanel.getFriendlyClassName((Class<ITridas>) entity.getClass()).toLowerCase();
					String expectedClassName = TridasTreeViewPanel.getFriendlyClassName(expectedClass).toLowerCase();

					String modifier = "";			
					
					if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT))
					{
						modifier = " (or above)";
					}
					else if (entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_DOWN_TO_SERIES))
					{
						modifier = " (or below)";
					}
					setWarning("The code you entered was for a TRiDaS " + givenClassName + "\n"+
							"when a TRiDaS "+ expectedClassName + modifier + " was expected");
				}
				else
				{
					setWarning("No records match");
				}
				

			}
			
	
	}
	
	
	/**
	 * Set the warning label.  If you want to remove a warning set to null and the label
	 * will be hidden.
	 * 
	 * @param text
	 */
	private void setWarning(String text)
	{
		// If warning is null then hide, otherwise show
		lblWarning.setVisible(!(text==null));
		
		lblWarning.setText(text);
	}
	
	
	/**
	 * Check that the specified entity is valid in terms of the level
	 * of the class that is expected to be returned.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Boolean checkEntityIsValid(ITridas ent)
	{
		if(ent==null)
		{
			return false;
		}
		else if (expectedClass.equals(ITridas.class))
		{
			return true;
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.ALL))
		{
			return true;
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_ONLY))
		{
			if(ent.getClass().equals(expectedClass) )
			{
				return true;
			}
			else if (ent.getClass().equals(TridasObjectEx.class) && expectedClass.equals(TridasObject.class))
			{
				return true;
			}
			else if (ent.getClass().equals(TridasObject.class) && expectedClass.equals(TridasObjectEx.class))
			{
				return true;
			}
			else
			{
				log.error("Invalid entity - a "+TridasTreeViewPanel.getFriendlyClassName(expectedClass)+" is expected");
				return false;
			}
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_DOWN_TO_SERIES))
		{
			Integer e1 = TridasUtils.getDepth((Class<ITridas>) ent.getClass());
			Integer e2 = TridasUtils.getDepth(expectedClass);
			if(e1==0 || e2==0) return false;
			return e1.compareTo(e2)>=0;
			
		}
		else if(entitiesAccepted.equals(EntitiesAccepted.SPECIFIED_ENTITY_UP_TO_PROJECT))
		{
			Integer e1 = TridasUtils.getDepth((Class<ITridas>) ent.getClass());
			Integer e2 = TridasUtils.getDepth(expectedClass);
			if(e1==0 || e2==0) return false;
			return e1.compareTo(e2)<=0;
		}
		return false;
	}
	
    /**
     * Populate the combo box with available objects
     */
    private void populateObjectCombo()
    {
    	TridasObjectRenderer rend = new TridasObjectRenderer();
    	rend.setMaximumTitleLength(30);
    	cboObject.setRenderer(rend);
    	objModel = new ArrayListModel<TridasObject>(App.tridasObjects.getObjectList());
    	//objModel.setSelectedItem(null);   
    	cboObject.setModel(objModel);
    }
    
    private void populateElementCombo()
    {
    	
    	if(retryCount>=4) {
    		Alert.error("Retry error", "Already tried to populate combo 4 times with no success");
    		retryCount=0;
    		return;
    	}
    	
    	TridasObject obj = null;
    	
    	if (cboObject.getSelectedItem()!=null)
    	{
    		obj = (TridasObject) cboObject.getSelectedItem();
    	}
    	else
    	{
    		log.debug("Can't populate element combo as no object is selected");
    		return;
    	}
    	    	    	
		// Find all elements for an object 
    	SearchParameters param = new SearchParameters(SearchReturnObject.ELEMENT);
    	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue().toString());

    	// we want an object return here, so we get a list of object->elements->samples when we use comprehensive
		EntitySearchResource<TridasElement> resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting elements from the search request");
			return;
		}
		
		List<TridasElement> elList = null;
		try{
			elList = resource.getAssociatedResult();
		} catch (Exception ex)
		{
			log.error("LeAnns bug! "+ ex.getLocalizedMessage());
			retryCount++;
			populateElementCombo();
		}
		
		if(elList==null)
		{
			log.error("Null response from getAssociatedResult");
		}
		if(elList.size()==0)
		{
			log.error("Zero results from getAssociatedResult");

		}
		
		retryCount = 0;
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(elList, numSorter);
		
		
		
		if(elModel!=null) log.debug("Element combo model previously had " + elModel.size() + " entries in it");
			
		
		elModel = new ArrayListModel<TridasElement>(elList);
		cboElement.setModel(elModel);
		    	
		log.debug("Element combo model *now* has " + elModel.size() + " entries in it");
		log.debug("Element combo gui has "+cboElement.getItemCount()+ " entries in it");
		
		
		// Pick first in list
		//if(elModel.size()>0) cboElement.setSelectedIndex(0);
    }
	
    /**
     * Populate combo box with available samples
     */
    private void populateSampleCombo()
    {
    	if(retryCount>=4) {
    		Alert.error("Retry error", "Already tried to populate combo 4 times with no success");
    		retryCount=0;
    		return;
    	}
    	
    	TridasElement el = null;
    	
    	if (cboElement.getSelectedItem()!=null)
    	{
    		el = (TridasElement) cboElement.getSelectedItem();
    	}
    	else
    	{
    		log.debug("Can't populate sample combo as no element is selected");
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
			log.error("Error getting samples from the search request");
			return;
		}
		
		List<TridasSample> sampList = null;
		
		try{
			sampList = resource.getAssociatedResult();
		} catch (Exception e)
		{
			log.error("LeAnns bug! "+e.getLocalizedMessage());
			retryCount++;
			populateSampleCombo();
		}
		
		retryCount=0;
		
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		sampModel.replaceContents(sampList); 	
    	
		// Pick first in list
		if(sampModel.size()>0) cboSample.setSelectedIndex(0);
    	
    	
    }
    
    private void populateRadiusCombo()
    {
    	if(retryCount>=4) {
    		Alert.error("Retry error", "Already tried to populate combo 4 times with no success");
    		retryCount=0;
    		return;
    	}
    	
    	TridasSample samp = null;
    	samp = (TridasSample) cboSample.getSelectedItem();
  	    	
		// Find all radii for an sample 
    	SearchParameters param = new SearchParameters(SearchReturnObject.RADIUS);
    	param.addSearchConstraint(SearchParameterName.SAMPLEDBID, SearchOperator.EQUALS, samp.getIdentifier().getValue().toString());

    	// we want a radius return here
		EntitySearchResource<TridasRadius> resource = new EntitySearchResource<TridasRadius>(param, TridasRadius.class);
		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(resource);
		resource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			log.error("Error getting radii from the search request");
			return;
		}
		
		List<TridasRadius> radiusList = null;
		
		try{
			radiusList = resource.getAssociatedResult();
		} catch (Exception e)
		{
			log.error("LeAnns bug! "+e.getLocalizedMessage());
			retryCount++;
			populateRadiusCombo();
		}
			
		retryCount=0;
		
		/*TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		*/
		radiusModel.replaceContents(radiusList); 	
    	
		// Pick first in list
		if(radiusModel.size()>0) cboRadius.setSelectedIndex(0);
    	
    	
    }
    
    

}
