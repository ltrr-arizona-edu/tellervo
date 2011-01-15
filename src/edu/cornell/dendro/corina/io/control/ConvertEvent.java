/**
 * Created at Jan 15, 2011, 5:52:10 AM
 */
package edu.cornell.dendro.corina.io.control;

import javax.swing.JFrame;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.io.model.ConvertModel;

/**
 * @author Daniel
 *
 */
public class ConvertEvent extends MVCEvent {
	
	public final String outputFormat;
	public final String namingConvention;
	public final ConvertModel model;
	public final JFrame modal;
	
	/**
	 * @param argKey
	 */
	public ConvertEvent(String argOutputFormat, String argNamingConvention, ConvertModel argModel, JFrame argModal) {
		super(IOController.CONVERT_PROJECTS);
		outputFormat =  argOutputFormat;
		model = argModel;
		namingConvention = argNamingConvention;
		modal = argModal;
	}
}
