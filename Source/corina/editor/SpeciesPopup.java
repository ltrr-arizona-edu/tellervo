package corina.editor;

import corina.Species;
import corina.UnknownSpeciesException;
import corina.Sample;
import corina.gui.ButtonLayout;
import corina.gui.Bug;
import corina.util.OKCancel;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Vector;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

public class SpeciesPopup extends JComboBox {

    private Sample s;
    private boolean customSpecies=false;
    private Editor parent;
    public SpeciesPopup(Sample s, Editor editor) {
        this.s = s;
        parent = editor;

        // don't show scrollbars unless i really need 'em -- 24 should work pretty well
        setMaximumRowCount(24);

        // -not specified-, as usual
        addItem("- not specified -");

        // parse and normalize
        String species = (String) s.meta.get("species");
        if (species != null) {
            try {
                String code = Species.getCode(species);
                s.meta.put("species", code);
                species = code;
            } catch (UnknownSpeciesException use) {
                // ignore.
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
                    new OtherDialog(parent);
                    newSpecies = (String) glue.meta.get("species");
                    selectSpecies(newSpecies);
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

                // set modified
                if ((newSpecies!=null && !newSpecies.equals(oldSpecies)) ||
                    (oldSpecies!=null && !oldSpecies.equals(newSpecies)))
                    glue.setModified();

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
//        System.out.println("selecting code=" + code);
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

    //
    // "other..." dialog
    //

    private class OtherDialog extends JDialog {
        OtherDialog(Editor parent) {
            super(parent, "Species", true);
            final Sample sample = parent.getSample();
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
            getContentPane().add(panel);

            // label
            JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            labelPanel.add(new JLabel("Choose the species:", JLabel.LEFT));
            panel.add(labelPanel);

            // TRY: use 2 lists: "Abies", "Pinus", "Quercus", etc. on the left size,
            // and update the right size when something is selected:
            // "Abies", "Abies alba", "Abies amabilis", etc.

            // list of all species
            Vector speciesNames = new Vector();
            try {
                Enumeration e = Species.species.keys();
                while (e.hasMoreElements()) {
                    String code = (String) e.nextElement();
                    speciesNames.add(Species.getName(code));
                }
            } catch (UnknownSpeciesException use) {
                // can't happen
                Bug.bug(use);
            }
            Collections.sort(speciesNames);
            
            // WRITEME
            final JList speciesList = new JList(speciesNames);
            panel.add(new JScrollPane(speciesList));
            // TODO: indent list,field 20px to the right

            // double-click in list selects that
            
            // -- (by default, select the current one.)
            // if user types something, automatically check the checkbox

            // (check box needs me)
            final JTextField otherField = new JTextField("", 20);

            final JCheckBox otherCheck = new JCheckBox("Or type in another species:");
            otherCheck.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    boolean other = otherCheck.isSelected();
                    speciesList.setEnabled(!other);
                    otherField.setEnabled(other);
                    if (other)
                        otherField.requestFocus();
                    else
                        speciesList.requestFocus();
                }
            });
            // win32: alt-O checks box, and sends focus to textfield
            JPanel checkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            checkPanel.add(otherCheck);
            panel.add(checkPanel);

            // --- (8px)
            panel.add(Box.createVerticalStrut(8));

            // entry blank (otherField)
            // WRITE ME
            //            JTextField otherField = new JTextField("", 20);
            otherField.setEnabled(false);
            otherField.setMaximumSize(otherField.getPreferredSize());
            panel.add(otherField);
            // TODO: indent list,field 20px to the right

            // --- (14px)
            panel.add(Box.createVerticalStrut(14));
            
            // -- Cancel, OK buttons
            JPanel buttons = new JPanel(new ButtonLayout());
            JButton cancel = new JButton("Cancel");
            cancel.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            JButton ok = new JButton("OK");
            ok.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // TODO: update sample, add to popup if needed
                    String species;
                    if (otherCheck.isSelected()) {
                        species = otherField.getText();
                        try {
//                            species = Species.getCode(species);
                            species = Species.closestSpecies(species);
                        } catch (UnknownSpeciesException use) {
                            // well, it really is an Other species.  that's fine.
                        }
                    } else {
                        try {
                            species = Species.getCode((String) speciesList.getSelectedValue());
                        } catch (UnknownSpeciesException use) {
                            // ignore, can't happen
                            species = "----";
                        }
                    }

                    // just set the sample; when this returns, the popup calls selectSpecies(),
                    // so i don't have to worry about that here.
                    sample.meta.put("species", species);
                    sample.fireSampleMetadataChanged();

                    // dispose after i find out what the species was.
                    dispose();
                }
            });
            buttons.add(cancel);
            buttons.add(ok);
            panel.add(buttons);
            OKCancel.addKeyboardDefaults(this, ok);

            // TODO:
            // -- focus on the list
            // -- allow typing at the list to select.

            // show myself
            pack();
//            setResizable(false); -- when layout is done, and the size is reasonable.
            this.show(); // what?
        }
    }
}
