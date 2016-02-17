package org.tellervo.desktop.bulkdataentry.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.BugReport;
import org.tellervo.desktop.versioning.Build;
import org.tellervo.desktop.wsi.WebJaxbAccessor;
import org.tellervo.desktop.wsi.util.WSCookieStoreHandler;

public class ODKFileDownloadProgress extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory
			.getLogger(ODKFileDownloadProgress.class);
	private JProgressBar progressBar;
	private final JPanel contentPanel = new JPanel();
	private DownloadTask task;
	private final URI url;
	private File filePath;
	private int kbCount = 0;
	private JLabel lblDownloading;
	private int fileSize;

	/**
	 * Create the dialog.
	 */
	public ODKFileDownloadProgress(Window parent, URI url) {

		log.debug("Starting to download file");
		setModal(true);
		this.setIconImage(Builder.getApplicationIcon());
		this.url = url;
		try {
			filePath = File.createTempFile("odk-", ".zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		setTitle("Downloading ODK Files");
		setBounds(100, 100, 444, 119);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[148px,grow,fill]",
				"[14px][]"));
		{
			progressBar = new JProgressBar();
			progressBar.setValue(0);

			progressBar.setIndeterminate(false);
			progressBar.setStringPainted(true);
			contentPanel.add(progressBar, "cell 0 0,alignx left,aligny top");
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		{
			lblDownloading = new JLabel("Connecting...");
			contentPanel.add(lblDownloading, "cell 0 1");
		}

		this.setLocationRelativeTo(parent);
		
		HttpEntity entity = getEntity();
		if (entity != null) {
			task = new DownloadTask(progressBar, lblDownloading, entity);
			task.execute();
		}
		
		this.setVisible(true);
	}



	public File getFile() {
		return this.filePath;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Cancel")) {
			filePath = null;
			this.dispose();
		}

	}

	private HttpEntity getEntity() {
		HttpClient client = new ContentEncodingHttpClient();
		HttpUriRequest req;
		HttpGet httpget = new HttpGet(url);
		Document outDocument = null;
		HttpResponse response;

		try {

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

			HttpEntity entity = response.getEntity();

			return entity;
			// log.debug("File download size: "+entity.getContentLength());

		} catch (UnknownHostException e) {
			log.error("The URL of the server you have specified is unknown");
			return null;
		}

		catch (HttpResponseException hre) {

			if (hre.getStatusCode() == 404) {
				log.error("The URL of the server you have specified is unknown");
				return null;
			}

			BugReport bugs = new BugReport(hre);

			bugs.addDocument("sent.xml", outDocument);

			new BugDialog(bugs);

			log.error("The server returned a protocol error "
					+ hre.getStatusCode() + ": " + hre.getLocalizedMessage());
			return null;
		} catch (IllegalStateException ex) {
			log.error("Webservice URL must be a full URL qualified with a communications protocol.\n"
					+ "Tellervo currently supports http:// and https://.");
			return null;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {

		}

	}

	class DownloadTask extends SwingWorker<Void, Integer> {

		JProgressBar jpb;
		JLabel label;
		HttpEntity entity;
		long longfileSize;
		
		public DownloadTask(JProgressBar jpb, JLabel label, HttpEntity entity) {
			this.jpb = jpb;
			this.label = label;
			this.entity = entity;
			longfileSize = entity.getContentLength();
			jpb.setMaximum((int) (longfileSize/1000));
		}

		@Override
		protected void process(List<Integer> chunks) {
			int i = chunks.get(chunks.size() - 1);
			jpb.setValue(i); // The last value in this array is all we care
								// about.
			System.out.println(i);
			label.setText("Downloading: " + i + " of "+(longfileSize/1000)+"kB");

		}

		/*
		 * Main task. Executed in background thread.
		 */
		@Override
		public Void doInBackground() {

			log.debug("doInBackground called");
			long byteCount = 0;
			if (entity != null) {

				BufferedInputStream bis = null;
				BufferedOutputStream bos = null;

				try {
					bis = new BufferedInputStream(entity.getContent());

					bos = new BufferedOutputStream(new FileOutputStream(
							filePath));
					int inByte;

					while ((inByte = bis.read()) != -1) {
						bos.write(inByte);
						byteCount++;

						if (fileSize < 0) {
							continue;
						}

					
						publish((int) (byteCount/1000));

					}
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {

					// log.debug("Final byte count = "+byteCount);
					try {
						bis.close();
						bos.close();

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			// log.debug("File download size: "+entity.getContentLength());

			return null;

		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {

			setCursor(null); // turn off the wait cursor
			setVisible(false);
		}
	}

}
