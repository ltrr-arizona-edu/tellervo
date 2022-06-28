package org.tellervo.desktop.odk;

import org.tellervo.desktop.odk.fields.ODKFieldInterface;

public class ODKFieldNode extends AbstractODKTreeNode {

	private static final long serialVersionUID = 1L;
	private boolean isMoveable;
	
	public ODKFieldNode(ODKFieldInterface field)
	{
		this.setUserObject(field);
		this.allowsChildren=false;
	}
	
	public boolean isMoveable() {
		return isMoveable;
	}

	public void setMoveable(boolean isMoveable) {
		this.isMoveable = isMoveable;
	}


	
}
