package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.tellervo.desktop.core.App;
import org.tridas.io.util.DateUtils;
import org.tridas.schema.Certainty;
import org.tridas.schema.Date;
import org.tridas.schema.DateTime;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.michaelbaranov.microba.calendar.DatePicker;


public class TridasDateEditor extends AbstractPropertyEditor {

	
	public TridasDateEditor()
	{
		editor = new JTextField();

	}
	
	
	@Override
	public Object getValue() 
	{
		
		try{
	    	DateTime dt = DateUtils.parseDateTimeFromNaturalString(((JTextField)editor).getText());
	    	
	    	Date date = new Date();
	    	date.setCertainty(dt.getCertainty());
	    	date.setValue(dt.getValue());
	    	
	    	return date;
    	}
    	catch (Exception e)
    	{
    		return null;
    	}
	}
	
	
	
	@Override
	public void setValue(Object value) {

		String str = "";
		if(value!=null) str = value.toString();
		if(value instanceof org.tridas.schema.Date)
		{
			str = DateUtils.getFormattedDate((Date) value, new SimpleDateFormat("YYYY-MM-dd"));
		}
		
		((JTextField)editor).setText(str);
		

		
	}
	
}
