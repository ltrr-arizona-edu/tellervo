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

package edu.cornell.dendro.corina.editor;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.metadata.MetadataTemplate;
import edu.cornell.dendro.corina.metadata.MetadataField;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.sample.SampleSummary;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
   A view of the metadata of a Sample, which lets the user edit it.

    <h2>Left to do:</h2>
    <ul>

	<li>use DialogLayout, instead of weird gridbaglayout that
	nobody understands.

        <li>Bug: comments field is sometimes zero-width (solved by
        dialog layout?) (still reproducible?)

	<li>don't bug the user to fix a bad value; simply leave the
	bad value there, until he tries to change it, and then only
	allow it to be changed to a valid value.

    </ul>

<pre>
fixme[dialoglayout]: change dialog-layout into generic "two-column layout".
       i'll have to make my own labels, but the increased flexibility should
       be worth it.
fixme[dialog]layout): shouldn't be any extra spacing to left of labels
fixme[dialog-layout?]: comments field should be as wide as possible

     -- Scroll when the user tabs out of view; scroll if the user
     types into a text component that's not in view

     -- Possible to use only one DocumentListener that works for all
     fields?  Probably not worth the effort (they share code, now,
     anyway).

     -- Make any changes here Undoable.  This will probably involve
     creating an inner class for "FieldEdit" ("MetadataEdit"?), which
     implements Undoable.  The fun part will be to coalesce multiple
     FieldEdits together -- if I type "Quercus", and then select Undo,
     it should un-type "Quercus", not just the "s".

 -- Figure out how to use a popup for the species?  (!)

FIXME: extend JPanel, and have a scroll pane -- don't BE the scroll pane (why?).

note: only the invoker of an edit should post the undo.  for example,
indexing itself should post the undo, and the user changing the format
should post an undo, but when indexing changes the format, that
shouldn't post an undo.  (OR: maybe i should make it so calling a
method to change something doesn't post an undo here -- but how would
that work?)

 </pre>

<pre>
fields are:
-- one-of (a,b,c)
   -- one-of, plus bad value (a,b,c,x) (WRITEME)
-- simple field ([___])
   -- number field? ([123])
-- read-only field ("cubic spline")
-- multi-line field (comments)
</pre>

    </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id: MetadataPanel.java 1073 2008-04-27 08:34:27Z lucasm $
*/
public class MetadataPanel extends JScrollPane implements SampleListener, ResourceEventListener {

	// the sample to view
	private Sample s;

	// hmm, would it be possible to use only one INSTANCE of this, even?
	// that sure would save on the enabled/disabled crap.
	private static class UpdateListener implements DocumentListener {
		private Sample s;

		private MetadataField f;

		private boolean enabled = true;

		public UpdateListener(Sample s, MetadataField f) {
			// get updates in field, send to sample
			this.s = s;
			this.f = f;
		}

		public void setEnabled(boolean newState) {
			enabled = newState;
		}

		private String getDocumentText(DocumentEvent e) {
			try { // need to synch on e to guarantee no exception?
				return e.getDocument().getText(0, e.getDocument().getLength());
			} catch (BadLocationException ble) {
				return ""; // can't happen
			}
		}

		public void changedUpdate(DocumentEvent e) {
			if (enabled)
				update(getDocumentText(e));
		}

		public void insertUpdate(DocumentEvent e) {
			if (enabled)
				update(getDocumentText(e));
		}

		public void removeUpdate(DocumentEvent e) {
			if (enabled)
				update(getDocumentText(e));
		}

		private void update(Object value) {
			// everything falls through to here

			// if it's the same, do nothing
			try {
				if (value.equals(s.getMeta(f.getVariable())))
					return;
				if (!s.hasMeta(f.getVariable())
						&& ((String) value).length() == 0)
					return;
			} catch (NullPointerException npe) {
				// ignore?
			}

			// (if it's a String, try to make it an Integer)
			// But be careful: no hex or octal, please.
			try {
				value = new Integer(Integer.parseInt((String) value, 10));
			} catch (NumberFormatException nfe) {
				// ok, it's just a String -- that's fine
			}

			// store it, and dirty the sample
			if (value instanceof String && ((String) value).length() == 0)
				s.removeMeta(f.getVariable());
			else
				s.setMeta(f.getVariable(), value);
			s.setModified();
			s.fireSampleMetadataChanged();
		}
	}

	private Map components = new Hashtable(); // field => component hash
	private List listeners = new ArrayList(); // all the listeners
	private Map popups = new Hashtable(); // field varname => jcombobox mapping


	private JPanel panel;
	/**
	 Construct a new view of the metadata for a sample.

	 @param sample the Sample
	 */
	public MetadataPanel(Sample sample) {
		// copy data
		this.s = sample;

		// no border!
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		panel = new JPanel(new GridBagLayout());
		
		setViewportView(panel);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = c.gridy = 0;
		
		addSummary(c);
		finishPart(c);

		addMetadata(c);
		finishPart(c);
		
		// more info for direct measurements... go down the data tree!
		if(s.getSampleType() == SampleType.DIRECT) {
			
		}
	}
	
	private void addSummary(GridBagConstraints c) {
		SampleSummary ss = (SampleSummary) s.getMeta("::summary");
		
		if(ss == null)
			return;
		
		addTitle("Measurement Summary", c, null);
		
		JLabel a, b;
		
		a = new JLabel("Lab code:");
		b = new JLabel(ss.getLabCode());
		addPair(a, b, c);

		a = new JLabel("Site code:");
		b = new JLabel(ss.getSiteCode());
		addPair(a, b, c);

		a = new JLabel("Common taxon:");
		b = new JLabel(ss.getCommonTaxon());
		addPair(a, b, c);

		
		a = new JLabel("Site description:");
		b = new JLabel(ss.siteDescription());
		addPair(a, b, c);

		a = new JLabel("Taxon description:");
		b = new JLabel(ss.taxonDescription());
		addPair(a, b, c);

		a = new JLabel("Type:");
		b = new JLabel(s.getSampleType().toString());
		addPair(a, b, c);
	}
	
	private void addMetadata(GridBagConstraints c) {
		JButton edit = new JButton("edit");
		addTitle("Measurement Metadata", c, edit);
		
		Map<String, Object> meta = s.getMetadata();
		Set<String> keys = meta.keySet();
		
		// pass 1: things that are nice
		for(String key : keys) {
			if(key.startsWith("::"))
				continue;
		
			// special case
			if(key.equalsIgnoreCase("comments"))
				continue;
			
			JLabel a = new JLabel(getMetaName(key) + ":");
			JComponent b = createComponentForObject(meta.get(key));
			
			addPair(a, b, c);
		}

		// pass 2: things that are unknown!
		for(String key : keys) {
			if(!key.startsWith("::-::"))
				continue;
			
			JLabel a = new JLabel(key.substring(5) + "(?):");
			JComponent b = createComponentForObject(meta.get(key));
			
			addPair(a, b, c);
		}
		
		String comments = (String) meta.get("comments");
		if(comments != null) {
			// on its own line
			finishPart(c);
			
			JLabel a = new JLabel("Comments:");
			
			addComments(a, comments, c);
		}
	}
	
	private JComponent createComponentForObject(Object o) {
		// special handling for booleans!
		/* no!
		if(o instanceof Boolean) {
			JCheckBox box = new JCheckBox();
			
			box.setSelected(((Boolean)o).booleanValue());
			box.setEnabled(false);
			
			return box;
		}*/
		
		JLabel label = new JLabel(o.toString());
		
		return label;
	}

	private void addPair(JLabel label, JComponent comp, GridBagConstraints c) {
		label.setLabelFor(comp);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(0, 8, 4, 8);

		panel.add(label, c);
		c.gridx++;
		c.anchor = GridBagConstraints.WEST;
		panel.add(comp, c);
		c.gridx++;
		
		// end of the line!
		if(c.gridx >= 3) {
			c.gridx = 0;
			c.gridy++;
		}
	}

	private void addComments(JLabel label, String comments, GridBagConstraints c) {
		JEditorPane comp = new JEditorPane();
		
		comp.setFocusable(false);
		comp.setEditable(false);
		comp.setOpaque(false);
		comp.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.gray));
		comp.setText(comments + comments + comments + comments);
		
		JPanel compPanel = new JPanel(new BorderLayout());
		//compPanel.add(comp, BorderLayout.CENTER);
		
		label.setLabelFor(compPanel);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setVerticalAlignment(SwingConstants.TOP);
		label.setAlignmentY(JLabel.TOP_ALIGNMENT);
		
		c.gridwidth = 1;
		c.weightx = 1.0;
		c.weighty = 0;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.insets = new Insets(0, 8, 0, 8);
		
		panel.add(label, c);
		c.gridx++;
		
		c.gridwidth = 3;
		c.weightx = c.weighty = 0;
		c.anchor = GridBagConstraints.WEST;
		c.fill = GridBagConstraints.BOTH;
		panel.add(comp, c);
		c.gridx++;
		
		c.gridx = 0;
		c.gridy++;
	}

	
	private void finishPart(GridBagConstraints c) {
		if(c.gridx != 0) {
			c.gridx = 0;
			c.gridy++;
		}
	}
	
	// number of sections added so far
	private int nsections = 0;
	private void addTitle(String title, GridBagConstraints c, JButton editButton) {
		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBorder(BorderFactory.createMatteBorder(0, 2, 2, 0, Color.gray));

		JLabel l = new JLabel(title);
		l.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		l.setHorizontalAlignment(SwingConstants.LEFT);
		
		titlePanel.add(l, BorderLayout.WEST);
		titlePanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		titlePanel.setAlignmentY(JPanel.TOP_ALIGNMENT);
		
		if(editButton != null) {
			editButton.setHorizontalAlignment(SwingConstants.RIGHT);
			titlePanel.add(editButton, BorderLayout.EAST);
		}
		
		c.gridwidth = 4;
		c.gridx = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		c.insets = new Insets((nsections == 0) ? 0 : 10, 0, 10, 0);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 2.0;
		c.weighty = 0.0;
		
		panel.add(titlePanel, c);
		
		c.gridwidth = 1;
		c.gridx = 0;
		c.weightx = 0;
		c.gridy++;
		nsections++;
	}
		
	private void bollocks() {
		// make sure we're notified when the dictionary changes!
		App.dictionary.addResourceEventListener(this);

		// temp panel to add stuff to; we'll scroll this
		final JPanel p = new JPanel(new GridBagLayout());

		// constraints for all components
		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 0.0;

		// we'll need to count the number of fields
		int n = 0;

		// add the fields, one at a time
		Iterator it = MetadataTemplate.getFields();
		while (it.hasNext()) {
			MetadataField f = (MetadataField) it.next();

			// (row)
			c.gridy = n++;

			// create and add the constrained label
			c.gridx = 0;
			c.anchor = GridBagConstraints.EAST;
			c.weightx = 0.0; // no effect?
			c.fill = GridBagConstraints.NONE;
			JLabel l = new JLabel(f.getFieldDescription() + ":");
			// WAS: JPanel fff = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // putting it in a jpanel lines up the lines -- REFACTOR
			// WAS: fff.add(l);
			// WAS: p.add(fff, c);
			p.add(l, c);

			// for computing height of first row
			if (firstLabel == null)
				firstLabel = l;

			// ... and the edit box
			c.gridx = 1;
			c.weightx = 1.0; // no effect?
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.WEST;

			// idea: each component is responsible for its own updating.  no, then i can't disable them all... (?)

			// SPECIES HACK
			/*
			 if (f.getVariable().equals("species")) {
			 p.add(Layout.flowLayoutL(new SpeciesPopup(s, editor)), c);
			 continue;
			 }
			 */

			// is this field a list of values?
			if (f.isList()) {
	//			p.add(makePopup(f), c);
				continue;
			} // integrate this clause into the rest...  easier once old-style-popups are all gone

			// if it has no choices, a plain jtextfield is perfect.
			// if it needs several lines, though, use a jtextarea.
			JTextComponent x = makeTextBlock(f);

			// add to the panel
			// use a sub-panel, so the left-alignment is ok.  without this, on OS X it
			// looks passable, but on windows it looks horrible.
			c.insets = new java.awt.Insets(5, 5, 5, 5);
			if (x instanceof JTextArea) {
				// p.add(Layout.flowLayoutL(new JScrollPane(x)), c);
				p.add(new JScrollPane(x), c);
			} else {
				p.add(x, c); // WAS: Layout.flowLayoutL(x), c);
			}
			c.insets = new java.awt.Insets(0, 0, 0, 0);

			// add to hashmap -- only used for textcomponents
			//if (x instanceof JTextComponent)
			components.put(f.getVariable(), x);

			// first one?  focus!
			if (n == 1)
				x.requestFocus();
		}

		// put a filler-panel below everything (not needed for dialog layout!)
		c.gridx = 0;
		c.gridy = n;
		c.weighty = 1.0;
		p.add(new JPanel(), c);

		// stuff everything in a scrollpane (=me)
		setViewportView(p);
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// set some sane unit increments (one line per click), but this can only be done after it's packed
		// FIXME: shouldn't this be an addNotify(), then?
		final JScrollPane glue = this;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				GridBagLayout gbl = (GridBagLayout) p.getLayout();
				int h = gbl.getLayoutDimensions()[1][0]; // (1=height, 0=first row)  "Most applications do not call this method directly."  (I'm special.)
				glue.getVerticalScrollBar().setUnitIncrement(h);
				glue.getHorizontalScrollBar().setUnitIncrement(h);
				// todo: i can remove myself now, right?  [--wtf does this comment mean?] [ it means this componentlistener is only used once.]
			}
		});
	}

	private JTextComponent makeTextBlock(MetadataField f) {
		// number of lines?
		int lines = f.getLines();

		// value -- REFACTOR?
		Object hash = s.getMeta(f.getVariable());
		String value = (hash == null ? "" : hash.toString());

		// HACK2: index_type gets looked up now; add index_type.options=-1,1,2,... to metadata properties file, perhaps
		if (f.getVariable().equals("index_type") && hash != null)
			value = I18n.getText("meta." + "index_type." + hash);

		// build component.
		// (the strange -- and seemingly pointless, even for a cast --
		// cast is required to make one arg assignable to the other.
		// see jls chapter 15, section 25, clause 4.)
		JTextComponent t = (lines > 1 ? new JTextArea(value, lines, 0) : // WAS: 50
				(JTextComponent) new JTextField(value, 32));
		// TODO: use COLUMNS when that's added.

		if (f.isReadOnly()) {
			// if read-only, it's not editable
			t.setEditable(false);
		} else {
			// listener for textfield
			UpdateListener u = new UpdateListener(s, f);
			t.getDocument().addDocumentListener(u);
			listeners.add(u);
		}

		return t;
	}

	// first label
	private JLabel firstLabel = null;

	// FIXME: javadoc these, or make them a private inner class
	public void sampleRedated(SampleEvent e) {
		// abs/rel are changed by redater, but that fires a metadataChanged event

		// BUG: abs/rel shouldn't be changed by redater: they're
		// metadata changes only, not dating changes!
	}

	public void sampleDataChanged(SampleEvent e) {
	}

	public void sampleMetadataChanged(SampleEvent e) {
		// no way to find out which fields changed!  darn...
		// this means we have to disable all listeners right at
		// the start, not just as we go.
		for (int i = 0; i < listeners.size(); i++)
			((UpdateListener) listeners.get(i)).setEnabled(false);

		// if something gets thrown in here, make sure to re-enable all the listeners
		try {

			Iterator i = MetadataTemplate.getFields();
			while (i.hasNext()) {
				MetadataField f = (MetadataField) i.next();

				// is this my format popup?  if so, choose the correct one...
				if (popups.containsKey(f.getVariable())) {
//					resetPopup(f);
					continue;
				}

				// get value -- DUPLICATE CODE, REFACTOR
				Object hash = s.getMeta(f.getVariable());
				String value = (hash == null ? "" : hash.toString());

				// get component
				String field = f.getVariable();
				JTextComponent comp = (JTextComponent) components.get(field);

				// HACK: index_type gets looked up now -- DUPLICATE CODE, REFACTOR
				if (f.getVariable().equals("index_type") && hash != null)
					value = I18n.getText("meta." + "index_type." + hash);

				try {
					// text component?
					comp.setText(value);
				} catch (IllegalStateException ise) {
					// there's got to be a better way to do this:
					// System.out.println("illegal state!  (skipping " + field + ")");
					continue;
					// this is caused by trying to update myself.  if something got thrown
					// here, the listeners wouldn't get re-enabled, and users would only
					// see the first letter they typed.  (they hate that.)
				}
			}

		} catch (Exception ex) {
			// shouldn't happen, but it does (BUG: why?)
			ex.printStackTrace();
		} finally {
			// re-enable all listeners
			for (int i = 0; i < listeners.size(); i++)
				((UpdateListener) listeners.get(i)).setEnabled(true);
		}
	}

	public void resourceChanged(ResourceEvent re) {
		// if our dictionary is reloaded, here's where we get notified!
		if(re.getEventType() == ResourceEvent.RESOURCE_QUERY_COMPLETE) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					Iterator it = popups.keySet().iterator();
					while(it.hasNext()) {
						MetadataField f = MetadataTemplate.getField((String) it.next());
						
					}					
				}
			});
		}
	}
	
	public void sampleElementsChanged(SampleEvent e) {
	}

	/**
	 * Get a nice name for a metadata key
	 * 
	 * @param key
	 * @return
	 */
	private static String getMetaName(String key) {
		String ret = niceMetaName.get(key);
		
		return (ret == null) ? key : ret;
	}
	
	private static HashMap<String, String> niceMetaName;
	static {
		niceMetaName = new HashMap<String, String>();
		
		niceMetaName.put("isreconciled", "Is reconciled");
	}
	
	/*
	 dialog layout:
	 -- when expanded, labels don't take up extra space (good!)
	 -- but, the stuff on the right side doesn't expand to fill the width
	 -- (if i make dialog layout do that, will it cause other breakage?)
	 -- it still has stdout debugging info -- use logging?
	 todo:
	 -- make a DialogLayout2
	 -- it'll be kind of like a 2-column grid layout
	 -- the left column will be exactly wide enough for max(min(widths))
	 -- the right column will take up the rest of the space
	 -- the height will be the minimum required -- extra on the bottom
	 refactoring:
	 -- make DialogLayout2
	 -- switch MetadataPanel to use DialogLayout2 (much simpler!)
	 -- switch other uses of DialogLayout to DialogLayout2
	 -- remove DialogLayout
	 -- rename DialogLayout2 to DialogLayout
	 */
}
