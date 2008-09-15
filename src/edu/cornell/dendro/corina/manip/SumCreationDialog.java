package edu.cornell.dendro.corina.manip;

import java.awt.Frame;

import edu.cornell.dendro.corina.gui.DBBrowser;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.SampleSummary;

public class SumCreationDialog {
	public SumCreationDialog(Frame parent, ElementList el) {
		DBBrowser sum = new DBBrowser(parent, true, true);
		
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

				if(ss != null)
					sum.selectSiteByCode(ss.getSiteCode());
			} catch (Exception ex) {
				// ignore...
			}
		}
		
		sum.setVisible(true);
	}
}
