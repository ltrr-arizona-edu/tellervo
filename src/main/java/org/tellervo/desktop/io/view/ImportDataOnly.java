package org.tellervo.desktop.io.view;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.editor.LiteEditor;
import org.tellervo.desktop.io.AbstractDendroReaderFileFilter;
import org.tellervo.desktop.io.DendroReaderFileFilter;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.ui.Alert;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.AbstractDendroFormat;
import org.tridas.io.DendroFileFilter;
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

import javax.swing.JTable;

import java.awt.BorderLayout;

import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class ImportDataOnly extends JDialog {
	private final static Logger log = LoggerFactory.getLogger(EditorFactory.class);

	private final Window parent;
	private ArrayList<Sample> sampleList = new ArrayList<Sample>();
	private NormalTridasUnit unitsIfNotSpecified = NormalTridasUnit.MICROMETRES;
	private AbstractDendroFileReader reader;
	private File file;
	private AbstractDendroFormat fileFormat;
	private JTable table;
	
	
	public ImportDataOnly()
	{
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		
		
		table = new JXTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"/tmp/", "abc.rwl", "abc123a", "ABC", "123", "A", null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"Folder", "File name", "Series name", "Object code", "Element code", "Sample code", "Radius code", "Series code"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(74);
		table.getColumnModel().getColumn(1).setPreferredWidth(109);
		table.getColumnModel().getColumn(2).setPreferredWidth(117);

		scrollPane.setViewportView(table);
		parent = null;
		
	}
	
	public ImportDataOnly(Window parent, File file, AbstractDendroFormat fileType)
	{
		this.parent = parent;
		this.fileFormat = fileType;
		this.file = file;
		parseFile();
		
	}
	
	public ImportDataOnly(Window parent, File file, DendroFileFilter filetypefilter) throws Exception
	{
		this.parent = parent;
		this.file = file;
		
		this.fileFormat = TridasIO.getDendroFormatFromDendroFileFilter(filetypefilter);
				

		parseFile();

		
		
	}
	
	/**
	 * Get list of series for each sample in imported file 
	 * 
	 * @return
	 */
	public ArrayList<ITridasSeries> getSeries()
	{
		
		ArrayList<ITridasSeries> serList = new ArrayList<ITridasSeries>();
		
		for(Sample s : sampleList)
		{
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
		return sampleList;
	}
	
	private void parseFile()
	{
		// Create a reader based on the file type supplied
		
		reader = TridasIO.getFileReaderFromFormat(fileFormat);
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
			Alert.error(parent, "Error", "The selected file is not a valid "+fileFormat.getShortName()+ " file.\nPlease check and try again");
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
								Sample sample = EditorFactory.createSampleFromSeries(ms, e, file, fileFormat, hideWarningsFlag);	
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
				Sample sample = EditorFactory.createSampleFromSeries(ds, null, file, fileFormat, hideWarningsFlag);
				
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

		if(unitsSet==false && sampleList.size()>0)
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
	}
	
	public void openEditors()
	{
		if(sampleList==null || sampleList.size()==0) return;
		
		if(parent instanceof FullEditor)
		{
			((FullEditor)parent).addSamples(sampleList);
		}
		else
		{
			FullEditor editor = FullEditor.getInstance();
			editor.addSamples(sampleList);
		}
	}
	
/*	public void openEditorLites()
	{
		if(sampleList==null || sampleList.size()==0) return;
		
		new LiteEditor(file, fileType, sampleList);
	}*/

}
