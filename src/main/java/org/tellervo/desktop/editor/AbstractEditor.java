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

import org.fhaes.util.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.VariableChooser.MeasurementVariable;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.hardware.AbstractMeasuringDevice;
import org.tellervo.desktop.hardware.MeasuringDeviceSelector;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.I18n;

public abstract class AbstractEditor extends JFrame implements SaveableDocument, PrefsListener,
SampleListener {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(AbstractEditor.class);
	protected ArrayList<Sample> sampleList;
	private JPanel contentPane;
	
	private EditorMeasurePanel measurePanel = null;
	protected SeriesDataMatrix dataView; 
	private int measuringPanelWidth = 340;
	
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
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		
		JPanel Workspace_panel = new JPanel();
		splitPane.setLeftComponent(Workspace_panel);
				
		final DefaultListModel model;
		model= new DefaultListModel();
		
		model.addElement(sampleList);
		
		Workspace_panel.setLayout(new MigLayout("", "[][]", "[235px,grow,fill][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		Workspace_panel.add(scrollPane, "cell 0 0 2 1,grow");
		
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
		
		JPanel dataPanel = new JPanel();
		tabbedPane.addTab("Data", null, dataPanel, null);
		dataPanel.setLayout(new BorderLayout(0, 0));
		dataView = new SeriesDataMatrix(getSample(), this);
		dataPanel.add(dataView);
		
		JPanel Metadata_panel = new JPanel();
		tabbedPane.addTab("Metadata", null, Metadata_panel, null);
		
		
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
	 * Get the currently selected sample
	 * 
	 * @return
	 */
	public Sample getSample()
	{
				
		return sampleList.get(0);
	}
	
	/**
	 * Whole ring width value measured by serial device
	 * 
	 * @param x
	 * @return
	 */
	public Year setCurrentRingValue(int x) {
		return dataView.measured(x);
	}
	
	/**
	 * Early/Late wood value measured by serial device
	 *  
	 * @param ew
	 * @param lw
	 * @return
	 */
	public Year setCurrentRingValue(int ew, int lw) {
		return dataView.measured(ew, lw);
	}
	
	
	public void itemSelected(){
		
		log.debug("Sample clicked in list");
		
	
	}

}
