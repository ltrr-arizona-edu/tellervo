package corina.editor;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.graph.BargraphFrame;
import corina.graph.GraphWindow;
import corina.ui.Builder;
import corina.ui.CorinaAction;
import corina.ui.I18n;

// graph
// - graph
// - graph elements
// - bargraph elements

public class EditorGraphMenu extends JMenu implements SampleListener {

	private JMenuItem plot, plotAll, bargraphAll;

	private Sample sample;

	EditorGraphMenu(Sample s) {
		super(I18n.getText("graph")); // i18n bypasses mnemonic here!

		this.sample = s;

		sample.addSampleListener(this);

		// plot
		plot = new JMenuItem(new CorinaAction("graph") {
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sample);
			}
		});
		add(plot);

		// plot all
		plotAll = new JMenuItem(new CorinaAction("graph_elements") {
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sample.elements);
			}

			public boolean isEnabled() {
				return sample.elements != null && sample.elements.size() > 0;
			}
		});
		add(plotAll);

		// bargraph all
		bargraphAll = new JMenuItem(new CorinaAction("bargraph_elements") {
			public void actionPerformed(ActionEvent e) {
				// FIXME: pass my title here so the bargraph
				// has my name as its title.
				new BargraphFrame(sample.elements);
			}

			public boolean isEnabled() {
				return sample.elements != null && sample.elements.size() > 0;
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
		boolean hasElements = (sample.elements != null)
				&& (sample.elements.size() > 0);
		// FIXME: didn't i want to have a hasElements() method in sample?

		plotAll.setEnabled(hasElements);
		bargraphAll.setEnabled(hasElements);
	}

	public void sampleElementsChanged(SampleEvent e) {
	}
}
