package corina.editor;

import corina.Sample;
import corina.SampleEvent;
import corina.SampleListener;
import corina.ui.Builder;
import corina.ui.I18n;
import corina.graph.GraphWindow;
import corina.graph.BargraphFrame;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

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
	plot = Builder.makeMenuItem("graph");
	plot.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    new GraphWindow(sample);
		}
	    });
	add(plot);

	// plot all
	plotAll = Builder.makeMenuItem("graph_elements");
	plotAll.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    new GraphWindow(sample.elements);
		}
	    });
	add(plotAll);

	// bargraph all
	bargraphAll = Builder.makeMenuItem("bargraph_elements");
	bargraphAll.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    // FIXME: pass my title here so the bargraph
		    // has my name as its title.
		    new BargraphFrame(sample.elements);
		}
	    });
	add(bargraphAll);
    }

    //
    // listener
    //
    public void sampleRedated(SampleEvent e) { }
    public void sampleDataChanged(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) {
	// re-en/disable menuitems based on whether the editor's sample
	// is summed.
	boolean hasElements = (sample.elements != null) &&
	                      (sample.elements.size() > 0);
	// FIXME: didn't i want to have a hasElements() method in sample?

	plotAll.setEnabled(hasElements);
	bargraphAll.setEnabled(hasElements);
    }
    public void sampleElementsChanged(SampleEvent e) { }
}
