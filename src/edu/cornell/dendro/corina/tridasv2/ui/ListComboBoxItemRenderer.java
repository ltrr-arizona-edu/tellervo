/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.tridas.schema.ControlledVoc;

import edu.cornell.dendro.corina.schema.SecurityUser;

/**
 * @author Lucas Madar
 *
 */
public class ListComboBoxItemRenderer extends JLabel implements ListCellRenderer {
	private static final long serialVersionUID = 1L;

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
				ControlledVoc cv = (ControlledVoc) value;
				
				if(cv.isSetNormal())
					setText(cv.getNormal());
				else if(cv.isSetValue())
					setText(cv.getValue());
				else
					setText("<invalid controlled vocabulary>");
			}
			else if(value instanceof SecurityUser) {
				SecurityUser u = (SecurityUser) value;
				
				setText(u.getLastName() + ", " + u.getFirstName());
			}
			else
				setText(value.toString());
		}		
	}

}
