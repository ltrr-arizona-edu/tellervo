/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
package org.tellervo.desktop.gui;

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.tellervo.desktop.platform.Platform;


// REFACTOR: this is very similar to OpenRecent, a menu shared by all open frames.

// this is the most gloriously fucked up class.  whee...
@SuppressWarnings("serial")
public class WindowMenu extends JMenu {
    //
    // class data
    //

    // list of windows: just ask Frame for it?

    // lists of menus and frames
    @SuppressWarnings("unchecked")
	private static List menus = new ArrayList();
    @SuppressWarnings("unchecked")
	private static List frames = new ArrayList();

    //
    // instance data
    //    
    @SuppressWarnings("unchecked")
	public WindowMenu(JFrame target) {
        super("Window");
        final JFrame w = target;
        
        // close
        JMenuItem close = new JMenuItem("Close Window");
        close.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // tell it to dispatch a WINDOW_CLOSING event, so it asks the user
                // to save the file first.
                w.dispatchEvent(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
            }
        });
        add(close);

        // zoom window
        JMenuItem zoom = new JMenuItem("Zoom Window"); // FIXME: "Maximize" on win32
        zoom.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // there's no default "zoom", so we'll have to sort of fake it.
                // the only reason they'd want wide windows is for element lists,
                // but that's not terribly common, so let's just maximize-tall.
                int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
                if (Platform.isMac()) // menubar
                    screenHeight -= 22;
                // take out any from the PC screenHeight?
                w.setSize(w.getSize().width, w.getSize().height>=screenHeight ? 480 : screenHeight);

                // in 1.4, there is, but it's weird.  it's like this:
                // w.setExtendedState(JFrame.MAXIMIZED_BOTH);
                // the catch: if you iconify it normally, it forgets that it was maximized (!).
                // solution: bitwise-or it in.
            }
        });
        add(zoom);

        // minimize window
        JMenuItem minimize = new JMenuItem("Minimize Window");
        minimize.setAccelerator(KeyStroke.getKeyStroke("meta M")); // macize!
        minimize.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                w.setState(Frame.ICONIFIED);
            }
        });
        add(minimize);

        addSeparator();

        // "Bring All to Front"
        JMenuItem toFront = new JMenuItem("Bring All to Front");
        toFront.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // WRITEME: is this even possible in java?
            }
        });
        toFront.setEnabled(false);
        add(toFront);

        addSeparator();

        // ---------------------------------------------------------
        // weird, evil, twisted stuff starts here
        // ---------------------------------------------------------
        
        // add me to list
        menus.add(this);
        frames.add(w);

        // add list of windows to me
        for (int i=0; i<menus.size(); i++) { // synch?
            JFrame frame = (JFrame) frames.get(i);
            JMenuItem window = new JMenuItem(frame.getTitle());
            final JFrame glue = frame;
            window.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (glue.getState() == Frame.ICONIFIED)
                        glue.setState(Frame.NORMAL);
                    glue.toFront();
                }
            });
            add(window);
        }

        // update all the other windows
        for (int i=0; i<menus.size(); i++) { // synch?  at same time as above?
            WindowMenu m = (WindowMenu) menus.get(i);
            if (m == this) continue; // skip me!
            JMenuItem window = new JMenuItem(w.getTitle());
            window.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    if (w.getState() == Frame.ICONIFIED)
                        w.setState(Frame.NORMAL);
                    w.toFront();
                }
            });
            m.add(window);
        }
        // REFACTOR: "add listener to myself" is remarkably similar to "(foreach window in the list,) update listener".

        // to me: add window listener
        w.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosed(WindowEvent e) {
                // remove me from all other windows
                for (int i=0; i<menus.size(); i++) {
                    WindowMenu m = (WindowMenu) menus.get(i);
                    int n = m.getItemCount();
                    for (int j=0; j<n; j++) {
                        if (m.getItem(j) == null)
                            continue; // null means "not a menuitem" (separator)
                        if (m.getItem(j).getText().equals(w.getTitle())) {
                            m.remove(j); // remove menuitem: DOESN'T QUITE WORK!
                            break;
                        }
                    }
                    if (Platform.isMac())
                        m.updateUI(); // WHOOPS: this swaps the help/windows menus.  but without it, it's worse.  ugh.
                }

                // remove me from lists
                for (int i=0; i<menus.size(); i++) {
                    if (w.equals(frames.get(i))) {
                        menus.remove(i);
                        frames.remove(i);
                    }
                }
            }
        });

        // if renamed/redated/modified, indicate in all menus
        // how?  i need to be a samplelistener, or some generic documentlistener (titlechanged)
        
        
        // make cmd-` cycle windows?
        // make cmd-1, -2, ... jump to a window?
        // make cmd-left/right jump to prev/next?
    }
}
