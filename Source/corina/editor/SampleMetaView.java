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
import corina.Metadata;
import corina.Metadata.Field;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Hashtable;
import java.util.ResourceBundle;

import java.awt.FlowLayout;
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
A view of the metadata of a Sample.

 Improvements to make:

     -- Scroll when the user tabs out of view; scroll if the user
     types into a text component that's not in view

     -- Possible to use only one DocumentListener that works for all
     fields?  Probably not worth the effort (they share code, now,
     anyway).

     -- Make any changes here Undoable.  This will probably involve
     creating an inner class for "FieldEdit" ("MetadataEdit"?), which implements
     Undoable.  The fun part will be to coalesce multiple
     FieldEdits together -- if I type "Quercus", and then select Undo,
     it should un-type "Quercus", not just the "s".

 -- Migrate fully to new popups, when I get the ok.
 
 -- Figure out how to use a popup for the species.  (!)
 
   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public class SampleMetaView extends JScrollPane implements SampleListener {

    // the sample to view
    private Sample s;

    // hmm, would it be possible to use only one INSTANCE of this, even?
    // that sure would save on the enabled/disabled crap.
    private static class UpdateListener implements DocumentListener {
        private Sample s;
        private Field f;
        private boolean enabled=true;
        public UpdateListener(Sample s, Field f) {
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
                return null; // can't happen
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
                if (value.equals(s.meta.get(f.variable)))
                    return;
                if (s.meta.get(f.variable)==null && ((String)value).length()==0)
                    return;
            } catch (NullPointerException npe) {
                // ignore?
            }

            // (if it's a String, try to make it an Integer)
            try {
                value = Integer.decode((String) value);
            } catch (NumberFormatException nfe) {
                // ok, it's just a String
            }

            // store it, and dirty the sample
            if (value instanceof String && ((String) value).length() == 0)
                s.meta.remove(f.variable);
            else
                s.meta.put(f.variable, value);
            s.setModified();
            s.fireSampleMetadataChanged();
        }
    }

    private Map components = new Hashtable(); // field => component hash
    private List listeners = new ArrayList(); // all the listeners

    private Map popups = new Hashtable(); // field varname => jcombobox mapping

    private JComponent makePopup(Field f) {
        final String field = f.variable;

        // construct popup, with english labels to display
        String[] values = new String[1 + f.values.length];
        values[0] = "- not specified -";
        for (int j=0; j<f.values.length; j++)
            values[j+1] = msg.getString(field + "." + f.values[j]);
        JComboBox popup = new JComboBox(values);
        // check f.readonly?
        popups.put(field, popup); // store for listener later

        final Field glue = f; // ack...

        resetPopup(f);
        popup.addItemListener(new ItemListener() { // user changes popup => we change metadata
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    final int which = ((JComboBox) e.getSource()).getSelectedIndex();

                    int tmp = 0; // also in resetPopup() -- refactor?
                    for (int i=0; i<glue.values.length; i++)
                        if (glue.values[i].equals(s.meta.get(glue.variable)))
                            break;
                    // fall-through?
                    final int oldWhich = tmp+1; // index into values[] of the current value -- isn't there a method to compute this somewhere?  refactor.

                    // debug
                    System.out.println("from " + oldWhich + " to " + which);

                    if (which == 0)
                        s.meta.remove(field);
                    else
                        s.meta.put(field, glue.values[which-1]);

                    // undoable
                    s.postEdit(new AbstractUndoableEdit() {
                        private boolean wasMod = s.isModified();
                        public void undo() throws CannotUndoException {
                            if (oldWhich == 0)
                                s.meta.remove(field);
                            else
                                s.meta.put(field, glue.values[oldWhich-1]);
                            if (!wasMod)
                                s.clearModified();
                            System.out.println("undo called, now at " + oldWhich);
                            s.setModified();
                            s.fireSampleMetadataChanged();
                        }
                        public void redo() throws CannotRedoException {
                            if (which == 0)
                                s.meta.remove(field);
                            else
                                s.meta.put(field, glue.values[which-1]);
                            System.out.println("redo called, now at " + which);
                            s.setModified();
                            s.fireSampleMetadataChanged();
                        }
                        public boolean canRedo() {
                            return true;
                        }
                        public String getPresentationName() {
                            return glue.description + " Change";
                        }
                    });
                    
                    s.setModified();
                    s.fireSampleMetadataChanged();
                }
            }
        });

        // stuff in jpanel, so it doesn't consume the entire width
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        flow.add(popup);
        return flow;
    }
    private void resetPopup(Field f) {
        String field = f.variable;
        JComboBox popup = (JComboBox) popups.get(f.variable);
        String newValue = (String) s.meta.get(field);

        // maybe it's null
        if (newValue == null) {
            popup.setSelectedIndex(0);
            return;
        }

        // for "PITH", what mecki called "+" i call "*", so i'll do that one for you.
        if (field.equals("PITH") && newValue.equals("+")) {
            s.meta.put("PITH", "*");
            s.setModified();
            s.fireSampleMetadataChanged();
            newValue = "*";
        }
        
        // maybe it's one of the legal values
        for (int j=0; j<f.values.length; j++) {
            if (f.values[j].toUpperCase().equals(newValue.toUpperCase())) { // case-insensitive!
                popup.setSelectedIndex(j+1);
                return;
            }
        }

        // crap.  we'll try asking the user what she was smoking.
        // (does this belong here?)
        String descriptions[] = new String[f.values.length+1];
        descriptions[0] = "unspecified";
        for (int i=0; i<f.values.length; i++)
            descriptions[i+1] = msg.getString(f.variable + "." + f.values[i]);
        int x = JOptionPane.showOptionDialog(this,
                                     "The field \"" + f.description + "\" has value \"" + newValue + "\", which I can't parse.  What did you mean?",
                                     "Re-enter this value",
                                     JOptionPane.YES_NO_OPTION, // i think this is meaningless
                                     JOptionPane.QUESTION_MESSAGE,
                                     null, // no icon
                                     descriptions,
                                     null); // NO DEFAULT -- important
        
        // result (x) is index into f.values -- store this.
        if (x == 0)
            s.meta.remove(field);
        else
            s.meta.put(field, f.values[x-1]);
        s.setModified();
        s.fireSampleMetadataChanged();

        // oh, wait, i'm supposed to select something in a popup.  gotcha.
        popup.setSelectedIndex(x);
    }

    /** Construct a new view of the metadata for a sample.
        @param sample the Sample */
    public SampleMetaView(Sample sample) {
        // copy data
        this.s = sample;

        // temp panel to add stuff to; we'll scroll this
        final JPanel p = new JPanel(new GridBagLayout());

        // constraints for all components
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 0.0;

        // add the fields, one at a time
        for (int i=0; i<Metadata.fields.length; i++) {
            // (row)
            c.gridy = i;

            // get field, and its value
            Field f = Metadata.fields[i];

            // create and add the constrained label
            c.gridx = 0;
            c.anchor = GridBagConstraints.EAST;
            c.weightx = 0.0; // no effect?
            c.fill = GridBagConstraints.NONE;
            JLabel l = new JLabel(f.description + ":");
            l.setToolTipText(f.help);
            JPanel fff = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // putting it in a jpanel lines up the lines -- REFACTOR
            fff.add(l);
            p.add(fff, c);

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
            if (f.variable.equals("species")) {
                JPanel fffff = new JPanel(new FlowLayout(FlowLayout.LEFT));
                fffff.add(new SpeciesPopup(s));
                p.add(fffff, c);
                continue;
            }
*/
 
            if (f.values != null) {
                p.add(makePopup(f), c);
                continue;
            } // integrate this clause into the rest...  easier once old-style-popups are all gone
            
            // if it has no choices, a plain jtextfield is perfect.
            // if it needs several lines, though, use a jtextarea.
            JTextComponent x = makeTextBlock(f);
            
            // add to the panel
//            p.add(x, c);
            // use a sub-panel, so the left-alignment is ok.  without this, on OS X it
            // looks passable, but on windows it looks horrible.
            JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
            if (x instanceof JTextArea)
                flow.add(new JScrollPane(x));
            else
                flow.add(x);
            p.add(flow, c);

            // add to hashmap -- only used for textcomponents
            //if (x instanceof JTextComponent)
                components.put(f.variable, x);

            // first one?  focus!
            if (i == 0)
                x.requestFocus();
        }

        // put a filler-panel below everything
        c.gridx = 0;
        c.gridy = Metadata.fields.length;
        c.weighty = 1.0;
        p.add(new JPanel(), c);

        // stuff everything in a scrollpane (=me)
        setViewportView(p);
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // set some sane unit increments (one line per click), but this can only be done after it's packed
        final JScrollPane glue = this;
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                GridBagLayout gbl = (GridBagLayout) p.getLayout();
                int h = gbl.getLayoutDimensions()[1][0]; // (1=height, 0=first row)  "Most applications do not call this method directly."  (I'm special.)
                glue.getVerticalScrollBar().setUnitIncrement(h);
                glue.getHorizontalScrollBar().setUnitIncrement(h);
                // todo: i can remove myself now, right?  [--wtf does this comment mean?]
            }
        });
    }

    private JTextComponent makeTextBlock(Field f) {
        // number of lines?
        int lines = f.lines;

        // value -- REFACTOR?
        Object hash = s.meta.get(f.variable);
        String value = (hash==null ? "" : hash.toString());

        // HACK2: index_type gets looked up now; add index_type.options=-1,1,2,... to metadata properties file, perhaps
        if (f.variable.equals("index_type") && hash!=null)
            value = msg.getString("index_type." + hash);

        // build component
        // (cast required to make one arg assignable to the other -- see jls chapter 15, section 25, clause 4)
        JTextComponent t = (lines > 1 ?
                            new JTextArea(value, lines, 40) :
                            (JTextComponent) /* ick! */ new JTextField(value, 32));

        if (f.readonly) {
            // if read-only, it's not editable
            t.setEditable(false);
        } else {
            // listener for textfield
            UpdateListener u = new UpdateListener(s, f);
            t.getDocument().addDocumentListener(u);
            listeners.add(u);
        }

        // tooltip for the editor, too (REFACTOR!)
        t.setToolTipText(f.help);

        return t;
    }
    
    // first label
    private JLabel firstLabel=null;

    // localizations
    private static ResourceBundle msg = ResourceBundle.getBundle("MetadataBundle");

    public void sampleRedated(SampleEvent e) {
        // abs/rel are changed by redater, but that fires a metadataChanged event
    }
    public void sampleDataChanged(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) {
        // no way to find out which fields changed!  darn...
        // this means we have to disable all listeners right at
        // the start, not just as we go.
        for (int i=0; i<listeners.size(); i++)
            ((UpdateListener) listeners.get(i)).setEnabled(false);

        for (int i=0; i<Metadata.fields.length; i++) {
            Field f = Metadata.fields[i];
            
            // is this my format popup?  if so, choose the correct one...
            if (popups.containsKey(f.variable)) {
                resetPopup(f);
                continue;
            }

            // get value -- DUPLICATE CODE, REFACTOR
            Object hash = s.meta.get(f.variable);
            String value = (hash==null ? "" : hash.toString());

            // get component
            String field = f.variable;
            JTextComponent comp = (JTextComponent) components.get(field);

            // HACK: index_type gets looked up now -- DUPLICATE CODE, REFACTOR
            if (f.variable.equals("index_type") && hash!=null)
                value = msg.getString("index_type." + hash);

            try {
                // text component?
                comp.setText(value);
            } catch (IllegalStateException ise) {
                // there's got to be a better way to do this:
                // System.out.println("illegal state!  (skipping " + field + ")");
                continue;
                // this is caused by trying to update myself.  if something got thrown
                // here, the listeners wouldn't get re-enabled, and users would only
                // see the first letter they typed.  they hate that.  trust me.
            }
        }

        // re-enable all listeners
        for (int i=0; i<listeners.size(); i++)
            ((UpdateListener) listeners.get(i)).setEnabled(true);
    }
    public void sampleFormatChanged(SampleEvent e) { }
    public void sampleElementsChanged(SampleEvent e) { }
}
