package org.tellervo.desktop.metadataexport;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.metadataexport.MetadataExportColumn.ExportColumnType;

public class MetadataExportColumnsTableModel extends AbstractTableModel {

	private final static Logger log = LoggerFactory.getLogger(MetadataExportColumnsTableModel.class);

	private String[] columnNames = { "Column name", "Type", "Value" };
	private ArrayList<MetadataExportColumn> data =  new ArrayList<MetadataExportColumn>();

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	public void addRow()
	{
		MetadataExportColumn datarow = new MetadataExportColumn();
		
		data.add(datarow);
		this.fireTableRowsInserted(0, data.size()-1);
	}
	
	public void deleteRow(int row) throws IndexOutOfBoundsException
	{		
		if(row>=data.size())
		{
			throw new IndexOutOfBoundsException("Unable to delete row.  Row index given is larger than number of rows in table");
		}	
		
		data.remove(row);
		this.fireTableRowsDeleted(row, row);
	}

	public Object getValueAt(int row, int col) throws IndexOutOfBoundsException{
		
		MetadataExportColumn datarow = data.get(row);
		
		if(datarow==null) 
		{
			throw new IndexOutOfBoundsException("Unable get value.  Row index given is larger than number of rows in table");
		}
		if(col>=getColumnCount())
		{
			throw new IndexOutOfBoundsException("Unable get value.  Column index given is larger than number of columns in table");
		}
		
		if(col==0)
		{
			return datarow.getTitle();
		}
		else if (col==1)
		{
			return datarow.getType();
		}
		else if (col==2)
		{
			return datarow.getValue();
		}
		
		log.debug("Invalid table column index");
		return null;
	}

	public Class getColumnClass(int col) throws IndexOutOfBoundsException{
		
		if(col==0)
		{
			return String.class;
		}
		else if (col==1)
		{
			return ExportColumnType.class;
		}
		else if (col==2)
		{
			return String.class;
		}
		
		throw new IndexOutOfBoundsException("Unable get column class.  Column index given is larger than number of columns in table");

	}

	
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		
		MetadataExportColumn datarow = data.get(row);
		
		if(datarow==null) 
		{
			throw new IndexOutOfBoundsException("Unable to set value.  Row index given is larger than number of rows in table");
		}
		if(col>=getColumnCount())
		{
			throw new IndexOutOfBoundsException("Unable to set value.  Column index given is larger than number of columns in table");
		}
		
		if(col==0)
		{
			datarow.setTitle((String) value);
		}
		else if (col==1)
		{
			datarow.setType(ExportColumnType.valueOf((String) value));
		}
		else if (col==2)
		{
			datarow.setValue((String) value);
		}
		
		
		fireTableCellUpdated(row, col);
	}
}
