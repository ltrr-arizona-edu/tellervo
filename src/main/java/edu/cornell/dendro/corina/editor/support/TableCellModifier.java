package edu.cornell.dendro.corina.editor.support;

import java.awt.Component;

import javax.swing.JTable;

/**
 * An interface that allows changing an already existing component.
 * 
 * Used to do things like change background colors.
 * 
 * @author Lucas Madar
 */

public interface TableCellModifier {
	/** Modify the component */
	public void modifyComponent(Component c, JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column);
	
	/** Set a listener that wants to know when we've changed (notify to repaint) */
	public void setListener(TableCellModifierListener listener);
}
