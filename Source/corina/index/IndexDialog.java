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

package corina.index;

import corina.Sample;
import corina.graph.GraphFrame;
import corina.gui.FileDialog;
import corina.gui.ButtonLayout;
import corina.gui.UserCancelledException;
import corina.util.OKCancel;
import corina.util.TextClipboard;
import corina.util.Platform;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

// todo:
// -- help button? => xcorina.org/manual/indexing
// -- graph multiple?  preview graph in same window?
// -- right-align the chi^2 numbers

// -- use radio buttons?  this solves the following other issues:
// -- (set size more intelligently?)
// -- ("no index selected" should never occur -- don't allow deselection, or disable it if that happens)

/**
   Indexing dialog.  Lets the user choose an indexing algorithm to use.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class IndexDialog extends JDialog {
    // data
    private Sample sample;
    private IndexSet iset;

    // table
    private JTable table;
    private IndexTableModel model;

    // proxying
    private JCheckBox useProxy;
    private JComboBox proxyPopup;
    private int oldSelection = 0;

    // i18n
    private ResourceBundle msg = ResourceBundle.getBundle("TextBundle");

    // table model -- (could be static but for msg.getString())
    private class IndexTableModel extends AbstractTableModel {
        private DecimalFormat fmtChi2 = new DecimalFormat("###,##0.0"); // for chi2
        private DecimalFormat fmtR = new DecimalFormat("0.000"); // for r
        private IndexSet iset;
        public IndexTableModel(IndexSet iset) {
            this.iset = iset;
        }
        public String getColumnName(int col) {
            switch (col) {
                case 0: return msg.getString("index");
                case 1: return "\u03C7\u00B2"; // "Chi^2"
                case 2: return "r"; // it's always "r", right?
                default: return null;
            }
        }
        public int getRowCount() {
            return iset.indexes.size();
        }
        public int getColumnCount() {
            return 3;
        }
        public Object getValueAt(int row, int col) {
            Index i = (Index) iset.indexes.get(row);
            switch (col) {
                case 0: return i.getName();
                case 1: return fmtChi2.format(i.getChi2());
                case 2: return fmtR.format(i.getR());
                default: return null;
            }
        }
        public void setIndexSet(IndexSet iset) {
            this.iset = iset;
            fireTableDataChanged();
        }
    }

    // just like a java.io.File except toString() returns its name, not its path.
    // used in a popup, where i want to know the path but users should only see the name.
    private static class UserFriendlyFile extends File {
        public UserFriendlyFile(String f)	{ super(f); }
        public String toString()		{ return getName(); }
    }
    
    // label, centered
   private JComponent makeLabel() {
        JPanel b = new JPanel(new BorderLayout());
        b.add(new JLabel(msg.getString("choose_index"), JLabel.CENTER), BorderLayout.CENTER);
        return b;
    }

    private JComponent makeTable() {
        iset = new IndexSet(sample);
        iset.run();

        model = new IndexTableModel(iset);
        table = new JTable(model);
        table.setShowGrid(false);
        table.setPreferredScrollableViewportSize(new Dimension(420, 200)); // 320, 150));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scroller = new JScrollPane(table);
        // scroller.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
        
        // select last entry
        table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);

        // don't allow reordering
        table.getTableHeader().setReorderingAllowed(false);

        return scroller;
    }

    private JButton makePreviewButton() {
        JButton preview = new JButton(msg.getString("preview"));
        preview.setMnemonic(msg.getString("preview_key").charAt(0));
        preview.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                int row = table.getSelectedRow();

                // error-checking!
                if (row == -1) {
                    JOptionPane.showMessageDialog(null,
                                                  "Select a possible index to graph first.",
                                                  "No index selected!",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // graph the index (and target -- see GraphFrame(Index))
                new GraphFrame((Index) iset.indexes.get(row));
            }
        });
        return preview;
    }

    private JButton makeCopyButton() {
        JButton b = new JButton("Copy");
        b.setMnemonic('C');
        b.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // error-checking!
                if (table.getSelectedRow() == -1) {
                    JOptionPane.showMessageDialog(null,
                                                  "Select a possible index to copy first.",
                                                  "No index selected!",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // copy the index
                copyIndex();
            }
        });
        return b;
    }

    // copy the currently-selected index; ASSUMES something is selected.
    private void copyIndex() {
        // get the index
        int row = table.getSelectedRow();
        Index ind = (Index) iset.indexes.get(row);

        // convert it to a 1-column string
        // -- add title here?
        // -- copy year, too?
        // -- copy data, too?
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<ind.data.size(); i++) { // Index.data is a LIST?  what was i thinking?
            buf.append(ind.data.get(i).toString());
            buf.append('\n');
        }

        // copy it
        TextClipboard.copy(buf.toString());
    }
    
    private JButton makeCancelButton() {
        JButton cancel = new JButton(msg.getString("cancel"));
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        return cancel;
    }

    /** Create a new indexing dialog for the given sample.
	@param s the Sample to be indexed */
    public IndexDialog(Sample s, JFrame owner) {
        super(owner);
        // setModal(true); -- no, graph becomes unusable, then.

        // data
        sample = s;

        // watch for already-indexed files
        if (sample.isIndexed()) {
            JOptionPane.showMessageDialog(null,
                                          msg.getString("already_indexed_text"),
                                          msg.getString("already_indexed_title"),
                                          JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // make sure there's data here
        // BETTER: make this "==0", and have individual indexes throw if they can't handle size==2, etc.
        if (sample.data.size() < 3) {
            JOptionPane.showMessageDialog(null,
                                          msg.getString("no_data_text"),
                                          msg.getString("no_data_title"),
                                          JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        // title
        String title = (String) sample.meta.get("title");
        setTitle(msg.getString("index") + " " + title);

        // border
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));
        setContentPane(p);
        
        // top panel: text
        p.add(makeLabel());

        p.add(Box.createVerticalStrut(12));
        
        // table
        p.add(makeTable());

        // proxy indexing -- REFACTOR: JESUS H CHRIST, EXTRACT METHOD
        p.add(Box.createVerticalStrut(12));
        useProxy = new JCheckBox("Use Proxy Data:");
        {
            JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p2.add(useProxy);
            p.add(p2);
            useProxy.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (useProxy.isSelected()) {
                        // checked
                        try {
                            proxyPopup.setEnabled(true);
                            if (proxyPopup.getSelectedIndex() == 0) { // special case: selected the sample itself
                                iset = new IndexSet(sample);
                            } else {
                                File f = (File) proxyPopup.getSelectedItem();
                                Sample proxy = new Sample(f.getPath());
                                iset = new IndexSet(sample, proxy);
                            }
                            iset.run(); // can't be bad here, afaik -- are you sure?
                            model.setIndexSet(iset);
                        } catch (IOException ioe) {
                            // can't happen ... can it?  better be really sure before you ignore this.
                            System.out.println("oops: " + ioe);
                        }
                    } else {
                        // unchecked
                        proxyPopup.setEnabled(false);
                        iset = new IndexSet(sample);
                        iset.run();
                        model.setIndexSet(iset);
                    }
                }
            });
        }
        p.add(Box.createVerticalStrut(8));
        {
            JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p2.add(Box.createHorizontalStrut(24));
            Vector sums = getSums();
            int numSums = sums.size();
            UserFriendlyFile me = new UserFriendlyFile((String) s.meta.get("filename"));
            sums.add(0, me);
            sums.add("Other...");
            proxyPopup = new JComboBox(sums); // this is the last time |sums| is used
            proxyPopup.setEnabled(false);
            proxyPopup.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    // now deal with this selection...

                    if (proxyPopup.getSelectedIndex() == proxyPopup.getItemCount()-1) {
                        // "other..." selected
                        Sample proxy=null;
                        try {
                            String fn = FileDialog.showSingle("Proxy");
                            // load it, etc.
                            proxy = new Sample(fn);
                            iset = new IndexSet(sample, proxy);
                            iset.run();
                            model.setIndexSet(iset);

                            // add to popup AFTER running -- if it couldn't run, we don't want to add it
                            int x = proxyPopup.getItemCount() - 1;
                            proxyPopup.insertItemAt(new UserFriendlyFile(fn), x);
                            proxyPopup.setSelectedIndex(x);
                        } catch (UserCancelledException uce) {
                            proxyPopup.setSelectedIndex(oldSelection); // reselect previous value.
                        } catch (IOException ioe) {
                            // IMPROVE THIS!
                            System.out.println("oops, can't load or something...");
                        } catch (RuntimeException re) { // ICK!
                            JOptionPane.showMessageDialog(null,
                                                          "That proxy dataset (" + proxy.range + ") doesn't cover\n" +
                                                          "the entire range of the sample (" + sample.range + ") you're indexing.",
                                                          "Proxy Dataset Too Short",
                                                          JOptionPane.ERROR_MESSAGE);
                            proxyPopup.setSelectedIndex(oldSelection);
                            return;
                        }
                    } else {
                        // a file already in the list was selected
                        Sample proxy = null;
                        try {
                            if (proxyPopup.getSelectedIndex() == 0) { // special case: selected the sample itself
                                iset = new IndexSet(sample);
                            } else {
                                File f = (File) proxyPopup.getSelectedItem();
                                proxy = new Sample(f.getPath());
                                iset = new IndexSet(sample, proxy);
                            }
                            iset.run();
                            model.setIndexSet(iset);
                        } catch (IOException ioe) {
                            System.out.println("oops2, can't load or something...");
                        } catch (RuntimeException re) {
                            JOptionPane.showMessageDialog(null,
                                                          "That proxy dataset (" + proxy.range + ") doesn't cover\n" +
                                                          "the entire range of the sample (" + sample.range + ") you're indexing.",
                                                          "Proxy Dataset Too Short",
                                                          JOptionPane.ERROR_MESSAGE);
                            proxyPopup.setSelectedIndex(oldSelection);
                            return;
                        }
                    }

                    // no matter what, save old selection
                    oldSelection = proxyPopup.getSelectedIndex();
                }
            });
            p2.add(proxyPopup);
            p.add(p2);
        }

        p.add(Box.createVerticalStrut(14));

        // bottom panel (buttons) -- REFACTOR: EXTRACT METHOD
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new ButtonLayout());
        p.add(buttonPanel);

        // graph button
        buttonPanel.add(makePreviewButton());

        // copy button
        buttonPanel.add(makeCopyButton());

        // also copy on cmd-C -- BROKEN
/*
        addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                // nothing selected, do nothing
                if (table.getSelectedRow() == -1)
                    return;

                System.out.println("e=" + e);
                
                // on command-C (control-C on the pc), copy the index.
                boolean command = ((!Platform.isMac && e.isControlDown()) ||
                                   (Platform.isMac && e.isMetaDown()));
                if (e.getKeyChar()=='c' && command)
                    copyIndex();
            }
        });
*/
        
        // (spacer)
        buttonPanel.add(Box.createHorizontalGlue());

        // cancel button
        buttonPanel.add(makeCancelButton());

        // ok button -- REFACTOR: EXTRACT METHOD
        JButton okButton = new JButton(msg.getString("ok"));
        okButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                int row = table.getSelectedRow();

                if (row == -1) { // error-checking!
                    JOptionPane.showMessageDialog(null,
                                                  "Select a possible index to apply first.",
                                                  "No index selected!",
                                                  JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // apply it
                Index index = (Index) iset.indexes.get(row);
                index.apply();

                // undo (index implements undoable)
                sample.postEdit(index);

                // also: clear filename, set modified
                sample.setModified();
                sample.meta.remove("filename");
                
                // tell editor, and close
                sample.fireSampleMetadataChanged();
                dispose();
            }
        });
        buttonPanel.add(okButton);

        // ok/cancel
        OKCancel.addKeyboardDefaults(this, okButton);

        // all done
        pack();
        setResizable(false);
        show();
    }

    // make a vector of sums (*.sum *.SUM) in the same folder as this sample,
    // since that's what they'll be using 99% of the time
    private Vector getSums() {
        // get files in this directory
        String filename = (String) sample.meta.get("filename");
        File files[] = new File(filename).getParentFile().listFiles();

        // return those that match "*.sum"
        // -- this is something like: (remove-if-not (lambda (x) (ends-with x ".sum")))
        Vector result = new Vector();
        for (int i=0; i<files.length; i++)
            if (files[i].getName().toUpperCase().endsWith(".SUM"))
                result.add(new UserFriendlyFile(files[i].getPath()));
        return result;
    }
}
