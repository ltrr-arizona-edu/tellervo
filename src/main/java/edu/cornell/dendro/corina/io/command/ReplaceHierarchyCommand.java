package edu.cornell.dendro.corina.io.command;

import javax.swing.tree.DefaultMutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.ImportSwapEntityEvent;
import edu.cornell.dendro.corina.io.control.ReplaceHierarchyEvent;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTableTreeRow;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;

public class ReplaceHierarchyCommand implements ICommand {
	private final static Logger log = LoggerFactory.getLogger(ReplaceHierarchyCommand.class);

	@Override
	public void execute(MVCEvent argEvent) {
		ReplaceHierarchyEvent event = (ReplaceHierarchyEvent) argEvent;
			
		TridasObject newObject   = null;
		TridasElement newElement = null;
		TridasSample newSample = null;
		TridasRadius newRadius = null;
		
		try{
		 newObject = getNewHierarchy(event);
		 newElement = newObject.getElements().get(0);
		 newSample = newElement.getSamples().get(0);
		 newRadius = newSample.getRadiuses().get(0);
		} catch (Exception e)
		{
			log.error("Unable to get details of the hierarchy specified");
			return;
		}
		TridasRepresentationTableTreeRow oldrow;
		TridasRepresentationTableTreeRow newrow;
		
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
		else
		{
			log.error("Unsupported new parent type");
			return null;
		}
		
		// associate a resource
		EntityResource<TridasObject> rsrc = new EntityResource<TridasObject>(entity, 
				CorinaRequestType.READ, TridasObject.class);
		log.debug("Searching webservice for new hierarchy - 2");
		// we want it comprehensive 
		// (because we're asking for a sample and getting back an object->series tree)
		rsrc.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
		log.debug("Searching webservice for new hierarchy - 3");
		
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(event.parentDialog, rsrc);
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
