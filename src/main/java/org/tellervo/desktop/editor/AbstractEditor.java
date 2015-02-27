package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTabbedPane;

public abstract class AbstractEditor extends JFrame {

	private JPanel contentPane;



	/**
	 * Create the frame.
	 */
	public AbstractEditor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow][grow]"));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, "cell 0 0,grow");
		
		JPanel Workspace_panel = new JPanel();
		splitPane.setLeftComponent(Workspace_panel);
		Workspace_panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JList Data_matrix_list = new JList();
		Workspace_panel.add(Data_matrix_list, "cell 0 0,grow");
		
		JPanel Main_panel = new JPanel();
		splitPane.setRightComponent(Main_panel);
		Main_panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		Main_panel.add(tabbedPane, "cell 0 0,grow");
		
		JPanel Data_panel = new JPanel();
		tabbedPane.addTab("Data", null, Data_panel, null);
		
		JPanel Metadata_panel = new JPanel();
		tabbedPane.addTab("Metadata", null, Metadata_panel, null);
		
		JPanel Status_panel = new JPanel();
		contentPane.add(Status_panel, "cell 0 1,grow");
	}

}
