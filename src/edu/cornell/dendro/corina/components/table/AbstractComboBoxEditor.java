/**
 * Created at Aug 22, 2010, 3:00:22 AM
 */
package edu.cornell.dendro.corina.components.table;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

/**
 * @author Daniel
 *
 */
public abstract class AbstractComboBoxEditor implements TableCellEditor, TreeCellEditor{
	private static final long serialVersionUID = 1L;
	
	private ComboBoxCellEditor delegate = null;
	
	/**
	 * @return the delegate
	 */
	public DefaultCellEditor getDelegate() {
		if(delegate == null){
			delegate = new ComboBoxCellEditor(new JComboBox(getComboBoxOptions()));
		}
		return delegate;
	}

	protected abstract String[] getComboBoxOptions();

	/**
	 * @param argL
	 * @see javax.swing.AbstractCellEditor#addCellEditorListener(javax.swing.event.CellEditorListener)
	 */
	public void addCellEditorListener(CellEditorListener argL) {
		getDelegate().addCellEditorListener(argL);
	}
	/**
	 * @param argL
	 * @see javax.swing.AbstractCellEditor#removeCellEditorListener(javax.swing.event.CellEditorListener)
	 */
	public void removeCellEditorListener(CellEditorListener argL) {
		getDelegate().removeCellEditorListener(argL);
	}
	
	/**
	 * @return
	 * @see javax.swing.AbstractCellEditor#getCellEditorListeners()
	 */
	public CellEditorListener[] getCellEditorListeners() {
		return getDelegate().getCellEditorListeners();
	}
	/**
	 * @param argObj
	 * @return
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object argObj) {
		return getDelegate().equals(argObj);
	}
	/**
	 * @return
	 * @see javax.swing.DefaultCellEditor#getComponent()
	 */
	public Component getComponent() {
		return getDelegate().getComponent();
	}
	/**
	 * @param argCount
	 * @see javax.swing.DefaultCellEditor#setClickCountToStart(int)
	 */
	public void setClickCountToStart(int argCount) {
		getDelegate().setClickCountToStart(argCount);
	}
	/**
	 * @return
	 * @see javax.swing.DefaultCellEditor#getClickCountToStart()
	 */
	public int getClickCountToStart() {
		return getDelegate().getClickCountToStart();
	}
	/**
	 * @param argAnEvent
	 * @return
	 * @see javax.swing.DefaultCellEditor#isCellEditable(java.util.EventObject)
	 */
	public boolean isCellEditable(EventObject argAnEvent) {
		return getDelegate().isCellEditable(argAnEvent);
	}
	/**
	 * @param argAnEvent
	 * @return
	 * @see javax.swing.DefaultCellEditor#shouldSelectCell(java.util.EventObject)
	 */
	public boolean shouldSelectCell(EventObject argAnEvent) {
		return getDelegate().shouldSelectCell(argAnEvent);
	}
	/**
	 * @return
	 * @see javax.swing.DefaultCellEditor#stopCellEditing()
	 */
	public boolean stopCellEditing() {
		return getDelegate().stopCellEditing();
	}
	/**
	 * 
	 * @see javax.swing.DefaultCellEditor#cancelCellEditing()
	 */
	public void cancelCellEditing() {
		getDelegate().cancelCellEditing();
	}
	/**
	 * @param argTree
	 * @param argValue
	 * @param argIsSelected
	 * @param argExpanded
	 * @param argLeaf
	 * @param argRow
	 * @return
	 * @see javax.swing.DefaultCellEditor#getTreeCellEditorComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int)
	 */
	public Component getTreeCellEditorComponent(JTree argTree, Object argValue, boolean argIsSelected,
			boolean argExpanded, boolean argLeaf, int argRow) {
		return getDelegate().getTreeCellEditorComponent(argTree, argValue, argIsSelected, argExpanded, argLeaf, argRow);
	}
	/**
	 * @param argTable
	 * @param argValue
	 * @param argIsSelected
	 * @param argRow
	 * @param argColumn
	 * @return
	 * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable argTable, Object argValue, boolean argIsSelected, int argRow,
			int argColumn) {
		return getDelegate().getTableCellEditorComponent(argTable, argValue, argIsSelected, argRow, argColumn);
	}

	/**
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue() {
		String name = getDelegate().getCellEditorValue().toString();
		return getValueFromString(name);
	}
	
	protected abstract Object getValueFromString(String argString);
}
