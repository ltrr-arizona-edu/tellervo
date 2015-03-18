package org.tellervo.desktop.gui.seriesidentity;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXTable;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.editor.view.FullEditor;
import org.tellervo.desktop.sample.Sample;
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

import javax.swing.JLabel;

import java.awt.Font;

/**
 * GUI panel designed to enable the user to specifiy the identity of series being imported from legacy text files 
 * 
 * @author pbrewer
 *
 */
public class IdentifySeriesPanel extends JPanel implements ActionListener, TableModelListener {

	private static final long serialVersionUID = 1L;
	private JXTable table;
	private Window containerFrame = null; 
	private NormalTridasUnit unitsIfNotSpecified = null;
	private SeriesIdentityTableModel model;
	private JButton btnFinish;

	
	public IdentifySeriesPanel()
	{
		init();
	}
	
	public IdentifySeriesPanel(Window parent, File file, String filetype) {

		setContainerFrame(parent);
		init();
		parseFile(file, filetype);
	}
	
	public IdentifySeriesPanel(Window parent, File[] files, String filetype) {

		setContainerFrame(parent);
		init();
		parseFiles(files, filetype);
	}
	
	public void setContainerFrame(Window parent)
	{
		this.containerFrame = parent;
	}
	
	/**
	 * Initialize the GUI
	 */
	private void init()
	{
		setLayout(new BorderLayout(0, 0));
		
		JPanel panelMain = new JPanel();
		add(panelMain, BorderLayout.CENTER);
		panelMain.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		panelMain.add(scrollPane, "cell 0 0,grow");
		
		table = new JXTable(){
			private static final long serialVersionUID = 1L;
			public void changeSelection(final int row, final int column, boolean toggle, boolean extend)
            {
                super.changeSelection(row, column, toggle, extend);
                if(table.isCellEditable(row, column))
                {
                	editCellAt(row, column);
                }
            }
		};
		
		table.setHorizontalScrollEnabled(true);
		table.setColumnControlVisible(true);
		model = new SeriesIdentityTableModel();
		
		SeriesIdentityTableCellRenderer renderer = new SeriesIdentityTableCellRenderer();
				
		table.setModel(model);
		
		table.getColumn(3).setCellRenderer(renderer);
		table.getColumn(4).setCellRenderer(renderer);
		table.getColumn(5).setCellRenderer(renderer);
		table.getColumn(6).setCellRenderer(renderer);
		table.getColumn(7).setCellRenderer(renderer);
		scrollPane.setViewportView(table);
		
		JPanel panelButton = new JPanel();
		add(panelButton, BorderLayout.SOUTH);
		panelButton.setLayout(new MigLayout("", "[grow][][]", "[grow][][]"));
		
		model.addTableModelListener(this);
		
		JPanel panel = new JPanel();
		panelButton.add(panel, "cell 0 0 3 1,grow");
		panel.setLayout(new MigLayout("", "[31px][162px]", "[16px][][]"));
		
		JLabel lblKey = new JLabel("Key:");
		panel.add(lblKey, "cell 0 0,alignx left,aligny top");
		
		JLabel lblPresentIn = new JLabel("= Present in database");
		lblPresentIn.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPresentIn.setIcon(Builder.getIcon("found.png", 16));
		panel.add(lblPresentIn, "flowy,cell 1 0,alignx left,aligny top");
		
		JLabel lblAbsentFrom = new JLabel("= Absent from database");
		lblAbsentFrom.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAbsentFrom.setIcon(Builder.getIcon("missing.png", 16));
		panel.add(lblAbsentFrom, "cell 1 1,alignx left,aligny top");
		
		JLabel lblNotYet = new JLabel("= Not yet searched");
		lblNotYet.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNotYet.setIcon(Builder.getIcon("wait.png", 16));
		panel.add(lblNotYet, "cell 1 2,alignx left,aligny top");
		
		JButton btnSearchDB = new JButton("Search Database");
		btnSearchDB.setActionCommand("SearchDB");
		btnSearchDB.addActionListener(this);
		
		JButton btnDefineByPattern = new JButton("Define by pattern");
		btnDefineByPattern.setActionCommand("DefineByPattern");
		btnDefineByPattern.addActionListener(this);
		
		panelButton.add(btnDefineByPattern, "cell 2 1");
		panelButton.add(btnSearchDB, "cell 0 2");
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand("Cancel");
		btnCancel.addActionListener(this);
		panelButton.add(btnCancel, "cell 1 2");
		
		btnFinish = new JButton("Finish");
		btnFinish.setActionCommand("Finish");
		btnFinish.addActionListener(this);
		btnFinish.setEnabled(false);
		panelButton.add(btnFinish, "cell 2 2");
		
		JToolBar toolBar = new JToolBar();
		add(toolBar, BorderLayout.NORTH);
	}
	
	
	/**
	 * Parse the specified legacy data file for series 
	 * 
	 * @param file
	 * @param fileType
	 */
	private void parseFile(File file, String fileType)
	{
		
		ArrayList<Sample> sampleList = new ArrayList<Sample>();

		
		// Create a reader based on the file type supplied		
		AbstractDendroFileReader reader = TridasIO.getFileReader(fileType);
		if(reader==null) 
		{
			Alert.error(containerFrame, "Error", "Unknown file type");
			return;
		}
		
		// Try and load the file
		try {
			reader.loadFile(file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(file.getAbsolutePath(), e);
			return;
		} catch (InvalidDendroFileException e) {
			Alert.error(containerFrame, "Error", "The selected file is not a valid "+fileType+ " file.\nPlease check and try again");
			return;
		}
		catch(NullPointerException e)
		{
			Alert.error(containerFrame, "Invalid File", e.getLocalizedMessage());
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
									sampleList.add(sample);
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
					sampleList.add(sample);
				}
				
			}
			
		}
		
		
		Boolean unitsSet = false;
		for(ITridasSeries ser : getSeries(sampleList))
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

		if(unitsSet==false && sampleList.size()>0 && unitsIfNotSpecified==null)
		{
			Object[] possibilities = {"1/1000th mm", 
					"1/100th mm",
					"1/50th mm",
					"1/20th mm",
					"1/10th mm"};
			Object s = JOptionPane.showInputDialog(
			                    containerFrame,
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
				Alert.error(containerFrame, "Error", "Invalid measurement units specified");
				return;
			}
		}
		
		for(Sample sample : sampleList)
		{
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
				Alert.error("Error", "One or more data values are not numbers.");
				return;
			} catch (ConversionWarningException e) {
				Alert.error("Error", "Error converting units");
				return;
			}
		}
		
		for(Sample s : sampleList)
		{
			SeriesIdentity id = new SeriesIdentity(file, fileType, s);
		
			model.addItem(id);
			
		}
		
		
	}
	
	/**
	 * Helper function for parsing multiple files
	 * 
	 * @param files
	 * @param filetype
	 */
	private void parseFiles(File[] files,  String filetype)
	{
		
		for(File file : files)
		{
			parseFile(file, filetype);
		}
		
	}
	
	
	/**
	 * Get list of series for each sample in array 
	 * 
	 * @return
	 */
	public static ArrayList<ITridasSeries> getSeries(ArrayList<Sample> sampleList)
	{
		
		ArrayList<ITridasSeries> serList = new ArrayList<ITridasSeries>();
		
		for(Sample s : sampleList)
		{
			serList.add(s.getSeries());
		}
		
		return serList;
	}
	
	
	/**
	 * Test harness
	 * 
	 * @param args
	 */
	public static void show(File[] files, String format)
	{
		App.init();
		
		JFrame frame = new JFrame();
	
		frame.getContentPane().add(new IdentifySeriesPanel(frame, files, format));
		frame.setIconImage(Builder.getApplicationIcon());
		frame.setTitle("Import Data");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(new Dimension(800, 500));
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
	}

	/**
	 * Search the database for entity matches based on the codes specified in the table
	 */
	public void searchDatabaseForMatches()
	{
		model.searchForMatches();
	}
	

	/**
	 * Open the editor with these series imported
	 */
	private void openEditor()
	{
		FullEditor editor = new FullEditor();
		
		editor.addSamples(model.getAllSamples());
		
	}
	
	/**
	 * Populate the table using information specified in the regex pattern dialog
	 */
	private void fillTableByPattern()
	{
		SeriesIdentityRegexDialog dialog = new SeriesIdentityRegexDialog(containerFrame, model.getSeriesIdentity(0));
		dialog.setLocationRelativeTo(containerFrame);
		dialog.setVisible(true);
		
		
		
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("SearchDB"))
		{
			searchDatabaseForMatches();
		}
		else if (evt.getActionCommand().equals("Finish"))
		{
			searchDatabaseForMatches();
			openEditor();
			containerFrame.setVisible(false);
		}
		else if (evt.getActionCommand().equals("Cancel"))
		{
			containerFrame.dispose();
		}
		else if (evt.getActionCommand().equals("DefineByPattern"))
		{
			fillTableByPattern();
		}
	}


	@Override
	public void tableChanged(TableModelEvent evt) {

		this.btnFinish.setEnabled(model.isTableComplete());
	}
	

}
