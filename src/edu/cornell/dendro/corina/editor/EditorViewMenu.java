package edu.cornell.dendro.corina.editor;

import edu.cornell.dendro.corina.gui.ElementsPanel;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleEvent;
import edu.cornell.dendro.corina.sample.SampleListener;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.ButtonGroup;

public class EditorViewMenu extends JMenu implements SampleListener {

    /*
      this should be:

      View
      ----
      Data        ^1
      Metadata    ^2
      Weiserjahre ^3
      Elements    ^4
      ---
      Elements by Filenames
      Elements by Summary Fields
      Elements by All Fields
      ---
      * Show Count as Histogram
      Show Count as Numbers
      ---
      Font >
      Size >
      (Style >)
      Text Color>
      Background Color>
      Show/Hide Gridlines

      (wj,elements dimmed if not present)
      (text/*, gridlines, and which meta view are global, and get saved!)

      (global means i'll need more references like open-recent.
      abstract that out somehow?  GlobalMenu?  GlobalMenuItem?)
    */

    private JMenuItem v1, v2, v3;

    private Sample sample;

    private ElementsPanel elemPanel;

    public EditorViewMenu(Sample sample, ElementsPanel el) {
	super(I18n.getText("view")); // TODO: mnemonic!

	this.sample = sample;
	this.elemPanel = el;

	// dim/undim to follow elements
	sample.addSampleListener(this);

	// create menuitems
	v1 = Builder.makeRadioButtonMenuItem("view_filenames");
	v2 = Builder.makeRadioButtonMenuItem("view_standard");
	v3 = Builder.makeRadioButtonMenuItem("view_all");

	// make action
	AbstractAction a = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    Object source = e.getSource();
		    if (source == v1)
			elemPanel.setView(ElementsPanel.VIEW_FILENAMES);
		    else if (source == v2)
			elemPanel.setView(ElementsPanel.VIEW_STANDARD);
		    else // (source == v3)
			elemPanel.setView(ElementsPanel.VIEW_ALL);
		}
	    };

	// add action to menuitems
	v1.addActionListener(a);
	v2.addActionListener(a);
	v3.addActionListener(a);

	// add menuitems to menu
	add(v1);
	add(v2);
	add(v3);

	// enabled only if relevant
	sampleMetadataChanged(null);

	// group v1, v2, v3 (mutex)
	ButtonGroup bg = new ButtonGroup();
	bg.add(v1);
	bg.add(v2);
	bg.add(v3);

	// first one is default
	v1.setSelected(true);

	// TODO: preserve last used value
	// BETTER: let user pick all fields to show
    }

    //
    // listener
    //
    public void sampleRedated(SampleEvent e) { }
    public void sampleDataChanged(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) {
	v1.setEnabled(sample.getElements() != null);
	v2.setEnabled(sample.getElements() != null);
	v3.setEnabled(sample.getElements() != null);
    }
    public void sampleElementsChanged(SampleEvent e) { 
	v1.setEnabled(sample.getElements() != null);
	v2.setEnabled(sample.getElements() != null);
	v3.setEnabled(sample.getElements() != null);
    }
    
    public void setElementsPanel(ElementsPanel ep) { elemPanel = ep; }

}
