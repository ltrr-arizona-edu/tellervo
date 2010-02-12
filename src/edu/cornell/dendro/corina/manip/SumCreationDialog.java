package edu.cornell.dendro.corina.manip;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.graph.GraphDialog;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleType;
import edu.cornell.dendro.corina.tridasv2.SeriesLinkUtil;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Center;
import edu.cornell.dendro.corina.util.openrecent.OpenRecent;
import edu.cornell.dendro.corina.util.openrecent.SeriesDescriptor;
import edu.cornell.dendro.corina.wsi.corina.NewTridasIdentifier;

public class SumCreationDialog {
	private DBBrowser sum;
	private String sumName = "New sum";
	private Sample baseS = null;
	
	/**
	 * Create sum based upon an existing sum by creating a new version
	 * 
	 * @param parent
	 * @param baseSample
	 */
	public SumCreationDialog(final Frame parent, Sample baseSample)
	{
		
		baseS = baseSample;
		ElementList el = null;
		
		// this sum is a modification of an existing one, so populate dbbrowser with the relevant elements
		if(baseS!=null)
		{
			el = baseS.getElements();
		}
		setup(parent, el);
	}
	
	/**
	 * Create a new sum
	 * 
	 * @param parent
	 * @param el
	 */
	public SumCreationDialog(final Frame parent, ElementList el) {
		
		setup(parent, el);
	}
	
	@SuppressWarnings("serial")
	private void setup(final Frame parent, ElementList el) {
		
		// create the db browser, but make it create the sum first before closing
		sum = new DBBrowser(parent, true, true) {
			@Override
			protected boolean finish() {
				try {
					
					if (baseS!=null)
					{
						// Based upon a previous sum so get previous name as suggestion
						sumName = baseS.getDisplayTitle();
					}

					sumName = JOptionPane.showInputDialog(parent,
							I18n.getText("question.chooseNameForSum")+": ",
							sumName);
					
			
					if ((sumName == null) || (sumName == "newSeries") || (sumName == "New sum"))
					{
						JOptionPane.showMessageDialog(this, I18n.getText("question.pleaseEnterValidName"), 
								I18n.getText("error.invalidName"), JOptionPane.ERROR_MESSAGE);
						finish();
					}
					
					return (applySum() && super.finish());
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, I18n.getText("error")+": " + e, I18n.getText("error.failedToCreateSum"), 
							JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		};
		
		// must have two for a sum!
		sum.setMinimumSelectedElements(2);



		// add the elements so they show in the dialog
		for(Element e : el)
			sum.addElement(e);
		
		
		// select the site in the first element
		/*Element e = el.get(0);
		if(e != null) {
			try	{
				BaseSample bs = e.loadBasic();
				
				if(bs.meta().hasSiteCode()) {
					String siteCode = bs.meta().getSiteCode();
					sum.selectSiteByCode(siteCode);
					sumName = "New " + siteCode + " sum";
				}
			} catch (Exception ex) {
				// ignore...
			}
		}*/

		addPreviewButton();
		
		sum.setTitle(I18n.getText("menus.tools.sum"));
		sum.setVisible(true);		
	}
	
	private boolean applySum() {
		// ok, for now this only works for creating *new* sums!

		TridasDerivedSeries series = new TridasDerivedSeries();
		
		// it's a new series? (to force update, set this to the id of the series to update!)
		TridasIdentifier identifier = null;
		String domainTag = null;
		
		series.setTitle(sumName);

		// it's a sum
		ControlledVoc voc = new ControlledVoc();
		voc.setValue(SampleType.SUM.toString());
		series.setType(voc);
		
		// Default to version 1 - will give user gui if v1 is already taken
		series.setVersion("1");
		
		// add each sum element to the sample...
		for(Element sume : sum.getSelectedElements()) {
			BaseSample bs;
			
			try {
				bs = sume.loadBasic();
			} catch (IOException ioe) {
				throw new IllegalStateException(I18n.getText("error.loadBasicFailed"));
			}
			
			TridasIdentifier sumElementId = bs.getSeries().getIdentifier();
			
			if(domainTag == null)
				domainTag = sumElementId.getDomain();
			else if(!domainTag.equals(sumElementId.getDomain())) {
				throw new IllegalArgumentException(I18n.getText("error.noSumsAcrossDomains"));
			}
		
			SeriesLinkUtil.addToSeries(series, sumElementId);
		}
		
		// create a new identifier based on the domain tag
		series.setIdentifier(identifier == null ? NewTridasIdentifier.getInstance(domainTag) : identifier);
		
		// create a new, empty sample
		Sample tmp = new Sample(series);

		try {
			CorinaWsiTridasElement cwe = new CorinaWsiTridasElement(NewTridasIdentifier.getInstance(domainTag));
			
			// here's where we do the "meat"
			if(cwe.save(tmp, sum)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
								
				// open a new editor 
				new Editor(tmp);
				return true;
			}
		} catch (UserCancelledException uce) {
			// do nothing...
		} catch (IOException ioe) {
			Alert.error(I18n.getText("error.failedToCreateSum"), I18n.getText("error")+ ": " + ioe.toString());
		}

		return false;
	}
	
	private void addPreviewButton() {
				
		JButton btnPreview = sum.getPreviewButton();
		
		btnPreview.setVisible(true);
		btnPreview.setToolTipText(I18n.getText("general.preview"));
		Icon previewIcon = Builder.getIcon("graph.png", 22);

		btnPreview.setIcon(previewIcon);

		btnPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Sample s = Sum.sum(sum.getSelectedElements());
					GraphDialog graph = new GraphDialog(sum, s, sum.getSelectedElements());
					
					Center.center(graph, sum);
					graph.setVisible(true);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(sum, I18n.getText("error.failedToCreateSum")+": " + ex.toString(), 
							I18n.getText("error"), JOptionPane.ERROR_MESSAGE);
				}
			}			
		});
	}
}
