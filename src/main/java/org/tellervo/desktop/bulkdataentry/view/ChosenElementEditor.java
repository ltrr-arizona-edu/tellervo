/*******************************************************************************
 * Copyright (C) 2010 Daniel Murphy and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
/**
 * Created at Oct 8, 2010, 12:52:44 PM
 */
package org.tellervo.desktop.bulkdataentry.view;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;

import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.model.TridasElementOrPlaceholder;
import org.tridas.schema.TridasElement;

import com.dmurph.mvc.gui.combo.MVCJComboBox;
import com.dmurph.mvc.model.MVCArrayList;


/**
 * Very coupled and specific, this is here so the editor is specific to each row.
 * @author Daniel
 *
 */
public class ChosenElementEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;

	public ChosenElementEditor(final MVCJComboBox<TridasElementOrPlaceholder> comboBox) {
		super(comboBox);
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
							ChosenElementEditor.this, 0, ""));
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
	@SuppressWarnings("unchecked")
	@Override
	public Component getTableCellEditorComponent(final JTable table,
			Object value, boolean isSelected, int row, int column) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (table.getCellEditor() != ChosenElementEditor.this)
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
		delegate.setValue(value);
        //in order to avoid a "flashing" effect when clicking a checkbox
        //in a table, it is important for the editor to have as a border
        //the same border that the renderer has, and have as the background
        //the same color as the renderer has. This is primarily only
        //needed for JCheckBox since this editor doesn't fill all the
        //visual space of the table cell, unlike a text field.
        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component c = renderer.getTableCellRendererComponent(table, value,
                isSelected, true, row, column);
        if (c != null) {
            editorComponent.setOpaque(true);
            editorComponent.setBackground(c.getBackground());
            if (c instanceof JComponent) {
                editorComponent.setBorder(((JComponent)c).getBorder());
            }
        } else {
            editorComponent.setOpaque(false);
        }
        
        // finally, populate the box from the corresponding row in the model list
		MVCJComboBox<TridasElementOrPlaceholder> comboBox = (MVCJComboBox<TridasElementOrPlaceholder>) super.editorComponent;

		SampleModel model = BulkImportModel.getInstance().getSampleModel();
		SingleSampleModel ssm = model.getRows().get(row);
        
		MVCArrayList<TridasElementOrPlaceholder> possibleTeops = new MVCArrayList<TridasElementOrPlaceholder>();
		
		for(TridasElement elem : ssm.getPossibleElements())
		{
			TridasElementOrPlaceholder teop = new TridasElementOrPlaceholder(elem);
			possibleTeops.add(teop);
		}
	
		if(possibleTeops != comboBox.getData()){
			comboBox.setData(possibleTeops);
		}
        
        return editorComponent;
	}

}
