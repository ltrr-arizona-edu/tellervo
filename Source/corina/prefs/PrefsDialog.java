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

package corina.prefs;

import corina.gui.HasPreferences;
import corina.gui.ButtonLayout;
import corina.gui.DialogLayout;
import corina.gui.UserCancelledException;
import corina.gui.Bug;

import java.io.IOException;
import java.util.List;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.Box;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

// todo:
// - i18n
// - (unit test?)
// - make me a jdialog, so OS X doesn't clear the menubar (why haven't i done this?)
// - policy for selecting a tab?
// --- from crossdate jframe, select crossdate tab, etc.
// --- start up in last tab selected (e.g., corina.prefs.lasttab=Graph)

/**
   A dialog for collecting user preferences.

   <p>Uses the Prefs/PrefsTemplate classes.  Not yet
   localized.  Don't use the constructor; the correct way to show this
   is Preferences.showPreferences(), which ensures only
   one preferences dialog is ever present.</p>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class PrefsDialog extends JFrame {
    // static reference: there can be only one! -- gets null on close, how?
    private static PrefsDialog me = null;

    // data
    private List options, categories;

    // the generic right-panel for a bunch of Options.
    // THIS IS GODAWFUL UGLY.  BREAK IT DOWN INTO METHODS OR SOMETHING.
    private class OptionsPanel extends JPanel {
        public OptionsPanel(String category) {
            // gui setup: gridbag!
            super.setLayout(new DialogLayout());

            for (int i=0; i<options.size(); i++) {
                final PrefsTemplate.Option o = (PrefsTemplate.Option) options.get(i);
                if (o.category.equals(category)) {

                    // ignore hidden prefs
                    if (o.type == PrefsTemplate.Option.TYPE_HIDDEN)
                        continue;
                    
                    // create and add the constrained label
                    String label;
                    if (o.type != PrefsTemplate.Option.TYPE_BOOL) {
                        label = o.description;
                    } else {
                        label = "";
                    }

                    switch (o.type) {

                        case PrefsTemplate.Option.TYPE_COLOR: {
                            // popup
                            ColorPopup p = new ColorPopup(Color.decode(o.value), o);
                            JPanel t = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                            t.add(p);
                            super.add(t, label);
                            break;
                        }

                        case PrefsTemplate.Option.TYPE_BOOL: {
                            JCheckBox t = new JCheckBox(o.description);
                            t.setSelected(o.value.equals("true"));
                            t.addChangeListener(new ChangeListener() {
                                public void stateChanged(ChangeEvent ce) {
                                    // use Boolean for this?
                                    o.value = (o.value.equals("true") ? "false" : "true");
                                }
                            });
                            super.add(Box.createVerticalStrut(8)); // need more space for all types?  wha?  hrm...
                            super.add(t, label);
                            break;
                        }

                        case PrefsTemplate.Option.TYPE_FONT: {
                            JPanel flow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

                            final JLabel p = new JLabel("Sample");
                            p.setFont(Font.decode(o.value));

                            JButton b = new JButton("Change font");
                            b.addActionListener(new AbstractAction() {
                                public void actionPerformed(ActionEvent ae) {
                                    try {
                                        o.value = FontChooser.showDialog(me /* !!! */, "Choose new font", o.value);
                                        p.setFont(Font.decode(o.value));
                                    } catch (UserCancelledException uce) {
                                        // do nothing
                                    }
                                }
                            });

                            flow.add(p);
                            flow.add(Box.createHorizontalStrut(8));
                            flow.add(b);

                            super.add(flow, label);
                            break;
                        }

                        default: { // must be PrefsTemplate.Option.TYPE_STRING
                                   // create the edit box
                            final JTextField t = new JTextField(System.getProperty(o.property, ""), 20);

                            // listener
                            t.getDocument().addDocumentListener(new DocumentListener() {
                                public void changedUpdate(DocumentEvent e) {
                                    update(e);
                                }
                                public void insertUpdate(DocumentEvent e) {
                                    update(e);
                                }
                                public void removeUpdate(DocumentEvent e) {
                                    update(e);
                                }
                                private void update(DocumentEvent e) {
                                    // get text from the widget, store it
                                    o.value = t.getText();
                                }
                            });

                            // put it in
                            super.add(t, label);
                        }
                    }
                }
            }

            // add some extra space below the last component.
            super.add(Box.createVerticalStrut(18), null);
        }
    }

    public static void updateAll() {
        // call refreshFromPreferences on every Frame that HasPreferences
        Frame f[] = Frame.getFrames();
        for (int i=0; i<f.length; i++)
            if (f[i] instanceof HasPreferences)
                try {
                    ((HasPreferences) f[i]).refreshFromPreferences();
                } catch (Exception e) {
                    Bug.bug(e);
                }

        // save all
        try {
            Prefs.save();
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null,
                                          "There was an error saving your preferences:\n" +
                                          ioe.getMessage(),
                                          "Error saving preferences",
                                          JOptionPane.ERROR_MESSAGE);
        }
    }

    private void apply() {
        // apply all options
        for (int i=0; i<options.size(); i++) {
            PrefsTemplate.Option o = (PrefsTemplate.Option) options.get(i);
            System.setProperty(o.property, o.value);
        }

        // update them
        updateAll();
    }

    // cancel: reset options, close frame
    private void cancel() {
        // reset all options to their defaults
        for (int i=0; i<options.size(); i++)
            ((PrefsTemplate.Option) options.get(i)).readValue();

        // i'm outta here
        goAway();
    }

    // hide myself
    private void goAway() {
        dispose();
        me = null;
    }

    /** Show the preferences dialog.  If there is already a Corina
	preferences dialog, this restores it and brings it to the
	front.  This prevents multiple preferences dialogs from
	appearing, which would serve only to confuse the user. */
    public static void showPreferences() {
        if (me == null) {
            me = new PrefsDialog();
        } else { // FIXME: THIS CODE IS NEVER USED (...why?)
            me.setVisible(true); // despite DISPOSE_ON_CLOSE, it's just getting hidden?
            me.setState(NORMAL);
            me.toFront();
        }
    }

    // show a specific tab
    public static void showPreferences(String tab) {
        // show dialog
        showPreferences();

        // select the tab
        for (int i=0; i<me.categories.size(); i++) {
            if (me.categories.get(i).equals(tab)) {
                me.tabs.setSelectedIndex(i);
                return;
            }
        }

        // note: if we're given a bogus tab name, it just falls
        // through, and the first tab remains selected.
    }

    private JTabbedPane tabs;

    // private, because nobody outside of showPreferences() should ever construct me
    private PrefsDialog() {
        setTitle("Preferences");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE); // dispose?  why?
        setResizable(false);

        // icon!
        ClassLoader cl = this.getClass().getClassLoader();
        setIconImage(new ImageIcon(cl.getResource("toolbarButtonGraphics/general/Preferences16.gif")).getImage());

        // get categories, options from Prefs
        categories = PrefsTemplate.getCategories();
        options = PrefsTemplate.getOptions();

        // use my own panel, so i can add a border
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(12, 20, 20, 20));
        setContentPane(p);

        // fill up the tabbed pane
        tabs = new JTabbedPane();
        for (int i=0; i<categories.size(); i++) {
            String cat = (String) categories.get(i);
            tabs.addTab(cat, new OptionsPanel(cat));
        }
        p.add(tabs);

        // button: okay
        JButton okay = new JButton("OK");
        okay.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                // apply, then close
                apply();
                goAway();
            }
        });
        getRootPane().setDefaultButton(okay); // (it's the default, of course)

        // button: cancel
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                cancel();
            }
        });

        // esc => cancel, too.
        // REFACTOR SO THIS CAN USE OKCANCEL, TOO
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancel();
                }
            }
        });

        // buttons
        JPanel buttons = new JPanel(new ButtonLayout());
        buttons.add(cancel);
        buttons.add(okay);
        p.add(Box.createVerticalStrut(12));
        p.add(buttons);

        // pack
        pack();

        // center it -- REFACTOR ME
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(new Point(d.width/2 - getWidth()/2,
                              d.height/2 - getHeight()/2));

        // show it
        show();
    }
}
