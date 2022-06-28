/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
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
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.tridasv2.ui;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.tellervo.desktop.tridasv2.ui.support.NotPresent;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;


public class ListComboBoxPropertyEditor extends AbstractPropertyEditor {
	/** The old value (for primitive undo on escape) */
	private Object oldValue;
	
	private static final NotPresentItemImpl NOT_PRESENT = new NotPresentItemImpl();
	
	@SuppressWarnings("serial")
	public ListComboBoxPropertyEditor(List<?> list) {	
		editor = new ComboBoxFilterable(list.toArray()) {
			// Cache the last selected item in case the user hits escape!
			public void setSelectedItem(Object anObject) {
				oldValue = getSelectedItem();
				super.setSelectedItem(anObject);
			}
		};
		
	    final JComboBox combo = ((ComboBoxFilterable)editor);
	    
	    // render enum values
	    combo.setRenderer(new ListComboBoxItemRenderer());
	    
	    combo.insertItemAt(NOT_PRESENT, 0);
	    
	    // save value when combo box goes away
	    combo.addPopupMenuListener(new PopupMenuListener() {
	      public void popupMenuCanceled(PopupMenuEvent e) {}
	      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	        ListComboBoxPropertyEditor.this.firePropertyChange(oldValue,
	          combo.getSelectedItem());
	      }
	      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
	    });
	    
	    // save value when user presses enter
	    combo.addKeyListener(new KeyAdapter() {
	      public void keyPressed(KeyEvent e) {
	        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	          ListComboBoxPropertyEditor.this.firePropertyChange(oldValue,
	            combo.getSelectedItem());          
	        }
	        else {
	        }
	      }
	    });
	    combo.setSelectedIndex(-1);
	}
	
	@Override
	public Object getValue() {
		Object obj = ((ComboBoxFilterable)editor).getSelectedItem();
		
		return (obj == NOT_PRESENT) ? null : obj;
	}
	
	@Override
	public void setValue(Object value) {
		JComboBox combo = ((ComboBoxFilterable)editor);
		Object current = null;
		int index = -1;
		
		if(value == null)
			value = NOT_PRESENT;
		
		for (int i = 0, c = combo.getModel().getSize(); i < c; i++) {
			current = combo.getModel().getElementAt(i);
			if (value == current || (current != null && current.equals(value))) {
				index = i;
				break;
			}
		}
		((ComboBoxFilterable)editor).setSelectedIndex(index);
	}
	
	@Override
	public boolean supportsCustomEditor() {
		return true;
	}

	@Override
	public Component getCustomEditor() {
		return editor;
	}
	
	private static class NotPresentItemImpl implements NotPresent {
		@Override
		public String toString() {
			return "<html><i>[Intentionally left empty]</i>";
		}
	}
}
