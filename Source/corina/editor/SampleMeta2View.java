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

package corina.editor;

import corina.Sample;
import corina.SampleListener;
import corina.SampleEvent;
import corina.Species;
import corina.Metadata.Field;

import java.util.ResourceBundle;
import java.util.MissingResourceException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class SampleMeta2View extends JPanel implements SampleListener {

    // sample to view
    private Sample s;

    // layout
    GridBagLayout gridbag = new GridBagLayout();
    GridBagConstraints c = new GridBagConstraints();

    // i18n
    ResourceBundle msg = ResourceBundle.getBundle("MetadataBundle");

    public SampleMeta2View(Sample sample) {
	// copy data
	this.s = sample;

	// layout
	setLayout(gridbag);
	c.insets = new Insets(4, 4, 4, 4);

	// constraints for all labels
	c.weightx = 1.0;
	c.weighty = 1.0;

	// ID
	addField("id", 0, 0, 6);

	// title
	addField("title", 0, 1, 16);

	// dating
	addPopup("dating", 0, 2, true, new String[] {
	    "",
	    msg.getString("dating.R"),
	    msg.getString("dating.A")});

	// unmeas_pre, post
	addField("unmeas_pre", 0, 3, 3);
	addField("unmeas_post", 0, 4, 3);

	// type
	addPopup("type", 0, 5, true, new String[] {
	    "",
	    msg.getString("type.S"),
	    msg.getString("type.H"),
	    msg.getString("type.C")});

	// species
	addPopup("species", 0, 6, true, (String[]) Species.species.entrySet().toArray(new String[0]));

	// format
	addPopup("format", 0, 7, true, new String[] {
	    msg.getString("format.R"),
	    msg.getString("format.I")});

	// right fields

	// index_type
	addPopup("index_type", 2, 0, false, new String[] {
	    msg.getString("index_type.-1"),
	    msg.getString("index_type.1"),
	    msg.getString("index_type.2"),
	    msg.getString("index_type.3"),
	    msg.getString("index_type.4"),
	    msg.getString("index_type.5"),
	    msg.getString("index_type.6")});

	// sapwood
	addField("sapwood", 2, 1, 3);

	// pith
	addPopup("pith", 2, 2, true, new String[] {
	    "",
	    msg.getString("pith.N"),
	    msg.getString("pith.*"),
	    msg.getString("pith.P")});

	// terminal
	addPopup("terminal", 2, 3, true, new String[] {
	    "",
	    msg.getString("terminal.B"),
	    msg.getString("terminal.W"),
	    msg.getString("terminal.v"),
	    msg.getString("terminal.vv")});

	// continuous
	addPopup("continuous", 2, 4, true, new String[] {
	    "",
	    msg.getString("continuous.C"),
	    msg.getString("continuous.R"),
	    msg.getString("continuous.N")});

	// quality
	addPopup("quality", 2, 5, true, new String[] {
	    "",
	    msg.getString("quality.+"),
	    msg.getString("quality.++")});

	// reconciled
	addPopup("reconciled", 2, 6, true, new String[] {
	    msg.getString("reconciled.N"),
	    msg.getString("reconciled.Y")});

	// author
	addField("author", 2, 7, 16);

	// comments
	addComments();
    }

    // if not, add document listener here
    private void addField(String var, int x, int y, int width) {
	// label
	c.gridx = x;
	c.gridy = y;
	c.anchor = GridBagConstraints.NORTHEAST;
	JLabel label = new JLabel(msg.getString(var + ".name") + ":");
	label.setHorizontalAlignment(SwingConstants.RIGHT);
	gridbag.setConstraints(label, c);
	add(label);

	// field
	c.anchor = GridBagConstraints.NORTHWEST;
	c.gridx = x+1;
	c.gridy = y;
	JTextField field = new JTextField(s.meta.get(var) == null ? "" : s.meta.get(var).toString(),
					  width);
	gridbag.setConstraints(field, c);
	add(field);
    }

    // fixme:
    // - add a listener!
    // - allow NULLs!
    private void addPopup(String var, int x, int y, boolean enabled, String values[]) {
	// label
	c.gridx = x;
	c.gridy = y;
	c.anchor = GridBagConstraints.NORTHEAST;
	JLabel label = new JLabel(msg.getString(var + ".name") + ":");
	label.setHorizontalAlignment(SwingConstants.RIGHT);
	gridbag.setConstraints(label, c);
	add(label);

	// popup
	c.gridx = x+1;
	c.gridy = y;
	c.anchor = GridBagConstraints.NORTHWEST;
	JComboBox popup = new JComboBox(values);
	try {
	    if (s.meta.get(var) != null) {
		popup.setSelectedItem(msg.getString(var + "." + s.meta.get(var))); // flakey!
		System.out.println("value is " + s.meta.get(var));
		System.out.println("selecting " + msg.getString(var + "." + s.meta.get(var)));
	    }
	} catch (MissingResourceException mre) {
	    System.out.println("var=" + var);
	}

	// enabled?
	popup.setEnabled(enabled);

	// listener
	popup.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    if (e.getStateChange() == ItemEvent.SELECTED)
			System.out.println("e.getItem() = " + e.getItem());
		    // IMPLEMENT ME
		}
	    });

	gridbag.setConstraints(popup, c);
	add(popup);
    }

    private JTextArea comments;
    private void addComments() {
	// bottom: comments label
	JLabel commentsLabel = new JLabel("Comments:");
	c.anchor = GridBagConstraints.NORTHEAST;
	c.gridx = 0;
	c.gridy = 8;
	gridbag.setConstraints(commentsLabel, c);
	add(commentsLabel);

	// bottom: comments block
	comments = new JTextArea(s.meta.get("comments") == null ? "" : s.meta.get("comments").toString(), 3, 32);
	JScrollPane commentsScroller = new JScrollPane(comments,
						       // JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						       JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
						       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	c.anchor = GridBagConstraints.NORTHWEST;
	c.gridx = 1;
	c.gridy = 8;
	c.gridwidth = GridBagConstraints.REMAINDER; // 3;
	c.gridheight = 3; // 3 rows, same as above -- refactor me
	c.fill = GridBagConstraints.HORIZONTAL;
	gridbag.setConstraints(commentsScroller, c);
	add(commentsScroller);
    }

    public void sampleRedated(SampleEvent e) {
	// abs/rel are changed by redater, so either that should fire
	// metadatachanged, or i should reset the dating field here.
    }
    public void sampleDataChanged(SampleEvent e) { } // ignore
    public void sampleMetadataChanged(SampleEvent e) {
	// if metadata could be changed elsewhere, i'd update myself
	// with it here, i think.  i don't think that'll ever happen,
	// so i'm safe.

	// wait: if you index some data, metadata fields change.  so
	// i'll have to implement this somehow (unless I want to
	// reconstruct the view each time an index or similar is done,
	// which might be easier as a quick-fix.)
    }
    public void sampleFormatChanged(SampleEvent e) { } // ignore
    public void sampleElementsChanged(SampleEvent e) { } // ignore

}
