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

import javax.swing.JDialog;
import javax.swing.JPanel;

/*
  i do this stuff sooo often, might as well make it its own class.

  what it does:
  -- builds a dialog
  ---- add your stuff to the middle
  -- adds cancel/ok buttons to bottom
  -- shows it
  ---- calls your "ok" method when ok is clicked
  ---- disposes when cancel is clicked
*/

public abstract class XDialog extends JDialog {

    // clients need to override these

    public abstract JPanel build();
    public abstract void ok();

    // i'll handle these...

    // WRITE ME

}
