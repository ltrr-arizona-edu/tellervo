package org.tellervo.desktop.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.datatransfer.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.miginfocom.swing.MigLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.SaveableDocument;
import org.tellervo.desktop.gui.menus.EditorActions;
import org.tellervo.desktop.gui.menus.EditorMenuBar;
import org.tellervo.desktop.gui.widgets.TitlelessButton;
import org.tellervo.desktop.prefs.PrefsListener;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.UnitUtils;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasTridas;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasValues;


public abstract class AbstractEditor extends JFrame implements PrefsListener, SaveableDocument, SampleListener, WindowListener, ActionListener{

	private static final long serialVersionUID = 1L;
	protected final static Logger log = LoggerFactory.getLogger(AbstractEditor.class);

	protected Window parent;
	protected JPanel contentPane;
	private JPanel workspacePanel;

	private JList<Sample> lstSamples;
	protected JPanel dataPanel;
	protected JTabbedPane tabbedPane;
	protected EditorActions actions;
	protected EditorMenuBar menuBar;	
	protected JButton btnAdd;
	protected JButton btnRemove;
	
	protected NormalTridasUnit unitsIfNotSpecified = NormalTridasUnit.MICROMETRES;
	protected AbstractDendroFileReader reader;
	protected File file;
	protected String fileType;
	protected SeriesDataMatrix dataView;
	protected SampleListModel samplesModel;
	


	protected AbstractEditor() {
		init();
	}

	/**
	 * Initalise the GUI
	 */
	protected void init() {
		
		// Initialize the app (needed for WindowBuilder)
		if (!App.isInitialized()) App.init();

		setTitle("Tellervo");
		this.setIconImage(Builder.getApplicationIcon());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setExtendedState( JFrame.MAXIMIZED_BOTH );
		this.setMinimumSize(new Dimension(640, 480));
		this.addWindowListener(this);
		
		// Setup the main content pane
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[424px,grow,fill]",
				"[20px:20.00px][32px:32.00px:32px][214px,grow,fill]"));

		// Setup the main split between workspace and content
		JSplitPane splitPaneWorkspaceAndContent = new JSplitPane();
		splitPaneWorkspaceAndContent.setOneTouchExpandable(true);
		splitPaneWorkspaceAndContent.setResizeWeight(0.0);
		contentPane.add(splitPaneWorkspaceAndContent, "cell 0 2,grow");

		samplesModel = new SampleListModel();

		// Setup the workspace panel and add
		initWorkspacePanel();
		splitPaneWorkspaceAndContent.setLeftComponent(workspacePanel);
		
		// Setup the main content panel and add
		JPanel panelMain = new JPanel();
		splitPaneWorkspaceAndContent.setRightComponent(panelMain);
		panelMain.setLayout(new MigLayout("", "[grow,fill]", "[grow,fill]"));
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		panelMain.add(tabbedPane, "cell 0 0,grow");
		dataPanel = new JPanel();
		tabbedPane.addTab("Data", Builder.getIcon("data.png", 16), dataPanel, null);
		dataPanel.setLayout(new BorderLayout(0, 0));
		

		// Init the actions class for menus and toolbars
		initActions();
		initMenu();
		initToolbar();
		
		btnRemove.setAction(actions.removeSeriesAction);
		btnAdd.setAction(actions.addSeriesAction);	


	}

	private void initWorkspacePanel() {
		// Setup the workspace panel
		workspacePanel = new JPanel();
		
		workspacePanel.setLayout(new MigLayout("", "[125.00,grow,fill][32.00][20.00,leading]", "[][235px,grow,baseline][fill]"));
		workspacePanel.setMinimumSize(new Dimension(240,10));

		
		btnRemove = new TitlelessButton(null);
		btnAdd = new TitlelessButton(null);	
		JLabel lblSeries = new JLabel("Series:");
		workspacePanel.add(lblSeries, "cell 0 0");
		workspacePanel.add(btnAdd, "cell 1 0");
		workspacePanel.add(btnRemove, "cell 2 0");
		JScrollPane scrollPane = new JScrollPane();
		workspacePanel.add(scrollPane, "cell 0 1 3 1,grow");
		lstSamples = new JList<Sample>(samplesModel);
		lstSamples.setValueIsAdjusting(true);
		lstSamples.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lstSamples.setLayoutOrientation(JList.VERTICAL);
		lstSamples.setVisibleRowCount(10);
		scrollPane.setViewportView(lstSamples);



	   	JPanel panel = new JPanel();
		workspacePanel.add(panel, "cell 0 2 3 1,grow");
		panel.setLayout(new MigLayout("", "[][grow]", "[]"));
		
		JLabel lblSortBy = new JLabel("Sort:");
		panel.add(lblSortBy, "cell 0 0,alignx trailing");
		
		final JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"None", "Title (asc.)", "Title (desc.)", "Start year (asc.)", "Start year (desc.)", "End year (asc.)", "End year (desc.)", "Length (asc.)", "Length (desc.)"}));
		panel.add(comboBox, "cell 1 0,growx");

		lstSamples.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				itemSelected();
			}

		});
		

	    //Add listener to components that can bring up popup menus.
 

		comboBox.addActionListener(new ActionListener(){
			
			@Override
			public void actionPerformed(ActionEvent arg0){
				 if (comboBox.getSelectedIndex() == 1)
				{
					samplesModel.sortAscending(new NameComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 2)
				{
					samplesModel.sortDescending(new NameComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 3)
				{
					samplesModel.sortAscending(new StartYearComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 4)
				{
					samplesModel.sortDescending(new StartYearComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 5)
				{
					samplesModel.sortAscending(new EndYearComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 6)
				{
					samplesModel.sortDescending(new EndYearComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 7)
				{
					samplesModel.sortAscending(new LengthComparator());
					lstSamples.repaint();
				}
				else if (comboBox.getSelectedIndex() == 8)
				{
					samplesModel.sortDescending(new LengthComparator());
					lstSamples.repaint();
				}
			}
		});
		
	}

	protected abstract void initPopupMenu();
	
	protected abstract void initMenu() ;

	protected abstract void initToolbar();
	
	
	
	/**
	 * Instruct the GUI to stop measuring and hide the measurement panel etc.
	 */
	public void stopMeasuring() {
		if(dataView!=null) dataView.stopMeasuring();
	}

	/**
	 * Toggle between measuring and not-measuring mode
	 */
	public void toggleMeasuring() {
		if(dataView!=null) dataView.toggleMeasuring();

	}

	/**
	 * Get the SeriesDataMatrix tab for this editor
	 * 
	 * @return
	 */
	public SeriesDataMatrix getSeriesDataMatrix() {
		return dataView;
	}

	/**
	 * Get the currently selected sample
	 * 
	 * @return
	 */
	public Sample getSample() {
		try {
			if (samplesModel != null && samplesModel.getSize() > 0) {
				if (lstSamples.getSelectedIndex() != -1) {
					return (Sample) samplesModel.getElementAt(lstSamples.getSelectedIndex());
				} else {
					return (Sample) samplesModel.getElementAt(0);
				}

			}
		} catch (Exception e) {

		}

		return null;

	}
	

	
	/**
	 * Add the specified sample to the workspace and select it
	 * 
	 * @param s
	 */
	public void addSample(Sample s)
	{
		samplesModel.addElement(s);
		
		lstSamples.setSelectedValue(s, true);
	}
	
	/**
	 * Add all the specified samples to the workspace
	 * 
	 * @param samples
	 */
	public void addSamples(Collection<Sample> samples)
	{
		for(Sample sample : samples)
		{
			samplesModel.addElement(sample);
		}
		
		lstSamples.setSelectedIndex(samplesModel.getSize()-1);
	}

	/**
	 * Set the title of the window to the name of the current sample and Tellervo
	 * 
	 */
	protected void setTitle() {

		if (getSample() != null) {
			this.setTitle(getSample().getDisplayTitle() + " - Tellervo");
		} else {
			this.setTitle("Tellervo");
		}
	}

	/**
	 * Parse a legacy data file to extract data for display in the editor
	 */
	protected void parseFile()
	{
		// Create a reader based on the file type supplied
		
		reader = TridasIO.getFileReader(fileType);
		if(reader==null) 
		{
			Alert.error(parent, "Error", "Unknown file type");
			return;
		}
		
		// Try and load the file
		try {
			reader.loadFile(file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(file.getAbsolutePath(), e);
			return;
		} catch (InvalidDendroFileException e) {
			Alert.error(parent, "Error", "The selected file is not a valid "+fileType+ " file.\nPlease check and try again");
			return;
		}
		catch(NullPointerException e)
		{
			Alert.error(parent, "Invalid File", e.getLocalizedMessage());
		}

		TridasTridas tc = reader.getTridasContainer();
		
		Boolean hideWarningsFlag = false;
		for(TridasProject p : tc.getProjects())
		{
			for(TridasObject o : p.getObjects())
			{
				for(TridasElement e : o.getElements())
				{
					
					for(TridasSample s : e.getSamples())
					{
						for(TridasRadius r : s.getRadiuses())
						{
							for(TridasMeasurementSeries ms : r.getMeasurementSeries())
							{
								Sample sample = EditorFactory.createSampleFromSeries(ms, e, file, fileType, hideWarningsFlag);	
								if(sample==null)
								{
									hideWarningsFlag=true;
								}
								else
								{
									samplesModel.addElement(sample);
								}
								
							}
						}
					}
				}
			}
			
			for(TridasDerivedSeries ds : p.getDerivedSeries())
			{
				Sample sample = EditorFactory.createSampleFromSeries(ds, null, file, fileType, hideWarningsFlag);
				
				if(sample==null)
				{
					hideWarningsFlag=true;
				}
				else
				{
					samplesModel.addElement(sample);
				}
				
			}
			
		}
		
		
		Boolean unitsSet = false;
		for(ITridasSeries ser : getSeries())
		{
			for(TridasValues  tv : ser.getValues())
			{	
				if(tv.isSetUnit())
				{
					if(tv.getUnit().isSetNormalTridas())
					{
						unitsSet = true;
					}
				}	
			}
		}

		if(unitsSet==false && samplesModel.getSize()>0)
		{
			Object[] possibilities = {"1/1000th mm", 
					"1/100th mm",
					"1/50th mm",
					"1/20th mm",
					"1/10th mm"};
			Object s = JOptionPane.showInputDialog(
			                    parent,
			                    "One or more series has no units defined.\n"
			                    + "Please specify units below:",
			                    "Set Units",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    "1/1000th mm");

			if (s.equals("1/1000th mm")) 
			{
			   unitsIfNotSpecified = NormalTridasUnit.MICROMETRES;
			}
			else if (s.equals("1/100th mm")) 
			{
				   unitsIfNotSpecified = NormalTridasUnit.HUNDREDTH_MM;
			}
			else if (s.equals("1/50th mm")) 
			{
				   unitsIfNotSpecified = NormalTridasUnit.FIFTIETH_MM;
			}	
			else if (s.equals("1/20th mm")) 
			{
				   unitsIfNotSpecified = NormalTridasUnit.TWENTIETH_MM;
			}	
			else if (s.equals("1/10th mm")) 
			{
				   unitsIfNotSpecified = NormalTridasUnit.TENTH_MM;
			}		
			else
			{
				Alert.error(parent, "Error", "Invalid measurement units specified");
				return;
			}
		}
		
		for(int n=0; n< samplesModel.getSize(); n++)
		{
			Sample sample = samplesModel.getElementAt(n);
			ITridasSeries series = sample.getSeries();
			
			try {				
				for(int i=0; i < series.getValues().size(); i++)
				{
					TridasValues tv = series.getValues().get(i);
					
					if(tv.isSetUnit())
					{
						if(!tv.getUnit().isSetNormalTridas())
						{
							tv.getUnit().setNormalTridas(unitsIfNotSpecified);
						}
					}	
					else
					{
						TridasUnit unit = new TridasUnit();
						unit.setNormalTridas(unitsIfNotSpecified);
						tv.setUnit(unit);
						tv.setUnitless(null);
					}
					
					tv = UnitUtils.convertTridasValues(NormalTridasUnit.MICROMETRES, tv, true);
					
					TridasUnit unit = new TridasUnit();
					unit.setNormalTridas(NormalTridasUnit.MICROMETRES);
					tv.setUnit(unit);
					series.getValues().set(i,tv);
				}
				
			} catch (NumberFormatException e) {
				Alert.error(parent, "Error", "One or more data values are not numbers.");
				return;
			} catch (ConversionWarningException e) {
				Alert.error(parent, "Error", "Error converting units");
				return;
			}
		}
		
		getSamplesModel().setDirty(false);
	}
	
	
	/**
	 * Get list of series for each sample in imported file 
	 * 
	 * @return
	 */
	public ArrayList<ITridasSeries> getSeries()
	{
		
		ArrayList<ITridasSeries> serList = new ArrayList<ITridasSeries>();
		
		for(int n=0; n< samplesModel.getSize(); n++)
		{
			Sample s = samplesModel.getElementAt(n);
			serList.add(s.getSeries());
		}
		
		return serList;
	}
	
	
	/**
	 * Get list of samples from the imported file 
	 * 
	 * @return
	 */
	public ArrayList<Sample> getSamples()
	{
		ArrayList<Sample> samplesList = new ArrayList<Sample>();
		
		for(int n=0; n< samplesModel.getSize(); n++)
		{
			Sample s = samplesModel.getElementAt(n);
			samplesList.add(s);
		}
		
		return samplesList;	
	}
	
	/**
	 * Get the ListModel for the samples edited by this editor 
	 * 
	 * @return
	 */
	public SampleListModel getSamplesModel() {
		return samplesModel;
	}
	
	/**
	 * Get the samples JList
	 * 
	 * @return
	 */
	public JList<Sample> getLstSamples() {
		return lstSamples;
	}
	
	/**
	 * A sample was selected from the list
	 */
	public abstract void itemSelected();
	
	/**
	 * Dispose of this editor after doing any necessary housekeeping
	 */
	
	public abstract void cleanupAndDispose();
	
	/**
	 * Initalise the actions used by the menus and toolbar
	 */
	protected abstract void initActions();
		
	@Override
	public void sampleRedated(SampleEvent e) {	
		setTitle();
		lstSamples.repaint();
	}

	@Override
	public void sampleDataChanged(SampleEvent e) {		
		setTitle();
		lstSamples.repaint();
	}

	@Override
	public void sampleMetadataChanged(SampleEvent e) {
		setTitle();
		lstSamples.repaint();
	}

	@Override
	public void sampleElementsChanged(SampleEvent e) {
		setTitle();
		lstSamples.repaint();
	}

	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {	
		setTitle();
		lstSamples.repaint();
	}

	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		setTitle();
		lstSamples.repaint();
	}

	@Override
	public void measurementVariableChanged(SampleEvent e) {
		setTitle();
		lstSamples.repaint();
	}

}
