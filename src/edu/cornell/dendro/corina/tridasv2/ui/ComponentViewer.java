/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

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
import javax.swing.border.EtchedBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.tridas.schema.TridasIdentifier;

import edu.cornell.dendro.corina.gui.dbbrowse.BooleanCellRenderer;
import edu.cornell.dendro.corina.gui.dbbrowse.ElementListCellRenderer;
import edu.cornell.dendro.corina.gui.dbbrowse.ElementListManager;
import edu.cornell.dendro.corina.gui.dbbrowse.ElementListPopupMenu;
import edu.cornell.dendro.corina.gui.dbbrowse.ElementListTableModel;
import edu.cornell.dendro.corina.gui.dbbrowse.ElementListTableSorter;
import edu.cornell.dendro.corina.sample.CachedElement;
import edu.cornell.dendro.corina.sample.CorinaWsiTridasElement;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.util.PopupListener;
import edu.cornell.dendro.corina.wsi.ResourceEvent;
import edu.cornell.dendro.corina.wsi.ResourceEventListener;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesResource;

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
	
	private JPanel contentPanel, tablePanel, treePanel;
	private JTable table;
	private JTree tree;
	
	private ElementListTableSorter tableSorter;
	private ElementListTableModel tableModel;
	private DefaultTreeModel treeModel;
	
	private final static String TABLEPANEL = "Series Table View";
	private final static String TREEPANEL = "Series Tree View";
	
	public ComponentViewer(Sample sample) {
		this.sample = sample;
		this.loadedComprehensive = false;
		
		initComponents();
		updateContent();
		
		// default to this view...
		btnTableView.doClick();
	}
	
	private void initComponents() {
		JLabel label;
		
		setLayout(new BorderLayout());
		
		// create button panel
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		label = new JLabel("View as: ");
		btnTableView = new JRadioButton("table");
		btnTableView.putClientProperty("cv.cardName", TABLEPANEL);
		btnTreeView = new JRadioButton("tree");
		btnTreeView.putClientProperty("cv.cardName", TREEPANEL);
		
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
		group.add(btnTableView);
		group.add(btnTreeView);
		
		// add it all to a panel
		topPanel.add(label);
		topPanel.add(Box.createHorizontalStrut(12));
		topPanel.add(btnTableView);
		topPanel.add(Box.createHorizontalStrut(8));
		topPanel.add(btnTreeView);
		topPanel.add(Box.createHorizontalGlue());
		
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
		
		contentPanel.add(tablePanel, TABLEPANEL);
		contentPanel.add(treePanel, TREEPANEL);
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
	}
	
	private void setupTable() {
		tableModel = new ElementListTableModel();
		table = new JTable(tableModel);
		
		tableSorter = new ElementListTableSorter(tableModel, table);
		table.getTableHeader().addMouseListener(tableSorter); // add sorter & header renderer
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
    	// set our column widths
    	ElementListTableModel.setupColumnWidths(table);
		
    	table.setDefaultRenderer(Object.class, new ElementListCellRenderer(this, false));
		table.setDefaultRenderer(Boolean.class, new BooleanCellRenderer());
		
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
	
	private void recurseAddElementsToList(ElementList elements, ElementList flat, 
			DefaultMutableTreeNode parent, int depth) {
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
					ElementList sampleElements = s.getElements();
					if(sampleElements != null)
						recurseAddElementsToList(sampleElements, flat, node, depth + 1);
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
		
		recurseAddElementsToList(elements, displayElements, rootNode, 0);

		// should be unsorted - default order is what we want
		tableModel.setElements(displayElements);
		treeModel.setRoot(rootNode);
		
		// expand all nodes in tree
		// note: tree.getRowCount() changes as we expand each node!
		for(int i = 0; i < tree.getRowCount(); i++) {
			tree.expandRow(i);
		}
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
		
		if(sample.getLoader() instanceof CorinaWsiTridasElement) {
			TridasIdentifier identifier = ((CorinaWsiTridasElement) sample.getLoader()).getTridasIdentifier();
			// create a new resource
			SeriesResource resource = new SeriesResource(identifier, EntityType.MEASUREMENT_SERIES, CorinaRequestType.READ);

			// flag it as comprehensive
			resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.COMPREHENSIVE);
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
}
