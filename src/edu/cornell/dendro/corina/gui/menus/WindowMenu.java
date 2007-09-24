package edu.cornell.dendro.corina.gui.menus;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import edu.cornell.dendro.corina.core.App;

// REFACTOR: this is very similar to OpenRecent, a menu shared by all open frames.

// TODO: gpl header
// TODO: javadoc
// TODO: do everything from c'tor automatically; use addnotify/etc.

// this is the most gloriously fucked up class.  whee...
public class WindowMenu extends JMenu {
    //
    // class data
    //

    // list of windows: just ask Frame for it?

    // lists of menus and frames
    private static List menus = new ArrayList();
    private static List frames = new ArrayList();

    //
    // instance data
    //    
    public WindowMenu(JFrame target) {
        super("Window");
        final JFrame w = target;
        
        // minimize window
        JMenuItem minimize = new JMenuItem("Minimize");
        minimize.setAccelerator(KeyStroke.getKeyStroke("meta M")); // macize!
        minimize.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                w.setState(JFrame.ICONIFIED);
            }
        });
        add(minimize);

        // zoom window
        JMenuItem zoom = new JMenuItem("Zoom");
        zoom.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                // there's no default "zoom", so we'll have to sort of fake it.
                // the only reason they'd want wide windows is for element lists,
                // but that's not terribly common, so let's just maximize-tall.
                int max = Toolkit.getDefaultToolkit().getScreenSize().height;
		// TODO: 1.4 has maximize methods

		final int normal = 480; // pick a number

                if (App.platform.isMac()) // menubar
                    max -= 22;
                // take out any from the PC screenHeight?
		// TODO: 1.4 has methods to get insets, i believe
                w.setSize(w.getWidth(),
			  w.getHeight()>=max ? normal : max);

                // in 1.4, there is, but it's weird.  it's like this:
                // w.setExtendedState(JFrame.MAXIMIZED_BOTH);
                // the catch: if you iconify it normally, it forgets that it was maximized (!).
                // solution: bitwise-or it in.
            }
        });
        add(zoom);

        addSeparator();

	// aqua higs say "bring all to front" is optional.
	// i don't see much use for it: if you really need
	// that, click the corina icon in the dock.  it's
	// not worth my time to duplicate that functionality.

        // ---------------------------------------------------------
        // weird, evil, twisted stuff starts here
        // ---------------------------------------------------------
        
        // add me to list
        menus.add(this);
        frames.add(w);

        // add list of windows to me
        for (int i=0; i<menus.size(); i++) { // synch?
            JFrame frame = (JFrame) frames.get(i);
            JMenuItem window = new JCheckBoxMenuItem(frame.getTitle(), frame==w);
            final JFrame glue = frame;
            window.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
		    // this automatically changes the flag state,
		    // so i need to set it back.
		    JCheckBoxMenuItem s = (JCheckBoxMenuItem) e.getSource();
		    setSelected(false); // !s.isSelected());

                    if (glue.getState() == JFrame.ICONIFIED)
                        glue.setState(JFrame.NORMAL);
                    glue.toFront();
		    // how to get focus?  requestFocus() doesn't work!
                }
            });
            add(window);
        }

        // update all the other windows
        for (int i=0; i<menus.size(); i++) { // synch?  at same time as above?
            WindowMenu m = (WindowMenu) menus.get(i);
            if (m == this) continue; // skip me!
            JMenuItem window = new JCheckBoxMenuItem(w.getTitle(), false);
            window.addActionListener(new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
		    // this automatically changes the flag state,
		    // so i need to set it back.
		    JCheckBoxMenuItem s = (JCheckBoxMenuItem) e.getSource();
		    setSelected(false); // !s.isSelected());
		    // BUG: if you select yourself, it should do nothing.
		    // BUG: this doesn't restore the state of .selected

                    if (w.getState() == JFrame.ICONIFIED)
                        w.setState(JFrame.NORMAL);
                    w.toFront();
		    // how to get focus?  requestFocus() doesn't work!
		    // idea: do-click in the titlebar?  in some safe place?
		    // -- it's w.dispatchEvent(new ***Event());
		    // (sending window-activated/deiconified events doesn't work.)
                }
            });
            m.add(window);
        }
        // REFACTOR: "add listener to myself" is remarkably similar to "(foreach window in the list,) update listener".

        // to me: add window listener
        w.addWindowListener(new WindowAdapter() {
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
                    if (App.platform.isMac())
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
        
        
        // make cmd-` cycle windows? -- (automatic on mac)
        // make cmd-1, -2, ... jump to a window?
        // make cmd-left/right jump to prev/next? -- (cross->prev/next is better)
    }
}
