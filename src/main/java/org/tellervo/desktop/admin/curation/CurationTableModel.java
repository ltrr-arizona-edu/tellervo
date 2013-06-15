package org.tellervo.desktop.admin.curation;

import static java.text.DateFormat.LONG;
import static java.text.DateFormat.getDateInstance;

import java.text.DateFormat;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.dictionary.SecurityUser;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.I18n;
import org.tellervo.desktop.ui.I18n.TellervoLocale;
import org.tellervo.schema.CurationStatus;
import org.tellervo.schema.WSICuration;
import org.tridas.schema.DateTime;

public class CurationTableModel extends AbstractTableModel {
	public static final String[] columns = {"Date", "Curation status", "Curator", "Notes"};
	
	private ArrayList<WSICuration> events = new ArrayList<WSICuration>();
	
	private static final long serialVersionUID = 1L;

	
	public CurationTableModel()
	{
		
	}
	
	public CurationTableModel(ArrayList<WSICuration> list)
	{
		setCurationEvents(list);
	}
	
	public void setCurationEvents(ArrayList<WSICuration> list)
	{
		events = list;
	}
	
	public void addCurationEvent(WSICuration curation)
	{
		events.add(curation);
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

	public WSICuration getRowAsWSICuration(int row)
	{
		try{
			return events.get(row);
		} catch (Exception e)
		{
			return null;
		}
	}
	
	@Override
	public Object getValueAt(int row, int col) {
		
		WSICuration event = null;
		
		try{
			event = events.get(row);	
		} catch (IndexOutOfBoundsException e)
		{
			return null;
		}
		
		switch(col)
		{
			case 0:
				String country = App.prefs.getPref(PrefKey.LOCALE_COUNTRY_CODE, "xxx");
				String language = App.prefs.getPref(PrefKey.LOCALE_LANGUAGE_CODE, "xxx");
				TellervoLocale loc = I18n.getTellervoLocale(country, language);
				
				DateFormat dateFormat =  getDateInstance(LONG, loc.getLocale());
				return dateFormat.format(event.getCurationtimestamp().toGregorianCalendar().getTime());
			case 1:
				return event.getStatus().value();
			case 2:
				return event.getSecurityUser().getLastName()+", "+event.getSecurityUser().getFirstName();
			case 3:
				return event.getNotes();
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
			default:
				return null;
		}
	}
}
