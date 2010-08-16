package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasGenericField;
import org.tridas.util.TridasObjectEx;

public class TridasTree extends JTree {

	private static final long serialVersionUID = 5358870985497921555L;

	
	public TridasTree(DefaultMutableTreeNode top) {
		super(top);
	}
 
	
	public String getToolTipString(Object value, boolean selected,
			boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
	
		return "blah";
	
		
	}
	
	
	@Override
	public String convertValueToText(Object value, boolean selected,
		boolean expanded, boolean leaf, int row,
		boolean hasFocus) {
		
		if(value == null) return "";
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

		if(node.getUserObject() instanceof TridasObjectEx)
		{
			TridasObjectEx obj = ((TridasObjectEx)node.getUserObject());
			for(TridasGenericField gf : obj.getGenericFields())
			{
				if (gf.getName().equals("corina.objectLabCode"))
				{
					return gf.getValue() + " - "+obj.getTitle();
				}
			}
			return obj.getTitle();
		}
		else if(node.getUserObject() instanceof ITridas)
		{
		    return ((ITridas)node.getUserObject()).getTitle();
		}
		else 
		{
			return node.getUserObject().toString();
		}
		    

	}



}
