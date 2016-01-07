package org.tellervo.desktop.components.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.tridas.io.util.DateUtils;
import org.tridas.schema.Date;
import org.tridas.schema.DateTime;

public class NattyDateEditor extends DefaultCellEditor {

	private JTextField textField;
	private static final Border red = new LineBorder(Color.red);
    private static final Border black = new LineBorder(Color.black);
    
	public NattyDateEditor(JTextField textField) {
		super(textField);
        this.textField = textField;
	}

	
	// DefaultCellEditor returns true for any and all KeyEvents which
	// is absolutely atrocious. This method returns false for KeyEvents
	// with an undefined character (e.g F1) and when one of Alt, Ctrl,
	// or Meta is pressed.
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof KeyEvent) {
			KeyEvent e = (KeyEvent)anEvent;
			return e.getKeyChar() != KeyEvent.CHAR_UNDEFINED &&
				(e.getModifiersEx() & (InputEvent.ALT_DOWN_MASK
					| InputEvent.CTRL_DOWN_MASK	| InputEvent.META_DOWN_MASK)) == 0;
		}
		return super.isCellEditable(anEvent);
	}
	@Override
	public Component getTableCellEditorComponent(final JTable table,
			Object value, boolean isSelected, int row, int column) {

		// This is done simply so that the contents is replaced rather than
		// appended to when edited
		
		String str = "";
		if(value!=null) str = value.toString();
		if(value instanceof org.tridas.schema.Date)
		{
			str = DateUtils.getFormattedDate((Date) value, new SimpleDateFormat("YYYY-MM-dd"));
		}
		
		final JTextField component = (JTextField) super.getTableCellEditorComponent(table, str, isSelected, row, column);
		component.setBorder(null);		
		if(isSelected)
		{
			component.selectAll();
		}
		
		component.setBorder(black);
		return component;
	}
	

    @Override
    public boolean stopCellEditing() {
       
    	if(textField.getText()==null || textField.getText().isEmpty())
    	{
    		 return super.stopCellEditing();
    	}
    	
    	try {
        	
        	DateTime dt = DateUtils.parseDateTimeFromNaturalString(textField.getText());
        	
        	if(textField.getText()!=null && textField.getText().length()>0 && dt==null)
        	{
        		throw new Exception("Invalid date text string");
        	}
        	
        	textField.setText(DateUtils.getFormattedDateTime(dt, new SimpleDateFormat("YYYY-MM-dd")));
        	
        } catch (Exception e) {
            textField.setBorder(red);
            return false;
        }
        return super.stopCellEditing();
    }

    @Override
    public Object getCellEditorValue(){
    	
    	try{
	    	DateTime dt = DateUtils.parseDateTimeFromNaturalString(textField.getText());
	    	
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
}
