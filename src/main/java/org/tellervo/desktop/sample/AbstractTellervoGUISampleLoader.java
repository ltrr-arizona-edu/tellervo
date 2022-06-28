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
package org.tellervo.desktop.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.wsi.tellervo.TellervoResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;


public abstract class AbstractTellervoGUISampleLoader<T extends TellervoResource>
		implements GUIAwareSampleLoader, ResourcePropertySupport {
	public Sample load(Dialog dialog) throws IOException {
		T resource = getResource(loadProperties);
		return doLoad(resource, new TellervoResourceAccessDialog(dialog, resource));
	}

	public Sample load(Frame frame) throws IOException {
		T resource = getResource(loadProperties);
		return doLoad(resource, new TellervoResourceAccessDialog(frame, resource));
	}

	public Sample load() throws IOException {
		T resource = getResource(loadProperties);
		return doLoad(resource, new TellervoResourceAccessDialog(resource));
	}
	
	/** For loading */
	protected abstract T getResource(Map<String, ? extends Object> properties);
	/** For saving */
	protected abstract T getResource(Sample s, Map<String, ? extends Object> properties);
	protected abstract Sample doLoad(T resource, TellervoResourceAccessDialog dialog) throws IOException;
	protected abstract boolean doSave(Sample s, T resource, TellervoResourceAccessDialog dialog) throws IOException;
	
	public BaseSample loadBasic(Dialog dialog) throws IOException {
		return load(dialog);
	}

	public BaseSample loadBasic(Frame frame) throws IOException {
		return load(frame);
	}
	
	public BaseSample loadBasic() throws IOException {
		return load();
	}

	public boolean save(Sample s, Dialog dialog) throws IOException, UserCancelledException {
		T resource = getResource(s, saveProperties);
		return doSave(s, resource, new TellervoResourceAccessDialog(dialog, resource));
	}

	public boolean save(Sample s, Frame frame) throws IOException {
		T resource = getResource(s, saveProperties);
		return doSave(s, resource, new TellervoResourceAccessDialog(frame, resource));
	}

	public boolean save(Sample s) throws IOException {
		T resource = getResource(s, saveProperties);
		return doSave(s, resource, new TellervoResourceAccessDialog(resource));
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
