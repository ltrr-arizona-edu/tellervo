/**
 * 
 */
package org.tellervo.desktop.editor.support;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A simple class that allows you to modify the components generated by a base table cell renderer
 * 
 * @author Lucas Madar
 *
 */
public class ModifiableTableCellRenderer implements TableCellRenderer {
	private final TableCellRenderer baseRenderer;
	private final List<TableCellModifier> modifiers;
	
	public ModifiableTableCellRenderer(TableCellRenderer baseRenderer) {
		this.baseRenderer = baseRenderer;
		
		modifiers = new ArrayList<TableCellModifier>();		
	}

	/**
	 * Add a modifier to this table cell renderer
	 * @param modifier
	 */
	public void addModifier(TableCellModifier modifier) {
		modifiers.add(modifier);
	}

	/**
	 * Remove a modifier from this table cell renderer
	 * @param modifier
	 */
	public void removeModifier(TableCellModifier modifier) {
		modifiers.remove(modifier);
	}
		
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// if it's a component, make sure we clear any foreground/background colors
		if(baseRenderer instanceof Component) {
			((Component)baseRenderer).setForeground(null);
			((Component)baseRenderer).setBackground(null);
		}
		
		// get the initial component
		Component c = baseRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
		// modify it based on our modifiers
		for(TableCellModifier modifier : modifiers)
			modifier.modifyComponent(c, table, value, isSelected, hasFocus, row, column);
		
		return c;
	}

}
