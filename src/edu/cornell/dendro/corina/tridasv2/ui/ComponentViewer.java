/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import javax.swing.SwingUtilities;

import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.wsi.ResourceEvent;
import edu.cornell.dendro.corina.wsi.ResourceEventListener;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesResource;

/**
 * @author Lucas Madar
 *
 */
public class ComponentViewer extends ComponentViewerUI implements ResourceEventListener {
	private static final long serialVersionUID = 1L;
	private Sample sample;
	private boolean loadedComprehensive;
	
	public ComponentViewer(Sample sample) {
		this.sample = sample;
		this.loadedComprehensive = false;
	}
	
	/**
	 * Called when someone has made this panel visible
	 */
	public void notifyPanelVisible() {
		if(loadedComprehensive)
			return;
		
		loadedComprehensive = true;
		
		if(sample.getLoader() instanceof CorinaWsiTridasElement) {
			TridasIdentifier identifier = ((CorinaWsiTridasElement) sample.getLoader()).getTridasIdentifier();
			// create a new resource
			SeriesResource resource = new SeriesResource(identifier, EntityType.MEASUREMENT_SERIES, CorinaRequestType.READ);

			// flag it as comprehensive
			resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
			resource.setOwnerWindow(SwingUtilities.getWindowAncestor(this));
			
			resource.addResourceEventListener(this);
			resource.query();
			
			setEnabled(false);
		}
	}

	/**
	 * Called from loading a resource
	 */
	public void resourceChanged(ResourceEvent re) {
		switch(re.getEventType()) {
		case ResourceEvent.RESOURCE_QUERY_COMPLETE:
			setEnabled(true);
			break;
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			setEnabled(true);
			break;
		}
	}
}
