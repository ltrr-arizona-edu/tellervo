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

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.actions.ExportDataAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.gui.menus.actions.GraphSeriesAction;
import org.tellervo.desktop.gui.menus.actions.InitDataGridAction;
import org.tellervo.desktop.gui.menus.actions.MeasureToggleAction;
import org.tellervo.desktop.gui.menus.actions.MetadatabaseBrowserAction;
import org.tellervo.desktop.gui.menus.actions.PrintAction;
import org.tellervo.desktop.gui.menus.actions.RemarkToggleAction;
import org.tellervo.desktop.gui.menus.actions.SaveAction;
import org.tellervo.desktop.gui.menus.actions.TruncateAction;
import org.tellervo.desktop.gui.widgets.TitlelessButton;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.MeasuringDeviceSelector;
import org.tellervo.desktop.io.control.IOController;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.JPopupMenu;

import java.awt.Button;

public abstract class AbstractEditor extends JFrame implements SaveableDocument, PrefsListener,
SampleListener {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AbstractEditor.class);
	
	private JPanel contentPane;
	
	private EditorMeasurePanel measurePanel = null;
	private SeriesDataMatrix dataView; 
	private int measuringPanelWidth = 340;
	protected DefaultListModel model;
	private JList Data_matrix_list;
	
	
	public AbstractEditor(Sample sample) {
		
		// copy data ref
		//this.sampleList = new ArrayList<Sample>();
		//this.sampleList.add(sample);

		init();
		initbar();

	}
	
	public AbstractEditor(ArrayList<Sample> samples)
	{
		/*if(samples!=null) 
		{
			this.sampleList = samples;
		}
		else
		{
			this.sampleList = new ArrayList<Sample>();
		}*/
		
		
		init();
		initbar();

	}
	

	/**
	 * Create the frame.
	 */
	public AbstractEditor() {
		
		//this.sampleList = new ArrayList<Sample>();
		init();
		initbar();
	}

	
	/**
	 * Initalise the GUI
	 */
	protected void init()
	{
		
		this.setIconImage(Builder.getApplicationIcon());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[424px,grow,fill]", "[20px:20.00px][94.00][214px,grow,fill][16px]"));
		
		
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, "cell 0 2,grow");
		
		JPanel Workspace_panel = new JPanel();
		splitPane.setLeftComponent(Workspace_panel);
				
		
		model= new DefaultListModel();
		
		//model.addElement(sampleList);
		
		Workspace_panel.setLayout(new MigLayout("", "[133.00,grow,fill][142.00,grow,fill]", "[235px,grow,fill][fill]"));
		
		JScrollPane scrollPane = new JScrollPane();
		Workspace_panel.add(scrollPane, "cell 0 0 2 1,grow");
		
		Data_matrix_list = new JList(model);
		scrollPane.setViewportView(Data_matrix_list);
		Data_matrix_list.setValueIsAdjusting(true);
		
		
		Data_matrix_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Data_matrix_list.setLayoutOrientation(JList.VERTICAL);
		Data_matrix_list.setVisibleRowCount(10);
		
		JButton ADD = new JButton();
		ADD.setIcon(Builder.getIcon("edit_add.png", 16));
		Workspace_panel.add(ADD, "cell 0 1");
		
		JButton REMOVE = new JButton();
		REMOVE.setIcon(Builder.getIcon("edit_cancel.png", 16));
		Workspace_panel.add(REMOVE, "cell 1 1");
		
		Data_matrix_list.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				itemSelected();
			}		
			
		});
				
		
		JPanel Main_panel = new JPanel();
		splitPane.setRightComponent(Main_panel);
		Main_panel.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		Main_panel.add(tabbedPane, "cell 0 0,grow");
		
		JPanel dataPanel = new JPanel();
		tabbedPane.addTab("Data", Builder.getIcon("edit_data.png", 16), dataPanel, null);
		dataPanel.setLayout(new BorderLayout(0, 0));
		dataView = new SeriesDataMatrix(getSample(), this);
		dataPanel.add(dataView, BorderLayout.CENTER);
		
		JPanel Metadata_panel = new JPanel();
		tabbedPane.addTab("Metadata", Builder.getIcon("edit_database.png", 16), Metadata_panel, null);
		
		JMenuBar menuBar = new JMenuBar();
		contentPane.add(menuBar, "cell 0 0,growx,aligny top");
		
		JMenu File = new JMenu("File");
		menuBar.add(File);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenu mnAdministration = new JMenu("Administration");
		menuBar.add(mnAdministration);
		
		JMenu mnView = new JMenu("View");
		menuBar.add(mnView);
		
		JMenu mnTools = new JMenu("Tools");
		menuBar.add(mnTools);
		
		JMenu mnGraph = new JMenu("Graph");
		menuBar.add(mnGraph);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		
	}


	
	
	
	
	protected void initbar()
	{
		
		JToolBar toolBar = new JToolBar();
		
		
		// File Buttons
		Action fileOpenAction = new FileOpenAction(this);
		AbstractButton fileOpen = new TitlelessButton(fileOpenAction);
		toolBar.add(fileOpen);
		
		/*Action saveAction = new SaveAction(this);
		AbstractButton save = new TitlelessButton(saveAction);
		toolBar.add(save);
		
		Action exportAction = new ExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		AbstractButton fileexport = new TitlelessButton(exportAction);
		toolBar.add(fileexport);
		
		Action printAction = new PrintAction(dataView.getSample());
		AbstractButton print = new TitlelessButton(printAction);
		toolBar.add(print);
		
		// Edit Buttons
		Action measureAction = new MeasureToggleAction(this);
		AbstractButton measure = new TitlelessButton(measureAction);
		toolBar.add(measure);
		
		// Initialize data grid button
		Action initGridAction = new InitDataGridAction(this, dataView);
		AbstractButton initGrid = new TitlelessButton(initGridAction);
		toolBar.add(initGrid);

		// Remarks Button
		//Action remarkAction = new RemarkToggleAction(this);
		//AbstractButton toggleRemarks = new TitlelessButton(remarkAction);
		//toolBar.add(toggleRemarks);
				
		// Admin Buttons
		toolBar.addSeparator();
		MetadatabaseBrowserAction metadbAction = new MetadatabaseBrowserAction();
		AbstractButton launchMetadb = new TitlelessButton(metadbAction);
		toolBar.add(launchMetadb);
		
		
		
		// s Buttons
		toolBar.addSeparator();
		//Action truncateAction = new TruncateAction(null, dataView.getSample(), this, null);
		//AbstractButton truncate = new TitlelessButton(truncateAction);
		//toolBar.add(truncate);
		
		
		
		// Graph Buttons
		toolBar.addSeparator();
		Action graphSeriesAction = new GraphSeriesAction(dataView.getSample());
		AbstractButton graph = new TitlelessButton(graphSeriesAction);
		toolBar.add(graph);
		*/

		contentPane.add(toolBar, "cell 0 1,growx,aligny top");
				
		
	}
	/**
	 * Instruct the GUI to stop measuring and hide the measurement panel etc.
	 */
	public void stopMeasuring() {
		if (measurePanel != null) {
			
			// Make sure the size is sensible
			int currentWidth = (int) getSize().getWidth();
			int currentHeight = (int) getSize().getHeight();
			
			measurePanel.cleanup();
			remove(measurePanel);
			
			//editorEditMenu.setMeasuring(false);
			dataView.enableEditing(true);
			
			setSize(currentWidth-this.measuringPanelWidth, currentHeight);
			getContentPane().validate();
			getContentPane().repaint();
			measurePanel = null;
			
		}
	}
	
	
	/**
	 * Toggle between measuring and not-measuring mode
	 */
	public void toggleMeasuring() {
		// are we already measuring?
		if(measurePanel != null) {
			stopMeasuring();
			return;
		}
		
		// ok, start measuring, if we can!
		
		// Set up the measuring device
		AbstractMeasuringDevice device;
		try {
			device = MeasuringDeviceSelector.getSelectedDevice(true);
			device.setPortParamsFromPrefs();
		}
		catch (Exception ioe) {
			
			Alert.error(this, I18n.getText("error"), 
					I18n.getText("error.initExtComms")+".\n"+
					I18n.getText("error.possWrongComPort"));
			
			App.showPreferencesDialog();
			
			return;
		}
		
		try{
			//editorEditMenu.setMeasuring(true);
		} catch (Exception e)
		{ 
		}
		
		dataView.enableEditing(false);
	
		// add the measure panel...
		measurePanel = new EditorMeasurePanel(this, device);
		getContentPane().add(measurePanel, BorderLayout.WEST);
		
		// Make sure the size is sensible
		int currentWidth = (int) getSize().getWidth();
		int currentHeight = (int) getSize().getHeight();	
		setSize(currentWidth+this.measuringPanelWidth, currentHeight);	
		
		getContentPane().validate();
		getContentPane().repaint();
		measurePanel.setDefaultFocus();
		
		// Change the variable to EW/LW if in sub-annual mode
		if(getSample().containsSubAnnualData())
		{
			App.prefs.setPref(PrefKey.MEASUREMENT_VARIABLE, MeasurementVariable.EARLY_AND_LATEWOOD_WIDTH.toString());
			getSample().fireMeasurementVariableChanged();
		}
		
	}

	
	public SeriesDataMatrix getSeriesDataMatrix()
	{
		return dataView;
	}

	
	/**
	 * Get the currently selected sample
	 * 
	 * @return
	 */
	public Sample getSample() {
		try {
			if (model != null && model.size() > 0) {
				if (Data_matrix_list.getSelectedIndex() != -1) {
					return (Sample) model.get(Data_matrix_list.getSelectedIndex());
				}
				else
				{
					return (Sample) model.get(0);
				}

			}
		} catch (Exception e) {

		}

		return null;

	}
	

	
	
	public SeriesDataMatrix getDataMatrix()
	{
		return this.dataView;
	}
	
	public void itemSelected(){
		
		dataView = new SeriesDataMatrix(getSample(), this);
		
	
	}

}

