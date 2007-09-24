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

package edu.cornell.dendro.corina.map.tools;

import edu.cornell.dendro.corina.map.MapPanel;
import edu.cornell.dendro.corina.map.View;

import javax.swing.JToolBar;
import javax.swing.JFrame;
import java.awt.Dimension;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.KeyStroke;

/**
   A toolbox of mapping tools: an arrow, a hand, a ruler, and a magnifying glass.

   <h2>Left to do:</h2>
   <ul>
     <li>Javadoc
     <li>Why not extract Zoomer into its own class, then add it to the toolbar?
         It keeps the controls on the same line, so there's more space to look
         at the map.  No, better: put a simple search field here.
     <li>Get rid of the magnifying glass tools.  They serve no purpose, any more.
     <li>Fix the arrow tool to do both site-moving and tag-moving
   </ul>
 
   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
*/
public class ToolBox extends JToolBar {

    private Tool tools[];
    private View view;
    private MapPanel p;
    private Tool oldTool;

    // get passed: mappanel (for cursors, events, etc.), and renderer (for rendering/unrendering)
    public ToolBox(View v, MapPanel p, JFrame f) {
        super(VERTICAL); // if you want it vertical -- put it on the west side, too, then.

        // keep view, panel
        this.view = v; // view is mutable, right?
        this.p = p;

        // nobody wants this "feature".
        setFloatable(false);

        tools = new Tool[] {
            new ArrowTool(p, v, this),
            null,
            new HandTool(p, v, this),
            null,
            new RulerTool(p, v, this),
            null,
            new ZoomInTool(p, v, this),
            new ZoomOutTool(p, v, this),
        };

        for (int i=0; i<tools.length; i++) {
            if (tools[i] != null)
                add(tools[i].getButton());
            else
                addSeparator(new Dimension(8, 8));
        }

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
                    if (tools[i] == null)
                        continue;

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
                    if (tools[i] == null)
                        continue;

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
        for (int i=0; i<tools.length; i++) {
            if (tools[i] == null)
                continue;
            if (tools[i] != t)
                tools[i].getButton().setSelected(false);
        }

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
