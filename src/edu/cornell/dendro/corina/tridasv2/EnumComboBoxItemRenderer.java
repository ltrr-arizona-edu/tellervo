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

import com.lowagie.text.Font;

/**
 * @author Lucas Madar
 *
 */
public class EnumComboBoxItemRenderer extends JLabel implements ListCellRenderer {

	public EnumComboBoxItemRenderer() {
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
			setText("Choose...");
			setFont(getFont().deriveFont(Font.ITALIC));
		} 
		else {
			// try to invoke the class' value() to get a String value
			try {
				Method method = value.getClass().getMethod("value", (Class<?>[]) null);
				setText(method.invoke(value, new Object[] {}).toString());
			} catch (Exception e) {
				e.printStackTrace();
				setText(value.toString());
			}
		}		
	}

}
