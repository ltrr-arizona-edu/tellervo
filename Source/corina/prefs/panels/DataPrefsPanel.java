package corina.prefs.panels;

import corina.prefs.Prefs;

import java.io.File;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: gpl header
// TODO: javadoc

// TODO: center this stuff

// TODO: i18n

// TODO: always use absolute filenames for this!
// -- on the jlabel, call new file().getPath()

// in the future, this panel can be used to set up RDBMS (and other) sources.
// or maybe it'll go away, because each source will use its own get-info dialog.

public class DataPrefsPanel extends JComponent implements ActionListener {
  private JLabel folder;
  private Component parent;
  private JFileChooser chooser = new JFileChooser();
  
  public DataPrefsPanel() {
    setLayout(new BorderLayout());

    chooser.setDialogTitle("Choose new data folder");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    // BUG: what if it's null?
    chooser.setCurrentDirectory(new File(Prefs.getPref("corina.dir.data")));
    // is this needed?
    // chooser.setApproveButtonText("OK");
    // chooser.setApproveButtonMnemonic('O');

    // BUG: what if corina.dir.data isn't set yet?
    // TODO: extract const "corina.dir.data"

    String oldFolder = Prefs.getPref("corina.dir.data");
    folder = new JLabel(new File(oldFolder).getAbsolutePath());

    JLabel l = new JLabel("Data is stored in folder:");
    JButton change = new JButton("Change...");
    l.setLabelFor(change);
    
    change.addActionListener(this);

    Container c = new Container();
    c.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));

    c.add(l);
    c.add(folder);
    c.add(change);
    
    add(c, BorderLayout.NORTH);
  }

  private Runnable showdialog = new Runnable() {
    public void run() {
      int rv = chooser.showDialog(parent, "OK");

      // --- JFileChooser then blocks here ---

      if (rv != JFileChooser.APPROVE_OPTION) return;
      
      //    it's done; set the pref, and update the label
      String newFolder = chooser.getSelectedFile().getPath();
      Prefs.setPref("corina.dir.data", newFolder);
      folder.setText(newFolder);
    }
  };

  public void actionPerformed(ActionEvent e) {
    parent = getTopLevelAncestor();
    // show it
    SwingUtilities.invokeLater(showdialog);
  }
}
