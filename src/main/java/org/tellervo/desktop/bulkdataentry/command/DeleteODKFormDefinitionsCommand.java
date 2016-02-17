package org.tellervo.desktop.bulkdataentry.command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.model.BulkImportModel;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.gui.BugDialog;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.util.BugReport;
import org.tellervo.desktop.versioning.Build;
import org.tellervo.desktop.wsi.WebJaxbAccessor;
import org.tellervo.desktop.wsi.util.WSCookieStoreHandler;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

public class DeleteODKFormDefinitionsCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(DeleteODKFormDefinitionsCommand.class);

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
		
		int r = JOptionPane.showConfirmDialog(BulkImportModel.getInstance().getMainView(), 
				"Are you sure you want to delete all your ODK form definitions from the server?",
				"Are you sure", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(r!=JOptionPane.YES_OPTION) return;
		

		URI url;
		try {
			url = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/deleteFormDefinitions.php");
	

			DefaultHttpClient httpclient = new DefaultHttpClient();

	        ResponseHandler<String> responseHandler = new BasicResponseHandler();

	        httpclient.getCredentialsProvider().setCredentials(
	                new AuthScope(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!"), 80), 
	                new UsernamePasswordCredentials(App.currentUser.getUsername(), App.currentUser.getOdkPassword()));   

	        HttpGet httpget = new HttpGet(url);
	         System.out.println("executing request" + httpget.getRequestLine());

	        String response = httpclient.execute(httpget, responseHandler);
	        System.out.println("Response :: " + response);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
