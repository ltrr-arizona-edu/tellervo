/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
/**
 * 
 */
package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.tellervo.desktop.gui.dbbrowse.BooleanCellRenderer;
import org.tellervo.desktop.gui.dbbrowse.ElementListCellRenderer;
import org.tellervo.desktop.gui.dbbrowse.ElementListManager;
import org.tellervo.desktop.gui.dbbrowse.ElementListPopupMenu;
import org.tellervo.desktop.gui.dbbrowse.ElementListTableModel;
import org.tellervo.desktop.gui.dbbrowse.ElementListTableSorter;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.TellervoWSILoader;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.EntityType;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tridas.schema.TridasIdentifier;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Timer;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.util.Animator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;

import java.awt.Component;

import net.miginfocom.swing.MigLayout;


/**
 * @author Lucas Madar
 *
 */
public class ComponentViewer extends JPanel implements ResourceEventListener, ElementListManager {
	private static final long serialVersionUID = 1L;
	
	/** The sample we're viewing */
	private Sample sample;
	/** True if we've loaded the comprehensive series */
	private boolean loadedComprehensive;
	
	private JRadioButton btnTableView;
	private JRadioButton btnTreeView;
	
	/** Status bar text */
	private JLabel txtStatus;
	/** Progress bar */
	private JProgressBar pbStatus;
	
	private JPanel contentPanel, tablePanel, treePanel, tree2Panel;
	
	private JXTable table;
	private JTree tree;
	
	private ElementListTableSorter tableSorter;
	private ElementListTableModel tableModel;
	private DefaultTreeModel treeModel;
	
	private final static String TABLEPANEL = "Series Table View";
	private final static String TREEPANEL = "Series Tree View";
	private final static String TREE2PANEL = "New Tree View";
	
	private Graph<String,String> g = null;
    private VisualizationViewer<String,String> vv = null;
    private AbstractLayout<String,String> layout = null;
	
	
	public ComponentViewer(Sample sample) {
		this.sample = sample;
		this.loadedComprehensive = false;
		
		initComponents();
		updateContent();
		
		// default to this view...
		btnTreeView.doClick();
	}
	
	private void initComponents() {
		JLabel label;
		
		setLayout(new BorderLayout());
		
		// create button panel
		JPanel topPanel = new JPanel();
		label = new JLabel("View as: ");
		btnTreeView = new JRadioButton("Tree");
		btnTreeView.putClientProperty("cv.cardName", TREEPANEL);
		btnTableView = new JRadioButton("Table");
		btnTableView.putClientProperty("cv.cardName", TABLEPANEL);
		JRadioButton btnTree2View = new JRadioButton("Flow chart");
		btnTree2View.putClientProperty("cv.cardName", TREE2PANEL);
		
		ActionListener btnListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// show the right layout panel
				String v = (String) ((AbstractButton)e.getSource()).getClientProperty("cv.cardName");
				if(v != null)
					((CardLayout) contentPanel.getLayout()).show(contentPanel, v);
			}
		};
		btnTableView.addActionListener(btnListener);
		btnTreeView.addActionListener(btnListener);	
		btnTree2View.addActionListener(btnListener);
		
		// connect buttons
		ButtonGroup group = new ButtonGroup();
		group.add(btnTreeView);
		group.add(btnTableView);
		group.add(btnTree2View);
		topPanel.setLayout(new MigLayout("", "[64px][55px][62px][63px][][]", "[23px]"));
		
		
		// add it all to a panel
		topPanel.add(label, "cell 0 0,alignx left,aligny center");
		topPanel.add(btnTreeView, "cell 1 0,alignx left,aligny center");
		topPanel.add(btnTableView, "cell 2 0,alignx left,aligny center");
		topPanel.add(btnTree2View, "cell 3 0,alignx left,aligny center");
		
		
		topPanel.setBorder(BorderFactory.createEmptyBorder(2, 8, 8, 8));
		
		add(topPanel, BorderLayout.NORTH);
				
		// create status bar
		JPanel status = new JPanel();
		status.setLayout(new BoxLayout(status, BoxLayout.X_AXIS));
		txtStatus = new JLabel("");
		pbStatus = new JProgressBar();
		pbStatus.setVisible(false);
		status.add(txtStatus);
		status.add(Box.createHorizontalStrut(8));
		status.add(pbStatus);
		status.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		
		add(status, BorderLayout.SOUTH);
		
		contentPanel = new JPanel(new CardLayout());
		add(contentPanel, BorderLayout.CENTER);
		
		tablePanel = new JPanel(new BorderLayout());
		setupTable();
		tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

		treePanel = new JPanel(new BorderLayout());
		setupTree();
		treePanel.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		tree2Panel = new JPanel(new BorderLayout());
		setupTreeGUI();

		contentPanel.add(tablePanel, TABLEPANEL);
		contentPanel.add(treePanel, TREEPANEL);
		contentPanel.add(tree2Panel, TREE2PANEL);

	}
	
	private void setupTree() {
		treeModel = new DefaultTreeModel(null);
		tree = new JTree(treeModel);
		
		tree.setCellRenderer(new ComponentTreeCellRenderer());
		
		// popup menu
		tree.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				TreePath path = tree.getPathForLocation(e.getX(), e.getY());
				DefaultMutableTreeNode node = (path == null) ? null :
					(DefaultMutableTreeNode) path.getLastPathComponent();
				
				if(node == null)
					return;
				
				// ensure we select the node...
				tree.setSelectionPath(path);
				
				// get the element
				Element element = (Element) node.getUserObject();
				
				// create and show the menu
				JPopupMenu popup = new ElementListPopupMenu(element, ComponentViewer.this);
				popup.show(tree, e.getX(), e.getY());
			}
		});
		ToolTipManager.sharedInstance().setInitialDelay(0);
		
		tree.setToolTipText("");
		
		
	}
	
	private void setupTable() {
		tableModel = new ElementListTableModel();
		table = new JXTable(tableModel);
		
		tableSorter = new ElementListTableSorter(tableModel, table);
		table.getTableHeader().addMouseListener(tableSorter); // add sorter & header renderer
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
    	// set our column widths
    	ElementListTableModel.setupColumnWidths(table);
		
    	table.setDefaultRenderer(Object.class, new ElementListCellRenderer(this, false));
		table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer(this, false));
    	
    	// hide irrelevent columns
    	TableColumnModelExt colmodel = (TableColumnModelExt)table.getColumnModel();
    	table.setColumnControlVisible(true);
    	colmodel.getColumnExt(I18n.getText("hidden.MostRecentVersion")).setVisible(false);
    	colmodel.getColumnExt(I18n.getText("dbbrowser.n")).setVisible(false);
    	colmodel.getColumnExt(I18n.getText("dbbrowser.rec")).setVisible(false);
    	colmodel.getColumnExt(I18n.getText("dbbrowser.hash")).setVisible(false);
				

		
		// popup menu
		table.addMouseListener(new PopupListener() {
			@Override
			public void showPopup(MouseEvent e) {
				// only clicks on tables
				if(!(e.getSource() instanceof JTable))
					return;
				
				JTable table = (JTable) e.getSource();
				ElementListTableModel model = (ElementListTableModel) table.getModel();
				
				// get the row and sanity check
				int row = table.rowAtPoint(e.getPoint());
				if(row < 0 || row >= model.getRowCount())
					return;
				
				// select it?
				table.setRowSelectionInterval(row, row);
				
				// get the element
				Element element = model.getElementAt(row);
				
				// create and show the menu
				JPopupMenu popup = new ElementListPopupMenu(element, ComponentViewer.this);
				popup.show(table, e.getX(), e.getY());
			}
		});
	}
	

	
	private void updateContent() {
		/*ElementList elements = sample.getElements();
		ElementList displayElements = new ElementList();
		
		// if we don't have any, use an empty list instead...
		if(elements == null)
			elements = new ElementList();
		
		// create root node
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new CachedElement(sample));
		

		//recurseAddElementsToList(elements, displayElements, rootNode, sample, 0);
		*/
		Task task = new Task(sample);
		task.execute();	
		
		
		//displayElements = task.getDisplayElements();

		// should be unsorted - default order is what we want
		//tableModel.setElements(displayElements);
		//treeModel.setRoot(rootNode);
		
		// expand all nodes in tree
		// note: tree.getRowCount() changes as we expand each node!
		/*for(int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}*/
		

	}
	
	private void setStatus(String status, boolean inProgress) {
		txtStatus.setText(status);
		pbStatus.setVisible(inProgress);
		pbStatus.setIndeterminate(inProgress);
	}
	
	/**
	 * Called when someone has made this panel visible
	 */
	public void notifyPanelVisible() {
		if(loadedComprehensive)
			return;
		
		loadedComprehensive = true;
		
		if(sample.getLoader() instanceof TellervoWSILoader) {
			TridasIdentifier identifier = ((TellervoWSILoader) sample.getLoader()).getTridasIdentifier();
			// create a new resource
			SeriesResource resource = new SeriesResource(identifier, EntityType.MEASUREMENT_SERIES, TellervoRequestType.READ);

			// flag it as comprehensive
			resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.COMPREHENSIVE);
			resource.setOwnerWindow(SwingUtilities.getWindowAncestor(this));
			
			resource.addResourceEventListener(this);
			resource.query();
			
			setStatus("Loading comprehensive series...", true);
			
			return;
		}
		
		setStatus("Cannot load comprehensive view for this series.", false);
	}

	/**
	 * Called from loading a resource
	 */
	public void resourceChanged(ResourceEvent re) {
		switch(re.getEventType()) {
		case ResourceEvent.RESOURCE_DEBUG_OUT:
			setStatus("Loading components please wait...", true);
			break;
			
		case ResourceEvent.RESOURCE_DEBUG_IN:
			setStatus("Receiving reply...", true);
			break;
			
		case ResourceEvent.RESOURCE_QUERY_COMPLETE: {
			setStatus("Done", false);
			
			SeriesResource resource = (SeriesResource) re.getSource();
			Sample s = resource.getSample(sample.getIdentifier());
			
			if(s == null) {
				setStatus("Error: Series was not found.", false);
				return;
			}
			
			sample = s;
			updateContent();
			break;
		}
			
		case ResourceEvent.RESOURCE_QUERY_FAILED:
			setStatus("Error loading view: " + re.getAttachedException().getLocalizedMessage(), false);
			break;
		}
	}

	public void deleteElement(Element e) {
		// this can never happen; all of our elements have dependents.
	}

	public boolean isElementDisabled(Element e) {
		// none of our elements are ever disabled
		return false;
	}
	
	
	private void setupTreeGUI()
	{
	       //create a graph
	    	Graph<String, String> ig = Graphs.<String,String>synchronizedDirectedGraph(new DirectedSparseMultigraph<String,String>());

	        ObservableGraph<String,String> og = new ObservableGraph<String,String>(ig);
	        og.addGraphEventListener(new GraphEventListener<String,String>() {

				public void handleGraphEvent(GraphEvent<String, String> evt) {
					System.err.println("got "+evt);

				}});
	        this.g = og;
	        //create a graphdraw
	        layout = new FRLayout2<String,String>(g);
//	        ((FRLayout)layout).setMaxIterations(200);

	        vv = new VisualizationViewer<String,String>(layout, new Dimension(600,600));

	        
	        tree2Panel.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

	        tree2Panel.setLayout(new BorderLayout());
	        tree2Panel.setBackground(java.awt.Color.lightGray);
	        tree2Panel.setFont(new Font("Serif", Font.PLAIN, 12));

	        vv.getModel().getRelaxer().setSleepTime(500);
	        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse<Number,Number>();

	        vv.setGraphMouse(graphMouse);

	        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
	        //vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Number>());
	        vv.setForeground(Color.white);
	        tree2Panel.add(vv, BorderLayout.CENTER);
	        

	        
	        JComboBox modeBox = graphMouse.getModeComboBox();
	        
	        modeBox.setRenderer(new JUNGModeRenderer());
	        
	        modeBox.addItemListener(graphMouse.getModeListener());
	        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

	        final ScalingControl scaler = new CrossoverScalingControl();

	        JButton plus = new JButton("+");
	        plus.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                scaler.scale(vv, 1.1f, vv.getCenter());
	            }
	        });
	        JButton minus = new JButton("-");
	        minus.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                scaler.scale(vv, 1/1.1f, vv.getCenter());
	            }
	        });

	        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
	        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

	        JPanel controls = new JPanel();
	        scaleGrid.add(plus);
	        scaleGrid.add(minus);
	        controls.add(scaleGrid);
	        controls.add(modeBox);

	        tree2Panel.add(controls, BorderLayout.SOUTH);

	}
	
	class Task extends SwingWorker<Void, Void> {
		
		
		private Sample sample;
		private ElementList de = new ElementList();
		
		public Task(Sample sample)
		{
			this.sample = sample;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			
			ElementList el = sample.getElements();
			ElementList de = new ElementList();
			
			// if we don't have any, use an empty list instead...
			if(el == null)
				el = new ElementList();
			
			// create root node
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new CachedElement(sample));
			

			recurseAddElementsToList(el, de, rootNode, sample, 0);
			
			return null;		
		}
		
		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {     
			tableModel.setElements(de);
			treeModel.setRoot(new DefaultMutableTreeNode(new CachedElement(sample)));
		}
		
		
		public ElementList getDisplayElements()
		{
			return de;
		}
		
		
		private void recurseAddElementsToList(ElementList elements, ElementList flat, 
				DefaultMutableTreeNode parent, Sample parentSample, int depth) {
			for(Element e : elements) {
				if(e instanceof CachedElement) {
					CachedElement ce = (CachedElement) e;
					
					// need the basic...
					if(!ce.hasBasic()) {
						System.err.println("Cached, but not even basic loaded!");
						continue;
					}
					
					// add to list
					flat.add(ce);
					
					// add to tree
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(ce);
					parent.add(node);
								
					
					// can't go any deeper if there's no series...
					if(!ce.hasFull())
						continue;
					
					try {
						Sample s = ce.load();
						
		            	layout.lock(true);
		                //add a vertex
		                String v1 = ComponentTreeCellRenderer.getFullTitle(s, false);

		                Relaxer relaxer = vv.getModel().getRelaxer();
		                relaxer.pause();
		                g.addVertex(v1);
		                System.err.println("added node " + v1);

		                // wire it to some edges
						g.addEdge(g.getEdgeCount()+"", 
								ComponentTreeCellRenderer.getFullTitle(s, false),
								ComponentTreeCellRenderer.getFullTitle(parentSample, false));

		                layout.initialize();
		                relaxer.resume();
		                layout.lock(false);

						ElementList sampleElements = s.getElements();
												
						if(sampleElements != null)
							recurseAddElementsToList(sampleElements, flat, node, s, depth + 1);
					} catch (IOException ioe) {
						// shouldn't happen
					} 
				}
				else
					System.err.println("Non-cached element: " + e);
			}	
			
			vv.repaint();
		}
		
	}
    
  
}
