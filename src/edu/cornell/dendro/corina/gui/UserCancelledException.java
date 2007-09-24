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

package edu.cornell.dendro.corina.gui;

/**
   Thrown when a user cancels an operation.

   <p>There's nothing magical about this class.  (It has no code,
   either: it's just a marker class.)  But it turned out to be really
   nice to be able to say things like:</p>

<pre>
   try {
      askUserInput();         // can throw UCE
      doComputation();

      askSomeMoreUserInput(); // can throw UCE
      doMoreComputation();

      reallyOverwrite();      // can throw UCE
      save();
   } catch (UserCancelledException uce) {
      // do nothing
   } catch (SomeOtherException soe) {
      // do error handling stuff here
   }
</pre>

   <p>(Without this class, you end up having lots of "if-else-return"
   clauses which make it much harder to follow the flow of a function.
   Besides, "user cancelled" <i>is</i> an exceptional situation.)</p>

   @see corina.gui.FileDialog
   @see corina.util.Overwrite

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class UserCancelledException extends Exception {
    /**
       Construct a new UserCancelledException.
    */
    public UserCancelledException() {
	// no code: just here for the javadoc tag
    }
}
