package org.tellervo.desktop.testing;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.testing.WSTest.WSTestKey;

public class WSTestTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;

	private ArrayList<WSTest> tests = new ArrayList<WSTest>();

	
	public WSTestTableModel() {
		
		tests = new ArrayList<WSTest>();
		
		for (WSTestKey val : WSTestKey.values()) {
		
			WSTest test = new WSTest();
			test.setKey(val);
			tests.add(test);
			
		}

	}

	public void setTest(int row, WSTest test)
	{
		tests.set(row, test);
		this.fireTableDataChanged();
	}
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		return tests.size();
	}
	
	@Override
	public String getColumnName(int col) {
		if(col==0)
		{
			return "Test";
		}
		else if (col==1)
		{
			return "Success";
		}
		else if (col==2)
		{
			return "Details";
		}
		
		return null;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSTest test = tests.get(rowIndex);
		
		if(columnIndex==0)
		{
			return test.getKey();
		}
		else if (columnIndex==1)
		{
			return test.getTestResult();
		}
		else if (columnIndex==2)
		{
			return test.getErrorMessage();
		}
		
		return null;
	}
	
	public Class<?> getColumnClass(int col)
	{
		if(col==0)
		{
			return WSTestKey.class;
		}
		
		return String.class;
		
	}
	
	public WSTest getTest(int row)
	{
		return tests.get(row);
	}
	
	

}

