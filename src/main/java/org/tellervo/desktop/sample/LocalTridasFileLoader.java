package org.tellervo.desktop.sample;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.Range;
import org.tellervo.desktop.Year;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.editor.EditorLite;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.io.view.ImportDataOnly;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.DendroFileFilter;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarningException;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.ITRDBTaxonConverter;
import org.tridas.io.util.SafeIntYear;
import org.tridas.io.util.TridasUtils;
import org.tridas.io.util.UnitUtils;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasProject;
import org.tridas.schema.TridasRadius;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasTridas;
import org.tridas.schema.TridasUnit;
import org.tridas.schema.TridasValues;

public class LocalTridasFileLoader implements GUIAwareSampleLoader {
	private final static Logger log = LoggerFactory.getLogger(LocalTridasFileLoader.class);

	Component parent;
	ITridasSeries series;
	
	public LocalTridasFileLoader(ITridasSeries series)
	{
		this.series = series;		
		
	}
	

	
	public static ArrayList<LocalTridasFileLoader> TricycleParser(String filename, String format)
	{
		ArrayList<LocalTridasFileLoader> loaderList = new ArrayList<LocalTridasFileLoader>();
		
		AbstractDendroFileReader reader = TridasIO.getFileReader(format);
		if(reader==null) 
		{
			log.debug("Unknown file type");
			return null;
		}
		
		// Try and load the file
		try {
			reader.loadFile(filename);
		} catch (IOException e) {
			log.error("error loading file "+filename);
			return null;
		} catch (InvalidDendroFileException e) {
			log.error("The file "+filename + " is not valid");
			return null;
		}
		catch(NullPointerException e)
		{
			log.error("Invalid File", e.getLocalizedMessage());
			return null;
		}
					
		ArrayList<TridasMeasurementSeries> seriesList = TridasUtils.getMeasurementSeriesFromTridasContainer(reader.getTridasContainer());
		ArrayList<TridasDerivedSeries> dseriesList = TridasUtils.getDerivedSeriesFromTridasContainer(reader.getTridasContainer());
				
		for(TridasMeasurementSeries s : seriesList)
		{
			loaderList.add(new LocalTridasFileLoader(s));
		}
		
		for(TridasDerivedSeries s : dseriesList)
		{
			loaderList.add(new LocalTridasFileLoader(s));
		}
		
		return loaderList;

	}
	
	public static ArrayList<LocalTridasFileLoader> TricycleParser(File file, String format)
	{
		return TricycleParser(file.getAbsoluteFile(), format);
	}
	
	@Override
	public Sample load() throws IOException {
		return new Sample(series);	
	}

	@Override
	public BaseSample loadBasic() throws IOException {
		return load();
	}

	@Override
	public boolean save(Sample s) throws IOException, UserCancelledException {
		// TODO Auto-generated method stub
		return false;
	}

	public void preload(BaseSample bs) {
		
	}
	
	@Override
	public String getName() {
		return series.getTitle();
	}

	@Override
	public String getShortName() {
		return series.getTitle();
	}

	@Override
	public SampleType getSampleType() {
		try {
			BaseSample s = loadBasic();
			return s.getSampleType();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}

	@Override
	public Sample load(Dialog dialog) throws IOException,
			UserCancelledException {
		return load();
	}

	@Override
	public BaseSample loadBasic(Dialog dialog) throws IOException,
			UserCancelledException {
		this.parent = dialog;

		return load();
	}

	@Override
	public boolean save(Sample s, Dialog dialog) throws IOException,
			UserCancelledException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Sample load(Frame frame) throws IOException {
		this.parent = frame;
		return load();
	}

	@Override
	public BaseSample loadBasic(Frame frame) throws IOException {
		this.parent = frame;
		return load();
	}

	@Override
	public boolean save(Sample s, Frame frame) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	

	
}
