package edu.cornell.dendro.corina.sample;

import java.io.IOException;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.tridas.TridasIdentifier;
import edu.cornell.dendro.corina.webdbi.MeasurementResource;
import edu.cornell.dendro.corina.webdbi.PrototypeLoadDialog;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;
import edu.cornell.dendro.corina.webdbi.ResourceQueryType;

public class TridasWSElement implements SampleLoader {
	private String shortName;
	private String name;
	private TridasIdentifier identifier;
		
	public TridasWSElement(TridasIdentifier identifier) {
		this.identifier = identifier;
		name = shortName = identifier.toString();
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}
	
	public Sample load() throws IOException {
		// set up the resource
		MeasurementResource xf = new MeasurementResource();
		xf.attachIdentifier(identifier);
		
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(xf);
		
		// start our query (remotely)
		xf.query();		
		
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful())
			throw new IOException("Failed to load: " + dlg.getFailException());
		
		// associate this loader with the object
		Sample ns = xf.getObject();
		ns.setLoader(this);

		// cache any data that we (this class) cares about
		preload(ns);

		// update our resourceidentifier
		if(ns.hasMeta(Metadata.TRIDAS_IDENTIFIER)) 
			identifier = (TridasIdentifier) ns.getMeta(Metadata.TRIDAS_IDENTIFIER);
		
		// update our recently opened documents
		// no, do this where we open them!
		//OpenRecent.sampleOpened(this);
		
		// and return it!
		return ns;
	}
	
	// Save actually means two things: 
	// UPDATE (if we already have an ID)
	// CREATE (if we have a parent)
	public boolean save(Sample s) throws IOException {
		ResourceQueryType queryType;
		
		if(s.hasMeta("::dbid")) {
			queryType = ResourceQueryType.UPDATE;
		}
		else if(s.hasMeta("::dbparent") && s.getMeta("::dbparent") instanceof ResourceIdentifier) {
			queryType = ResourceQueryType.CREATE;
		}
		else {
			new Bug(new IllegalStateException(
					"saving corina web element, but it doesn't have a valid parent OR an id"));
			return false;
		}
		
		// set up the resource
		MeasurementResource xf = new MeasurementResource(queryType);
		xf.setObject(s);
		
		PrototypeLoadDialog dlg = new PrototypeLoadDialog(xf);
		
		// start our query (remotely)
		xf.query();		
		
		dlg.setVisible(true);
		
		if(!dlg.isSuccessful())
			throw new IOException("Failed to save: " + dlg.getFailException());
		
		// get the new sample, and copy its data over
		Sample ns = (Sample) xf.getObject();

		//FIXME: TRIDAS!
		// get our stored loader for this sample
		/*
		if(ns.hasMeta("::dbrid") && ns.getMeta("::dbrid") instanceof ResourceIdentifier) {
			ns.setLoader(new CorinaWebElement((ResourceIdentifier) ns.getMeta("::dbrid")));
		}
		else if(ns.hasMeta("::dbid")) {
			ResourceIdentifier rid = new ResourceIdentifier("measurement", 
					ns.getMeta("::dbid").toString());
			ns.setLoader(new CorinaWebElement(rid));
		}
		else {
			new Bug(new IllegalStateException("No way to create a resourceidentifier for saved sample!"));
			return false;
		}
		*/
			
		// copy everything over (including the new loader we just made)
		Sample.copy(ns, s);
		
		// cache any data that we care about...
		// make sure we use the *new* loader, because it might not be us!
		s.getLoader().preload(s);
		
		return true;
	}

	public BaseSample loadBasic() throws IOException {
		return load();  
	}
	
	public void preload(BaseSample bs) {
		SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
		
		if(ss != null)
			shortName = name = ss.getLabCode();
		else if(bs.hasMeta("title"))
			shortName = name = bs.getMeta("title").toString();
		else {
			shortName = name = identifier.toString();
		}
	}
}
