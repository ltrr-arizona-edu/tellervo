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

package corina.browser;

import java.io.File;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.AbstractAction;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;

/**
    A popup showing a folder, and all of its parent folders.

    <p>A folder popup is a popup (JComboBox) which shows a folder,
    and allows the user to choose a parent folder.  For example:
    
    <table border="1" cellspacing="0" align="center">
        <tr><td>Corina</td></tr>
        <tr><td>&nbsp;&nbsp;Data</td></tr>
        <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;Forest</td></tr>
        <tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ZKB</td></tr>
    </table>
    
    Here, you're in the folder ZKB, which is in the folder Forest,
    which is in the folder Data, and so forth.  If you want to move
    up to the Data folder, simply choose that item.  The selected
    folder is always the bottom entry, so when you choose Data, the
    bottom entries are removed.</p>

    <p>Each FolderPopup has a <i>top-level</i>, and a <i>folder</i>.
    The <i>folder</i> is the folder you're looking at right now.
    In the above example, ZKB is the folder.  The <i>top-level</i>
    is the topmost folder which is displayed; above, Corina is the
    top-level.  The top-level is set when the FolderPopup is
    created, and can't be changed.  The top-level can be an actual
    top-level on your filesystem (like <code>/</code>), but it doesn't
    have to be.</p>

    <p>If you want to find out when a new folder is selected, add a
    ChangeListener to your FolderPopup.  ChangeListener is a generic
    Swing interface with a single method:
    
<pre>
    void stateChanged(ChangeEvent e);
</pre>

    To find out what the new folder is, use the FolderPopup's getFolder()
    method.</p>

    <h2>Left to do</h2>
    <ul>
        <li>verify that callListeners() is called exactly once
            when setFolders() is called.  i think it gets automatically
            called when setSelectedIndex() is called, here.
            but it might not.  or it might get called by addItem(), even.
            i'm not sure.

        <li>refactor: do init() and setFolder() have a bunch of similar code?

        <li>offer FolderPopup() c'tor which goes all the way up?
        <li>in callListeners(), report exceptions as bugs?

        <li>future: have folder icons in popup?
        <li>future: how to adapt this for use with ftp folders?
        
        <li>remove main() testing method
    </ul>
    
    @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id$
*/
public class FolderPopup extends JComboBox {

    private File toplevel;

    private File folder;

    /**
        Make a new folder popup, starting at a particular folder.
        The top-level folder is selected, by default (so the popup
        has just 1 entry).

        @param toplevel the top-level folder to use
        @exception IllegalArgumentException if the top-level isn't a folder
    */
    public FolderPopup(File toplevel) {
        // if it's not a folder, abort.
        if (!toplevel.isDirectory())
            throw new IllegalArgumentException("not a folder");

        // store args
        this.toplevel = toplevel;
        this.folder = toplevel;

        // set up the listener
        init();
    }
    
    /**
        Change the popup to select a new folder.

        @param folder the new folder to select
        @exception IllegalArgumentException if the new folder
        value isn't a folder, or if it isn't a subfolder of
        the top-level
    */
    public void setFolder(File folder) {
        if (!folder.isDirectory())
            throw new IllegalArgumentException("not a folder");
    
        this.folder = folder;
        
        removeAllItems();

        // since we can only add things from top to bottom,
        // and i can only find them bottom to top,
        // i'll have to put them in a list.
        File bottom = folder;
        File top = toplevel;
        List list = new ArrayList(); // a list of folder names
        while (!bottom.equals(top)) {
            list.add(bottom.getName());
            bottom = bottom.getParentFile();
            
            // we've hit the top of the filesystem, but haven't
            // hit top -- this might happen, for example, if
            // toplevel="/Users/shared/" and folder="/Applications/",
            // or if toplevel="G:\" and folder="C:\".
            if (bottom == null)
                throw new IllegalArgumentException();
        }

        // now, add everything to the list, in reverse order.
        Collections.reverse(list);
        String indent = "";
        for (int i=0; i<list.size(); i++) {
            addItem(indent + list.get(i));
            indent = "  " + indent; // indent each subsequent entry by 2 spaces
        }
            
        // finally, select the last item
        setSelectedIndex(getItemCount() - 1);
    }

    /**
        Return the currently selected folder.

        @return the selected folder
    */
    public File getFolder() {
        // note: it's immutable, so this is safe.
        return folder;
    }

    /**
        Get the top-level folder of this popup.

        @return the top-level folder of this popup
    */
    public File getTopLevel() {
        // note: it's immutable, so this is safe.
        return toplevel;
    }

    // when something is selected, do appropriate things
    private void init() {
        addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int selection = getSelectedIndex();
                int totalItems = getItemCount();

                // no change?  abort.
                if (selection == totalItems-1)
                    return;

                // set folder to this value
                for (int i=0; i<(totalItems-selection-1); i++)
                    folder = folder.getParentFile();

                // remove everything after it
                for (int i=selection+1; i<totalItems; i++)
                    removeItemAt(i);

                // call listeners
                callListeners();
            }
        });
    }

    //
    // LISTENERS
    //

    // a list of ChangeListeners to fire when the user selects
    // a new folder.
    private Vector listeners = new Vector();

    // call all the listeners.
    private void callListeners() {
        // alert all listeners
        Vector l;
        synchronized (this) {
            l = (Vector) listeners.clone();
        }

        int size = l.size();

        if (size == 0)
            return;

        ChangeEvent e = new ChangeEvent(this);

        for (int i=0; i<size; i++) {
            try {
                ChangeListener listener = (ChangeListener) l.elementAt(i);
                listener.stateChanged(e);
            } catch (Exception ex) {
                // just ignore any exceptions that happen now.
            }
        }
    }

    /**
        Add a new listener.

        @param listener the listener to add
    */
    public void addChangeListener(ChangeListener listener) {
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    /**
        Remove a listener.

        @param listener the listener to remove
    */
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    //
    // TESTING
    //

    public static void main(String args[]) {
        final FolderPopup p = new FolderPopup(new File("/Users/kenharris/Desktop/Corina/"));
        p.setFolder(new File("/Users/kenharris/Desktop/Corina/Source/corina/prefs/"));

        ChangeListener c = new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                System.out.println("folder = " + p.getFolder());
            }
        };
        p.addChangeListener(c);

        javax.swing.JFrame f = new javax.swing.JFrame();
        f.getContentPane().add(p);
        f.pack();
        f.show();
    }
}
