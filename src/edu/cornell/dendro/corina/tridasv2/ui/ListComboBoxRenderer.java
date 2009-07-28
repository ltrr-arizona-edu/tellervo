/**
 * 
 */
package edu.cornell.dendro.corina.tridasv2.ui;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import edu.cornell.dendro.corina.ui.Builder;

/**
 * @author Lucas Madar
 *
 */
public class ListComboBoxRenderer extends JPanel implements
		TableCellRenderer {
	
	private static final long serialVersionUID = 1L;

	private ListComboBoxItemRenderer renderer;
	private JLabel dropdown;

	public ListComboBoxRenderer() {
		renderer = new ListComboBoxItemRenderer();
		
        Icon icon = Builder.getIcon("dropdown.png", Builder.ICONS, 22);
        if(icon == null)
        	dropdown = new JLabel("[...]");
        else
        	dropdown = new JLabel(icon);
        
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(renderer);
        add(Box.createHorizontalGlue());
        add(dropdown);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		
		dropdown.setVisible(table.isCellEditable(row, column));

		renderer.modifyComponent(value);
		
        if (isSelected) {
            renderer.setForeground(table.getSelectionForeground());
            renderer.setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
            renderer.setForeground(table.getForeground());
            renderer.setBackground(table.getBackground());
        }

        return this;
	}

}
