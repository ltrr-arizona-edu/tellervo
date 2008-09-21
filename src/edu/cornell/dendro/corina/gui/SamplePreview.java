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

package edu.cornell.dendro.corina.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import edu.cornell.dendro.corina.Preview;
import edu.cornell.dendro.corina.Previewable;
import edu.cornell.dendro.corina.cross.legacy.Grid;
import edu.cornell.dendro.corina.formats.WrongFiletypeException;
import edu.cornell.dendro.corina.logging.CorinaLog;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.ElementFactory;

public class SamplePreview extends JPanel implements PropertyChangeListener {
  private static final CorinaLog log = new CorinaLog(SamplePreview.class);

  //private PreviewComponent preview;
  private JScrollPane sp;

  public SamplePreview(JFileChooser fc) {
    setLayout(new GridBagLayout());
    //setPreferredSize(new Dimension(240, 200));

    GridBagConstraints gbc = new GridBagConstraints();
    //label.setContentType("text/html");
    //label.setEditable(false);
    // --- using preview component now ---- label.setVerticalAlignment(SwingConstants.TOP);

    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.insets = new java.awt.Insets(0,0,2,0);
    gbc.anchor = GridBagConstraints.NORTH;
    gbc.fill = GridBagConstraints.BOTH;

    log.trace(getInsets());
    setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,2,0,0),
                                                 BorderFactory.createTitledBorder("Preview")));
    //setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED ));
    //label.setContentType("text/html");
    //label.setEditable(false);
    //label.setBorder(BorderFactory.createTitledBorder("label"));
    // --- using preview component now --- label.setOpaque(false);

    sp = new JScrollPane() {
      @Override
	public Dimension getPreferredSize () {
        Dimension pref = super.getPreferredSize();
        Dimension max = super.getMaximumSize();
        if (pref.height > max.height) pref.height = max.height;
        if (pref.width > max.width) pref.width = max.width;
        return pref;
      }
    };
    sp.setBorder(BorderFactory.createEmptyBorder());
    sp.setMaximumSize(new Dimension(200, 200));

    add(sp, gbc);

    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.gridy = 1;
    //gbc.insets = new java.awt.Insets(0,2,0,2);
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;

    JButton b = new JButton("Hide >>");

    add(b, gbc);

    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
        setVisible(false);
      }
    });


    fc.addPropertyChangeListener(this);
    setVisible(false);
  }

  // PropertyChangeListener helper
  private void loadSample(File file) {
    // no file?
    if (file == null)
      return;

  	// i would check for file.isDirectory() here and display a
  	// blank preview component if it's a folder, but apparently
  	// JFileChooser doesn't fire a propertyChangeEvent (or event
  	// of any sort) if a folder is selected.  why?  i have no
  	// idea.

    try {
			Previewable s = null;

			// new: loop to find a Previewable
			try {
				log.trace("creating grid");
				// XXX: aaron - well, Grid constructor likes to hang on
				// invalid files apparently... not sure what causes it
				// but the parse method never returns
				s = new Grid(file.getPath());
				log.trace("done creating grid");
			} catch (WrongFiletypeException wfte) {
				// um.. no. this is annoying!
				// wfte.printStackTrace();
				s = ElementFactory.createElement(file.getPath()).load();
				//s = new Sample(file.getPath());
			} // but can't string catches here ... darn

			// get preview, and show it.
			showPreview(s.getPreview());

			// TODO: if title is too long, wrap? use tooltip? (jlinedlabel?)
		} catch (WrongFiletypeException wfte) {
			wfte.printStackTrace();
			showPreview(new Preview.NotDendroDataPreview());
		} catch (FileNotFoundException fnfe) {
			// file doesn't exist, so just hide our preview window.
			// useful for saving files. :)
			setVisible(false);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			showPreview(new Preview.ErrorLoadingPreview(ioe));
		}
  }

  private void showPreview(Preview p) {
    // remove old value, and put new preview component up.
    /* aaron if (getComponentCount() > 0)
	    remove(0);*/
    sp.getViewport().setView(new PreviewComponent(p)); 
    //add(pc, BorderLayout.CENTER);

    // beat swing with a stick until it repaints me.
    /* aaron invalidate();
     validate();*/
  }

  // implements PropertyChangeListener
  public void propertyChange(PropertyChangeEvent e) {
    String prop = e.getPropertyName();
    log.trace("PROPERTY NAME: " + e.getPropertyName());
    log.trace("OLD VALUE: " + e.getOldValue());
    log.trace("NEW VALUE: " + e.getNewValue());
    if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
      if (e.getNewValue() != null) {
        log.trace("Setting visible");
        setVisible(true);
        //if (isShowing()) {
          log.trace("Setting empty size");
          setSize(new Dimension(0, 0));
          log.trace("loading sample");
          loadSample((File) e.getNewValue());
          //sp.getViewport().setView(label);
          /*label.setSize(0,0);
          sp.setSize(0, 0);
          setSize(0,0);
          setVisible(false);

          label.revalidate();
          sp.revalidate();
          /*label.repaint();
          sp.repaint();*/
          /*label.invalidate();
          sp.invalidate();
          invalidate();
          ((JComponent) getParent()).invalidate();
          ((JComponent) getParent()).validate();

          //setVisible(true);*/
          log.trace("revalidating");
          revalidate(); // must check if it had to be resized

          //System.out.println("LABEL: " + label.getSize() + " " + label.getPreferredSize() + " " + label.getMaximumSize());
          log.trace("SP: " + sp.getSize() + " " + sp.getPreferredSize() + " " + sp.getMaximumSize());
          log.trace("this: " + getSize() + " " + getPreferredSize() + " " + getMaximumSize());
        //}
      } else {
        log.trace("HIDING");
        setVisible(false);
      }
    }
  }
}
