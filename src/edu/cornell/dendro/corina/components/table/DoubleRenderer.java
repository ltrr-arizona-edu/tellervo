/**
 * Created at Aug 22, 2010, 3:42:33 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Daniel
 *
 */
public class DoubleRenderer extends DoubleTextField implements TableCellRenderer {
	private static final long serialVersionUID = 1L;

	/**
	 * @param argPrecision
	 * @param argCommitsOnValidEdit
	 */
	public DoubleRenderer(int argPrecision) {
		super(argPrecision, false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object value,
			boolean arg2, boolean arg3, int arg4, int arg5) {
 
		setText((value == null) ? "" : getNumberFormat().format(value));
		return this;
	}
 
}
