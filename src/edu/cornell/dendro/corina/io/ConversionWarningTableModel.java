package edu.cornell.dendro.corina.io;

import javax.swing.table.AbstractTableModel;

import org.tridas.io.exceptions.ConversionWarning;

public class ConversionWarningTableModel extends AbstractTableModel {

	private ConversionWarning[] warnings;
	
	
	String[] columnNames = {"Type", "Field", "Message"};

	
	public ConversionWarningTableModel()
	{
	}
	
	public ConversionWarningTableModel(ConversionWarning[] warnings)
	{
		this.warnings = warnings;
	}
	

	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		if(warnings!=null)
		{
			return warnings.length;
		}
		return 0;
	}

	@Override
	public Object getValueAt(int row, int col) {
		ConversionWarning warning = warnings[row];
		
		switch(col)
		{
		case 0:
			return warning.getWarningType();
		case 1:
			return warning.getField();
		case 2:
			return warning.getMessage();
		}
	
		return null;
		
	}
	
	@Override
	public Class<?> getColumnClass(int col)
	{
		return String.class;
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col].toString();
	}

	@Override
	public boolean isCellEditable(int row, int col)
    { 
		return false; 
	}

	
}
