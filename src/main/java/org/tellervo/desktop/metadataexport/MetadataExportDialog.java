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

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.TitledBorder;

public class MetadataExportDialog extends JDialog implements ActionListener  {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtFolder;
	private JComboBox<MetadataExportTemplate> cboTemplate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MetadataExportDialog dialog = new MetadataExportDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MetadataExportDialog() {
		setTitle("Export Metadata");
		setBounds(100, 100, 572, 407);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[546px,grow,fill]", "[133px][184px,grow]"));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Template", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, "cell 0 0,grow");
			panel.setLayout(new MigLayout("", "[135px:135.00,right][grow,fill][fill]", "[][][]"));
			{
				JLabel lblTemplateFolder = new JLabel("Template folder:");
				panel.add(lblTemplateFolder, "cell 0 0");
			}
			{
				txtFolder = new JTextField();
				panel.add(txtFolder, "cell 1 0");
				txtFolder.setColumns(10);
			}
			{
				JButton btnBrowse = new JButton("Browse");
				btnBrowse.addActionListener(this);
				btnBrowse.setActionCommand("BrowseTemplateFolder");
				panel.add(btnBrowse, "cell 2 0");
			}
			{
				JLabel lblTemplate = new JLabel("Template:");
				panel.add(lblTemplate, "cell 0 1");
			}
			{
				cboTemplate = new JComboBox<MetadataExportTemplate>();
				panel.add(cboTemplate, "cell 1 1");
			}
			{
				JButton btnEdit = new JButton("Edit");
				btnEdit.addActionListener(this);
				btnEdit.setActionCommand("EditTemplate");
				panel.add(btnEdit, "cell 2 1");
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, "cell 1 2,alignx left,growy");
				panel_1.setLayout(new MigLayout("", "[left]", "[]"));
				{
					JButton btnCreateTemplate = new JButton("Create new template");
					btnCreateTemplate.addActionListener(this);
					btnCreateTemplate.setActionCommand("CreateTemplate");
					panel_1.add(btnCreateTemplate, "cell 0 0,alignx left");
				}
			}
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, "cell 0 1,grow");
			panel.setLayout(new MigLayout("", "[135px:135px,right][grow,fill][]", "[grow,top][]"));
			{
				JLabel lblExportItems = new JLabel("Export items:");
				panel.add(lblExportItems, "cell 0 0,aligny top");
			}
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, "cell 1 0,grow");
				{
					JList lstItems = new JList();
					scrollPane.setViewportView(lstItems);
				}
			}
			{
				JPanel panel_1 = new JPanel();
				panel.add(panel_1, "cell 2 0,aligny top");
				panel_1.setLayout(new MigLayout("", "[fill]", "[][]"));
				{
					JButton btnAdd = new JButton("+");
					panel_1.add(btnAdd, "cell 0 0");
				}
				{
					JButton btnRemove = new JButton("-");
					panel_1.add(btnRemove, "cell 0 1");
				}
			}
			{
				JLabel lblFormat = new JLabel("Format:");
				panel.add(lblFormat, "cell 0 1");
			}
			{
				JComboBox cboFormat = new JComboBox();
				panel.add(cboFormat, "cell 1 1 2 1");
				cboFormat.setModel(new DefaultComboBoxModel(new String[] {"Microsoft Excel 2007-2013 (.xlsx)", "ODF Spreadsheet (.odf)", "Comma separated values (.csv)"}));
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Export");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().equals("CreateTemplate"))
		{
			MetadataExportTemplateDesigner designer = new MetadataExportTemplateDesigner();
			designer.setVisible(true);
		}
		else if(evt.getActionCommand().equals("EditTemplate"))
		{
			MetadataExportTemplate template = (MetadataExportTemplate) cboTemplate.getSelectedItem();
			
			if(template!=null)
			{		
				MetadataExportTemplateDesigner designer = new MetadataExportTemplateDesigner();
				designer.setVisible(true);
			}
			
		}
	}


}
