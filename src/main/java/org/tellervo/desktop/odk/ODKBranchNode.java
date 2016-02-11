package org.tellervo.desktop.odk;

import javax.swing.tree.MutableTreeNode;

import org.tridas.interfaces.ITridas;

public class ODKBranchNode extends AbstractODKTreeNode {

	private static final long serialVersionUID = 1L;
	private final boolean canDelete;
	private final boolean repeats;

	private final Class<? extends ITridas> childClassType;
	
	
	public ODKBranchNode(boolean canDelete, boolean isRepeatable, String name, Class<? extends ITridas> childClassType)
	{
		this.canDelete = canDelete;
		this.childClassType = childClassType;
		this.allowsChildren=true;
		this.repeats = isRepeatable;
		
		this.setUserObject(name);
	}
	
	public boolean isDeletable()
	{
		
		return canDelete;
	}
	
	public boolean isRepeatable()
	{
		return repeats;
	}
	
	@Override
	public void add(MutableTreeNode newChild)
	{
		if(newChild.getClass() == childClassType)
		{
			// Add to node
		}
		else
		{
			// Not acceptable - don't add
			return;
			
		}
	}
	
}
