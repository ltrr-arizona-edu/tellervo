/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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
 *     Peter Brewer
 ******************************************************************************/

package org.tellervo.desktop.sample;

import java.util.EventListener;

/**
   An interface for views-of-a-samples to implement.  When a Sample is
   modified, one (or more) of these methods will be called, so the
   view can update itself.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public interface SampleListener extends EventListener {
    /**
       The starting/ending dates of this sample have changed.

       @param e the Event object
    */
    public void sampleRedated(SampleEvent e);

    /**
       One or more of the numbers of the dataset have changed.

       @param e the Event object
    */
    public void sampleDataChanged(SampleEvent e);

    /**
       One or more of the metadata fields has been changed (the
       element changed, or a key was added or removed).

       @param e the Event object
    */
    public void sampleMetadataChanged(SampleEvent e);
    // TODO: indicate which, if just one/some?

    /**
       The elements were changed.

       @param e the Event object
    */
    public void sampleElementsChanged(SampleEvent e);

    /**
     * The display units were changed
     * 
     * @param e the Event object
     */
    public void sampleDisplayUnitsChanged(SampleEvent e);
    
    
    
    public  void sampleDisplayCalendarChanged(SampleEvent e);
    
    
    public void measurementVariableChanged(SampleEvent e);
    // 
    // -> add other types of events here
    // 
}
