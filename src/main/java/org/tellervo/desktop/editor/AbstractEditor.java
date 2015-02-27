package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
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
				
		final DefaultListModel model;
		model= new DefaultListModel();
		
		model.addElement(sampleList);
		
		Workspace_panel.setLayout(new MigLayout("", "[][][1px]", "[235px][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		Workspace_panel.add(scrollPane, "cell 0 0 3 1,grow");
		
		JList Data_matrix_list = new JList(model);
		scrollPane.setViewportView(Data_matrix_list);
		Data_matrix_list.setValueIsAdjusting(true);
		
		
		Data_matrix_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Data_matrix_list.setLayoutOrientation(JList.VERTICAL);
		Data_matrix_list.setVisibleRowCount(10);
		
		JButton ADD = new JButton("ADD");
		Workspace_panel.add(ADD, "cell 0 1");
		
		JButton REMOVE = new JButton("REMOVE");
		Workspace_panel.add(REMOVE, "cell 1 1");
		
		Data_matrix_list.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				itemSelected();
			}

			public void valueChanged1(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		
		
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
	
	public void itemSelected(){
	
	}
}
