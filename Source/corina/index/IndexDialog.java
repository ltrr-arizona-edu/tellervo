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
import corina.util.UserFriendlyFile;
import corina.util.NoEmptySelection;
import corina.util.TextClipboard;
import corina.util.Platform;
import corina.gui.HelpBrowser;
import corina.gui.XButton;
import corina.gui.Bug;

import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;

import java.util.ResourceBundle;
import java.util.Collections;
import java.util.Vector;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

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

    // formatting for decimals
    private static final String CHI2_FORMAT = "#,##0.0";
    private static final String RHO_FORMAT = "0.000";

    // table model -- (could be static but for msg.getString())
    private class IndexTableModel extends AbstractTableModel {
        private DecimalFormat fmtChi2 = new DecimalFormat(CHI2_FORMAT);
        private DecimalFormat fmtR = new DecimalFormat(RHO_FORMAT);
        private IndexSet iset;
        public IndexTableModel(IndexSet iset) {
            this.iset = iset;
        }
        public String getColumnName(int col) {
            switch (col) {
                case 0:
                    return msg.getString("algorithm");
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
                case 1: return fmtChi2.format(i.getChi2());
                case 2: return fmtR.format(i.getR());
                default: throw new IllegalArgumentException(); // can't happen
            }
        }
        public void setIndexSet(IndexSet iset) {
            this.iset = iset;
            fireTableDataChanged();
        }
    }

    // given a String to render, draw everything before the |dot| to the left of
    // a certain x-position, and everything after it to the right.  the |dot| char
    // is centered at |position| of the way across the column.
    public static class DecimalRenderer extends DefaultTableCellRenderer {
        // default decimal point for this locale
        private char dot = new DecimalFormatSymbols().getDecimalSeparator();
        public DecimalRenderer(String sample) {
            this.sample = sample;
        }
        private int offset=0;
        private String sample=null;
        private boolean offsetSet=false;
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            // hack!
            if (!offsetSet) {
		// use the table's font -- only needs to get set once
		super.setFont(table.getFont());

                // compute position from a sample value -- needs a table, too
                int split = sample.indexOf(dot); // assumes: there is a dot somewhere

		// if not, let's assume it's at the end -- HOW?  i think this is harder...
		if (split == -1) {
		    int width = table.getGraphics().getFontMetrics().stringWidth(sample);
		    offset = width / 2;
		    offsetSet = true;
		} else {
		    // good, there was a dot
		    String leftValue = sample.substring(0, split);
		    String rightValue = sample.substring(split+1);
		    int leftWidth = table.getGraphics().getFontMetrics().stringWidth(leftValue);
		    int rightWidth = table.getGraphics().getFontMetrics().stringWidth(rightValue);
		    // if i don't know the column width here, i can't compute position
		    // but i wouldn't want to, since it'd be wrong if it ever was resized
		    // instead i should have a pixel offset from centerline
		    offset = (leftWidth - rightWidth) / 2;

		    offsetSet = true;
		}
            }

	    if (!(value instanceof String))
		value = String.valueOf(value); // for Integers...
            setText((String) value); // assumes: value is a string
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            // compute baseline
            int baseline = getSize().height - g2.getFontMetrics().getDescent(); // (is this right?)

            // get text to draw
            String value = getText();

            // fill background -- (is this needed/allowed/automatic?)
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getSize().width, getSize().height); // on isOpaque() only?

            // set foreground
            g2.setColor(getForeground());

            // get everything to the left of the dot
            int split = value.indexOf(dot); // assumes: there is a dot somewhere

	    // hack -- no dot!
	    if (split == -1) {
		int guide = getSize().width / 2 + offset;
		int width = g2.getFontMetrics().stringWidth(value);
		// (TODO: draw elipsis here if not enough room)
		g2.drawString(value, guide - width, baseline);
		return;
	    }

            String valueLeft = value.substring(0, split);

            // compute position the dot should be at
            int guide = offset + getSize().width / 2;

            // compute width of left half
            int dotWidth = g2.getFontMetrics().stringWidth(String.valueOf(dot));
            int leftWidth = g2.getFontMetrics().stringWidth(valueLeft);

            // if they go outside the cell, just draw "..."
            int width = g2.getFontMetrics().stringWidth(value);
            if ((guide - dotWidth/2 - leftWidth < 0) ||
		(guide - dotWidth/2 - leftWidth + width >= getSize().width)) {
                String elipsis = "\u2026"; // "..." -- 3 dots -- the
		// unicode is \u2026, but "..." is guaranteed -- pick one
                int elipsisWidth = g2.getFontMetrics().stringWidth(elipsis);
                g2.drawString(elipsis, getSize().width/2 - elipsisWidth/2, baseline);
                return;
            }

            // draw them all at once
            g2.drawString(value, guide - dotWidth/2 - leftWidth, baseline);
        }
    }

    // label, centered
   private JComponent makeLabel() {
        JPanel b = new JPanel(new BorderLayout());
        b.add(new JLabel(msg.getString("choose_index"), JLabel.LEFT));
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

        // or deselecting the only selection
        NoEmptySelection.noEmptySelection(table);

        // select exponential, because that's probably the one that's going to get used
        // (and we should encourage that)
        for (int i=0; i<iset.indexes.size(); i++) {
            if (iset.indexes.get(i) instanceof Exponential) {
                table.setRowSelectionInterval(i, i);
                break;
            }
        }

        return scroller;
    }

    private JButton makePreviewButton() {
        JButton preview = new XButton("preview");
        preview.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                int row = table.getSelectedRow();
                // my users, sir, they didn't care for this dialog, at first.  one of them actually
                // tried to deselect the only selected entry.  but i ... CORRECTED them, sir.
                if (row == -1) {
                    Bug.bug(new IllegalArgumentException("bug: no row selected in preview"));
                    return;
                }

                // graph the index (and target -- see GraphFrame(Index))
                new GraphFrame((Index) iset.indexes.get(row));
            }
        });
        return preview;
    }

    private JButton makeCopyButton() {
        JButton b = new XButton("copy");
        b.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                
                // this should never happen, but just in case...
                if (row == -1) {
                    Bug.bug(new IllegalArgumentException("no index selected in copy!"));
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
        JButton cancel = new XButton("cancel");
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

        // watch for already-indexed files -- shouldn't this be a "never happens", too?
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
        setTitle(MessageFormat.format(msg.getString("index_of"), new Object[] { title }));

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
                            // FIXME: runtimeexception?  what was i smoking?
                            JOptionPane.showMessageDialog(null,
                                                          "That proxy dataset (" + proxy.range + ") doesn't cover\n" +
                                                          "the entire range of the sample (" + sample.range + ") you're indexing.",
                                                          "Proxy Dataset Too Short",
                                                          JOptionPane.ERROR_MESSAGE);
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

        // bottom panel (buttons) -- REFACTOR: EXTRACT METHOD
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new ButtonLayout());
        p.add(buttonPanel);

        // TESTING
        JButton help = new XButton("help");
        help.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                new HelpBrowser("Manual/Indexing.html");
            }
        });
        buttonPanel.add(help);
        buttonPanel.add(Box.createHorizontalGlue());

        // graph button
        buttonPanel.add(makePreviewButton());

        // copy button
//        buttonPanel.add(makeCopyButton()); -- CAROL WANTS THIS, BUT IT'S NOW TEMPORARILY DISABLED

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
//        buttonPanel.add(Box.createHorizontalGlue());
        
        // cancel button
        buttonPanel.add(makeCancelButton());

        // ok button -- REFACTOR: EXTRACT METHOD
        JButton okButton = new XButton("ok");
        okButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                int row = table.getSelectedRow();

                // should never happen...
                if (row == -1) {
                    Bug.bug(new IllegalArgumentException("bug: no row selected in apply"));
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
    // (why a Vector?  because jcombobox takes a vector, but not a list.  yeah, suck.)
    private Vector getSums() {
        // get files in this directory
        String filename = (String) sample.meta.get("filename");
        // whoops!  what if no filename?  abort!
        if (filename == null)
            return new Vector(); // not pretty
        File files[] = new File(filename).getParentFile().listFiles();

        // return those that match "*.sum"
        // -- this is something like: (remove-if-not (lambda (x) (ends-with x ".SUM")) (list-files filename))
        Vector result = new Vector();
        if (files != null) { // null if not-a-dir
            for (int i=0; i<files.length; i++)
                // if it ends with .sum, and it's not me (i'm already in the list), add it
                if (files[i].getName().toUpperCase().endsWith(".SUM") && !files[i].getPath().equals(filename))
                    result.add(new UserFriendlyFile(files[i].getPath()));
        }

        // sort the list, and return it
        Collections.sort(result);
        return result;
    }
}
