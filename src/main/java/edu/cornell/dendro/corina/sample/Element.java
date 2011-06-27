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
package edu.cornell.dendro.corina.sample;

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

public class Element implements Comparable<Element> {
	
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
		
		// don't forget to preload for us!
		loader.preload(sample);
	}
	
	private SampleLoader loader;

	public SampleLoader getLoader() {
		return loader;
	}
	
	public void setLoader(SampleLoader loader) {
		this.loader = loader;
	}
	
	public Sample load() throws IOException {
		return loader.load();
	}
	
	public BaseSample loadBasic() throws IOException {
		return loader.loadBasic();
	}
	
	/**
	 * 
	 * @return true if this element can potentially be deleted
	 */
	public boolean isDeletable() {
		return loader instanceof DeletableSampleLoader;
	}
	
	/**
	 * 
	 * @return true if delete was successful
	 * @throws IOException for errors during delete process
	 * @throws IllegalArgumentException if this element type can't be deleted
	 */
	public boolean delete() throws IOException, IllegalArgumentException {
		if(loader instanceof DeletableSampleLoader)
			return ((DeletableSampleLoader)loader).delete();
		
		throw new IllegalArgumentException("Cannot delete this type of element");
	}
	
	/**
	 * Gets the name of the element
	 * For a FileElement, returns the full path name
	 * @return
	 */
	public String getName() {
		return loader.getName();
	}
	
	/**
	 * Gets the short name of the element
	 * For a FileElement, returns the file's base name
	 */
	public String getShortName() {
		return loader.getShortName();
	}
	
	public String toString() {
		return getName();
	}

	/**
	 * Compares names of one element against another
	 */
	public int compareTo(Element o) {
		if(o == this)
			return 0;
		
		return o.getName().compareTo(getName());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		
		if(o instanceof Element) {
			Element e = (Element) o;
	
			// compare loaders first, if they're the same class
			if(loader.getClass().isInstance(e.loader)) {
				return loader.equals(e.loader);
			}
			
			// otherwise, try name
			return (compareTo(e) == 0);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		int hv = loader.hashCode();
		
		return hv;
	}
}
