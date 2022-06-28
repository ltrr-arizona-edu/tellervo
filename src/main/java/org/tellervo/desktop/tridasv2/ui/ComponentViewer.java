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
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.collections15.Transformer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnModelExt;
import org.tellervo.desktop.gui.dbbrowse.BooleanCellRenderer;
import org.tellervo.desktop.gui.dbbrowse.ElementListCellRenderer;
import org.tellervo.desktop.gui.dbbrowse.ElementListManager;
import org.tellervo.desktop.gui.dbbrowse.ElementListPopupMenu;
import org.tellervo.desktop.gui.dbbrowse.ElementListTableModel;
import org.tellervo.desktop.gui.dbbrowse.ElementListTableSorter;
import org.tellervo.desktop.sample.CachedElement;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.TellervoWSILoader;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.wsi.ResourceEvent;
import org.tellervo.desktop.wsi.ResourceEventListener;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesResource;
import org.tellervo.schema.EntityType;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tridas.schema.TridasIdentifier;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.ObservableGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.event.GraphEventListener;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;


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
    private JLabel lblViewAllThe;
	
	
	public ComponentViewer(Sample sample) {
		this.sample = sample;
		this.loadedComprehensive = false;
		
		initComponents();
		updateContent();
		
		// default to this view...
		btnTreeView.doClick();

	}
	
	private void initComponents() {
		
		setLayout(new BorderLayout());
		
		// create button panel
		JPanel topPanel = new JPanel();
		btnTreeView = new JRadioButton("Tree");
		btnTreeView.putClientProperty("cv.cardName", TREEPANEL);
		btnTableView = new JRadioButton("Table");
		btnTableView.putClientProperty("cv.cardName", TABLEPANEL);
		
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
		
		// connect buttons
		ButtonGroup group = new ButtonGroup();
		group.add(btnTreeView);
		group.add(btnTableView);
		topPanel.setLayout(new MigLayout("", "[64px][55px][62px][grow]", "[][23px]"));
		
		lblViewAllThe = new JLabel("<html>View all the component series that combine to create the current series. Series can be viewed as:");
		topPanel.add(lblViewAllThe, "cell 0 0 4 1");
		JRadioButton btnTree2View = new JRadioButton("Flow chart");
		btnTree2View.putClientProperty("cv.cardName", TREE2PANEL);
		btnTree2View.addActionListener(btnListener);
		group.add(btnTree2View);
		topPanel.add(btnTree2View, "cell 0 1,alignx left,aligny center");
		topPanel.add(btnTreeView, "cell 1 1,alignx left,aligny center");
		topPanel.add(btnTableView, "cell 2 1,alignx left,aligny center");
		
		
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
		PopulateComponentsTask task = new PopulateComponentsTask(sample);
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
	        layout = new FRLayout<String,String>(g);
	        //layout = new ISOMLayout<String, String>(g);
	        //layout = new KKLayout<String, String>(g);
	        //layout = new CircleLayout<String, String>(g);

	   
	        vv = new VisualizationViewer<String,String>(layout, null);

	        
	        //tree2Panel.putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);

	        tree2Panel.setLayout(new BorderLayout());
	        tree2Panel.setBackground(java.awt.Color.lightGray);
	        tree2Panel.setFont(new Font("Serif", Font.PLAIN, 12));

	        vv.getModel().getRelaxer().setSleepTime(500);
	        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse<Number,Number>();

	        vv.setGraphMouse(graphMouse);

	        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
	        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
	        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
	        
	        Transformer<String,Paint> vertexColor = new Transformer<String,Paint>() {
	            public Paint transform(String i) {
	            	
	            	if(i.equals(ComponentTreeCellRenderer.getFullTitle(sample, false)))
	            	{
	            		return Color.ORANGE;
	            	}
	            	else
	            	{
	            		return Color.PINK;
	            	}
	            }
	        };
	        
	        Transformer<String,Paint> edgeColor = new Transformer<String,Paint>() {
	            public Paint transform(String i) {
	                return Color.WHITE;
	            }
	        };
	        
			Transformer<String, Shape> vertexShape = new Transformer<String, Shape>() {
				public Shape transform(String i) {
	            	
	            	if(i.equals(ComponentTreeCellRenderer.getFullTitle(sample, false)))
	            	{
	            		return new Rectangle(-150, -20, 300, 40);

	            	}
	            	else
	            	{
	            		return new Rectangle(-150, -10, 300, 20);
	            	}
				}
			};

	        vv.getRenderContext().setVertexFillPaintTransformer(vertexColor);
	        vv.getRenderContext().setEdgeDrawPaintTransformer(edgeColor);
	        vv.getRenderContext().setVertexShapeTransformer(vertexShape);;
	        
	        
	        vv.setForeground(Color.BLACK);
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

	        tree2Panel.add(controls, BorderLayout.NORTH);

	}
	
	class PopulateComponentsTask extends SwingWorker<Void, Void> {
		
		
		private Sample sample;
		private ElementList de = new ElementList();
		private DefaultMutableTreeNode rootNode;
		
		public PopulateComponentsTask(Sample sample)
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
			rootNode = new DefaultMutableTreeNode(new CachedElement(sample));
			

			recurseAddElementsToList(el, rootNode, sample, 0);
			
			return null;		
		}
		
		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {     
			tableModel.setElements(de);
			treeModel.setRoot(rootNode);
		}
		
		
		private void recurseAddElementsToList(ElementList elements,
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
					de.add(ce);
					
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

		                //Relaxer relaxer = vv.getModel().getRelaxer();
		                //relaxer.pause();
		                g.addVertex(v1);
		                System.err.println("added node " + v1);

		                // wire it to some edges
		                String verb  = "";
		                try{
			            	verb = parentSample.getSampleType().toString();
			                if(verb.equals("Sum")) verb = "Summed";
			                if(verb.equals("Index")) verb = "Indexed";
			                if(verb.equals("Clean")) verb = "Cleaned";
			                if(verb.equals("Redate")) verb = "Redated";
			                if(verb.equals("Crossdate")) verb = "Crossdated";
			                if(verb.equals("Truncate")) verb = "Truncated";
		                } catch (NullPointerException ex)
		                {
		                
		                }
		                
		                verb = verb + " ["+g.getEdgeCount()+"]";
		                
						g.addEdge(verb, 
								ComponentTreeCellRenderer.getFullTitle(s, false),
								ComponentTreeCellRenderer.getFullTitle(parentSample, false));

		                layout.initialize();
		                //relaxer.resume();
		                layout.lock(false);

						ElementList sampleElements = s.getElements();
												
						if(sampleElements != null)
							recurseAddElementsToList(sampleElements, node, s, depth + 1);
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
