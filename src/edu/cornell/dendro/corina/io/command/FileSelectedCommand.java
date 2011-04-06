package edu.cornell.dendro.corina.io.command;

import java.io.IOException;

import org.tridas.io.AbstractDendroFileReader;
import org.tridas.io.TridasIO;
import org.tridas.io.exceptions.ConversionWarning;
import org.tridas.io.exceptions.InvalidDendroFileException;
import org.tridas.io.exceptions.ConversionWarning.WarningType;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.NormalTridasVariable;
import org.tridas.schema.TridasMeasurementSeries;
import org.tridas.schema.TridasValues;

import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.io.control.FileSelectedEvent;
import edu.cornell.dendro.corina.io.model.TridasRepresentationTreeModel;
import edu.cornell.dendro.corina.ui.Alert;

public class FileSelectedCommand implements ICommand {

	@Override
	public void execute(MVCEvent argEvent) {
		FileSelectedEvent event = (FileSelectedEvent) argEvent;
		try {
			event.model.setFileToImport(event.file);
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
			return;
		}
		
		// Create a reader based on the file type supplied
		AbstractDendroFileReader reader;
		reader = TridasIO.getFileReader(event.fileType);
		if(reader==null) 
		{
			Alert.error("Error", "Unknown file type");
			return;
		}
		
		// Try and load the file
		try {
			reader.loadFile(event.file.getAbsolutePath());
		} catch (IOException e) {
			Alert.errorLoading(event.file.getAbsolutePath(), e);
			return;
		} catch (InvalidDendroFileException e) {
			event.model.setFileException(e);
			return;
		}
		catch(NullPointerException e)
		{
			Alert.error("Invalid File", e.getLocalizedMessage());
		}
		
		// Dispatch any warnings
		if(reader.getWarnings().length>0)
		{
			event.model.setConversionWarnings(reader.getWarnings());
		}
		
		// Warn if project contains derivedSeries
		if(reader.getProject().isSetDerivedSeries())
		{
			event.model.appendConversionWarning(new ConversionWarning(
					WarningType.IGNORED, 
					"Corina does not currently support importing of derived series / chronologies"));
		}
		
		// Add custom Corina warnings for unsupported aspects of TRiDaS
		for (TridasMeasurementSeries series: TridasUtils.getMeasurementSeriesFromTridasProject(reader.getProject()))
		{
			if(series.isSetValues())
			{
				for(TridasValues values : series.getValues())
				{
					if(values.isSetUnit())
					{
						if(!values.getUnit().isSetNormalTridas())
						{
							event.model.appendConversionWarning(new ConversionWarning(
									WarningType.NOT_STRICT, 
									"Series does not use standard units - assuming 1/100th mm"));
						}
					}
					else
					{
						event.model.appendConversionWarning(new ConversionWarning(
								WarningType.AMBIGUOUS, 
								"Series does not have units associated - assuming 1/100th mm"));
					}
					
					if(values.isSetVariable())
					{
						if(values.getVariable().isSetNormalTridas())
						{
							NormalTridasVariable var = values.getVariable().getNormalTridas();
							
							switch(var)
							{
							case RING_WIDTH:
								break;
							default:
								event.model.appendConversionWarning(new ConversionWarning(
										WarningType.UNREPRESENTABLE, 
										"Corina currently only supports whole ring width values"));
							}
						}
					}
					else
					{
						event.model.appendConversionWarning(new ConversionWarning(
								WarningType.AMBIGUOUS, 
								"Series does not have variables - assuming whole ring width"));
					}
				}
			}
		}
		
		
		// Extract project from file
		TridasRepresentationTreeModel treeMdl = new TridasRepresentationTreeModel(reader.getProject());
		event.model.setTreeModel(treeMdl);
		
	}
	
	

}
