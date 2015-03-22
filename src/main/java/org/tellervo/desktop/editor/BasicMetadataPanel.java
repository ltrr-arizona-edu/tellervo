package org.tellervo.desktop.editor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Builder;

public class BasicMetadataPanel extends AbstractMetadataPanel implements DocumentListener{

	private static final Logger log = LoggerFactory.getLogger(BasicMetadataPanel.class);

	private static final long serialVersionUID = 1L;
	public JTextField txtTitle;
	public JTextField txtKeycode;
	public JTextField txtSpecies;
	public JTextField txtAuthor;

	private JLabel lblNewLabel;
	private JLabel lblTheseBasicMetadata;
	private JLabel txtWarning;
	private JLabel lblAuthor;
	private Sample sample;
	private boolean listenersActive = false;
	
	
	
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

		addListeners();
	}
	
	private void addListeners()
	{
		txtAuthor.getDocument().addDocumentListener(this);
		txtKeycode.getDocument().addDocumentListener(this);
		txtTitle.getDocument().addDocumentListener(this);
		txtSpecies.getDocument().addDocumentListener(this);
		
		listenersActive = true;
	}
	
	/**
	 * 
	 * @param s
	 */
	public void setSample(Sample s)
	{
		listenersActive = false;
		
		sample = s;
		
		txtAuthor.setText("");
		txtKeycode.setText("");
		txtTitle.setText("");
		txtSpecies.setText("");
		
		if(sample==null) return;
		
		txtAuthor.setText(sample.getMetaString(Metadata.AUTHOR));
		txtKeycode.setText(sample.getMetaString(Metadata.KEYCODE));
		
		txtTitle.setText(sample.getMetaString(Metadata.TITLE));
		
		
		if(sample.getMetaString(Metadata.SPECIES)!="Plantae")
		{
			txtSpecies.setText(sample.getMetaString(Metadata.SPECIES));
		}
		
		listenersActive = true;
	}
	
	private void updateSample()
	{
		if(!listenersActive) return;
		
		if(sample==null)
		{
			log.error("Sample is null so can't update");
		}
		
		sample.getSeries().setTitle(txtTitle.getText());
		sample.setMeta(Metadata.TITLE, txtTitle.getText());
		sample.setMeta(Metadata.AUTHOR, txtAuthor.getText());
		sample.setMeta(Metadata.SPECIES, txtSpecies.getText());
		sample.setMeta(Metadata.KEYCODE, txtKeycode.getText());
		
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

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateSample();
		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateSample();
		
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateSample();
		
	}
	

}
