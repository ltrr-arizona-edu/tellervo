/**
 * Created at Jan 24, 2011, 4:32:28 PM
 */
package org.tellervo.desktop.io.control;

import java.util.ArrayList;
import java.util.Collection;

import org.tellervo.desktop.sample.Sample;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 *
 */
public class OpenExportEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;
	
	public final Sample sample;
	public final Collection<Sample> allSamples;
	
	
	/**
	 * Open the export window, choose elements from dbbrowser
	 * @param argKey
	 */
	public OpenExportEvent() {
		super(IOController.OPEN_EXPORT_WINDOW);
		sample = null;
		allSamples = new ArrayList<Sample>();
	}
	
	/**
	 * Open the export window with the given sample
	 * @param argSample
	 */
	public OpenExportEvent(Collection<Sample> samples, Sample argSample) {
		super(IOController.OPEN_EXPORT_WINDOW);
		sample = argSample;
		allSamples = samples;
	}

}
