package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;

public abstract class AbstractCorinaGUISampleLoader<T extends CorinaResource> implements GUIAwareSampleLoader {
	public Sample load(Dialog dialog) throws IOException {
		T resource = getResource(loadProperties);
		return doLoad(resource, new CorinaResourceAccessDialog(dialog, resource));
	}

	public Sample load(Frame frame) throws IOException {
		T resource = getResource(loadProperties);
		return doLoad(resource, new CorinaResourceAccessDialog(frame, resource));
	}

	public Sample load() throws IOException {
		T resource = getResource(loadProperties);
		return doLoad(resource, new CorinaResourceAccessDialog(resource));
	}
	
	/** For loading */
	protected abstract T getResource(Map<String, ? extends Object> properties);
	/** For saving */
	protected abstract T getResource(Sample s, Map<String, ? extends Object> properties);
	protected abstract Sample doLoad(T resource, CorinaResourceAccessDialog dialog) throws IOException;
	protected abstract boolean doSave(Sample s, T resource, CorinaResourceAccessDialog dialog) throws IOException;
	
	public BaseSample loadBasic(Dialog dialog) throws IOException {
		return load(dialog);
	}

	public BaseSample loadBasic(Frame frame) throws IOException {
		return load(frame);
	}
	
	public BaseSample loadBasic() throws IOException {
		return load();
	}

	public boolean save(Sample s, Dialog dialog) throws IOException {
		T resource = getResource(s, saveProperties);
		return doSave(s, resource, new CorinaResourceAccessDialog(dialog, resource));
	}

	public boolean save(Sample s, Frame frame) throws IOException {
		T resource = getResource(s, saveProperties);
		return doSave(s, resource, new CorinaResourceAccessDialog(frame, resource));
	}

	public boolean save(Sample s) throws IOException {
		T resource = getResource(s, saveProperties);
		return doSave(s, resource, new CorinaResourceAccessDialog(resource));
	}

	/** A set of properties given to the server for loading */
	private Map<String, Object> loadProperties;
	/** A set of properties given to the server for saving */
	private Map<String, Object> saveProperties;

	/**
	 * Set a server query property for loading
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void setLoadProperty(String propertyName, Object value) {
		if(loadProperties == null)
			loadProperties = new HashMap<String, Object>();
		
		loadProperties.put(propertyName, value);
	}

	/**
	 * Set a server query property for saving
	 * 
	 * @param propertyName
	 * @param value
	 */
	public void setSaveProperty(String propertyName, Object value) {
		if(saveProperties == null)
			saveProperties = new HashMap<String, Object>();
		
		saveProperties.put(propertyName, value);
	}
}
