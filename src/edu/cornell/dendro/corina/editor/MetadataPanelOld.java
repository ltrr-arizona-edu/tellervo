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
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.webdbi.ResourceEvent;
import edu.cornell.dendro.corina.webdbi.ResourceEventListener;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.Iterator;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
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
   @version $Id$
*/
public class MetadataPanelOld extends JScrollPane implements SampleListener, ResourceEventListener {

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

	private JComponent makePopup(final MetadataField f) {		
		JComboBox popup = new JComboBox();
		popups.put(f.getVariable(), popup);
		
		resetPopup(f);

		if(f.isReadOnly())
			popup.setEnabled(false);
		
		popup.addItemListener(new ItemListener() { // user changes popup => we change metadata
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED && !resetting) {
					System.out.println("POPUP CHANGED!!!");
					popupChanged((JComboBox) e.getSource(), f);
				}
			}
		});
		
		// put in a flow, so it doesn't consume the entire width
		return Layout.flowLayoutL(popup);
	}
	
	private void popupChanged(JComboBox popup, final MetadataField f) {
		final int selectedIndex = popup.getSelectedIndex();
		final Object oldValue = s.getMeta(f.getVariable());
		final boolean wasAlreadyModified = s.isModified();
		final String field = f.getVariable();
		
		// do the actual change...
		if (selectedIndex == 0)
			s.removeMeta(field);
		else
			s.setMeta(field, f.getListItemValue(selectedIndex - 1));
		
		s.setModified();
		s.fireSampleMetadataChanged();

		s.postEdit(new AbstractUndoableEdit() {
			@Override
			public void undo() throws CannotUndoException {
				if (oldValue == null)
					s.removeMeta(field);
				else
					s.setMeta(field, oldValue);
				
				if(!wasAlreadyModified)
					s.clearModified();
				
				s.fireSampleMetadataChanged();
			}			
			
			@Override
			public void redo() throws CannotRedoException {
				if(selectedIndex == 0)
					s.removeMeta(field);
				else
					s.setMeta(field, f.getListItemValue(selectedIndex - 1));

				s.setModified();
				s.fireSampleMetadataChanged();
			}

			@Override
			public boolean canRedo() {
				return true;
			}

			@Override
			public String getPresentationName() {
				return f.getFieldDescription() + " Change";
			}

		});
	}

	/*
	 * This is old and so full of bugs.
	 * I'm keeping it here for reference; hopefully soon it will go away.
	 * 
	private JComponent makePopupOld(MetadataField f) {
		final String field = f.getVariable();

		// construct popup, with english labels to display
		String values[] = new String[1 + f.getValues().length];
		values[0] = I18n.getText("meta.unspecified");
		for (int j = 0; j < f.getValues().length; j++)
			values[j + 1] = I18n.getText("meta." + field + "." + f.getValues()[j]);
		JComboBox popup = new JComboBox(values);

		if(f.isReadOnly())
			popup.setEnabled(false);
		
		popups.put(field, popup); // store for listener later

		final MetadataField glue = f; // ack...

		resetPopup(f);
		popup.addItemListener(new ItemListener() { // user changes popup => we change metadata
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							final int which = ((JComboBox) e.getSource())
									.getSelectedIndex();

							int tmp = 0; // also in resetPopup() -- refactor?
							for (int i = 0; i < glue.getValues().length; i++)
								if (glue.getValues()[i].equals(s.getMeta(glue
										.getVariable())))
									break;
							// fall-through?
							final int oldWhich = tmp + 1; // index into values[] of the current value -- isn't there a method to compute this somewhere?  refactor.

							if (which == 0)
								s.removeMeta(field);
							else
								s.setMeta(field, glue.getValues()[which - 1]);

							// undoable
							s.postEdit(new AbstractUndoableEdit() {
								private boolean wasMod = s.isModified();

								public void undo() throws CannotUndoException {
									if (oldWhich == 0)
										s.removeMeta(field);
									else
										s.setMeta(field,
												glue.getValues()[oldWhich - 1]);
									if (!wasMod)
										s.clearModified();
									System.out.println("undo called, now at "
											+ oldWhich);
									s.setModified();
									s.fireSampleMetadataChanged();
								}

								public void redo() throws CannotRedoException {
									if (which == 0)
										s.removeMeta(field);
									else
										s.setMeta(field,
												glue.getValues()[which - 1]);
									System.out.println("redo called, now at "
											+ which);
									s.setModified();
									s.fireSampleMetadataChanged();
								}

								public boolean canRedo() {
									return true;
								}

								public String getPresentationName() {
									return glue.getFieldDescription() + " Change";
								}
							});

							s.setModified();
							s.fireSampleMetadataChanged();
							// BUG: undo doesn't fire this correctly.
							// what purpose does this serve, anyway, over
							// metadata-changed events?
						}
					}
				});

		// put in a flow, so it doesn't consume the entire width
		return Layout.flowLayoutL(popup);
	}
	*/

	private void doResetPopupContents(MetadataField f) {
		JComboBox popup = (JComboBox) popups.get(f.getVariable());
		
		popup.removeAllItems();
		popup.addItem(I18n.getText("meta.unspecified"));
		for (int j = 0; j < f.getListSize(); j++) 
			popup.addItem(f.getListItemDescription(j));
	}
	
	private boolean resetting = false;

	private void resetPopup(MetadataField f) {
		resetting = true;
		doResetPopupContents(f);
		doResetPopup(f);
		resetting = false;
	}
	
	private void doResetPopup(MetadataField f) {
		String field = f.getVariable();
		JComboBox popup = (JComboBox) popups.get(f.getVariable());
		// newValue can be a String, Integer, or null...
		Object nvTmp = s.getMeta(field);
		String newValue;
		
		// maybe it's null
		if (nvTmp == null) {
			popup.setSelectedIndex(0);
			return;
		} else {
			newValue = nvTmp.toString();
		}

		/*
		 * Don't bother this this anymore. No kludges!
		 * 
		// for "PITH", what mecki's corina called "+" i call "*"
		// (because the cheat sheet on the wall said this)
		// so i'll do that one for you.
		if (field.equals("PITH") && newValue.equals("+")) {
			s.setMeta("PITH", "*");
			s.setModified();
			s.fireSampleMetadataChanged();
			newValue = "*";
		}
		// BUG: isn't it "pith", not "PITH"?
		 * 
		 */

		// maybe it's one of the legal values
		for (int j = 0; j < f.getListSize(); j++) {
			if (f.getListItemValue(j).toUpperCase().equals(newValue.toUpperCase())) { // case-insensitive!
				popup.setSelectedIndex(j + 1);
				return;
			}
		}

		// finally, "?" sounds pretty unspecified to me.
		if (newValue.equals("?")) {
			s.removeMeta(field);
			popup.setSelectedIndex(0);
			return;
		}

		// crap.  we'll try asking the user what she was smoking.
		// (does this belong here?  NO!)
		String descriptions[] = new String[f.getListSize() + 1];
		descriptions[0] = "unspecified";
		for (int i = 0; i < f.getListSize(); i++)
			descriptions[i + 1] = f.getListItemDescription(i); 
		int x = JOptionPane.showOptionDialog(this, "The field \""
				+ f.getFieldDescription() + "\" has value \"" + newValue
				+ "\", which I don't understand.  What did you mean?",
				"Re-enter Value", JOptionPane.YES_NO_OPTION, // i think this is meaningless, in this context
				JOptionPane.QUESTION_MESSAGE, null, // no icon
				descriptions, null); // NO DEFAULT -- important!
		/*
		 what i really want: don't bug the user, until/unless she
		 wants to change this value.  so, for example:

		 [unspecified]  invalid value: XXX

		 -- if you don't change it, it gets saved as it was
		 -- if you do change it, you're forced to pick a valid value

		 that's much nicer than a dialog.

		 WRITEME.
		 */

		// result (x) is index into f.values -- store this.
		if (x == 0)
			s.removeMeta(field);
		else
			s.setMeta(field, f.getListItemValue(x - 1));
		s.setModified();
		s.fireSampleMetadataChanged();

		// oh, wait, i'm supposed to select something in a popup.  gotcha.
		popup.setSelectedIndex(x);
	}

	/**
	 Construct a new view of the metadata for a sample.

	 @param sample the Sample
	 */
	public MetadataPanelOld(Sample sample) {
		// copy data
		this.s = sample;

		// no border!
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
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
				p.add(makePopup(f), c);
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
					resetPopup(f);
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
						
						resetPopup(f);
					}					
				}
			});
		}
	}
	
	public void sampleElementsChanged(SampleEvent e) {
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
