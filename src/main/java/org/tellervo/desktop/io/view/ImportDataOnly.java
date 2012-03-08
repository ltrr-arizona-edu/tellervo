package org.tellervo.desktop.io.view;

import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.tellervo.desktop.editor.EditorFactory;
import org.tellervo.desktop.ui.Alert;
import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasUnit;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasValues;

public class ImportDataOnly extends Object {

	private static final long serialVersionUID = 1L;
	private final Window parent;
	
	public ImportDataOnly(Window parent, File file, String fileType)
	{
		this.parent = parent;
		parseFile(file, fileType);
	}
	
	
	private void parseFile(File file, String fileType)
	{
		
		
		// Create a reader based on the file type supplied
		AbstractDendroFileReader reader;
		reader = TridasIO.getFileReader(fileType);
		if(reader==null) 
		{
			Alert.error("Error", "Unknown file type");
			return;
		}
		
		// Try and load the file
		try {
			reader.loadFile(file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(file.getAbsolutePath(), e);
			return;
		} catch (InvalidDendroFileException e) {
			Alert.error("Error", "Invalid dendro file");
			return;
		}
		catch(NullPointerException e)
		{
			Alert.error("Invalid File", e.getLocalizedMessage());
		}
					
		ArrayList<TridasMeasurementSeries> seriesList = TridasUtils.getMeasurementSeriesFromTridasProject(reader.getProjects()[0]);
		
		Integer count = reader.getProjects()[0].getDerivedSeries().size() + seriesList.size();
		
		if(count>3)
		{
			int n = JOptionPane.showConfirmDialog(
				    parent,
				    "This file contains "+count+" series.\nAre you sure you want to open all of these?",
				    "Multiple series",
				    JOptionPane.YES_NO_OPTION);
			if(n != JOptionPane.YES_OPTION)
			{
				return;
			}
			
		}
		
		Boolean unitsSet = false;
		for(TridasMeasurementSeries ser : seriesList)
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
		
		for(TridasDerivedSeries ser : reader.getProjects()[0].getDerivedSeries())
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
		
		
		NormalTridasUnit unitsIfNotSpecified = NormalTridasUnit.MICROMETRES;
				
		if(unitsSet==false)
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
			                    "Customized Dialog",
			                    JOptionPane.PLAIN_MESSAGE,
			                    null,
			                    possibilities,
			                    "1/100th mm");

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
				Alert.error("Error", "Invalid measurement units specified");
				return;
			}
		}
		
		
		// Warn if project contains derivedSeries
		if(reader.getProjects()[0].isSetDerivedSeries())
		{
			for(TridasDerivedSeries series : reader.getProjects()[0].getDerivedSeries())	
			{
				EditorFactory.newSeriesFromDerivedSeries(parent, series, unitsIfNotSpecified);
			}
		}

		for(TridasMeasurementSeries series : seriesList)
		{			
			EditorFactory.newSeriesFromMeasurementSeries(parent, series, unitsIfNotSpecified);
		}
	}
}
