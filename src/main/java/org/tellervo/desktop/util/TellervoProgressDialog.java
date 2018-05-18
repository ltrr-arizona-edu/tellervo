package org.tellervo.desktop.util;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;

import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;

import org.tellervo.desktop.ui.Builder;

public class TellervoProgressDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TellervoProgressDialog dialog = new TellervoProgressDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void initProgress(int max)
	{
		progressBar.setMaximum(max);
		this.setVisible(true);
		
	}
	
	public void setProgress(int value)
	{
		if(value<=progressBar.getMaximum())
		{
			progressBar.setValue(value);
		}
		else
		{
			progressBar.setIndeterminate(true);
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public TellervoProgressDialog() {
		this.setIconImage(Builder.getApplicationIcon());
		this.setLocationRelativeTo(null);
		this.setTitle("Processing...");
		setBounds(100, 100, 450, 127);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[148px,grow,fill]", "[][14px][grow,fill]"));
		{
			JLabel lblPleaseWait = new JLabel("Please wait...");
			contentPanel.add(lblPleaseWait, "cell 0 0");
		}
		{
			progressBar = new JProgressBar();
			contentPanel.add(progressBar, "cell 0 1,alignx left,aligny top");
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
