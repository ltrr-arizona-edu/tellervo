package edu.cornell.dendro.corina.tridasv2.ui;

import java.text.SimpleDateFormat;

import org.tridas.schema.Date;
import org.tridas.schema.DateTime;
import org.tridas.schema.Year;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;

/**
 * A renderer that shows Tridas Dates and DateTimes in a viewer friendly manner
 * 
 * @author Lucas Madar
 */

public class TridasYearDateTimeCellRenderer extends DefaultCellRenderer {
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertToString(Object value) {
		if(value instanceof Date) {
			Date dv = (Date) value;
			
			java.util.Date date = dv.getValue().toGregorianCalendar().getTime();
			String val = SimpleDateFormat.getDateInstance().format(date);
			if(dv.getCertainty() != null)
				val += " [" + dv.getCertainty().value() + "]";
			
			return val;
		}
		else if(value instanceof DateTime) {
			DateTime dv = (DateTime) value;
			
			java.util.Date date = dv.getValue().toGregorianCalendar().getTime();
			String val = SimpleDateFormat.getDateTimeInstance().format(date);
			if(dv.getCertainty() != null)
				val += " [" + dv.getCertainty().value() + "]";
			
			return val;			
		}
		else if(value instanceof Year) {
			Year year = (Year) value;
			
			String val = year.getValue().toString() + ' ' + year.getSuffix().value();
			if(year.getCertainty() != null)
				val += " [" + year.getCertainty().value() + "]";
			
			return val;
		}
		
		return null;
	}
}
