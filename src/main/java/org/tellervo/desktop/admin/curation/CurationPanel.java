package org.tellervo.desktop.admin.curation;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class CurationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable table;
	private JTextField txtBox;
	private JTextField txtStorageLocation;
	private CurationTableModel curationTableModel;

	/**
	 * Create the panel.
	 */
	public CurationPanel() {
		
		setupGUI();
		
	}
	
	
	private void setupGUI()
	{
		setLayout(new MigLayout("", "[54.00,grow,right][grow]", "[][74.00,grow]"));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Box details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel, "cell 0 0 2 1,grow");
		panel.setLayout(new MigLayout("", "[135px:135px,right][grow,fill]", "[][]"));
		
		JLabel lblBox = new JLabel("Name");
		panel.add(lblBox, "cell 0 0");
		
		txtBox = new JTextField();
		panel.add(txtBox, "cell 1 0");
		txtBox.setColumns(10);
		
		JLabel lblStorageLocation = new JLabel("Storage location:");
		panel.add(lblStorageLocation, "cell 0 1");
		
		txtStorageLocation = new JTextField();
		panel.add(txtStorageLocation, "cell 1 1");
		txtStorageLocation.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(99, 130, 191)), "Curation details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		add(panel_1, "cell 0 1 2 1,grow");
		panel_1.setLayout(new MigLayout("", "[135px:135px,right][grow,fill]", "[][][]"));
		
		JLabel lblCurationStatus = new JLabel("Current status:");
		panel_1.add(lblCurationStatus, "cell 0 0");
		
		JComboBox cboCurationStatus = new JComboBox();
		panel_1.add(cboCurationStatus, "flowx,cell 1 0");
		
		JLabel lblCurationHistory = new JLabel("History:");
		panel_1.add(lblCurationHistory, "cell 0 1");
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "cell 1 1");
		
		table = new JTable();
		curationTableModel = new CurationTableModel();
		
		 
		
		scrollPane.setViewportView(table);
		table.setModel(curationTableModel);
		
		JButton btnApply = new JButton("Apply");
		btnApply.setMinimumSize(new Dimension(70,10));
		btnApply.setMaximumSize(new Dimension(70,70));
		panel_1.add(btnApply, "cell 1 0");
	}

}
