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
package org.tellervo.desktop.graph;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;


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
						I18n.getText("graph.number_of_samples")));
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
