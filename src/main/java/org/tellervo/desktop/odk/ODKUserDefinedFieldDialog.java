package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.odk.fields.ODKDataType;

import net.miginfocom.swing.MigLayout;

public class ODKUserDefinedFieldDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	public JTextField txtDescription;
	public JTextField txtName;
	private JComboBox<String> cboFieldType ;
	public boolean success  =false;
	private String[] datatypenames = {"String", "Integer", "Decimal", "Location", "Image", "Audio", "Video"};
	
	
	/**
	 * Create the dialog.
	 */
	public ODKUserDefinedFieldDialog() {
		this.setModal(true);
		
		setBounds(100, 100, 450, 205);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[right][grow]", "[][][]"));
		{
			JLabel lblFieldType = new JLabel("Field type:");
			contentPanel.add(lblFieldType, "cell 0 0,alignx trailing");
		}
		{
			cboFieldType = new JComboBox<String>(datatypenames);
			contentPanel.add(cboFieldType, "cell 1 0,growx");
		}
		{
			JLabel lblFieldName = new JLabel("Name:");
			contentPanel.add(lblFieldName, "cell 0 1,alignx trailing");
		}
		{
			txtName = new JTextField();
			contentPanel.add(txtName, "cell 1 1,growx");
			txtName.setColumns(10);
		}
		{
			JLabel lblDescription = new JLabel("Description");
			contentPanel.add(lblDescription, "cell 0 2,alignx trailing");
		}
		{
			txtDescription = new JTextField();
			contentPanel.add(txtDescription, "cell 1 2,growx");
			txtDescription.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						
						success=true;
						
						setVisible(false);
						
					}
					
					
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						
						success=false;
						
						setVisible(false);
						
					}
					
					
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	public ODKDataType getDataType()
	{
		if(cboFieldType.getSelectedItem().equals("String"))
		{
			return ODKDataType.STRING;
		}
		else if(cboFieldType.getSelectedItem().equals("Integer"))
		{
			return ODKDataType.INTEGER;
		}
		else if(cboFieldType.getSelectedItem().equals("Decimal"))
		{
			return ODKDataType.DECIMAL;
		}
		else if(cboFieldType.getSelectedItem().equals("Location"))
		{
			return ODKDataType.LOCATION;
		}
		else if(cboFieldType.getSelectedItem().equals("Image"))
		{
			return ODKDataType.IMAGE;
		}
		else if(cboFieldType.getSelectedItem().equals("Audio"))
		{
			return ODKDataType.AUDIO;
		}
		else if(cboFieldType.getSelectedItem().equals("Video"))
		{
			return ODKDataType.VIDEO;
		}
		else
		{
			return ODKDataType.STRING;
		}
		
	}

}
