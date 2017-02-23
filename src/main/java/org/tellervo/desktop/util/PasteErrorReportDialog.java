package org.tellervo.desktop.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXTable;
import org.tellervo.desktop.ui.Builder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;

public class PasteErrorReportDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JXTable tblErrors;
	private final DefaultTableModel errorTableModel;

	/**
	 * Create the dialog.
	 */
	public PasteErrorReportDialog(DefaultTableModel errorTableModel) {
		
		this.errorTableModel = errorTableModel;
		initGUI();
		
	}
	

	
	private void initGUI(){
		setBounds(100, 100, 450, 300);
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Error Pasting");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][grow]"));
		{
			JLabel lblTheFollowingErrors = new JLabel("<html>The following errors were found when pasting data.  Fields with erroneous data have been left blank");
			contentPanel.add(lblTheFollowingErrors, "cell 0 1");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 2,grow");
			{
				tblErrors = new JXTable();
				scrollPane.setViewportView(tblErrors);
				tblErrors.setModel(this.errorTableModel);		
				tblErrors.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				tblErrors.setAutoCreateRowSorter(true);
				tblErrors.getColumnModel().getColumn(0).setWidth(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("OK"))
		{
			this.dispose();
		}
		
	}

}
