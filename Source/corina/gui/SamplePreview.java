//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package corina.gui;

import corina.Previewable;
import corina.Sample;
import corina.files.WrongFiletypeException;
import corina.cross.Grid;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JFileChooser;

public class SamplePreview extends JLabel implements PropertyChangeListener {

    public SamplePreview(JFileChooser fc) {
	setVerticalAlignment(TOP);
        setPreferredSize(new Dimension(240, 200));
        fc.addPropertyChangeListener(this);
    }

    // PropertyChangeListener helper
    private void loadSample(File file) {
	// no file?
        if (file == null)
            return;
 
	try {
	    Previewable s=null;

	    // new: loop to find a Previewable
	    try {
		s = new Grid(file.getPath());
	    } catch (WrongFiletypeException wfte) {
		s = new Sample(file.getPath());
	    } // but can't string catches here ... darn

	    // old: Sample s = new Sample(file.getPath());

	    // JIT-compiling this (HTML JLabel) is *slow*!  => thread it, but how?
	    final String glue = s.getHTMLPreview();
	    (new Thread() {
		    public void run() {
			setText(glue);
		    }
		}).start();

	} catch (WrongFiletypeException wfte) {
	    final int size = 256;
	    char buffer[] = new char[size];
	    int len;
	    try {
		FileReader fr = new FileReader(file);
		len = fr.read(buffer);
	    } catch (IOException ioe) {
		setText("<html><b>Unreadable file</b>");
		return;
	    }
	    // the next line won't always work.  fixme.
	    setText("<html><b>Unreadable file:</b><pre>" + new String(buffer, 0, len) +
		    (len==size ? "\n...</pre>" : "</pre>")); // no "..." if that's all there is
	    // apparently an empty file has len=-1 (!!!)
	} catch (IOException ioe) {
	    setText("<html><b>Error loading:</b><br>" + ioe.getMessage());
	}
    }

    // implements PropertyChangeListener
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
            if (isShowing()) {
                loadSample((File) e.getNewValue());
                repaint();
            }
        }
    }

}
