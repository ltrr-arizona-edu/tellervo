package org.tellervo.desktop.io.command;

import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.control.ImportSwapEntityEvent;
import org.tellervo.desktop.io.control.ReplaceHierarchyEvent;
import org.tellervo.desktop.io.model.TridasRepresentationTableTreeRow;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.WSIEntity;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tridas.interfaces.ITridas;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class ReplaceHierarchyCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(ReplaceHierarchyCommand.class);

	@Override
	public void execute(MVCEvent argEvent) {
		
		try {
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
		        // this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
		        // this means that this MVC thread is not the main thread, it was already splitOff() previously
		        e.printStackTrace();
		}
		
		ReplaceHierarchyEvent event = (ReplaceHierarchyEvent) argEvent;
			
		TridasObject newObject   = null;
		TridasElement newElement = null;
		TridasSample newSample = null;
		TridasRadius newRadius = null;
		
		try{
		 newObject = getNewHierarchy(event);
		 
		 if(newObject.isSetElements())
		 {
			 newElement = newObject.getElements().get(0);
			 if(newElement.isSetSamples())
			 {
				 newSample = newElement.getSamples().get(0);
				 if(newSample.isSetRadiuses())
				 {
					 newRadius = newSample.getRadiuses().get(0);
				 }
			 }
		 }
		} catch (Exception e)
		{
			log.error("Unable to get details of the hierarchy specified");
			return;
		}
		TridasRepresentationTableTreeRow oldrow;
		TridasRepresentationTableTreeRow newrow;
		
		


		int currentClassDepth = TridasUtils.getDepth(event.currentNode.getUserObject().getClass());
		
			
		// Start by swapping the Radius	
		DefaultMutableTreeNode oldRadiusNode = (DefaultMutableTreeNode)event.currentNode.getParent();
		oldrow = new TridasRepresentationTableTreeRow(oldRadiusNode, null);
		
		log.debug("Swapping radius from "+((ITridas)oldRadiusNode.getUserObject()).getTitle()+" to "+newRadius.getTitle());

		
		DefaultMutableTreeNode newRadiusNode = new DefaultMutableTreeNode();
		newRadiusNode.setUserObject(newRadius);
		newrow = new TridasRepresentationTableTreeRow(newRadiusNode, null);
		
		ImportSwapEntityEvent swapEvent = new ImportSwapEntityEvent(event.model, newrow, oldrow, false);
		swapEvent.dispatch();
		
		
		// Then the sample
		DefaultMutableTreeNode oldSampleNode = (DefaultMutableTreeNode)oldRadiusNode.getParent();
		oldrow = new TridasRepresentationTableTreeRow(oldSampleNode, null);
		
		log.debug("Swapping sample from "+((ITridas)oldSampleNode.getUserObject()).getTitle()+" to "+newSample.getTitle());
		
		DefaultMutableTreeNode newSampleNode = new DefaultMutableTreeNode();
		newSampleNode.setUserObject(newSample);
		newrow = new TridasRepresentationTableTreeRow(newSampleNode, null);
		
		
		ImportSwapEntityEvent swapEvent2 = new ImportSwapEntityEvent(event.model, newrow, oldrow, false);
		swapEvent2.dispatch();
		
	
		// Then the element
		DefaultMutableTreeNode oldElementNode = (DefaultMutableTreeNode)oldSampleNode.getParent();
		oldrow = new TridasRepresentationTableTreeRow(oldElementNode, null);
		
		log.debug("Swapping element from "+((ITridas)oldElementNode.getUserObject()).getTitle()+" to "+newElement.getTitle());
		
		DefaultMutableTreeNode newElementNode = new DefaultMutableTreeNode();
		newElementNode.setUserObject(newElement);
		newrow = new TridasRepresentationTableTreeRow(newElementNode, null);
		
		ImportSwapEntityEvent swapEvent3 = new ImportSwapEntityEvent(event.model, newrow, oldrow, false);
		swapEvent3.dispatch();
		
		
		// Then the object
		DefaultMutableTreeNode oldObjectNode = (DefaultMutableTreeNode)oldElementNode.getParent();
		oldrow = new TridasRepresentationTableTreeRow(oldObjectNode, null);
		
		log.debug("Swapping object from "+((ITridas)oldObjectNode.getUserObject()).getTitle()+" to "+newObject.getTitle());
		
		DefaultMutableTreeNode newObjectNode = new DefaultMutableTreeNode();
		newObjectNode.setUserObject(newObject);
		newrow = new TridasRepresentationTableTreeRow(newObjectNode, null);
		
		ImportSwapEntityEvent swapEvent4 = new ImportSwapEntityEvent(event.model, newrow, oldrow, false);
		swapEvent4.dispatch();
		
		// Select the original node
		/*TridasRepresentationTableTreeRow row = new TridasRepresentationTableTreeRow(event.currentNode, null);	
		ImportNodeSelectedEvent selectEvent = new ImportNodeSelectedEvent(event.model, row);
		selectEvent.dispatch();*/

	}
	
	/**
	 * Get a TridasObject populated with children all the way down to Radius or 
	 * Series.  This represents the new hierarchy that we want to use.
	 * 
	 * @param event
	 * @return
	 */
	private TridasObject getNewHierarchy(ReplaceHierarchyEvent event)
	{
		log.debug("Searching webservice for new hierarchy");
		
		// create an entity for reading
		WSIEntity entity = new WSIEntity();
		entity.setId(event.newParent.getIdentifier().getValue());
		if(event.newParent instanceof TridasMeasurementSeries)
		{
			entity.setType(EntityType.MEASUREMENT_SERIES);
		}
		else if(event.newParent instanceof TridasRadius)
		{
			entity.setType(EntityType.RADIUS);
		}
		else if(event.newParent instanceof TridasSample)
		{
			entity.setType(EntityType.SAMPLE);
		}
		else if(event.newParent instanceof TridasElement)
		{
			entity.setType(EntityType.ELEMENT);
		}		
		else
		{
			log.error("Unsupported new parent type");
			return null;
		}
		
		// associate a resource
		EntityResource<TridasObject> rsrc = new EntityResource<TridasObject>(entity, 
				TellervoRequestType.READ, TridasObject.class);
		log.debug("Searching webservice for new hierarchy - 2");
		// we want it comprehensive 
		// (because we're asking for a sample and getting back an object->series tree)
		rsrc.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
		log.debug("Searching webservice for new hierarchy - 3");
		
		TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(event.parentDialog, rsrc);
		log.debug("Searching webservice for new hierarchy - 4");
		rsrc.query();
		log.debug("Searching webservice for new hierarchy - 5");
		accdialog.setVisible(true);
		log.debug("Searching webservice for new hierarchy - 6");

		TridasObject newHierarchy;
		if(accdialog.isSuccessful())
		{
			log.debug("Searching webservice for new hierarchy - 7");
			newHierarchy =  rsrc.getAssociatedResult();
		}
		else
		{
			log.error("Error loading: " + accdialog.getFailException().getLocalizedMessage());
			return null;
		}
		
		log.debug("Searching webservice for new hierarchy - 8");
		log.debug("New hierarchy found successfully");
		return newHierarchy;
	}

}
