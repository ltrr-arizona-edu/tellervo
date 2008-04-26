package edu.cornell.dendro.corina;

import java.io.IOException;

/*
 * An element is essentially a pointer to a Sample resource.
 * 
 * This sample resource can be located on disk, on the web, in a database...
 * The idea is to abstract the loading and saving of samples away from the sample class.
 * 
 * An element needs the following methods:
 * 		load() 		loads a Sample object
 * 					throws IOException on failure
 * 
 * 		loadBase()	loads a BaseSample object
 * 					may return a casted Sample object
 * 					throws IOException on failure
 * 
 * 		save() 		saves a Sample object 
 * 					throws IOException on failure
 */

public class Element {
	
	/**
	 * Construct an element from a loader
	 * @param loader
	 */
	public Element(SampleLoader loader) {
		this.loader = loader;
	}
	
	/**
	 * Construct an element from another element
	 * (shallow copy!)
	 * @param element
	 */
	public Element(Element element) {
		this.loader = element.loader;
	}

	/**
	 * Construct an element from a sample
	 * NOTE: Sample *must* have a loader!
	 * @throws UnsupportedOperationException if sample doesn't have a loader
	 * @param sample
	 */
	public Element(BaseSample sample) throws UnsupportedOperationException {
		this.loader = sample.getLoader();
		
		if(loader == null)
			throw new UnsupportedOperationException("Element created from sample without a loader!");
	}
	
	private SampleLoader loader;

	public SampleLoader getaLoader() {
		return loader;
	}
	
	public Sample load() throws IOException {
		return loader.load();
	}
	
	public BaseSample loadBasic() throws IOException {
		return loader.loadBasic();
	}
	
	/**
	 * Gets the name of the element
	 * For a FileElement, returns the filename
	 * @return
	 */
	public String getName() {
		return loader.getName();
	}
	
	public String toString() {
		return getName();
	}
}
