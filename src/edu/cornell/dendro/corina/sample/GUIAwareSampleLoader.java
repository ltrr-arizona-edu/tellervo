package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

public interface GUIAwareSampleLoader extends SampleLoader {
	public Sample load(Dialog dialog) throws IOException;
	public BaseSample loadBasic(Dialog dialog) throws IOException;
	public boolean save(Sample s, Dialog dialog) throws IOException;

	public Sample load(Frame frame) throws IOException;
	public BaseSample loadBasic(Frame frame) throws IOException;
	public boolean save(Sample s, Frame frame) throws IOException;
}
