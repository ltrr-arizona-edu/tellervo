/*******************************************************************************
 * Copyright (C) 2011 Daniel Murphy and Peter Brewer.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Daniel Murphy
 *     Peter Brewer
 ******************************************************************************/
package org.tellervo.desktop.bulkdataentry.command;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.view.ODKParserLogViewer;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.odk.ODKParser;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.util.DictionaryUtil;
import org.tellervo.desktop.wsi.tellervo.TridasElementTemporaryCacher;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.util.TridasObjectEx;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;
import com.ibm.icu.text.SimpleDateFormat;


public class PopulateFromODKFileCommand implements ICommand {
	
	TridasElementTemporaryCacher cache = new TridasElementTemporaryCacher();
	String otherErrors = "";
	Integer filesLoadedSuccessfully = 0;
	Integer filesFound = 0;
	
	@Override
	public void execute(MVCEvent argEvent) {

		try {
			MVC.splitOff(); // so other mvc events can execute
		} catch (IllegalThreadException e) {
			// this means that the thread that called splitOff() was not an MVC thread, and the next event's won't be blocked anyways.
			e.printStackTrace();
		} catch (IncorrectThreadException e) {
			// this means that this MVC thread is not the main thread, it was already splitOff() previously
			e.printStackTrace();
		}

		PopulateFromODKFileEvent event = (PopulateFromODKFileEvent) argEvent;
		ArrayList<ODKParser> filesProcessed = new ArrayList<ODKParser>();
		ArrayList<ODKParser> filesFailed = new ArrayList<ODKParser>();
		
		JFileChooser fc = new JFileChooser(App.prefs.getPref(PrefKey.FOLDER_LAST_READ, null));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			App.prefs.setPref(PrefKey.FOLDER_LAST_READ, fc.getSelectedFile().getAbsolutePath());

			SuffixFileFilter fileFilter = new SuffixFileFilter(".xml");
			@SuppressWarnings("unchecked")
			Iterator<File> iterator = FileUtils.iterateFiles(fc.getSelectedFile(), fileFilter, TrueFileFilter.INSTANCE);	

			File file = null;




			while(iterator.hasNext())
			{
				file = iterator.next();
				filesFound++;

				try {

					if(event.model instanceof ObjectModel)
					{
						ODKParser parser = new ODKParser(file, TridasObject.class);
						filesProcessed.add(parser);

						if(!parser.isValidODKFile()) {
							filesFailed.add(parser);
							continue;
						}

						ObjectModel model = (ObjectModel) event.model;
						addObjectFromParser(parser, model);

					}
					else if (event.model instanceof ElementModel)
					{
						ODKParser parser = new ODKParser(file, TridasElement.class);
						filesProcessed.add(parser);
						
						if(!parser.isValidODKFile()) {
							filesFailed.add(parser);
							continue;
						}

						ElementModel model = (ElementModel) event.model;
						addElementFromParser(parser, model);
					}
					else if (event.model instanceof SampleModel)
					{
						ODKParser parser = new ODKParser(file, TridasSample.class);
						filesProcessed.add(parser);
						
						if(!parser.isValidODKFile()) {
							filesFailed.add(parser);
							continue;
						}

						SampleModel model = (SampleModel) event.model;
						addSampleFromParser(parser, model);
					}

				} catch (FileNotFoundException e) {
					otherErrors+="<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(file);
					otherErrors+="<br/>  - File not found<br/><br/>";
				} catch (IOException e) {
					otherErrors+="<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(file);
					otherErrors+="<br/>  - IOException - "+e.getLocalizedMessage()+"<br/><br/>";
				}  catch (Exception e) {
					otherErrors+="<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(file);
					otherErrors+="<br/>  - Exception - "+e.getLocalizedMessage()+"<br/><br/>";				}    

			}
			
			StringBuilder log = new StringBuilder();
			
			log.append("<html>\n");
			for(ODKParser parser : filesFailed)
			{
				log.append("<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(parser.getFile()));
				log.append("<br/>  - "+parser.getParseErrorMessage()+"<br/><br/>");
			}
			
			for(ODKParser parser : filesProcessed)
			{
				if(filesFailed.contains(parser)) continue;
				if(parser.getParseErrorMessage()=="") continue;
				
				log.append("<p color=\"orange\">Warning loading file:</p>\n"+ ODKParser.formatFileNameForReport(parser.getFile()));
				log.append("<br/>  - "+parser.getParseErrorMessage()+"<br/><br/>");
			}
			
			log.append(otherErrors);
			
			log.append("</html>");
			
			ODKParserLogViewer logDialog = new ODKParserLogViewer(null);
			logDialog.setLog(log.toString());
			logDialog.setFileCount(filesFound, filesLoadedSuccessfully);
			logDialog.setVisible(true);

		}		



	}

	private void addObjectFromParser(ODKParser parser, ObjectModel model)
	{
		SingleObjectModel newrow = (SingleObjectModel) model.createRowInstance();

		newrow.setProperty(SingleObjectModel.OBJECT_CODE, parser.getFieldValueAsString("PlotSubplotID"));
		newrow.setProperty(SingleObjectModel.TITLE, parser.getFieldValueAsString("PlotSubplotID"));
		newrow.setProperty(SingleObjectModel.LATITUDE, parser.getLatitude("Location"));
		newrow.setProperty(SingleObjectModel.LONGITUDE, parser.getLongitude("Location"));
		newrow.setProperty(SingleObjectModel.LOCATION_PRECISION, parser.getError("Location"));
		newrow.setProperty(SingleObjectModel.VEGETATION_TYPE, parser.getFieldValueAsString("VegetationType"));

		newrow.setProperty(SingleObjectModel.COMMENTS, "Imported from "+parser.getFile().getName());


		model.getRows().add(newrow);
		filesLoadedSuccessfully++;
	}

	private void addElementFromParser(ODKParser parser, ElementModel model)
	{
		SingleElementModel newrow = (SingleElementModel) model.createRowInstance();

		String objcode = parser.getFieldValueAsString("PlotSubplotID").toString();
		TridasObjectEx obj = getTridasObjectByCode(objcode);

		newrow.setProperty(SingleElementModel.TITLE, parser.getFieldValueAsString("TreeNO"));
		newrow.setProperty(SingleElementModel.OBJECT, obj);


		newrow.setProperty(SingleElementModel.TAXON, DictionaryUtil.getControlledVocForName("Abies alba Mill.", "taxonDictionary"));
		model.getRows().add(newrow);
		filesLoadedSuccessfully++;
	}


	private void addSampleFromParser(ODKParser parser, SampleModel model)
	{
		Boolean loadedSuccessfully = false;
		
		NodeList nList = parser.getNodeListByName("SamplesRepeat");

		if(nList==null || nList.getLength()==0) return;

		for(int i=0; i<nList.getLength(); i++)
		{
			Node node = nList.item(i);
			SingleSampleModel newrow = (SingleSampleModel) model.createRowInstance();

			String objcode = parser.getFieldValueAsString("PlotSubplotID").toString();
			TridasObjectEx obj = getTridasObjectByCode(objcode);
			newrow.setProperty(SingleSampleModel.OBJECT, obj);
								
			TridasElement element = parser.getTridasElement(cache, "PlotSubplotID", "TreeNO");
			if(element==null) continue;
			
			newrow.setProperty(SingleSampleModel.ELEMENT, element);
			newrow.setProperty(SingleSampleModel.TITLE, 
					parser.getFieldValueAsStringForNodeList("SampleID", node.getChildNodes()));
			
			
			newrow.setProperty(SingleSampleModel.SAMPLING_DATE, parser.getDate());


			model.getRows().add(newrow);
			loadedSuccessfully = true;

		}
		
		if(loadedSuccessfully)
		{
			filesLoadedSuccessfully++;
		}
	}

	/**
	 * Query the object dictionary to find an object based on it's lab code
	 * 
	 * @param code
	 * @return
	 */
	private TridasObjectEx getTridasObjectByCode(String code)
	{
		if(code==null) return null;

		List<TridasObjectEx> entities = App.tridasObjects.getObjectList();

		for(TridasObjectEx obj : entities)
		{
			if(TridasUtils.getGenericFieldByName(obj, "tellervo.objectLabCode").getValue().equals(code))
			{
				return obj;
			}
		}
		return null;
	}
}