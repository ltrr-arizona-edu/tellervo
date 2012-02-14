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
package org.tellervo.desktop.editor;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.tellervo.desktop.graph.BargraphFrame;
import org.tellervo.desktop.graph.GraphWindow;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.CorinaAction;
import org.tellervo.desktop.ui.I18n;


// graph
// - graph
// - graph elements
// - bargraph elements

@SuppressWarnings("serial")
public class EditorGraphMenu extends JMenu implements SampleListener {

	private JMenuItem plot, plotElements, plotAll, bargraphAll;

	private Sample sample;

	EditorGraphMenu(Sample s) {
		super(I18n.getText("menus.graph")); // i18n bypasses mnemonic here!
		
		this.sample = s;

		sample.addSampleListener(this);

		// plot
		plot = new JMenuItem(new CorinaAction("menus.graph.activeSeries", "graph.png", 22) {
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sample);
			}
		});
		add(plot);

		// plot elements
		plotElements = new JMenuItem(new CorinaAction("menus.graph.components") {
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sample.getElements());
			}

			@Override
			public boolean isEnabled() {
				return sample.getElements() != null && sample.getElements().size() > 0;
			}
		});
		add(plotElements);
		
		// plot all
		plotAll = new JMenuItem(new CorinaAction("menus.graph.everything") {
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sample, sample.getElements());
			}

			@Override
			public boolean isEnabled() {
				return sample.getElements() != null && sample.getElements().size() > 0;
			}
		});
		add(plotAll);

		// bargraph all
		bargraphAll = new JMenuItem(new CorinaAction("menus.graph.bargraph_components") {
			public void actionPerformed(ActionEvent e) {
				// FIXME: pass my title here so the bargraph
				// has my name as its title.
				new BargraphFrame(sample.getElements());
			}

			@Override
			public boolean isEnabled() {
				return sample.getElements() != null && sample.getElements().size() > 0;
			}
		});
		add(bargraphAll);
	}

	//
	// listener
	//
	public void sampleRedated(SampleEvent e) {
	}

	public void sampleDataChanged(SampleEvent e) {
	}

	public void sampleMetadataChanged(SampleEvent e) {
		// re-en/disable menuitems based on whether the editor's sample
		// is summed.
		boolean hasElements = (sample.getElements() != null)
				&& (sample.getElements().size() > 0);
		// FIXME: didn't i want to have a hasElements() method in sample?

		plotElements.setEnabled(hasElements);
		plotAll.setEnabled(hasElements);
		bargraphAll.setEnabled(hasElements);
	}

	public void sampleElementsChanged(SampleEvent e) {
	}

	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}
