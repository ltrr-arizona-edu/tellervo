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

import java.text.DecimalFormat;
import java.util.ResourceBundle;

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
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

// todo:
// -- (make sure sample.data.size() is never 0 here, and remove that test -- ??)
// -- help button? => xcorina.org/manual/indexing
// -- graph multiple?  preview graph in same window?
// -- right-align the chi^2 numbers
// -- make modal, if passed a JFrame, too
// -- need better placement?

// -- use radio buttons?  this solves the following other issues:
// -- (set size more intelligently?)
// -- ("no index selected" should never occur -- don't allow deselection, or disable it if that happens)

/**
   Indexing dialog.  Lets the user choose an indexing algorithm to
   use.

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$
*/

public class IndexDialog extends JDialog {

    // buttons
    private JPanel buttonPanel;
    private JButton graphButton;
    private JButton saveButton;
    private JButton closeButton;

    // data
    private Sample sample;
    private IndexSet iset;

    // table
    private JTable table;

    // i18n
    private ResourceBundle msg = ResourceBundle.getBundle("IndexBundle");

    // table model -- uses iset member field
    // withtable
    private class IndexTableModel extends AbstractTableModel {
	private DecimalFormat _fmt; // formatter for chi^2
	public IndexTableModel() {
	    // make formatter
	    _fmt = new DecimalFormat("###,##0.00");
	}
	public String getColumnName(int col) {
	    return (col == 0 ? msg.getString("index") : "\u03C7\u00B2"); // "Chi^2"
	}
	public int getRowCount() {
	    return iset.indexes.size();
	}
	public int getColumnCount() {
	    return 2;
	}
        public Object getValueAt(int row, int col) {
            Index i = (Index) iset.indexes.get(row);
            return (col == 0 ? i.getName() : _fmt.format(i.getChi2()));
        }
    }

    // text
    private void initText() {
	JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
	getContentPane().add(textPanel, BorderLayout.NORTH);
	textPanel.add(new JLabel(msg.getString("select_index")));
    }

    private void initTable() {
	iset = new IndexSet(sample);
	iset.run(); // thread me?  naw, i'm fast: for 100yr sample, 10
		    // indexes, 900mhz: 30-40ms

	// withtable
	table = new JTable(new IndexTableModel());
	// table.setShowGrid(false);
	table.setPreferredScrollableViewportSize(new Dimension(420, 200)); // 320, 150));
	table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	JScrollPane scroller = new JScrollPane(table);
	getContentPane().add(scroller, BorderLayout.CENTER);
	// getContentPane().add(table, BorderLayout.CENTER);

	// select last entry
	table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);

	// don't allow reordering
	table.getTableHeader().setReorderingAllowed(false);

	// withgrid
	/*
	JPanel grid = new JPanel(new GridLayout(0, 2, 0, 0));
	ButtonGroup group = new ButtonGroup();
	DecimalFormat fmt = new DecimalFormat("###,##0.00");
	int n = iset.indexes.size();
	for (int row=0; row<n; row++) {
	    final Index i = (Index) iset.indexes.get(row);

	    JRadioButton radio = new JRadioButton(i.getName(), row==n-1);
	    radio.addActionListener(new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
			selectedIndex = i;
		    }
		});
	    JLabel chi2 = new JLabel("\u03C7\u00B2 = " + fmt.format(i.getChi2()));

	    group.add(radio);

	    grid.add(radio);
	    grid.add(chi2);
	}
	getContentPane().add(grid);
	*/

	// add spacers
	getContentPane().add(Box.createHorizontalStrut(8), BorderLayout.EAST);
	getContentPane().add(Box.createHorizontalStrut(8), BorderLayout.WEST);
    }

    // withgrid
    // private Index selectedIndex = null;

    private JButton makeGraphButton() {
	graphButton = new JButton(msg.getString("graph"));
	graphButton.setMnemonic(msg.getString("graph_key").charAt(0));
	graphButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // withtable
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

		    // withgrid
		    // new GraphFrame(selectedIndex);
		}
	    });
	return graphButton;
    }

    private JButton makeCloseButton() {
	closeButton = new JButton(msg.getString("cancel"));
	closeButton.setMnemonic(msg.getString("cancel_key").charAt(0));
	closeButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    dispose();
		}
	    });
	return closeButton;
    }

    /** Create a new indexing dialog for the given sample.
	@param s the Sample to be indexed */
    public IndexDialog(Sample s, JFrame owner) {
	// modal
	super(owner);
	// setModal(true); -- graph becomes unusable, then.

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
	if (sample.data.size() < 3) {
	    JOptionPane.showMessageDialog(null,
					  msg.getString("no_data_text"),
					  msg.getString("no_data_title"),
					  JOptionPane.ERROR_MESSAGE);
	    dispose();
	    return;
	}

	// title
	setTitle(msg.getString("index"));

	// top panel: text
	initText();

	// table
	initTable();

	// bottom panel (buttons)
	buttonPanel = new JPanel();
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	getContentPane().add(buttonPanel, BorderLayout.SOUTH);

	// graph button
	buttonPanel.add(makeGraphButton());

	// (spacer)
	buttonPanel.add(Box.createHorizontalGlue());

	// close button
	buttonPanel.add(makeCloseButton());

	// (spacer)
	buttonPanel.add(Box.createHorizontalStrut(8));

	// apply button
	saveButton = new JButton(msg.getString("ok"));
	saveButton.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent ae) {
		    // withtable
		    int row = table.getSelectedRow();

		    if (row == -1) { // error-checking!
			JOptionPane.showMessageDialog(null,
						      "Select a possible index to apply first.",
						      "No index selected!",
						      JOptionPane.ERROR_MESSAGE);
			return;
		    }

		    // apply it
		    // withtable
                    Index index = (Index) iset.indexes.get(row);
		    // withgrid
		    // final Index index = selectedIndex;
		    index.apply();

		    // undo (index implements undoable)
		    sample.postEdit(index);

		    // also: clear filename, set modified
		    index.target.setModified();
		    index.target.meta.remove("filename");

		    // tell editor
		    sample.fireSampleDataChanged(); // REDUNDANT?
		    sample.fireSampleMetadataChanged();

		    // close me
		    dispose();
		}
	    });
	buttonPanel.add(saveButton);

	// apply is default
	getRootPane().setDefaultButton(saveButton);

	// esc => cancel
	addKeyListener(new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			dispose();
		}
	    });

	// all done
	pack();
	setResizable(false);
	show();
    }

}
