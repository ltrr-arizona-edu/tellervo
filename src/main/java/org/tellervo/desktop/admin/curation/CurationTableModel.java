package org.tellervo.desktop.admin.curation;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.dictionary.SecurityUser;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.WSICurationEvent;
import org.tellervo.schema.WSILoan;
import org.tridas.schema.DateTime;

public class CurationTableModel extends AbstractTableModel {
	public static final String[] columns = {"Date", "Curation status", "Curator", "Notes", "Loan"};
	
	private ArrayList<WSICurationEvent> events = new ArrayList<WSICurationEvent>();
	
	private static final long serialVersionUID = 1L;

	
	public CurationTableModel()
	{
		
	}
	
	public CurationTableModel(ArrayList<WSICurationEvent> list)
	{
		setCurationEvents(list);
	}
	
	public void setCurationEvents(ArrayList<WSICurationEvent> list)
	{
		events = list;
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
		return events.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		WSICurationEvent event = null;
		
		try{
			event = events.get(row);	
		} catch (IndexOutOfBoundsException e)
		{
			return null;
		}
		
		switch(col)
		{
			case 0:
				return event.getCurationTimeStamp();
			case 1:
				return event.getStatus();
			case 2:
				return event.getSecurityUser();
			case 3:
				return event.getNotes();
			case 4: 
				return event.getLoan();		
			default:
				return null;
		}
			

		
	}
	
	@Override
	public Class<?> getColumnClass(int c) {
		
		switch(c)
		{
			case 0:
				return DateTime.class;
			case 1:
				return CurationStatus.class;
			case 2:
				return SecurityUser.class;
			case 3:
				return String.class;
			case 4: 
				return WSILoan.class;
			default:
				return null;
		}
	}
}
