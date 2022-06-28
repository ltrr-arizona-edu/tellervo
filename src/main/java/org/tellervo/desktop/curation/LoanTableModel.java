package org.tellervo.desktop.curation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tellervo.desktop.ui.I18n;

import org.tellervo.schema.WSILoan;
import org.tridas.schema.DateTime;

import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateInstance;
import java.text.DateFormat;

public class LoanTableModel extends AbstractTableModel {
	
	public static final String[] columns = {"First name", "Last name", "Organisation", "Issue date", "Due date"};
	
	private ArrayList<WSILoan> loans = new ArrayList<WSILoan>();
	
	private static final long serialVersionUID = 1L;
	
	public LoanTableModel()
	{
		
	}
	
	public LoanTableModel(List<WSILoan> loans)
	{
		setLoans(loans);
	}
	
	
	public void setLoans(List<WSILoan> loans)
	{
		this.loans = new ArrayList<WSILoan>(loans);
	}

	/**
	 * Get the loan representation of a row
	 * 
	 * @param row
	 * @return
	 */
	public WSILoan getRow(int row)
	{		
		try{
			return loans.get(row);	
		} catch (IndexOutOfBoundsException e)
		{
			return null;
		}
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
		
		WSILoan loan = getRow(row);
		
		if(loan==null) return null;
		
		String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
		String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
		TellervoLocale loc = I18n.getTellervoLocale(country, language);
		
		DateFormat dateFormat =  getDateInstance(SHORT, loc.getLocale());
		
		
		switch(col)
		{
			case 0:
				return loan.getFirstname();
			case 1:
				return loan.getLastname();
			case 2:
				return loan.getOrganisation();
			case 3:
				if(loan.isSetIssuedate())
				{
					return dateFormat.format(loan.getIssuedate().toGregorianCalendar().getTime());
				}
				else
				{
					return null;
				}
			case 4: 
				if(loan.isSetDuedate())
				{
					return dateFormat.format(loan.getDuedate().toGregorianCalendar().getTime());
				}
				else
				{
					return null;
				}
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
