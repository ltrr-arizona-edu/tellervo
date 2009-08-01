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
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasDerivedSeries;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.graph.GraphActions;
import edu.cornell.dendro.corina.graph.GraphController;
import edu.cornell.dendro.corina.graph.GraphInfo;
import edu.cornell.dendro.corina.graph.GraphToolbar;
import edu.cornell.dendro.corina.graph.Graphable;
import edu.cornell.dendro.corina.graph.GrapherPanel;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.Help;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.gui.menus.OpenRecent;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;
import edu.cornell.dendro.corina.tridasv2.SeriesLinkUtil;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.NoEmptySelection;
import edu.cornell.dendro.corina.util.OKCancel;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;
import edu.cornell.dendro.corina_indexing.Exponential;

/**
 * Indexing dialog. Lets the user choose an indexing algorithm to use.
 * 
 * Shows them a graph of the index in the same dialog.
 * 
 * @author Ken Harris 
 * @author Lucas Madar
 * 
 * @version $Id$
 */
public class IndexDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private GrapherPanel graphPanel;

	private List<Graph> graphSamples;
	private JTextComponent indexName;
	private JTextComponent versionName;
	
	private IndexSet iset;
	private IndexTableModel model;

	private JButton okButton;

	// source data
	private Sample sample;

	// table
	private JTable table;

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
		
		// Create split pane...
		JSplitPane splitPane = new JSplitPane();
		setContentPane(splitPane);
		//splitPane.setDividerLocation(450);
		
		// calculate any extra width in this dialog
		// 20 = separator + padding
		int extraWidth = 20 + getInsets().left + getInsets().right;
		
		// add the content
		JPanel tableAndButtons = createTableAndButtons();
		content.add(tableAndButtons);
		content.add(Box.createHorizontalStrut(5));
		content.add(new JSeparator(JSeparator.VERTICAL));
		content.add(Box.createHorizontalStrut(5));
			
		JComponent graph = createGraph(content.getPreferredSize(), extraWidth);
		splitPane.setRightComponent(content);
		splitPane.setLeftComponent(graph);
		
		content.setMaximumSize(new Dimension(400,400));
		splitPane.setResizeWeight(1.0);
		
		
		// ok/cancel
		OKCancel.addKeyboardDefaults(okButton);

		// all done
		pack();
		Center.center(this, owner);
		setVisible(true);
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
		
		if(loader instanceof CorinaWsiTridasElement) 
			return corinaWsiApplyIndex(index);
				
		// well, fine then. Just apply the index to the existing sample.
		legacyApplyIndex(index);
		return true;
	}

	private boolean corinaWsiApplyIndex(Index index) {
		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// it's a new series! (to force update, set this to the id of the series to update!)
		// call gets a new identifier with the domain of our parent
		series.setIdentifier(NewTridasIdentifier.getInstance(sample.getSeries().getIdentifier()));
		series.setTitle(indexName.getText());
		series.setVersion(versionName.getText());

		// it's an index
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.INDEX.toString());
		series.setType(voc);

		// the index type
		series.setStandardizingMethod(index.getIndexFunction().getDatabaseRepresentation());

		// the sample we're basing this index on
		SeriesLinkUtil.addToSeries(series, sample.getSeries().getIdentifier());
		
		// create a new sample to hold this all
		Sample tmp = new Sample(series);

		try {
			CorinaWsiTridasElement cwe = new CorinaWsiTridasElement(series.getIdentifier());
			
			// here's where we do the "meat"
			if(cwe.save(tmp, this)) {
				// put it in our menu
				OpenRecent.sampleOpened(tmp.getLoader());
								
				// open a new editor 
				new Editor(tmp);
				return true;
			}
		} catch (IOException ioe) {
			Alert.error("Could not create index", "Error: " + ioe.toString());
		}
		
		return false;		
	}
	
	private JComponent createGraph(final Dimension otherPanelDim, final int extraWidth) {
		// create a new graphinfo structure, so we can tailor it to our needs.
		GraphInfo gInfo = new GraphInfo();
		
		// force no drawing of graph names
		gInfo.setShowGraphNames(false);
		
		// Make sure the graphs can't be dragged
		graphSamples.get(0).setDraggable(false);
		graphSamples.get(1).setDraggable(false);
		
		// create a graph panel; put it in a scroll pane
		graphPanel = new GrapherPanel(graphSamples, null, gInfo) {
			private static final long serialVersionUID = 1L;

			@Override
			public Dimension getPreferredScrollableViewportSize() {
				// -10s are for insets set below in the emptyBorder
				int screenWidth = super.getPreferredScrollableViewportSize().width - (otherPanelDim.width + extraWidth);
				int graphWidth = getGraphPixelWidth();
				return new Dimension((graphWidth < screenWidth) ? graphWidth : screenWidth, otherPanelDim.height);
			}
		};

		JScrollPane scroller = new JScrollPane(graphPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// make the default viewport background the same color as the graph
		scroller.getViewport().setBackground(gInfo.getBackgroundColor());
		
		GraphActions actions = new GraphActions(graphPanel, null, new GraphController(graphPanel, scroller));
		GraphToolbar toolbar = new GraphToolbar(actions);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scroller, BorderLayout.CENTER);
		panel.add(toolbar, BorderLayout.NORTH);
		
		return panel;
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
		okButton.addActionListener(new ActionListener() {
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
	
	private JButton makeCancelButton() {
		JButton cancel = Builder.makeButton("cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		return cancel;
	}
	
	// label, aligned
	private JComponent makeLabel() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,1));
		JLabel l = new JLabel(I18n.getText("choose_index"));
		//l.setAlignmentX(LEFT_ALIGNMENT);
		p.add(l);
		return p;
	}
	
	// flow containing name and version
	private JComponent makeNameBox() {
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(2,2,5,5));
			
		// Create name components
		JLabel l = new JLabel("Series code:  ");
		
		JLabel prefix = new JLabel("C-XXX-X-X-X-");

		// make the prefix more relevant if we have a labcode
		if(sample.hasMeta(Metadata.LABCODE)) {
			prefix.setText(LabCodeFormatter.getSeriesPrefixFormatter().format(
					sample.getMeta(Metadata.LABCODE, LabCode.class)) + "- ");
		}
		
		JTextField name = new JTextField(sample.getSeries().getTitle());
		name.setColumns(10);
		indexName = name;
		prefix.setLabelFor(indexName);
		
		JPanel titlePanel = new JPanel();
		titlePanel.setLayout(new BorderLayout());
		titlePanel.add(prefix, BorderLayout.WEST);
		titlePanel.add(indexName, BorderLayout.CENTER);
			
		// Create version components
		JLabel l2 = new JLabel("Version:");
		JTextField version = new JTextField("1");
		version.setColumns(20);		
		versionName = version;
		l.setLabelFor(versionName);
				
		
		// Add items to panel
		p.add(l);
		p.add(titlePanel);
		p.add(l2);
		p.add(versionName);

		JPanel outer = new JPanel(new BorderLayout());
		outer.add(p, BorderLayout.NORTH);
		
		return outer;
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
		table.getColumnModel().getColumn(1).setCellRenderer(new DecimalRenderer(IndexTableModel.CHI2_FORMAT.replace('#', '0')));
		table.getColumnModel().getColumn(2).setCellRenderer(new DecimalRenderer(IndexTableModel.RHO_FORMAT.replace('#', '0')));
		
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
		graphSamples = new ArrayList<Graph>(3);
		graphSamples.add(new Graph(iset.indexes.get(0).getTarget()));
		graphSamples.add(new Graph(iset.indexes.get(0)));
		graphSamples.add(getDiffGraph(iset.indexes.get(0)));
		
    	table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// ignore if we haven't yet made a selection
				if(e.getValueIsAdjusting())
					return;
				
				int row = table.getSelectedRow();
				if(row == -1) // ignore if this happens??
					return;

				Index i = iset.indexes.get(row);
				float scale = graphSamples.get(0).scale;
				
				// change the graph samples...
				graphSamples.set(0, new Graph(i.getTarget()));
				graphSamples.set(1, new Graph(i));
				graphSamples.set(2, getDiffGraph(i));
				
				// propagate old scale
				graphSamples.get(0).scale = scale;
				graphSamples.get(1).scale = scale;
				graphSamples.get(2).scale = scale * 0.1f;
				
				// Make sure the graphs can't be dragged
				graphSamples.get(0).setDraggable(false);
				graphSamples.get(1).setDraggable(false);				
				graphSamples.get(2).setDraggable(false);				
				
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
	
	/**
	 * Get a difference graph
	 * @param indexidx
	 * @return a graph of the differences in the ratio
	 */
	private Graph getDiffGraph(Index index) {		
		Graphable series = index.getTarget();

		return new Graph(new DiffGraph(series.getData(), index.getData(), series.getStart()));
	}
	
	private static final class DiffGraph implements Graphable {
		private final Year startYear;
		private final List<? extends Number> output;
		
		public DiffGraph(List<? extends Number> seriesData, List<? extends Number> indexData, Year startYear) {
			this.startYear = startYear;
		
			int len = seriesData.size();
			
			List<Integer> output = new ArrayList<Integer>(len);
			for(int i = 0; i < len; i++) {
				double ind = indexData.get(i).doubleValue();
				double raw = seriesData.get(i).doubleValue();
				double ratio = raw / ind;
				int val = (int) Math.round(ratio * 1000.0d);			
				
				output.add(val);
			}
			this.output = output;
		}
		
		public List<? extends Number> getData() {
			return output;
		}

		public float getScale() {
			return 1.0f;
		}

		public Year getStart() {
			return startYear;
		}
	}
}
