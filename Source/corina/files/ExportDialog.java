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

package corina.files;

import corina.Sample;
import corina.gui.Buttons;
import corina.gui.DialogLayout;
import corina.gui.UserCancelledException;
import corina.gui.FileDialog;
import corina.gui.Bug;
import corina.gui.Help;
import corina.util.OKCancel;
import corina.util.Overwrite;
import corina.util.TextClipboard;
import corina.browser.FileLength;
import corina.ui.Builder;
import corina.ui.I18n;

import java.io.File;
import java.io.StringWriter;
import java.io.IOException;

import java.awt.Font;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;

// issues:
// -- heidelberg/hohenheim need wj -- give intelligent error when they fail?  or don't present them?
// -- this is a fairly basic class, probably should have javadoc
// -- filetype should have a suggestedExtension, so "ok" here gives you a default of "<old-filename>.<sug-ext>"
// -- make it resizable, if i can figure out how to get the jtextarea to resize, as well.

/*
 notes on this implementation:

 -- users find it informative, and even fun, to use this dialog.  every single person i've seen
 has figured out how to use it instantly without being told.  (another dialog that shares these
 traits is the old mac color chooser.)  all signs of a good interface, i think.

 -- it was fairly fast to program, given (1) that i'd been working with swing for over a year,
 and (2) that i had a collection of classes (buttonlayout, dialoglayout, okcancel, filelength,
 overwrite) ready to use, in addition to the actual data and i/o routines.

 -- this means to me that (1) swing has a steep learning curve, and (2) it's missing a bit of
 basic functionality that should be included, and would make programmers' lives easier.

 -- unfortunately, given the strict separation between the language, the default class library,
 and user classes, i can't just extend java to include features i want; they'll always be second-
 class citizens (so to speak).

 -- it doesn't behave exactly the same on windows and mac.  i don't know why it doesn't scroll to
 the top on windows.  (windows users don't seem to notice minor bugs, anyway.)
*/

/**
   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/
public class ExportDialog extends JDialog {

    private static final String EXPORTERS[] = new String[] {
        "corina.files.Tucson",
        "corina.files.TwoColumn",
        "corina.files.Corina",
        "corina.files.TRML",
        "corina.files.Heidelberg",
        "corina.files.Hohenheim",
        "corina.files.TSAPMatrix",
        "corina.files.RangesOnly",
        "corina.files.Spreadsheet",
        "corina.files.PackedTucson",
    };

    private JComboBox popup;
    private JLabel size;
    private JTextArea preview;
    private JButton ok;
    private Sample sample;

    public ExportDialog(Sample s, Frame parent) {
        super(parent, I18n.getText("export"), true);
        this.sample = s;

        // filetype popup
        int n = (s.elements!=null ? EXPORTERS.length : EXPORTERS.length-3); // "last 3 are summed-only"
        String v[] = new String[n];
        for (int i=0; i<n; i++) {
            try {
                Filetype f = (Filetype) Class.forName(EXPORTERS[i]).newInstance();
                v[i] = f.toString();
            } catch (Exception e) {
                Bug.bug(e);
            }
        }
        popup = new JComboBox(v);
        popup.setMaximumRowCount(10); // i have 9 now, and this shouldn't scroll for so few
        popup.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });

        // size preview
        size = new JLabel("");

        // text preview
        preview = new JTextArea(12, 80) {
            public boolean isManagingFocus() { // what's this for?  i've forgotten, document me!
                return false;
            }
        };
        Font oldFont = preview.getFont();
        preview.setFont(new Font("monospaced", oldFont.getStyle(), oldFont.getSize()));
        preview.setEditable(false);

        // in a panel
        JPanel tuples = new JPanel(new DialogLayout());
        tuples.add(popup, I18n.getText("filetype"));
        tuples.add(size, I18n.getText("size"));
        tuples.add(new JScrollPane(preview), I18n.getText("export_preview"));

        // buttons
        JButton help = Builder.makeButton("help");
	Help.addToButton(help, "exporting");
	// BETTER?: look at state of filetype popup, and present appropriate format page

        JButton copy = Builder.makeButton("copy");
        copy.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TextClipboard.copy(preview.getText());
            }
        });
        JButton cancel = Builder.makeButton("cancel");
        ok = Builder.makeButton("ok");

        // button actions
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // just close
                dispose();
            }
        });
        final JDialog me = this;
        ok.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // close
                    dispose();

                    // ask for filename
                    String fn = FileDialog.showSingle(I18n.getText("export"));

                    // check for already-exists
                    if (new File(fn).exists() && Overwrite.overwrite(fn))
                        return;

                    // save it
                    int i = popup.getSelectedIndex();
                    Filetype f = (Filetype) Class.forName(EXPORTERS[i]).newInstance();
                    f.save(fn, sample);
                } catch (UserCancelledException uce) {
                    // do nothing
                } catch (IOException ioe) {
                    // problem saving, tell user
                    JOptionPane.showMessageDialog(me,
						  I18n.getText("xport_error") + ioe,
                                                  I18n.getText("export_error_title"),
						  JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    // problem creating filetype, or npe, or whatever -- bug.
                    Bug.bug(ex);
                }
            }
        });
        
        // in a panel
	Buttons buttons = new Buttons(help, copy, null, cancel, ok);
        buttons.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        setContentPane(main);
        main.add(tuples, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        OKCancel.addKeyboardDefaults(ok);

        // initial view
        updatePreview();

        // show
        pack();
        setResizable(false);
        show();
    }

    // use the same StringWriter for all previews, because that way it uses the same StringBuffer
    private StringWriter writer = new StringWriter(10240); // 10K

    private void updatePreview() {
        int i = popup.getSelectedIndex();
        try {
            // save to buffer
            setCursor(new Cursor(Cursor.WAIT_CURSOR)); // this could take a second...
            Filetype f = (Filetype) Class.forName(EXPORTERS[i]).newInstance();
            StringBuffer buf = writer.getBuffer();
            buf.delete(0, buf.length()); // clear it
            f.save(writer, sample);
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // ok, done with the slow part

            // update views
            preview.setText(buf.toString());
            size.setText(new FileLength(buf.length()).toString());
            ok.setEnabled(true);

            // move cursor to start -- this scrolls to the top, as well
            preview.setCaretPosition(0);
        } catch (IOException ioe) {
            // for now, use this to mean "inappropriate data/filetype" -- WTFE will be better, though.
//            size.setText("");
//            preview.setText("Problem using this format:\n" + ioe.getMessage());
//            ok.setEnabled(false);

            // problem saving it -- bug
            Bug.bug(ioe);
        } catch (Exception e) {
            // problem creating the filetype -- bug
            Bug.bug(e);
        }
    }
}
