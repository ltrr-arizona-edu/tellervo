/**
 * 
 */
package edu.cornell.dendro.corina.editor.support;


/**
 * @author Lucas Madar
 *
 */
public abstract class AbstractTableCellModifier implements TableCellModifier {
	private TableCellModifierListener listener;
	
	public void setListener(TableCellModifierListener listener) {
		if(this.listener != null && listener != null)
			throw new UnsupportedOperationException("Attempting to set a secondary listener not supported");
		
		this.listener = listener;
	}
	
	/**
	 * Ask our parent to repaint
	 */
	protected void repaintParent() {
		if(listener != null)
			listener.cellModifierChanged(this);
	}
}
