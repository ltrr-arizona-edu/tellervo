package org.tellervo.desktop.gis2;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class OpacitySpinnerRenderer extends AbstractCellEditor  implements TableCellEditor, TableCellRenderer  {
	JSpinner spinner = new JSpinner();
	JSpinner renderSpinner = new JSpinner();



	  public OpacitySpinnerRenderer() {
	    super();    
	    
	  }


	@Override
	public Object getCellEditorValue() {
		return spinner.getValue();
	}


	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		if (arg1 == null) {
		      renderSpinner.setValue(0);
		    } else {
		      int intValue = ((Integer) arg1).intValue();
		      renderSpinner.setValue(intValue);
		    }
		    return renderSpinner;
	}


	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
	    spinner.setValue(value);
	    return spinner;
	}
	  
	  
}
