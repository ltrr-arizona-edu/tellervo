package corina.map.tools;

import corina.map.MapPanel;
import corina.map.View;

import javax.swing.JToolBar;
import javax.swing.JFrame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.KeyStroke;

// a box of mapping tools: ruler, hand, zoom, etc.
public class ToolBox extends JToolBar {

    private Tool tools[];
    private View view;
    private MapPanel p;
    private Tool oldTool;

    // get passed: mappanel (for cursors, events, etc.), and renderer (for rendering/unrendering)
    public ToolBox(View v, MapPanel p, JFrame f) {
        // keep view, panel
        this.view = v; // view is mutable, right?
        this.p = p;

        tools = new Tool[] {
            new ArrowTool(p, v, this),
            new HandTool(p, v, this),
            new RulerTool(p, v, this),
            new ZoomInTool(p, v, this),
            new ZoomOutTool(p, v, this),
            new TagMoverTool(p, v, this),
        };

        for (int i=0; i<tools.length; i++)
            add(tools[i].getButton());

        // start out with the hand
        tools[0].getButton().setSelected(true);
        selected(tools[0]);

        // let user press the key to select a tool (H => hand, etc.)
        f.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                // get the letter he typed
                char c = Character.toUpperCase(e.getKeyChar());

                // look for the tool that has that key
                for (int i=0; i<tools.length; i++) {
                    Character test = tools[i].getKey();
                    if (test!=null && c==Character.toUpperCase(test.charValue()) && !tools[i].getButton().isSelected()) {
                        tools[i].getButton().doClick();
                        return;
                    }
                }
            }
        });

        // let user temporarily get a tool by holding down a certain key combination (space => hand, etc.)
        f.addKeyListener(new KeyAdapter() {
            private Tool oldTool = null;
            public void keyPressed(KeyEvent e) {
                // oldTool non-null?  then something was already pressed
                if (oldTool != null)
                    return;

                // look through the tools for a fastkey that matches this one
                for (int i=0; i<tools.length; i++) {
                    KeyStroke test = tools[i].getFastKey();

                    if (test == null) // not all tools have fastkeys
                        continue;

                    // compare the key that was pressed to this fastkey -- keychar and modifiers
                    if (test.getKeyChar()==e.getKeyChar() && test.getModifiers()==e.getModifiers()) {
                        System.out.println("looks like " + tools[i].getTooltip());

// this code is COMPLETELY UNTESTED

                        // if there is one, set oldtool (so we can get it back later)
                        // oldTool = tool; // ???

                        // and give the user this tool (but don't select the button)
                        selected(tools[i]); // ???

                        // (and stop looking)
                        break;
                    }
                }
            }
            public void keyReleased(KeyEvent e) {
                // if the user was using this feature,
                if (oldTool != null) {
                    // go back to whatever the old tool was
                    oldTool.getButton().doClick();
                    
                    // reset oldTool 
                    oldTool = null;
                }

                // need to wait for mouse-released?
            }
        });
    }

    protected void selected(Tool t) {
        // disable all others
        for (int i=0; i<tools.length; i++)
            if (tools[i] != t)
                tools[i].getButton().setSelected(false);

        // remove old mouse/mousemotion listeners, and decorator
        p.removeMouseListener(oldTool);
        p.removeMouseMotionListener(oldTool);
        p.removeKeyListener(oldTool);
        p.removeDecorator(oldTool);

        // set up new mouse/mousemotion listeners
        p.addMouseListener(t);
        p.addMouseMotionListener(t);
        p.addKeyListener(t);
        p.addDecorator(t);

        // (we'll remove this one next time)
        oldTool = t;

        // set cursor
        p.setCursor(t.getCursor());
    }
}
