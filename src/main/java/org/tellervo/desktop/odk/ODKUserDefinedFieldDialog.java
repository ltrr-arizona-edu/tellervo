package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringEscapeUtils;
import org.tellervo.desktop.odk.fields.ODKDataType;
import org.tellervo.desktop.odk.fields.ODKUserDefinedField;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;

public class ODKUserDefinedFieldDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtDescription;
	private JTextField txtName;
	private JComboBox<String> cboFieldType ;
	public boolean success  =false;
	private String[] datatypenames = {"String", 
			//"Choose from list", 
			"Integer", "Decimal", "Location", "Image", "Audio", "Video"};
	private DefaultComboBoxModel<String> dataTypeModel;
	private DefaultComboBoxModel<Class <? extends ITridas>> attachToModel;
	private JComboBox<Class <? extends ITridas>> cboAttachTo;
	private boolean attachToObject = true;
	
	/**
	 * Create the dialog.
	 */
	public ODKUserDefinedFieldDialog() {
	
		init();
	}

	public ODKUserDefinedFieldDialog(boolean attachToObject, JDialog parent) {
		
		this.attachToObject = attachToObject;
		init();
		this.setLocationRelativeTo(parent);
	}
	
	private void init()
	{
		this.setModal(true);
		this.setIconImage(Builder.getApplicationIcon());
		this.setTitle("User defined field");
		setBounds(100, 100, 486, 436);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("hidemode 3", "[right][grow]", "[][][][][]"));
		{
			JLabel lblFieldType = new JLabel("Field type:");
			contentPanel.add(lblFieldType, "cell 0 0,alignx trailing");
		}
		{
			cboFieldType = new JComboBox<String>();
			dataTypeModel = new DefaultComboBoxModel<String>();
			cboFieldType.setModel(dataTypeModel);
			for(String type : datatypenames)
			{
				dataTypeModel.addElement(type);
			}
			
			cboFieldType.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent evt) {
					
				}
				
			});
			
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
			JLabel lblAttachTo = new JLabel("Associate with:");
			contentPanel.add(lblAttachTo, "cell 0 3,alignx trailing");
			
			cboAttachTo = new JComboBox<Class <? extends ITridas>>();
			this.attachToModel = new DefaultComboBoxModel<Class <? extends ITridas>>();
			cboAttachTo.setModel(attachToModel);
			attachToModel.addElement(TridasElement.class);
			attachToModel.addElement(TridasSample.class);
			cboAttachTo.setSelectedIndex(0);
			
			cboAttachTo.setRenderer(new ListCellRenderer<Class<? extends ITridas>>(){

				@Override
				public Component getListCellRendererComponent(JList<? extends Class<? extends ITridas>> arg0,
						Class<? extends ITridas> arg1, int arg2, boolean arg3, boolean arg4) {
					
					String text = "Element";
					if( arg1.equals(TridasSample.class))
					{
						
						text = "Sample";
					}
					
					JPanel panel = new JPanel();
					panel.setLayout(new FlowLayout(FlowLayout.LEFT));
					JLabel label = new JLabel(text);
					label.setHorizontalAlignment(SwingConstants.LEFT);
					panel.add(label);
					return panel;
				}
				
			});
			
			if(this.attachToObject)
			{
				lblAttachTo.setVisible(false);
				cboAttachTo.setVisible(false);
			}
			
			contentPanel.add(cboAttachTo, "cell 1 3,growx");
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
	
	public String getFieldCode()
	{
		String name = this.txtName.getText().toLowerCase();
		name = StringEscapeUtils.escapeXml(name.replaceAll(" ", "_"));
		return "tellervo.user."+name;
	}
	
	public String getFieldName()
	{
		return this.txtName.getText();
	}
	
	public String getDescription()
	{
		return this.txtDescription.getText();
		
	}
	public Class <? extends ITridas> getAttachedTo()
	{
		if(attachToObject)
		{
			return TridasObject.class;
		}
		else
		{
			return (Class<? extends ITridas>) this.cboAttachTo.getSelectedItem();
		}
	}
	
	public ODKDataType getDataType()
	{
		if(cboFieldType.getSelectedItem().equals("String"))
		{
			return ODKDataType.STRING;
		}
		else if(cboFieldType.getSelectedItem().equals("Choose from list"))
		{
			return ODKDataType.SELECT_ONE;
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
	
	public ODKUserDefinedField getField()
	{
		ODKUserDefinedField userfield = new ODKUserDefinedField(getDataType(),
				getFieldCode(),
				getFieldName(),
				getDescription(),
				null,
				getAttachedTo()
				);
		
		if(getDataType().equals(ODKDataType.SELECT_ONE))
		{
			
		}
		
		return userfield;
	}

}

