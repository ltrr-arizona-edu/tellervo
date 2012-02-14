/**
 * Created at Jan 15, 2011, 5:52:10 AM
 */
package edu.cornell.dendro.corina.io.control;

import javax.swing.JFrame;

import org.tridas.io.naming.INamingConvention;

import com.dmurph.mvc.MVCEvent;

import edu.cornell.dendro.corina.io.model.ConvertModel;

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
