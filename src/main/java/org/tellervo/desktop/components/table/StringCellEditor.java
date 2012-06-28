package org.tellervo.desktop.components.table;

import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

public class StringCellEditor extends DefaultCellEditor {

	public StringCellEditor() {
		super(new JTextField());
		
		// TODO Auto-generated constructor stub
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
		JTextField component = (JTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
		component.setBorder(null);		
		if(isSelected)
		{
			component.selectAll();
		}
		
		return component;
	}
}
