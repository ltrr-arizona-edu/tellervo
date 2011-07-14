package edu.cornell.dendro.corina.io.command;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.netbeans.swing.outline.Outline;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.ExpandImportTreeEvent;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;

public class ExpandImportTreeCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		ExpandImportTreeEvent event = (ExpandImportTreeEvent) argEvent;
		expandTree(event.expand, event.model, event.treeTable);

	}

    private void expandTree(Boolean expands, TridasRepresentationTreeModel model, Outline treeTable) {
        TreeNode roots = (TreeNode) model.getRoot();
        expandAll(new TreePath(roots), expands, treeTable);
    }
	    
    private void expandAll(TreePath path, boolean expands, Outline treeTable) {
        TreeNode node = (TreeNode) path.getLastPathComponent();
 
       if (node.getChildCount() >= 0) {
            Enumeration enumeration = node.children();
           while (enumeration.hasMoreElements()) {
                TreeNode ns = (TreeNode) enumeration.nextElement();
                TreePath ps = path.pathByAddingChild(ns);
 
                expandAll(ps, expands, treeTable);
            }
        }
 
        if (expands) {
        	treeTable.expandPath(path);
        } else {
        	treeTable.collapsePath(path);
        }
    } 

}
