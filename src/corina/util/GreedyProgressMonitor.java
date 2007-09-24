package corina.util;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.ProgressMonitor;

/**
 * A quick and dirty interface for ProgressMonitors that allows you to wait to make sure they update.
 * 
 * MUST be run from your own thread, not the AWT event dispatch thread.
*/

public class GreedyProgressMonitor extends ProgressMonitor {
    public GreedyProgressMonitor(Component parentComponent,
            Object message,
            String note,
            int min,
            int max) {
    	super(parentComponent, message, note, min, max);
    }
	
	public void setProgressGreedy(final int value) {
		try {
			EventQueue.invokeAndWait(new Runnable() {
				public void run() {
					setProgress(value);
				}
			});
		}
		catch (Exception e) {
			// eh... who cares
		}
	}    
}