package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.openrosa.xforms.xformslist.Xform;
import org.openrosa.xforms.xformslist.Xforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.command.PopulateFromODKCommand;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.ui.Builder;
import org.tridas.io.I18n;
import org.tridas.io.util.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.emory.mathcs.backport.java.util.Collections;
import net.miginfocom.swing.MigLayout;

public class ODKManagerPanel extends JPanel implements ActionListener {
	private JList<Xform> lstDesign;
	protected final static Logger log = LoggerFactory
			.getLogger(ODKManagerPanel.class);
	private JButton btnDeleteDesign;
	private JList lstInstances;

	/**
	 * Create the panel.
	 */
	public ODKManagerPanel() {

		setupGUI();

	}
	
	public void setupGUI()
	{

			setLayout(new BorderLayout(0, 0));

			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			add(tabbedPane);

			JPanel panelFormDesign = new JPanel();
			tabbedPane.addTab("Form Designs", null, panelFormDesign, null);
			panelFormDesign.setLayout(new BorderLayout(0, 0));

			lstDesign = new JList<Xform>();	
			//lstDesign.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lstDesign.setCellRenderer(new XformRenderer());
			
	
			panelFormDesign.add(lstDesign);
	
			JPanel panelButtons = new JPanel();
			panelFormDesign.add(panelButtons, BorderLayout.SOUTH);
			panelButtons.setLayout(new MigLayout("", "[grow][]", "[]"));
	
			btnDeleteDesign = new JButton("Delete Selected");
			btnDeleteDesign.setActionCommand("DeleteFormDefinition");
			btnDeleteDesign.addActionListener(this);
			panelButtons.add(btnDeleteDesign, "cell 1 0");
	
			JPanel panelFormInstances = new JPanel();
			tabbedPane.addTab("Form Data", null, panelFormInstances, null);
			panelFormInstances.setLayout(new BorderLayout(0, 0));
			
			JScrollPane scrollPane = new JScrollPane();
			panelFormInstances.add(scrollPane, BorderLayout.CENTER);
			
			lstInstances = new JList<ODKParser>();	
			lstInstances.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			scrollPane.setViewportView(lstInstances);
			lstInstances.setCellRenderer(new ODKParserRenderer());

			JPanel instancesPanel = new JPanel();
			panelFormInstances.add(instancesPanel, BorderLayout.SOUTH);
			instancesPanel.setLayout(new MigLayout("", "[grow][][]", "[]"));
			
			JButton btnPopulateInstance = new JButton("Populate");
			btnPopulateInstance.setActionCommand("populateInstance");
			btnPopulateInstance.addActionListener(this);
			instancesPanel.add(btnPopulateInstance, "cell 1 0");
			
			
			JButton btnDeleteSelectedInstance = new JButton("Delete Selected");
			btnDeleteSelectedInstance.setActionCommand("deleteInstance");
			btnDeleteSelectedInstance.addActionListener(this);
			instancesPanel.add(btnDeleteSelectedInstance, "cell 2 0");
		
	}

	public boolean populateDefinitionsList() {
		
		return populateDefinitionsList(false);
	}
	
	private boolean populateDefinitionsList(boolean forceNewCredentials) {
		
				
		URI url;
		try {
			url = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/formList.php");
	
			
			  CredentialsProvider credsProvider = App.getODKCredentialsProvider(forceNewCredentials);
			  
		        CloseableHttpClient httpclient = HttpClients.custom()
		                .setDefaultCredentialsProvider(credsProvider)
		                .build();
		
		            HttpGet httpget = new HttpGet(url);

		            System.out.println("Executing request " + httpget.getRequestLine());
		            CloseableHttpResponse response = httpclient.execute(httpget);
		        
		                System.out.println("----------------------------------------");
		                System.out.println(response.getStatusLine());
		                
		                if(response.getStatusLine().toString().contains("401 Unauthorized"))
		                {
		                	Alert.error("Error", "Invalid password");
		                	return populateDefinitionsList(true);
		                	
		                }
		                
		                String body = EntityUtils.toString(response.getEntity());
		            	
		                List<Xform> xforms = this.getXformsFromXMLFile(body, true);

		        		XformListModel model = new XformListModel();

		    			for (Xform form : xforms) {
		    				model.addItem(form);

		    			}
		    			this.lstDesign.setModel(model);
		    			return true;
	
		} catch (UnknownHostException e) {
			log.error("The URL of the server you have specified is unknown");
			return false;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contentEquals("Cancelled"))
			{
				return false;
			}
		}
		
		return false;

		
	}

	/**
	 * Open an ICMS XML file, validate, parse and return a list of
	 * RediscoveryExport items
	 * 
	 * @param filename
	 * @param logErrors
	 *            - if false then throw exceptions if true just log errors
	 * @return
	 */
	public static List<Xform> getXformsFromXMLFile(String body,
			boolean logErrors) throws Exception {

		
		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL file = IOUtils.getFileInJarURL("schemas/xformlist.xsd");
		Schema schema = null;
		StringReader reader;
		if (file == null) {
			log.error(I18n.getText("Schema missing"));
			return null;
		} else {
			// Next try to load the schema to validate
			try {
				schema = factory.newSchema(file);
			} catch (Exception e) {

				if (logErrors) {
					log.error(I18n.getText("Schema missing",
							e.getLocalizedMessage()));
				} else {
					throw e;
				}

				return null;
			}

			reader = new StringReader(body);

			// Do the validation
			Validator validator = schema.newValidator();
			StreamSource source = new StreamSource();
			source.setReader(reader);
			try {
				MyErrorHandler eh = new MyErrorHandler();
				eh.logErrors = logErrors;
				validator.setFeature("http://xml.org/sax/features/validation",
						true);
				validator.setFeature(
						"http://apache.org/xml/features/validation/schema",
						true);
				validator.setErrorHandler(eh);
				validator.validate(source);

				HashSet<Integer> errors = eh.getLineErrors();

				List<Integer> list = new ArrayList<Integer>(errors);
				Collections.sort(list);

				if (logErrors) {
					for (Integer i : list) {
						log.error("Error found on line " + i.toString());
					}
				}

			} catch (SAXException ex) {

				if (logErrors) {
					log.error(ex.getLocalizedMessage());
				} else {
					throw ex;
				}

				return null;
			} catch (IOException e) {

				if (logErrors) {
					log.error(e.getLocalizedMessage());
				} else {
					throw e;
				}

				return null;
			}
		}

		// All should be ok so now unmarshall to Java classes
		JAXBContext jc;
		reader = new StringReader(body);
		try {
			jc = JAXBContext.newInstance("org.openrosa.xforms.xformslist");
			Unmarshaller u = jc.createUnmarshaller();
			// Read the file into the project

			Object root = u.unmarshal(reader);
			ArrayList<Xform> lst = new ArrayList<Xform>();

			if (root instanceof Xforms) {

				lst.addAll(((Xforms) root).getXforms());

				// log.debug("Total of "+lst.size()+ " ICMS records read ");
				return lst;

			}

		} catch (Exception e) {

			if (logErrors) {
				log.error(e.getLocalizedMessage());
			} else {
				throw e;
			}

			return null;
		}

		return null;
	}
	
	public static void showDialog(Component parent)
	{
		JDialog dialog= new JDialog();
		ODKManagerPanel panel = new ODKManagerPanel();
		
		if(panel.populateDefinitionsList())
		{
			dialog.setTitle("Tellervo ODK Form Manager");
			dialog.setIconImage(Builder.getApplicationIcon());
			dialog.getContentPane().setLayout(new BorderLayout());
			dialog.getContentPane().add(panel, BorderLayout.CENTER);
			dialog.setModal(true);
			dialog.pack();
			dialog.setLocationRelativeTo(parent);
			
			
			//panel.populateInstancesList();
			
			
			dialog.setVisible(true);
		}		
	}

	private static class MyErrorHandler extends DefaultHandler {

		private HashMap<Integer, String> errors = new HashMap<Integer, String>();
		private HashSet<Integer> lineerrs = new HashSet<Integer>();
		public boolean logErrors = true;

		public void warning(SAXParseException e) throws SAXException {
			if (logErrors) {
				log.warn(printInfo(e));
			} else {
				throw e;
			}
		}

		public void error(SAXParseException e) throws SAXException {
			if (logErrors) {
				log.error(printInfo(e));
			} else {
				throw e;
			}
		}

		public void fatalError(SAXParseException e) throws SAXException {
			if (logErrors) {
				log.error(printInfo(e));
			} else {
				throw e;
			}
		}

		private String printInfo(SAXParseException e) {
			String msg = "\n";
			msg += "   Line number  : " + e.getLineNumber() + "\n";
			msg += "   Column number: " + e.getColumnNumber() + "\n";
			msg += "   Message      : " + e.getMessage() + "\n";

			errors.put(e.getLineNumber(), e.getMessage());
			lineerrs.add(e.getLineNumber());
			return msg;
		}

		public HashMap<Integer, String> getErrors() {
			return errors;
		}

		public HashSet<Integer> getLineErrors() {
			return lineerrs;
		}
	}


	
	class XformRenderer implements ListCellRenderer {

		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			JLabel renderer = (JLabel) defaultRenderer
					.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);
			
			if(value instanceof Xform)
			{
				 renderer.setText(((Xform) value).getName());
			}
			
	
			return renderer;
		}
	}

	class ODKParserListModel extends AbstractListModel{
		
		ArrayList<ODKParser> items = new ArrayList<ODKParser>();
		
		public ODKParserListModel()
		{
			
		}
		
		public void addItem(ODKParser parser)
		{
			items.add(parser);
			fireContentsChanged(this, 0, items.size());
		}
		
		
		@Override
		public Object getElementAt(int index) {
			return items.get(index);
		}
		
		public void remove(int index)
		{
			items.remove(index);
			this.fireIntervalRemoved(this, 0, items.size());
		}
		
		public void removeMultiple(int[] indexes)
		{
			ArrayList<Integer> ind = new ArrayList<Integer>();
			for(int i : indexes)
			{
				ind.add(i);
			}

			Collections.sort(ind, Collections.reverseOrder());
			for (int i : ind)
			    this.remove(i);
			
			
		}
		
		public void removeMultiple(ArrayList<Integer> ind)
		{
			Collections.sort(ind, Collections.reverseOrder());
			for (int i : ind)
			    this.remove(i);	
		}

		@Override
		public int getSize() {
			
			return items.size();
		}
		
		public void sort(){
		    Collections.sort(items);
		    fireContentsChanged(this, 0, items.size());
		}
		
	}
	

	class XformListModel extends AbstractListModel{
		
		ArrayList<Xform> items = new ArrayList<Xform>();
		
		public XformListModel()
		{
			
		}
		
		public void addItem(Xform form)
		{
			items.add(form);
			fireContentsChanged(this, 0, items.size());
		}
		
		@Override
		public Object getElementAt(int index) {
			return items.get(index);
		}
		
		public void remove(int index)
		{
			items.remove(index);
			this.fireIntervalRemoved(this, 0, items.size());
		}
		
		public void removeMultiple(int[] indexes)
		{
			ArrayList<Integer> ind = new ArrayList<Integer>();
			for(int i : indexes)
			{
				ind.add(i);
			}

			Collections.sort(ind, Collections.reverseOrder());
			for (int i : ind)
			    this.remove(i);
			
			
		}
		
		public void removeMultiple(ArrayList<Integer> ind)
		{
			Collections.sort(ind, Collections.reverseOrder());
			for (int i : ind)
			    this.remove(i);	
		}

		@Override
		public int getSize() {
			
			return items.size();
		}
		
		/*public void sort(){
		    Collections.sort(items);
		    fireContentsChanged(this, 0, items.size());
		}*/
		
	}
	
	class ODKParserRenderer implements ListCellRenderer {

		protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			
			JLabel renderer = (JLabel) defaultRenderer
					.getListCellRendererComponent(list, value, index,
							isSelected, cellHasFocus);
			
			if(value instanceof ODKParser)
			{
				 renderer.setText(((ODKParser)value).getFieldValueAsString("instanceName"));
			}
			
	
			return renderer;
		}
	}


	@Override
	public void actionPerformed(ActionEvent evt) {

		if(evt.getActionCommand().contentEquals("deleteInstance"))
		{
			int r = JOptionPane.showConfirmDialog(App.mainWindow, 
					"Are you sure you want to delete this ODK form data from the server?",
					"Are you sure", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(r!=JOptionPane.YES_OPTION) return;
			
			ODKParser parser = (ODKParser) lstInstances.getSelectedValue();
			
			String formid = parser.getFormID();
			UUID uuid = UUID.fromString(formid);
			
			if(deleteFormInstance(uuid))
			{
				((ODKParserListModel)lstInstances.getModel()).remove(lstInstances.getSelectedIndex());
			}
			
			
		}
		
		if(evt.getActionCommand().contentEquals("populateInstance"))
		{
			this.populateInstancesList();
		}
		
		if(evt.getActionCommand().contentEquals("DeleteFormDefinition"))
		{
			int r = JOptionPane.showConfirmDialog(App.mainWindow, 
					"Are you sure you want to delete this ODK form definition from the server?",
					"Are you sure", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(r!=JOptionPane.YES_OPTION) return;
			
			
				int[] indices = lstDesign.getSelectedIndices();
				
				ArrayList<Integer> successfullyDeleted = new ArrayList<Integer>();
				
				for (int i : indices)
				{
					Xform selected = lstDesign.getModel().getElementAt(i);
					
					String formid = selected.getFormID();
					UUID uuid = UUID.fromString(formid);
					if(deleteFormDefinition(uuid))
					{
						successfullyDeleted.add(i);
					}

				}
					
				if(indices.length!=successfullyDeleted.size())
				{
					Alert.error("Error", "There was a problem deleting a form");
				}
				
				((XformListModel)lstDesign.getModel()).removeMultiple(successfullyDeleted);
				
				
		}
	}
	
	public boolean deleteFormInstance(UUID formid)
	{
		URI url;
		try {
			url = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/deleteSpecificFormInstance.php?id="+formid);
	
			
			  CredentialsProvider credsProvider = App.getODKCredentialsProvider();
			  
		        CloseableHttpClient httpclient = HttpClients.custom()
		                .setDefaultCredentialsProvider(credsProvider)
		                .build();
		
		            HttpGet httpget = new HttpGet(url);

		            System.out.println("Executing request " + httpget.getRequestLine());
		            CloseableHttpResponse response = httpclient.execute(httpget);
		        
		                System.out.println("----------------------------------------");
		                System.out.println(response.getStatusLine());
		                
		                if(response.getStatusLine().toString().contains("401 Unauthorized"))
		                {
		                	Alert.error("Error", "Invalid password");
		                	return false;
		                	
		                }
		                
		                
		               return true;

		} catch (UnknownHostException e) {
			log.error("The URL of the server you have specified is unknown");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contentEquals("Cancelled"))
			{
				return false;
			}
		}
		
		return false;

	}
	
	public boolean deleteFormDefinition(UUID formid)
	{
		
		
		URI url;
		try {
			url = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/deleteSpecificFormDefinition.php?id="+formid);
	
			
			  CredentialsProvider credsProvider = App.getODKCredentialsProvider();
			  
		        CloseableHttpClient httpclient = HttpClients.custom()
		                .setDefaultCredentialsProvider(credsProvider)
		                .build();
		
		            HttpGet httpget = new HttpGet(url);

		            System.out.println("Executing request " + httpget.getRequestLine());
		            CloseableHttpResponse response = httpclient.execute(httpget);
		        
		                System.out.println("----------------------------------------");
		                System.out.println(response.getStatusLine());
		                
		                if(response.getStatusLine().toString().contains("401 Unauthorized"))
		                {
		                	Alert.error("Error", "Invalid password");
		                	return false;
		                	
		                }
		                
		                
		               return true;

		} catch (UnknownHostException e) {
			log.error("The URL of the server you have specified is unknown");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(e.getMessage().contentEquals("Cancelled"))
			{
				return false;
			}
		}
		
		return false;
		
		
	}

	
	public boolean populateInstancesList()
	{
		Path instanceFolder = null;
		
		// Doing remote server download of ODK files
		try {
			
			// Request a zip file of ODK files from the server ensuring the temp file is deleted on exit
			URI uri;
			uri = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/fetchInstances.php");
			String file = PopulateFromODKCommand.getRemoteODKFiles(uri);
			
			if(file==null) {
				// Download was cancelled
				return false;
			}
			
			new File(file).deleteOnExit();
			
			// Unzip to a temporary folder, again ensuring it is deleted on exit 
			instanceFolder = Files.createTempDirectory("odk-unzip");
			instanceFolder.toFile().deleteOnExit();
			
			log.debug("Attempting to open zip file: '"+file+"'");
			
			ZipFile zipFile =null;

		    try {
		    	zipFile= new ZipFile(file);
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
		            org.apache.commons.io.IOUtils.copy(in, out);
		            org.apache.commons.io.IOUtils.closeQuietly(in);
		            out.close();
		        }
		      }
		    } finally {
		    	
		    	if(zipFile!=null) zipFile.close();
		    }
		} catch (ZipException e)
		{
			log.debug(e.getLocalizedMessage());
			return false;
		}
		  catch (URISyntaxException e) {
			e.printStackTrace();
			Alert.error("Error", "Error downloading ODK data from server.  Please contact the developers");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			Alert.error("Error", "Error downloading ODK data from server.  Please contact the developers");
			return false;
		}

		// Check the instance folder specified exists
		File folder = instanceFolder.toFile();
		int filesFound = 0;
		ArrayList<ODKParser> filesProcessed = new ArrayList<ODKParser>();
		ArrayList<ODKParser> filesFailed = new ArrayList<ODKParser>();
		
		if(!folder.exists()) {
		
			Alert.error("Error", "Error accessing ODK data.  Please contact the developers");
			log.error("Instances folder does not exist");
			return false;
		}

		
		// Compile a hash set of all media files in the instance folder and subfolders
		File file = null;
		SuffixFileFilter fileFilter = new SuffixFileFilter(".xml");
		Iterator<File> iterator = FileUtils.iterateFiles(folder, fileFilter, TrueFileFilter.INSTANCE);
		ODKParserListModel model = new ODKParserListModel();
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
				else if (parser.getFieldValueAsString("instanceName")!=null)
				{
					model.addItem(parser);
   			
	    			
					continue;
				}

			} catch (Exception e) {
				e.printStackTrace();		
			}    

		}
		
		model.sort();
		lstInstances.setModel(model);
		
		return true;
	}
	

}
