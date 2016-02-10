package org.tellervo.desktop.odk;

import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.odk.fields.AbstractODKField;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class ODKTreeModel extends DefaultTreeModel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ODKFormDesignPanel.class);
	
	private DefaultMutableTreeNode sampleGroup ;
	private final Class<? extends ITridas> classType;
	
	public ODKTreeModel(DefaultMutableTreeNode root, Class<? extends ITridas> classtype) {
		super(root);
		this.classType = classtype;

		if(classType==TridasSample.class)
		{
			DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode) root;
			
			sampleGroup = new ODKBranchNode(false, "Samples", TridasSample.class);
			rootnode.add(sampleGroup);
			 
		}
	}
	
	public Class<? extends ITridas> getClassType()
	{
		return classType;
	}
	
	@Override
	public void insertNodeInto(MutableTreeNode newChild, MutableTreeNode parent, int index)
	{
		
		
		if(index==-1) index = 0;
		
		log.debug("Inserting new child in parent node at index "+index);
		
		try{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) newChild;
			
			ODKFieldInterface obj = (ODKFieldInterface) node.getUserObject();
			
			if(obj.getTridasClass() == TridasSample.class && parent!=sampleGroup)
			{
				super.insertNodeInto(newChild, sampleGroup, sampleGroup.getChildCount());
				return;
			}
		} catch (Exception e)
		{
			log.debug("exception caught");
			
		}
		
		super.insertNodeInto(newChild, parent, index);
	}
	
	public void addNodeToRoot(ODKFieldInterface newChild)
	{
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(newChild);
		DefaultMutableTreeNode rootnode = (DefaultMutableTreeNode) this.getRoot();
		insertNodeInto(childNode, rootnode, rootnode.getChildCount());
	}
	
	public void moveFieldUp(DefaultMutableTreeNode node)
	{
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		int index = parent.getIndex(node)-1;		
		this.removeNodeFromParent(node);
		this.insertNodeInto(node, parent, index);
	}

	public void moveFieldDown(DefaultMutableTreeNode node)
	{
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		int index = parent.getIndex(node)+1;		
		this.removeNodeFromParent(node);
		this.insertNodeInto(node, parent, index);
	
	}
	
	public ArrayList<ODKFieldInterface> getArrayOfFields()
	{
		ArrayList<ODKFieldInterface> fields = new ArrayList<ODKFieldInterface>();
		

	}
	
}
