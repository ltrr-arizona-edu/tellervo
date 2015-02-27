package org.tellervo.desktop.editor;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import net.miginfocom.swing.MigLayout;

public class DataPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public DataPanel() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane);
		
		JPanel panel = new JPanel();
		splitPane.setLeftComponent(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel Measuring_panel = new JPanel();
		panel.add(Measuring_panel, BorderLayout.WEST);
		
		JPanel SeriesDataMatrix = new JPanel();
		panel.add(SeriesDataMatrix, BorderLayout.CENTER);
		SeriesDataMatrix.setLayout(new BorderLayout(0, 0));
		
		JPanel Graph_Panel = new JPanel();
		splitPane.setRightComponent(Graph_Panel);
		
		
		
		splitPane.setDividerLocation(0.8);


	}

}
