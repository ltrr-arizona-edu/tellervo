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
package org.tellervo.desktop.gui.widgets;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectEvent.TridasSelectType;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.labelgen.LabBarcode;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.SoundUtil;
import org.tellervo.desktop.util.SoundUtil.SystemSound;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.WSIBox;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


/**
 * TellervoCodePanel is a jpanel containing a text field that accepts and interprets
 * barcodes and labcodes.  It fires events for TridasListeners to listen for the 
 * specified entities.
 * 
 * @author peterbrewer
 *
 */
public class TellervoCodePanel extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1514161084317278783L;
	private EventListenerList tridasListeners = new EventListenerList();
	private ObjectListMode browseMode = ObjectListMode.TOP_LEVEL_ONLY;
	private JDialog parent;
	private JTextField textField;
	private JProgressBar progressBar;
	Boolean showProgress = false;
	
	/**
	 * Basic constructor 
	 * 
	 * @param parent
	 */
	public TellervoCodePanel(JDialog parent)
	{
		this.parent = parent;
		setup();
	}
	
	/**
	 * Constructor which includes a progress bar in the panel
	 * 
	 * @param parent
	 * @param showProgress
	 */
	public TellervoCodePanel(JDialog parent, Boolean showProgress)
	{
		this.showProgress = showProgress;
		this.parent = parent;
		setup();
	}
	
	/**
	 * Basic constructor
	 */
	public TellervoCodePanel() {
		parent = new JDialog();
		setup();
	}
	
	/**
	 * Constructor which includes a progress bar in the panel
	 * 
	 * @param showProgress
	 */
	public TellervoCodePanel(Boolean showProgress) {
		this.showProgress = showProgress;
		parent = new JDialog();
		setup();
	}

	public String getText()
	{
		return textField.getText();
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
	
	/**
	 * Set up the gui
	 */
	@SuppressWarnings("deprecation")
	private void setup()
	{	
		textField = new JTextField();
	    textField.addKeyListener(this);
	    setLayout(new MigLayout("hidemode 3", "[450px,grow,fill]", "[][14px][grow]"));
	    add(textField, "cell 0 0,grow");
	    
	    progressBar = new JProgressBar();
	    add(progressBar, "cell 0 1,growx,aligny top");
	    progressBar.setVisible(false);
	    textField.requestDefaultFocus();
	}

	/**
	 * Set the focus on the text field
	 */
	public void setFocus()
	{
		this.textField.requestFocusInWindow();
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
     * Interpret the text field as a lab code
     */
	public void forceFireEvent()
	{
    	fireEventByTellervoCode(textField.getText());
    	textField.setText("");
	}


	private void fireEventByTellervoCode(String labcodestr)
	{
		String [] strarray = null;
		String objcode = null;
		String elemcode = null;
		String sampcode = null;
		String radcode = null;
		String seriescode = null;
		
		// Trim off white space
		labcodestr = labcodestr.trim();
		
		// Return if empty
		if(labcodestr.length()==0) return;
				  
		// Remove the lab acronym part (e.g. "C-")  from beginning if present
		String prefix = App.getLabCodePrefix();
		if(prefix!=null && prefix.length()>0 && labcodestr.length()> prefix.length() )
		{
			if (labcodestr.substring(0, prefix.length()).compareToIgnoreCase(prefix)==0) 
			{
				labcodestr = labcodestr.substring(prefix.length(), labcodestr.length());
			}
		}
		  
		// Explode based on '-' delimiter
		strarray = labcodestr.split("-");
			
		SearchParameters search = null;
		
		// Get codes from array and set up search parameter
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
			  
			ArrayList<ITridas> siteList2 = new ArrayList<ITridas>();
			siteList2.addAll(siteList);
			  
			this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, siteList2) );
			return;
		}
		  	
		// set up our query...
		if(objcode!=null) search.addSearchConstraint(SearchParameterName.ANYPARENTOBJECTCODE, SearchOperator.EQUALS, objcode);		
		if(elemcode!=null) search.addSearchConstraint(SearchParameterName.ELEMENTCODE, SearchOperator.EQUALS, elemcode);
		if(sampcode!=null) search.addSearchConstraint(SearchParameterName.SAMPLECODE, SearchOperator.EQUALS, sampcode);
		if(radcode!=null) search.addSearchConstraint(SearchParameterName.RADIUSCODE, SearchOperator.EQUALS, radcode);
		if(seriescode!=null) search.addSearchConstraint(SearchParameterName.SERIESCODE, SearchOperator.EQUALS, seriescode);
		
		
		
		// Do the search 
		EntitySearchResource<TridasObject> searchResource = new EntitySearchResource<TridasObject>(search, TridasObject.class);
		
		
		searchResource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(parent, searchResource);
		searchResource.query();
		
		setGuiSearching(true);
		dlg.setVisible(true);
		setGuiSearching(false);

			
		if(!dlg.isSuccessful()) 
		{
			// Search failed
			new BugDialog(dlg.getFailException());
			return;
		} 
		else 
		{
			// Search successful
			List<TridasObject> foundEntities = (List<TridasObject>) searchResource.getAssociatedResult();

			if(foundEntities.size()==0)
			{
				this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, TridasSelectType.FORCED));
			}
			else if(foundEntities.size()==1)
			{
				this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, foundEntities.get(0), TridasSelectType.FORCED));
			}
			else
			{	
				ArrayList<ITridas> foundEntities2 = new ArrayList<ITridas>();
				foundEntities2.addAll(foundEntities);
				this.fireTridasSelectListener(new TridasSelectEvent(this, 1001, foundEntities2, TridasSelectType.FORCED));
			}
			
		}		
		 
		
	}

	/**
	 * Set the gui to show a search is in progress or not
	 * 
	 * @param b
	 */
	private void setGuiSearching(Boolean b)
	{		
		if(showProgress)
		{
			progressBar.setIndeterminate(true);
			progressBar.setVisible(b);
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
				//Alert.error("Error", "Invalid barcode type");
				return;
			}
			
			SoundUtil.playSystemSound(SystemSound.BARCODE_SCAN);

			
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
		    	forceFireEvent();
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
	
	
	public enum ObjectListMode {
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
