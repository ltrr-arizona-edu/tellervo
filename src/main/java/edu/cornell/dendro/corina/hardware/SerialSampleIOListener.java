/**
 * 
 */
package edu.cornell.dendro.corina.hardware;

import java.util.EventListener;

/**
 * @author Lucas Madar
 *
 */
public interface SerialSampleIOListener extends EventListener {
	
	// triggered when a serial sample is sent...
	public void SerialSampleIONotify(SerialSampleIOEvent e);

}
