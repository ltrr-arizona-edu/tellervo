package org.tellervo.desktop.editor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;

public class BasicMetadataPanel extends AbstractMetadataPanel {

	private static final long serialVersionUID = 1L;
	public JTextField txtTitle;
	public JTextField txtKeycode;
	public JTextField txtSpecies;
	public JTextField txtAuthor;

	private JLabel lblNewLabel;
	private JLabel lblTheseBasicMetadata;
	private JLabel txtWarning;
	private JLabel lblAuthor;

	/**
	 * Create the panel.
	 */
	public BasicMetadataPanel() {
		setLayout(new MigLayout("", "[84.00,right][grow]", "[][][][][grow][]"));
		
		JLabel lblTitle = new JLabel("Title / Series name:");
		add(lblTitle, "cell 0 0,alignx trailing");
		
		txtTitle = new JTextField();
		add(txtTitle, "cell 1 0,growx");
		txtTitle.setColumns(10);
		
		JLabel lblKeycode = new JLabel("Sample code / Keycode:");
		add(lblKeycode, "cell 0 1,alignx trailing");
		
		txtKeycode = new JTextField();
		add(txtKeycode, "cell 1 1,growx");
		txtKeycode.setColumns(10);
		
		JLabel lblSpecies = new JLabel("Species:");
		add(lblSpecies, "cell 0 2,alignx trailing");
		
		txtSpecies = new JTextField();
		add(txtSpecies, "cell 1 2,growx");
		txtSpecies.setColumns(10);
		
		lblAuthor = new JLabel("Author:");
		add(lblAuthor, "cell 0 3,alignx trailing");
		
		txtAuthor = new JTextField();
		add(txtAuthor, "cell 1 3,growx");
		txtAuthor.setColumns(10);
		
		lblNewLabel = new JLabel();
		lblNewLabel.setIcon(Builder.getIcon("warning.png", 22));
		add(lblNewLabel, "cell 0 5,aligny top");
		
		lblTheseBasicMetadata = new JLabel("");
		add(lblTheseBasicMetadata, "flowx,cell 1 5");
		
		txtWarning = new JLabel();
		txtWarning.setFont(new Font("Dialog", Font.PLAIN, 10));

		txtWarning.setText("<html>These basic metadata fields simply provide you with a method of adding rudimentary metadata to legacy dendro file formats. They are supported sporadically" +
				" depending on the format you save to and therefore may be truncated or omitted. When you load legacy files into Tellervo-<i>lite</i> unsupported" +
				" fields will be ignored so you may prefer to make copies of files before editing.");

		txtWarning.setOpaque(false);
		txtWarning.setBackground(new Color(0,0,0,0));
		txtWarning.setBorder(new EmptyBorder(0,0,0,0));
		txtWarning.setFocusable(false);
		add(txtWarning, "cell 1 5,growx");

	}

	public void populateFromSample(Sample s)
	{
		if(s==null) return;
		
		txtAuthor.setText(s.getMetaString(Metadata.AUTHOR));
		txtKeycode.setText(s.getMetaString(Metadata.KEYCODE));
		
		if(!s.getMeta(Metadata.TITLE).equals("New entry: [New series]"))
		{
			txtTitle.setText(s.getSeries().getTitle());
		}
		
		if(s.getMetaString(Metadata.SPECIES)!="Plantae")
		{
			txtSpecies.setText(s.getMetaString(Metadata.SPECIES));
		}
		
		
	}
	
	/**
	 * Have any of the metadata fields been set?
	 * 
	 * @return
	 */
	public Boolean isMetadataSet()
	{
		if(txtTitle.getText().isEmpty() 
				&& txtKeycode.getText().isEmpty()
				&& txtSpecies.getText().isEmpty()
				&& txtAuthor.getText().isEmpty()
		)
		{
			return false;
		}
		
		return true;
	}
	

}
