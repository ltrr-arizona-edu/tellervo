package org.tellervo.desktop.admin;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class CurationPanel extends JPanel {
	private JTable table;

	/**
	 * Create the panel.
	 */
	public CurationPanel() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 0,grow");
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"1 Jan 2013", "Archived", "Brewer, Peter", "", null},
				{"1 Jul 2012", "On loan", "Brewer, Peter", null, null},
			},
			new String[] {
				"Date", "Status", "Curated by", "Notes", "Loan"
			}
		));

	}

}
