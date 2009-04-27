package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

import edu.cornell.dendro.corina.wsi.corina.CorinaResource;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;

public abstract class AbstractCorinaGUISampleLoader implements GUIAwareSampleLoader {

	public Sample load(Dialog dialog) throws IOException {
		CorinaResource resource = getResource();
		return doLoad(resource, new CorinaResourceAccessDialog(dialog, resource));
	}

	public Sample load(Frame frame) throws IOException {
		CorinaResource resource = getResource();
		return doLoad(resource, new CorinaResourceAccessDialog(frame, resource));
	}

	public Sample load() throws IOException {
		CorinaResource resource = getResource();
		return doLoad(resource, new CorinaResourceAccessDialog(resource));
	}
	
	protected abstract CorinaResource getResource();
	protected abstract Sample doLoad(CorinaResource resource, CorinaResourceAccessDialog dialog) throws IOException;
	protected abstract boolean doSave(Sample s, CorinaResource resource, CorinaResourceAccessDialog dialog) throws IOException;
	
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
		CorinaResource resource = getResource();
		return doSave(s, resource, new CorinaResourceAccessDialog(dialog, resource));
	}

	public boolean save(Sample s, Frame frame) throws IOException {
		CorinaResource resource = getResource();
		return doSave(s, resource, new CorinaResourceAccessDialog(frame, resource));
	}

	public boolean save(Sample s) throws IOException {
		CorinaResource resource = getResource();
		return doSave(s, resource, new CorinaResourceAccessDialog(resource));
	}
}
