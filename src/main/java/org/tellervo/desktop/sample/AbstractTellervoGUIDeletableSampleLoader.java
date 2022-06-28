/**
 * 
 */
package org.tellervo.desktop.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

import org.tellervo.desktop.wsi.tellervo.TellervoResource;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;


/**
 * @author Lucas Madar
 *
 */
public abstract class AbstractTellervoGUIDeletableSampleLoader<T extends TellervoResource> extends
		AbstractTellervoGUISampleLoader<T> implements
		GUIAwareDeletableSampleLoader {

	public boolean delete(Dialog dialog) throws IOException {
		T resource = getDeletionResource();
		return doDelete(resource, new TellervoResourceAccessDialog(dialog, resource));
	}

	public boolean delete(Frame frame) throws IOException {
		T resource = getDeletionResource();
		return doDelete(resource, new TellervoResourceAccessDialog(frame, resource));
	}

	public boolean delete() throws IOException {
		T resource = getDeletionResource();
		return doDelete(resource, new TellervoResourceAccessDialog(resource));
	}
	
	protected abstract T getDeletionResource();
	protected abstract boolean doDelete(T resource, TellervoResourceAccessDialog dialog) throws IOException;
}
