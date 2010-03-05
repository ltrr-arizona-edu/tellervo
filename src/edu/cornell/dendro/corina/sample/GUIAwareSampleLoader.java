package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

import edu.cornell.dendro.corina.gui.UserCancelledException;

public interface GUIAwareSampleLoader extends SampleLoader {
	public Sample load(Dialog dialog) throws IOException, UserCancelledException;
	public BaseSample loadBasic(Dialog dialog) throws IOException, UserCancelledException;
	public boolean save(Sample s, Dialog dialog) throws IOException, UserCancelledException;

	public Sample load(Frame frame) throws IOException;
	public BaseSample loadBasic(Frame frame) throws IOException;
	public boolean save(Sample s, Frame frame) throws IOException;
}
