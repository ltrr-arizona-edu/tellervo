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
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import org.apache.http.Header;
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

public class ODKFileDownloadProgress extends JDialog implements ActionListener, PropertyChangeListener  {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(ODKFileDownloadProgress.class);
	private JProgressBar progressBar;
	private final JPanel contentPanel = new JPanel();
	private DownloadTask task;
	private final URI url;
	private File filePath;
	private int byteCount = 0;
	
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
		contentPanel.setLayout(new MigLayout("", "[148px,grow,fill]", "[14px]"));
		{
			progressBar = new JProgressBar(0, 100);
			progressBar.setValue(0);
        	//progressBar.setIndeterminate(true);
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
		this.setLocationRelativeTo(parent);
		task = new DownloadTask();
        task.addPropertyChangeListener(this);
        task.execute();
	}
	


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
       
		log.debug("Property change of type: "+evt.getPropertyName());
		
		if ("progress".equals(evt.getPropertyName())) {
			log.debug("Property change - was progress update");
            int progress = (Integer) evt.getNewValue();
            log.debug("Progres : "+progress);
            
            //progressBar.setIndeterminate(false);
            //progressBar.setStringPainted(true);
            progressBar.setValue(progress);
  
        } 
		
	}


	public File getFile()
	{
		return this.filePath;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancel"))
		{
			filePath = null;
			this.dispose();
		}
		
	}
	
    class DownloadTask extends SwingWorker<Void, Void> {
        /*
         * Main task. Executed in background thread.
         */
        @Override
        public Void doInBackground() {
           
    		log.debug("doInBackground called");

        	/*HttpClient client = new ContentEncodingHttpClient();
    		HttpUriRequest req;
    		HttpGet httpget = new HttpGet(url);
    		Document outDocument = null;
    		HttpResponse response;
    		
    		
    		try {

    			req = new HttpGet(url);
    		
    			// load cookies
    			((AbstractHttpClient) client).setCookieStore(WSCookieStoreHandler.getCookieStore().toCookieStore());
    			
    			String clientModuleVersion = "1.0";
    			req.setHeader("User-Agent", "Tellervo WSI " + Build.getUTF8Version() + 
    					" (" + clientModuleVersion  + "; ts " + Build.getCompleteVersionNumber() +")");
    			
    			
    			if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_USE_STRICT_SECURITY, false))
    			{
    				// Using strict security so don't allow self signed certificates for SSL
    			}
    			else
    			{
    				// Not using strict security so allow self signed certificates for SSL
    				if(url.getScheme().equals("https")) 
    				WebJaxbAccessor.setSelfSignableHTTPSScheme(client);
    			}
    			
    			
    			response = client.execute(httpget);
    			Header[] headers = response.getAllHeaders();
    			
    			log.debug("-----HEADERS-----");
    			for(Header header : headers)
    			{
    				log.debug(header.getName()+ " : "+header.getValue());
    			}
    			log.debug("-----END-----");
    			
    			HttpEntity entity = response.getEntity();
    			long fileSize = entity.getContentLength();
    			log.debug("File download size: "+fileSize);
    			long byteCount=0;
    			
    		    if (entity != null) {	      
    		    	
    		    	BufferedInputStream bis = null;
    		    	BufferedOutputStream bos = null;
    		        try {
    					bis = new BufferedInputStream(entity.getContent());
    					
    					bos = new BufferedOutputStream(new FileOutputStream(filePath));
    					int inByte;
    					
    					while((inByte = bis.read()) != -1) {
    						bos.write(inByte);
    						byteCount++;
    						
    						int percComplete = (int) (byteCount/fileSize)*100;
    						
    						if(percComplete<=100) {
    							setProgress(percComplete);
    						} else {
    							log.error("Perc complete out of range");
    						}
    					}
    		        } finally {
    		        	
    		        	log.debug("Final byte count was "+byteCount);
    		            bis.close();
    		            bos.close();
    		        }
    		    }
    			
    			
    			
    			
    		} catch (UnknownHostException e)
    		{
    			log.debug("The URL of the server you have specified is unknown");
    			return null;
    		}
    		
    		catch (HttpResponseException hre) {
    			
    			if(hre.getStatusCode()==404)
    			{
    				log.debug("The URL of the server you have specified is unknown");
    				return null;
    			}
    			
    			
    			BugReport bugs = new BugReport(hre);
    			
    			bugs.addDocument("sent.xml", outDocument);
    			
    			//new BugDialog(bugs);

    			log.debug("The server returned a protocol error " + hre.getStatusCode() + 
    					": " + hre.getLocalizedMessage());
    			return null;
    			
    		} catch (IllegalStateException ex)
    		{
    			log.debug("Webservice URL must be a full URL qualified with a communications protocol.\n" +
    				"Tellervo currently supports http:// and https://.");	
    		} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		finally {
    		
    		}

    		return null;*/
    		
    		
    		HttpClient client = new ContentEncodingHttpClient();
    		HttpUriRequest req;
    		HttpGet httpget = new HttpGet(url);
    		Document outDocument = null;
    		HttpResponse response;
    		setProgress(0);
    		
    		try {

    			req = new HttpGet(url);
    		
    			// load cookies
    			((AbstractHttpClient) client).setCookieStore(WSCookieStoreHandler.getCookieStore().toCookieStore());
    			
    			String clientModuleVersion = "1.0";
    			req.setHeader("User-Agent", "Tellervo WSI " + Build.getUTF8Version() + 
    					" (" + clientModuleVersion  + "; ts " + Build.getCompleteVersionNumber() +")");
    			
    			
    			if(App.prefs.getBooleanPref(PrefKey.WEBSERVICE_USE_STRICT_SECURITY, false))
    			{
    				// Using strict security so don't allow self signed certificates for SSL
    			}
    			else
    			{
    				// Not using strict security so allow self signed certificates for SSL
    				if(url.getScheme().equals("https")) 
    				WebJaxbAccessor.setSelfSignableHTTPSScheme(client);
    			}
    			
    			
    			response = client.execute(httpget);
    			Header[] headers = response.getAllHeaders();
    			
    			log.debug("-----HEADERS-----");
    			for(Header header : headers)
    			{
    				log.debug(header.getName()+ " : "+header.getValue());
    			}
    			log.debug("-----END-----");
    			
    			HttpEntity entity = response.getEntity();
    			log.debug("File download size: "+entity.getContentLength());
    			long fileSize = entity.getContentLength();
    			
    		    if (entity != null) {	      
    		    	
    		    	BufferedInputStream bis = null;
    		    	BufferedOutputStream bos = null;
    		    	
    		        try {
    					bis = new BufferedInputStream(entity.getContent());
    					
    					bos = new BufferedOutputStream(new FileOutputStream(filePath));
    					int inByte;
    					
    					while((inByte = bis.read()) != -1) {
    						bos.write(inByte);
    						byteCount++;
    						
    						if(fileSize<0){
    							continue;
    						}
    						
    						int mod = byteCount%1000;
    						if(mod ==0)
    						{
	    						int percComplete = (int) (byteCount/fileSize)*100;
	    						setProgress(Math.min(percComplete, 100));
    						}
    						
    					}
    		        } finally {
    		        	
    		        	log.debug("Final byte count = "+byteCount);
    		            bis.close();
    		            bos.close();
    		        }
    		    }
    			log.debug("File download size: "+entity.getContentLength());
    			
    			
    			
    		} catch (UnknownHostException e)
    		{
    			log.error("The URL of the server you have specified is unknown");
    			return null;
    		}
    		
    		catch (HttpResponseException hre) {
    			
    			if(hre.getStatusCode()==404)
    			{
    				log.error("The URL of the server you have specified is unknown");
    				return null;
    			}
    			
    			
    			BugReport bugs = new BugReport(hre);
    			
    			bugs.addDocument("sent.xml", outDocument);
    			
    			new BugDialog(bugs);

    			log.error("The server returned a protocol error " + hre.getStatusCode() + 
    					": " + hre.getLocalizedMessage());
    			return null;
    		} catch (IllegalStateException ex)
    		{
    			log.error("Webservice URL must be a full URL qualified with a communications protocol.\n" +
    				"Tellervo currently supports http:// and https://.");
    			return null;
    		} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
    		finally {
    		
    		}
    		
    		return null;
    		
    		
        	
        }
 
        /*
         * Executed in event dispatching thread
         */
        @Override
        public void done() {

            setCursor(null); //turn off the wait cursor
            setVisible(false);
        }
    }

}
