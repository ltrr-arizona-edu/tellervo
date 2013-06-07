package org.tellervo.desktop.admin;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JToolBar;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JSplitPane;
import javax.swing.JSeparator;

public class LoanDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LoanDialog dialog = new LoanDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LoanDialog() {
		setBounds(100, 100, 498, 520);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			contentPanel.add(splitPane, "cell 0 0,grow");
			{
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setMinimumSize(new Dimension(100,100));
				splitPane.setLeftComponent(scrollPane);
				{
					table = new JTable();
					table.setMinimumSize(new Dimension(100,100));
					table.setModel(new DefaultTableModel(
						new Object[][] {
							{"Peter", "Brewer", "LTRR", "01/05/2013", "01/05/2014"},
							{"Joe", "Bloggs", "ACME University", "01/01/2013", "01/01/2015"},
						},
						new String[] {
							"First name", "Last name", "Organisation", "Issue date", "Due data"
						}
					));
					scrollPane.setViewportView(table);
				}
			}
			
			{
				LoanPanel panel = new LoanPanel();
				splitPane.setRightComponent(panel);
				panel.setBorder(null);
			}
		}
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		{
			JButton btnNewLoan = new JButton("New");
			toolbar.add(btnNewLoan);
		}
		{
			JButton btnReturn = new JButton("Return");
			toolbar.add(btnReturn);
		}
		{
			JSeparator separator = new JSeparator();
			toolbar.add(separator);
		}
		{
			JButton btnNewButton = new JButton("Current");
			toolbar.add(btnNewButton);
		}
		{
			JButton btnNewButton_1 = new JButton("Delinquent");
			toolbar.add(btnNewButton_1);
		}
		{
			JButton btnNewButton_2 = new JButton("All");
			toolbar.add(btnNewButton_2);
		}
		{
			JButton btnNewButton_3 = new JButton("Search");
			toolbar.add(btnNewButton_3);
		}
	}

}
