package edu.cornell.dendro.corina;

import java.io.IOException;

public class CachedElement extends Element {

	public CachedElement(SampleLoader loader) {
		super(loader);
		
		baseSample = null;
		fullSample = null;
	}

	public CachedElement(BaseSample sample) throws UnsupportedOperationException {
		super(sample);
		
		// cache the sample properly
		if(sample instanceof Sample)
			this.fullSample = (Sample) sample;
		else
			this.baseSample = sample;
	}
	
	/**
	 * Construct an element from another element
	 * (shallow copy!)
	 * @param element
	 */
	public CachedElement(Element element) {
		super(element);

		if(element instanceof CachedElement) {
			baseSample = ((CachedElement) element).baseSample;
			fullSample = ((CachedElement) element).fullSample;
		} else {
			baseSample = null;
			fullSample = null;
		}
	}
	
	@Override
	public void setLoader(SampleLoader loader) {
		super.setLoader(loader);

		// maybe this fixes our loading problems? :)
		dead = false;
		
		if(baseSample != null)
			baseSample.setLoader(loader);
		if(fullSample != null)
			fullSample.setLoader(loader);
	}


	@Override
	public Sample load() throws IOException {
		if(fullSample != null)
			return fullSample;
		
		// if dead, don't try again...
		if(dead)
			throw deadException;
	
		// load it...
		try {
			fullSample = super.load();
		} catch (IOException ioe) {
			dead = true;
			deadException = ioe;
			throw deadException;
		}
		
		// no need to keep this around
		baseSample = null;
		
		return fullSample;
	}

	@Override
	public BaseSample loadBasic() throws IOException {
		if(baseSample != null)
			return baseSample;
		
		// downcast :)
		if(fullSample != null)
			return fullSample;
		
		// if dead, don't try again...
		if(dead)
			throw deadException;
	
		// load it...
		try {
			baseSample = super.loadBasic();
		} catch (IOException ioe) {
			dead = true;
			deadException = ioe;
			throw deadException;
		}
		
		return baseSample;
	}

	private BaseSample baseSample;
	private Sample fullSample;
	
	// if we fail once, don't keep trying.
	boolean dead;
	IOException deadException;
}
