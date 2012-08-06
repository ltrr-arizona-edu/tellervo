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
package org.tellervo.desktop.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.EventListenerList;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.TridasSelectEvent;
import org.tellervo.desktop.gui.TridasSelectListener;
import org.tellervo.desktop.gui.hierarchy.TridasTree;
import org.tellervo.desktop.gui.hierarchy.TridasTreeCellRenderer;
import org.tellervo.desktop.gui.hierarchy.TridasTreeViewPanel_UI;
import org.tellervo.desktop.gui.widgets.TellervoCodePanel.ObjectListMode;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.schema.TellervoRequestFormat;
import org.tellervo.schema.TellervoRequestType;
import org.tellervo.schema.SearchOperator;
import org.tellervo.schema.SearchParameterName;
import org.tellervo.schema.SearchReturnObject;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.tridasv2.TridasComparator;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceProperties;
import org.tellervo.desktop.wsi.tellervo.SearchParameters;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tellervo.desktop.wsi.tellervo.resources.EntitySearchResource;
import org.tellervo.desktop.wsi.tellervo.resources.SeriesSearchResource;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils.TreeDepth;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


public class TridasTreeViewPanel extends TridasTreeViewPanel_UI implements ActionListener, TridasSelectListener {
	private final static Logger log = LoggerFactory.getLogger(TridasTreeViewPanel.class);
	private static final long serialVersionUID = 1185669228536105855L;
	protected TridasTree tree;
	protected TellervoCodePanel panel;
	protected EventListenerList tridasListeners = new EventListenerList();
	protected String textForSelectPopup = "Search for associated series";
	protected Boolean listenersAreCheap = false;
	protected TreeDepth depth = TreeDepth.RADIUS;
	protected ObjectListMode baseObjectListMode = ObjectListMode.TOP_LEVEL_ONLY;
	protected Boolean derivedVisible = false;
	private Window parent = null;
	
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
	 * @see org.tellervo.desktop.gui.widgets.TellervoCodePanel.ObjectListMode
	 *   default = Top level only
	 */
	public TridasTreeViewPanel(Boolean setupTree)
	{
		setupGui();
		if(setupTree)
		{
			setupTree(null);
		}
			
	}
	
	/**
	 * Complete constructor for tree view panel.  
	 * 
	 * @param depth - @see #setTreeDepth(TreeDepth)
	 * @param listenersAreCheap - @see #setListenersAreCheap(Boolean)
	 * @param textForSelectPopup - @see #setTextForSelectPopup(String)
	 */
	public TridasTreeViewPanel(Window parent, TreeDepth depth, Boolean listenersAreCheap, 
			String textForSelectPopup, Boolean setupTree)
	{
		this.parent = parent;
		setupGui();
		if(setupTree)
		{
			setupTree(null);
			setTreeDepth(depth);
		}
		setListenersAreCheap(listenersAreCheap);
		setTextForSelectPopup(textForSelectPopup);
	}
	
	
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
	 * @see org.tellervo.desktop.gui.widgets.TellervoCodePanel.ObjectListMode
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
	public TridasTreeViewPanel(Window parent, TreeDepth depth, Boolean listenersAreCheap, 
			String textForSelectPopup)
	{
		this.parent = parent;
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
		panel = new TellervoCodePanel();
		panel.addTridasSelectListener(this);
		containerPanel.setLayout(new BorderLayout());
		this.containerPanel.add(panel, BorderLayout.CENTER);	
	}



	
	/**
	 * Set whether derived series should be shown in the tree
	 * 
	 * @param b
	 */
	public void setDerivedVisible(Boolean b)
	{
		derivedVisible = b;
	}
	
	public Boolean isDerivedVisible()
	{
		return derivedVisible;
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
		log.debug("starting to set up tree");
		// Set up tree
    	DefaultMutableTreeNode top = new DefaultMutableTreeNode(App.getLabName()+" Database");
    	if(objList!=null)
    	{
    		addObjectsToTree(tree, top, objList);
    	}
    	else
    	{
    		addObjectsToTree(tree, top);
    	}
    	tree = new TridasTree(top);
    	tree.setCellRenderer(new TridasTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);        
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
		log.debug("Adding mouse listener...");

        tree.addMouseListener(new PopupListener() {
			
        	
        	@Override
        	public void mouseClicked(MouseEvent e) { 
				        		
        		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				tree.setSelectionPath(selPath);
				
				if(listenersAreCheap)
				{
			        if ( e.getButton()== MouseEvent.BUTTON1)
			        {
			        	try{
				        	if(e.getClickCount()>1)
				            {
					        	// Double left click event so expand entity
				        		expandEntity((DefaultMutableTreeNode) selPath.getLastPathComponent());
				            }
				        	else 
				        	{
					        	// Listeners are cheap so do select on single left click
					        	DefaultMutableTreeNode node1 = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
					        	doSelectEntity(node1);
				        	} 
			        	}catch (NullPointerException ex)
		        		{
		        			// Don't worry - probably just clicked on a handle not a node
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
			public void showPopup(MouseEvent e) 
			{
				log.trace("showPopup called");
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				
				tree.setSelectionPath(selPath);
				
				if(selRow==-1) return;

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
        });
        
        ToolTipManager.sharedInstance().registerComponent(tree);
    	treeScrollPane.setViewportView(tree);
    	
    	log.debug("Finished setting up tree");
	}
		
	

	
	/**
	 * Set up the popup menu 
	 */
	protected JPopupMenu initPopupMenu(boolean expandEnabled, Class<?> clazz)
	{
		
		String className = TridasTreeViewPanel.getFriendlyClassName(clazz);
		Boolean isTridas = false;
		if(clazz.getSimpleName().startsWith("Tridas"))
		{
			isTridas = true;
		}
		
        // define the popup
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem;
        
        if(isTridas)
        {
	        // Expand 
	        menuItem = new JMenuItem("Expand branch");
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("expand");
	       	menuItem.setEnabled(expandEnabled);
	        menuItem.setIcon(Builder.getIcon("view_tree.png", 16));
	     
	        popupMenu.add(menuItem);
	        
	        // Select
	        menuItem = new JMenuItem(this.textForSelectPopup);
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("select");
	        menuItem.setIcon(Builder.getIcon("select.png", 16));
	        popupMenu.add(menuItem);
	        popupMenu.addSeparator();
	        
	        // Delete
	        menuItem = new JMenuItem("Delete this "+className.toLowerCase());
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("delete");
	        menuItem.setIcon(Builder.getIcon("cancel.png", 16));
	        popupMenu.add(menuItem);
	        	        
	        popupMenu.addSeparator();   
        }
        
        // Refresh
        menuItem = new JMenuItem("Refresh");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("refresh");
        menuItem.setIcon(Builder.getIcon("reload.png", 16));
        popupMenu.add(menuItem);
        
        popupMenu.setOpaque(true);
        popupMenu.setLightWeightPopupEnabled(false);

        return popupMenu;
	}
	
	/**
	 * Populate the tree with specified objects
	 * 
	 * @param top
	 */
	protected void addObjectsToTree(TridasTree thetree, DefaultMutableTreeNode top, List<TridasObjectEx> objectList)
    {
    	DefaultMutableTreeNode objectNode = null;

        log.debug("Beginning to loop through object list and adding to tree in order");
        for(TridasObjectEx object : objectList)
        {
            objectNode = new DefaultMutableTreeNode(object);   
            addTridasNodeInOrder(thetree, top, objectNode);
        }
        log.debug("Finished adding objects to tree in order");
    }
    
    /**
     * Populate the tree with the object list specified by @see {@link #baseObjectListMode}
     * 
     * @param parentNode - node to which the objects should be added
     */
    protected void addObjectsToTree(TridasTree thetree, DefaultMutableTreeNode parentNode)
    {
    	log.debug("Starting to get object list from dictionary");
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
    	log.debug("Finished getting object list from dictionary");
    	
    	addObjectsToTree(thetree, parentNode, objectList);
    }
    
    /**
     * Add the specified node to the tree making sure that it goes in order amongst its siblings
     * 
     * @param theTree
     * @param parent
     * @param nodeToAdd
     */
    protected void addTridasNodeInOrder(TridasTree theTree, DefaultMutableTreeNode parent, DefaultMutableTreeNode nodeToAdd)
    {
    	Integer insertPos = null;
    	
    	ITridas entityToAdd = (ITridas)nodeToAdd.getUserObject();
    	
    	TridasComparator comparator = new TridasComparator(TridasComparator.Type.SENIOR_ENTITIES_THEN_TITLES, 
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
    	if(theTree==null || parent.getChildCount()==0)
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
    @SuppressWarnings({ "unchecked" })
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
    		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
    		
        	// Do Search     	
    		TellervoResourceAccessDialog dialog = TellervoResourceAccessDialog.forWindow(parent, resource);
    		resource.query();	
    		dialog.setVisible(true);
    		if(!dialog.isSuccessful()) 
    		{ 
    			log.error("Error searching for updates to tree view");
    			return;
    		}
    	
    		// Add returned entities to tree
    		List<ITridas> returnList = (List<ITridas>) resource.getAssociatedResult();
    		DefaultMutableTreeNode newChildNode = null;
    		for (ITridas ent : returnList)
    		{
                newChildNode = new DefaultMutableTreeNode(ent);  
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
    		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
    		    		
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
    		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
    	    		
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
    		resource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
    	    		
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
        	
        	// Limit search to measurement series if requested
        	if(derivedVisible==false)
        	{
        		param.addSearchConstraint(SearchParameterName.SERIESTYPE, SearchOperator.EQUALS, "Direct");
        	}
        	        	
    		seriesSearchResource = new SeriesSearchResource(param);
    		seriesSearchResource.setProperty(TellervoResourceProperties.ENTITY_REQUEST_FORMAT, TellervoRequestFormat.MINIMAL);
    	    		
    	}

    	// Do Search     	
		TellervoResourceAccessDialog dialog;
		List<ITridas> returnList;
		if(node.getUserObject() instanceof TridasRadius) 
		{
			dialog = new TellervoResourceAccessDialog(seriesSearchResource);
			seriesSearchResource.query();
			dialog.setVisible(true);
			if(!dialog.isSuccessful()) 
			{ 
				log.error("Error searching for updates to tree view");
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
			dialog = new TellervoResourceAccessDialog(resource);
			resource.query();
			dialog.setVisible(true);
			if(!dialog.isSuccessful()) 
			{ 
				log.error("Error searching for updates to tree view");
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
	protected void doSelectEntity(DefaultMutableTreeNode node)
	{
   	
    	if(node.getUserObject() instanceof ITridas)
    	{
    		TridasSelectEvent event = new TridasSelectEvent(tree, TridasSelectEvent.ENTITY_SELECTED, ((ITridas)node.getUserObject()), node);
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
	void showPopupMenu(JComponent source, int x, int y, Class<?> clazz, boolean expandEnabled)
	{
		log.trace("Go ahead and show popup! as x="+x+", y="+y+", class="+clazz+", expandEnabled="+expandEnabled);
		JPopupMenu popupMenu = initPopupMenu(expandEnabled, clazz);
		log.trace("Popup menu = "+popupMenu.toString());
		popupMenu.show(source, x, y);
	}
	
	/**
	 * Get a human friendly name for a class
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getFriendlyClassName(Class<?> clazz)
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
    
	public static String getFriendlyPluralClassName(Class<?> clazz)
	{
		String className = getFriendlyClassName(clazz);
		
		if(className.equals("Radius"))
		{
			return "Radii";
		}
		else
		{
			return className+"s";
		}
	}

	
	
	/**
	 * Remove the specified node from the tree
	 * 
	 * @param node
	 */
	@SuppressWarnings("rawtypes")
	protected void deleteEntity(DefaultMutableTreeNode node)
	{
		ITridas entity = null;
		EntityResource rsrc = null;
		String entityType = "";
		
		if(node.getUserObject() instanceof TridasObject)
		{
			entityType = "Object";
			entity = (TridasObject) node.getUserObject();
			rsrc = new EntityResource<TridasObject>(entity, TellervoRequestType.DELETE, TridasObject.class);
		}
		else if(node.getUserObject() instanceof TridasElement)
		{
			entityType = "Element";
			entity = (TridasElement) node.getUserObject();
			rsrc = new EntityResource<TridasElement>(entity, TellervoRequestType.DELETE, TridasElement.class);
		}
		else if(node.getUserObject() instanceof TridasSample)
		{
			entityType = "Sample";
			entity = (TridasSample) node.getUserObject();
			rsrc = new EntityResource<TridasSample>(entity, TellervoRequestType.DELETE, TridasSample.class);
		}
		else if(node.getUserObject() instanceof TridasRadius)
		{
			entityType = "Radius";
			entity = (TridasRadius) node.getUserObject();
			rsrc = new EntityResource<TridasRadius>(entity, TellervoRequestType.DELETE, TridasRadius.class);
		}
		else
		{
			return;
		}
			    			
		// Do query
		TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(rsrc);
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
			DefaultMutableTreeNode node = ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
			this.refreshNode(node);
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
	
	public void refreshNode(DefaultMutableTreeNode node)
	{
		// Remove all children then add again
		node.removeAllChildren();
		node.removeAllChildren();
		//((DefaultTreeModel)tree.getModel()).reload();
		expandEntity(node);
		((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
	}
	
	@Override
	public void entitySelected(TridasSelectEvent event) {
		List<? extends ITridas> entities = event.getEntityList();
		
		if(entities==null)
		{
			return;
		}

		ITridas entity;
		try{
			entity = entities.get(0);
		} catch (IndexOutOfBoundsException e)
		{
			Alert.message("No matches", "No matches for this code");
			return;
		}
		
		
		if(entity instanceof TridasObject)
		{
			TridasObjectEx object = (TridasObjectEx) entity;
					
			
			TreePath path = tree.getNextMatch(GenericFieldUtils.findField(object, "tellervo.objectLabCode").getValue().toString(), 0, Position.Bias.Forward);
			tree.setSelectionPath(path);
			tree.scrollPathToVisible(path);
			
			this.fireTridasSelectListener(event);
		}
		else if (entity instanceof ITridas)
		{
			this.fireTridasSelectListener(event);
		}
		
		
	}
	
	

}
