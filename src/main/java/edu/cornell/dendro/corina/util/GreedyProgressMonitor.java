/*******************************************************************************
 * Copyright (C) 2010 Lucas Madar and Peter Brewer
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *     Lucas Madar
 *     Peter Brewer
 ******************************************************************************/
package edu.cornell.dendro.corina.util;

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
