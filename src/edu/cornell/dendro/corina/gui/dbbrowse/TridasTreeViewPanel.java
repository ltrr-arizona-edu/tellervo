package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.event.EventListenerList;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.BaseSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasIdentifier;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.gui.dbbrowse.CorinaCodePanel.ObjectListMode;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.CorinaRequestType;
import edu.cornell.dendro.corina.schema.EntityType;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.schema.WSIEntity;
import edu.cornell.dendro.corina.tridasv2.GenericFieldUtils;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.util.labels.LabBarcode.Type;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntityResource;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;
import edu.cornell.dendro.corina.wsi.corina.resources.SeriesSearchResource;
import edu.cornell.dendro.corina.wsi.corina.resources.WSIEntityResource;

public class TridasTreeViewPanel extends TridasTreeViewPanel_UI implements MouseListener, ActionListener, TridasSelectListener {

	private static final long serialVersionUID = 1185669228536105855L;
	private TridasTree tree;
	private JPopupMenu popup;
	private JMenuItem menuItem;
	private CorinaCodePanel panel;
	private EventListenerList tridasListeners = new EventListenerList();
	private String textForSelectPopup = "Search for associated series";
	private Boolean listenersAreCheap = false;
	private TreeDepth depth = TreeDepth.RADIUS;
	private ObjectListMode baseObjectListMode = ObjectListMode.TOP_LEVEL_ONLY;
	
	/**
	 * Basic constructor for tree view panel used in the context of searching
	 * for series.  Defaults are:
	 * 
	 * TreeDepth - Depth beyond which the tree will not expand 
	 * @see #setTreeDepth(TreeDepth) 
	 *   default = radius 
	 * 
	 * ListenersAreCheap - Are the attached listeners computationally cheap?
	 * @see #setListenersAreCheap(Boolean)
	 *   default = false
	 *  
	 * TextForSelectPopup - Text to display in popup menu when selecting entity
	 * @see #setTextForSelectPopup(String)
	 *   default = Search for associated series
	 * 
	 * BaseObjectListMode - Object list mode to use for base objects
	 * @see edu.cornell.dendro.corina.gui.dbbrowse.CorinaCodePanel.ObjectListMode
	 *   default = Top level only
	 */
	public TridasTreeViewPanel()
	{
		setupGui();
		setupTree(null);
		
	}
	
	/**
	 * Complete constructor for tree view panel.  
	 * 
	 * @param depth - @see #setTreeDepth(TreeDepth)
	 * @param listenersAreCheap - @see #setListenersAreCheap(Boolean)
	 * @param textForSelectPopup - @see #setTextForSelectPopup(String)
	 */
	public TridasTreeViewPanel(TreeDepth depth, Boolean listenersAreCheap, 
			String textForSelectPopup)
	{
		setupGui();
		setupTree(null);
		setTreeDepth(depth);
		setListenersAreCheap(listenersAreCheap);
		setTextForSelectPopup(textForSelectPopup);
	}
	
	/**
	 * Setup the gui
	 */
	private void setupGui()
	{
		panel = new CorinaCodePanel();
		panel.addTridasSelectListener(this);
		containerPanel.setLayout(new BorderLayout());
		this.containerPanel.add(panel, BorderLayout.CENTER);	
	}


	public static enum TreeDepth {
		OBJECT(1),
		ELEMENT(2),
		SAMPLE(3),
		RADIUS(4),
		SERIES(5);
		
		private int depth;
		
		TreeDepth(int c) {
			depth = c;
		}
		
		public int getDepth() {
			return depth;
		}
		
		public static TreeDepth valueOf(int c) {
			for(TreeDepth depth : values()) {
				if(depth.getDepth() == c)
					return depth;
			}
			
			throw new IllegalArgumentException("Invalid type");
		}
	}
	
	
	/**
	 * Sets the focus to the primary element
	 */
	public void setFocus()
	{
		panel.setFocus();
	}
	
	/**
	 * Set the depth the tree should expand to. At the specified point the 
	 * popup menu entry for 'Expand branch' will be disabled.
	 * 
	 * @param depth
	 */
	public void setTreeDepth(TreeDepth depth)
	{
		this.depth = depth;
	}
	
	/**
	 * Get the depth to which the tree should expand
	 * 
	 * @return
	 */
	public TreeDepth getTreeDepth()
	{
		return depth;
	}
	
	/**
	 * Set the base nodes of the tree to the specified objects
	 * 
	 * @param objList
	 */
	public void setObjectList(List<TridasObjectEx> objList)
	{
		setupTree(objList);
	}
	
	/**
	 * Set the base nodes of the tree to one of the default object lists
 	 *
	 * @param mode
	 */
	public void setObjectList(ObjectListMode mode)
	{
		this.baseObjectListMode = mode;
		setupTree();
	}
	
	
	/**
	 * Set whether the listeners listening to this panel are computationally 
	 * cheap when events are thrown.  This changes the behaviour of single and
	 * double clicks.
	 * 
	 * @param cheap
	 */
	public void setListenersAreCheap(Boolean cheap)
	{
		this.listenersAreCheap = cheap;
	}
	
	/**
	 * Set the text that will appear in the popup menu for selecting
	 * an entity.
	 * 
	 * @param text
	 */
	public void setTextForSelectPopup(String text)
	{
		textForSelectPopup = text;
	}
	
	/**
	 * Set up the tree pre-populated with objects according to 
	 * baseObjectListMode.
	 */
	private void setupTree()
	{
		setupTree(null);
	}
	
	/**
	 * Set up the tree.  If objectList is passed then this is used
	 * as the base nodes of the tree, otherwise the objects specified
	 * by baseObjectListMode are used.
	 * 
	 * @param objList
	 */
	private void setupTree(List<TridasObjectEx> objList)
	{
		// Set up tree
    	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Corina Database");
    	if(objList!=null)
    	{
    		addObjectsToTree(top, objList);
    	}
    	else
    	{
    		addObjectsToTree(top);
    	}
    	tree = new TridasTree(top);
    	tree.setCellRenderer(new TridasTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);        
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.addMouseListener(this);
        ToolTipManager.sharedInstance().registerComponent(tree);
    	treeScrollPane.setViewportView(tree);
	}
	
	
	/**
	 * Set up the popup menu 
	 */
	private void initPopupMenu(boolean expandEnabled, Class<?> clazz)
	{
		String className = this.getFriendlyClassName(clazz);
		Boolean isTridas = false;
		if(clazz.getSimpleName().startsWith("Tridas"))
		{
			isTridas = true;
		}
		
        // define the popup
        popup = new JPopupMenu();
        
        if(isTridas)
        {
	        // Expand 
	        menuItem = new JMenuItem("Expand branch");
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("expand");
	       	menuItem.setEnabled(expandEnabled);
	        popup.add(menuItem);
	        
	        // Select
	        menuItem = new JMenuItem(this.textForSelectPopup);
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("select");
	        popup.add(menuItem);
	        popup.addSeparator();
	        
	        // Delete
	        menuItem = new JMenuItem("Delete this "+className.toLowerCase());
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("delete");
	        popup.add(menuItem);
	        popup.addSeparator();
        }
        
        // Refresh
        menuItem = new JMenuItem("Refresh");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("refresh");
        popup.add(menuItem);
        
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);

	}
	
	/**
	 * Populate the tree with specified objects
	 * 
	 * @param top
	 */
    @SuppressWarnings("unchecked")
	private void addObjectsToTree(DefaultMutableTreeNode top, List<TridasObjectEx> objectList)
    {
    	DefaultMutableTreeNode objectNode = null;

        
        for(TridasObjectEx object : objectList)
        {
            objectNode = new DefaultMutableTreeNode(object);   
            //top.add(objectNode);
            addTridasNodeInOrder(tree, top, objectNode);
        }
    }
    
    /**
     * Populate the tree with the object list specified by @see {@link #baseObjectListMode}
     * 
     * @param parentNode - node to which the objects should be added
     */
    private void addObjectsToTree(DefaultMutableTreeNode parentNode)
    {
    	List<TridasObjectEx> objectList = null;
    	
    	if(this.baseObjectListMode==ObjectListMode.TOP_LEVEL_ONLY)
    	{
    		objectList = App.tridasObjects.getTopLevelObjectList();
    	}
    	else if (this.baseObjectListMode==ObjectListMode.POPULATED)
    	{
    		objectList = App.tridasObjects.getPopulatedObjectList();
    	}
    	else if (this.baseObjectListMode==ObjectListMode.POPULATED_FIRST)
    	{
    		objectList = App.tridasObjects.getPopulatedFirstObjectList();
    	}
    	else if (this.baseObjectListMode==ObjectListMode.ALL)
    	{
    		objectList = App.tridasObjects.getObjectList();
    	}
    	
    	addObjectsToTree(parentNode, objectList);
    }
    
    /**
     * Add the specified node to the tree making sure that it goes in order amongst its siblings
     * 
     * @param theTree
     * @param parent
     * @param nodeToAdd
     */
    private void addTridasNodeInOrder(TridasTree theTree, DefaultMutableTreeNode parent, DefaultMutableTreeNode nodeToAdd)
    {
    	Integer insertPos = null;
    	
    	ITridas entityToAdd = (ITridas)nodeToAdd.getUserObject();
    	
    	TridasComparator comparator = new TridasComparator(TridasComparator.Type.SENIOR_ENTITIES_THEN_LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
   
    	for(int i=0; i<parent.getChildCount(); i++)
    	{	
    		
    		if(((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject() instanceof ITridas)
    		{
	    		ITridas child = (ITridas) ((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject();
	    		
	    		if(comparator.compare(child, entityToAdd)>=0)
	    		{
	    			insertPos = i;
	    			break;
	    		}

    		}
    	}
    	
    	// Add node to Tree
    	if(tree==null || parent.getChildCount()==0)
    	{
    		// First node to be added to parent
    		parent.add(nodeToAdd);
    	}
    	else if (insertPos==null)
    	{
    		// No later nodes found under parent so add to end
    		parent.add(nodeToAdd);
    	}
    	else
    	{
    		// Insert before specified node
	    	DefaultTreeModel model = (DefaultTreeModel) theTree.getModel();  
	    	model.insertNodeInto(nodeToAdd, parent, insertPos);
    	}
    }

    /**
     * Expand the specified entity node in the tree
     * 
     * @param node
     */
    @SuppressWarnings({ "unchecked", "null" })
	public void expandEntity(DefaultMutableTreeNode node)
    {
    	ITridas entity;
    	SearchParameters param;
    	EntitySearchResource<?> resource = null;
    	SeriesSearchResource seriesSearchResource = null;
    	TreePath path = tree.getSelectionPath();
    	
    	// Node already expanded so return
    	if(node.getChildCount()>0){	return;	}
    	
    	if(!(node.getUserObject() instanceof ITridas))
    	{
    		// Node not a Tridas entity so return
    		return;
    	}
    	else
    	{
    		entity = (ITridas)node.getUserObject();
    	}
    	
    	// Build search
    	if((node.getUserObject() instanceof TridasObjectEx) || 
    	   (node.getUserObject() instanceof TridasObject))
    	{
    		// Objects are a special case as they can have both object and element children
    		// Do sub-object search first
    		
    		// Set return type to element and set search param
        	param = new SearchParameters(SearchReturnObject.OBJECT);
        	param.addSearchConstraint(SearchParameterName.PARENTOBJECTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasObject>(param, TridasObject.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    		
        	// Do Search     	
    		CorinaResourceAccessDialog dialog = new CorinaResourceAccessDialog(resource);
    		resource.query();	
    		dialog.setVisible(true);
    		if(!dialog.isSuccessful()) 
    		{ 
    			System.out.println("Error searching for updates to tree view");
    			return;
    		}
    	
    		// Add returned entities to tree
    		List<ITridas> returnList = (List<ITridas>) resource.getAssociatedResult();
    		DefaultMutableTreeNode newChildNode = null;
    		for (ITridas ent : returnList)
    		{
                newChildNode = new DefaultMutableTreeNode(ent);  
                Integer childCount = node.getChildCount();
                //((DefaultTreeModel) tree.getModel()).insertNodeInto(objectNode, node, childCount);
                addTridasNodeInOrder(tree, node, newChildNode);

    		}

    		// Now set up to do the element search   		
    		if(depth.getDepth()<=TreeDepth.OBJECT.getDepth())
    		{
    			return;
    		}
    		
    		// Set return type to element and set search param
        	param = new SearchParameters(SearchReturnObject.ELEMENT);
        	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    		    		
    	}
    	else if(node.getUserObject() instanceof TridasElement) 
    	{
    		if(depth.getDepth()<=TreeDepth.ELEMENT.getDepth())
    		{
    			return;
    		}
    		
    		// Set return type to sample and set search param
        	param = new SearchParameters(SearchReturnObject.SAMPLE);
        	param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    	    		
    	}
    	else if(node.getUserObject() instanceof TridasSample) 
    	{
    		if(depth.getDepth()<=TreeDepth.SAMPLE.getDepth())
    		{
    			return;
    		}
    		
    		// Set return type to radius and set search param
        	param = new SearchParameters(SearchReturnObject.RADIUS);
        	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasRadius>(param, TridasRadius.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    	    		
    	}
    	else if(node.getUserObject() instanceof TridasRadius) 
    	{
    		if(depth.getDepth()<=TreeDepth.RADIUS.getDepth())
    		{
    			return;
    		}
    		
    		// Special case 
    		
    		
    		// Set return type to radius and set search param
        	param = new SearchParameters(SearchReturnObject.MEASUREMENT_SERIES);
        	param.addSearchConstraint(SearchParameterName.RADIUSID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		seriesSearchResource = new SeriesSearchResource(param);
    		seriesSearchResource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    	    		
    	}

    	// Do Search     	
		CorinaResourceAccessDialog dialog;
		List<ITridas> returnList;
		if(node.getUserObject() instanceof TridasRadius) 
		{
			dialog = new CorinaResourceAccessDialog(seriesSearchResource);
			seriesSearchResource.query();
			dialog.setVisible(true);
			if(!dialog.isSuccessful()) 
			{ 
				System.out.println("Error searching for updates to tree view");
				return;
			}

			ElementList elements = seriesSearchResource.getAssociatedResult();
			returnList = new ArrayList<ITridas>();
			for(Element el : elements)
			{
				Sample s;
				try {
					s = el.load();
					ITridasSeries ser = s.getSeries();
					returnList.add(ser);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			dialog = new CorinaResourceAccessDialog(resource);
			resource.query();
			dialog.setVisible(true);
			if(!dialog.isSuccessful()) 
			{ 
				System.out.println("Error searching for updates to tree view");
				return;
			}
		
			// Add returned entities to tree
			returnList = (List<ITridas>) resource.getAssociatedResult();
		}
			

		DefaultMutableTreeNode newChildNode = null;
		for (ITridas ent : returnList)
		{
            newChildNode = new DefaultMutableTreeNode(ent);  
            //Integer childCount = node.getChildCount();
            //((DefaultTreeModel) tree.getModel()).insertNodeInto(objectNode, node, childCount);
            addTridasNodeInOrder(tree, node, newChildNode);
		} 	
		
		// Expand tree
		tree.expandPath(path);
    	
    }
    
    /**
     * Select an entity and notify listeners
     * 
     * @param node
     */
	private void doSelectEntity(DefaultMutableTreeNode node)
	{
   	
    	if(node.getUserObject() instanceof ITridas)
    	{
    		TridasSelectEvent event = new TridasSelectEvent(tree, TridasSelectEvent.ENTITY_SELECTED, ((ITridas)node.getUserObject()));
    		this.fireTridasSelectListener(event);
    	}
	}
		
	/**
	 * Display a popup menu with the expand button enabled or disabled
	 * 
	 * @param source
	 * @param x
	 * @param y
	 * @param expandEnabled
	 */
	private void showPopupMenu(JComponent source, int x, int y, Class<?> clazz, boolean expandEnabled)
	{
		this.initPopupMenu(expandEnabled, clazz);
		popup.show(source, x, y);
	}
	
	/**
	 * Get a human friendly name for a class
	 * 
	 * @param clazz
	 * @return
	 */
	private String getFriendlyClassName(Class<?> clazz)
	{
		String className = null;
		if(clazz.getSimpleName().equals("TridasObjectEx"))
		{
			className = "Object";
		}
		else if(clazz.getSimpleName().startsWith("Tridas"))
		{
			className = clazz.getSimpleName().substring(6);
		}
		else
		{
			className = clazz.getSimpleName();
		}
		
		return className;
	}
    
	

	/**
	 * Remove the specified node from the tree
	 * 
	 * @param node
	 */
	private void deleteEntity(DefaultMutableTreeNode node)
	{
		ITridas entity = null;
		EntityResource rsrc = null;
		String entityType = "";
		
		if(node.getUserObject() instanceof TridasObject)
		{
			entityType = "Object";
			entity = (TridasObject) node.getUserObject();
			rsrc = new EntityResource<TridasObject>(entity, CorinaRequestType.DELETE, TridasObject.class);
		}
		else if(node.getUserObject() instanceof TridasElement)
		{
			entityType = "Element";
			entity = (TridasElement) node.getUserObject();
			rsrc = new EntityResource<TridasElement>(entity, CorinaRequestType.DELETE, TridasElement.class);
		}
		else if(node.getUserObject() instanceof TridasSample)
		{
			entityType = "Sample";
			entity = (TridasSample) node.getUserObject();
			rsrc = new EntityResource<TridasSample>(entity, CorinaRequestType.DELETE, TridasSample.class);
		}
		else if(node.getUserObject() instanceof TridasRadius)
		{
			entityType = "Radius";
			entity = (TridasRadius) node.getUserObject();
			rsrc = new EntityResource<TridasRadius>(entity, CorinaRequestType.DELETE, TridasRadius.class);
		}
		else
		{
			return;
		}
			    			
		// Do query
		CorinaResourceAccessDialog accdialog = new CorinaResourceAccessDialog(rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		
		if(accdialog.isSuccessful())
		{
			rsrc.getAssociatedResult();
			JOptionPane.showMessageDialog(this, entityType+" deleted", "Success", JOptionPane.NO_OPTION);
			((DefaultTreeModel)tree.getModel()).removeNodeFromParent(node);
			return;
		}
		
		JOptionPane.showMessageDialog(this, "Unable to delete this "+ entityType.toLowerCase() +" as it is referenced by other entries in the database.", 
				"Error", JOptionPane.ERROR_MESSAGE);
		
		return;
	}
	
	/***********
	 * LISTENERS
	 ***********/

	/**
	 * Add a listener 
	 * 
	 * @param listener
	 */
	public void addTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.add(TridasSelectListener.class, listener);
	}
	
	/**
	 * Remove a listener
	 * @param listener
	 */
	public void removeTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.remove(TridasSelectListener.class, listener);
	}
	
	/**
	 * Fire a selected entity event
	 * 
	 * @param event
	 */
	protected void fireTridasSelectListener(TridasSelectEvent event)
	{
	     Object[] listeners = tridasListeners.getListenerList();
	     // loop through each listener and pass on the event if needed
	     Integer numListeners = listeners.length;
	     for (int i = 0; i<numListeners; i+=2) 
	     {
	          if (listeners[i]==TridasSelectListener.class) 
	          {
	               // pass the event to the listeners event dispatch method
	                ((TridasSelectListener)listeners[i+1]).entitySelected(event);
	          }            
	     }

	}
	
	/********
	 * EVENTS
	 ********/
	
	@Override
	public void mouseClicked(MouseEvent e) { }

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void mouseReleased(MouseEvent e) { }
	
	@Override
	public void mousePressed(MouseEvent e) {
		
		int selRow = tree.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());

		if(selRow==-1) return;
		
        if (e.getButton()== MouseEvent.BUTTON3 || e.getButton()== MouseEvent.BUTTON2) 
        {
    		// Right click event so show menu
        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
        	
        	// Only enabled the expand option if we're not too deep.
        	if( ((node.getUserObject() instanceof TridasObject)  && (depth.getDepth()<= TreeDepth.OBJECT .getDepth())) || 
        		((node.getUserObject() instanceof TridasElement) && (depth.getDepth()<= TreeDepth.ELEMENT.getDepth())) ||
        		((node.getUserObject() instanceof TridasSample)  && (depth.getDepth()<= TreeDepth.SAMPLE .getDepth())) ||
        		((node.getUserObject() instanceof TridasRadius)  && (depth.getDepth()<= TreeDepth.RADIUS .getDepth())) ||
        		((node.getUserObject() instanceof TridasElement) && (depth.getDepth()<= TreeDepth.ELEMENT.getDepth())) 
        	   )
        	{
        			showPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), node.getUserObject().getClass(), false);
        	}
        	else
        	{
        		// Show menu with expand branch enabled
        		showPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), node.getUserObject().getClass(), true);
        	}
        }
		
		if(this.listenersAreCheap)
		{
	        if ( e.getButton()== MouseEvent.BUTTON1)
	        {
	        	if(e.getClickCount()>1)
	            {
		        	// Double left click event so expand entity
	        		this.expandEntity((DefaultMutableTreeNode) selPath.getLastPathComponent());
	            }
	        	else
	        	{
		        	// Listeners are cheap so do select on single left click
		        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		        	doSelectEntity(node);
	        	}
	        }
        }
        else
        {
	        if ( e.getButton()== MouseEvent.BUTTON1)
	        {
	        	if(e.getClickCount()>1)
	            {
	        		// Select on double left click
		        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
		        	doSelectEntity(node); 	
	            }
	        	else
	        	{
	        		// Listeners are expensive so don't do anything on single left click
	        	}
	        }
        }
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("expand"))
		{
			// Request to expand the current node of tree
			expandEntity((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent());
		}
		else if (e.getActionCommand().equals("select"))
		{
			doSelectEntity((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
		}
		else if (e.getActionCommand().equals("refresh"))
		{
			// Remove all children then add again
			DefaultMutableTreeNode node = ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
			Integer children = node.getChildCount();
			node.removeAllChildren();
			children = node.getChildCount();
			node.removeAllChildren();
			((DefaultTreeModel)tree.getModel()).reload();
			expandEntity(node);
		}
		else if (e.getActionCommand().equals("delete"))
		{
			// Delete this entity
			Object[] options = {"OK",
            "Cancel"};
			int ret = JOptionPane.showOptionDialog(getParent(), 
					"Are you sure you want to permanently delete this entity?", 
					"Confirm delete", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			if(ret == JOptionPane.YES_OPTION)
			{
				deleteEntity((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
			}			
		}
	}
	
	@Override
	public void entitySelected(TridasSelectEvent event) {
		List<? extends ITridas> entities = event.getEntityList();
		
		if(entities==null)
		{
			return;
		}
		else if (entities.size()!=1)
		{
			return;
		}
		
		ITridas entity = entities.get(0);
		if(entity instanceof TridasObject)
		{
			TridasObjectEx object = (TridasObjectEx) entity;
					
			
			TreePath path = tree.getNextMatch(GenericFieldUtils.findField(object, "corina.objectLabCode").getValue().toString(), 0, Position.Bias.Forward);
			tree.setSelectionPath(path);
			tree.scrollPathToVisible(path);
			
			this.fireTridasSelectListener(new TridasSelectEvent(this, TridasSelectEvent.ENTITY_SELECTED, entity));
		}
		else if (entity instanceof ITridas)
		{
			this.fireTridasSelectListener(new TridasSelectEvent(this, TridasSelectEvent.ENTITY_SELECTED, entity));
		}
		
		
	}
	
	

}
