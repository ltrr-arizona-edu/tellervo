package org.tellervo.desktop.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;

public class JUNGModeRenderer extends JLabel implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
	
    	if(value.equals(Mode.TRANSFORMING))
    	{
    		setText("Pan");
    	}
    	else if (value.equals(Mode.PICKING))
    	{
    		setText("Grab");
    	}
		
    	
        Color background;
        Color foreground;

        // check if this cell represents the current DnD drop location
        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            background = Color.BLUE;
            foreground = Color.WHITE;

        // check if this cell is selected
        } else if (isSelected) {
            background = Color.RED;
            foreground = Color.WHITE;

        // unselected, and not the DnD drop location
        } else {
            background = Color.WHITE;
            foreground = Color.BLACK;
        };

        setBackground(background);
        setForeground(foreground);

		
		return this;
	}
	
}
