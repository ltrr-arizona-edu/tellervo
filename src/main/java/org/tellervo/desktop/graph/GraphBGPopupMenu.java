/*******************************************************************************
 * Copyright (C) 2001 Ken Harris
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

package org.tellervo.desktop.graph;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

// used by: GraphWindow, BargraphFrame
@SuppressWarnings("serial")
public class GraphBGPopupMenu extends JPopupMenu {

    public GraphBGPopupMenu() {
        
        // add
        JMenuItem addSeries = new JMenuItem("Add series...");
        addSeries.addActionListener(new AbstractAction(){
        	public void actionPerformed(ActionEvent ae){
        		// FIXME: implement!
        		
        	}
        });
        
        // gridlines
        JMenuItem gridLines = new JMenuItem("Gridlines");
        gridLines.addActionListener(new AbstractAction(){
        	public void actionPerformed(ActionEvent ae){
        		// FIXME: implement!
        		
        	}
        });
        
        // axis
        JMenuItem axis = new JMenuItem("Vertical axis");
        axis.addActionListener(new AbstractAction(){
        	public void actionPerformed(ActionEvent ae){
        		// FIXME: implement!
        		
        	}
        });        
        
        addSeries.setEnabled(false);
        axis.setEnabled(false);
        gridLines.setEnabled(false);
        
        add(gridLines);
        add(axis);
        addSeparator(); 
        add(addSeries);

    }

}
