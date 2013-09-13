package org.tellervo.desktop.editor;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class BasicMetadataPanel extends JPanel {
	public JTextField txtTitle;
	public JTextField txtKeycode;
	public JTextField txtSpecies;

	/**
	 * Create the panel.
	 */
	public BasicMetadataPanel() {
		setLayout(new MigLayout("", "[right][grow]", "[][][]"));
		
		JLabel lblTitle = new JLabel("Title");
		add(lblTitle, "cell 0 0,alignx trailing");
		
		txtTitle = new JTextField();
		add(txtTitle, "cell 1 0,growx");
		txtTitle.setColumns(10);
		
		JLabel lblKeycode = new JLabel("Keycode:");
		add(lblKeycode, "cell 0 1,alignx trailing");
		
		txtKeycode = new JTextField();
		add(txtKeycode, "cell 1 1,growx");
		txtKeycode.setColumns(10);
		
		JLabel lblSpecies = new JLabel("Species");
		add(lblSpecies, "cell 0 2,alignx trailing");
		
		txtSpecies = new JTextField();
		add(txtSpecies, "cell 1 2,growx");
		txtSpecies.setColumns(10);

	}

}
