/**
 * Created on Feb 1, 2011, 7:11:18 PM
 */
package org.tellervo.desktop.io.control;

import java.awt.Frame;
import java.io.File;

import org.tellervo.desktop.io.model.ConvertModel.WriterObject;

import com.dmurph.mvc.MVCEvent;


/**
 * @author Daniel Murphy
 *
 */
public class SaveEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;
	
	public final WriterObject[] writers;
	public final File folder;
	public final Frame modal;
	
	public SaveEvent(WriterObject[] argWriters, File argFolder, Frame argModal){
		super(IOController.SAVE);
		writers = argWriters;
		folder = argFolder;
		modal = argModal;
	}
}
