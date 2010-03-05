package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import edu.cornell.dendro.corina.tridasv2.ui.support.NotPresent;
import edu.cornell.dendro.corina.util.ColorUtils;

public abstract class AbstractComboBoxRenderer extends JPanel implements
		TableCellRenderer {

	private static final long serialVersionUID = 1L;

	/**
	 * @return The renderer to use to render cells
	 */
	abstract public ComboBoxItemRenderer getRenderer();
	
	/**
	 * @return the drop down box
	 */
	abstract public JComponent getDropdownBox();
	
	/**
	 * @return true if this is a required attribute
	 */
	abstract public boolean isRequired();
	
	/**
	 * Get the table cell renderer for this attribute
	 */
	public final Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		// get the renderer
		ComboBoxItemRenderer renderer = getRenderer();
		JComponent dropdown = getDropdownBox();
		
		dropdown.setVisible(table.isCellEditable(row, column));

		renderer.modifyComponent(value);
		
        if (isSelected) {
            renderer.setForeground(table.getSelectionForeground());
            setForeground(table.getSelectionForeground());
            
            // highlight bad values
            if(isRequired() && (value == null || value instanceof NotPresent)) {
            	Color blend = ColorUtils.blend(table.getSelectionBackground(), Color.red);
            	renderer.setBackground(blend);
            	super.setBackground(blend);
            }
            else {
            	renderer.setBackground(table.getSelectionBackground());
            	super.setBackground(table.getSelectionBackground());
            }
        } else {
            setForeground(table.getForeground());
            renderer.setForeground(table.getForeground());

            // highlight bad values
            if(isRequired() && (value == null || value instanceof NotPresent)) {
            	renderer.setBackground(Color.red);
            	setBackground(Color.red);
            }
            else {
            	setBackground(table.getBackground());
            	renderer.setBackground(table.getBackground());
            }
        }

        return this;
	}
}
