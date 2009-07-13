package edu.cornell.dendro.corina.cross;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
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

import edu.cornell.dendro.corina.Range;
import edu.cornell.dendro.corina.cross.gui.Ui_CrossdatePanel;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.graph.GraphActions;
import edu.cornell.dendro.corina.graph.GraphController;
import edu.cornell.dendro.corina.graph.GraphInfo;
import edu.cornell.dendro.corina.graph.GraphPrefs;
import edu.cornell.dendro.corina.graph.GraphToolbar;
import edu.cornell.dendro.corina.graph.GrapherEvent;
import edu.cornell.dendro.corina.graph.GrapherListener;
import edu.cornell.dendro.corina.graph.GrapherPanel;
import edu.cornell.dendro.corina.gui.ReverseScrollBar;
import edu.cornell.dendro.corina.gui.dbbrowse.DBBrowser;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.util.Center;

/**
 *
 * @author  peterbrewer
 * @author Lucas Madar
 */
@SuppressWarnings("serial")
public class Crossdater extends Ui_CrossdatePanel implements GrapherListener {
	private JFrame window;
	
	private ElementList crossdatingElements;
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

	private CrossdateStatusBar status;
	
    /** Creates new form CrossDatingWizard */
    public Crossdater(java.awt.Frame parent) {
    	super();

    	window = new JFrame();
     
        initialize(parent, null, null);
    }

    /**
     * Creates a new CrossDatingWizaard with the Element specified selected as the secondary
     * 
     * @param parent
     * @param preexistingElements
     * @param firstSecondary
     */
    public Crossdater(java.awt.Frame parent,
    		ElementList preexistingElements, Element firstSecondary) {
    	super();
    	
    	window = new JFrame();
        
        initialize(parent, preexistingElements, firstSecondary);
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

    private void initialize(Frame parent, ElementList preElements, Element firstSecondary) {  	
    	// let user choose crossdates, exit if they close quietly
    	if((crossdatingElements = showOpenDialog(parent, true, preElements)) == null) {
    		window.dispose();
    		return;
    	}
    	
    	// start our new crossdates
    	crossdates = new CrossdateCollection();
    	crossdatingElements = crossdates.setElements(crossdatingElements);   	
     	
    	status = new CrossdateStatusBar();
    	
    	setupTables();
    	setupGraph();
    	setupListeners();
    	setupLists(null, firstSecondary);
    	
    	// add ourself to the window...
    	window.setContentPane(this);
    	window.pack();
    	
    	window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    	// first, center it for it current size
    	Center.center(window);
    	// then maximize the window
    	window.setExtendedState(window.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    	window.setVisible(true);
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
    			int row = cboPrimary.getSelectedIndex();
    			int col = cboSecondary.getSelectedIndex();
    			
    			// make a nice title?
    			window.setTitle("Crossdating: " + cboSecondary.getSelectedItem().toString());
    			
    			try {
    				CrossdateCollection.Pairing pairing = crossdates.getPairing(row, col);
    				status.setPairing(pairing);
    				sigScoresModel.setCrossdates(pairing);
    				allScoresModel.setCrossdates(pairing);
    				histogramModel.setCrossdates(pairing);
    			} catch (CrossdateCollection.NoSuchPairingException nspe) {
    				status.setPairing(null);
    				sigScoresModel.clearCrossdates();
    				allScoresModel.clearCrossdates();
    				histogramModel.clearCrossdates();
    				newCrossdateRange = null;
    			}
    			
				updateGraph(null);
    		}
    	};
    	
    	cboPrimary.addActionListener(listChanged);
    	cboSecondary.addActionListener(listChanged);
    	
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
    		}
    	});

    	// when the score type selected on our histogram table changes
    	cboDisplayHistogram.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			ScoreType score = (ScoreType) cboDisplayHistogram.getSelectedItem();
    			
    			// show scores for this class...
    			if(score != null)
    				histogramModel.setScoreClass(score.scoreClass);
    		}
    	});
    	
    	// swap button...
    	btnSwap.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			int i1 = cboPrimary.getSelectedIndex();
    			int i2 = cboSecondary.getSelectedIndex();
    			
    			cboPrimary.setSelectedIndex(i2);
    			cboSecondary.setSelectedIndex(i1);
    		}
    	});

    	btnAddMeasurement.addActionListener(new ActionListener() {
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
    	    	Object o1 = cboPrimary.getSelectedItem();
    	    	Object o2 = cboSecondary.getSelectedItem();
    	    	setupLists(((o1 instanceof Sample) ? new CachedElement((Sample)o1) : (Element) o1),
    	    			((o2 instanceof Sample) ? new CachedElement((Sample)o2) : (Element) o2));
    		}
    	});

       	btnOk.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			int row = cboPrimary.getSelectedIndex();
    			int col = cboSecondary.getSelectedIndex();
    			    			
    			try {
    				CrossdateCollection.Pairing pairing = crossdates.getPairing(row, col);
    				
    				CrossdateCommitDialog commit = new CrossdateCommitDialog(window, true);
    				Center.center(commit, window);
    				
    				commit.setup(pairing.getPrimary(), pairing.getSecondary(), newCrossdateRange);
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
    
    private void setupLists(Element firstPrimary, Element firstSecondary) {
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
    	
    	cboPrimary.setModel(new DefaultComboBoxModel(samples.toArray()));
    	cboSecondary.setModel(new DefaultComboBoxModel(samples.toArray()));
    	
    	// choose what shows up by default in our combos
    	if(firstSecondary == null && firstPrimary == null) {
    		// easy case
    		cboPrimary.setSelectedIndex(0);
    		cboSecondary.setSelectedIndex(1);
    	}
    	else {
    		// ensure our 'secondary' box is populated
    		boolean havePrimary = false;
    		boolean haveSecondary = false;
    		
    		for(int i = 0; (i < myElements.size()) && !(havePrimary && haveSecondary); i++) {
    			Element e = myElements.get(i);
    			
    			if(!haveSecondary && ((firstSecondary == null && !e.equals(firstPrimary)) || e.equals(firstSecondary))) {
    				cboSecondary.setSelectedIndex(i);
    				haveSecondary = true;
    			}    			
    			else if(!havePrimary && (firstPrimary == null || e.equals(firstPrimary))) {
    				cboPrimary.setSelectedIndex(i);
    				havePrimary = true;
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
    	cboDisplayHistogram.setModel(new DefaultComboBoxModel(scoreTypes.toArray()));
    	
    	cboDisplayStats.setSelectedIndex(0);
    	cboDisplayHistogram.setSelectedIndex(0);
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
				status.setMovingRange(graphSamples.get(1).getRange());
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
}
