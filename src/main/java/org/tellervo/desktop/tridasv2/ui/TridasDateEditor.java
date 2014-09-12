package org.tellervo.desktop.tridasv2.ui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.util.GregorianCalendar;

import javax.swing.JPanel;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.tellervo.desktop.core.App;
import org.tridas.schema.Certainty;
import org.tridas.schema.Date;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.michaelbaranov.microba.calendar.DatePicker;


public class TridasDateEditor extends AbstractPropertyEditor {

	private DatePicker datePicker = new DatePicker();
	
	public TridasDateEditor()
	{
		editor = new JPanel();
		((JPanel)editor).setLayout(new BorderLayout());
		
		datePicker = new DatePicker();
		datePicker.setShowNoneButton(true);
		((JPanel)editor).add(datePicker, BorderLayout.CENTER);

	}
	
	
	@Override
	public Object getValue() 
	{
		
		if(datePicker.getDate()==null) return null;
		
		try{
			java.util.Date dataFromGui = null;
			dataFromGui = datePicker.getDate();
			GregorianCalendar c = new GregorianCalendar();		
			c.setTime(dataFromGui);
			XMLGregorianCalendar xmlgc;
			xmlgc = App.datatypeFactory.newXMLGregorianCalendar(c);
			xmlgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
			org.tridas.schema.Date d = new org.tridas.schema.Date();
			d.setValue(xmlgc);
			d.setCertainty(Certainty.EXACT);
			return d;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
		

	}
	
	
	
	@Override
	public void setValue(Object value) {

		if(value instanceof org.tridas.schema.Date && value!=null)
		{
			java.util.Date tempdate = ((org.tridas.schema.Date) value).getValue().toGregorianCalendar().getTime();
			try {
				datePicker.setDate(tempdate);
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		else
		{
			/*try {
				datePicker.setDate(null);
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		
	}
	
	@Override
	public String getAsText()
	{
		
		org.tridas.schema.Date val = (Date) getValue();
		
		
		return "bb";
		
	}
	
	private void setToNull() {
		setValue(null);
	}
	
}
