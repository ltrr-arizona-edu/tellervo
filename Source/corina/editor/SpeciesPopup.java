package corina.editor;

/*
 what's the job description for the species popup?
 -x- list all the common (to the user) species
 -x- also [below a separator] list "Other..."
 --- Other... dialog shows all known species, plus blank
 -x- when user selects a species, update backing Sample
 -x- if Sample gets updated, update popup
 --- if common-list gets updated, update all popups?
 -x- always use 4-letter code for sample, latin name for popup
 -x- if a non-listed species is selected, display it first
 -x- migration: 4-letter code stays 4-letter code
 -x- if a species matches exactly, convert to 4-letter-code
 --- what if it's different?  keep old way... (?)
 */

import corina.Species;
import corina.UnknownSpeciesException;
import corina.Sample;
import corina.gui.ButtonLayout;

import java.util.Map;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

public class SpeciesPopup extends JComboBox { // implements my own listeners?

    private Sample s;
    private boolean customSpecies=false;
    public SpeciesPopup(Sample s) {
        this.s = s;
        String species = (String) s.meta.get("species");

        // -not specified-, as usual
        addItem("- not specified -");

        // parse and normalize
        if (species!=null) {
            try {
                String code = Species.getCode(species);
                s.meta.put("species", code);
                species = code;
            } catch (UnknownSpeciesException use) {
                // ignore?
            }
        }

        // add the most common species
        int n = Species.common.size();
        for (int i=0; i<n; i++) {
            try {
                String name = Species.getName((String) Species.common.get(i));
                addItem(name);
            } catch (UnknownSpeciesException use) {
                // ignore -- can't happen: Species.common all exist
            }
        }

        // select whichever it is (maybe a special one)
        selectSpecies(species);
        
        // add "other"
        addItem("Other...");

        // add listener
        final Sample glue = s;
        addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // for undo
                final boolean wasMod = glue.isModified();
                final String oldSpecies = (String) glue.meta.get("species");
                String newSpecies = null; // to be set later...

                // from selected index, choose the species
                int i = getSelectedIndex(), m = getItemCount();
                if (i == 0) {
                    // not spec: remove
                    removeCustomEntry();
                    glue.meta.remove("species");
                    // newSpecies is null, no need to change
                    glue.fireSampleMetadataChanged();
                } else if (i == m-1) {
                    // "Other..." dialog
                    new OtherDialog(); // (this)?
                    // be sure to set newSpecies!
                } else if (i == 1 && customSpecies) {
                    // custom species: user must have cancelled, right?
                    // so do nothing...
                    // don't even post an undo!
                    return; // assumes nothing else happens in this constructor.
                } else {
                    i -= (customSpecies ? 2 : 1);
                    // common species |i|
                    glue.meta.put("species", Species.common.get(i));
                    removeCustomEntry();
                    newSpecies = (String) Species.common.get(i);
                    glue.fireSampleMetadataChanged();
                }

                // undoability!
                final String newSpeciesGlue = newSpecies; // augh!
                glue.postEdit(new AbstractUndoableEdit() {
                    public void undo() throws CannotUndoException {
                        // -- set species back to old one
                        selectSpecies(oldSpecies);
                        // -- set/clear modified
                        if (wasMod)
                            glue.setModified();
                        else
                            glue.clearModified();
                        // -- fire meta-changed
                        glue.fireSampleMetadataChanged();
                        // HEY, this is a really common pattern.  can it be shoved up into Sample, maybe?
                    }
                    public void redo() throws CannotRedoException {
                        // -- set species back to new one
                        selectSpecies(newSpeciesGlue);
                        // -- set modified
                        glue.setModified();
                        // -- fire meta-changed
                        glue.fireSampleMetadataChanged();
                    }
                    public boolean canRedo() {
                        return true;
                    }
                    public String getPresentationName() {
                        return "Species Change";
                    }
                });
            }
        });
    }

    private void removeCustomEntry() {
        if (customSpecies) {
            removeItemAt(1);
            customSpecies = false;
        }
    }

    // given a species |species|, select that item in the popup
    private void selectSpecies(String code) {
        System.out.println("selecting code=" + code);
        removeCustomEntry();

        // null?  don't bother
        if (code == null) {
            setSelectedIndex(0);
            return;
        }

        if (Species.common.contains(code)) {
            // it's a common one, so select it.
            int i = Species.common.indexOf(code);
            setSelectedIndex(1 + i);
        } else {
            String value;
            try {
                // look up its name, at least
                value = Species.getName(code);
            } catch (UnknownSpeciesException use) {
                // just use it as-is
                value = code;
            }

            // add it, and select it
            insertItemAt(value, 1); // MAYBE BUG: will this get set correctly when selected?  doesn't need to...
            setSelectedIndex(1);
            customSpecies = true;
        }
    }
    
    private class OtherDialog extends JDialog {
        OtherDialog() {
            super((JDialog)null, "Other Species"); // null?  what's the arg?  i'm modal, right?

            // -- all species, by name, in a big list
            // -- (by default, select the current one?)
            // -- checkbox, with "[x] Other: [          ]" field (dimmed if not checked, else list is dimmed)
            JCheckBox other = new JCheckBox("Other:");

            // -- Cancel, OK buttons
            JButton cancel, ok;
            JPanel buttons = new JPanel(new ButtonLayout());
            cancel = new JButton("Cancel");
            ok = new JButton("OK");
            buttons.add(cancel);
            buttons.add(ok);
            getContentPane().add(buttons, BorderLayout.SOUTH);

            // show myself
            pack();
            this.show(); // what?
        }
    }
}
