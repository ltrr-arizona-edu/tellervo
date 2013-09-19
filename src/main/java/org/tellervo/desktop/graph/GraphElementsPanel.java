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
package org.tellervo.desktop.graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.ValidationEventCollector;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.io.view.ImportView;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.LocalTridasFileLoader;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.openrecent.OpenRecent;
import org.tellervo.desktop.util.openrecent.SeriesDescriptor;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.formats.tridas.TridasWriter;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasTridas;
import org.tridas.schema.TridasValues;
import org.tridas.schema.TridasVariable;


public class GraphElementsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public GraphElementsPanel(List<Graph> samples, GraphWindow gwindow) {
		super(new BorderLayout());
		
		this.window = gwindow;
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		
		// Create panel to hold legend
		JPanel legendPanel = new JPanel();
		legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
		legendPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Components"));
		topPanel.add(legendPanel);
		
		// Create list of series
		listmodel = new DefaultListModel();
		list = new JList(listmodel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		legendPanel.add(list);
		
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				window.listSelectionChanged();

				boolean enabled = list.getSelectedIndex() < 0 ? false : true;

				removeButton.setEnabled(enabled);
				colorselect.setEnabled(enabled);
			}
		});
		
		JPanel colorSelectorPanel = new JPanel();
		colorSelectorPanel.setLayout(new BoxLayout(colorSelectorPanel, BoxLayout.X_AXIS));
		JLabel lblColor = new JLabel();
		lblColor.setText("Selected color:");
		colorselect = new ColorComboBox();
		colorSelectorPanel.add(lblColor);
		colorSelectorPanel.add(colorselect);
		legendPanel.add(colorSelectorPanel);
		
		colorselect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				window.setActiveColor(colorselect.getSelectedColor());
			}
		});
		
		add(topPanel, BorderLayout.NORTH);
		loadSamples(samples);
				
	    JPanel addButtonContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
	    add(addButtonContainer, BorderLayout.CENTER);
	    
	    JButton btnAddFromFile = new JButton("Add from file...");
	    addButtonContainer.add(btnAddFromFile);
	    final Frame glue = gwindow;
	    
	    btnAddFromFile.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {

	    		// custom jfilechooser
	    		File file = null;
	    		String format = null;
	    		JFileChooser fc = new JFileChooser();
	    	
	    		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    		fc.setMultiSelectionEnabled(false);
	    								
	    		// Loop through formats and create filters for each
	    		fc.setAcceptAllFileFilterUsed(false);
	    		ArrayList<DendroFileFilter> filters = TridasIO.getFileReadingFilterArray();
	    		Collections.sort(filters);
	    		for(DendroFileFilter filter : filters)
	    		{
	    			fc.addChoosableFileFilter(filter);
	    			if(App.prefs.getPref(PrefKey.IMPORT_FORMAT, null)!=null)
	    			{
	    				if(App.prefs.getPref(PrefKey.IMPORT_FORMAT, null).equals(filter.getFormatName()))
	    				{
	    					fc.setFileFilter(filter);
	    				}
	    			}
	    		}
	    		

	    		// Pick the last used directory by default
	    		try{
	    			File lastDirectory = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
	    			if(lastDirectory != null){
	    				fc.setCurrentDirectory(lastDirectory);
	    			}
	    		} catch (Exception e)
	    		{
	    		}
			    
	    		Integer retValue = fc.showOpenDialog(glue);
	    		
	    		if (retValue == JFileChooser.APPROVE_OPTION) {
	    			file = fc.getSelectedFile();
	    			String formatDesc = fc.getFileFilter().getDescription();
	    			try{
	    				format = formatDesc.substring(0, formatDesc.indexOf("(")).trim();
		    			ImportDataOnly importDO = new ImportDataOnly(glue, file, format);
		    			Integer seriesPlotted = 0;
		    			for(ITridasSeries series : importDO.getSeries())
		    			{
		    				if(!series.isSetValues()) continue;
		    				
		    				if(series.getValues().size()==1)
		    				{
		    					TridasVariable var = new TridasVariable();
		    					var.setNormalTridas(NormalTridasVariable.RING_WIDTH);
		    					series.getValues().get(0).setVariable(var);
		    				}
		    				
		    				Boolean allgood = false;
		    				for(TridasValues  tvs: series.getValues())
		    				{
		    					if(tvs.isSetVariable() 
		    							&& tvs.getVariable().isSetNormalTridas() 
		    							&& tvs.getVariable().getNormalTridas().equals(NormalTridasVariable.RING_WIDTH))
		    					{
		    						allgood = true;
		    					}
		    				}
		    					
		    				
		    				if(allgood)
		    				{
			    				Sample sample = new Sample(series);
			    				sample.setLoader(new LocalTridasFileLoader(series));
			    				
			    				sample.setMeta("filename", series.getTitle());
			    				sample.setMeta("title", series.getTitle());
			    				
			    				window.add(sample);
			    				seriesPlotted++;
		    				}
		    			}
		    			
		    			if(seriesPlotted==0)
		    			{
		    				Alert.error(window, "Error", "Unable to find any ring-width data to plot");
		    			}
		    			
	    			} catch (Exception e){}
	    		}
	    	}

				
			});
	    		
	    
	    btnAddFromDB = new JButton("Add from database...");
	    
	    if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_DISABLED, false)) btnAddFromDB.setVisible(false);
	    
	    addButtonContainer.add(btnAddFromDB);
	    btnAddFromDB.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
	    		Window ancestor = SwingUtilities.getWindowAncestor(GraphElementsPanel.this);
	    		
				DBBrowser browser; 
				
				if(ancestor instanceof Dialog)
					browser = new DBBrowser((Dialog) ancestor, true, true);
				else if(ancestor == null || window instanceof Frame)
					browser = new DBBrowser((Frame) ancestor, true, true);
				else
					throw new IllegalStateException("GraphElementsPanel has no real parents!");
	    		
	    		browser.setVisible(true);
	    		
	    		if(browser.getReturnStatus() == DBBrowser.RET_OK) {
	    			ElementList ss = browser.getSelectedElements();
	    			
	    			for(Element e : ss) {
	    				// load it
	    				Sample s;
	    				try {
	    					s = e.load();
	    				} catch (IOException ioe) {
	    					Alert.error("Error Loading Sample",
	    							"Can't open this file: " + ioe.getMessage());
	    					continue;
	    				}

	    				OpenRecent.sampleOpened(new SeriesDescriptor(s));
	    				
	    				// add it to graph
	    				window.add(s);
	    			}
	    		}	
	    	}
	    });
	    
	

	    removeButton = new JButton("Remove");
	    removeButton.setEnabled(false);
	    addButtonContainer.add(removeButton);
	    removeButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent ae) {
	    		window.remove(getSelectedIndex());
	    	}
	    });
	}
	
	public void setSelectedIndex(int idx) {
		list.setSelectedIndex(idx);
	}
	
	public void setColor(Color c) {
		colorselect.setColor(c);
	}
	
	public int getSelectedIndex() {
		return list.getSelectedIndex();
	}
	
	public void loadSamples(List<Graph> samples) {
		listmodel.clear();
		for(int i = 0; i < samples.size(); i++) {
			Graph cg = (Graph) samples.get(i);
			
			listmodel.addElement(cg.graph.toString());
		}
		list.revalidate();
	}
	
	private DefaultListModel listmodel;
	private JList list;
	private GraphWindow window;
	private JButton btnAddFromDB;
	private JButton removeButton;
	private JButton btnAddFile;
	private ColorComboBox colorselect;
}
