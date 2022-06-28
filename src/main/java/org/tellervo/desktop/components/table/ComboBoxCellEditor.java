/**
 * Created at Sep 21, 2010, 11:11:44 AM
 */
package org.tellervo.desktop.components.table;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 * @author Daniel
 *
 */
public class ComboBoxCellEditor extends DefaultCellEditor {
	private static final long serialVersionUID = 1L;

	
	public ComboBoxCellEditor(final JComboBox comboBox) {
		super(comboBox);
		
		this.setClickCountToStart(2);
		
		comboBox.removeActionListener(delegate);
		delegate = new EditorDelegate() {
			private static final long serialVersionUID = 1L;

			// No change from super
			@Override
			public void setValue(Object value) {
				comboBox.setSelectedItem(value);
			}
 
			// No change from super
			@Override
			public Object getCellEditorValue() {
				return comboBox.getSelectedItem();
			}
 
			// No change from super
			@Override
			public boolean shouldSelectCell(EventObject anEvent) { 
				if (anEvent instanceof MouseEvent) { 
					MouseEvent e = (MouseEvent)anEvent;
					return e.getID() != MouseEvent.MOUSE_DRAGGED;
				}
				return true;
			}
			
			// No change from super
			@Override
			public boolean stopCellEditing() {
				if (comboBox.isEditable()) {
					// Commit edited value.
					comboBox.actionPerformed(new ActionEvent(
							ComboBoxCellEditor.this, 0, ""));
				}
				return super.stopCellEditing();
			}
 
			// JComboBox sends action events when a key is pressed
			// that changes the selected item via the JList's getNextMatch
			// method. Why? Probably a bug.. workaround filters all
			// KeyEvents except when the key code is VK_ENTER
			@Override
			public void actionPerformed(ActionEvent e) {
				if (EventQueue.getCurrentEvent() instanceof KeyEvent) {
					KeyEvent ke = (KeyEvent)EventQueue.getCurrentEvent();
					if (ke.getKeyCode() != KeyEvent.VK_ENTER)
						return;
				}
				super.actionPerformed(e);
			}
			
		};
		comboBox.addActionListener(delegate);
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
	
	// Popup doesn't become visible if the editing was triggered
	// by a KeyEvent - not sure why. It also wont be visible
	// if this editor was already active when another row is clicked.
	// The runnable within this method ensures that the popup is made visible.
	@Override
	public Component getTableCellEditorComponent(final JTable table,
			Object value, boolean isSelected, int row, int column) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (table.getCellEditor() != ComboBoxCellEditor.this)
					return;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				JComboBox combo = (JComboBox)getComponent();
				if (!combo.isPopupVisible()) {
					combo.showPopup();
					combo.requestFocusInWindow();
				}
			}
		});
		return super.getTableCellEditorComponent(table, value, isSelected, row, column);
	}
}