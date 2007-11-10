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

package edu.cornell.dendro.corina.index;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.NoEmptySelection;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.TextClipboard;
import edu.cornell.dendro.corina.util.UserFriendlyFile;

import edu.cornell.dendro.corina_indexing.Exponential;

/**
   Indexing dialog.  Lets the user choose an indexing algorithm to use.

<pre>
NOTE: the proper order of buttons is:
 (help) --- (alternative) (cancel) ((ok))
</pre>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
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
    private JComboBox proxyPopup;
    private int oldSelection = 0;

    // formatting for decimals
    private static final String CHI2_FORMAT = "#,##0.0";
    private static final String RHO_FORMAT = "0.000";

    // table model -- (could be static but for i18n)
    private static class IndexTableModel extends AbstractTableModel {
        private DecimalFormat fmtChi2 = new DecimalFormat(CHI2_FORMAT);
        private DecimalFormat fmtR = new DecimalFormat(RHO_FORMAT);
        private IndexSet iset;
        public IndexTableModel(IndexSet iset) {
            this.iset = iset;
        }
        public String getColumnName(int col) {
            switch (col) {
                case 0:
		    return I18n.getText("algorithm");
                case 1:
                    return "\u03C7\u00B2"; // should be "Chi\overline^2" = \u03C7\u0304\u00B2
                    // unfortunately, Mac OS X (at least -- so probably others) apparently doesn't
                    // combine combining diacritics after greek letters, so it looks just plain bad.
                    // (but a\u0304\u00B2 looks fine, so it's not completely ignorant of combining diacritics)
                    // (actually, a\u0304 looks fine in window titles, but not tables headers, so ... yeah.  ick.)
                    // Windows 2000 report: swing labels can do the greek but not the diacritic (chi, followed
                    // by a box), window titles can do the diacritic but not the greek (box, with an overline).

                case 2:
                    return "\u03C1"; // "rho"
                default:
                    throw new IllegalArgumentException(); // can't happen
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
	    case 1: return fmtChi2.format(i.getChi2()); // (is defined, as long as N!=0)
	    case 2:
		if (Double.isNaN(i.getR())) // can happen
		    return "-";
		else
		    return fmtR.format(i.getR());
	    default: throw new IllegalArgumentException(); // can't happen
            }
        }
        public void setIndexSet(IndexSet iset) {
            this.iset = iset;
            fireTableDataChanged();
        }
    }

    // label, centered
    private JComponent makeLabel() {
        JPanel b = new JPanel(new BorderLayout());
        b.add(new JLabel(I18n.getText("choose_index"), JLabel.LEFT));
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

        // use decimal renderers for chi^2 and rho
        table.getColumnModel().getColumn(1).setCellRenderer(new DecimalRenderer(CHI2_FORMAT.replace('#', '0')));
        table.getColumnModel().getColumn(2).setCellRenderer(new DecimalRenderer(RHO_FORMAT.replace('#', '0')));

        // don't allow reordering or resizing of the columns
        table.getTableHeader().setReorderingAllowed(false);
        // disabled for debugging:
        //        for (int i=0; i<3; i++)
        //            table.getColumn(table.getColumnName(i)).setResizable(false);

        // or deselecting the only selection.
	// (my users, sir, they didn't care for this dialog, at first.
	// one of them actually tried to deselect the only selected
	// entry.  but i ... CORRECTED them, sir.)
        NoEmptySelection.noEmptySelection(table);

        // select exponential, because that's probably the one that's going to get used
        // (and we should encourage that)
        for (int i=0; i<iset.indexes.size(); i++) {
            if (((Index)iset.indexes.get(i)).getIndexFunction() instanceof Exponential) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }

        return scroller;
    }

    private JButton makePreviewButton() {
        JButton preview = Builder.makeButton("preview");
        preview.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
		int row = table.getSelectedRow();

                // graph the index (and target -- see GraphWindow(Index))
                new GraphWindow((Index) iset.indexes.get(row));
            }
        });
        return preview;
    }

    private JButton makeCopyButton() {
        JButton b = Builder.makeButton("copy");
        b.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
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
        for (int i=0; i<ind.getData().size(); i++) { // Index.data is a LIST?  what was i thinking?
            buf.append(ind.getData().get(i).toString());
            buf.append('\n');
        }

        // copy it
        TextClipboard.copy(buf.toString());
    }
    
    private JButton makeCancelButton() {
        JButton cancel = Builder.makeButton("cancel");
        cancel.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                dispose();
            }
        });
        return cancel;
    }

    /**
       Create a new indexing dialog for the given sample.

       @param s the Sample to be indexed
    */
    public IndexDialog(Sample s, JFrame owner) {
        super(owner);
        // setModal(true); -- no, graph becomes unusable, then.

        // data
        sample = s;

        // watch for already-indexed files -- shouldn't this be a "never happens", too?
        if (sample.isIndexed()) {
	    Alert.error(I18n.getText("already_indexed_title"),
			I18n.getText("already_indexed_text"));
            dispose();
            return;
        }

        // make sure there's data here
        // BETTER: make this "==0", and have individual indexes throw if they can't handle size==2, etc.
        if (sample.data.size() < 3) {
            Alert.error(I18n.getText("no_data_title"),
			I18n.getText("no_data_text"));
            dispose();
            return;
        }

        // title
        String title = sample.meta.get("title").toString();
	if (title == null) // (DESIGN: can i do better than "untitled"?)
	    title = I18n.getText("Untitled");
        setTitle(MessageFormat.format(I18n.getText("index_of"),
				      new Object[] { title }));

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

        // proxy indexing -- REFACTOR: extract method
        p.add(Box.createVerticalStrut(12));
        final JCheckBox useProxy = new JCheckBox("Use Proxy Data:");
        {
            JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p2.add(useProxy);
            p.add(p2);
            useProxy.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                	int lastSelectedRow = table.getSelectedRow();
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
                    // clean up and make sure we reselect!
                   	table.setRowSelectionInterval(lastSelectedRow, lastSelectedRow);
                }
            });
        }
        p.add(Box.createVerticalStrut(8));
        {
            JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p2.add(Box.createHorizontalStrut(24));
            Vector sums = getSums();
            int numSums = sums.size();
            UserFriendlyFile me = new UserFriendlyFile((String) s.meta.get("filename")); // BUG:
	    // fails if this file hasn't been saved (but shouldn't i probably warn, then, anyway?)
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
			    Alert.error("Proxy Dataset Too Short",
					"That proxy dataset (" + proxy.range + ") doesn't cover\n" +
					"the entire range of the sample (" + sample.range + ") you're indexing.");
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
                            // FIXME: runtimeexception?  what was i smoking?
			    // REFACTOR: this looks awfully familiar
			    Alert.error("Proxy Dataset Too Short",
					"That proxy dataset (" + proxy.range + ") doesn't cover\n" +
					"the entire range of the sample (" + sample.range + ") you're indexing.");
                            // that wasn't very localizeable
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

        // bottom panel (buttons) ------------

        // help button
        JButton help = Builder.makeButton("help");
	Help.addToButton(help, "indexing");

        // graph button
	JButton preview = makePreviewButton();

        // copy button
	// buttonPanel.add(makeCopyButton()); // -- CAROL WANTS THIS, BUT IT'S NOW TEMPORARILY DISABLED
        // also: copy on cmd-C -- BROKEN
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
        
        // cancel button
	JButton cancel = makeCancelButton();

        // ok button -- REFACTOR: EXTRACT METHOD
        JButton okButton = Builder.makeButton("ok");
        okButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                int row = table.getSelectedRow();

                // apply it
                Index index = (Index) iset.indexes.get(row);
                index.apply();

                // undo (index implements undoable)
                sample.postEdit(index);

                // also: clear filename, set modified
                sample.setModified();
                sample.meta.remove("filename"); // BUG: this should be in Index.apply()
		// (otherwise undo doesn't put the filename back)

                // tell editor, and close
                sample.fireSampleDataChanged();
                sample.fireSampleMetadataChanged();
                dispose();
            }
        });

        p.add(Layout.buttonLayout(help, null, preview, cancel, okButton));

        // ok/cancel
        OKCancel.addKeyboardDefaults(okButton);

        // all done
        pack();
        setResizable(false);
        show();
    }

    // make a vector of sums (*.sum *.SUM) in the same folder as this sample,
    // since that's what they'll be using 99% of the time
    // (why a Vector?  because jcombobox takes a vector, but not a list.  yeah, suck.)
    private Vector getSums() {
        // get files in this directory
        String filename = (String) sample.meta.get("filename");

        // whoops!  what if no filename?  abort!
        if (filename == null)
            return new Vector(); // not pretty

	// list my siblings
        File files[] = new File(filename).getParentFile().listFiles();

	// whoops!  how can this happen?
	if (files == null)
	    return new Vector();

        // return those that match "*.sum"
        // -- this is something like:
	// (remove-if-not (lambda (x) (ends-with x ".SUM")) (list-files filename))
        Vector result = new Vector();
	for (int i=0; i<files.length; i++) {
	    // if doesn't end with .sum, skip;
	    // BETTER: load cache(summary), and get real sums?
	    if (!files[i].getName().toUpperCase().endsWith(".SUM"))
		continue;

	    // if it's me, skip -- BUG? should this check equals(File(filename))?
	    if (files[i].getPath().equals(filename))
		continue;

	    // ok, add it
	    result.add(new UserFriendlyFile(files[i].getPath()));
        }

        // sort the list, and return it
        Collections.sort(result); // TODO: case-insensitive sort!
        return result;
    }
}
