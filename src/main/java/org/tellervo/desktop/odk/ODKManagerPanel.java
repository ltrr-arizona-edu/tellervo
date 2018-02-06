package org.tellervo.desktop.odk;

import java.awt.BorderLayout;
import java.awt.Component;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import net.miginfocom.swing.MigLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.jdom.Document;
import org.openrosa.xforms.xformslist.Xform;
import org.openrosa.xforms.xformslist.Xforms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.versioning.Build;
import org.tellervo.desktop.wsi.WebJaxbAccessor;
import org.tellervo.desktop.wsi.util.WSCookieStoreHandler;
import org.tridas.io.I18n;
import org.tridas.io.TridasIO;
import org.tridas.io.util.FileHelper;
import org.tridas.io.util.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import edu.emory.mathcs.backport.java.util.Collections;

public class ODKManagerPanel extends JPanel {
	private JList<Xform> lstDesign;
	protected final static Logger log = LoggerFactory
			.getLogger(ODKManagerPanel.class);

	/**
	 * Create the panel.
	 */
	public ODKManagerPanel() {
		setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane);

		JPanel panelFormDesign = new JPanel();
		tabbedPane.addTab("Form Designs", null, panelFormDesign, null);
		panelFormDesign.setLayout(new BorderLayout(0, 0));

		lstDesign = new JList<Xform>();
		populateList();
		lstDesign.setCellRenderer(new XformRenderer());

		panelFormDesign.add(lstDesign);

		JPanel panelButtons = new JPanel();
		panelFormDesign.add(panelButtons, BorderLayout.SOUTH);
		panelButtons.setLayout(new MigLayout("", "[grow][]", "[]"));

		JButton btnDeleteDesign = new JButton("Delete Selected");
		panelButtons.add(btnDeleteDesign, "cell 1 0");

		JPanel panelFormInstances = new JPanel();
		tabbedPane.addTab("Form Data", null, panelFormInstances, null);

	}

	private void populateList() {
		
		URI url;
		try {
			url = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/formList.php");

			HttpClient client = new ContentEncodingHttpClient();
			HttpUriRequest req;
			HttpGet httpget = new HttpGet(url);
			Document outDocument = null;
			HttpResponse response;


			req = new HttpGet(url);

			// load cookies
			((AbstractHttpClient) client).setCookieStore(WSCookieStoreHandler
					.getCookieStore().toCookieStore());

			String clientModuleVersion = "1.0";
			req.setHeader(
					"User-Agent",
					"Tellervo WSI " + Build.getUTF8Version() + " ("
							+ clientModuleVersion + "; ts "
							+ Build.getCompleteVersionNumber() + ")");

			if (App.prefs.getBooleanPref(
					PrefKey.WEBSERVICE_USE_STRICT_SECURITY, false)) {
				// Using strict security so don't allow self signed certificates
				// for SSL
			} else {
				// Not using strict security so allow self signed certificates
				// for SSL
				if (url.getScheme().equals("https"))
					WebJaxbAccessor.setSelfSignableHTTPSScheme(client);
			}

			response = client.execute(httpget);
		

		} catch (UnknownHostException e) {
			log.error("The URL of the server you have specified is unknown");
			return;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		URI uri;
		try {
			//uri = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!") + "/" + "odk/formList.php");
			uri = new URI("https://tellervo.ltrr.arizona.edu/dev/odk/formList.php");

			List<Xform> xforms = this
					.getXformsFromXMLFile("https://tellervo.ltrr.arizona.edu/dev/odk/formList.php", false);

			DefaultListModel model = new DefaultListModel();

			for (Xform form : xforms) {
				model.addElement(form);

			}
			this.lstDesign.setModel(model);

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public static List<Xform> getXformsFromXMLFile(String filename,
			boolean logErrors) throws Exception {
		StringBuilder fileString = new StringBuilder();
		StringReader reader;
		FileHelper fileHelper = new FileHelper();

		String[] argFileString = null;
		if (TridasIO.getReadingCharset() != null) {
			try {
				argFileString = fileHelper.loadStrings(filename,
						TridasIO.getReadingCharset());
			} catch (UnsupportedEncodingException e) {

				if (logErrors) {
					e.printStackTrace();
				} else {
					throw e;
				}
			}
		} else {
			if (TridasIO.isCharsetDetection()) {
				argFileString = fileHelper
						.loadStringsFromDetectedCharset(filename);
			} else {
				argFileString = fileHelper.loadStrings(filename);
			}
		}

		SchemaFactory factory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		URL file = IOUtils.getFileInJarURL("schemas/xformlist.xsd");
		Schema schema = null;
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

			// Build the string array into a FileReader
			Boolean firstLine = true;

			for (String s : argFileString) {
				if (firstLine) {
					fileString.append(s.replaceFirst("^[^<]*", "") + "\n");
					firstLine = false;
				} else {
					fileString.append(s + "\n");
				}
			}
			reader = new StringReader(fileString.toString());

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
		reader = new StringReader(fileString.toString());
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
	
	public static void showDialog()
	{
		JDialog dialog= new JDialog();
		ODKManagerPanel panel = new ODKManagerPanel();
		
		dialog.setLayout(new BorderLayout());
		dialog.add(panel, BorderLayout.CENTER);
		dialog.setVisible(true);
		
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

}
