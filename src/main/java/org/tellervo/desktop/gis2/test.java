package org.tellervo.desktop.gis2;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;

public class test extends JPanel {

	/**
	 * Create the panel.
	 */
	public test() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane);
		
		JPanel panelLeft = new JPanel();
		splitPane.setLeftComponent(panelLeft);
		
		JPanel panelRight = new JPanel();
		splitPane.setRightComponent(panelRight);

	}

}
