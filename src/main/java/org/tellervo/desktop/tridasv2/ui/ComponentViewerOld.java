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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
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
import org.tellervo.desktop.sample.TellervoWsiTridasElement;
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
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

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
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
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformerDecorator;
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
import edu.uci.ics.jung.visualization.util.Animator;

import java.awt.Component;

import net.miginfocom.swing.MigLayout;


/**
 * @author Lucas Madar
 *
 */
public class ComponentViewerOld extends JPanel implements ResourceEventListener, ElementListManager {
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
	
	
	   /**
     * the graph
     */
    Forest<String,Integer> graph;
    
    Factory<DirectedGraph<String,Integer>> graphFactory = 
    	new Factory<DirectedGraph<String,Integer>>() {

			public DirectedGraph<String, Integer> create() {
				return new DirectedSparseMultigraph<String,Integer>();
			}
		};
			
	Factory<Tree<String,Integer>> treeFactory =
		new Factory<Tree<String,Integer>> () {

		public Tree<String, Integer> create() {
			return new DelegateTree<String,Integer>(graphFactory);
		}
	};
	
	Factory<Integer> edgeFactory = new Factory<Integer>() {
		int i=0;
		public Integer create() {
			return i++;
		}};
    
    Factory<String> vertexFactory = new Factory<String>() {
    	int i=0;
		public String create() {
			return "V"+i++;
		}};

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<String,Integer> vv;
    
    VisualizationServer.Paintable rings;
    
    String root;
    
    TreeLayout<String,Integer> treeLayout;
    
    BalloonLayout<String, Integer> radialLayout;

	
	
	
	
	public ComponentViewerOld(Sample sample) {
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
		btnTreeView = new JRadioButton("tree");
		btnTreeView.putClientProperty("cv.cardName", TREEPANEL);
		btnTableView = new JRadioButton("table");
		btnTableView.putClientProperty("cv.cardName", TABLEPANEL);
		JRadioButton btnTree2View = new JRadioButton("tree2");
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
		
		JButton btnOrder = new JButton("Order");
		btnOrder.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				
			}
			
			
		});
		topPanel.add(btnOrder, "cell 5 0");
		
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
		
		createTree();
		//setupTreeGUI();
		
		
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
				JPopupMenu popup = new ElementListPopupMenu(element, ComponentViewerOld.this);
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
				JPopupMenu popup = new ElementListPopupMenu(element, ComponentViewerOld.this);
				popup.show(table, e.getX(), e.getY());
			}
		});
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
					
	                //Relaxer relaxer = vv.getModel().getRelaxer();
	                //relaxer.pause();
					
					graph.addEdge(edgeFactory.create(), 
							ComponentTreeCellRenderer.getFullTitle(s, false),
							ComponentTreeCellRenderer.getFullTitle(parentSample, false));
					
					ElementList sampleElements = s.getElements();
					
	                //treeLayout.initialize();
	                //radialLayout.initialize();
	                //relaxer.resume();
					
					if(sampleElements != null)
						recurseAddElementsToList(sampleElements, flat, node, s, depth + 1);
				} catch (IOException ioe) {
					// shouldn't happen
				} 
			}
			else
				System.err.println("Non-cached element: " + e);
		}
		
	}
	

	
	private void updateContent() {
		ElementList elements = sample.getElements();
		ElementList displayElements = new ElementList();
		
		// if we don't have any, use an empty list instead...
		if(elements == null)
			elements = new ElementList();
		
		// create root node
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new CachedElement(sample));
		

		recurseAddElementsToList(elements, displayElements, rootNode, sample, 0);

		// should be unsorted - default order is what we want
		tableModel.setElements(displayElements);
		treeModel.setRoot(rootNode);
		
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
		
		if(sample.getLoader() instanceof TellervoWsiTridasElement) {
			TridasIdentifier identifier = ((TellervoWsiTridasElement) sample.getLoader()).getTridasIdentifier();
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
			setupTreeGUI();
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
       
        treeLayout = new TreeLayout<String,Integer>(graph);
        radialLayout = new BalloonLayout<String,Integer>(graph);
        
        radialLayout.setSize(new Dimension(600,600));
        vv =  new VisualizationViewer<String,Integer>(treeLayout, new Dimension(600,600));
        vv.setBackground(Color.white);
        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.E);
        rings = new Rings(radialLayout);
        
        
        Container content = tree2Panel;
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        content.add(panel);
        
        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

        vv.setGraphMouse(graphMouse);
        
        JComboBox modeBox = graphMouse.getModeComboBox();
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
        
        JToggleButton radial = new JToggleButton("Radial");
        radial.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					
					LayoutTransition<String,Integer> lt =
						new LayoutTransition<String,Integer>(vv, treeLayout, radialLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.addPreRenderPaintable(rings);
				} else {
					LayoutTransition<String,Integer> lt =
						new LayoutTransition<String,Integer>(vv, radialLayout, treeLayout);
					Animator animator = new Animator(lt);
					animator.start();
					vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
					vv.removePreRenderPaintable(rings);
				}
				vv.repaint();
			}});

        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

        JPanel controls = new JPanel();
        scaleGrid.add(plus);
        scaleGrid.add(minus);
        controls.add(radial);
        controls.add(scaleGrid);
        controls.add(modeBox);

        content.add(controls, BorderLayout.SOUTH);
	
	}
	
	  /**
     * 
     */
    private void createTree() {
    	
        // create a simple graph for the demo
        graph = new DelegateForest<String,Integer>();
    	graph.addVertex(ComponentTreeCellRenderer.getFullTitle(sample, false));
    	
    	/*graph.addEdge(edgeFactory.create(), "V0", "V1");
    	graph.addEdge(edgeFactory.create(), "V0", "V2");
    	graph.addEdge(edgeFactory.create(), "V1", "V4");
    	graph.addEdge(edgeFactory.create(), "V2", "V3");
    	graph.addEdge(edgeFactory.create(), "V2", "V5");
    	graph.addEdge(edgeFactory.create(), "V4", "V6");
    	graph.addEdge(edgeFactory.create(), "V4", "V7");
    	graph.addEdge(edgeFactory.create(), "V3", "V8");
    	graph.addEdge(edgeFactory.create(), "V6", "V9");
    	graph.addEdge(edgeFactory.create(), "V4", "V10");
    	
       	graph.addVertex("A0");
       	graph.addEdge(edgeFactory.create(), "A0", "A1");
       	graph.addEdge(edgeFactory.create(), "A0", "A2");
       	graph.addEdge(edgeFactory.create(), "A0", "A3");
       	
       	graph.addVertex("B0");
    	graph.addEdge(edgeFactory.create(), "B0", "B1");
    	graph.addEdge(edgeFactory.create(), "B0", "B2");
    	graph.addEdge(edgeFactory.create(), "B1", "B4");
    	graph.addEdge(edgeFactory.create(), "B2", "B3");
    	graph.addEdge(edgeFactory.create(), "B2", "B5");
    	graph.addEdge(edgeFactory.create(), "B4", "B6");
    	graph.addEdge(edgeFactory.create(), "B4", "B7");
    	graph.addEdge(edgeFactory.create(), "B3", "B8");
    	graph.addEdge(edgeFactory.create(), "B6", "B9");*/
       	
    }
    
    
    class Rings implements VisualizationServer.Paintable {
    	
    	BalloonLayout<String,Integer> layout;
    	
    	public Rings(BalloonLayout<String,Integer> layout) {
    		this.layout = layout;
    	}
    	
		public void paint(Graphics g) {
			g.setColor(Color.gray);
		
			Graphics2D g2d = (Graphics2D)g;

			Ellipse2D ellipse = new Ellipse2D.Double();
			for(String v : layout.getGraph().getVertices()) {
				Double radius = layout.getRadii().get(v);
				if(radius == null) continue;
				Point2D p = layout.transform(v);
				ellipse.setFrame(-radius, -radius, 2*radius, 2*radius);
				AffineTransform at = AffineTransform.getTranslateInstance(p.getX(), p.getY());
				Shape shape = at.createTransformedShape(ellipse);
				
				MutableTransformer viewTransformer =
					vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
				
				if(viewTransformer instanceof MutableTransformerDecorator) {
					shape = vv.getRenderContext().getMultiLayerTransformer().transform(shape);
				} else {
					shape = vv.getRenderContext().getMultiLayerTransformer().transform(Layer.LAYOUT,shape);
				}

				g2d.draw(shape);
			}
		}

		public boolean useTransform() {
			return true;
		}
    }
    
}
