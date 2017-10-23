package com.rediscov.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;

public class ICMSImportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;

	


	public ICMSImportDialog() {

		FileNameExtensionFilter filter = new FileNameExtensionFilter("ICMS XML export file", "xml");
		
		File lastFolder = null;
		try{
			lastFolder = new File(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
		} catch (Exception e){}
		
		JFileChooser fc = new JFileChooser(lastFolder);
		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);
		fc.setMultiSelectionEnabled(false);

    	ICMSImporter importer = new ICMSImporter(fc.getSelectedFile().getAbsolutePath());
		importer.doImport();
		
		
		
		initGUI();
		
		
		
		
		this.setVisible(true);
		
	}
	
	/**
	 * Create the dialog.
	 */
	private void initGUI(){
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				model = new DefaultTableModel(
					new Object[][] {
					},
					new String[] {
						"Catalog Code", "Success", "Message" 
					}
				) {
					Class[] columnTypes = new Class[] {
						String.class, Object.class, String.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
					boolean[] columnEditables = new boolean[] {
						false, true, true
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				};
				
				table.setModel(model);
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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

}
