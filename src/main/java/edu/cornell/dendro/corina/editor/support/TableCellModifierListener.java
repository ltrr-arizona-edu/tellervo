package edu.cornell.dendro.corina.editor.support;

/**
 * Notification that a cell modifier has changed and wants a repaint
 * 
 * @author Lucas Madar
 */

public interface TableCellModifierListener {
	public void cellModifierChanged(TableCellModifier modifier);
}
