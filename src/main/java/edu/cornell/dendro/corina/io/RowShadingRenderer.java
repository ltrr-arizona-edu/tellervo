package edu.cornell.dendro.corina.io;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class RowShadingRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
	
	private static final long serialVersionUID = -5231781208719739960L;
	private Color grey = new Color(0.9f, 0.9f, 0.9f);
	  
	  public RowShadingRenderer() {

		  }

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
		      
			Component cell = super.getTableCellRendererComponent(
                      table, value, isSelected, hasFocus, row, column);
		    
		    if(!isSelected)
		    {
			    if(row % 2 ==0)
				{
					cell.setBackground(Color.WHITE);
				}
			    else
			    {
					cell.setBackground(grey);
			    }
			}
		    else
		    {
		    	cell.setBackground(table.getSelectionBackground());
		    }
	 
			return cell;
		}


}

