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

package corina.editor;

import corina.ui.Builder;
import corina.ui.I18n;

import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.UnsupportedCommOperationException;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.TooManyListenersException;
import java.util.ArrayList;
import java.util.List;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/*
  
 */

public class Measure extends Thread implements SerialPortEventListener {

    // port ID
    private CommPortIdentifier portID;

    // ???
    BufferedReader reader;
    SerialPort serialPort;

    private Editor _e;

    // singleton!
    private static Measure _m=null;

    // ----------------------------------------------------------------------
    // menu commands
    public static boolean hasSerialAPI() {
        try {
            Class.forName("javax.comm.SerialPort");
            return true;
        } catch (ClassNotFoundException cnfe) {
            return false;
        }
    }
    // a bunch of menuitems:
    // -- when "start" is selected, it becomes "stop" and all others are dimmed.
    // -- when "stop" is selected, it becomes "start" and all others are enabled.
    private static List menus = new ArrayList(); // of jmenuitem
    public JMenuItem makeMenuItem() {
	// make a new menuitem
	final JMenuItem menu = Builder.makeMenuItem("start_measuring"); // shortcut key?  F1/F2?
	menu.setAccelerator(KeyStroke.getKeyStroke("F1"));
	if (_m != null) // is running?
	    menu.setEnabled(false);
	menu.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    if (_m != null) { // was running, stop now
			// i become "start"
			menu.setText(I18n.getText("start_measuring"));

			// stop thread
			_m.pleaseStop();
			_m = null;

			// close the port -- BUG, why is this null?
			// serialPort.close();

			// all others un-dimmed
			for (int i=0; i<menus.size(); i++)
			    if (((Reference) menus.get(i)).get() != menu)
				((JMenuItem) ((Reference) menus.get(i)).get()).setEnabled(true);
		    } else { // (was not running, start now)
			// i become "stop"
			menu.setText(I18n.getText("stop_measuring"));

			// all others get dimmed
			for (int i=0; i<menus.size(); i++)
			    if (((Reference) menus.get(i)).get() != menu)
				((JMenuItem) ((Reference) menus.get(i)).get()).setEnabled(false);

			// start thread
			_m = new Measure(_e);
			_m.start();
		    }
		}
	    });

	// i'll remember this one, but only as long as you do
	menus.add(new WeakReference(menu));

	// return it
	return menu;
    }
    // ----------------------------------------------------------------------

    public Measure(Editor e) {
	_e = e;

	// nothing else
    }

    public void start() {
	// identify the port to use
	try {
	    portID = CommPortIdentifier.getPortIdentifier(System.getProperty("corina.measure.port", "COM1"));
	} catch (NoSuchPortException nspe) {
	    System.out.println("no such port!");
	    // HANDLE ME
	    return;
	}

	// try to open the port
	try {
            serialPort = (SerialPort) portID.open("Corina", 1000 /* ms */);
        } catch (PortInUseException piue) {
	    System.out.println("port in use!");
	    // HANDLE ME
	    return;
	}

	// open an input stream
        try {
	    reader = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
        } catch (IOException ioe) {
	    System.out.println("can't open stream!");
	    // HANDLE ME
	    return;
	}
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException tmle) {
	    System.out.println("can't add event listener!");
	    // HANDLE ME
	    return;
	}

	// let me know when there's data
        serialPort.notifyOnDataAvailable(true);

	// configure for 9600/8/N/1
        try {
            serialPort.setSerialPortParams(9600,
					   SerialPort.DATABITS_8,
					   SerialPort.STOPBITS_1,
					   SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException ucoe) {
	    System.out.println("can't set parameters!");
	    // HANDLE ME
	    return;
	}

	// now you can start
	super.start();
    }

    // why don't i need to override run()?  because i do my stuff by
    // registering a listener in start().  maybe i should sleep...
    public void run() {
	while (!pleaseStop) {
	    try {
		Thread.sleep(100 /* ms */);
		// i'll bet nobody's going to stop measure-mode in
		// this window, then start measure-mode somewhere else
		// in under 0.1s.
	    } catch (InterruptedException ie) {
		// ignore
	    }
	}
    }

    private boolean pleaseStop = false;
    public void pleaseStop() {
	// kill the thread nicely -- editor's "close" should do this
	pleaseStop = true;
    }

    public void serialEvent(SerialPortEvent spe) {
	// if it's not DATA, i don't know what it is, so ignore it
	if (spe.getEventType() != SerialPortEvent.DATA_AVAILABLE)
	    return;

	// as long as there's data available...
	for (;;) {
	    // get line
	    String line;
	    try {
		line = reader.readLine().trim();
	    } catch (IOException ioe) {
		// can't read a line.  i can't imagine what would
		// cause this.  but we're the first test, so it'll
		// come right back, so we'd better just return.
		return;
	    }

	    // if it starts with ';', it's just me being silly
	    if (line.startsWith(";"))
		continue;

	    // it must be a number, therefore, parse it
	    int value = Integer.parseInt(line);

	    // tell the editor
	    _e.measured(value);

	    // done?
	    try {
		if (!reader.ready())
		    return;
	    } catch (IOException ioe) {
		// what to do on ioe here?  can't continue, because
		// it'll probably keep throwing.  gotta return.
		return;
	    }
	}
    }
}
