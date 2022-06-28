package org.tellervo.desktop.gui.menus.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;

import org.tellervo.desktop.admin.SetPasswordUI;
import org.tellervo.desktop.admin.view.PermissionByEntityDialog;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerDialog;
import org.tellervo.desktop.gui.widgets.TridasEntityPickerPanel.EntitiesAccepted;
import org.tellervo.desktop.ui.Builder;
import org.tellervo.desktop.util.labels.ui.LabelPrintingDialog;
import org.tridas.interfaces.ITridas;
import org.tridas.schema.TridasSample;

public class HelpVideoTutorialsAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	ArrayList<Map.Entry<String, URI>> links;	
	String videoname;

	
	
	public HelpVideoTutorialsAction(String videoname) {
        super(videoname, Builder.getIcon("video.png", 22));
		putValue(SHORT_DESCRIPTION, "Video Tutorial - "+videoname);
        //putValue(MNEMONIC_KEY,I18n.getMnemonic("menus.file.new")); 
        //putValue(ACCELERATOR_KEY, I18n.getKeyStroke("menus.file.new"));

		this.videoname = videoname;

		
		links = new ArrayList<Map.Entry<String, URI>>();
    	
    	try{
    	links.add(new AbstractMap.SimpleEntry<String, URI>
    			("Introduction", 
    			new URI("http://www.tellervo.org/tutorials/index.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Getting started", 
				new URI("http://www.tellervo.org/tutorials/gettingstarted.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Server installation", 
				new URI("http://www.tellervo.org/tutorials/serverinstallation.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Entering metadata", 
				new URI("http://www.tellervo.org/tutorials/bulkdataentry.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Measuring samples", 
				new URI("http://www.tellervo.org/tutorials/measuring.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("mapping", 
				new URI("http://www.tellervo.org/tutorials/mapping.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Administering users and groups", 
				new URI("http://www.tellervo.org/tutorials/usergroup.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Curating your collection", 
				new URI("http://www.tellervo.org/tutorials/curation.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Exporting data", 
				new URI("http://www.tellervo.org/tutorials/export.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Importing", 
				new URI("http://www.tellervo.org/tutorials/import.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Graphing", 
				new URI("http://www.tellervo.org/tutorials/graphing.php")));
    	links.add(new AbstractMap.SimpleEntry<String, URI>
				("Data manipulation", 
				new URI("http://www.tellervo.org/tutorials/datamanipulation.php")));

    	} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }

	
	
	
	
	@Override
	
	public void actionPerformed(ActionEvent e) {
		for(final Map.Entry<String, URI> link : links){
			
		Desktop desktop = Desktop.getDesktop();
		try {
			if((String) link.getKey() == videoname)
			desktop.browse((URI) link.getValue());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		}
		
	}
		
	}
			

