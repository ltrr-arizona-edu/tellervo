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

package corina.cross;

import corina.gui.XFrame;
import corina.gui.PrintableDocument;
import corina.gui.FileDialog;
import corina.gui.UserCancelledException;
import corina.print.Printer;

import java.io.IOException;

import java.util.List;

import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PageFormat;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public class TableFrame extends XFrame implements PrintableDocument {

    // gui
    private JTable table;

    // data
    private String s=null;
    private List ss=null;
    private Table t=null;

    // toString -- ??
    public String toString() {
	// ouch, toString might get called before t is initialized.
	if (t == null)
	    return "???";

	// normal return statement here
	return t.toString();
    }

    private void computeTable() {
	try {
	    t = new Table(s, ss);
	    setTitle(t.toString());
	} catch (IOException ioe) {
	    // t = null;
	    return;
	}
	t.run();
    }

    private void initTable() {
        table = new JTable(new TableTableModel(t));
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
    }

    public int getPrintingMethod() {
        return PrintableDocument.PAGEABLE;
    }
    public Pageable makePageable(PageFormat pf) {
        return null;
    }
    public Printable makePrintable(PageFormat pf) {
        TablePrinter printer = new TablePrinter(t, pf);
        return Printer.print(printer);
    }
    public String getPrintTitle() {
        return "Table"; // customize with master?
    }

    private void create() {
	// create frame
	setTitle("Table");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	// init gui
	computeTable();
	initTable();

        // using buttons is silly.  it's just save-text and save-html.
        // how it should be done: file->export has options "plain text"
        // and "HTML" (possibly multiple versions, as long as there's help saying when to use what).
        // so: make exportdialog flexible enough to handle non-sample data, and add a "copy" button
        // so users can copy the HTML (for example) to the clipboard directly.
        // but make sure there's a plain ol' "save" command to save the crossdate (seq only?), too.
        
	// show the frame
	pack();
	setSize(new Dimension(640, 480));
	show();
    }

    public TableFrame(String s, List ss) {
        // data
        this.s = s;
        this.ss = ss;
        create(); // pseudo-constructor
    }

    public TableFrame() {
        try {
            s = FileDialog.showSingle("First");
            ss = FileDialog.showMulti("Samples");
            create();
        } catch (UserCancelledException uce) {
            dispose(); // ok?
            return;
        }
    }
}
