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

import java.net.URL;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

/**
   A generic XCorina frame (window).  All non-transient frames should
   subclass this.  It automatically provides:

   <ul>
     <li>Auto-dispose on close
     <li>Nifty tree icon
   </ul>

   @author <a href="mailto:kbh7@cornell.edu">Ken Harris</a>
   @version $Id$ */

public abstract class XFrame extends JFrame implements WindowListener {

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
	ImageIcon treeIcon = null;
	URL iconURL = ClassLoader.getSystemResource("Images/tree.png");
	if (iconURL != null) {
	    treeIcon = new ImageIcon(iconURL);
	    setIconImage(treeIcon.getImage());
	}
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
	    if (_dialogVisible) return;
	    _dialogVisible=true;
	    ConfirmSave.showDialog((SaveableDocument) this);
	    _dialogVisible=false;

	} else {
	    // just close it
	    dispose();
	}
    }
}
