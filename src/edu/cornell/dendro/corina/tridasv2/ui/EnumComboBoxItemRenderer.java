/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JLabel;
import javax.swing.JList;

/**
 * @author Lucas Madar
 *
 */
public class EnumComboBoxItemRenderer extends JLabel implements ComboBoxItemRenderer {
	private static final long serialVersionUID = 1L;

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
			setText("");
		} 
		else {
			// try to invoke the class' value() to get a String value
			try {
				Method method = value.getClass().getMethod("value", (Class<?>[]) null);
				setText(method.invoke(value, new Object[] {}).toString());
			} catch (Exception e) {
				setText(value.toString());
			}
		}		
	}

}
