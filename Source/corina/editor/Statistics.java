package corina.editor;

import corina.Sample;
import corina.SampleListener;
import corina.SampleEvent;
import corina.manip.MeanSensitivity;
import corina.prefs.Prefs;
import corina.ui.I18n;

import java.io.IOException;

import java.text.DecimalFormat;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JPopupMenu;

/*
  TODO:
  -- L10n?
  -- fix sample listener bug.
  -- (add keyboard shortcut for "next stat"?  something like accel-|, perhaps)
  -- rename: maybe "StatsLabel"?
*/

public class Statistics extends JLabel implements SampleListener {
    private Sample sample;

    public Statistics(Sample s) {
	this.sample = s;

	sample.addSampleListener(this);
	// BUG: what if you open an editor, make a graph, close the editor?  any changes
	// cause computeAllStats() to run, though the output can never be seen.
	// SOLUTION: if an event is fired, and this component is no longer (visible,
	// packed, ???), remove it as a sample listener.

	setToolTipText(I18n.getText("modeline_tooltip"));

	final JLabel label = this; // ...

	addMouseListener(new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
		    JPopupMenu popup = new JPopupMenu();

		    // for each stat, create a menuitem with text "name = value",
		    // which sets the text of |stats| to itself when selected.
		    for (int i=0; i<stat_names.length; i++) {
			JMenuItem s = new JRadioButtonMenuItem(stat_names[i] + " = " + stat_values[i],
							       i==state);
			final int glue = i;

			s.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
				    // store state locally
				    state = glue;

				    // store state in pref
				    Prefs.setPref("corina.modeline.statistic", stat_keys[state]);

				    // update label
				    label.setText(((JMenuItem) e.getSource()).getText());
				}
			    });
			popup.add(s);
		    }

		    popup.show(label, e.getX(), e.getY());
		}
	    });

	// compute stats
	computeAllStats();

	// which one?  read from prefs
	String pref = Prefs.getPref("corina.modeline.statistic");
	if (pref != null) {
	    for (int i=0; i<stat_names.length; i++) {
		if (pref.equals(stat_keys[i])) {
		    state = i;
		    break;
		}
	    }
	}

	// set initial text
	setText(stat_names[state] + " = " + stat_values[state]); // (oops, violates OAOO again)
    }

    private int state = 0; // which stat_*[] is being seen

    private final String NA = I18n.getText("n/a");

    // on my old (500MHz) powerbook, doing a compute-all for baillie's
    // 7200-year northern irish oak chronology takes up to 39ms the
    // first time, and up to 7ms on subsequent runs.  that's plenty
    // fast.  so i won't make any effort to complicate things by
    // letting certain events recompute only some stats.
    private void computeAllStats() {
	int i=0;

	// mean sensitivity
        float m = MeanSensitivity.meanSensitivity(sample.data);
        DecimalFormat f = new DecimalFormat("0.000");
	stat_values[i++] = (Float.isNaN(m) ? NA : f.format(m));

	// total radius
	f = new DecimalFormat("0.00 " + I18n.getText("mm"));
	float radius = sample.computeRadius() / 100f; // in mm -- UNUSED if sample isIndexed
	stat_values[i++] = (sample.isIndexed() ? NA : f.format(radius));

	// (fall-through on f, radius)

	// avg ring width
	int years = sample.data.size();
	stat_values[i++] = (sample.isIndexed() ? NA : f.format(radius / years));

	// number of years
	stat_values[i++] = String.valueOf(years);

	// number of rings
	stat_values[i++] = String.valueOf(sample.countRings());

	// number of elements
	stat_values[i++] = (sample.elements == null ? NA : String.valueOf(sample.elements.size()));
    }

    //
    // messy stuff
    //
    private String stat_keys[] = new String[] {
	"mean_sensitivity",
	"total_radius",
	"average_ring_width",
	"number_of_years",
	"number_of_rings",
	"number_of_elements",
    };
    private String stat_names[];
    {
	stat_names = new String[stat_keys.length];
	for (int i=0; i<stat_keys.length; i++)
	    stat_names[i] = I18n.getText(stat_keys[i]);
    }
    private String stat_values[];
    {
	stat_values = new String[stat_names.length];
	// don't need to fill, constructor calls computeAllStats()
    }

    //
    // sample listener -- if sample changes, update stats
    //

    public void sampleDataChanged(SampleEvent e) {
	// compute all?
	computeAllStats();

	// restore from local state here
	setText(stat_names[state] + " = " + stat_values[state]); // (" = " violates OAOO)
    }
    public void sampleRedated(SampleEvent e) {
	// doesn't change any stats
    }
    public void sampleMetadataChanged(SampleEvent e) {
	sampleDataChanged(e); // because if "indexed" is set, some
			      // stats become N/A -- inefficient,
			      // true, but simple
	// (Q: how much other work am i doing?  setting indexed->raw
	// seems simple enough, but if i do too many "simple" things
	// it could turn into a problem.)
    }
    public void sampleElementsChanged(SampleEvent e) {
	sampleDataChanged(e);
    }
}
