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

package corina.gui;

import corina.ui.Builder;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
   A generic frame (window).  All non-transient frames should subclass
   this.  It automatically provides:

   <ul>
     <li>Auto-dispose on close
     <li>Nifty tree icon
   </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public abstract class XFrame extends JFrame implements WindowListener {

    /*
      i hate this class.  i'd much prefer to simply have a
      disposeIfNotSaved(SaveableDoc) method, which any Frame could use
      to override dispose(), so i wouldn't need to care about XFrames
      and close().  but i can't get that to work.  actually, i had
      incredible difficulties getting anything to work, because the
      behavior of dispose() and windowClosing(), etc., isn't very well
      documented.  so: please refactor me, but i don't know how.

      -- this looks like a perfect example of paul graham's argument
      against OOPS.  i'd much rather have the ability to add these
      features to windows as i like, rather than having the union of
      all of them to take or leave.
    */

    //
    // constructor
    //
    public XFrame() {
	setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
	addWindowListener(this);

	// tree icon
	setTreeIcon();
    }

    //
    // icon
    //
    private void setTreeIcon() {
	setIconImage(Builder.getImage("Tree.png"));
    }

    //
    // if closing an unsaved document, make sure that's what the user wants.
    //
    public void windowClosing(WindowEvent e) {
	close(); // close me, if user says ok.
    }
    public void windowActivated(WindowEvent e) { }
    public void windowClosed(WindowEvent e) { }
    public void windowDeactivated(WindowEvent e) { }
    public void windowDeiconified(WindowEvent e) { }
    public void windowIconified(WindowEvent e) { }
    public void windowOpened(WindowEvent e) { }

    // call me to close an XFrame.  confirms close with user, if
    // document has been modified since last save.
    private boolean _dialogVisible=false;
    public void close() {
	// if saveable...
	if (this instanceof SaveableDocument) {
	    // saved?  ok, close.
	    if (((SaveableDocument) this).isSaved()) {
		dispose();
		return;
	    }

	    // no?  ask if user wants to save first.
	    if (_dialogVisible) return; // -- could synch handle this better?
	    _dialogVisible=true;
	    ConfirmSave.showDialog((SaveableDocument) this);
	    _dialogVisible=false;

	} else {
	    // just close it
	    dispose();
	}
    }
}
