/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.sample.Element;

public class ElementListCellRenderer extends DefaultTableCellRenderer {
	private static final long serialVersionUID = 1L;

	/** Make odd rows have this background color */
	public static final Color BROWSER_ODD_ROW_COLOR = new Color(236, 243, 254);

	private ElementListManager manager;

	private boolean disableSelections;

	public ElementListCellRenderer(ElementListManager manager, boolean disableSelections) {
		this.manager = manager;
		this.disableSelections = disableSelections;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {

		Component c = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		// if it's been selected, grey it out
		ElementListTableModel model = (ElementListTableModel) table.getModel();
		Element element = model.getElementAt(row);
		if (disableSelections && manager.isElementDisabled(element)) {
			setForeground(UIManager.getColor("Label.disabledForeground"));
			setBackground(UIManager.getColor("Label.background"));
		} else {
			// every-other-line colors
			if (!isSelected) {
				setBackground(row % 2 == 0 ? BROWSER_ODD_ROW_COLOR : table
						.getBackground());
			}

			// default foreground
			setForeground(null);
		}

		// special case for isReconciled column
		if (column == 10) {
			if (value instanceof Boolean) {
				setToolTipText((Boolean) value ? "Not reconciled"
						: "Reconciled");
			}
		} else if (value != null) {
			setToolTipText(value.toString());
		} else {
			setToolTipText(null);
		}

		return c;
	}
}
