package edu.cornell.dendro.corina.sample;

import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;

public interface GUIAwareDeletableSampleLoader extends GUIAwareSampleLoader, DeletableSampleLoader {
	public boolean delete(Frame frame) throws IOException;
	public boolean delete(Dialog dialog) throws IOException;
}
