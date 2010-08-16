package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.schema.CorinaRequestFormat;
import edu.cornell.dendro.corina.schema.SearchOperator;
import edu.cornell.dendro.corina.schema.SearchParameterName;
import edu.cornell.dendro.corina.schema.SearchReturnObject;
import edu.cornell.dendro.corina.tridasv2.TridasComparator;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceAccessDialog;
import edu.cornell.dendro.corina.wsi.corina.CorinaResourceProperties;
import edu.cornell.dendro.corina.wsi.corina.SearchParameters;
import edu.cornell.dendro.corina.wsi.corina.resources.EntitySearchResource;

public class TridasTreeViewPanel extends TridasTreeViewPanel_UI implements MouseListener, ActionListener {

	private static final long serialVersionUID = 1185669228536105855L;
	TridasTree tree;
	JPopupMenu popup;
	JMenuItem menuItem;
	DefaultMutableTreeNode currentSearchedNode = null;
	EventListenerList tridasListeners = new EventListenerList();
	
	public TridasTreeViewPanel()
	{
    	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Corina Database");
    	createNodes(top);
    	tree = new TridasTree(top);
    	tree.setCellRenderer(new TridasTreeCellRenderer());
    	ToolTipManager.sharedInstance().registerComponent(tree);

    	//treeModel = new DefaultTreeModel(top);
    	//treeModel.addTreeModelListener(new TridasTreeModelListener());

    	//Listen for when the selection changes.
        tree.addMouseListener(this);


        treeScrollPane.setViewportView(tree);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);        
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
	}
	
	/**
	 * Set up the popup menu 
	 */
	public void initPopupMenu(boolean expandEnabled)
	{
        // define the popup
        popup = new JPopupMenu();
        
        // Expand 
        menuItem = new JMenuItem("Expand branch");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("expand");
       	menuItem.setEnabled(expandEnabled);
        popup.add(menuItem);
        
        // Select
        menuItem = new JMenuItem("Select this entity");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("select");
        popup.add(menuItem);
        
        // Refresh
        menuItem = new JMenuItem("Refresh");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("refresh");
        popup.add(menuItem);
        
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);

	}
	
	/**
	 * Populate the tree with object nodes
	 * 
	 * @param top
	 */
    @SuppressWarnings("unchecked")
	private void createNodes(DefaultMutableTreeNode top)
    {
    	DefaultMutableTreeNode objectNode = null;

        List<TridasObjectEx> objectList = App.tridasObjects.getObjectList();
        for(TridasObjectEx object : objectList)
        {
            objectNode = new DefaultMutableTreeNode(object);   
            //top.add(objectNode);
            addTridasNodeInOrder(tree, top, objectNode);
        }
        

    }
    
    private void addTridasNodeInOrder(TridasTree theTree, DefaultMutableTreeNode parent, DefaultMutableTreeNode nodeToAdd)
    {
    	int insertPos = 0;
    	
    	ITridas entityToAdd = (ITridas)nodeToAdd.getUserObject();
    	
    	TridasComparator comparator = new TridasComparator(TridasComparator.Type.SENIOR_ENTITIES_THEN_LAB_CODE_THEN_TITLES, 
				TridasComparator.NullBehavior.NULLS_LAST, 
				TridasComparator.CompareBehavior.AS_NUMBERS_THEN_STRINGS);
   
    	
    	for(int i=0; i<parent.getChildCount(); i++)
    	{	
    		insertPos = i;
    		if(((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject() instanceof ITridas)
    		{
	    		ITridas child = (ITridas) ((DefaultMutableTreeNode)parent.getChildAt(i)).getUserObject();
	    		
	    		if(comparator.compare(child, entityToAdd)>=0)
	    		{
	    			break;
	    		}

    		}
    	}
    	
    	// Add node to Tree
    	if(tree==null)
    	{
    		parent.add(nodeToAdd);
    	}
    	else
    	{
	    	DefaultTreeModel model = (DefaultTreeModel) theTree.getModel();  
	    	model.insertNodeInto(nodeToAdd, parent, insertPos);
    	}
    }

    /**
     * Expand the specified entity node in the tree
     * 
     * @param node
     */
    @SuppressWarnings("unchecked")
	public void expandEntity(DefaultMutableTreeNode node)
    {
    	ITridas entity;
    	SearchParameters param;
    	EntitySearchResource<?> resource = null;
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
    		
    		// Set return type to element and set search param
        	param = new SearchParameters(SearchReturnObject.ELEMENT);
        	param.addSearchConstraint(SearchParameterName.OBJECTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasElement>(param, TridasElement.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    		    		
    	}
    	else if(node.getUserObject() instanceof TridasElement) 
    	{
    		// Set return type to sample and set search param
        	param = new SearchParameters(SearchReturnObject.SAMPLE);
        	param.addSearchConstraint(SearchParameterName.ELEMENTID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasSample>(param, TridasSample.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    	    		
    	}
    	else if(node.getUserObject() instanceof TridasSample) 
    	{
    		// Set return type to radius and set search param
        	param = new SearchParameters(SearchReturnObject.RADIUS);
        	param.addSearchConstraint(SearchParameterName.SAMPLEID, SearchOperator.EQUALS, entity.getIdentifier().getValue());
    		resource = new EntitySearchResource<TridasRadius>(param, TridasRadius.class);
    		resource.setProperty(CorinaResourceProperties.ENTITY_REQUEST_FORMAT, CorinaRequestFormat.MINIMAL);
    	    		
    	}
    	
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
            //Integer childCount = node.getChildCount();
            //((DefaultTreeModel) tree.getModel()).insertNodeInto(objectNode, node, childCount);
            addTridasNodeInOrder(tree, node, newChildNode);
		}

		// Expand tree
		tree.expandPath(path);
    	
    }
    
	private void doSearch(DefaultMutableTreeNode node)
	{
   	
    	if(node.getUserObject() instanceof ITridas)
    	{
    		TridasSelectEvent event = new TridasSelectEvent(tree, 1001, ((ITridas)node.getUserObject()));
    		this.fireTridasSelectListener(event);
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
			doSearch((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
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
	}
	
	private void showPopupMenu(JComponent source, int x, int y, boolean expandEnabled)
	{
		this.initPopupMenu(expandEnabled);
		popup.show(source, x, y);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
        if ( e.getButton()== MouseEvent.BUTTON3 || e.getButton()== MouseEvent.BUTTON2) 
        {
    		// Right click event so show menu
 
        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        	if(node.getUserObject() instanceof TridasRadius)
        	{
        		// Don't allow the user to expand this branch
        		showPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), false);
        	}
        	else
        	{
        		showPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), true);
        	}
        }
        else if ( e.getButton()== MouseEvent.BUTTON1)
        {
       /* 	if(e.getClickCount()>1)
            {
            	// Double left click so expand branch
            	this.expandEntity((DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent());
            }
        	else
        	{
	        	// Single left click event so select current entity
	        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
	        	doSearch(node);
        	}
        */
        	
        }
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) {	}

	@Override
	public void mousePressed(MouseEvent e) { }

	@Override
	public void mouseReleased(MouseEvent e) { }
	
	public void addTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.add(TridasSelectListener.class, listener);
	}
	
	public void removeTridasSelectListener(TridasSelectListener listener)
	{
		tridasListeners.remove(TridasSelectListener.class, listener);
	}
	
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
}
