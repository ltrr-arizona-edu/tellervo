package org.tellervo.desktop.gui.hierarchy;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.tellervo.schema.WSITag;

public class WSITagComboRenderer extends JLabel implements ListCellRenderer {


	private static final long serialVersionUID = 1L;


	public WSITagComboRenderer() {
        setOpaque(true);
    }


	public Component getListCellRendererComponent(JList list,
			Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

		
		if(value == null)
		{
			setText("");
		}
		else if(value instanceof WSITag)
		{
			setText(((WSITag) value).getValue());
		}
		else if (value instanceof String)
		{
	        setText(value.toString());

		}
		else
		{
			setText("???");
		}
		
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        
		return this;
	}



	
}
