package corina.files;

import corina.Sample;
import corina.gui.ButtonLayout;
import corina.gui.DialogLayout;
import corina.gui.UserCancelledException;
import corina.gui.FileDialog;
import corina.gui.Bug;
import corina.util.OKCancel;
import corina.util.Overwrite;
import corina.browser.FileLength;

import java.io.File;
import java.io.StringWriter;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.ResourceBundle;

import java.awt.Font;
import java.awt.Frame;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JDialog;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.AbstractAction;

// issues:
// -- needs i18n!
// -- needs help!  (hook up the "help" button, that is.  the code's fine.)
// -- heidelberg/hohenheim need wj -- give intelligent error when they fail
// -- rangesonly/spreadsheet need elements -- give intelligent error when they fail
// -- this is a fairly basic class, probably should have javadoc
// -- "problem using this format" in textarea is bad ... replace the text area with a label, perhaps, or don't even list unusable choices (yeah!)
// -- filetype should have a suggestedExtension, so "ok" here gives you a default of "<old-filename>.<sug-ext>"
// -- make it resizable, if i can figure out how to get the jtextarea to resize, as well.
// -- add a "copy" button, so you can get a funny format to the clipboard without saving first

// -- don't even list rangesonly,spreadsheet,packedtucson if there's no elements?

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

 -- it doesn't behave the same on windows and mac.  i don't know why it doesn't scroll to
 the top on windows.  windows users don't seem to notice minor bugs, anyway.

 -- on help: yes, help should be included with the application, and not require an internet
 connection or a web browser to view.  if it's in a jar, it won't be loaded until the user asks
 for it, and i can always distribute a version that matches the program.  i can also keep
 it up-to-date with web-start.  of course, it can still be HTML; it'll just ignore my stylesheets.
*/

public class ExportDialog extends JDialog {

    private static final String EXPORTERS[] = new String[] {
        "corina.files.Tucson",
        "corina.files.TwoColumn",
        "corina.files.Corina",
        "corina.files.Heidelberg",
        "corina.files.Hohenheim",
        "corina.files.TSAPMatrix",
        "corina.files.RangesOnly",
        "corina.files.Spreadsheet",
        "corina.files.PackedTucson", // new!
    };
    private JComboBox popup;
    private JLabel size;
    private JTextArea preview;
    private JScrollPane scroll;
    private JButton ok;
    private Sample sample;
    public ExportDialog(Sample s, Frame parent) {
        super(parent, "Export", true);
        this.sample = s;

        // filetype popup
        int n = EXPORTERS.length;
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
        popup.setMaximumRowCount(10); // i have 9 now, and this shouldn't scroll
        popup.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updatePreview();
            }
        });

        // size preview
        size = new JLabel("");

        // text preview
        preview = new JTextArea(12, 80) {
            public boolean isManagingFocus() {
                return false;
            }
        };
        Font oldFont = preview.getFont();
        preview.setFont(new Font("monospaced", oldFont.getStyle(), oldFont.getSize()));
        preview.setEditable(false);

        // in a panel
        JPanel tuples = new JPanel(new DialogLayout());
        tuples.add(popup, "Filetype:");
        tuples.add(size, "Size:");
        scroll = new JScrollPane(preview);
        tuples.add(scroll, "Preview:");
                        
        // buttons
        JButton help = new JButton("Help");
//        help.setEnabled(false);
        help.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("javax.jnlp.ServiceManager");
                    Bug.bug(new IllegalArgumentException("jnlp!"));
                } catch (Exception ee) {
                    Bug.bug(new IllegalArgumentException("no jnlp!"));
                }
/*
                BasicService bs = (javax.jnlp.BasicService) javax.jnlp.ServiceManagor.lookup("javax.jnlp.BasicService");
                URL url = getClass().getClassLoader().getResource("Filetypes.html");
                bs.showDocument(url);
 */
            }
        });
        JButton cancel = new JButton("Cancel");
        ok = new JButton("OK");
        OKCancel.addKeyboardDefaults(this, ok);

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
                    String fn = FileDialog.showSingle("Export");

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
                    JOptionPane.showMessageDialog(me, "Error exporting sample: " + ioe,
                                                  "Error Exporting", JOptionPane.WARNING_MESSAGE);
                } catch (Exception ex) {
                    // problem creating filetype, or npe, or whatever -- bug.
                    Bug.bug(ex);
                }
            }
        });
        
        // in a panel
        JPanel buttons = new JPanel(new ButtonLayout());
        buttons.add(help);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(cancel);
        buttons.add(ok);
        buttons.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        setContentPane(main);
        main.add(tuples, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

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
            Filetype f = (Filetype) Class.forName(EXPORTERS[i]).newInstance();
            StringBuffer buf = writer.getBuffer();
            buf.delete(0, buf.length()); // clear it
            f.save(writer, sample);

            // update views
            preview.setText(buf.toString());
            size.setText(new FileLength(buf.length()).toString());
            ok.setEnabled(true);

            // move cursor to start -- this scrolls to the top, as well
            preview.setCaretPosition(0);
        } catch (IOException ioe) {
            // for now, use this to mean "inappropriate data/filetype" -- WTFE will be better, though.
            size.setText("");
            preview.setText("Problem using this format:\n" + ioe.getMessage());
            ok.setEnabled(false);

            // problem saving it -- bug
//            Bug.bug(ioe);
        } catch (Exception e) {
            // problem creating the filetype -- bug
            Bug.bug(e);
        }
    }
}
