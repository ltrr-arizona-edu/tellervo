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

/**
   An implementation of SampleListener, with all method bodies empty.

   @see SampleListener

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/

public class SampleAdapter implements SampleListener {
    public void sampleRedated(SampleEvent e) { }
    public void sampleDataChanged(SampleEvent e) { }
    public void sampleMetadataChanged(SampleEvent e) { }
    public void sampleElementsChanged(SampleEvent e) { }
	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) { }
	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
    

    // 
    // -> add other types of events here
    // 
}
