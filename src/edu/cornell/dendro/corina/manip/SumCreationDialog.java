package edu.cornell.dendro.corina.manip;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.jdesktop.layout.GroupLayout;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.DBBrowser;
import edu.cornell.dendro.corina.gui.menus.OpenRecent;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CorinaWebElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleLoader;
import edu.cornell.dendro.corina.sample.SampleSummary;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.webdbi.ResourceIdentifier;

public class SumCreationDialog {
	private DBBrowser sum;
	private String sumName = "New sum";

	public SumCreationDialog(Frame parent, ElementList el) {
		
		// create the db browser, but make it create the sum first before closing
		sum = new DBBrowser(parent, true, true) {
			@Override
			protected boolean finish() {
				return (applySum() && super.finish());
			}
		};
		
		// must have two for a sum!
		sum.setMinimumSelectedElements(2);
		
		// add the elements so they show in the dialog
		for(Element e : el)
			sum.addElement(e);
		
		// select the site in the first element
		Element e = el.get(0);
		if(e != null) {
			try	{
				BaseSample bs = e.loadBasic();
				
				SampleSummary ss = (SampleSummary) bs.getMeta("::summary");

				if(ss != null) {
					sum.selectSiteByCode(ss.getSiteCode());
					sumName = "New " + ss.getSiteCode() + " sum";
				}
			} catch (Exception ex) {
				// ignore...
			}
		}
		
		sumName = JOptionPane.showInputDialog(parent,
				"Please choose a name for your sum:",
				sumName);
		
		addButtons();
		
		sum.setTitle("Sum creation: " + sumName);
		sum.setVisible(true);		
	}
	
	private boolean applySum() {
		// ok, for now this only works for creating *new* sums!

		// create an empty resource identifier and saver
		ResourceIdentifier rid = new ResourceIdentifier("measurement");
		CorinaWebElement cwe = new CorinaWebElement(rid);
		
		// create a new, empty sample
		Sample tmp = new Sample();

		// set it up
		tmp.setMeta("name", sumName);
		tmp.setMeta("title", sumName); // not necessary, but consistent?
		tmp.setMeta("::saveoperation", SampleType.SUM);
		tmp.setMeta("::dbparent", rid);

		// add the elements...
		tmp.setElements(sum.getSelectedElements());

		try {
			// here's where we do the "meat"
			if(cwe.save(tmp)) {
				// put it in our menu
				OpenRecent.sampleOpened(tmp.getLoader());
								
				// open a new editor 
				new Editor(tmp);
				return true;
			}
		} catch (IOException ioe) {
			Alert.error("Could not create sum", "Error: " + ioe.toString());
		}

		return false;
	}
	
	private void addButtons() {
		JPanel panel = sum.getExtraButtonPanel();
		
		JButton preview = new JButton("Preview");
		preview.setSize(panel.getWidth(), preview.getHeight());
		preview.setAlignmentX(JButton.RIGHT_ALIGNMENT);

		BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
		panel.setLayout(layout);
		
		panel.add(Box.createVerticalStrut(52));
		panel.add(preview);
		panel.add(Box.createVerticalStrut(12));
		
		preview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new GraphWindow(sum.getSelectedElements());
			}			
		});
	}
}
