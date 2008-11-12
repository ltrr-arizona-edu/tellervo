package edu.cornell.dendro.corina.browser;

import java.io.IOException;

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;

public class SampleHandle {
	private FileElement element;
	private Sample sample;
	private BaseSample baseSample;
	private boolean loaded;
	
	public SampleHandle(FileElement element) {
		this.element = element;
		this.baseSample = this.sample = null;

		loaded = false;
		lastModified = -1;
	}
	
	public FileElement getElement() {
		return element;
	}
	
	public Sample getSample() throws IOException {
		if(sample == null)
			sample = element.load();
		
		loaded = true;
		return sample;
	}
	
	public BaseSample getBaseSample() throws IOException {
		if(sample != null)
			baseSample = new BaseSample(sample);
		else
			baseSample = element.loadBasic();

		loaded = true;
		return baseSample;
	}
	
	public void reset() {
		loaded = false;
		baseSample = sample = null;
	}
	
	public void setBaseSample(BaseSample bs) {
		if(loaded)
			throw new java.lang.UnsupportedOperationException("Can't set base sample on already loaded sample!");
		
		baseSample = new BaseSample(bs);
		loaded = true;
	}
	
	private long lastModified;
	
	public long getLastModified() {
		return lastModified;
	}
	
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}
}
