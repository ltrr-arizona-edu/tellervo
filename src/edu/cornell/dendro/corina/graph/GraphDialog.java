package edu.cornell.dendro.corina.graph;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.I18n;

@SuppressWarnings("serial")
public class GraphDialog extends JDialog {
	/**
	 * Graph a list of elements
	 * @param parent
	 * @param elements
	 */
	public GraphDialog(Frame parent, ElementList elements) {
		super(parent, true);		
		initialize(elements, null);
	}
	public GraphDialog(Dialog parent, ElementList elements) {
		super(parent, true);		
		initialize(elements, null);
	}

	/**
	 * Convenience constructor
	 * @param parent
	 * @param s
	 */
	public GraphDialog(Frame parent, Sample s) {
		this(parent, ElementList.singletonList(new CachedElement(s)));
	}
	public GraphDialog(Dialog parent, Sample s) {
		this(parent, ElementList.singletonList(new CachedElement(s)));
	}

	/**
	 * Sum constructor
	 * @param parent
	 * @param s the sum element
	 * @param elements the constituents of the sum
	 */
	public GraphDialog(Frame parent, Sample s, ElementList elements) {
		super(parent, true);
		initialize(elements, s);
	}
	public GraphDialog(Dialog parent, Sample s, ElementList elements) {
		super(parent, true);
		initialize(elements, s);
	}

	private void initialize(ElementList elements, Sample primary) {
		// samples
		boolean problem = false;
		ArrayList<Graph> samples = new ArrayList<Graph>(elements.size() + 2);
		
		if(primary != null) {
			samples.add(new Graph(primary));

			// summed -- add count, too
			if (primary.isSummed())
				samples.add(new Graph(primary.getCount(), primary.getRange().getStart(), 
						I18n.getText("number_of_samples")));
		}

		for (int i = 0; i < elements.size(); i++) {
			Element e = elements.get(i);

			if (!elements.isActive(e)) // skip inactive
				continue;

			try {
				Sample ns = e.load();
				samples.add(new Graph(ns));
			} catch (IOException ioe) {
				problem = true; // ick.
			}
		}

		// problem?
		if (problem) {
			Alert.error("Error loading sample(s)",
					"Some samples were not able to be loaded.");
		}

		// no samples => don't bother doing anything
		if (samples.isEmpty()) {
			dispose();
			return;
		}
		
		// ok, so things are good now. 
		
		// create a new graphinfo structure, so we can tailor it to our needs.
		GraphInfo gInfo = new GraphInfo();
		
		// force no drawing of graph names
		gInfo.setShowGraphNames(false);
		
		// create a graph panel; put it in a scroll pane
		final JDialog glue = this;
		GrapherPanel graphPanel = new GrapherPanel(samples, null, gInfo) {
			@Override
			public Dimension getPreferredScrollableViewportSize() {
				int extraWidth = 2 + glue.getInsets().right + glue.getInsets().right;
				int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width - extraWidth;
				int graphWidth = getGraphPixelWidth();
				return new Dimension((graphWidth < screenWidth) ? graphWidth : screenWidth, 480);
			}
		};;

		JScrollPane scroller = new JScrollPane(graphPanel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		setContentPane(scroller);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pack();
	}
}
