package edu.cornell.dendro.corina.admin.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.tridas.interfaces.ITridas;

import edu.cornell.dendro.corina.ui.Builder;

public class PermissionByEntityDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public PermissionByEntityDialog(ITridas entity) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Close");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		
		PermissionByEntityUI panel = new PermissionByEntityUI(entity);
		
		if(panel.getUserPermissionsList() == null)
		{
			this.dispose();
			return;
		}
		
		setTitle("Access Permissions");
		setIconImage(Builder.getApplicationIcon());
		getContentPane().add(panel);
		pack();
		setSize(750, 200);
		
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public static void showDialog(ITridas entity)
	{
		PermissionByEntityDialog dialog = new PermissionByEntityDialog(entity);
		
	}

}
