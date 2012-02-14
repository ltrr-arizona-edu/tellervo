/**
 * Created at Jan 24, 2011, 4:32:28 PM
 */
package org.tellervo.desktop.io.control;

import org.tellervo.desktop.sample.Sample;

import com.dmurph.mvc.MVCEvent;

/**
 * @author Daniel
 *
 */
public class OpenExportEvent extends MVCEvent {
	private static final long serialVersionUID = 1L;
	
	public final Sample sample;
	/**
	 * Open the export window, choose elements from dbbrowser
	 * @param argKey
	 */
	public OpenExportEvent() {
		super(IOController.OPEN_EXPORT_WINDOW);
		sample = null;
	}
	
	/**
	 * Open the export window with the given sample
	 * @param argSample
	 */
	public OpenExportEvent(Sample argSample) {
		super(IOController.OPEN_EXPORT_WINDOW);
		sample = argSample;
	}

}
