package org.tellervo.desktop.metadataexport;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;

import org.tellervo.desktop.ui.Alert;
import javax.swing.ListSelectionModel;

public class MetadataExportTemplateDesigner extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private MetadataExportColumnsTableModel tablemodel;
	private JTextField textField;
	private JButton btnAddColumn;
	private JButton btnRemoveColumn;
	private JButton btnUp;
	private JButton btnDown;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MetadataExportTemplateDesigner dialog = new MetadataExportTemplateDesigner();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MetadataExportTemplateDesigner() {
		setBounds(100, 100, 761, 489);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][]", "[][grow]"));
		{
			JPanel panel2 = new JPanel();
			contentPanel.add(panel2, "cell 0 0 2 1,grow");
			panel2.setLayout(new MigLayout("", "[right][grow]", "[][][]"));
			{
				JLabel lblTemplateName = new JLabel("Template name:");
				panel2.add(lblTemplateName, "cell 0 0,alignx trailing");
			}
			{
				textField = new JTextField();
				panel2.add(textField, "cell 1 0,growx");
				textField.setColumns(10);
			}
			{
				JLabel lblTemplateForExporting = new JLabel("For exporting:");
				panel2.add(lblTemplateForExporting, "cell 0 1,alignx trailing");
			}
			{
				JComboBox comboBox = new JComboBox();
				comboBox.setModel(new DefaultComboBoxModel(new String[] {"Samples"}));
				panel2.add(comboBox, "cell 1 1,growx");
			}
		}
		{
			{
				JScrollPane scrollPane = new JScrollPane();
				contentPanel.add(scrollPane, "cell 0 1,grow");
				table = new JTable();
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				scrollPane.setViewportView(table);
				tablemodel = new MetadataExportColumnsTableModel();
				table.setModel(tablemodel);
				table.getColumnModel().getColumn(2).setCellEditor(new CompositeMetadataFieldEditor());
				table.setRowHeight(20);
				{
					JPanel panel = new JPanel();
					contentPanel.add(panel, "cell 1 1,grow");
					panel.setLayout(new MigLayout("", "[fill]", "[][][][]"));
					{
						btnAddColumn = new JButton("+");
						btnAddColumn.setActionCommand("AddColumn");
						btnAddColumn.addActionListener(this);
						panel.add(btnAddColumn, "cell 0 0");
					}
					{
						btnRemoveColumn = new JButton("-");
						btnRemoveColumn.setActionCommand("RemoveColumn");
						btnRemoveColumn.addActionListener(this);
						panel.add(btnRemoveColumn, "cell 0 1");
					}
					{
						btnUp = new JButton("Up");
						btnUp.setActionCommand("ColumnUp");
						btnUp.addActionListener(this);
						panel.add(btnUp, "cell 0 2");
					}
					{
						btnDown = new JButton("Down");
						btnDown.setActionCommand("ColumnDown");
						btnDown.addActionListener(this);
						panel.add(btnDown, "cell 0 3");
					}
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton("Save");
				btnSave.setActionCommand("Save");
				btnSave.addActionListener(this);
				buttonPane.add(btnSave);
				getRootPane().setDefaultButton(btnSave);
			}
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				buttonPane.add(btnCancel);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("Save"))
		{
			
		}
		else if(evt.getActionCommand().equals("Cancel"))
		{
			
		}
		else if(evt.getActionCommand().equals("AddColumn"))
		{
			tablemodel.addRow();	
			
		}
		else if(evt.getActionCommand().equals("RemoveColumn"))
		{
			int row = table.getSelectedRow();
			try{
			tablemodel.deleteRow(row);
			} catch (IndexOutOfBoundsException e){
				
				Alert.error("Error", e.getLocalizedMessage());
			}
			
			
		}
		else if(evt.getActionCommand().equals("ColumnUp"))
		{
			
		}
		else if(evt.getActionCommand().equals("ColumnDown"))
		{
			
		}
	}


}
