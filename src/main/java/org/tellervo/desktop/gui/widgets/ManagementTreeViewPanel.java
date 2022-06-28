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

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.admin.view.PermissionByEntityDialog;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.hierarchy.AddRemoveWSITagDialog;
import org.tellervo.desktop.gui.hierarchy.TridasTree;
import org.tellervo.desktop.gui.hierarchy.TridasTreeCellRenderer;
import org.tellervo.desktop.gui.hierarchy.WSITagNameDialog;
import org.tellervo.desktop.gui.widgets.TellervoCodePanel.ObjectListMode;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.schema.EntityType;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.wsi.tellervo.TellervoResourceAccessDialog;
import org.tellervo.desktop.wsi.tellervo.resources.EntityResource;
import org.tridas.interfaces.ITridas;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.util.TridasUtils.TreeDepth;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;


/**
 * Extension of the standard TridasTreeViewPanel with the addition of 
 * a reassign right click menu for moving entities to different parents
 * 
 * @author peterbrewer
 *
 */
public class ManagementTreeViewPanel extends TridasTreeViewPanel implements KeyListener {

	private final static Logger log = LoggerFactory.getLogger(ManagementTreeViewPanel.class);
	protected ObjectListMode baseObjectListMode = ObjectListMode.TOP_LEVEL_ONLY;
	
	
	private static final long serialVersionUID = -7973400038586992025L;
    private Window parent;
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
	public ManagementTreeViewPanel()
	{
		super(false);
		setupMultiTree(null);
		super.tree.addKeyListener(this);	
		
	}
	
	/**
	 * Complete constructor for tree view panel.  
	 * 
	 * @param depth - @see #setTreeDepth(TreeDepth)
	 * @param listenersAreCheap - @see #setListenersAreCheap(Boolean)
	 * @param textForSelectPopup - @see #setTextForSelectPopup(String)
	 */
	public ManagementTreeViewPanel(Window parent, TreeDepth depth, Boolean listenersAreCheap, String textForSelectPopup)
	{
		super(false);
		setupMultiTree(null);
		setTreeDepth(depth);
		setListenersAreCheap(listenersAreCheap);
		setTextForSelectPopup(textForSelectPopup);
		this.parent = parent;
		super.tree.addKeyListener(this);
	}
	
	
	protected JPopupMenu initMultiSelectPopupMenu(Class<?> clazz, Boolean expandEnabled, Boolean ismulti)
	{
		String className = ManagementTreeViewPanel.getFriendlyClassName(clazz);
		Boolean isTridas = false;
		if(clazz.getSimpleName().startsWith("Tridas"))
		{
			isTridas = true;
		}
		
        // define the popup
        JPopupMenu popup = new JPopupMenu();
        JMenuItem menuItem;
        
        if(isTridas)
        {
		  	Boolean adm = App.isAdmin;
        	
	        // Expand 
	        menuItem = new JMenuItem("Expand branch");
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("expand");
	        menuItem.setIcon(Builder.getIcon("view_tree.png", 16));
	        menuItem.setEnabled(expandEnabled && !ismulti);
	        popup.add(menuItem);
	       	   
	        
	        // Select
	        menuItem = new JMenuItem(this.textForSelectPopup);
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("select");
	        menuItem.setIcon(Builder.getIcon("select.png", 16));
	        menuItem.setEnabled(!ismulti);
	        popup.add(menuItem);
	        popup.addSeparator();
	        
	        // Delete
	        menuItem = new JMenuItem("Delete this "+className.toLowerCase());
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("delete");
	        menuItem.setIcon(Builder.getIcon("cancel.png", 16));
	        menuItem.setEnabled(!ismulti);
	        popup.add(menuItem);
	        

	        
	        // Reassign
	        menuItem = new JMenuItem("Reassign to another parent");
	        menuItem.addActionListener(this);
	        menuItem.setEnabled(adm);
	        menuItem.setIcon(Builder.getIcon("newparent.png", 16));
	        popup.add(menuItem);
		  	if(ismulti)
		  	{
		        menuItem.setActionCommand("reassignmulti");
		  	}
		  	else
		  	{
		        menuItem.setActionCommand("reassign");
		  	}

		  	
	        if(clazz.equals(TridasMeasurementSeries.class) || clazz.equals(TridasDerivedSeries.class))
	        {
	        	// Agg tag options

	    		// Tag series 
	    		menuItem = new JMenuItem("Tag this series");
	    		menuItem.setIcon(Builder.getIcon("tag.png", 16));
	    		menuItem.setActionCommand("tagSeries");
	    		menuItem.addActionListener(this);
	    		popup.add(menuItem);	
	    		
	    		// Add/remove tag 
	    		menuItem = new JMenuItem("Add/remove tag(s) from series");
	    		menuItem.setIcon(Builder.getIcon("tags.png", 16));
	    		menuItem.setActionCommand("addRemoveTag");
	    		menuItem.addActionListener(this);
	    		popup.add(menuItem);	
	    		
	    		popup.addSeparator();
	        }
	        
	        // Merge
	        menuItem = new JMenuItem("Merge with another record");
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("merge");
	        menuItem.setEnabled(adm && !ismulti);
	        menuItem.setIcon(Builder.getIcon("merge.png", 16));
	        popup.add(menuItem);

	        popup.addSeparator();   
	        
	        menuItem = new JMenuItem("View permissions");
	        menuItem.addActionListener(this);
	        menuItem.setActionCommand("permissions");
	        menuItem.setEnabled(adm && !ismulti);
	        menuItem.setIcon(Builder.getIcon("trafficlight.png", 16));
	        popup.add(menuItem);
	        
	        popup.addSeparator();  
	        
        }
        
        // Open series
        if(clazz.equals(TridasMeasurementSeries.class) || clazz.equals(TridasDerivedSeries.class))
        {
            menuItem = new JMenuItem("Open");
            menuItem.setIcon(Builder.getIcon("open.png", 16));
            menuItem.setActionCommand("openSeries");
            menuItem.addActionListener(this);
            popup.add(menuItem);
            
            menuItem = new JMenuItem("Chart series");
            menuItem.setIcon(Builder.getIcon("graph.png", 16));
            menuItem.setActionCommand("chartSeries");
            menuItem.addActionListener(this);
            popup.add(menuItem);
            
            popup.addSeparator();
        }
        
        
        // Refresh
        menuItem = new JMenuItem("Refresh");
        menuItem.addActionListener(this);
        menuItem.setActionCommand("refresh");
        menuItem.setIcon(Builder.getIcon("reload.png", 16));
        menuItem.setEnabled(!ismulti);
        popup.add(menuItem);
        
        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(false);
		return popup;
	}
			
	
	/**
	 * Set up the tree.  If objectList is passed then this is used
	 * as the base nodes of the tree, otherwise the objects specified
	 * by baseObjectListMode are used.
	 * 
	 * @param objList
	 */
	private void setupMultiTree(List<TridasObjectEx> objList)
	{
		log.debug("Setting up multi-tree");
		
		// Set up tree
    	top = new DefaultMutableTreeNode(App.getLabName()+" Database");
    	if(objList!=null)
    	{
    		addObjectsToTree(this.tree, objList);
    	}
    	else
    	{
    		addObjectsToTree(this.tree);
    	}
    	tree = new TridasTree(top);
    	tree.setCellRenderer(new TridasTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);        
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
		log.debug("Adding mouse listener...");

        tree.addMouseListener(new PopupListener() {
			
        	
        	@Override
        	public void mouseClicked(MouseEvent e) { 
				
        		String modifiers = MouseEvent.getMouseModifiersText(e.getModifiers());
        		if(modifiers.contains("Shift") || modifiers.contains("Ctrl"))
        		{
        			// User is selecting multiple nodes so we ignore 
        			return;
        		}
        		
        		try{
        			if(tree.getSelectionPaths().length>1)
	        		{
	        			// User is part way through selecting multiple nodes so ignore
	        			return;
	        		}
        		} catch (Exception e2)
        		{
        			return;
        		}
        		
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
		
				TreePath[] selPaths = tree.getSelectionModel().getSelectionPaths();

		        ArrayList<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
		        for(TreePath tp : selPaths)
		        {
		        	DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
		        	nodeList.add(node);
		        }
		        	
		        log.debug("Number of nodes in list: "+nodeList.size());
		        
		        if(nodeList.size()==1)
		        {
		        	// Same as original code as only 1 thing is picked
		        	
		        	DefaultMutableTreeNode node = nodeList.get(0);
		        	
		        	// Only enabled the expand option if we're not too deep.
		        	if( ((node.getUserObject() instanceof TridasObject)  && (depth.getDepth()<= TreeDepth.OBJECT .getDepth())) || 
		        		((node.getUserObject() instanceof TridasElement) && (depth.getDepth()<= TreeDepth.ELEMENT.getDepth())) ||
		        		((node.getUserObject() instanceof TridasSample)  && (depth.getDepth()<= TreeDepth.SAMPLE .getDepth())) ||
		        		((node.getUserObject() instanceof TridasRadius)  && (depth.getDepth()<= TreeDepth.RADIUS .getDepth())) ||
		        		((node.getUserObject() instanceof TridasElement) && (depth.getDepth()<= TreeDepth.ELEMENT.getDepth())) 
		        	   )
		        	{
		        		showMultiPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), node.getUserObject().getClass(), false, false);
		        	}
		        	else
		        	{
		        		// Show menu with expand branch enabled
		        		showMultiPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), node.getUserObject().getClass(), true, false);
		        	}
		        }
		        else if (nodeList.size()==0)
		        {
		        	return;
		        }
		        else
		        {
		        	Class clazz = nodeList.get(0).getUserObject().getClass();
		        	
		        	for(DefaultMutableTreeNode nde: nodeList)
		        	{
		        		if(!nde.getUserObject().getClass().equals(clazz))
		        		{
		        			// different classes so can't do anything
		        			log.debug("Cant handle nodes of different classes");
		            		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		    				tree.setSelectionPath(selPath);
		        			return;
		        		}
		        	}
		        	
	        		showMultiPopupMenu((JComponent) e.getSource(), e.getX(), e.getY(), nodeList.get(0).getUserObject().getClass(), false, true);

		        }
		        	
				
				
			}
        });
        
        ToolTipManager.sharedInstance().registerComponent(tree);
    	treeScrollPane.setViewportView(tree);
    	
    	log.debug("Finished setting up multi-tree");
	}
	
	/**
	 * Display a popup menu with the expand button enabled or disabled
	 * 
	 * @param source
	 * @param x
	 * @param y
	 * @param expandEnabled
	 */
	void showMultiPopupMenu(JComponent source, int x, int y, Class<?> clazz, boolean expandEnabled, boolean ismulti)
	{
		
		JPopupMenu popupMenu = initMultiSelectPopupMenu(clazz, expandEnabled, ismulti);

		popupMenu.show(source, x, y);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Get the currently selected node
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent();
		
		
		if(e.getActionCommand().equals("expand"))
		{
			// Request to expand the current node of tree
			expandEntity(selectedNode);
		}
		else if (e.getActionCommand().equals("select"))
		{
			doSelectEntity(selectedNode);
		}
		else if (e.getActionCommand().equals("refresh"))
		{
			this.refreshNode(selectedNode);
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
				deleteEntity(selectedNode);
			}			
		}
		else if (e.getActionCommand().equals("tagSeries"))
		{
			ITridasSeries series = (ITridasSeries)selectedNode.getUserObject();
			WSITagNameDialog.addTagToSeries(null, series);
		}
		else if (e.getActionCommand().equals("addRemoveTag"))
		{
			ITridasSeries series = (ITridasSeries)selectedNode.getUserObject();
			AddRemoveWSITagDialog.showDialog(null, series);
		}
		else if (e.getActionCommand().equals("reassign"))
		{
			Object[] options = {"OK",
            "Cancel"};
			int ret = JOptionPane.showOptionDialog(getParent(), 
					"Are you sure you want to move this to another parent?\n"+
					"Changes will also impact entities subordinate to this one\n"+
					"so only continue if you know what you're doing!", 
					"Confirm move", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			if(ret != JOptionPane.YES_OPTION)
			{
				return;
			}	
			
			ITridas selected = (ITridas) selectedNode.getUserObject();
			Class<? extends ITridas> expectedClass = ITridas.class;
			
			if((selected.getClass().equals(TridasMeasurementSeries.class)) || 
					(selected.getClass().equals(TridasDerivedSeries.class)))
			{
				expectedClass = TridasRadius.class;
			}
			else if (selected.getClass().equals(TridasRadius.class))
			{
				expectedClass = TridasSample.class;
			}
			else if (selected.getClass().equals(TridasSample.class))
			{
				expectedClass = TridasElement.class;
			}
			else if ((selected.getClass().equals(TridasElement.class)) || 
					(selected.getClass().equals(TridasObject.class)) || 
					(selected.getClass().equals(TridasObjectEx.class)))
			{
				expectedClass = TridasObject.class;
			}
			
			
			
			ITridas newParent = TridasEntityPickerDialog.pickEntity(parent, 
					"Select new parent", 
					expectedClass, 
					EntitiesAccepted.SPECIFIED_ENTITY_ONLY);

			// Actually do the reassign
			reassignEntity(selectedNode, newParent);
			
			
		}
		
		else if (e.getActionCommand().equals("reassignmulti"))
		{
			Object[] options = {"OK",
            "Cancel"};
			int ret = JOptionPane.showOptionDialog(getParent(), 
					"Are you sure you want to move these to another parent?\n"+
					"Changes will also impact all subordinate entities so \n"+
					"only continue if you know what you're doing!", 
					"Confirm move", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			if(ret != JOptionPane.YES_OPTION)
			{
				return;
			}	
			
			
			TreePath[] selPaths = tree.getSelectionModel().getSelectionPaths();

	        ArrayList<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
	        ArrayList<ITridas> tridasList = new ArrayList<ITridas>();

	        for(TreePath tp : selPaths)
	        {
	        	nodeList.add(selectedNode);
	        	tridasList.add((ITridas) selectedNode.getUserObject());
	        }
			
			ITridas selected = tridasList.get(0);

	        
			Class<? extends ITridas> expectedClass = ITridas.class;
			
			if((selected.getClass().equals(TridasMeasurementSeries.class)) || 
					(selected.getClass().equals(TridasDerivedSeries.class)))
			{
				expectedClass = TridasRadius.class;
			}
			else if (selected.getClass().equals(TridasRadius.class))
			{
				expectedClass = TridasSample.class;
			}
			else if (selected.getClass().equals(TridasSample.class))
			{
				expectedClass = TridasElement.class;
			}
			else if ((selected.getClass().equals(TridasElement.class)) || 
					(selected.getClass().equals(TridasObject.class)) || 
					(selected.getClass().equals(TridasObjectEx.class)))
			{
				expectedClass = TridasObject.class;
			}
			
			
			
			ITridas newParent = TridasEntityPickerDialog.pickEntity(parent, 
					"Select new parent", 
					expectedClass, 
					EntitiesAccepted.SPECIFIED_ENTITY_ONLY);

			// Actually do the reassign	
			for(int i=0; i<nodeList.size(); i++)
			{
				DefaultMutableTreeNode node = nodeList.get(i);
				reassignEntity(node, newParent, i, nodeList.size());
			}
			
		}
		else if (e.getActionCommand().equals("openSeries"))
		{
			openSeries();

		}
		else if (e.getActionCommand().equals("chartSeries"))
		{
			chartSeries();

		}
		else if (e.getActionCommand().equals("merge"))
		{
			Object[] options = {"OK",
            "Cancel"};
			int ret = JOptionPane.showOptionDialog(getParent(), 
					"Are you sure you want to merge this with another record?\n"+
					"Changes will also impact entities subordinate to this one\n"+
					"so only continue if you know what you're doing!", 
					"Confirm merge", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.WARNING_MESSAGE, null, options, options[1]);
			
			if(ret != JOptionPane.YES_OPTION)
			{
				return;
			}	
			
			ITridas selected = (ITridas) selectedNode.getUserObject();
			Class<? extends ITridas> expectedClass = selected.getClass();
						
			ITridas correctEntity = TridasEntityPickerDialog.pickEntity(parent, 
					"Select correct entity", 
					expectedClass, 
					EntitiesAccepted.SPECIFIED_ENTITY_ONLY);

			// Actually do the merge
			mergeEntity(selectedNode, correctEntity);
			
			
		}
		
		else if (e.getActionCommand().equals("permissions"))
		{
			ITridas selected = (ITridas) selectedNode.getUserObject();

			PermissionByEntityDialog.showDialog(selected);
		}
	}
	
	private void reassignEntity(DefaultMutableTreeNode node, ITridas newParent)
	{
		reassignEntity(node, newParent, null, null);
	}

	private void reassignEntity(DefaultMutableTreeNode node, ITridas newParent, Integer currentProgress, Integer totalProgress)
	{	
		ITridas entity = null;
		EntityResource rsrc = null;

		if(newParent==null)
		{
			return;
		}
		
		String newParentEntityID = newParent.getIdentifier().getValue();
		
		if(node.getUserObject() instanceof TridasMeasurementSeries)
		{
			entity = (TridasMeasurementSeries) node.getUserObject();
			rsrc = new EntityResource<TridasMeasurementSeries>(entity, newParentEntityID, TridasMeasurementSeries.class);
			
		}
		else if(node.getUserObject() instanceof TridasRadius)
		{
			entity = (TridasRadius) node.getUserObject();
			rsrc = new EntityResource<TridasRadius>(entity, newParentEntityID, TridasRadius.class);
			
		}
		else if(node.getUserObject() instanceof TridasSample)
		{
			entity = (TridasSample) node.getUserObject();
			rsrc = new EntityResource<TridasSample>(entity, newParentEntityID, TridasSample.class);	
		}
		else if(node.getUserObject() instanceof TridasElement)
		{
			entity = (TridasElement) node.getUserObject();
			rsrc = new EntityResource<TridasElement>(entity, newParentEntityID, TridasElement.class);	
		}
		else if(node.getUserObject() instanceof TridasObject)
		{
			Alert.message("Not implemented", "Moving sub-objects is not yet supported");
			/*entity = (TridasObject) node.getUserObject();
			if(newParentEntityID.equals(entity.getIdentifier().getValue()))
			{
				Alert.message("Invalid", "You can't move and object into itself!");
				return;
			}
			rsrc = new EntityResource<TridasObject>(entity, newParentEntityID, TridasObject.class);*/				
		}
		else
		{
			Alert.message("Not implemented", "You shouldn't have been able to get here!");
		}
		
		// Do query
		TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(parent, rsrc, currentProgress, totalProgress);
		rsrc.query();
		accdialog.setVisible(true);
		
		if(accdialog.isSuccessful())
		{
			rsrc.getAssociatedResult();
			((DefaultTreeModel)tree.getModel()).removeNodeFromParent(node);
			return;
		}

		
		Exception exception = accdialog.getFailException();
		
		if(exception.getLocalizedMessage().contains("duplicate key value"))
		{
			JOptionPane.showMessageDialog(this, "Operation aborted:\nReassigning to this parent would result in "
					+getFriendlyPluralClassName(node.getUserObject().getClass()).toLowerCase()+" with duplicate titles.", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		else
		{
			JOptionPane.showMessageDialog(this, "There was a problem reassigning this entity\n"+exception, 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		
		return;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	private void mergeEntity(DefaultMutableTreeNode node, ITridas correctEntity)
	{
		ITridas entity = null;
		EntityResource rsrc = null;

		if(correctEntity==null)
		{
			return;
		}
		
		String correctEntityID = correctEntity.getIdentifier().getValue();
		
		if(node.getUserObject() instanceof TridasMeasurementSeries)
		{
			entity = (TridasMeasurementSeries) node.getUserObject();
			rsrc = new EntityResource<TridasMeasurementSeries>(EntityType.MEASUREMENT_SERIES, 
					TridasMeasurementSeries.class, entity, correctEntityID );
			
		}
		else if(node.getUserObject() instanceof TridasRadius)
		{
			entity = (TridasRadius) node.getUserObject();
			rsrc = new EntityResource<TridasRadius>(EntityType.RADIUS,
					TridasRadius.class, entity, correctEntityID);
			
		}
		else if(node.getUserObject() instanceof TridasSample)
		{
			entity = (TridasSample) node.getUserObject();
			rsrc = new EntityResource<TridasSample>(EntityType.SAMPLE,
					TridasSample.class, entity, correctEntityID);	
		}
		else if(node.getUserObject() instanceof TridasElement)
		{
			entity = (TridasElement) node.getUserObject();
			rsrc = new EntityResource<TridasElement>(EntityType.ELEMENT,
					TridasElement.class, entity, correctEntityID);	
		}
		else if(node.getUserObject() instanceof TridasObject)
		{
			entity = (TridasObject) node.getUserObject();
			rsrc = new EntityResource<TridasObject>(EntityType.OBJECT,
					TridasObject.class, entity, correctEntityID);	
		}
		else
		{
			Alert.message("Not implemented", "You shouldn't have been able to get here!");
		}
		
		// Do query
		TellervoResourceAccessDialog accdialog = new TellervoResourceAccessDialog(rsrc);
		rsrc.query();
		accdialog.setVisible(true);
		
		if(accdialog.isSuccessful())
		{
			rsrc.getAssociatedResult();
			((DefaultTreeModel)tree.getModel()).removeNodeFromParent(node);
			return;
		}

		
		Exception exception = accdialog.getFailException();
		

		JOptionPane.showMessageDialog(this, "There was a problem merging entities\n"+exception, 
				"Error", JOptionPane.ERROR_MESSAGE);
		
		
		return;
		
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if(event.getKeyCode()==KeyEvent.VK_F5)
		{
			DefaultMutableTreeNode node = ((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
			this.refreshNode(node);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
		
}
