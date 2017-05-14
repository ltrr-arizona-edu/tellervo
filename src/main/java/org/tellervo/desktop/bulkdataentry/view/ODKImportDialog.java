package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JCheckBox;

public class ODKImportDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextField txtBaseURI;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ODKImportDialog dialog = new ODKImportDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ODKImportDialog() {
		setBounds(100, 100, 450, 434);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][grow][]", "[][grow][grow]"));
		{
			JLabel lblOdkInstanceFolder = new JLabel("ODK folder:");
			contentPanel.add(lblOdkInstanceFolder, "cell 0 0,alignx trailing");
		}
		{
			textField = new JTextField();
			contentPanel.add(textField, "flowx,cell 1 0,growx");
			textField.setColumns(10);
		}
		{
			JButton btnBrowse = new JButton("Browse");
			contentPanel.add(btnBrowse, "cell 2 0");
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Multimedia files", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
			contentPanel.add(panel, "cell 0 1 3 1,grow");
			panel.setLayout(new MigLayout("", "[grow][]", "[][][][][]"));
			{
				JCheckBox chckbxIncludingOdkMultimedia = new JCheckBox("Including ODK multimedia files in import");
				panel.add(chckbxIncludingOdkMultimedia, "cell 0 0 2 1");
			}
			{
				JCheckBox chckbxRenameToMatch = new JCheckBox("Rename files to match database entries");
				panel.add(chckbxRenameToMatch, "cell 0 1 2 1");
			}
			{
				JLabel lblBaseUriWhere = new JLabel("Base URI where files will be accessed from:");
				panel.add(lblBaseUriWhere, "cell 0 2 2 1");
			}
			{
				txtBaseURI = new JTextField();
				panel.add(txtBaseURI, "cell 0 3,growx");
				txtBaseURI.setColumns(10);
			}
			{
				JButton btnBrowse_1 = new JButton("Browse");
				panel.add(btnBrowse_1, "cell 1 3");
			}
			{
				JLabel lblNewLabel = new JLabel("New label");
				panel.add(lblNewLabel, "cell 0 4");
			}
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "cell 0 2 3 1,grow");
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
