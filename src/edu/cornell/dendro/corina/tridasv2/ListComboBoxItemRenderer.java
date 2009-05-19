/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.tridas.schema.ControlledVoc;

import com.lowagie.text.Font;

/**
 * @author Lucas Madar
 *
 */
public class ListComboBoxItemRenderer extends JLabel implements ListCellRenderer {

	public ListComboBoxItemRenderer() {
		setOpaque(true);
		setHorizontalAlignment(LEFT);
		setVerticalAlignment(CENTER);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {

		// set backgrounds and such
		if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

		modifyComponent(value);
		
		return this;
	}
	
	public void modifyComponent(Object value) {
		if(value == null) {
			setText("");
		} 
		else {
			if(value instanceof ControlledVoc) {
				setText(((ControlledVoc) value).getNormal());
			}
			else
				setText(value.toString());
		}		
	}

}
