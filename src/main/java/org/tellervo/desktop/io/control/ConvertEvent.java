/**
 * Created at Jan 15, 2011, 5:52:10 AM
 */
package org.tellervo.desktop.io.control;

import javax.swing.JFrame;

import org.tellervo.desktop.io.model.ConvertModel;
import org.tridas.io.naming.INamingConvention;

import com.dmurph.mvc.MVCEvent;


/**
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class ConvertEvent extends MVCEvent {
	
	public final String outputFormat;
	public final INamingConvention namingConvention;
	public final ConvertModel model;
	public final JFrame modal;
	
	/**
	 * @param argKey
	 */
	public ConvertEvent(String argOutputFormat, INamingConvention argNamingConvention, ConvertModel argModel, JFrame argModal) {
		super(IOController.CONVERT_PROJECTS);
		outputFormat =  argOutputFormat;
		model = argModel;
		namingConvention = argNamingConvention;
		modal = argModal;
	}
}
