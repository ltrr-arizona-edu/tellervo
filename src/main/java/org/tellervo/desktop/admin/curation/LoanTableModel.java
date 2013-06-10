package org.tellervo.desktop.admin.curation;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.tellervo.schema.WSILoan;
import org.tridas.schema.DateTime;

public class LoanTableModel extends AbstractTableModel {
	
	public static final String[] columns = {"First name", "Last name", "Organisation", "Issue date", "Due date"};
	
	private ArrayList<WSILoan> loans = new ArrayList<WSILoan>();
	
	private static final long serialVersionUID = 1L;
	
	public LoanTableModel()
	{
		
	}
	
	public LoanTableModel(ArrayList<WSILoan> loans)
	{
		this.loans = loans;
	}
	
	
	public void setLoans(ArrayList<WSILoan> loans)
	{
		this.loans = loans;
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	/**
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
	
	
	@Override
	public int getRowCount() {
		return loans.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		WSILoan loan = null;
		
		try{
			loan = loans.get(row);	
		} catch (IndexOutOfBoundsException e)
		{
			return null;
		}
		
		switch(col)
		{
			case 0:
				return loan.getFirstname();
			case 1:
				return loan.getLastname();
			case 2:
				return loan.getOrganisation();
			case 3:
				return loan.getIssuedate();
			case 4: 
				return loan.getDuedate();		
			default:
				return null;
		}
			

		
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
		if(c==3 || c==4)
		{
			return DateTime.class;
		}
		else
		{
			return String.class;
		}
	}

}
