package corina.prefs.panels;

import corina.prefs.Prefs;
import corina.gui.layouts.DialogLayout;

import java.io.File;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.AbstractAction;
import java.awt.Frame;
import java.awt.event.ActionEvent;

// TODO: gpl header
// TODO: javadoc

// TODO: center this stuff

// TODO: i18n

// TODO: always use absolute filenames for this!
// -- on the jlabel, call new file().getPath()

// in the future, this panel can be used to set up RDBMS (and other) sources.
// or maybe it'll go away, because each source will use its own get-info dialog.

public class DataPrefsPanel extends JPanel {

    public DataPrefsPanel() {
        setLayout(new DialogLayout());

        // BUG: what if corina.dir.data isn't set yet?

        // TODO: extract const "corina.dir.data"

        String oldFolder = Prefs.getPref("corina.dir.data");
        final JLabel folder = new JLabel(new File(oldFolder).getPath());
        JButton change = new JButton("Change...");

        final Frame parent = (Frame) getTopLevelAncestor();

        change.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser f = new JFileChooser();

                f.setDialogTitle("Choose new data folder");

                // BUG: what if it's null?
                f.setCurrentDirectory(new File(Prefs.getPref("corina.dir.data")));

                // is this needed?
                // f.setApproveButtonText("OK");
                // f.setApproveButtonMnemonic('O');
                
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                // show it
                int rv = f.showDialog(parent, "OK");

                // --- JFileChooser then blocks here ---

                if (rv != JFileChooser.APPROVE_OPTION)
                    return;

                // it's done; set the pref, and update the label
                String newFolder = f.getSelectedFile().getPath();
                Prefs.setPref("corina.dir.data", newFolder);
                folder.setText(newFolder);
            }
        });

        add(folder, "Data is stored in folder:");
        add(change, "");
    }
}
