package org.tellervo.desktop.editor;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.tellervo.desktop.ui.Builder;
import java.awt.Font;

public class BasicMetadataPanel extends JPanel {
	public JTextField txtTitle;
	public JTextField txtKeycode;
	public JTextField txtSpecies;
	public JTextField txtAuthor;

	private JLabel lblNewLabel;
	private JLabel lblTheseBasicMetadata;
	private JTextArea txtWarning;
	private JLabel lblAuthor;

	/**
	 * Create the panel.
	 */
	public BasicMetadataPanel() {
		setLayout(new MigLayout("", "[84.00,right][grow]", "[][][][][grow][]"));
		
		JLabel lblTitle = new JLabel("Title:");
		add(lblTitle, "cell 0 0,alignx trailing");
		
		txtTitle = new JTextField();
		add(txtTitle, "cell 1 0,growx");
		txtTitle.setColumns(10);
		
		JLabel lblKeycode = new JLabel("Keycode:");
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
		
		txtWarning = new JTextArea();
		txtWarning.setFont(new Font("Dialog", Font.PLAIN, 10));
		txtWarning.setWrapStyleWord(true);
		txtWarning.setLineWrap(true);
		txtWarning.setText("These basic metadata fields simply provide you with a method of adding rudimentary metadata to legacy dendro file formats. They are not populated when you load legacy data files.");
		txtWarning.setEditable(false);
		txtWarning.setEditable(false);
		txtWarning.setOpaque(false);
		txtWarning.setBackground(new Color(0,0,0,0));
		txtWarning.setBorder(new EmptyBorder(0,0,0,0));
		txtWarning.setFocusable(false);
		add(txtWarning, "cell 1 5,growx");

	}

	public JTextField getTxtAuthor() {
		return txtAuthor;
	}
}
