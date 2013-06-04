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
package org.tellervo.desktop.cross;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.graph.Graph;
import org.tellervo.desktop.graph.GraphActions;
import org.tellervo.desktop.graph.GraphController;
import org.tellervo.desktop.graph.GraphInfo;
import org.tellervo.desktop.graph.GraphToolbar;
import org.tellervo.desktop.graph.GrapherEvent;
import org.tellervo.desktop.graph.GrapherListener;
import org.tellervo.desktop.graph.GrapherPanel;
import org.tellervo.desktop.graph.SkeletonPlot;
import org.tellervo.desktop.gui.Bug;
import org.tellervo.desktop.gui.ReverseScrollBar;
import org.tellervo.desktop.gui.cross.Ui_CrossdatePanel;
import org.tellervo.desktop.gui.dbbrowse.DBBrowser;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.Center;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasGenericField;


/**
 *
 * @author  peterbrewer
 * @author Lucas Madar
 */
@SuppressWarnings("serial")
public class CrossdateDialog extends Ui_CrossdatePanel implements GrapherListener {
	private JFrame window;
	
	private ElementList crossdatingElements;
	private Element firstFloating = null;
	private Element firstReference = null;
	private CrossdateCollection crossdates;
	
	private SigScoresTableModel sigScoresModel;
	private AllScoresTableModel allScoresModel;
	private HistogramTableModel histogramModel;

	private GraphActions actions;
	private GrapherPanel graph;
	private GraphInfo graphInfo;
	private GraphController graphController;
	private List<Graph> graphSamples;
	private JScrollPane graphScroller;
	private Range newCrossdateRange;

	private Boolean reviewMode = false;
	private CrossdateStatusBar status;
	
   
	/**
	 * Create a crossdate dialog with no preselected series
	 * 
	 * @param parent
	 */
    public CrossdateDialog(java.awt.Frame parent) {
    	super();
    	window = new JFrame();  
        initialize();
    }

    /**
     * Creates a new crossdate dialog with a list of preselected series
     * and a particular series set as the first floating 
     * 
     * @param parent
     * @param preexistingElements
     * @param firstFloating
     */
    public CrossdateDialog(java.awt.Frame parent,
    		ElementList preexistingElements, Element firstFloating) {
    	super();
    	window = new JFrame();
        
    	// Set up lists of series and initialize gui
    	this.firstFloating = firstFloating;
    	if(setSeriesPoolFromGUI(parent, preexistingElements)==false)
    	{
    		return;
    	}
        initialize();
    }
    
    /**
     * The specified element is shown in a crossdate dialog in 'review mode'
     * so the user can inspect the decision that was made  
     * 
     * @param parent
     * @param floating
     */
    public CrossdateDialog(java.awt.Frame parent, Element floating) {
    	super();
    	window = new JFrame();
        
    	// Load floating element and check it's a 'crossdate'
		BaseSample bs;
		try {
			bs = floating.load();
		} catch (IOException ioe) {
			// shouldn't happen?
			return;
		}	
		TridasDerivedSeries ds = (TridasDerivedSeries) bs.getSeries();
		if(!ds.getType().getValue().equals("Crossdate"))
		{
			new Bug(new Exception(I18n.getText("error.mustBeCrossdate")));
			return;
		}
		    	
		// Query for the master chronology used to do the crossdate
		SearchParameters search = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
		search.addSearchConstraint(SearchParameterName.SERIESDBID, 
				SearchOperator.EQUALS, ds.getInterpretation().getDatingReference().getLinkSeries().getIdentifier().getValue());
	
		SeriesSearchResource searchResource = new SeriesSearchResource(search);
		TellervoResourceAccessDialog dlg = new TellervoResourceAccessDialog(new JDialog(), searchResource);
		
		// start our query (remotely)
		searchResource.query();
		dlg.setVisible(true);
		
		Element reference = null;
		if(!dlg.isSuccessful()) {
			new Bug(dlg.getFailException());
		} else {
			reference = searchResource.getAssociatedResult().get(0);
		}
    	
    	// Set up lists of series
    	ElementList tmp = new ElementList();
    	tmp.add(reference);
    	tmp.add(floating);
    	crossdatingElements = tmp;
    	this.firstFloating = floating;
    	this.firstReference = reference;
    	    	
    	// Set up gui
        initialize();
        setReviewMode(true);
    }   
    
    /**
     * Show an open dialog as a child of a frame
     * @param parent
     * @param preexistingElements
     */
    private ElementList showOpenDialog(Frame parent, boolean modal, ElementList preexistingElements) {
    	DBBrowser dbb = new DBBrowser(parent, modal, true) {
			@Override
			protected boolean finish() {
				return (loadAllElements() && super.finish());
			}
		};
		
		return doOpenDialog(dbb, preexistingElements);
    }
    
    private ElementList doOpenDialog(DBBrowser dbb, ElementList preexistingElements) {
		if(preexistingElements != null)
			for(Element e : preexistingElements)
				dbb.addElement(e);
		
		// select the site in the first element
		Element e = preexistingElements.get(0);
		if(e != null) {
			try	{
				BaseSample bs = e.loadBasic();

				String siteCode = bs.meta().getSiteCode();
				if(siteCode != null)
					dbb.selectSiteByCode(siteCode);
				
			} catch (Exception ex) {
				// ignore...
			}
		}
		
    	dbb.setMinimumSelectedElements(2);
    	dbb.setTitle("Crossdate...");
    	
    	dbb.setVisible(true);
    	
    	if(dbb.getReturnStatus() != DBBrowser.RET_OK)
    		return null;
    	
    	return dbb.getSelectedElements();
    }

    /**
     * Set the pool of series from which the crossdating gui works.  This takes a list 
     * of existing series, and presents the user with a prepopulate DBBrowser to add 
     * to this list
     * 
     * @param parent
     * @param preexistingElements
     */
    private boolean setSeriesPoolFromGUI(Frame parent, ElementList preexistingElements)
    {
    	// let user choose crossdates, exit if they close quietly
    	if((crossdatingElements = showOpenDialog(parent, true, preexistingElements)) == null) {
    		window.dispose();
    		return false;
    	}
    	
    	return true;
    }
    
    /**
     * Turn the dialog into review mode (read only) for inspecting an existing crossdate
     */
    private void setReviewMode(Boolean reviewMode)
    {
    	this.reviewMode = reviewMode;
    	
    	// More specific gui stuff to set
    	if(reviewMode)
    	{
    		// Check that both floating and reference have been set before going any further
    		if (firstFloating==null || firstReference==null) 
    		{
    			System.out.println("Floating and/or reference series have not been set");
    			setReviewMode(false);
    		}
	    	
	        // Move floating to the correct position
	        Sample floatingSample;
	        Sample referenceSample;
	        String reviewString = null;
	        	        
	        try {
				floatingSample = firstFloating.load();
				referenceSample = firstReference.load();
				TridasDerivedSeries floatingSeries = (TridasDerivedSeries) floatingSample.getSeries();
				
				setFloatingPosition(floatingSample.getRange());
				
				reviewString = floatingSample.getDisplayTitle() + " " + I18n.getText("crossdate.wasDatedUsing")+ ": " + referenceSample.getDisplayTitle() + ".\n";
				reviewString += I18n.getText("meta.author")+": " + floatingSeries.getAuthor() + "\n";
				for (TridasGenericField gf: floatingSample.getSeries().getGenericFields())
				{
					if(gf.getName().equals("tellervo.justification"))
					{
						reviewString += I18n.getText("general.justification") +": "+gf.getValue().toString() + "\n";
					}
					if(gf.getName().equals("tellervo.crossdateConfidenceLevel"))
					{
						reviewString += I18n.getText("general.certainty")+ ": " + gf.getValue().toString() + " "+ I18n.getText("general.star")+"\n";
					}

				}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			this.btnCancel.setText(I18n.getText("general.close"));
			scrollInfo.setVisible(true);
			txtInfo.setText(reviewString);

    	} else 
    	{
    		this.btnCancel.setText(I18n.getText("general.cancel"));
    		scrollInfo.setVisible(false);
    	}
    	
    	// Set basic visible/enabled items
    	Boolean action = false;
    	if(reviewMode==false) action= true;    	
    	this.paneStatistics.setEnabledAt(1, action);
    	this.paneStatistics.setEnabledAt(2, action);
    	this.cboFloating.setVisible(action);
    	this.cboReference.setVisible(action);
    	this.lblPrimary.setVisible(action);
    	this.lblSecondary.setVisible(action);
    	this.btnAddRemoveSeries.setVisible(action);
    	this.btnOk.setVisible(action);


    	
    }
    
    /**
     * Set the position of the floating series to the specified range
     * 
     * @param pos
     */
    private void setFloatingPosition(Range pos)
    {
    	tableSignificantScores.setRowSelectionInterval(sigScoresModel.getRowForRange(pos), sigScoresModel.getRowForRange(pos));
    }
    
    private void initialize() {  	
    	
    	// start our new crossdates
    	crossdates = new CrossdateCollection();
    	status = new CrossdateStatusBar();
    	crossdatingElements = crossdates.setElements(crossdatingElements);   	
     	
    	// Hide unwanted components
        btnSwap.setVisible(false);
        scrollInfo.setVisible(false);
    	
        // Setup all components
    	setupTables();
    	setupGraph();
    	setupListeners();
    	setupLists();
    	
    	// add ourself to the window, center, maximize and show
    	window.setContentPane(this);
    	window.pack(); 	
    	window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	Center.center(window);
    	window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    	window.setVisible(true);
    	window.setIconImage(Builder.getApplicationIcon());
    	
    	if (tableSignificantScores.getRowCount()>0)	tableSignificantScores.setRowSelectionInterval(0, 0);

    	
    }
    
    private void setupTables() {
    	// sig scores table
       	sigScoresModel = new SigScoresTableModel(tableSignificantScores);
    	tableSignificantScores.setModel(sigScoresModel);
    	sigScoresModel.applyFormatting();
    	
    	tableSignificantScores.getTableHeader().setReorderingAllowed(false);    	
    	tableSignificantScores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tableSignificantScores.setRowSelectionAllowed(true);
    	tableSignificantScores.setColumnSelectionAllowed(false);
    	    	
    	// all scores table
    	allScoresModel = new AllScoresTableModel(tblAllScores);
    	tblAllScores.setModel(allScoresModel);
    	allScoresModel.applyFormatting();
    	
    	tblAllScores.getTableHeader().setReorderingAllowed(false);
    	tblAllScores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tblAllScores.setCellSelectionEnabled(true);
    	
    	// histogram table
    	histogramModel = new HistogramTableModel(tblHistogram);
    	tblHistogram.setModel(histogramModel);
    	histogramModel.applyFormatting();
    	
    	tblHistogram.getTableHeader().setReorderingAllowed(false);    	
    	tblHistogram.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tblHistogram.setRowSelectionAllowed(false);
    	tblHistogram.setColumnSelectionAllowed(false);
    }
    
    private void setupListeners() {    	

    	// whenever one of our combo boxes change...
    	ActionListener listChanged = new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			int row = cboReference.getSelectedIndex();
    			int col = cboFloating.getSelectedIndex();
    			
    			// make a nice title?
    			window.setTitle(I18n.getText("crossdate.crossdating")+": " + cboFloating.getSelectedItem().toString());
    			
    			try {
    				CrossdateCollection.Pairing pairing = crossdates.getPairing(row, col);
    				status.setPairing(pairing);
    				sigScoresModel.setCrossdates(pairing);
    				allScoresModel.setCrossdates(pairing);
    				histogramModel.setCrossdates(pairing);
    				updateTables();
    				
    			} catch (CrossdateCollection.NoSuchPairingException nspe) {
    				status.setPairing(null);
    				sigScoresModel.clearCrossdates();
    				allScoresModel.clearCrossdates();
    				histogramModel.clearCrossdates();
    				newCrossdateRange = null;
    			}
    			
    			// If table has rows - select first (most significant)
    			if (tableSignificantScores.getRowCount()>0)	tableSignificantScores.setRowSelectionInterval(0, 0);
    		}
    	};
    	
    	cboReference.addActionListener(listChanged);
    	cboFloating.addActionListener(listChanged);
    	
    	// now, when our table row changes
    	tableSignificantScores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting() || tableSignificantScores.getSelectedRow() == -1) 
					return;
				
				// deselect anything in tblAllScores
				tblAllScores.clearSelection();

				int row = tableSignificantScores.getSelectedRow();
				
				// make our new range
				newCrossdateRange = sigScoresModel.getSecondaryRangeForRow(row);
				
				// make the graph reflect the row we selected!
				updateGraph(sigScoresModel.getGraphForRow(row));
			}
    	});
    	
    	// ok, now our all scores table. More complicated, because it can change both col & row selection.
    	ListSelectionListener allScoresSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				// don't fire if we're deselecting
				if(lse.getValueIsAdjusting() || 
						tblAllScores.getSelectedColumn() == -1 || 
						tblAllScores.getSelectedRow() == -1)
					return;
				
				// unset any selections in sig scores
				tableSignificantScores.clearSelection();
			
				int row = tblAllScores.getSelectedRow();
				int col = tblAllScores.getSelectedColumn();
				
				// make our new range
				newCrossdateRange = allScoresModel.getSecondaryRangeForCell(row, col); 
				
				// just like before...
				updateGraph(allScoresModel.getGraphForCell(row, col));
			}
    	};
    	tblAllScores.getSelectionModel().addListSelectionListener(allScoresSelectionListener);
    	tblAllScores.getColumnModel().getSelectionModel().addListSelectionListener(allScoresSelectionListener);
    	
    	// when the score type selected on our all scores table changes
    	cboDisplayStats.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			ScoreType score = (ScoreType) cboDisplayStats.getSelectedItem();
    			
    			// show scores for this class...
    			if(score != null)
    				allScoresModel.setScoreClass(score.scoreClass);
    				histogramModel.setScoreClass(score.scoreClass);
    				
    		}
    	});
   	
    	// Favourite stat type changed
    	cboDisplayStats.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			updateTables();
    		}
    	});
    	
    	
    	// reset button...
    	btnResetPosition.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			if(reviewMode)
    			{
    		        // Move floating to the correct position
    		        Sample s;
    		        try {
    					s = firstFloating.load();
    					setFloatingPosition(s.getRange());
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			}
    			else
    			{
    				initialize();
    			}
    		}
    	});
    	
    	btnAddRemoveSeries.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    	    	// let user choose crossdates, exit if they close quietly
    			ElementList tmpElements;
    	    	if((tmpElements = showOpenDialog(window, true, crossdatingElements)) == null)
    	    		return; // user cancelled
    	    	
    	    	crossdatingElements = tmpElements;
    	    	
    	    	// start our new crossdates
    	    	crossdates = new CrossdateCollection();
    	    	crossdatingElements = crossdates.setElements(crossdatingElements);   	

    	    	// try to keep our settings where they were
    	    	Object o1 = cboReference.getSelectedItem();
    	    	Object o2 = cboFloating.getSelectedItem();    	    	
    	    	if (o1 instanceof Sample){
    	    		firstReference =  new CachedElement((Sample)o1);
    	    	} else {
    	    		firstReference = (Element) o1;
    	    	}	
    	    	if (o2 instanceof Sample){
    	    		firstFloating =  new CachedElement((Sample)o2);
    	    	} else {
    	    		firstFloating = (Element) o2;
    	    	}
    	    	setupLists();
    	    	
    	    	
    		}
    	});

    	final CrossdateDialog glue = this;
       	btnOk.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			int row = cboReference.getSelectedIndex();
    			int col = cboFloating.getSelectedIndex();
    			    			
    			try {
    				CrossdateCollection.Pairing pairing = crossdates.getPairing(row, col);
    				
    				CrossdateCommitDialog commit = new CrossdateCommitDialog(window, true, pairing.getPrimary(), pairing.getSecondary(), newCrossdateRange);
    				commit.setLocationRelativeTo(glue);
    				commit.setVisible(true);
    				
    				if(commit.didSave())
    					window.dispose();
    			} catch (CrossdateCollection.NoSuchPairingException nspe) {
    				JOptionPane.showMessageDialog(window, "Choose a valid crossdate", 
    						"Can't crossdate", JOptionPane.ERROR_MESSAGE);
    			}
    		}
       	});

       	btnCancel.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {   
    			window.dispose();
    		}
       	});
}
    
    private void setupGraph() {	    	
		// create a new graphinfo structure, so we can tailor it to our needs.
		graphInfo = new GraphInfo();
		
		// force no drawing of graph names and drawing of vertical axis
		graphInfo.setShowGraphNames(false);
		graphInfo.setShowVertAxis(true);
		
		// set up our samples
		graphSamples = new ArrayList<Graph>(2);
				
		// create a graph panel; put it in a scroll pane
		graph = new GrapherPanel(graphSamples, null, graphInfo) {
			@Override
			public Dimension getPreferredSize(Dimension parent, Dimension scroll) {
				// our height is the size of the graph or the size of the viewport
				// whichever is greater
				return new Dimension(parent.width, Math.max(getGraphHeight(), scroll.height));
			}
		};
		graph.setUseVerticalScrollbar(true);
		graph.setEmptyGraphText("Choose a crossdate");
		
		graphScroller = new JScrollPane(graph,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphScroller.setVerticalScrollBar(new ReverseScrollBar());

		// make the default viewport background the same color as the graph
		graphScroller.getViewport().setBackground(graphInfo.getBackgroundColor());
		
		graphController = new GraphController(graph, graphScroller);
				
		panelChart.setLayout(new BorderLayout());
		panelChart.add(graphScroller, BorderLayout.CENTER);
		
		// add a toolbar
    	actions = new GraphActions(graph, null, graphController);
    	GraphToolbar graphToolbar = new GraphToolbar(actions);
    	panelChart.add(graphToolbar, BorderLayout.NORTH);
    	
    	// add a status bar
    	panelChart.add(status, BorderLayout.SOUTH);
    	
    	// listen to this graph
    	graph.addGrapherListener(this);
    	
		// get our basic graph set up
		updateGraph(null);
    }
    
    private void setupLists() {
    	ArrayList<Object> samples = new ArrayList<Object>();
    	List<Element> myElements = crossdatingElements.toActiveList();

    	// make a list of samples... (so we can get range, etc)
    	for(Element e : myElements) {
    		try {
    			Sample s = e.load();
    			samples.add(s);
    		} catch (Exception ex) {
    			samples.add(e);
    		}
    	}
    	
    	cboReference.setModel(new DefaultComboBoxModel(samples.toArray()));
    	cboFloating.setModel(new DefaultComboBoxModel(samples.toArray()));
    	
    	// choose what shows up by default in our combos
    	if(firstFloating == null && firstReference == null) {
    		// easy case
    		cboReference.setSelectedIndex(0);
    		cboFloating.setSelectedIndex(1);
    	}
    	else {
    		// ensure our 'reference' box is populated
    		boolean haveReference = false;
    		boolean haveFloating = false;
    		
    		for(int i = 0; (i < myElements.size()) && !(haveReference && haveFloating); i++) {
    			Element e = myElements.get(i);
    			
    			if(!haveFloating && ((firstFloating == null && !e.equals(firstReference)) || e.equals(firstFloating))) {
    				cboFloating.setSelectedIndex(i);
    				haveFloating = true;
    			}    			
    			else if(!haveReference && (firstReference == null || e.equals(firstReference))) {
    				cboFloating.setSelectedIndex(i);
    				haveReference = true;
    			}    			
    		}
    	}
    	
    	// Now, the score types
    	ArrayList<ScoreType> scoreTypes = new ArrayList<ScoreType>();
    	// make a nice list of all score types
    	for(String t : Cross.ALL_CROSSDATES) {
    		try {
    			scoreTypes.add(new ScoreType(t));
    		} catch (Exception ex) {
    			continue;
    		}
    	}
    	
    	// and set the model
    	cboDisplayStats.setModel(new DefaultComboBoxModel(scoreTypes.toArray()));
    	//cboDisplayHistogram.setModel(new DefaultComboBoxModel(scoreTypes.toArray()));
    	
    	cboDisplayStats.setSelectedIndex(0);
    	//cboDisplayHistogram.setSelectedIndex(0);
    }

    /**
     * A score type class 
     * so we can stick classes in a combo box and have it look pretty
     */
    private static class ScoreType {
    	public Class<?> scoreClass;
    	public String name;
    	
    	public ScoreType(String className) throws Exception {
    		try {
				scoreClass = Class.forName(className);
				Method m = scoreClass.getDeclaredMethod("getNameStatic", (Class[]) null);
				name = (String) m.invoke((Object[]) null, (Object[]) null);
			} catch (Exception e) {
				throw e; // lame, but ok. we don't really care.
			}
    	}
    	
    	public String toString() {
    		return name;
    	}
    }
    
    private void updateTables()
    {
    	sigScoresModel.sortByStatType(StatType.fromName(cboDisplayStats.getSelectedItem().toString()));
    	
    }
    
    private void updateGraph(List<Graph> newGraphs) {    	
   		graphSamples.clear();
   		
    	if(!(newGraphs == null || newGraphs.size() != 2)) {
    		// copy the graphs over
    		graphSamples.add(newGraphs.get(0));
    		graphSamples.add(newGraphs.get(1));
    		
    		// make sure we can't drag our fixed graph
    		newGraphs.get(0).setDraggable(false);
    		
    		// also, display the moving range
    		status.setMovingRange(newGraphs.get(1).getRange());
    		
    		// fit the height of the graph
        	graphController.scaleToFitHeight(5);
    	}
    	else
    		status.setMovingRange(null);
    	
		graphInfo.setShowVertAxis(true);
		
		
    	graph.update(true);
		btnOk.setEnabled(graphSamples.size() == 2);
    }

    /** Get notified when the graph changes */
	public void graphChanged(GrapherEvent evt) {
		if(evt.getEventType() == GrapherEvent.Type.XOFFSET_CHANGED) {
			if(graphSamples.size() == 2)
				newCrossdateRange = graphSamples.get(1).getRange();
				status.setMovingRange(newCrossdateRange);
		}
	}
	
	/**
	 * A status bar that works with the graph
	 * Shows all the scores, offset, moving range
	 * 
	 * @author Lucas Madar
	 */
	public static class CrossdateStatusBar extends JPanel {
		private JLabel range;
		private JLabel overlap;
		private JLabel[] contentHeading;
		private JLabel[] content;
		private CrossdateCollection.Pairing pairing;
		
		public CrossdateStatusBar() {			
			range = new JLabel();
			overlap = new JLabel();
			setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			add(range);
			add(Box.createHorizontalStrut(8));
			add(new JSeparator(JSeparator.VERTICAL));
			add(Box.createHorizontalStrut(4));
			add(overlap);
			add(Box.createHorizontalStrut(8));
			add(new JSeparator(JSeparator.VERTICAL));
			add(Box.createHorizontalGlue());

			// create a bunch of JLabels...
			int n = SigScoresTableModel.columns.size();
			contentHeading = new JLabel[n];
			content = new JLabel[n];
			for(int i = 0; i < n; i++) {
				if(i > 0) {
					add(Box.createHorizontalStrut(8));
					add(new JSeparator(JSeparator.VERTICAL));
					add(Box.createHorizontalStrut(4));
				}
				contentHeading[i] = new JLabel(SigScoresTableModel.columns.get(i).heading);
				add(contentHeading[i]);
				add(Box.createHorizontalStrut(4));
				content[i] = new JLabel("     ");
				add(content[i]);
			}
			
			setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		}
		
		private void clearContent() {
			range.setText("");
			overlap.setText("");
			for(int i = 0; i < content.length; i++) {
				content[i].setText("     ");
			}
		}
		
		public void setPairing(CrossdateCollection.Pairing pairing) {
			this.pairing = pairing;
			clearContent();
		}
		
		public void setMovingRange(Range mRange) {
			if(mRange == null) {
				clearContent();
				return;
			}
			
			range.setText("Moving range: " + mRange);
			overlap.setText("Overlap: " + mRange.overlap(pairing.getPrimary().getRange()));
			
			for(int i = 0; i < SigScoresTableModel.columns.size(); i++) {
				// score type for this column
				Class<?> scoreClass = SigScoresTableModel.columns.get(i).scoreType;
				Cross cross = pairing.getCrossForClass(scoreClass);

				contentHeading[i].setEnabled(cross != null);
				content[i].setEnabled(cross != null);
				
				if(cross == null) {
					content[i].setText("n/a");
					return;
				}
				
				DecimalFormat df = new DecimalFormat(cross.getFormat());				
				float val = cross.getScore(mRange.getEnd());
				
				content[i].setText(df.format(val));
			}			
		}
	}
	
	public enum StatType {
		TSCORE ("T-Score"),
		DSCORE ("D-Score"),
		TREND ("Trend"), 
		WJ ("Weiserjahre"),
		RVALUE ("R-Value");
		
		final String name;
		
		StatType(String name){
		this.name = name;

	}
		
	public final String toString(){ return this.name;}

	public static StatType fromName(String name){ 
		for (StatType val : StatType.values()){
			if (val.toString().equals(name)) return val;
		}
		
		return null;
		
	}
	
	}
	
	
}
