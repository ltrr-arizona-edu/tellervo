package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.actions.ExportDataAction;
import org.tellervo.desktop.gui.menus.actions.FileOpenAction;
import org.tellervo.desktop.gui.menus.actions.GraphSeriesAction;
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
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.ui.I18n;

public abstract class AbstractEditor extends JFrame implements SaveableDocument, PrefsListener,
SampleListener {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AbstractEditor.class);
	
	private JPanel contentPane;
	
	private EditorMeasurePanel measurePanel = null;
	private SeriesDataMatrix dataView; 
	private int measuringPanelWidth = 340;
	protected DefaultListModel<Sample> samplesModel;
	private JList<Sample> lstSamples;
	private JPanel dataPanel;
	

	public AbstractEditor(Sample sample) {
				
		init();
		initbar();
		samplesModel.addElement(sample);
		itemSelected();

	}
	
	public AbstractEditor(ArrayList<Sample> samples)
	{
			
		init();
		initbar();
				
		for(Sample sample: samples)
		{
			samplesModel.addElement(sample);
		}
		itemSelected();
	}
	
	public AbstractEditor() {
				
		init();
		initbar();
	}

	
	/**
	 * Initalise the GUI
	 */
	protected void init()
	{
		if(!App.isInitialized()) App.init();
		
		setTitle("Tellervo Editor");
		
		this.setIconImage(Builder.getApplicationIcon());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 851, 540);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[424px,grow,fill]", "[20px:20.00px][32px:32.00px:32px][214px,grow,fill]"));

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOneTouchExpandable(true);
		splitPane.setResizeWeight(0.0);
		contentPane.add(splitPane, "cell 0 2,grow");
		
		JPanel workspacePanel = new JPanel();
		splitPane.setLeftComponent(workspacePanel);
				
		
		samplesModel= new DefaultListModel<Sample>();
		
		//model.addElement(sampleList);
		
		workspacePanel.setLayout(new MigLayout("", "[133.00,grow,fill][142.00,grow,leading]", "[235px,grow,baseline][fill]"));
		
		JScrollPane scrollPane = new JScrollPane();
		workspacePanel.add(scrollPane, "cell 0 0 2 1,grow");
		
		lstSamples = new JList<Sample>(samplesModel);
		scrollPane.setViewportView(lstSamples);
		lstSamples.setValueIsAdjusting(true);
		
		
		lstSamples.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstSamples.setLayoutOrientation(JList.VERTICAL);
		lstSamples.setVisibleRowCount(10);
		
		JButton btnAdd = new JButton();
		btnAdd.setIcon(Builder.getIcon("edit_add.png", 16));
		workspacePanel.add(btnAdd, "cell 0 1");
		
		JButton btnRemove = new JButton();
		btnRemove.setIcon(Builder.getIcon("cancel.png", 16));
		workspacePanel.add(btnRemove, "cell 1 1,growx");
		
		lstSamples.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent e){
				itemSelected();
			}		
			
		});
				
		
		JPanel panelMain = new JPanel();
		splitPane.setRightComponent(panelMain);
		panelMain.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		panelMain.add(tabbedPane, "cell 0 0,grow");
		
		dataPanel = new JPanel();
		tabbedPane.addTab("Data", Builder.getIcon("data.png", 16), dataPanel, null);
		dataPanel.setLayout(new BorderLayout(0, 0));
		
		
		JPanel metadataPanel = new JPanel();
		tabbedPane.addTab("Metadata", Builder.getIcon("database.png", 16), metadataPanel, null);
		
		initMenu();
		
		
	}


	
	protected void initMenu()
	{
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
		
		Action saveAction = new SaveAction(this);
		AbstractButton save = new TitlelessButton(saveAction);
		toolBar.add(save);
		
		Action exportAction = new ExportDataAction(IOController.OPEN_EXPORT_WINDOW);
		AbstractButton fileexport = new TitlelessButton(exportAction);
		toolBar.add(fileexport);
		
		Action printAction = new PrintAction(this);
		AbstractButton print = new TitlelessButton(printAction);
		toolBar.add(print);
		
		// Edit Buttons
		Action measureAction = new MeasureToggleAction(this);
		AbstractButton measure = new TitlelessButton(measureAction);
		toolBar.add(measure);
		
		// Initialize data grid button
		//Action initGridAction = new InitDataGridAction(this, dataView);
		//AbstractButton initGrid = new TitlelessButton(initGridAction);
		//toolBar.add(initGrid);

		// Remarks Button
		Action remarkAction = new RemarkToggleAction(this);
		AbstractButton toggleRemarks = new TitlelessButton(remarkAction);
		toolBar.add(toggleRemarks);
				
		// Admin Buttons
		toolBar.addSeparator();
		MetadatabaseBrowserAction metadbAction = new MetadatabaseBrowserAction();
		AbstractButton launchMetadb = new TitlelessButton(metadbAction);
		toolBar.add(launchMetadb);
		
		
		
		// s Buttons
		toolBar.addSeparator();
		Action truncateAction = new TruncateAction(null, this, null);
		AbstractButton truncate = new TitlelessButton(truncateAction);
		toolBar.add(truncate);
		
		
		
		// Graph Buttons
		toolBar.addSeparator();
		Action graphSeriesAction = new GraphSeriesAction(this);
		AbstractButton graph = new TitlelessButton(graphSeriesAction);
		toolBar.add(graph);
		

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

	/**
	 * Get the SeriesDataMatrix tab for this editor
	 * 
	 * @return
	 */
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
			if (samplesModel != null && samplesModel.size() > 0) {
				if (lstSamples.getSelectedIndex() != -1) {
					return (Sample) samplesModel.get(lstSamples.getSelectedIndex());
				}
				else
				{
					return (Sample) samplesModel.get(0);
				}

			}
		} catch (Exception e) {

		}

		return null;

	}
	
	
	/**
	 * A sample was selected from the list
	 */
	public void itemSelected() {

		log.debug("Item selected");
		Sample sample = getSample();
		if (sample != null) {
			
			if(dataView!=null) 
			{
				dataView.saveRemarksDividerLocation();			
			}
			
			dataView = new SeriesDataMatrix(sample, this);
			dataPanel.removeAll();
			dataPanel.add(dataView, BorderLayout.CENTER);
			dataPanel.repaint();
			this.repaint();
			dataView.restoreRemarksDividerLocation();

		}
	}





}
