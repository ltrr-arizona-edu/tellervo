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
package org.tellervo.desktop.manip;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.graph.GraphDialog;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.sample.TellervoWSILoader;
import org.tellervo.desktop.tridasv2.LabCode;
import org.tellervo.desktop.tridasv2.LabCodeFormatter;
import org.tellervo.desktop.tridasv2.SeriesLinkUtil;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tellervo.desktop.wsi.tellervo.NewTridasIdentifier;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasIdentifier;


public class SumCreationDialog {
	private DBBrowser sum;
	private String sumName = null;
	private Sample baseS = null;
	private ElementList allComponents = new ElementList();
	
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
	private void setup(final Frame parent, final ElementList el) {
				
		// create the db browser, but make it create the sum first before closing
		sum = new DBBrowser(parent, true, true) {
			@Override
			protected boolean finish() {
				try {
					// Calculate a name to suggest to the user
					setSuggestedName(this.getSelectedElements());
					
					// Show suggested name to user for confirmation
					sumName = JOptionPane.showInputDialog(parent,
							I18n.getText("question.chooseNameForSum")+": ",
							sumName);
					
					// Check to see if user canceled
					if (sumName == null) return false;
					
					// Check we're happy with the name
					if ((sumName == "newSeries") || (sumName == "New sum"))
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
			TellervoWSILoader cwe = new TellervoWSILoader(NewTridasIdentifier.getInstance(domainTag));
			
			// here's where we do the "meat"
			if(cwe.save(tmp, sum)) {
				// put it in our menu
				OpenRecent.sampleOpened(new SeriesDescriptor(tmp));
								
				// open a new editor 
				FullEditor editor = FullEditor.getInstance();
				editor.addSample(tmp);
				return true;
			}
		} catch (UserCancelledException uce) {
			// do nothing...
		} catch (IOException ioe) {
			Alert.error(I18n.getText("error.failedToCreateSum"), I18n.getText("error")+ ": " + ioe.toString());
		}

		return false;
	}
	
	
	/**
	 * Set the sum name to something sensible based upon its child series
	 */
	private void setSuggestedName(ElementList el){
		
		recurseAddElementsToList(el);
		ArrayList<String> objlist = new ArrayList<String>();
		ArrayList<String> ellist = new ArrayList<String>();
		ArrayList<String> samplist = new ArrayList<String>();
		ArrayList<String> radlist = new ArrayList<String>();
		ArrayList<String> serlist = new ArrayList<String>();
		ArrayList<String> uniqueVals = new ArrayList<String>();
		
		// Load the first sample.  We'll use this a representative when 
		// creating labcodes
		Sample anysample = null;
		try {
			anysample = el.get(0).load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		
		// Loop through all the referenced elements
		for (Element item : allComponents)
		{
			Sample thisSample;
			try {
				thisSample = item.load();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			// Get the various portions of the labcode
			objlist.add(LabCodeFormatter.getElementPrefixFormatter().format(thisSample.getMeta(Metadata.LABCODE, LabCode.class)));
			ellist.add(LabCodeFormatter.getSamplePrefixFormatter().format(thisSample.getMeta(Metadata.LABCODE, LabCode.class)));
			samplist.add(LabCodeFormatter.getRadiusPrefixFormatter().format(thisSample.getMeta(Metadata.LABCODE, LabCode.class)));
			radlist.add(LabCodeFormatter.getSeriesPrefixFormatter().format(thisSample.getMeta(Metadata.LABCODE, LabCode.class)));
			serlist.add(thisSample.getDisplayTitle());
				
		}
		
		// First check objects to see if they are all the same
		uniqueVals.clear();
		for (String x : objlist){
		    if (!uniqueVals.contains(x))
		        uniqueVals.add(x);
		}
		if (uniqueVals.size()>1){
			// Not even the objects are the same so suggest simply 'new sum'
			sumName = "New sum";
			return;
		}
		
		// Now elements
		uniqueVals.clear();
		for (String x : ellist){
		    if (!uniqueVals.contains(x))
		        uniqueVals.add(x);
		}
		if (uniqueVals.size()>1){
			// Elements are different so just set the sum name to the object code
			anysample.getMetaString("tellervo.objectCode[0]");
			sumName = LabCodeFormatter.getElementPrefixFormatter().format(anysample.getMeta(Metadata.LABCODE, LabCode.class));
			return;
		}
		
		// Now samples
		uniqueVals.clear();
		for (String x : samplist){
		    if (!uniqueVals.contains(x))
		        uniqueVals.add(x);
		}
		if (uniqueVals.size()>1){
			sumName = LabCodeFormatter.getSamplePrefixFormatter().format(anysample.getMeta(Metadata.LABCODE, LabCode.class));
			return;
		}
		
		// Now radii
		uniqueVals.clear();
		for (String x : radlist){
		    if (!uniqueVals.contains(x))
		        uniqueVals.add(x);
		}
		if (uniqueVals.size()>1){
			sumName = LabCodeFormatter.getRadiusPrefixFormatter().format(anysample.getMeta(Metadata.LABCODE, LabCode.class));
			return;
		}

		// Now series
		uniqueVals.clear();
		for (String x : serlist){
		    if (!uniqueVals.contains(x))
		        uniqueVals.add(x);
		}
		if (uniqueVals.size()>1){
			sumName = LabCodeFormatter.getSeriesPrefixFormatter().format(anysample.getMeta(Metadata.LABCODE, LabCode.class));
			return;
		}
		else
		{
			sumName = anysample.getDisplayTitle();
		}

	}
	
	private void recurseAddElementsToList(ElementList elements) {
		for(Element e : elements) {
			if(e instanceof CachedElement) {
				CachedElement ce = (CachedElement) e;
				
				// need the basic...
				if(!ce.hasBasic()) {
					System.err.println("Cached, but not even basic loaded!");
					continue;
				}
				
				// add to list
				allComponents.add(ce);
								
				// can't go any deeper if there's no series...
				if(!ce.hasFull())
					continue;
				
				try {
					Sample s = ce.load();
					ElementList sampleElements = s.getElements();
					if(sampleElements != null)
						recurseAddElementsToList(sampleElements);
				} catch (IOException ioe) {
					// shouldn't happen
				} 
			}
			else
				System.err.println("Non-cached element: " + e);
		}
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
