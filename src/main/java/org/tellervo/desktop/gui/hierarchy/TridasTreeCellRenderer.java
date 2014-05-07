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
package org.tellervo.desktop.gui.hierarchy;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.tellervo.desktop.ui.Builder;
import org.tellervo.schema.WSITag;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;

import sun.swing.DefaultLookup;

public class TridasTreeCellRenderer extends DefaultTreeCellRenderer{

	private static final long serialVersionUID = -3507013809012231633L;

	JTree tree;
	Icon databaseIcon;
	Icon objectIcon;
	Icon elementIcon;
	Icon sampleIcon;
	Icon radiusIcon;
	Icon mseriesIcon;
	Icon dseriesIcon;
	Icon tagIcon;
	Boolean isDropCell = false;

	
	public TridasTreeCellRenderer()
	{
		databaseIcon = ((ImageIcon) Builder.getIcon("database.png", Builder.ICONS, 16));
		objectIcon   = ((ImageIcon) Builder.getIcon("object.png",   Builder.ICONS, 16));
		elementIcon  = ((ImageIcon) Builder.getIcon("element.png",  Builder.ICONS, 16));
		sampleIcon   = ((ImageIcon) Builder.getIcon("sample.png",   Builder.ICONS, 16));
		radiusIcon   = ((ImageIcon) Builder.getIcon("radius.png",   Builder.ICONS, 16));
		mseriesIcon  = ((ImageIcon) Builder.getIcon("measurementseries.png",   Builder.ICONS, 16));
		dseriesIcon  = ((ImageIcon) Builder.getIcon("derivedseries.png",   Builder.ICONS, 16));
		tagIcon      = ((ImageIcon) Builder.getIcon("tag.png",   Builder.ICONS, 16));

	}
		
	protected String getToolTipString(Object value)
	{
		if(value instanceof DefaultMutableTreeNode)
		{
			if(((DefaultMutableTreeNode)value).getUserObject() instanceof ITridas)
			{
			//TODO Return labcode as tooltip text	
			}
		}
		return null;

	}
	
    public Component getTreeCellRendererComponent(JTree tree, Object value,
					  boolean sel,
					  boolean expanded,
					  boolean leaf, int row,
					  boolean hasFocus) 
    {
		String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);		
		
		this.tree = (JTree) tree;
		this.hasFocus = hasFocus;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		setText(stringValue);
		setToolTipText(this.getToolTipString(value));		

		
		Color fg = null;
		isDropCell = false;
		
		JTree.DropLocation dropLocation = tree.getDropLocation();
		if (dropLocation != null
		  && dropLocation.getChildIndex() == -1
		  && tree.getRowForPath(dropLocation.getPath()) == row) 
		{
		
			Color col = DefaultLookup.getColor(this, ui, "Tree.dropCellForeground");
			if (col != null) 
			{
			  fg = col;
			} 
			else 
			{
			  fg = getTextSelectionColor();
			}
		
			isDropCell = true;
		} 
		else if (sel) 
		{
			fg = getTextSelectionColor();
		} 
		else 
		{
			fg = getTextNonSelectionColor();
		}
		
		setForeground(fg);
		
		Icon icon = null;
		if(node.getUserObject() instanceof TridasObjectEx)
		{
			icon = objectIcon;
		}
		else if (node.getUserObject() instanceof TridasElement)
		{
			icon = elementIcon;
		}
		else if (node.getUserObject() instanceof TridasSample)
		{
			icon = sampleIcon;
		}
		else if (node.getUserObject() instanceof TridasRadius)
		{
			icon = radiusIcon;
		}
		else if (node.getUserObject() instanceof TridasMeasurementSeries)
		{
			icon = mseriesIcon;
		}
		else if (node.getUserObject() instanceof TridasDerivedSeries)
		{
			icon = dseriesIcon;
		}
		else if (node.getUserObject() instanceof WSITag)
		{
			icon = tagIcon;
		}
		else
		{
			icon = databaseIcon;
		}
		
		
		
		if (!tree.isEnabled()) {
			setEnabled(false);
			LookAndFeel laf = UIManager.getLookAndFeel();
			Icon disabledIcon = laf.getDisabledIcon(tree, icon);
			if (disabledIcon != null) icon = disabledIcon;
				setDisabledIcon(icon);
			} 
			else 
			{
				setEnabled(true);
				setIcon(icon);
			}
			setComponentOrientation(tree.getComponentOrientation());
			
			selected = sel;
		
			
			return this;
		}
}
