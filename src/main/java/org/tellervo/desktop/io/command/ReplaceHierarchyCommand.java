package org.tellervo.desktop.io.command;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.control.ImportNodeSelectedEvent;
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
		
		/*try {
			log.debug("splitOff() called in ReplaceHierarchyCommand");
	        MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
				log.error("splitOff() called from non-MVC thread");
		        e.printStackTrace();
		} catch (IncorrectThreadException e) {
				log.error("splitOff() called, but this is not the main thread");
				e.printStackTrace();
		}*/
		
		log.debug("Retrieving the replacement hierarchy");
		ReplaceHierarchyEvent event = (ReplaceHierarchyEvent) argEvent;
			
		TridasObject newObject   = null;
		TridasElement newElement = null;
		TridasSample newSample = null;
		TridasRadius newRadius = null;
		TridasMeasurementSeries newMeasurementSeries = null;
		
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
					 if(newRadius.isSetMeasurementSeries())
					 {
						 newMeasurementSeries = newRadius.getMeasurementSeries().get(0);
					 }
				 }
			 }
		 }
		} catch (Exception e)
		{
			log.error("Unable to get details of the hierarchy specified");
			return;
		}
	
		int oldClassDepth = TridasUtils.getDepth(event.currentNode.getUserObject().getClass());
		
		DefaultMutableTreeNode oldNode = (DefaultMutableTreeNode)event.currentNode;
		
		if(oldClassDepth==TridasUtils.getDepth(TridasMeasurementSeries.class))
		{
			if(newMeasurementSeries!=null)
			{
				log.debug("Switching measurementSeries node");
				oldNode = (DefaultMutableTreeNode) this.swapEntity(event, newMeasurementSeries, oldNode).getParent();
				oldClassDepth--;
			}
		}
		
		if(oldClassDepth==TridasUtils.getDepth(TridasRadius.class))
		{
			if(newRadius!=null)
			{
				log.debug("Switching radius node");
				oldNode = (DefaultMutableTreeNode) this.swapEntity(event, newRadius, oldNode).getParent();
				oldClassDepth--;
			}
		}
				
		if(oldClassDepth==TridasUtils.getDepth(TridasSample.class))
		{
			if(newSample!=null)
			{
				log.debug("Switching sample node");
				oldNode = (DefaultMutableTreeNode) this.swapEntity(event, newSample, oldNode).getParent();
				oldClassDepth--;
			}
		}
		
		if(oldClassDepth==TridasUtils.getDepth(TridasElement.class))
		{
			if(newElement!=null)
			{
				log.debug("Switching element node");
				oldNode = (DefaultMutableTreeNode) this.swapEntity(event, newElement, oldNode).getParent();
				oldClassDepth--;
			}
		}
			
		if(oldClassDepth==TridasUtils.getDepth(TridasObject.class))
		{
			if(newObject!=null)
			{
				log.debug("Switching object node");
				oldNode = (DefaultMutableTreeNode) this.swapEntity(event, newObject, oldNode);
				//oldClassDepth--;
			}
		}
			
	}
	
	
	private DefaultMutableTreeNode swapEntity(ReplaceHierarchyEvent event, ITridas newentity, DefaultMutableTreeNode oldnode)
	{
		if(newentity.getClass()!=oldnode.getUserObject().getClass())
		{
			log.error("Cannot swap nodes of different types");
			log.error("Old node = "+oldnode.getUserObject().getClass().getName());
			log.error("New node = "+newentity.getClass().getName());
		}
		
		
		
		TridasRepresentationTableTreeRow oldrow = new TridasRepresentationTableTreeRow(oldnode, null);		
		log.debug("Swapping the "+oldnode.getUserObject().getClass().getName()+ " "
				+((ITridas)oldnode.getUserObject()).getTitle() 
				+ " for " + newentity.getClass().getName()+ " "+newentity.getTitle());
		
		
			
		DefaultMutableTreeNode newnode = (DefaultMutableTreeNode) oldnode.clone();
		newnode.setUserObject(newentity);
		
		TridasRepresentationTableTreeRow newrow = new TridasRepresentationTableTreeRow(newnode, null);
		
		/*ImportSwapEntityEvent swapEvent = new ImportSwapEntityEvent(event.model, newrow, oldrow, true);
		log.debug("Dispatching the swapEvent for "+ newentity.getClass().getName());
		swapEvent.dispatch();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("Finished sleeping... will now continue");
			
		return event.model.getSelectedRow().getDefaultMutableTreeNode();
		*/
		
		// Get the tree node representing the parent of the entity we're replacing
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) oldnode.getParent();
		
		// Calculate the index for the node we're replacing
		int newNodeIndex = parentNode.getIndex(oldnode);
		
		// Loop through the old node cloning across all its children onto the new node
		// otherwise all the child enties will be lost
		for(int i=0; i<oldnode.getChildCount(); i++)
		{
			try {
				EntitySwappedCommand.copySubTree(newnode, oldnode);
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Go ahead and remove the old node
		event.model.getTreeModel().removeNodeFromParent(oldnode);
		
		// Add new node in its place
		event.model.getTreeModel().insertNodeInto(newnode, parentNode, newNodeIndex);
		
		/*if(event.selectNodeAfterSwap)
		{
			// Select the new node
			TridasRepresentationTableTreeRow row = new TridasRepresentationTableTreeRow(newNode, null);	
			ImportNodeSelectedEvent event2 = new ImportNodeSelectedEvent(event.model, row);
			event2.dispatch();
		}*/
		
		log.debug("Completed executing EntitySwappedCommand");
		
		return newnode;
		
		
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
