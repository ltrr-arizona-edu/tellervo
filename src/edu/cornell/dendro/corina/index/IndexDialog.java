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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.graph.GraphInfo;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.graph.Graphable;
import edu.cornell.dendro.corina.graph.GrapherPanel;
import edu.cornell.dendro.corina.graph.PlotAgents;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.menus.OpenRecent;
import edu.cornell.dendro.corina.sample.FileElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.NoEmptySelection;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.util.TextClipboard;
import edu.cornell.dendro.corina.util.UserFriendlyFile;

import edu.cornell.dendro.corina_indexing.Exponential;

/**
 * Indexing dialog. Lets the user choose an indexing algorithm to use.
 * 
 * <pre>
 * NOTE: the proper order of buttons is:
 *  (help) --- (alternative) (cancel) ((ok))
 * </pre>
 * 
 * @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i
 *         style="color: gray">dot</i> edu&gt;
 * @version $Id$
 */
public class IndexDialog extends JDialog {
	// data
	private Sample sample;
	private IndexSet iset;

	// table
	private JTable table;
	private IndexTableModel model;

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

		@Override
		public String getColumnName(int col) {
			switch (col) {
			case 0:
				return I18n.getText("algorithm");
			case 1:
				return "\u03C7\u00B2"; // should be "Chi\overline^2" =
										// \u03C7\u0304\u00B2
				// unfortunately, Mac OS X (at least -- so probably others)
				// apparently doesn't
				// combine combining diacritics after greek letters, so it looks
				// just plain bad.
				// (but a\u0304\u00B2 looks fine, so it's not completely
				// ignorant of combining diacritics)
				// (actually, a\u0304 looks fine in window titles, but not
				// tables headers, so ... yeah. ick.)
				// Windows 2000 report: swing labels can do the greek but not
				// the diacritic (chi, followed
				// by a box), window titles can do the diacritic but not the
				// greek (box, with an overline).

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
			Index i = iset.indexes.get(row);
			switch (col) {
			case 0:
				return i.getName();
			case 1:
				return fmtChi2.format(i.getChi2()); // (is defined, as long as
													// N!=0)
			case 2:
				if (Double.isNaN(i.getR())) // can happen
					return "-";
				else
					return fmtR.format(i.getR());
			default:
				throw new IllegalArgumentException(); // can't happen
			}
		}

		public void setIndexSet(IndexSet iset) {
			this.iset = iset;
			fireTableDataChanged();
		}
	}

	// label, aligned
	private JComponent makeLabel() {
		JLabel l = new JLabel(I18n.getText("choose_index"));
		l.setAlignmentX(RIGHT_ALIGNMENT);
		return l;
	}

	// flow containing name: and box
	private JComponent makeNameBox() {
		JPanel p = new JPanel();
		
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
		
		JLabel l = new JLabel("Series code:  " + sample.getMetaString("title"));
		
		indexName = new JTextField();
		indexName.setText("Index");
		
		l.setLabelFor(indexName);
		
		p.add(l);
		p.add(Box.createHorizontalStrut(5));
		p.add(indexName);
		
		return p;
	}

	private JComponent makeTable() {
		iset = new IndexSet(sample);

		model = new IndexTableModel(iset);
		table = new JTable(model);
		table.setShowGrid(false);
		
		int theight = table.getRowHeight() * iset.indexes.size();
		table.setPreferredScrollableViewportSize(new Dimension(220, theight));
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroller = new JScrollPane(table);
		// scroller.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

		// use decimal renderers for chi^2 and rho
		table.getColumnModel().getColumn(1).setCellRenderer(new DecimalRenderer(CHI2_FORMAT.replace('#', '0')));
		table.getColumnModel().getColumn(2).setCellRenderer(new DecimalRenderer(RHO_FORMAT.replace('#', '0')));
		
		// calculate the maximum string width for the first column
		int maxWidth = -1;
		FontMetrics fm = table.getFontMetrics(table.getFont());
		for(Index i : iset.indexes) {
			int iwidth = fm.stringWidth(i.getName());
			if(iwidth > maxWidth)
				maxWidth = iwidth;
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(maxWidth);

		// don't allow reordering or resizing of the columns
		table.getTableHeader().setReorderingAllowed(false);
		// disabled for debugging:
		// for (int i=0; i<3; i++)
		// table.getColumn(table.getColumnName(i)).setResizable(false);

		// or deselecting the only selection.
		// (my users, sir, they didn't care for this dialog, at first.
		// one of them actually tried to deselect the only selected
		// entry. but i ... CORRECTED them, sir.)
		NoEmptySelection.noEmptySelection(table);

		// set up graphSamples, and ensure that it's set to something sane
		// otherwise, graph won't initialize
		graphSamples = new ArrayList<Graph>(2);
		graphSamples.add(new Graph(iset.indexes.get(0).getTarget()));
		graphSamples.add(new Graph(iset.indexes.get(0)));
		
    	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// ignore if we haven't yet made a selection
				if(e.getValueIsAdjusting())
					return;
				
				int row = table.getSelectedRow();
				if(row == -1) // ignore if this happens??
					return;

				Index i = iset.indexes.get(row);
				
				// change the graph samples...
				graphSamples.set(0, new Graph(i.getTarget()));
				graphSamples.set(1, new Graph(i));
				
				if(graphPanel != null)
					graphPanel.update();
			}
    	});

		
		// select exponential, because that's probably the one that's going to
		// get used
		// (and we should encourage that)
		for (int i = 0; i < iset.indexes.size(); i++) {
			if ((iset.indexes.get(i)).getIndexFunction() instanceof Exponential) {
				table.setRowSelectionInterval(i, i);
				break;
			}
		}
		
		return scroller;
	}

	/* 
	 * This isn't used at all, but I'm keeping it in case it proves useful in the future.
	 * 
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
		Index ind = iset.indexes.get(row);

		// convert it to a 1-column string
		// -- add title here?
		// -- copy year, too?
		// -- copy data, too?
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < ind.getData().size(); i++) { // Index.data is a
															// LIST? what was i
															// thinking?
			buf.append(ind.getData().get(i).toString());
			buf.append('\n');
		}

		// copy it
		TextClipboard.copy(buf.toString());
	}
	*/

	private JButton makeCancelButton() {
		JButton cancel = Builder.makeButton("cancel");
		cancel.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		return cancel;
	}
	
	private JComponent createGraph(final Dimension otherPanelDim, final int extraWidth) {
		// initialize our plotting agents
		PlotAgents agents = new PlotAgents();
		
		// create a new graphinfo structure, so we can tailor it to our needs.
		GraphInfo gInfo = new GraphInfo();
		
		// force no drawing of graph names
		gInfo.overrideDrawGraphNames(false);
		
		// create a graph panel; put it in a scroll pane
		graphPanel = new GrapherPanel(graphSamples, agents, null, gInfo) {
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				// -10s are for insets set below in the emptyBorder
				int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width - (otherPanelDim.width + extraWidth);
				int graphWidth = getGraphPixelWidth();
				return new Dimension((graphWidth < screenWidth) ? graphWidth : screenWidth, otherPanelDim.height);
			}
		};

		JScrollPane scroller = new JScrollPane(graphPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		return scroller;
	}
	
	private JPanel createTableAndButtons() {
		// border
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		p.setBorder(BorderFactory.createEmptyBorder(14, 20, 20, 20));

		// top panel: text
		p.add(makeLabel());

		p.add(Box.createVerticalStrut(12));

		// table
		p.add(makeTable());
		
		// name
		p.add(Box.createVerticalStrut(8));
		p.add(makeNameBox());

		p.add(Box.createVerticalStrut(14));

		// bottom panel (buttons) ------------

		// help button
		JButton help = Builder.makeButton("help");
		Help.addToButton(help, "indexing");

		// cancel button
		JButton cancel = makeCancelButton();

		// ok button -- REFACTOR: EXTRACT METHOD
		okButton = Builder.makeButton("ok");
		okButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				int row = table.getSelectedRow();

				// apply it
				Index index = iset.indexes.get(row);
				
				if(applyIndex(index))
					dispose();
			}
		});

		p.add(Layout.buttonLayout(help, null, okButton, cancel));
		p.add(Box.createVerticalGlue());
		return p;
	}

	/**
	 * Create a new indexing dialog for the given sample.
	 * 
	 * @param s
	 *            the Sample to be indexed
	 */
	public IndexDialog(Sample s, JFrame owner) {
		super(owner, true);
		// setModal(true); -- no, graph becomes unusable, then.

		// data
		sample = s;

		// watch for already-indexed files -- shouldn't this be a
		// "never happens", too?
		if (sample.isIndexed()) {
			Alert.error(I18n.getText("already_indexed_title"), I18n.getText("already_indexed_text"));
			dispose();
			return;
		}

		// make sure there's data here
		// BETTER: make this "==0", and have individual indexes throw if they
		// can't handle size==2, etc.
		if (sample.getData().size() < 3) {
			Alert.error(I18n.getText("no_data_title"), I18n.getText("no_data_text"));
			dispose();
			return;
		}

		// title
		String title = sample.getMeta("title").toString();
		if (title == null) // (DESIGN: can i do better than "untitled"?)
			title = I18n.getText("Untitled");
		setTitle(MessageFormat.format(I18n.getText("indexing"), new Object[] { title }));

		// create content pane...
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		setContentPane(content);
		
		// calculate any extra width in this dialog
		// 20 = separator + padding
		int extraWidth = 20 + getInsets().left + getInsets().right;
		
		// add the content
		JPanel tableAndButtons = createTableAndButtons();
		content.add(tableAndButtons);
		content.add(Box.createHorizontalStrut(5));
		content.add(new JSeparator(JSeparator.VERTICAL));
		content.add(Box.createHorizontalStrut(5));
		content.add(createGraph(tableAndButtons.getPreferredSize(), extraWidth));
		
		// ok/cancel
		OKCancel.addKeyboardDefaults(okButton);

		// all done
		pack();
		Center.center(this, owner);
		setVisible(true);
	}
	
	/**
	 * Apply an index directly to a sample, not using a server...
	 * @param index
	 */
	private void legacyApplyIndex(Index index) {
		index.apply();

		// undo (index implements undoable)
		sample.postEdit(index);

		// also: clear filename, set modified
		sample.setModified();
		sample.removeMeta("filename"); // BUG: this should be in
										// Index.apply()
		// (otherwise undo doesn't put the filename back)

		// tell editor, and close
		sample.fireSampleDataChanged();
		sample.fireSampleMetadataChanged();		
	}
	
	/**
	 * 
	 * @return true on success, false on failure
	 */
	private boolean applyIndex(Index index) {
		SampleLoader loader = sample.getLoader();
		if(loader == null) {
			new Bug(new Exception("Attempting to apply an index to a sample without a loader. Shouldn't be possible!"));
			return false;
		}
		
		if(loader instanceof CorinaWebElement) {
			// create a new, empty sample
			Sample tmp = new Sample();

			// set it up
			tmp.setMeta("name", indexName.getText());
			tmp.setMeta("title", indexName.getText()); // not necessary, but consistent?
			tmp.setMeta("::saveoperation", SampleType.INDEX);
			tmp.setMeta("::indexclass", index);
			
			// the new sample's parent is our current sample
			tmp.setMeta("::dbparent", sample.getMeta("::dbrid"));
			
			try {
				// here's where we do the "meat"
				if(loader.save(tmp)) {
					// put it in our menu
					OpenRecent.sampleOpened(tmp.getLoader());
					
					/*
					// copy it over...
					Sample.copy(tmp, sample);
					sample.fireSampleMetadataChanged();
					sample.clearModified();
					*/
					
					// instead, open a new editor 
					new Editor(tmp);
					return true;
				}
			} catch (IOException ioe) {
				Alert.error("Could not create index", "Error: " + ioe.toString());
			}

			return false;
		}
		
		// well, fine then. Just apply the index to the existing sample.
		legacyApplyIndex(index);
		return true;
	}
	
	private JButton okButton;
	private JTextField indexName;
	private GrapherPanel graphPanel;
	private List<Graph> graphSamples;
}
