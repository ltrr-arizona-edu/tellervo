//
// This file is part of Corina.
// 
// Corina is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
// 
// Corina is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with Corina; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// Copyright 2003 Ken Harris <kbh7@cornell.edu>
//

package corina.prefs;

import java.util.EventObject;

/**
    An event indicating a user preference was changed.
    
    <h2>Left to do:</h2>
    <ul>
        <li>rename to PrefChangedEvent?
    </ul>

    @see corina.prefs.PrefsListener

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class PrefsEvent extends EventObject {

    private String pref;

    /**
        Make a new PrefsEvent.
        
        @param source the object which fired this event
        @param pref the key for the preference which was changed
    */
    public PrefsEvent(Object source, String pref) {
	super(source);
	this.pref = pref;
    }

    /**
        Get the key for the preference which was changed.
        
        @return the key of the pref which was changed
    */
    public String getPref() {
	return pref;
    }
}
