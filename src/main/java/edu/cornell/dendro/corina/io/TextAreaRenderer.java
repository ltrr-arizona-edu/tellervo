package edu.cornell.dendro.corina.io;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public class TextAreaRenderer extends JTextArea implements TableCellRenderer {
	
	  private Color grey = new Color(0.1f, 0.1f, 0.1f);
	  
	  public TextAreaRenderer() {
		    setLineWrap(true);
		    setWrapStyleWord(true);
		  }

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
		    setText((String) value);
		    
		    if(!isSelected)
		    {
			    if(row % 2 ==0)
				{
			    	
					this.setBackground(grey);
				}
			    else
			    {
					this.setBackground(Color.WHITE);
			    }
			}
		    else
		    {
		    	this.setBackground(table.getSelectionBackground());
		    }
	    
            int fontHeight = this.getFontMetrics(this.getFont()).getHeight();
            int textLength = this.getText().length();
            int lines = 1;
            if(this.getColumns()!=0)
            {
            	lines = textLength / this.getColumns() +1;//+1, cause we need at least 1 row.
            }

            int height = fontHeight * lines;                        
            this.setPreferredSize(new Dimension(this.getWidth(), height));
            
            
			return this;
		}


}

