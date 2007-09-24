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
// Copyright 2001 Ken Harris <kbh7@cornell.edu>
//

package edu.cornell.dendro.corina.gui;

/**
   An interface for frames that use user-settable preferences.  When
   preferences are changed, all Frames that implement this will have
   refreshFromPreferences() called.

   <h2>Left to do</h2>
   <ul>
     <li>switch to real events instead; delete this class, and its uses
   </ul>

   @deprecated Use real events instead.

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public interface HasPreferences {
    /**
       Called on every JFrame that HasPreferences when the preferences
       change.
    */
    public abstract void refreshFromPreferences();
}
