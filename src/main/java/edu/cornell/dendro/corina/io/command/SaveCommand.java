/**
 * Created on Feb 1, 2011, 4:37:00 PM
 */
package edu.cornell.dendro.corina.io.command;

import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tridas.io.IDendroFile;

import com.dmurph.mvc.IllegalThreadException;
import com.dmurph.mvc.IncorrectThreadException;
import com.dmurph.mvc.MVC;
import com.dmurph.mvc.MVCEvent;
import com.dmurph.mvc.control.ICommand;

import edu.cornell.dendro.corina.components.popup.OverwritePopup;
import edu.cornell.dendro.corina.components.popup.OverwritePopupModel;
import edu.cornell.dendro.corina.components.popup.ProgressPopup;
import edu.cornell.dendro.corina.components.popup.ProgressPopupModel;
import edu.cornell.dendro.corina.io.control.SaveEvent;
import edu.cornell.dendro.corina.io.model.ConvertModel.WriterObject;
import edu.cornell.dendro.corina.ui.I18n;

/**
 * @author Daniel Murphy
 *
 */
public class SaveCommand implements ICommand {
	private static final Logger log = LoggerFactory.getLogger(SaveCommand.class);
	/**
	 * @see com.dmurph.mvc.control.ICommand#execute(com.dmurph.mvc.MVCEvent)
	 */
	@Override
	public void execute(MVCEvent argEvent) {
		SaveEvent event = (SaveEvent) argEvent;
		
		try {
			MVC.splitOff();
		} catch (IllegalThreadException e1) {
			e1.printStackTrace();
		} catch (IncorrectThreadException e1) {
			e1.printStackTrace();
		}

		ProgressPopup storedConvertProgress = null;
		try{
			
			ProgressPopupModel pmodel = new ProgressPopupModel();
			pmodel.setCancelString(I18n.getText("io.convert.cancel"));

			int totalFiles = 0;
			for(WriterObject o : event.writers){
				if(o.writer == null){
					continue;
				}
				totalFiles ++;
			}
			if(totalFiles == 0){
				// TODO common alert window
				JOptionPane.showMessageDialog(event.modal, I18n.getText("control.convert.noFiles"), "", JOptionPane.PLAIN_MESSAGE);
				return;
			}
			
			int currFile = 0;
			final ProgressPopup savingProgress = new ProgressPopup(event.modal, true, pmodel);
			storedConvertProgress = savingProgress;
			// i have to do this in a different thread
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					savingProgress.setVisible(true);
				}
			});
			int slept = 0;
			while(!savingProgress.isVisible()){
				try {
					Thread.sleep(50); // for bug 213
				} catch (InterruptedException e) {}
				slept+=50;
				if(slept >= 5000){
					log.error("Slept for 5 seconds but saving progress window is still not open. breaking");
					savingProgress.setVisible(false);
					break;
				}
			}
			
			int response = -1;
			boolean all = false;
			for (int i = 0; i < event.writers.length; i++) {
				if(pmodel.isCanceled()){
					break;
				}
				WriterObject p = event.writers[i];
				if (p.writer != null) {
					
					String outputFolder = event.folder.getAbsolutePath();
					// custom implementation of saveAllToDisk, as we need to
					// keep track of each dendro file for the progress window
					
					if (!outputFolder.endsWith(File.separator) && !outputFolder.equals("")) {
						outputFolder += File.separator;
					}
					
					IDendroFile[] files = p.writer.getFiles();
					for (int j=0; j<files.length; j++) {
						IDendroFile dof = files[j];
						if(pmodel.isCanceled()){
							break;
						}
						currFile++;
						String filename = p.writer.getNamingConvention().getFilename(dof);
						
						pmodel.setStatusString(I18n.getText("io.convert.savingStatus",	filename + "." + dof.getExtension()));
						
						// check to see if it exists:
						File file = new File(outputFolder+File.separator+filename + "." + dof.getExtension());
						if(file.exists()){
							if(response == -1){
								OverwritePopupModel om = new OverwritePopupModel();
								om.setApplyToAll(false);
								om.setResponse(OverwritePopupModel.RENAME);
								om.setMessage(I18n.getText("io.convert.overwrite", filename, filename+"(1)"));
								OverwritePopup op = new OverwritePopup(event.modal, om, true);
								// this should hang until the window is closed
								op.setVisible(true);
								
								response = om.getResponse();
								all = om.isApplyToAll();
							}
							
							switch(response){
								case OverwritePopupModel.IGNORE:
									response = -1;
									continue;
								case OverwritePopupModel.OVERWRITE:
									p.writer.saveFileToDisk(outputFolder, dof);
									break;
								case OverwritePopupModel.RENAME:
									p.writer.getNamingConvention().setFilename(dof, filename+"(1)");
									j--;
									currFile--;
									if(!all){
										response = -1;
									}
									continue;
							}
							if(!all){
								response = -1;
							}
						}
						p.writer.saveFileToDisk(outputFolder, dof);
						pmodel.setPercent(currFile * 100 / totalFiles);
					}
				}
			}
		}catch (Exception e) {
			log.error("Error while saving",e);
		}finally{
			if(storedConvertProgress != null){
				storedConvertProgress.setVisible(false);
			}
		}
	}
}
