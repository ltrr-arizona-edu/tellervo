package org.tellervo.desktop.components.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tridas.io.util.DateUtils;
import org.tridas.schema.DateTime;
import org.tridas.schema.TridasFile;

public class TridasFileListEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private static final Border red = new LineBorder(Color.red);
    private static final Border black = new LineBorder(Color.black);
    
	public TridasFileListEditor(JTextField textField) {
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
		if(value!=null) {
			
			if(value instanceof TridasFileList)
			{
				str = value.toString();
			}
			else
			{
				str = value.toString();
			}
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
        	
    		TridasFileList fl = new TridasFileList(textField.getText());
        	
        } catch (Exception e) {
            textField.setBorder(red);
            return false;
        }
        return super.stopCellEditing();
    }

	
    @Override
    public Object getCellEditorValue(){
    	
    	try{
    		
    		return new TridasFileList(textField.getText());

    	}
    	catch (Exception e)
    	{
    		return null;
    	}
    	
    }
}
