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
//
// This file is part of Corina.
// 
// Tellervo is free software; you can redistribute it and/or modify
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

package org.tellervo.desktop.util;

import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

/**
   A DocumentListener for the 99% of the time when you don't care how
   the change got there.

   <p>All of changedUpdate(), insertUpdate(), and removeUpdate()
   simply call update().  Great for anonymous classes:</p>

<pre>
   text.addDocumentListener(new DocumentListener2() {
      public void update(DocumentEvent e) {
         // only 1 method to implement, instead of 3
      }
   });
</pre>

   <p>Note: should this be more properly called "DocumentAdapter",
   since it's an abstract class, and not an interface?</p>

   @see javax.swing.event.DocumentListener

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
 */
public abstract class DocumentListener2 implements DocumentListener {
    /**
       A "document changed" update occured.  Calls update().
       @param e the document event
    */
    public void changedUpdate(DocumentEvent e) {
	update(e);
    }

    /**
       A "document insert" update occured.  Calls update().
       @param e the document event
    */
    public void insertUpdate(DocumentEvent e) {
	update(e);
    }

    /**
       A "document remove" update occured.  Calls update().
       @param e the document event
    */
    public void removeUpdate(DocumentEvent e) {
	update(e);
    }

    /**
       A document update occured.
       @param e the document event
    */
    public abstract void update(DocumentEvent e);
}
