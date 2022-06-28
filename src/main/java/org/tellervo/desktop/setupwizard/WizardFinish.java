package org.tellervo.desktop.setupwizard;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;

import org.tellervo.desktop.gui.widgets.AbstractWizardPanel;
import org.tellervo.desktop.ui.Builder;

public class WizardFinish extends AbstractWizardPanel {

	private static final long serialVersionUID = 1L;

	
	
	public WizardFinish() {
		super("Setup complete", 
		"That is all the information that is required for you to proceed.  If you " +
		"would like to change any of these settings in the future you can do so" +
		"in the preferences dialog.\n\n" +
		"Before you start using Tellervo you may like to watch the video tutorials. "+
		"You can also access these later through the help menu or the Tellervo website."
		);
		
		JButton btnWatchTutorialVideos = new JButton("Watch tutorial videos now");
		btnWatchTutorialVideos.setIcon(Builder.getIcon("video.png", 48));
		
		btnWatchTutorialVideos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Desktop desktop = Desktop.getDesktop();
				try {
					URI uri = new URI("http://www.tellervo.org/tutorials/"); 
					desktop.browse(uri);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
			}
		});
		
		add(btnWatchTutorialVideos);
	}


}
