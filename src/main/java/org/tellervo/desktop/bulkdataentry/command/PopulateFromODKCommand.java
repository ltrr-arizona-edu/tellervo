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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.PopulateFromODKFileEvent;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.bulkdataentry.model.ElementModel;
import org.tellervo.desktop.bulkdataentry.model.ObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SampleModel;
import org.tellervo.desktop.bulkdataentry.model.SingleElementModel;
import org.tellervo.desktop.bulkdataentry.model.SingleObjectModel;
import org.tellervo.desktop.bulkdataentry.model.SingleSampleModel;
import org.tellervo.desktop.bulkdataentry.model.TridasElementOrPlaceholder;
import org.tellervo.desktop.bulkdataentry.model.TridasFileList;
import org.tellervo.desktop.bulkdataentry.model.TridasObjectOrPlaceholder;
import org.tellervo.desktop.bulkdataentry.view.ODKFileDownloadProgress;
import org.tellervo.desktop.bulkdataentry.view.ODKParserLogViewer;
import org.tellervo.desktop.bulkdataentry.view.odkwizard.ODKImportWizard;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.odk.ODKParser;
import org.tellervo.desktop.odk.ODKParser.ODKFileType;
import org.tellervo.desktop.odk.fields.ODKFieldInterface;
import org.tellervo.desktop.odk.fields.ODKFields;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.BugReport;
import org.tellervo.desktop.util.DictionaryUtil;
import org.tellervo.desktop.versioning.Build;
import org.tellervo.desktop.wsi.WebJaxbAccessor;
import org.tellervo.desktop.wsi.tellervo.TridasElementTemporaryCacher;
import org.tellervo.desktop.wsi.util.WSCookieStoreHandler;
import org.tridas.io.util.ITRDBTaxonConverter;
import org.tridas.io.util.TridasUtils;
import org.tridas.schema.ControlledVoc;
import org.tridas.schema.NormalTridasLocationType;
import org.tridas.schema.NormalTridasShape;
import org.tridas.schema.TridasElement;
import org.tridas.schema.TridasFile;
import org.tridas.schema.TridasObject;
import org.tridas.schema.TridasSample;
import org.tridas.schema.TridasShape;
import org.tridas.util.TridasObjectEx;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import au.com.bytecode.opencsv.CSVWriter;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;


public class PopulateFromODKCommand implements ICommand {
	
	TridasElementTemporaryCacher cache = new TridasElementTemporaryCacher();
	String otherErrors = "";
	Integer filesLoadedSuccessfully = 0;
	Integer filesFound = 0;
	private static final Logger log = LoggerFactory.getLogger(PopulateFromODKCommand.class);

	private static void debugODKCodes()
	{
		ODKFieldInterface[] fields = ODKFields.getFieldsAsArray(TridasObject.class);
		for(ODKFieldInterface field : fields)
		{
			log.debug(field.getFieldCode());
		}
		
		ODKFieldInterface[] fields2 = ODKFields.getFieldsAsArray(TridasElement.class);
		for(ODKFieldInterface field : fields2)
		{
			log.debug(field.getFieldCode());
		}
		
		ODKFieldInterface[] fields3 = ODKFields.getFieldsAsArray(TridasSample.class);
		for(ODKFieldInterface field : fields3)
		{
			log.debug(field.getFieldCode());
		}
	}
	
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
		Path instanceFolder = null;
		
		// Launch the ODK wizard to collect parameters from user
		ODKImportWizard wizard = new ODKImportWizard(BulkImportModel.getInstance().getMainView());
				
		if(wizard.wasCancelled()) return;
		
		if(wizard.isRemoteAccessSelected())
		{
			// Doing remote server download of ODK files
			try {
				
				// Request a zip file of ODK files from the server ensuring the temp file is deleted on exit
				URI uri;
				uri = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/fetchInstances.php");
				String file = getRemoteODKFiles(uri);
				
				if(file==null) {
					// Download was cancelled
					return;
				}
				
				new File(file).deleteOnExit();
				
				// Unzip to a temporary folder, again ensuring it is deleted on exit 
				instanceFolder = Files.createTempDirectory("odk-unzip");
				instanceFolder.toFile().deleteOnExit();
				
				log.debug("Attempting to open zip file: '"+file+"'");
				
				ZipFile zipFile = new ZipFile(file);
			    try {
			      Enumeration<? extends ZipEntry> entries = zipFile.entries();
			      while (entries.hasMoreElements()) {
			        ZipEntry entry = entries.nextElement();
			        File entryDestination = new File(instanceFolder.toFile(),  entry.getName());
			        entryDestination.deleteOnExit();
			        if (entry.isDirectory()) {
			            entryDestination.mkdirs();
			        } else {
			            entryDestination.getParentFile().mkdirs();
			            InputStream in = zipFile.getInputStream(entry);
			            OutputStream out = new FileOutputStream(entryDestination);
			            IOUtils.copy(in, out);
			            IOUtils.closeQuietly(in);
			            out.close();
			        }
			      }
			    } finally {
			      zipFile.close();
			    }
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			// Accessing ODK files from local folder
			instanceFolder = new File(wizard.getODKInstancesFolder()).toPath();
		}
		
		
		// Check the instance folder specified exists
		File folder = instanceFolder.toFile();
		if(!folder.exists()) {
		
			log.error("Instances folder does not exist");
			return;
		}

		
		// Compile a hash set of all media files in the instance folder and subfolders
		File file = null;
		File[] mediaFileArr = null;
		if(wizard.isIncludeMediaFilesSelected())
		{
			HashSet<File> mediaFiles = new HashSet<File>();

			// Array of file extensions to consider as media files
			String[] mediaExtensions = {"jpg", "mpg", "snd", "mp4", "m4a"};
			for(String ext : mediaExtensions)
			{
				SuffixFileFilter filter = new SuffixFileFilter("."+ext);
				Iterator<File> it = FileUtils.iterateFiles(folder, filter, TrueFileFilter.INSTANCE);
				while(it.hasNext())
				{
					file = it.next();
					mediaFiles.add(file);
				}
			}
			
			// Copy files to consolidate to a new folder 
			mediaFileArr = mediaFiles.toArray(new File[mediaFiles.size()]);
			String copyToFolder = wizard.getCopyToLocation();
			for(int i=0; i<mediaFileArr.length; i++)
			{
				file = mediaFileArr[i];
				
				File target = new File(copyToFolder+file.getName());
				try {
					FileUtils.copyFile(file, target, true);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mediaFileArr[i] = target;
			}
		}
		
		SampleModel smodel = event.sampleModel;
		ElementModel emodel = event.elementModel;
		ObjectModel model = event.objectModel;
		
		try{	
			if(smodel.getTableModel().getRowCount()==1 && 
					smodel.getTableModel().getAllSingleRowModels().get(0).getProperty(SingleSampleModel.TITLE)==null)
			{
				// Empty table first
				smodel.getTableModel().getAllSingleRowModels().clear();
			}
		}catch (Exception ex)
		{
			log.debug("Error deleting empty rows");
		}
			
		try{
			if(emodel.getTableModel().getRowCount()==1 && 
					emodel.getTableModel().getAllSingleRowModels().get(0).getProperty(SingleElementModel.TITLE)==null)
			{
				// Empty table first
				emodel.getTableModel().getAllSingleRowModels().clear();
			}
		} catch (Exception ex)
		{
			log.debug("Error deleting empty rows");
		}
		
		try{
			if(model.getTableModel().getRowCount()==1 && 
					model.getTableModel().getAllSingleRowModels().get(0).getProperty(SingleObjectModel.OBJECT_CODE)==null)
			{
				// Empty table first
				model.getTableModel().getAllSingleRowModels().clear();
			}
		} catch (Exception ex)
		{
			log.debug("Error deleting empty rows");
		}
		
		
		SuffixFileFilter fileFilter = new SuffixFileFilter(".xml");
		Iterator<File> iterator = FileUtils.iterateFiles(folder, fileFilter, TrueFileFilter.INSTANCE);
		while(iterator.hasNext())
		{
			file = iterator.next();
			filesFound++;

			try {

				ODKParser parser = new ODKParser(file);
				filesProcessed.add(parser);
								
				if(!parser.isValidODKFile()) {
					filesFailed.add(parser);
					continue;
				}
				else if(parser.getFileType()==null)
				{
					filesFailed.add(parser);
					continue;
				}
				else if (parser.getFileType()==ODKFileType.OBJECTS)
				{
					addObjectToTableFromParser(parser, model, wizard, mediaFileArr);
				}
				else if (parser.getFileType()==ODKFileType.ELEMENTS_AND_SAMPLES)
				{
					addElementFromParser(parser, emodel, wizard, mediaFileArr);										
					addSampleFromParser(parser, smodel, wizard, mediaFileArr);
				}
				else
				{
					filesFailed.add(parser);
					continue;
				}

			} catch (FileNotFoundException e) {
				otherErrors+="<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(file);
				otherErrors+="<br/>  - File not found<br/><br/>";
			} catch (IOException e) {
				otherErrors+="<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(file);
				otherErrors+="<br/>  - IOException - "+e.getLocalizedMessage()+"<br/><br/>";
			}  catch (Exception e) {
				otherErrors+="<p color=\"red\">Error loading file:</p>\n"+ ODKParser.formatFileNameForReport(file);
				otherErrors+="<br/>  - Exception - "+e.getLocalizedMessage()+"<br/><br/>";				
			}    

		}
		
		// Create a CSV file of metadata if the user requested it
		if(wizard.isCreateCSVFileSelected())
		{
			try {
				createCSVFile(filesProcessed, wizard.getCSVFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// Compile logs to display to user
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
		ODKParserLogViewer logDialog = new ODKParserLogViewer(BulkImportModel.getInstance().getMainView());
		logDialog.setLog(log.toString());
		logDialog.setFileCount(filesFound, filesLoadedSuccessfully);
		
		// Display log if there were any errors or if no files were found
		if(filesFound>filesLoadedSuccessfully ) 
		{
			logDialog.setVisible(true);
		}
		else if (filesFound==0 && wizard.isRemoteAccessSelected())
		{
			Alert.error(BulkImportModel.getInstance().getMainView(), "Not found", "No ODK data files were found on the server.  Please ensure you've used the 'send finalized form' option in ODK Collect and try again");
			return;
		}
		else if (filesFound==0)
		{
			Alert.error(BulkImportModel.getInstance().getMainView(), "Not found", "No ODK data files were found in the specified folder.  Please check and try again.");
			return;
		}
		else if(wizard.isIncludeMediaFilesSelected())
		{
			Alert.message(BulkImportModel.getInstance().getMainView(), "Download Complete", "The ODK download is complete.  As requested, your media files have been temporarily copied to the local folder '"+wizard.getCopyToLocation()+"'.  Please remember to move them to their final location");
		}

	}		
		
	
	public static String getRemoteODKFiles(URI url) throws IOException {
		
		ODKFileDownloadProgress dialog = new ODKFileDownloadProgress(App.mainWindow, url);
		
		if(dialog.getFile()==null)
		{
			log.error("Download failed");
			return null;
		}
		return dialog.getFile().getAbsolutePath();
		
	}
	
	private void createCSVFile(ArrayList<ODKParser> parsers, String csvfilename ) throws IOException
	{
		File file = new File(csvfilename);
		file.createNewFile();
		if(!file.canWrite()) throw new IOException("Cannot write to file: "+file.getAbsolutePath());
		
		HashSet<String> fieldNames = new HashSet<String>();
		for(ODKParser parser: parsers)
		{
			HashMap<String, String> fields = parser.getAllFields();
		    Iterator it = fields.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        fieldNames.add((String) pair.getKey());
		    }
		}
		
		String[] fieldNamesArr =fieldNames.toArray(new String[fieldNames.size()]);
		
		
		String[][] table = new String[parsers.size()][fieldNames.size()];
		

		for(int r=0; r<parsers.size(); r++)
		{
			ODKParser parser = parsers.get(r);
			
			for(int c=0; c<fieldNamesArr.length; c++)
			{	
				table[r][c] = parser.getFieldValueAsString(fieldNamesArr[c]);
			}
		}
			
		CSVWriter writer = new CSVWriter(new FileWriter(csvfilename), '\t');
		


		String[] header = fieldNames.toArray(new String[fieldNames.size()]);
		
		writer.writeNext(header);
		for(int r=0; r<table.length; r++)
		{
			writer.writeNext(table[r]);
		}
		
		writer.close();


		
	}
	
	/*@Override
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
		
		//debugODKCodes();
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



	}*/
	
	private void addObjectToTableFromParser(ODKParser parser, ObjectModel model, ODKImportWizard wizard, File[] mediaFileArr)
	{
		SingleObjectModel newrow = (SingleObjectModel) model.createRowInstance();

		String objcode = parser.getFieldValueAsString("tridas_parent_object_code");
		
		if(objcode!=null) 
		{
			TridasObjectEx obj = getTridasObjectByCode(objcode);
			TridasObjectOrPlaceholder toph;
			if(obj!=null)
			{
				toph = new TridasObjectOrPlaceholder(obj);
				newrow.setProperty(SingleObjectModel.PARENT_OBJECT, toph);
			}
			else
			{
				toph = new TridasObjectOrPlaceholder(objcode);
				newrow.setProperty(SingleObjectModel.PARENT_OBJECT, toph);
			}
		}
		
		String objectcode = parser.getFieldValueAsString("tridas_object_code");
		newrow.setProperty(SingleObjectModel.OBJECT_CODE, objectcode);
		newrow.setProperty(SingleObjectModel.TITLE, parser.getFieldValueAsString("tridas_object_title"));

		
		try{
			newrow.setProperty(SingleObjectModel.TYPE, DictionaryUtil.getControlledVocForName(parser.getFieldValueAsString("tridas_object_type"), "objectTypeDictionary"));
		} catch (Exception e)
		{
			log.debug("Error getting dictionary value from tag");
			newrow.setProperty(SingleObjectModel.TYPE, DictionaryUtil.getControlledVocForName("Forest", "objectTypeDictionary"));

		}
		String comments = "";
		if(parser.getFieldValueAsString("tridas_object_comments")!=null) comments += parser.getFieldValueAsString("tridas_object_comments")+"; ";
		
		//if(!wizard.isRemoteAccessSelected()) comments += "Imported from ODK file: '"+ parser.getFile().getName()+"'. ";
		
		newrow.setProperty(SingleObjectModel.COMMENTS, comments);
		String description = parser.getFieldValueAsString("tridas_object_description");
		if(parser.getFieldValueAsString("StandType")!=null) description += " "+parser.getFieldValueAsString("StandType");

		newrow.setProperty(SingleObjectModel.DESCRIPTION, description);
		newrow.setProperty(SingleObjectModel.LATITUDE, parser.getLatitude("tridas_object_location", "Location"));
		newrow.setProperty(SingleObjectModel.LONGITUDE, parser.getLongitude("tridas_object_location", "Location"));
		String loctype = parser.getFieldValueAsString("tridas_object_location_type");
		newrow.setProperty(SingleObjectModel.LOCATION_TYPE, NormalTridasLocationType.fromValue(loctype));
		newrow.setProperty(SingleObjectModel.LOCATION_PRECISION, parser.getError("tridas_object_location", "Location"));
		newrow.setProperty(SingleObjectModel.LOCATION_COMMENT, parser.getFieldValueAsString("tridas_object_location_comments"));
		newrow.setProperty(SingleObjectModel.ADDRESSLINE1, parser.getFieldValueAsString("tridas_object_address_line1"));
		newrow.setProperty(SingleObjectModel.ADDRESSLINE2, parser.getFieldValueAsString("tridas_object_address_line2"));
		newrow.setProperty(SingleObjectModel.CITY_TOWN, parser.getFieldValueAsString("tridas_object_address_cityortown"));
		newrow.setProperty(SingleObjectModel.STATE_PROVINCE_REGION, parser.getFieldValueAsString("tridas_object_address_stateorprovince"));
		newrow.setProperty(SingleObjectModel.POSTCODE, parser.getFieldValueAsString("tridas_object_address_postalcode"));
		newrow.setProperty(SingleObjectModel.COUNTRY, parser.getFieldValueAsString("tridas_object_address_country"));
		newrow.setProperty(SingleObjectModel.VEGETATION_TYPE, parser.getFieldValueAsString("tridas_object_vegetation_type"));
		newrow.setProperty(SingleObjectModel.OWNER, parser.getFieldValueAsString("tridas_object_owner"));
		newrow.setProperty(SingleObjectModel.CREATOR, parser.getFieldValueAsString("tridas_object_creator"));

		// Handle media files..
		TridasFileList filesList = this.getMediaFileList(parser, mediaFileArr, wizard, objectcode);
		if(filesList!=null && filesList.size()>0)
		{
			newrow.setProperty(SingleObjectModel.FILES, filesList);
		}
		
		


		
		
		model.getRows().add(newrow);
		filesLoadedSuccessfully++;
	}
	

	private TridasFileList getMediaFileList(ODKParser parser, File[] mediaFileArr, ODKImportWizard wizard, String codeprefix)
	{
		return getMediaFileList(parser, null, mediaFileArr, wizard, codeprefix);
	}
	
	/**
	 * Get a list of media files 
	 * 
	 * @param parser
	 * @param mediaFilenames
	 * @param mediaFileArr
	 * @param wizard
	 * @param codeprefix
	 * @return
	 */
	private TridasFileList getMediaFileList(ODKParser parser, ArrayList<String> mediaFilenames, File[] mediaFileArr, ODKImportWizard wizard, String codeprefix)
	{
		TridasFileList filesList = new TridasFileList();

		if(mediaFilenames==null) mediaFilenames = parser.getMediaFileFields();
		
		
		for(String mediafile : mediaFilenames)
		{
			
			if(mediafile==null) continue;
			
			File f = getFileFromList(mediaFileArr, mediafile);
			if(f!=null)
			{
				// Rename file if requested
				if(wizard.isRenameMediaFilesSelected())
				{			
					try {
						Path path = renameFile(f, wizard.getFilenamePrefix()+codeprefix+"."+FilenameUtils.getExtension(f.getName()));
						f = path.toFile();
						
											
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	
				String finalloc = wizard.getFinalLocation();
				if(!finalloc.endsWith("/")) finalloc = finalloc+"/";
				String fullurl = finalloc+f.getName();
				
				
				TridasFile tf = new TridasFile();
				tf.setHref(fullurl);
				filesList.add(tf);
			}
			else
			{
				log.warn("Media file '"+mediafile +"' not found. Ignoring.");
				log.warn("Media file list: "+mediaFileArr);
			}
		}
		
		return filesList;
	}
	
	/**
	 * Search for a file in the File[] based on the filename ignoring the path
	 * 
	 * @param mediaFileArr
	 * @param filename
	 * @return
	 */
	private File getFileFromList(File[] mediaFileArr, String filename)
	{
		for(File f :mediaFileArr)
		{
			if(f.getName().equals(filename))
			{
				return f;
			}
		}
		
		return null;
	}
	
	/**
	 * Rename a file, ensuring the new file is unique.  If a file with the suggested new name already exists then an index is added to the end of the filename.
	 * 
	 * @param fileToRename
	 * @param newname
	 * @return
	 * @throws IOException
	 */
	private Path renameFile(File fileToRename, String newname) throws IOException
	{
		File newFile = getUniqueFilename(new File(fileToRename.getParent(), newname));
		log.debug("Renaming file from '"+fileToRename.toString()+"' to '"+newFile+"'");

		
		return Files.move(fileToRename.toPath(), newFile.toPath(), StandardCopyOption.ATOMIC_MOVE);
		
	}
	
	/**
	 * Ensures a filename is unique by adding an index on the end if the file already exists
	 * 
	 * @param file
	 * @return
	 */
	private File getUniqueFilename(File file)
	{
		if(file.exists())
		{
			file = getUniqueFilename(file, 1);
		}

		return file;
		
	}
	
	/**
	 * Ensures a filename is unique by adding an index on the end if the file already exists
	 * 
	 * @param file
	 * @param i
	 * @return
	 */
	private File getUniqueFilename(File file, int i)
	{
		String index = "("+i+").";
		File originalfile = file;
		String filename = file.getAbsolutePath();
		file = new File(FilenameUtils.getPrefix(filename)+
				FilenameUtils.getPath(filename)+
				FilenameUtils.removeExtension(file.getName())+
				index+
				FilenameUtils.getExtension(filename));
		
		if(file.exists())
		{
			return getUniqueFilename(originalfile, i+1);
		}
		
		return file;
		
	}

	private void addElementFromParser(ODKParser parser, ElementModel model, ODKImportWizard wizard, File[] mediaFileArr)
	{
		SingleElementModel newrow = (SingleElementModel) model.createRowInstance();

		String objcode = parser.getFieldValueAsString("tridas_object_code").toString();
		TridasObjectEx obj = getTridasObjectByCode(objcode);
		
		TridasObjectOrPlaceholder object;
		if(obj!=null)
		{
			object = new TridasObjectOrPlaceholder(obj);
		}
		else
		{
			object = new TridasObjectOrPlaceholder(objcode);
		}
		newrow.setProperty(SingleElementModel.OBJECT, object);

		newrow.setProperty(SingleElementModel.TITLE, parser.getFieldValueAsString("tridas_element_title"));
		newrow.setProperty(SingleElementModel.COMMENTS, parser.getFieldValueAsString("tridas_element_comments"));
		try{
			newrow.setProperty(SingleElementModel.TYPE, DictionaryUtil.getControlledVocForName(parser.getFieldValueAsString("tridas_element_type"), "elementTypeDictionary"));
		} catch (Exception e)
		{
			log.debug("Failed to get element type from ODK");
		}
		newrow.setProperty(SingleElementModel.DESCRIPTION, parser.getFieldValueAsString("tridas_element_description"));
		newrow.setProperty(SingleElementModel.TAXON, DictionaryUtil.getControlledVocForName(parser.getFieldValueAsString("tridas_element_taxon"), "taxonDictionary"));
		newrow.setProperty(SingleElementModel.AUTHENTICITY, parser.getFieldValueAsString("tridas_element_authenticity"));
		newrow.setProperty(SingleElementModel.LATITUDE, parser.getLatitude("tridas_element_location", "TreeLocation"));
		newrow.setProperty(SingleElementModel.LONGITUDE, parser.getLongitude("tridas_element_location", "TreeLocation"));
		String loctype = parser.getFieldValueAsString("tridas_element_location_type");
		newrow.setProperty(SingleElementModel.LOCATION_TYPE, NormalTridasLocationType.fromValue(loctype));
		newrow.setProperty(SingleElementModel.LOCATION_PRECISION, parser.getError("tridas_element_location", "TreeLocation"));
		newrow.setProperty(SingleElementModel.LOCATION_COMMENT, parser.getFieldValueAsString("tridas_element_location_comments"));
		newrow.setProperty(SingleElementModel.ADDRESSLINE1, parser.getFieldValueAsString("tridas_element_address_line1"));
		newrow.setProperty(SingleElementModel.ADDRESSLINE2, parser.getFieldValueAsString("tridas_element_address_line2"));
		newrow.setProperty(SingleElementModel.CITY_TOWN, parser.getFieldValueAsString("tridas_element_address_cityortown"));
		newrow.setProperty(SingleElementModel.STATE_PROVINCE_REGION, parser.getFieldValueAsString("tridas_element_address_stateorprovince"));
		newrow.setProperty(SingleElementModel.POSTCODE, parser.getFieldValueAsString("tridas_element_address_postalcode"));
		newrow.setProperty(SingleElementModel.COUNTRY, parser.getFieldValueAsString("tridas_element_address_country"));
		newrow.setProperty(SingleElementModel.PROCESSING, parser.getFieldValueAsString("tridas_element_processing"));
		newrow.setProperty(SingleElementModel.MARKS, parser.getFieldValueAsString("tridas_element_marks"));
		newrow.setProperty(SingleElementModel.BEDROCK_DESCRIPTION, parser.getFieldValueAsString("tridas_element_bedrock_description"));
		newrow.setProperty(SingleElementModel.SOIL_DESCRIPTION, parser.getFieldValueAsString("tridas_element_soil_description"));
		newrow.setProperty(SingleElementModel.SLOPE_ANGLE, parser.getFieldValueAsInteger("tridas_element_slope_angle"));
		newrow.setProperty(SingleElementModel.SLOPE_AZIMUTH, parser.getFieldValueAsInteger("tridas_element_slope_azimuth"));
		newrow.setProperty(SingleElementModel.SOIL_DEPTH, parser.getFieldValueAsDouble("tridas_element_soil_depth"));
		newrow.setProperty(SingleElementModel.HEIGHT, parser.getFieldValueAsDouble("tridas_element_dimensions_height"));
		newrow.setProperty(SingleElementModel.WIDTH, parser.getFieldValueAsDouble("tridas_element_dimensions_width"));
		newrow.setProperty(SingleElementModel.DEPTH, parser.getFieldValueAsDouble("tridas_element_dimensions_depth"));

		newrow.setProperty(SingleElementModel.ALTITUDE, parser.getElevation("tridas_element_location", "TreeLocation"));
		NormalTridasShape shape = NormalTridasShape.fromValue(parser.getFieldValueAsString("tridas_element_shape"));
		if(shape!=null)
		{
			TridasShape ts = new TridasShape();
			ts.setNormalTridas(shape);
			newrow.setProperty(SingleElementModel.SHAPE, ts);
		}

		// Handle media files...
		String code = objcode+"-"+parser.getFieldValueAsString("tridas_element_title");
		TridasFileList filesList = this.getMediaFileList(parser, mediaFileArr, wizard, code);
		if(filesList!=null && filesList.size()>0)
		{
			newrow.setProperty(SingleElementModel.FILES, filesList);
		}

		
		model.getRows().add(newrow);
		filesLoadedSuccessfully++;
	}


	private void addSampleFromParser(ODKParser parser, SampleModel model, ODKImportWizard wizard, File[] mediaFileArr)
	{		
		NodeList nList = parser.getNodeListByName("group_sample_fields");

		if(nList==null || nList.getLength()==0) {
			nList = parser.getNodeListByName("SamplesRepeat");
		}
		
		if(nList==null || nList.getLength()==0) {
			return;
		}

		for(int i=0; i<nList.getLength(); i++)
		{
			Node node = nList.item(i);
			SingleSampleModel newrow = (SingleSampleModel) model.createRowInstance();
								
			String objcode = parser.getFieldValueAsString("tridas_object_code").toString();
			TridasObjectEx obj = getTridasObjectByCode(objcode);
			
			TridasObjectOrPlaceholder object;
			if(obj!=null)
			{
				object = new TridasObjectOrPlaceholder(obj);
			}
			else
			{
				object = new TridasObjectOrPlaceholder(objcode);
			}
			newrow.setProperty(SingleSampleModel.OBJECT, object);
					
			String elmcode = parser.getFieldValueAsString("tridas_element_title").toString();
			if(object.getTridasObject()!=null)
			{
				TridasElement element = parser.getTridasElement(cache, "tridas_object_code", "tridas_element_title");
				if(element==null){
					newrow.setProperty(SingleSampleModel.ELEMENT, new TridasElementOrPlaceholder(elmcode));	
				}
				else
				{
					newrow.setProperty(SingleSampleModel.ELEMENT, new TridasElementOrPlaceholder(element));	
				}
			}
			else
			{
				newrow.setProperty(SingleSampleModel.ELEMENT, new TridasElementOrPlaceholder(elmcode));	
			}
			
			String samplecode = parser.getFieldValueAsStringFromNodeList("tridas_sample_title", node.getChildNodes());
			log.debug("Sample code is: "+samplecode);
			newrow.setProperty(SingleSampleModel.TITLE, samplecode);
			newrow.setProperty(SingleSampleModel.TYPE, DictionaryUtil.getControlledVocForName(parser.getFieldValueAsString("tridas_sample_type"), "sampleTypeDictionary"));
			newrow.setProperty(SingleSampleModel.COMMENTS, parser.getFieldValueAsStringFromNodeList("tridas_sample_comments", node.getChildNodes()));
			newrow.setProperty(SingleSampleModel.DESCRIPTION, parser.getFieldValueAsStringFromNodeList("tridas_sample_description", node.getChildNodes()));
			newrow.setProperty(SingleSampleModel.SAMPLING_DATE, parser.getDate());
			newrow.setProperty(SingleSampleModel.POSITION, parser.getFieldValueAsStringFromNodeList("tridas_sample_position", node.getChildNodes()));
			newrow.setProperty(SingleSampleModel.STATE, parser.getFieldValueAsStringFromNodeList("tridas_sample_state", node.getChildNodes()));
			newrow.setProperty(SingleSampleModel.EXTERNAL_ID, parser.getFieldValueAsStringFromNodeList("tridas_sample_externalid", node.getChildNodes()));

			String knots = parser.getFieldValueAsStringFromNodeList("tridas_sample_knots", node.getChildNodes());
			Boolean kb = null;
			if(knots!=null)
			{
				if(knots.equals("Yes"))
				{
					kb = true;
				}
				else
				{
					kb = false;
				}
			}
			
			newrow.setProperty(SingleSampleModel.KNOTS, kb);

			// Handle media files...
			String code = objcode+"-"+elmcode+"-"+parser.getFieldValueAsString("tridas_sample_title");
			ArrayList<String> fieldnames = new ArrayList<String>();
			fieldnames.add("tridas_sample_file_photo");
			fieldnames.add("tridas_sample_file_video");
			fieldnames.add("tridas_sample_file_sound");
			TridasFileList filesList = this.getMediaFileList(parser, fieldnames, mediaFileArr, wizard, code);
			if(filesList!=null && filesList.size()>0)
			{
				newrow.setProperty(SingleElementModel.FILES, filesList);
			}
			
			model.getRows().add(newrow);
		}

	}

	/**
	 * Query the object dictionary to find an object based on it's lab code
	 * 
	 * @param code
	 * @return
	 */
	private static TridasObjectEx getTridasObjectByCode(String code)
	{
		if(code==null) return null;

		List<TridasObjectEx> entities = App.tridasObjects.getObjectList();

		for(TridasObjectEx obj : entities)
		{
			if(TridasUtils.getGenericFieldByName(obj, TridasUtils.GENERIC_FIELD_STRING_OBJECTCODE).getValue().equals(code))
			{
				return obj;
			}
		}
		return null;
	}
}
