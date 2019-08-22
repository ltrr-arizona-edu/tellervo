package org.tellervo.desktop.labelgen;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.dbbrowse.TridasObjectRenderer;
import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.tridasv2.ui.EntityListComboBox;
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
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.emory.mathcs.backport.java.util.Collections;


public class LGWizardSamplePicker extends AbstractWizardPanel implements ActionListener{


	private static final long serialVersionUID = 1L;
	private JList lstSelected;
	private JList lstAvailable;
	protected ArrayListModel<TridasSample> selModel = new ArrayListModel<TridasSample>();
	protected ArrayListModel<TridasSample> availModel = new ArrayListModel<TridasSample>();
	private JButton btnAddAll;
	private JButton btnAddOne;
	private JButton btnRemoveOne;
	private JButton btnRemoveAll;
	private JPanel panel_1;
	private JLabel lblObject;
	private EntityListComboBox cboObject;
	private JButton btnSearch;
    private final static Logger log = LoggerFactory.getLogger(LGWizardSamplePicker.class);

	
	/**
	 * Create the panel.
	 */
	public LGWizardSamplePicker() {
		super("Step 2 - Which samples?", 
				"The next step is to define which samples you would like labels for.");
		setLayout(new MigLayout("", "[243.00,grow][45.00,center][grow]", "[grow][][grow][53.00,top][grow]"));
		
		panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		add(panel_1, "cell 0 0 3 1,grow");
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][]"));
		
		lblObject = new JLabel("Object:");
		panel_1.add(lblObject, "cell 0 0,alignx trailing");
		
		
		cboObject = new EntityListComboBox(App.tridasObjects.getObjectList());	
    	TridasObjectRenderer rend = new TridasObjectRenderer();
    	cboObject.setRenderer(rend);

		panel_1.add(cboObject, "cell 1 0,growx");
		
		btnSearch = new JButton("Search");
		btnSearch.setActionCommand("Search");
		btnSearch.addActionListener(this);
		panel_1.add(btnSearch, "cell 1 1,alignx right");
		
		JLabel lblAvailable = new JLabel("Available:");
		add(lblAvailable, "cell 0 1");
		
		JLabel lblSelected = new JLabel("Selected:");
		add(lblSelected, "cell 2 1");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 2 1 3,grow");
		
		lstAvailable = new JList();
		scrollPane.setViewportView(lstAvailable);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		add(panel, "cell 1 3,grow");
		panel.setLayout(new MigLayout("", "[fill][]", "[][][][]"));
		
		btnAddAll = new JButton(">>");
		btnAddAll.setActionCommand("AddAll");
		btnAddAll.addActionListener(this);

		panel.add(btnAddAll, "cell 0 0");
		
		btnAddOne = new JButton(">");
		btnAddOne.setActionCommand("AddOne");
		btnAddOne.addActionListener(this);
		panel.add(btnAddOne, "cell 0 1");
		
		btnRemoveOne = new JButton("<");
		btnRemoveOne.addActionListener(this);
		btnRemoveOne.setActionCommand("RemoveOne");

		panel.add(btnRemoveOne, "cell 0 2");
		
		btnRemoveAll = new JButton("<<");
		btnRemoveAll.setActionCommand("RemoveAll");
		btnRemoveAll.addActionListener(this);
		panel.add(btnRemoveAll, "cell 0 3");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		add(scrollPane_1, "cell 2 2 1 3,grow");
		
		lstSelected = new JList();
		scrollPane_1.setViewportView(lstSelected);
		
		availModel = new ArrayListModel<TridasSample>();
		
		lstAvailable.setModel(availModel);
		lstAvailable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		lstAvailable.setCellRenderer(new TridasListCellRenderer());
		
		
		// Set up selected list
		selModel = new ArrayListModel<TridasSample>();

		lstSelected.setModel(selModel);
		lstSelected.setCellRenderer(new TridasListCellRenderer());
		
		
		
		
			
	}
	
	public ArrayList<TridasSample> getSamples()
	{
		ArrayList<TridasSample> selected = new ArrayList<TridasSample>();
		
		for(int i=0; i<selModel.size(); i++)
		{
			selected.add(selModel.get(i));
		}
			
		return selected;
	}

	public void actionPerformed(ActionEvent evt) {

		
		if(evt.getSource() == btnAddOne){

			for (Object obj : lstAvailable.getSelectedValues())
			{
				TridasSample myobj = (TridasSample) obj;
				selModel.add(myobj);
				availModel.remove(myobj);
			}
			
			
		}
			
		if(evt.getSource() == btnRemoveOne)
		{
			for (Object obj : lstSelected.getSelectedValues())
			{
				TridasSample myobj = (TridasSample) obj;
				availModel.add(myobj);
				selModel.remove(myobj);

			}
		}
		
		if(evt.getActionCommand()=="AddAll")
		{
			
			for (int i=0; i<availModel.getSize(); i++) {
				TridasSample myobj = (TridasSample) availModel.get(i);
				selModel.add(myobj);
			}
			availModel.clear();
		}
		
		
		if(evt.getActionCommand()=="RemoveAll")
		{
			for (int i=0; i<selModel.getSize(); i++) {
				TridasSample myobj = (TridasSample) selModel.get(i);
				availModel.add(myobj);
			}
			selModel.clear();
		}
		
		if(evt.getActionCommand().equals("Search"))
		{
			searchForSamples((TridasObject) cboObject.getSelectedItem());
		}
		
	}
	
	private void searchForSamples(TridasObject obj)
	{
		
		if(availModel.size()>0) availModel.clear();
		
		 
		// Find all samples for an object 
    	SearchParameters sampparam = new SearchParameters(SearchReturnObject.SAMPLE);
    	sampparam.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, obj.getIdentifier().getValue().toString());

    	// we want an object return here, so we get a list of object->elements->samples when we use comprehensive
		EntitySearchResource<TridasObject> sampresource = new EntitySearchResource<TridasObject>(sampparam, TridasObject.class);
		sampresource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		
		TellervoResourceAccessDialog dialog = new TellervoResourceAccessDialog(sampresource);
		sampresource.query();	
		dialog.setVisible(true);
		
		if(!dialog.isSuccessful()) 
		{ 
			System.out.println("Error getting samples");
			return;
		}
		
		List<TridasObject> objList = sampresource.getAssociatedResult();
		List<TridasSample> sampList = getSamplesList2(objList, null, null);
		
		TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
		Collections.sort(sampList, numSorter);
		
		availModel.addAll(sampList);
		
		
	}
	
    public static List<TridasSample> getSamplesList2(List<TridasObject> objects, 
    		TridasObject[] objsThisLevel, List<TridasSample> returns) {
    	
       	
    	// create this on the fly
    	if(returns == null)
    		returns = new ArrayList<TridasSample>();
    	
    	for(TridasObject obj : objects) {
    		
    		// handle stupid recursive objects
    		List<TridasObject> currentObjects;    		
    		if(objsThisLevel == null)
    			currentObjects = new ArrayList<TridasObject>();
    		else 
    			currentObjects = new ArrayList<TridasObject>(Arrays.asList(objsThisLevel));
    		
			currentObjects.add(obj);
			
			// grar...
			for(@SuppressWarnings("unused") TridasObject obj2 : obj.getObjects()) {
				getSamplesList2(obj.getObjects(), currentObjects.toArray(new TridasObject[0]), returns);
			}
			
			
			for(TridasElement ele : obj.getElements()) {
				for(TridasSample samp : ele.getSamples()) {
					
					// Set labcode generic field based on preferred style
					String thelabcode = LabCodeFormatter.getDefaultFormattedLabCode(currentObjects, ele, samp, null, null);
					GenericFieldUtils.setField(samp, "tellervo.internal.labcodeText", thelabcode);	
					
					// Also copy across the project ID					
					GenericFieldUtils.setField(samp, "tellervo.internal.projectID", 
							GenericFieldUtils.findField(currentObjects.get(0), "tellervo.object.projectid").getValue());
					
					// And the object name
					GenericFieldUtils.setField(samp, "tellervo.internal.objectID", 
							obj.getIdentifier().getValue());
					
					// add the sample to the returns list
					returns.add(samp);
					
				}
			}
    	}
    	
    	return returns;
    	
    }
	
	
	/**
     * Stupid function to get a list of samples from a list of objects
     * 
     * Has the side effect of setting tellervo.internal.labcode generic field ;)
     * Hack to get lab code!
     * 
     * @param objects
     * @param objsThisLevel
     * @param returns
     * @return
     */
    public static List<TridasSample> getSamplesList(List<TridasObject> objects, 
    		TridasObject[] objsThisLevel, List<TridasSample> returns) {
    	
    	// create this on the fly
    	if(returns == null)
    		returns = new ArrayList<TridasSample>();
    	
    	for(TridasObject obj : objects) {
    		
    		// handle stupid recursive objects
    		List<TridasObject> currentObjects;    		
    		if(objsThisLevel == null)
    			currentObjects = new ArrayList<TridasObject>();
    		else 
    			currentObjects = new ArrayList<TridasObject>(Arrays.asList(objsThisLevel));
    		
			currentObjects.add(obj);
			
			// grar...
			for(@SuppressWarnings("unused") TridasObject obj2 : obj.getObjects()) {
				getSamplesList(obj.getObjects(), currentObjects.toArray(new TridasObject[0]), returns);
			}
			
			for(TridasElement ele : obj.getElements()) {
				for(TridasSample samp : ele.getSamples()) {
					
					// make lab code
					LabCode labcode = new LabCode();
					
					// objects first...
					labcode.appendSiteCode(((TridasObjectEx) currentObjects.get(0)).getLabCode());
					
					// Cornell only wants top level object in lab code. 
					// Make this client selectable before releasing to the world
					
					for(TridasObject obj2 : currentObjects) {
						if(obj2 instanceof TridasObjectEx)
							labcode.appendSiteCode(((TridasObjectEx) obj2).getLabCode());
						else
							labcode.appendSiteCode(obj2.getTitle());
						labcode.appendSiteTitle(obj2.getTitle());
					}
					
					
					labcode.setElementCode(ele.getTitle());
					labcode.setSampleCode(samp.getTitle());
					
					// set the lab code kludgily on the sample
					GenericFieldUtils.setField(samp, "tellervo.internal.labcodeText", 
							LabCodeFormatter.getRadiusPrefixFormatter().format(labcode));
					
					// Also copy across the project ID
					GenericFieldUtils.setField(samp, "tellervo.internal.projectID", 
							GenericFieldUtils.findField(obj, "tellervo.object.projectid").getValue());
					
					// And the object name
					GenericFieldUtils.setField(samp, "tellervo.internal.objectID", 
							obj.getIdentifier().getValue());
					
					// add the sample to the returns list
					returns.add(samp);
				}
			}
    	}
    	
    	TridasComparator numSorter = new TridasComparator(TridasComparator.Type.LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
    	Collections.sort(returns, numSorter);
    	
    	return returns;
    }

	




}
