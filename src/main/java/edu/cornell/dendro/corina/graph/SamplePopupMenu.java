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

package edu.cornell.dendro.corina.graph;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.sample.Sample;
import edu.cornell.dendro.corina.ui.Builder;

// used by: GraphWindow, BargraphFrame
@SuppressWarnings("serial")
public class SamplePopupMenu extends JPopupMenu {

    private JMenuItem titleItem, rangeItem, scaleItem;

    public SamplePopupMenu(GrapherPanel gpanel) {
    	
    	this.panel = gpanel;
    	
        // dummy entries: the name of the sample, and its range
        titleItem = new JMenuItem();
        titleItem.setEnabled(false);
        rangeItem = new JMenuItem();
        rangeItem.setEnabled(false);
        scaleItem = new JMenuItem();
        scaleItem.setEnabled(false);
        
        titleItem.setForeground(java.awt.Color.BLUE);

        // TODO: add cut, copy commands here?

        // open
        JMenuItem open = Builder.makeMenuItem("menus.file.open");
        open.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                new Editor(s);
            }
        });

        // plot
        JMenuItem plot = new JMenuItem("Plot on new graph"); // FIXME: i18n?  use Builder?
        plot.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent ae) {
                new GraphWindow(s);
            }
        });
        
        // remove
        JMenuItem remove = new JMenuItem("Remove");
        remove.addActionListener(new AbstractAction(){
        	public void actionPerformed(ActionEvent ae){
        		// FIXME: implement!
        		
        	}
        });
        
        // properties
        JMenuItem properties = new JMenuItem("Format series");
        properties.addActionListener(new AbstractAction(){
        	public void actionPerformed(ActionEvent ae){
        		// FIXME: implement!
        		
        	}
        });
        
        remove.setEnabled(false);
        properties.setEnabled(false);

        //add(titleItem);
        //add(rangeItem);
        //add(scaleItem);
        add(open);
        add(plot);       
        add(remove);
        addSeparator(); 
        add(properties);
    }

    private Sample s;
	@SuppressWarnings("unused")
	private GrapherPanel panel;

    public void setSample(Sample s) {
        this.s = s;

        // update title, range
        titleItem.setText(s.getDisplayTitle());
        rangeItem.setText(Builder.INDENT + s.getRange().toStringWithSpan());
        scaleItem.setText("Sample scale: " + String.valueOf(s.getScale()));
    }
}
