package org.tellervo.desktop.editor;

import java.awt.Component;
import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SeriesDataMatrixEditor extends DefaultCellEditor {



		  public SeriesDataMatrixEditor() {
		    super(new JTextField());
		    ((JTextField)this.getComponent()).setBorder(null);
		    
		  }

		  public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
		      int row, int column) {
		    JTextField editor = (JTextField) super.getTableCellEditorComponent(table, value, isSelected,
		        row, column);
  
		    
		    if (value != null)
		      editor.setText(value.toString());
		    if (column == 0) {
		      editor.setHorizontalAlignment(SwingConstants.CENTER);
		      editor.setFont(new Font("Serif", Font.BOLD, 14));
		    } else {
		      editor.setHorizontalAlignment(SwingConstants.RIGHT);
		      editor.setFont(new Font("Serif", Font.ITALIC, 12));
		    }
		    return editor;
		  }
		
	
}
