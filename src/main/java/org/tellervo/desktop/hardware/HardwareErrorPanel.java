package org.tellervo.desktop.hardware;

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.JTextArea;

public class HardwareErrorPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public HardwareErrorPanel(String message) {
		setLayout(new MigLayout("", "[grow,fill]", "[][grow]"));
		
		JLabel txtWarning = new JLabel("There was an error communicating with your measuring platform");
		txtWarning.setForeground(Color.RED);
		add(txtWarning, "cell 0 0");
		setBackground(Color.WHITE);
		setOpaque(false);
		
		
		JTextArea txtError = new JTextArea();
		txtError.setWrapStyleWord(true);
		txtError.setLineWrap(true);
		txtError.setText(message);
		txtError.setEditable(false);
		txtError.setOpaque(false);
		txtError.setBorder(null);
		
		add(txtError, "cell 0 1,grow");

	}

}
