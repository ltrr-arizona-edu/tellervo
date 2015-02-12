package org.tellervo.desktop.sample;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.gui.UserCancelledException;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasMeasurementSeries;

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
