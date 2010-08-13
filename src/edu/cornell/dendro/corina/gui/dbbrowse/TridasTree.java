package edu.cornell.dendro.corina.gui.dbbrowse;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasGenericField;
import org.tridas.util.TridasObjectEx;

public class TridasTree extends JTree {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 5358870985497921555L;

	public TridasTree(DefaultMutableTreeNode top) {
		super(top);
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
