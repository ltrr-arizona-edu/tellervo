package org.tellervo.desktop.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.ui.Builder;

public class PasteErrorReportDialog extends JDialog implements ActionListener{

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JXTable tblErrors;
	private final DefaultTableModel errorTableModel;
	private Boolean continueAnyway = false;

	/**
	 * Create the dialog.
	 */
	public PasteErrorReportDialog(DefaultTableModel errorTableModel) {
		
		this.errorTableModel = errorTableModel;
		initGUI();
		
	}
	

	
	private void initGUI(){
		
		this.setModal(true);
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("Error Pasting");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow]", "[][grow]"));
		{
			JLabel lblNewLabel = new JLabel("");
			lblNewLabel.setIcon(Builder.getIcon("warning.png", 64));
			contentPanel.add(lblNewLabel, "cell 0 0");
		}
		{
			JLabel lblTheFollowingErrors = new JLabel("<html>There are problems pasting the requested data into the table.  You can choose to continue with the paste (in which case the erroneous fields will be left blank), or cancel so you can fix your data and try again.");
			contentPanel.add(lblTheFollowingErrors, "cell 1 0");
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, "cell 0 1 2 1,grow");
			{
				tblErrors = new JXTable();
				scrollPane.setViewportView(tblErrors);
				tblErrors.setModel(this.errorTableModel);		
				tblErrors.setAutoCreateRowSorter(true);
				tblErrors.getColumnModel().getColumn(0).setWidth(10);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnCancel = new JButton("Cancel");
				btnCancel.setActionCommand("Cancel");
				btnCancel.addActionListener(this);
				{
					JButton btnPasteAnyway = new JButton("Continue Anyway");
					btnPasteAnyway.setActionCommand("Continue");
					btnPasteAnyway.addActionListener(this);
					buttonPane.add(btnPasteAnyway);
				}
				buttonPane.add(btnCancel);
				getRootPane().setDefaultButton(btnCancel);
			}
		}
		
		this.setSize(new Dimension(1115, 493));
		
		tblErrors.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		tblErrors.getColumn(0).setMaxWidth(50);
		tblErrors.getColumn(1).setMaxWidth(200);
		tblErrors.getColumn(2).setMaxWidth(200);
		//this.pack();
		
		this.setLocationRelativeTo(App.mainWindow);
	}

	public Boolean getContinue()
	{
		return this.continueAnyway;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Cancel"))
		{
			this.setVisible(false);
		}
		else if (e.getActionCommand().equals("Continue"))
		{
			this.continueAnyway = true;
			this.setVisible(false);
		}
		
	}

}
