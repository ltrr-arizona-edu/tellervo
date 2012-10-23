/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
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

package org.tellervo.desktop.index;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
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
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.editor.Editor;
import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.graph.Graph;
import org.tellervo.desktop.graph.GraphActions;
import org.tellervo.desktop.graph.GraphController;
import org.tellervo.desktop.graph.GraphInfo;
import org.tellervo.desktop.graph.GraphToolbar;
import org.tellervo.desktop.graph.Graphable;
import org.tellervo.desktop.graph.GrapherPanel;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.Help;
import org.tellervo.desktop.gui.Layout;
import org.tellervo.desktop.gui.NameVersionJustificationPanel;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleLoader;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.NoEmptySelection;
import org.tellervo.desktop.util.OKCancel;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tellervo.indexing.Exponential;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasValue;


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
	private final static Logger log = LoggerFactory.getLogger(IndexDialog.class);


	private GrapherPanel graphPanel;

	private List<Graph> graphSamples;
	
	private IndexSet iset;
	private IndexTableModel model;

	private NameVersionJustificationPanel nameAndVersion;
	
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
		setModal(false); //-- no, graph becomes unusable, then.

		// data
		sample = s;

		// watch for already-indexed files -- shouldn't this be a
		// "never happens", too?
		if (sample.isIndexed()) {
			Alert.error(I18n.getText("error"), I18n.getText("error.alreadyIndexed"));
			dispose();
			return;
		}
				
		// Make sure they save before indexing
		if(!sample.isSynced())
		{
			Alert.error(I18n.getText("error"), "Please make sure the series is saved before indexing");
			dispose();
			return;
		}
		

		// make sure there's data here
		// BETTER: make this "==0", and have individual indexes throw if they
		// can't handle size==2, etc.
		if (sample.getRingWidthData().size() < 3) {
			Alert.error(I18n.getText("error"), I18n.getText("error.noData"));
			dispose();
			return;
		}

		// title
		String title = sample.getMeta("title").toString();
		if (title == null) // (DESIGN: can i do better than "untitled"?)
			title = I18n.getText("general.untitled");
		setTitle(MessageFormat.format(I18n.getText("index.indexing"), new Object[] { title }));

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
			new Bug(new Exception(I18n.getText("error.indexingWithoutLoader")));
			return false;
		}
		
		if(loader instanceof TellervoWsiTridasElement) 
			return tellervoWsiApplyIndex(index);
				
		// well, fine then. Just apply the index to the existing sample.
		legacyApplyIndex(index);
		return true;
	}

	private boolean tellervoWsiApplyIndex(Index index) {
		// we have to have a name set in order to create an index
		if(!nameAndVersion.testAndComplainRequired(EnumSet.of(NameVersionJustificationPanel.Fields.NAME)))
			return false;
		
		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// it's a new series! (to force update, set this to the id of the series to update!)
		// call gets a new identifier with the domain of our parent
		series.setIdentifier(NewTridasIdentifier.getInstance(sample.getSeries().getIdentifier()));
		series.setTitle(nameAndVersion.getSeriesName());
		
		if(nameAndVersion.hasVersion())
			series.setVersion(nameAndVersion.getVersion());

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
			TellervoWsiTridasElement cwe = new TellervoWsiTridasElement(series.getIdentifier());
			
			// here's where we do the "meat"
			if(cwe.save(tmp, this)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
								
				// open a new editor 
				new Editor(tmp);
				return true;
			}
		} catch (UserCancelledException uce) {
			// do nothing...
		} catch (IOException ioe) {
			Alert.error(I18n.getText("error"), I18n.getText("error.couldNotCreateIndex")+ ": " + ioe.toString());
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
		
		// Override units and show labels
		gInfo.setHundredUnitHeight(2);
		gInfo.setShowGraphNames(true);
				
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
		JButton help = Builder.makeButton("menus.help");
		Help.assignHelpPageToButton(help, "Indexing");
		

		// cancel button
		JButton cancel = makeCancelButton();

		// ok button -- REFACTOR: EXTRACT METHOD
		okButton = Builder.makeButton("general.ok");
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
		JButton cancel = Builder.makeButton("general.cancel");
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
		JLabel l = new JLabel(I18n.getText("index.choose")+":");
		//l.setAlignmentX(LEFT_ALIGNMENT);
		p.add(l);
		return p;
	}
	
	// flow containing name and version
	private JComponent makeNameBox() {
		nameAndVersion = new NameVersionJustificationPanel(sample, false, false);
		return nameAndVersion;
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
				graphSamples.get(2).scale = scale;
				//graphSamples.get(2).scale = scale * 0.1f;
				
				// Make sure the graphs can't be dragged
				graphSamples.get(0).setDraggable(false);
				graphSamples.get(1).setDraggable(false);				
				graphSamples.get(2).setDraggable(false);	
				
				// Label lines with friendly names
				graphSamples.get(0).setGraphName("Original data");
				graphSamples.get(1).setGraphName(i.getName());
				graphSamples.get(2).setGraphName("Normalized data");
						
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

		return new Graph(new DiffGraph(series.getRingWidthData(), index.getRingWidthData(), series.getStart()));
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
		
		public List<? extends Number> getRingWidthData() {
			return output;
		}

		public float getScale() {
			return 1.0f;
		}

		public Year getStart() {
			return startYear;
		}


		@Override
		public List<TridasValue> getTridasValues() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
