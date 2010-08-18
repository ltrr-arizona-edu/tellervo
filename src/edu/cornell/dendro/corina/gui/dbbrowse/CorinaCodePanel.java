package edu.cornell.dendro.corina.gui.dbbrowse;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FocusTraversalPolicy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIBox;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.labels.LabBarcode;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;

public class CorinaCodePanel extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1514161084317278783L;
	private EventListenerList tridasListeners = new EventListenerList();
	private ObjectListMode browseMode = ObjectListMode.TOP_LEVEL_ONLY;
	private JDialog parent;
	private JTextField textField;
	
	public CorinaCodePanel(JDialog parent)
	{
		this.parent = parent;
		setup();
	}
	
	public CorinaCodePanel() {
		parent = new JDialog();
		setup();
	}

	/**
	 * Set the object list mode for this widget.  Should be one
	 * of all, populated or populated_first.  
	 * 
	 * @param mode
	 */
	public void setobjectListMode(ObjectListMode mode)
	{
		this.browseMode = mode;
	}
	
	private void setup()
	{	
		textField = new JTextField();
	    textField.addKeyListener(this);    
	    this.setLayout(new BorderLayout());
	    add(textField, java.awt.BorderLayout.CENTER);
	    textField.requestDefaultFocus();
	}

	public void setFocus()
	{
		this.textField.requestFocus();
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
	
	/**
	 * Remove a listener
	 * @param listener
	 */
	public void removeTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.remove(TridasSelectListener.class, listener);
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
	
	
    /**
     * Play a beep.  Useful for when a barcode has been scanned.
     */
    public void playBarcodeBeep(){
		AudioClip beep;
		try {	
			// play this to indicate measuring is on...
			beep = Applet.newAudioClip(getClass().getClassLoader().getResource("edu/cornell/dendro/corina_resources/Sounds/checkout.wav"));
			if(beep != null)
				beep.play();
		} catch (Exception ae) { 
			System.out.println("Failed to play sound");
			System.out.println(ae.getMessage());
			}
		
    }
	
	


	@SuppressWarnings("unchecked")
	private void fireEventByCorinaCode(String labcodestr)
	{
		String [] strarray = null;
		String objcode = null;
		String elemcode = null;
		String sampcode = null;
		String radcode = null;
		String seriescode = null;
		  
		// Remove the "C-" from beginning if present
		if (labcodestr.substring(0, 2).compareToIgnoreCase("C-")==0) labcodestr = labcodestr.substring(2, labcodestr.length());
		  
		// Explode based on '-' delimiter
		strarray = labcodestr.split("-");
			
		SearchParameters search = null;
		
		// Get codes from array and set up search parameter
		if (strarray.length==0) return;
		if (strarray.length>=1) {
			objcode = strarray[0];
			search = new SearchParameters(SearchReturnObject.OBJECT);
		}
		if (strarray.length>=2) {
			elemcode = strarray[1];
			search = new SearchParameters(SearchReturnObject.ELEMENT);
		}
		if (strarray.length>=3) {
			sampcode = strarray[2];
			search = new SearchParameters(SearchReturnObject.SAMPLE);
		}
		if (strarray.length>=4) {
			radcode = strarray[3];
			search = new SearchParameters(SearchReturnObject.RADIUS);
		}
		if (strarray.length>=5) {
			seriescode = strarray[4];
			search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
		}
		    
		// Only site code included so filter site list and go
		if (strarray.length==1){
			  
			ArrayList<TridasObjectEx> siteList = getFilteredSiteList(objcode);
			  
			ArrayList<TridasObject> siteList2 = new ArrayList<TridasObject>();
			siteList2.addAll(siteList);
			  
			this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, siteList2) );
			return;
		}
		  	
		// set up our query...
		if(objcode!=null) search.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTCODE, SearchOperator.EQUALS, objcode);		
		if(elemcode!=null) search.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, elemcode);
		if(sampcode!=null) search.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.LIKE, sampcode);
		if(radcode!=null) search.addSearchConstraint(SearchParameterName.RADIUSCODE, SearchOperator.LIKE, radcode);
		if(seriescode!=null) search.addSearchConstraint(SearchParameterName.SERIESCODE, SearchOperator.LIKE, seriescode);
		
		
		
		// Do the search 
		EntitySearchResource<TridasObject> searchResource = new EntitySearchResource<TridasObject>(search, TridasObject.class);
		
		
		searchResource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
		CorinaResourceAccessDialog dlg = new CorinaResourceAccessDialog(parent, searchResource);
		searchResource.query();
		dlg.setVisible(true);

			
		if(!dlg.isSuccessful()) 
		{
			// Search failed
			new Bug(dlg.getFailException());
			return;
		} 
		else 
		{
			// Search successful
			List<TridasObject> foundEntities = (List<TridasObject>) searchResource.getAssociatedResult();
			
			if(foundEntities.size()==0)
			{
				this.fireTridasSelectListener(new TridasSelectEvent(this, 1001));
			}
			else
			{	
				this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, foundEntities));
			}
			
		}		
		 
		
	}

	
	
	private ArrayList<TridasObjectEx> getFilteredSiteList(String searchStr) {
		
		Collection<TridasObjectEx> sites = null;
		ArrayList<TridasObjectEx> filteredSites = new ArrayList<TridasObjectEx>();
		
		switch(this.browseMode)
		{
		case TOP_LEVEL_ONLY:
			sites = App.tridasObjects.getTopLevelObjectList();
			break;
		case ALL:
			sites = App.tridasObjects.getObjectList();
			break;
		case POPULATED:
			sites = App.tridasObjects.getPopulatedObjectList();
			break;
		case POPULATED_FIRST:
			sites = App.tridasObjects.getPopulatedFirstObjectList();
			break;
		}
	
		if (searchStr.equals("")) {
			// User has NOT entered filter text
			filteredSites.addAll(sites);			
		}
		else
		{
			// User HAS entered filter text
			String filter = searchStr.toLowerCase();
	    	for(TridasObjectEx s : sites)
	    	{
				String search = s.toTitleStringWithParentCode().toLowerCase();
				if (search.indexOf(filter) != -1)
					filteredSites.add(s);
	    	}
		}
		
		return filteredSites;
    }
	
	@Override
	public void keyReleased(KeyEvent e) {
		if(textField.getText().length()==0) {
    	    // Filter text is empty so reset object list
    		TridasSelectEvent event = new TridasSelectEvent(this, 1001);
    		this.fireTridasSelectListener(event);
    		return;
    	}
	
		if(textField.getText().length()==24)
		{
			// A barcode was probably just scanned
			playBarcodeBeep();		
			String barcodeText = textField.getText();
			textField.setText("");
			
			// Decode the barcode string
			LabBarcode.DecodedBarcode barcode = LabBarcode.decode(barcodeText);
			
			ITridas scannedEntity;
			
			if(barcode.uuidType.equals(LabBarcode.Type.BOX))
			{
				scannedEntity = new WSIBox();
			}
			else if (barcode.uuidType.equals(LabBarcode.Type.SAMPLE))
			{
				scannedEntity = new TridasSample();
			}
			else if (barcode.uuidType.equals(LabBarcode.Type.SERIES))
			{
				scannedEntity = new TridasMeasurementSeries();
			}
			else
			{
				Alert.error("Error", "Invalid barcode type");
				return;
			}
			
			TridasIdentifier id = new TridasIdentifier();
			id.setValue(barcode.uuid.toString());
			scannedEntity.setIdentifier(id);
			this.fireTridasSelectListener(new TridasSelectEvent(this, TridasSelectEvent.ENTITY_SELECTED, scannedEntity));
			return;
		}
		else {
	    	// User is typing a lab code
		    if (e.getKeyCode() == KeyEvent.VK_ENTER)
		    {
		    	fireEventByCorinaCode(textField.getText());
		    	textField.setText("");
		    }
   		     
		    // Enter has not been pressed so user is 
		    // still typing lab code    
		    return;
			 
		 }
		
	}

	@Override
	public void keyTyped(KeyEvent e) { }
	
	@Override
	public void keyPressed(KeyEvent e) { }
	
	
	private enum ObjectListMode {
		ALL(I18n.getText("dbbrowser.showAllObjects")),
		POPULATED(I18n.getText("dbbrowser.showPopulatedObjects")),
		POPULATED_FIRST(I18n.getText("dbbrowser.showPopulatedObjectsFirst")),
		TOP_LEVEL_ONLY("Top level only");
		
		private final String value;
		
		private ObjectListMode(String value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
}
