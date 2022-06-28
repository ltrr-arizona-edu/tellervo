package org.tellervo.desktop.prefs.panels;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.ui.I18n.TellervoLocale;

public class LocaleComboRenderer extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(LocaleComboRenderer.class);
	
	public LocaleComboRenderer()
	{
	    setOpaque(true);
	    setHorizontalAlignment(LEFT);
	    setVerticalAlignment(CENTER);
	}
	
	@Override
	public Component getListCellRendererComponent(JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

		TellervoLocale loc;
		
		if(value instanceof TellervoLocale)
		{
			
			loc = (TellervoLocale) value;
		}
		else
		{
			log.error("Combo item is not a TellervoLocale");
			return null;
		}
		
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        ImageIcon icon = (ImageIcon) loc.getFlag();
        setIcon(icon);
        setText(loc.getName());
        
		return this;
	}




}
