/**
 * 
 */
package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;

/**
 * @author Lucas Madar
 *
 */
public abstract class AbstractCorinaGUIDeletableSampleLoader<T extends CorinaResource> extends
		AbstractCorinaGUISampleLoader<T> implements
		GUIAwareDeletableSampleLoader {

	public boolean delete(Dialog dialog) throws IOException {
		T resource = getDeletionResource();
		return doDelete(resource, new CorinaResourceAccessDialog(dialog, resource));
	}

	public boolean delete(Frame frame) throws IOException {
		T resource = getDeletionResource();
		return doDelete(resource, new CorinaResourceAccessDialog(frame, resource));
	}

	public boolean delete() throws IOException {
		T resource = getDeletionResource();
		return doDelete(resource, new CorinaResourceAccessDialog(resource));
	}
	
	protected abstract T getDeletionResource();
	protected abstract boolean doDelete(T resource, CorinaResourceAccessDialog dialog) throws IOException;
}
