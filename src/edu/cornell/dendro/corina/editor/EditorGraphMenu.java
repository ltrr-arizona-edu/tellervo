package edu.cornell.dendro.corina.editor;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import edu.cornell.dendro.corina.graph.BargraphFrame;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.CorinaAction;
import edu.cornell.dendro.corina.ui.I18n;

// graph
// - graph
// - graph elements
// - bargraph elements

public class EditorGraphMenu extends JMenu implements SampleListener {

	private JMenuItem plot, plotElements, plotAll, bargraphAll;

	private Sample sample;

	EditorGraphMenu(Sample s) {
		super(I18n.getText("graph_menu")); // i18n bypasses mnemonic here!
		
		this.sample = s;

		sample.addSampleListener(this);

		// plot
		plot = new JMenuItem(new CorinaAction("graph", "graph.png", 22) {
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sample);
			}
		});
		add(plot);

		// plot elements
		plotElements = new JMenuItem(new CorinaAction("graph_components") {
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
		plotAll = new JMenuItem(new CorinaAction("graph_everything") {
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
		bargraphAll = new JMenuItem(new CorinaAction("bargraph_components") {
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
}
