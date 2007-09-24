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

package edu.cornell.dendro.corina.prefs;

import java.util.EventListener;

/**
    A listener for preference-changed events.
    
    <p>To use a PrefsListener, add it using
    
    <pre>
    Prefs.addPrefsListener(pl);
    </pre>
    
    and remove it by using
    
    <pre>
    Prefs.removePrefsListener(pl);
    </pre>

    @see corina.prefs.PrefsEvent

    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public interface PrefsListener extends EventListener {
    /**
        Called on each PrefsListener when a preference is changed.
        
        @param e the event object
    */
    public void prefChanged(PrefsEvent e);
}
