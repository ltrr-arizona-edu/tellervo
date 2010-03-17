package edu.cornell.dendro.corina.model.editor;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

public class AnnotationTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;

	private final String[] columnNames;
	private final Class<?>[] columnClasses;

	private ArrayList<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
	public boolean[] canEdit;

	public AnnotationTableModel( String[] argColumnNames, Class<?>[] argColumnClasses,
									boolean[] argEditableColumns) {
		columnNames = argColumnNames;
		columnClasses = argColumnClasses;
		canEdit = argEditableColumns;
		if ( argColumnNames.length != argEditableColumns.length
				&& argColumnClasses.length != argColumnNames.length) {
			throw new RuntimeException("Column names, classes, and editable data must all be same length");
		}
	}
	
	/**
	 * Returns the ArrayLists used to store the model data.  The first ArrayList is for rows,
	 * and the second is for each object by column.
	 * @return
	 */
	public ArrayList<ArrayList<Object>> getRowData(){
		return rows;
	}
	
	/**
	 * Returns data in array form.  access by [row][column].  Modifying this data will
	 * not modify the table model, instead use {@link #getRowData()}
	 * @return
	 */
	public Object[][] getRowDataArray(){
		Object[][] returnArray = new Object[rows.size()][];
		for(int i=0; i<rows.size(); i++){
			ArrayList<Object> row = rows.get(i);
			returnArray[i] = new Object[row.size()];
			for(int j=0; j<row.size(); j++){
				returnArray[i][j] = row.get(j);
			}
		}
		return returnArray;
	}

	public void addRow( Object[] argRow) {
		ArrayList<Object> row = new ArrayList<Object>();
		for ( Object o : argRow) {
			row.add( o);
		}
		rows.add( row);
	}

	public void setValueAt( Object argValue, int argRow, int argColumn) {
		rows.get( argRow).set( argColumn, argValue);
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName( int argColumn) {
		return columnNames[argColumn];
	}

	/**
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return columnNames.length;
	}

	/**
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return rows.size();
	}

	/**
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt( int argRowIndex, int argColumnIndex) {
		return rows.get( argRowIndex).get( argColumnIndex);
	}

	@Override
	public boolean isCellEditable( int rowIndex, int columnIndex) {
		return canEdit[columnIndex];
	}

	@Override
	public Class<?> getColumnClass( int column) {
		return columnClasses[column];
	}

}
