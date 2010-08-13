package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.tridas.interfaces.ITridas;
import org.tridas.util.TridasObjectEx;

import edu.cornell.dendro.corina.core.App;

public class TridasTreeViewPanel extends TridasTreeViewPanel_UI implements MouseListener {

	private static final long serialVersionUID = 1185669228536105855L;
	TridasTree tree;
	DefaultTreeModel treeModel;
	
	public TridasTreeViewPanel()
	{
    	DefaultMutableTreeNode top = new DefaultMutableTreeNode("Corina Database");
    	createNodes(top);
    	tree = new TridasTree(top);
    	tree.setCellRenderer(new TridasTreeCellRenderer());
    	//treeModel = new DefaultTreeModel(top);
    	//treeModel.addTreeModelListener(new TridasTreeModelListener());

    	//Listen for when the selection changes.
        tree.addMouseListener(this);


        treeScrollPane.setViewportView(tree);

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        

	}
	
    @SuppressWarnings("unchecked")
	private void createNodes(DefaultMutableTreeNode top)
    {
    	DefaultMutableTreeNode objectNode = null;

        List<TridasObjectEx> objectList = App.tridasObjects.getObjectList();
        for(TridasObjectEx object : objectList)
        {
            objectNode = new DefaultMutableTreeNode(object);
         
            top.add(objectNode);
        }
        

    }

    public void expandEntity(ITridas entity)
    {
    	System.out.println("Selected "+entity.getTitle());
    	DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();

    	
    }
    
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Mouse clicked");
		TreePath path = tree.getSelectionPath();
		DefaultMutableTreeNode sel =  (DefaultMutableTreeNode) path.getLastPathComponent();
		
		if(sel.getUserObject() instanceof ITridas)
		{
			expandEntity((ITridas) sel.getUserObject());
			
		}
		
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	
}
