package org.tellervo.desktop.bulkdataentry.command;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.bulkdataentry.control.DeleteSpecificODKDefinitionEvent;
import org.tellervo.desktop.core.App;
import org.tellervo.desktop.prefs.Prefs.PrefKey;
import org.tellervo.desktop.ui.Alert;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

public class DeleteSpecificODKFormDefinitionCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(DeleteSpecificODKFormDefinitionCommand.class);

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
		
		int r = JOptionPane.showConfirmDialog(App.mainWindow, 
				"Are you sure you want to delete this ODK form definition from the server?",
				"Are you sure", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(r!=JOptionPane.YES_OPTION) return;
		

		DeleteSpecificODKDefinitionEvent ev = (DeleteSpecificODKDefinitionEvent) argEvent;
				
		URI url;
		try {
			url = new URI(App.prefs.getPref(PrefKey.WEBSERVICE_URL, "invalid url!")+"/"+"odk/deleteSpecificFormDefinition.php?id="+ev.getValue());
	
			
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
		                	return;
		                	
		                }
		                
		                
		                Alert.message("Success", "Form deleted");

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
				return;
			}
		}
		
		

	}

}
