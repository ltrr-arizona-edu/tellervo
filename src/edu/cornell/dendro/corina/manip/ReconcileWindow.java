package edu.cornell.dendro.corina.manip;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;

import edu.cornell.dendro.corina.Year;
import edu.cornell.dendro.corina.editor.DecadalModel;
import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.graph.GraphActions;
import edu.cornell.dendro.corina.graph.GraphController;
import edu.cornell.dendro.corina.graph.GraphInfo;
import edu.cornell.dendro.corina.graph.GraphToolbar;
import edu.cornell.dendro.corina.graph.GrapherPanel;
import edu.cornell.dendro.corina.gui.Bug;
import edu.cornell.dendro.corina.gui.ReverseScrollBar;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.util.Center;

import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.graph.GraphActions;
import edu.cornell.dendro.corina.graph.GraphController;
import edu.cornell.dendro.corina.graph.GraphInfo;
import edu.cornell.dendro.corina.graph.GraphToolbar;
import edu.cornell.dendro.corina.graph.GrapherEvent;
import edu.cornell.dendro.corina.graph.GrapherListener;
import edu.cornell.dendro.corina.graph.GrapherPanel;

public class ReconcileWindow extends XFrame implements ReconcileNotifier, SaveableDocument {
	
	private ReconcileDataView dv1, dv2;
	private Sample s1, s2;
    private Sample originals1; // for aborting.
    private Sample originals2; // for aborting.
	
	private JButton btnShowHideRef; // shows/hides our ref panel
	private JButton btnCancel;
	private JButton btnFinish;
	private JButton btnViewType;
	private JButton btnRemeasure;
	private JPanel refPanel; // the panel with the reference measurement
	private JSeparator sepLine;
	protected JPanel panelChart;
	
	private GraphActions actions;	
	private GrapherPanel graph;	
	private GraphInfo graphInfo;
	private List<Graph> graphSamples;
	private JScrollPane graphScroller;	
	private GraphController graphController;	
	
	
	private boolean extraIsShown = false;
	private final static String EXPAND = "Show reference";
	private final static String HIDE = "Hide reference";
	
	public ReconcileWindow(Sample s1, Sample s2) {
		JPanel content = new JPanel(new BorderLayout());
				
		setTitle("Reconciling " + s1.toSimpleString());

		// create a copy of our samples before we even touch them
		// we keep s2 now for future compatibility, when we might have multiple
		// windows referring to s2 open
		originals1 = new Sample();
		originals2 = new Sample();
		Sample.copy(s1, originals1);
		Sample.copy(s2, originals2);		

		this.s1 = s1;
		this.s2 = s2;
		dv1 = new ReconcileDataView(s1, s2, false);
		dv2 = new ReconcileDataView(s2, s1, false);
		
		dv1.setReconcileNotifier(this);
		dv2.setReconcileNotifier(this);
			
		// Create split pane to hold two series
		JPanel seriesHolder = new JPanel();
		seriesHolder.setLayout(new BoxLayout(seriesHolder, BoxLayout.X_AXIS));
		
		// Create panel for primary series
		JPanel reconcilePanel = new JPanel();
		reconcilePanel.setLayout(new BoxLayout(reconcilePanel, BoxLayout.Y_AXIS));
		reconcilePanel.add(createReconcilePane(s1, dv1, "Primary series: "));
		
		// Create panel for reference series
		JPanel refPanel = new JPanel();
		refPanel.setLayout(new BoxLayout(refPanel, BoxLayout.Y_AXIS));
		refPanel.add(createReconcilePane(s2, dv2, "Reference series: "));
		
		//JPanel refPanel = new JPanel(new BorderLayout());
		//refPanel.add(createReconcilePane(s2, dv2), BorderLayout.CENTER);
		//refPanel.setVisible(extraIsShown);
		
		// Graph panel
		panelChart = new JPanel();
		setupGraph();
		updateGraph(s1, s2);	
				
		// Create split pane to hold tab pane and graph pane
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);	
		splitPane.setTopComponent(seriesHolder);
		splitPane.setBottomComponent(panelChart);
		splitPane.setDividerLocation(300);
		
		// Add series panels to seriesHolder panel
		seriesHolder.add(reconcilePanel);
		seriesHolder.add(refPanel);
		
		// Add panels to main panel
		content.add(splitPane, BorderLayout.CENTER);
		content.add(dv1.getReconcileInfoPanel(), BorderLayout.WEST);
		content.add(createButtonPanel(), BorderLayout.SOUTH);

		setContentPane(content);	

		pack();
		setVisible(true);
		this.setExtendedState(this.getExtendedState() | ReconcileWindow.MAXIMIZED_BOTH);
		
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel(new FlowLayout());
		
		btnShowHideRef = new JButton(extraIsShown ? HIDE : EXPAND);
		btnFinish = new JButton("Finish");
		btnRemeasure = new JButton("Remeasure selected ring");
		btnCancel = new JButton("Cancel");
		btnViewType = new JButton("Graph");
		sepLine = new javax.swing.JSeparator();
		
		btnShowHideRef.setVisible(false);
		
		// TODO - hide until implemented
		btnViewType.setVisible(false);
		
		sepLine.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		
		// Only enable apply button immediately if there are no errors at all, otherwise disable
		if (dv1.reconciliationErrorCount()==0){
			btnFinish.setEnabled(true);
		} else {
			btnFinish.setEnabled(false);
		}
			
		// glue for our buttons...
		final ReconcileWindow glue = this;

		// Layout the buttons
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(buttonPanel);
        buttonPanel.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(btnViewType)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnShowHideRef)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnRemeasure)                
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 361, Short.MAX_VALUE)
                .add(btnFinish)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(btnCancel))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, sepLine, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(sepLine, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnShowHideRef)
                    .add(btnCancel)
                    .add(btnFinish)
                    .add(btnRemeasure)
                    .add(btnViewType))
                .addContainerGap(26, Short.MAX_VALUE))
        );			
	
		        
		// Cancel should loose any unsaved changes so reload sample from db
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {

				if(s1.isModified() || s2.isModified()) {
					int ret = JOptionPane.showConfirmDialog(glue, 
							"Are you sure you want to abandon all \nthe changes you have made?\n\n" +
							"Click 'no' to return to the reconciler\nor 'yes' to confirm.\n\n",
							"Abandon changes?", JOptionPane.YES_NO_OPTION);
						
					if (ret == JOptionPane.YES_OPTION) {
						// restore our originals
						Sample.copy(originals1, s1);
						Sample.copy(originals2, s2);

						// let anything watching them know they changed
						s1.fireSampleMetadataChanged();
						s2.fireSampleMetadataChanged();
						
						// and go away
						dispose();
					} else {
						// Return to reconciler
						return;
					}
				}
				else {
					// not modified, just go away
					dispose();
				}
			}
		});
		
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				
				// This button should save changes to both current and
				// reference measurement and if there are completely
				// reconciled, then the isreconciled flags should be set
								
				// Mark as reconciled if neither measurement has errors otherwise make sure flag is set to false
				Boolean s1rec = (Boolean) s1.getMeta("isreconciled");
				Boolean s2rec = (Boolean) s2.getMeta("isreconciled");
				if (dv1.reconciliationErrorCount() == 0 && dv2.reconciliationErrorCount() == 0){
					if(s1rec == null || s1rec.booleanValue() == false) {
						s1.setMeta("isreconciled", true);
						s1.setModified();
					}
					
					if(s2rec == null || s2rec.booleanValue() == false) {
						s2.setMeta("isreconciled", true);
						s2.setModified();					
					}
				} else {
					if(s1rec == null || s1rec.booleanValue() == true) {
						s1.setMeta("isreconciled", false);
						s1.setModified();
					}
					
					if(s2rec == null || s2rec.booleanValue() == true) {
						s2.setMeta("isreconciled", false);
						s2.setModified();					
					}
				}

				// Save the current series
				if(s1.isModified())
				{
					try {
						s1.getLoader().save(s1);
					} catch (IOException ioe) {
						Alert.error("I/O Error", "There was an error while saving the reference measurement series: \n" + ioe.getMessage());
						return;
					} catch (Exception e) {
						new Bug(e);
					}
				}

				// Now save the reference series
				if(s2.isModified())
				{
					try {
						s2.getLoader().save(s2);
					} catch (IOException ioe) {
						Alert.error("I/O Error", "There was an error while saving the current measurement series: \n" + ioe.getMessage());
						return;
					} catch (Exception e) {
						new Bug(e);
					}
				}

				// Warn user if there are errors remaining.
				int nErrors;
				if ((nErrors = dv1.reconciliationErrorCount()) > 0) {
					Alert.message("Reconcilitation Error", "There "
							+ (nErrors > 1 ? "are" : "is") + " still "
							+ nErrors + " error" + (nErrors > 1 ? "s" : "") 
							+ " remaining.\nPlease remember to fix this later!");
				}
				
				// set the necessary bits...
				s1.clearModified();
				s2.clearModified();
				
				// and close!
				close();								
			
			}
		});
		
		btnRemeasure.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int col = dv1.myTable.getSelectedColumn();
				int row = dv1.myTable.getSelectedRow();

				Year y = ((DecadalModel) dv1.myTable.getModel()).getYear(row, col);
				
				ReconcileMeasureDialog dlg = new ReconcileMeasureDialog(glue, true, s1, s2, y);
				
				Center.center(dlg, glue);
				dlg.setVisible(true);

				// did we get a value?
				Integer value = dlg.getFinalValue();
				if(value != null) {
					// kludge in some updates!
					((DecadalModel) dv1.myTable.getModel()).setValueAt(value, row, col);
					((DecadalModel) dv2.myTable.getModel()).setValueAt(value, row, col);
					
					dv1.myTable.setColumnSelectionInterval(col, col);
					dv1.myTable.setRowSelectionInterval(row, row);
					dv2.myTable.setColumnSelectionInterval(col, col);
					dv2.myTable.setRowSelectionInterval(row, row);
				}
			}
		});
		
		return buttonPanel;
	}
	
	
	private JPanel createReconcilePane(Sample s, ReconcileDataView dv, String titlePrefix) {
		JPanel p = new JPanel();
		
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		p.setBorder(BorderFactory.createTitledBorder(titlePrefix + s.toSimpleString()));
		
		p.add(dv);	
		return p;
	}

	public void reconcileSelectionChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.duplicateSelectionFrom(dataview);
		
		// enable if it's a valid index into both datasets!
		Year y = dv1.getSelectedYear();
		int col = dv1.myTable.getSelectedColumn();
		int idx = y.diff(s1.getStart());
		btnRemeasure.setEnabled(col > 1 && col < 11 && idx >= 0 && idx < s1.getData().size() && idx < s2.getData().size());
	}

	public void reconcileDataChanged(ReconcileDataView dataview) {
		ReconcileDataView dv;
		
		// we want to notify the other reconciler
		if(dataview == dv1)
			dv = dv2;
		else
			dv = dv1;
		
		dv.forceReconciliation();
		
		// enable our apply button?
		if(s1.isModified() || s2.isModified()) btnFinish.setEnabled(true);
		
		updateGraph(s1, s2);
		
	}
	
	
	// SaveableDocument
	public String getDocumentTitle() {
		return "reference sample " + s2.getDisplayTitle();
	}

	public String getFilename() {
		return null;
	}

	public Object getSavedDocument() {
		return null;
	}

	public boolean isNameChangeable() {
		return false;
	}

	public boolean isSaved() {
		return !s2.isModified();
	}

	public void save() {
		// now, actually try and save the sample
		try {
			s2.getLoader().save(s2);
		} catch (IOException ioe) {
			Alert.error("I/O Error", "There was an error while saving the sample: \n" + ioe.getMessage());
			return;
		} catch (Exception e) {
			new Bug(e);
		}

		// set the necessary bits...
		s2.clearModified();
	}
	
	public void setFilename(String fn) {
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
    	graphToolbar.setOrientation(1);
    	panelChart.add(graphToolbar, BorderLayout.WEST);
    	
    	// listen to this graph
    	//graph.addGrapherListener(this);
    	
		// get our basic graph set up
		updateGraph(null, null);
	 
    }
    
    private void updateGraph(Sample s1, Sample s2) {    	
   		graphSamples.clear();
   		
   		ArrayList<Graph> myGraphs = new ArrayList<Graph>();
   		
   		if(!(s1 == null || s2 == null)){ 				
			myGraphs.add(new Graph(s1));
			myGraphs.add(new Graph(s2));  
   		}
    	
		if(myGraphs.size() == 2) {
    		// copy the graphs over
    		graphSamples.add(myGraphs.get(0));
    		graphSamples.add(myGraphs.get(1));
    		
    		// make sure we can't drag our graphs
    		myGraphs.get(0).setDraggable(false);
    		myGraphs.get(1).setDraggable(false);
    		
    		// fit the height of the graph
        	graphController.scaleToFitHeight(5);
    	}
    	
		graphInfo.setShowVertAxis(true);
    	graph.update(true);
    }
    
    
}
