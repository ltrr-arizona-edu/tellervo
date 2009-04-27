package edu.cornell.dendro.corina.sample;

import java.io.IOException;

import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;

/**
 * An implementation of SampleLoader
 * 
 * Loads a TRiDaS document from a Corina-style resource
 * 
 * @author Lucas Madar
 *
 */

public class CorinaWsiTridasElement extends AbstractCorinaGUISampleLoader {
	private String shortName;
	private String name;
	private TridasIdentifier identifier;
	
	public CorinaWsiTridasElement(TridasIdentifier identifier) {
		this.identifier = identifier;
		
		name = shortName = identifier.toString();
	}
	
	@Override
	protected Sample doLoad(CorinaResource resource,
			CorinaResourceAccessDialog dialog) throws IOException {
		return null;
	}

	@Override
	protected boolean doSave(Sample s, CorinaResource resource,
			CorinaResourceAccessDialog dialog) throws IOException {
		return false;
	}

	@Override
	protected CorinaResource getResource() {
		return null;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public void preload(BaseSample bs) {
	}
}
