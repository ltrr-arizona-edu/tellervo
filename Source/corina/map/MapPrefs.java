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

package corina.map;

import java.io.IOException;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.Timer;

public class MapPrefs extends JDialog {

    public MapPrefs() throws IOException {
	setTitle("Map Preferences");

	// for now, just the spinning small globe
	final MapPanel mapPanel = new MapPanel();
	mapPanel.setMinimumSize(new Dimension(100, 100));
	mapPanel.setMaximumSize(new Dimension(100, 100));
	getContentPane().add(mapPanel, BorderLayout.CENTER);

        mapPanel.getView().zoom = 5;

	Timer timer = new Timer(50 /* ms */, new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    // lat/long are backwards, it appears

                    mapPanel.getView().center.latitude -= 10/60.;
                    // mapPanel.myMap.setLongitude(mapPanel.myMap.getLongitude() + 1);

		    mapPanel.updateBuffer();
		    mapPanel.repaint();
		}
	    });
	timer.start();

	pack();
	show();

	addWindowListener(new WindowAdapter() {
		public void windowClosed(WindowEvent e) {
		    System.exit(0);
		}
	    });

	setSize(400, 400);
    }

    public static void main(String args[]) throws IOException {
	new MapPrefs();
    }
}
