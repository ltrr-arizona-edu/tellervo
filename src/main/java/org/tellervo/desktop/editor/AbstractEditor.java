package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleListener;

public abstract class AbstractEditor extends JFrame implements SaveableDocument, PrefsListener,
SampleListener {

	private final static Logger log = LoggerFactory.getLogger(AbstractEditor.class);
	protected ArrayList<Sample> sampleList;
	private JPanel contentPane;

	
	public AbstractEditor(Sample sample) {
		// copy data ref
		this.sampleList = new ArrayList<Sample>();
		this.sampleList.add(sample);

		init();

	}
	
	public AbstractEditor(ArrayList<Sample> samples)
	{
		if(samples!=null) 
		{
			this.sampleList = samples;
		}
		else
		{
			this.sampleList = new ArrayList<Sample>();
		}
		
		
		init();

	}
	

	/**
	 * Create the frame.
	 */
	public AbstractEditor() {
		
		this.sampleList = new ArrayList<Sample>();
		init();
	}

	
	
	protected void init()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		
		JPanel Workspace_panel = new JPanel();
		splitPane.setLeftComponent(Workspace_panel);
		Workspace_panel.setLayout(new MigLayout("", "[grow]", "[grow]"));
			
		
		JList Data_matrix_list = new JList(sampleList);
		Data_matrix_list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		Data_matrix_list.setLayoutOrientation(JList.);
		Data_matrix_list.setVisibleRowCount(-1);
		
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
		
		
	}
}
