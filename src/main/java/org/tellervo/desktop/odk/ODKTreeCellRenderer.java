package org.tellervo.desktop.odk;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tellervo.desktop.ui.Builder;

public class ODKTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = 1L;


	public Component getTreeCellRendererComponent(JTree tree, Object item,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, item, sel, expanded, leaf,
				row, hasFocus);
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) item;
		
		if(!(node.getUserObject() instanceof ODKFieldInterface))
		{
			setIcon(Builder.getIcon("folder.png", 16));
			return this;
		}
		
		ODKFieldInterface value = (ODKFieldInterface) node.getUserObject();
	        
	        if(value.isFieldRequired())
	        {
	        	if(value.isFieldHidden())
	        	{
	        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i>[required and hidden]</i></font>");
	        	}
	        	else
	        	{
	        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i>[required]</i></font>");
	        	}
	        }
	        else
	        {
	        	if(value.isFieldHidden())
	        	{
	        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i>[hidden]</i></font>");
	        	}
	        	else
	        	{
	        		setText("<html>"+value.getFieldName()+" <font size=\"-2\"><i></i></font>");
	        	}
	        }
	        
	        if(value.getFieldType().equals(ODKDataType.AUDIO))
	        {
	        	setIcon(Builder.getIcon("sound.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.IMAGE))
	        {
	        	setIcon(Builder.getIcon("photo.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.VIDEO))
	        {
	        	setIcon(Builder.getIcon("movie.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.STRING))
	        {
	        	setIcon(Builder.getIcon("letters.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.SELECT_MANY) || 
	        		value.getFieldType().equals(ODKDataType.SELECT_ONE))
	        {
	        	setIcon(Builder.getIcon("list.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.INTEGER) ||
	        		value.getFieldType().equals(ODKDataType.DECIMAL))
	        {
	        	setIcon(Builder.getIcon("numbers.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.LOCATION))
	        {
	        	setIcon(Builder.getIcon("pin.png", 16));
	        }
	        else if(value.getFieldType().equals(ODKDataType.DATE)||
	        		value.getFieldType().equals(ODKDataType.DATE_TIME))
	        {
	        	setIcon(Builder.getIcon("calendar.png", 16));
	        }
	        else
	        {
	        	setIcon(null);
	        }
	        return this;

	}

}
