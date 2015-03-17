package org.tellervo.desktop.io;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;

public class SeriesIdentityTableCellRenderer extends JLabel implements TableCellRenderer {
	private final static Logger log = LoggerFactory.getLogger(SeriesIdentityTableCellRenderer.class);

	@Override
	public Component getTableCellRendererComponent(
            JTable table, Object item,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
		
		this.setOpaque(true);
		setText(item.toString());

		
		if(item instanceof IdentityItem)
		{
			IdentityItem id = (IdentityItem) item;

			log.debug("Item code : "+id.getCode());
			
			if(id.getCode()==null || id.getCode().trim().length()==0)
			{
				// Blank
				this.setIcon(null);
			}
			else 
			{
				if((id.isDbChecked()))
				{
					 if (id.isInDatabase())
					 {
						 // DB checked and item is present
						 this.setIcon(Builder.getIcon("found.png", 16));
					 }
					 else
					 {
						 this.setIcon(Builder.getIcon("missing.png", 16));
					 }
				}
				else
				{
					// DB not checked
					this.setIcon(Builder.getIcon("wait.png", 16));
				}
			}
				
		
		}

		
		return this;
	}
	
	
	

}
