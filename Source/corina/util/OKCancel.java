package corina.util;

import javax.swing.JDialog;
import javax.swing.JButton;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

// class for dealing with ok/cancel stuff, which happens all the friggin' time.
// swing, why do you suck so much?
public class OKCancel {

    // add esc=>cancel and return=>ok bindings
    // TODO: accept "cancel" button, too -- null=dispose
    public static void addKeyboardDefaults(JDialog d, JButton ok) {
        final JDialog dd = d;
        
        // esc => cancel
        dd.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    dd.dispose();
            }
        });

        // return => ok
        dd.getRootPane().setDefaultButton(ok);
    }

    // other things to do:
    // -- ok/cancel i18ns
    // -- ordering is different on mac/win32 (unix is ???)
    // -- make panel with just ok/cancel, given "ok", "cancel" code (runnables?)
}
