package edu.cornell.dendro.corina.cross;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import edu.cornell.dendro.corina.graph.Graph;
import edu.cornell.dendro.corina.graph.GraphController;
import edu.cornell.dendro.corina.graph.GraphInfo;
import edu.cornell.dendro.corina.graph.GrapherPanel;
import edu.cornell.dendro.corina.graph.PlotAgents;
import edu.cornell.dendro.corina.gui.DBBrowser;
import edu.cornell.dendro.corina.gui.ReverseScrollBar;
import edu.cornell.dendro.corina.index.DecimalRenderer;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.sample.SampleSummary;
/*
 * CrossDatingWizard.java
 *
 * Created on June 11, 2008, 10:35 AM
 */
import edu.cornell.dendro.corina.util.Center;

/**
 *
 * @author  peterbrewer
 */
public class CrossdateDialog extends javax.swing.JDialog {
	private ElementList crossdatingElements;
	private CrossdateCollection crossdates;
	
	private SigScoresTableModel sigScoresModel;
	private AllScoresTableModel allScoresModel;
	private HistogramTableModel histogramModel;

	private GrapherPanel graph;
	private GraphController graphController;
	private List<Graph> graphSamples;
	private JScrollPane graphScroller;
    
    /** Creates new form CrossDatingWizard */
    public CrossdateDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
     
        initialize(parent, null);
    }

    public CrossdateDialog(java.awt.Frame parent, boolean modal, ElementList preexistingElements) {
        super(parent, modal);
        initComponents();
        
        initialize(parent, preexistingElements);
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

    /**
     * Show an open dialog as a child of another dialog
     * @param parent
     * @param preexistingElements
     */
    private ElementList showOpenDialog(Dialog parent, ElementList preexistingElements) {
    	DBBrowser dbb = new DBBrowser(parent, true, true) {
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
				
				SampleSummary ss = (SampleSummary) bs.getMeta("::summary");

				if(ss != null) {
					dbb.selectSiteByCode(ss.getSiteCode());
				}
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

    private void initialize(Frame parent, ElementList preElements) {
    	// let user choose crossdates, exit if they close quietly
    	if((crossdatingElements = showOpenDialog(parent, true, preElements)) == null) {
    		dispose();
    	}
    	
    	// start our new crossdates
    	crossdates = new CrossdateCollection();
    	crossdatingElements = crossdates.setElements(crossdatingElements);   	
     	
    	setupTables();
    	setupGraph();
    	setupListeners();
    	setupLists();

    	Center.center(this);
    	setVisible(true);
    }
    
    private void setupTables() {
    	// all windows need a vertical scroll bar!
    	jScrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	jScrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    	jScrollPane3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    	// sig scores table
       	sigScoresModel = new SigScoresTableModel(tableSignificantScores);
    	tableSignificantScores.setModel(sigScoresModel);
    	sigScoresModel.applyFormatting();
    	
    	tableSignificantScores.getTableHeader().setReorderingAllowed(false);    	
    	tableSignificantScores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tableSignificantScores.setRowSelectionAllowed(true);
    	tableSignificantScores.setColumnSelectionAllowed(false);
    	
    	// all scores table
    	allScoresModel = new AllScoresTableModel(tableAllScores);
    	tableAllScores.setModel(allScoresModel);
    	allScoresModel.applyFormatting();
    	
    	tableAllScores.getTableHeader().setReorderingAllowed(false);
    	tableAllScores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tableAllScores.setCellSelectionEnabled(true);
    	
    	// histogram table
    	histogramModel = new HistogramTableModel(tableHistogram);
    	tableHistogram.setModel(histogramModel);
    	histogramModel.applyFormatting();
    	
    	tableHistogram.getTableHeader().setReorderingAllowed(false);    	
    	tableHistogram.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	tableHistogram.setRowSelectionAllowed(true);
    	tableHistogram.setColumnSelectionAllowed(false);
    }
    
    private void setupListeners() {    	
    	// whenever one of our combo boxes change...
    	ActionListener listChanged = new ActionListener() {
    		public void actionPerformed(ActionEvent ae) {
    			int row = cboPrimary.getSelectedIndex();
    			int col = cboSecondary.getSelectedIndex();
    			
    			try {
    				CrossdateCollection.Pairing pairing = crossdates.getPairing(row, col);
    				sigScoresModel.setCrossdates(pairing);
    				allScoresModel.setCrossdates(pairing);
    				histogramModel.setCrossdates(pairing);
    			} catch (CrossdateCollection.NoSuchPairingException nspe) {
    				sigScoresModel.clearCrossdates();
    				allScoresModel.clearCrossdates();
    				histogramModel.clearCrossdates();
    			}
    		}
    	};
    	
    	cboPrimary.addActionListener(listChanged);
    	cboSecondary.addActionListener(listChanged);
    	
    	// now, when our table row changes
    	tableSignificantScores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting())
					return;
				
				// make the graph reflect the row we selected!
				updateGraph(sigScoresModel.getGraphForRow(tableSignificantScores.getSelectedRow()));
			}
    	});
    	
    	// ok, now our all scores table. More complicated, because it can change both col & row selection.
    	ListSelectionListener allScoresSelectionListener = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent lse) {
				if(lse.getValueIsAdjusting())
					return;
				
				// just like before...
				updateGraph(allScoresModel.getGraphForCell(tableAllScores.getSelectedRow(), 
						tableAllScores.getSelectedColumn()));
			}
    	};
    	tableAllScores.getSelectionModel().addListSelectionListener(allScoresSelectionListener);
    	tableAllScores.getColumnModel().getSelectionModel().addListSelectionListener(allScoresSelectionListener);
    	
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

    }
    
    private void setupGraph() {
		// initialize our plotting agents
		PlotAgents agents = new PlotAgents();
		
		// create a new graphinfo structure, so we can tailor it to our needs.
		GraphInfo gInfo = new GraphInfo();
		
		// force no drawing of graph names and drawing of vertical axis
		gInfo.overrideDrawGraphNames(false);
		gInfo.overrideShowVertAxis(true);
		
		// set up our samples
		graphSamples = new ArrayList<Graph>(2);
		
		// *grumble* we need a graph for GrapherPanel to init...
		try {
			Sample s = crossdatingElements.get(0).load();
			graphSamples.add(new Graph(s));
		} catch (Exception e) {
			// shouldn't happen at all.
			return;
		}
		
		// create a graph panel; put it in a scroll pane
		graph = new GrapherPanel(graphSamples, agents, null, gInfo);
		graph.setUseVerticalScrollbar(true);
		
		graphScroller = new JScrollPane(graph,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphScroller.setVerticalScrollBar(new ReverseScrollBar());

		graphController = new GraphController(graph, graphScroller);

		// get our JLabel set up
		updateGraph(null);
		
		panelChart.setLayout(new BorderLayout());
		panelChart.add(graphScroller, BorderLayout.CENTER);
    }
    
    private void setupLists() {
    	ArrayList<Object> samples = new ArrayList<Object>();

    	// make a list of samples... (so we can get range, etc)
    	for(Element e : crossdatingElements.toActiveList()) {
    		try {
    			Sample s = e.load();
    			samples.add(s);
    		} catch (Exception ex) {
    			samples.add(e);
    		}
    	}
    	
    	cboPrimary.setModel(new DefaultComboBoxModel(samples.toArray()));
    	cboSecondary.setModel(new DefaultComboBoxModel(samples.toArray()));
    	
    	cboPrimary.setSelectedIndex(0);
    	cboSecondary.setSelectedIndex(1);
    	
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
    	if(newGraphs == null || newGraphs.size() != 2) {
    		JLabel invalid = new JLabel("Choose a valid crossdate");
    		invalid.setAlignmentX(CENTER_ALIGNMENT);
    		invalid.setHorizontalAlignment(SwingConstants.CENTER);

    		graphScroller.setRowHeader(null);
    		graphScroller.setViewportView(invalid);
    		return;
    	}

    	// copy the graphs over
    	if(graphSamples.size() == 1) {
    		graphSamples.set(0, newGraphs.get(0));
    		graphSamples.add(newGraphs.get(1));
    	}
    	else {
    		graphSamples.set(0, newGraphs.get(0));
    		graphSamples.set(1, newGraphs.get(1));
    	}
    	
    	graph.update(false);
    	graphController.scaleToFitHeight(); // calls graph.update(true) for us
    	graphScroller.setViewportView(graph);
		graph.setAxisVisible(true, true);
		panelChart.revalidate();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelMeasurements = new javax.swing.JPanel();
        lblPrimary = new javax.swing.JLabel();
        cboPrimary = new javax.swing.JComboBox();
        lblSecondary = new javax.swing.JLabel();
        cboSecondary = new javax.swing.JComboBox();
        btnAddMeasurement = new javax.swing.JButton();
        panelSwap = new javax.swing.JPanel();
        btnSwap = new javax.swing.JButton();
        btnResetMeasurements = new javax.swing.JButton();
        panelCrossdates = new javax.swing.JPanel();
        splitCrossDates = new javax.swing.JSplitPane();
        tabpanelStats = new javax.swing.JTabbedPane();
        panelSignificantScores = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSignificantScores = new javax.swing.JTable();
        panelAllScores = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAllScores = new javax.swing.JTable();
        lblDisplayStats = new javax.swing.JLabel();
        cboDisplayStats = new javax.swing.JComboBox();
        panelHistogram = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableHistogram = new javax.swing.JTable();
        lblDisplayHistogram = new javax.swing.JLabel();
        cboDisplayHistogram = new javax.swing.JComboBox();
        panelChart = new javax.swing.JPanel();
        panelButtons = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        seperatorButtons = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panelMeasurements.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Measurements"));

        lblPrimary.setLabelFor(cboPrimary);
        lblPrimary.setText("Primary:");

        cboPrimary.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "C-ABC-1-4-1", "C-ABC-1-4-2" }));
        cboPrimary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboPrimaryActionPerformed(evt);
            }
        });

        lblSecondary.setLabelFor(cboSecondary);
        lblSecondary.setText("Secondary:");

        cboSecondary.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "C-ABC-1-4-2", "C-ABC-1-4-1" }));
        cboSecondary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboSecondaryActionPerformed(evt);
            }
        });

        btnAddMeasurement.setText("Add ");

        btnSwap.setText("Swap");

        org.jdesktop.layout.GroupLayout panelSwapLayout = new org.jdesktop.layout.GroupLayout(panelSwap);
        panelSwap.setLayout(panelSwapLayout);
        panelSwapLayout.setHorizontalGroup(
            panelSwapLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(btnSwap)
        );
        panelSwapLayout.setVerticalGroup(
            panelSwapLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelSwapLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .add(btnSwap)
                .addContainerGap())
        );

        btnResetMeasurements.setText("Reset");

        org.jdesktop.layout.GroupLayout panelMeasurementsLayout = new org.jdesktop.layout.GroupLayout(panelMeasurements);
        panelMeasurements.setLayout(panelMeasurementsLayout);
        panelMeasurementsLayout.setHorizontalGroup(
            panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelMeasurementsLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(lblPrimary, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(lblSecondary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 97, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(11, 11, 11)
                .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(panelMeasurementsLayout.createSequentialGroup()
                        .add(btnAddMeasurement)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btnResetMeasurements))
                    .add(panelMeasurementsLayout.createSequentialGroup()
                        .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cboSecondary, 0, 482, Short.MAX_VALUE)
                            .add(cboPrimary, 0, 482, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelSwap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        panelMeasurementsLayout.setVerticalGroup(
            panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelMeasurementsLayout.createSequentialGroup()
                .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelSwap, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(panelMeasurementsLayout.createSequentialGroup()
                        .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(lblPrimary)
                            .add(cboPrimary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(panelMeasurementsLayout.createSequentialGroup()
                                .add(lblSecondary)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 13, Short.MAX_VALUE))
                            .add(cboSecondary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(18, 18, 18)
                .add(panelMeasurementsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btnAddMeasurement)
                    .add(btnResetMeasurements))
                .addContainerGap())
        );

        panelCrossdates.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cross dates"));

        splitCrossDates.setBorder(null);
        splitCrossDates.setDividerLocation(230);
        splitCrossDates.setDividerSize(12);
        splitCrossDates.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        tableSignificantScores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "#", "Position", "Overlap", "Trend", "T Score", "R Score", "D Score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSignificantScores.setColumnSelectionAllowed(true);
        jScrollPane1.setViewportView(tableSignificantScores);
        tableSignificantScores.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        org.jdesktop.layout.GroupLayout panelSignificantScoresLayout = new org.jdesktop.layout.GroupLayout(panelSignificantScores);
        panelSignificantScores.setLayout(panelSignificantScoresLayout);
        panelSignificantScoresLayout.setHorizontalGroup(
            panelSignificantScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSignificantScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelSignificantScoresLayout.setVerticalGroup(
            panelSignificantScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelSignificantScoresLayout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabpanelStats.addTab("Significant Scores", panelSignificantScores);

        tableAllScores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1001", null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Year", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "#"
            }
        ));
        tableAllScores.setGridColor(new java.awt.Color(204, 204, 204));
        jScrollPane2.setViewportView(tableAllScores);

        lblDisplayStats.setText("Display values for:");

        cboDisplayStats.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R Score", "T Score", "D Score", "Trend" }));

        org.jdesktop.layout.GroupLayout panelAllScoresLayout = new org.jdesktop.layout.GroupLayout(panelAllScores);
        panelAllScores.setLayout(panelAllScoresLayout);
        panelAllScoresLayout.setHorizontalGroup(
            panelAllScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAllScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelAllScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                    .add(panelAllScoresLayout.createSequentialGroup()
                        .add(lblDisplayStats)
                        .add(18, 18, 18)
                        .add(cboDisplayStats, 0, 514, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelAllScoresLayout.setVerticalGroup(
            panelAllScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelAllScoresLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelAllScoresLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDisplayStats)
                    .add(cboDisplayStats, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabpanelStats.addTab("All Scores", panelAllScores);

        tableHistogram.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Stats Value", "#", "Histogram"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(tableHistogram);

        lblDisplayHistogram.setText("Display histogram for:");

        cboDisplayHistogram.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "R Score", "T Score", "D Score", "Trend" }));

        org.jdesktop.layout.GroupLayout panelHistogramLayout = new org.jdesktop.layout.GroupLayout(panelHistogram);
        panelHistogram.setLayout(panelHistogramLayout);
        panelHistogramLayout.setHorizontalGroup(
            panelHistogramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelHistogramLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelHistogramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
                    .add(panelHistogramLayout.createSequentialGroup()
                        .add(lblDisplayHistogram)
                        .add(18, 18, 18)
                        .add(cboDisplayHistogram, 0, 490, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelHistogramLayout.setVerticalGroup(
            panelHistogramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelHistogramLayout.createSequentialGroup()
                .addContainerGap()
                .add(panelHistogramLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDisplayHistogram)
                    .add(cboDisplayHistogram, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabpanelStats.addTab("Histogram", panelHistogram);

        splitCrossDates.setTopComponent(tabpanelStats);

        org.jdesktop.layout.GroupLayout panelChartLayout = new org.jdesktop.layout.GroupLayout(panelChart);
        panelChart.setLayout(panelChartLayout);
        panelChartLayout.setHorizontalGroup(
            panelChartLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 710, Short.MAX_VALUE)
        );
        panelChartLayout.setVerticalGroup(
            panelChartLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 245, Short.MAX_VALUE)
        );

        splitCrossDates.setRightComponent(panelChart);

        org.jdesktop.layout.GroupLayout panelCrossdatesLayout = new org.jdesktop.layout.GroupLayout(panelCrossdates);
        panelCrossdates.setLayout(panelCrossdatesLayout);
        panelCrossdatesLayout.setHorizontalGroup(
            panelCrossdatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(splitCrossDates, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
        );
        panelCrossdatesLayout.setVerticalGroup(
            panelCrossdatesLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(splitCrossDates, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
        );

        btnOK.setText("OK");

        seperatorButtons.setBackground(new java.awt.Color(153, 153, 153));
        seperatorButtons.setOpaque(true);

        org.jdesktop.layout.GroupLayout panelButtonsLayout = new org.jdesktop.layout.GroupLayout(panelButtons);
        panelButtons.setLayout(panelButtonsLayout);
        panelButtonsLayout.setHorizontalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, panelButtonsLayout.createSequentialGroup()
                .addContainerGap(671, Short.MAX_VALUE)
                .add(btnOK)
                .add(16, 16, 16))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, seperatorButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
        );
        panelButtonsLayout.setVerticalGroup(
            panelButtonsLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtonsLayout.createSequentialGroup()
                .add(seperatorButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnOK)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(panelButtons, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelCrossdates, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, panelMeasurements, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(panelMeasurements, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelCrossdates, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(panelButtons, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboPrimaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboPrimaryActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboPrimaryActionPerformed

    private void cboSecondaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboSecondaryActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cboSecondaryActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMeasurement;
    private javax.swing.JButton btnOK;
    private javax.swing.JButton btnResetMeasurements;
    private javax.swing.JButton btnSwap;
    private javax.swing.JComboBox cboDisplayHistogram;
    private javax.swing.JComboBox cboDisplayStats;
    private javax.swing.JComboBox cboPrimary;
    private javax.swing.JComboBox cboSecondary;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblDisplayHistogram;
    private javax.swing.JLabel lblDisplayStats;
    private javax.swing.JLabel lblPrimary;
    private javax.swing.JLabel lblSecondary;
    private javax.swing.JPanel panelAllScores;
    private javax.swing.JPanel panelButtons;
    private javax.swing.JPanel panelChart;
    private javax.swing.JPanel panelCrossdates;
    private javax.swing.JPanel panelHistogram;
    private javax.swing.JPanel panelMeasurements;
    private javax.swing.JPanel panelSignificantScores;
    private javax.swing.JPanel panelSwap;
    private javax.swing.JSeparator seperatorButtons;
    private javax.swing.JSplitPane splitCrossDates;
    private javax.swing.JTable tableAllScores;
    private javax.swing.JTable tableHistogram;
    private javax.swing.JTable tableSignificantScores;
    private javax.swing.JTabbedPane tabpanelStats;
    // End of variables declaration//GEN-END:variables
    
}
