package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;

import edu.cornell.dendro.corina.tridasv2.ui.support.NotPresent;

public class EnumComboBoxPropertyEditor extends AbstractPropertyEditor {
	/** The old value (for primitive undo on escape) */
	private Object oldValue;

	/** The class of the enum */
	private Class enumClass;
	
	private static final NotPresentItemImpl NOT_PRESENT = new NotPresentItemImpl();
	
	public EnumComboBoxPropertyEditor(Class<?> enumClass) {
		if(!enumClass.isEnum())
			throw new IllegalArgumentException("Not an enum!");
	
		this.enumClass = enumClass;
		
		editor = new ComboBoxFilterable(enumClass.getEnumConstants()) {
			// Cache the last selected item in case the user hits escape!
			public void setSelectedItem(Object anObject) {
				oldValue = getSelectedItem();
				super.setSelectedItem(anObject);
			}
		};
		
	    final JComboBox combo = (JComboBox)editor;
	    
	    // render enum values
	    combo.setRenderer(new EnumComboBoxItemRenderer());
	    
	    combo.insertItemAt(NOT_PRESENT, 0);
	    
	    // save value when combo box goes away
	    combo.addPopupMenuListener(new PopupMenuListener() {
	      public void popupMenuCanceled(PopupMenuEvent e) {}
	      public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
	        EnumComboBoxPropertyEditor.this.firePropertyChange(oldValue,
	          combo.getSelectedItem());
	      }
	      public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}
	    });
	    
	    // save value when user presses enter
	    combo.addKeyListener(new KeyAdapter() {
	      public void keyPressed(KeyEvent e) {
	        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	          EnumComboBoxPropertyEditor.this.firePropertyChange(oldValue,
	            combo.getSelectedItem());          
	        }
	      }
	    });
	    combo.setSelectedIndex(-1);
	}
	
	@Override
	public Object getValue() {
		Object obj = ((JComboBox)editor).getSelectedItem();
		
		return (obj == NOT_PRESENT) ? null : obj;
	}
	
	@Override
	public void setValue(Object value) {
		JComboBox combo = (JComboBox) editor;
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
		((JComboBox) editor).setSelectedIndex(index);
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
		public String value() {
			return "<html><i>Not Present</i>";
		}
	}
}
