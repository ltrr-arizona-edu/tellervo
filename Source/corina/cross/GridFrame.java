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

import corina.graph.GraphFrame;
import corina.gui.SaveableDocument;
import corina.gui.PrintableDocument;
import corina.gui.HasPreferences;
import corina.prefs.PrefsDialog;
import corina.prefs.Prefs;
import corina.gui.XFrame;
import corina.gui.XMenubar;
import corina.gui.FileDialog;
import corina.gui.WindowMenu;

import java.util.List;
import java.util.ResourceBundle;

import java.io.File;
import java.io.IOException;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import javax.swing.JTable;
import javax.swing.JOptionPane;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.ButtonGroup;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Action;
import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

public class GridFrame extends XFrame
                    implements SaveableDocument,
			       PrintableDocument,
			       HasPreferences {
    // gui
    private JTable output;

    // data
    private Grid grid=null;

    // i18n
    private ResourceBundle msg = ResourceBundle.getBundle("CrossdatingBundle");

    // saving
    private String filename=null;

    // saveabledocument
    public boolean isSaved() { // don't worry about deleting grids, for now
	return true; // fixme
    }
    public void save() {
	// check filename
	if (filename == null) {
	    filename = FileDialog.showSingle("Save");
	    if (filename == null)
		return;

	    // check for already-exists
	    {
		File f = new File(filename);
		    if (f.exists()) {
			Object options[] = new Object[] { "Overwrite", "Cancel" }; // good, explicit commands
			int x = JOptionPane.showOptionDialog(null,
							     "A file called \"" + filename + "\"\n" +
							       "already exists; overwrite it with this grid?",
							     "Already Exists",
							     JOptionPane.YES_NO_OPTION,
							     JOptionPane.QUESTION_MESSAGE,
							     null, // icon
							     options,
							     null); // default
			if (x == 1) // cancel
			    return; // should return FAILURE -- how?
		    }
	    }
	}

	// save!
	try {
	    grid.save(filename);
	} catch (IOException ioe) {
	    // error!
	    JOptionPane.showMessageDialog(null,
					  "Error: " + ioe.getMessage(),
					  "Error saving",
					  JOptionPane.ERROR_MESSAGE);
	}
    }
    public String getDocumentTitle() {
        return "Grid: " + filename;
    }
    public void setFilename(String fn) {
	filename = fn;
    }
    public String getFilename() {
	return filename;
    }

    // table model for the grid
    private final class GridTableModel extends AbstractTableModel {
	public int getColumnCount() {
	    return (grid == null ? 0 : grid.size()+1);
	}
	public int getRowCount() {
	    return (grid == null ? 0 : grid.size()+1);
	}
	public Object getValueAt(int row, int col) {
	    return grid.getCells()[row][col];
	}
    }

    private static double scale = Double.parseDouble(System.getProperty("corina.grid.scale", "1.0"));

    // cell renderer
    private class GridRenderer implements TableCellRenderer {
	public Component getTableCellRendererComponent(JTable table,
						       Object value,
						       boolean isSelected,
						       boolean hasFocus,
						       int row, int column) {
	    final Grid.Cell cell = (Grid.Cell) value;

	    // hilite on isSelected&&hasFocus?  (why can't i get that
	    // to work?)

	    // new component each time seems inefficient.  come up with a better way.
	    JComponent c = new JComponent() {
		    public void paintComponent(Graphics g) {
			// set font: get original font, and scale it
			Font origFont = (System.getProperty("corina.grid.font")==null ? g.getFont() : Font.getFont("corina.grid.font"));
			Font scaledFont = new Font(origFont.getName(),
						   origFont.getStyle(),
						   (int) ((double) origFont.getSize() * scale));
			g.setFont(scaledFont);

			// if (System.getProperty("corina.grid.font") != null)
			// g.setFont(Font.getFont("corina.grid.font"));

			cell.print((Graphics2D) g, 0, 0, scale);
		    }
	    };
	    return c;
	}
    }

    // PrintableDocument
    public int getPrintingMethod() {
        return PrintableDocument.PAGEABLE;
    }
    public Pageable makePageable(PageFormat pf) {
	return grid.makeHardcopy(pf);
    }
    public Printable makePrintable(PageFormat pf) {
        return null;
    }
    public String getPrintTitle() {
        return "Grid";
    }

    private void initTable() {
	output = new JTable(new GridTableModel());

	// cell-selection only
	output.setRowSelectionAllowed(false);

	// set cell height/width from Grid
	output.setRowHeight((int) (Grid.getCellHeight()*scale) + 2);
	for (int i=0; i<output.getColumnCount(); i++)
	    output.getColumnModel().getColumn(i).setPreferredWidth((int) (Grid.getCellWidth()*scale) + 2);

	// no top-header
	output.setTableHeader(null);
	output.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

	// renderer -- use same as for printer
	output.setDefaultRenderer(Object.class, new GridRenderer());
	output.setShowGrid(false);

	// put the table in a scroller
        JScrollPane scroller = new JScrollPane(output);
	getContentPane().add(scroller, BorderLayout.CENTER);
    }

    public GridFrame(List s) {
        grid = new Grid(s);
        grid.run(); // change cursor to WAIT?
        init();
    }

    public GridFrame(Grid g) {
        grid = g;
        // move grid.run() to init(), and make run() a no-op if already run?
        init();
    }

    public GridFrame() {
        // get args
        List samples = FileDialog.showMulti(msg.getString("grid"));
        if (samples == null) { // nothing selected ... exit gracefully
            dispose();
            return;
        }

        grid = new Grid(samples);
        grid.run(); // change cursor to WAIT?
        init();
    }

    void init() {
        setTitle(msg.getString("crossdating_grid"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initTable();

        setJMenuBar(new XMenubar(this, makeMenus()));

        pack();
        setSize(new Dimension(640, 480));
        show();
    }
    
    private JMenu[] makeMenus() {
	// menubar
	JMenu view = new XMenubar.XMenu("View", 'V');

	/*
	JMenuItem mark = new XMenubar.XMenuItem("Mark Cells...");
	mark.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    new MarkCrosses();
		}
	    });
	view.add(mark);
	*/

	JMenuItem graph = new XMenubar.XMenuItem("Graph All");
	graph.addActionListener(new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
		    new GraphFrame(grid.getFiles());
		}
	    });
	view.add(graph);

	// ---
	view.addSeparator();

	// zoom in
	AbstractAction zoomIn = new AbstractAction("Zoom In") {
		public void actionPerformed(ActionEvent e) {
		    // increase by 0.1
		    scale += 0.1;
		    
		    // set property
		    System.setProperty("corina.grid.scale", Double.toString(scale));

		    // tell 'em -- use Preferences.updateAll()?
		    refreshFromPreferences();

		    // save new pref
		    try {
			Prefs.save();
		    } catch (IOException ioe) {
			// ignore
		    }
		}
	    };
        zoomIn.putValue(Action.MNEMONIC_KEY, new Integer('I'));
        zoomIn.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(XMenubar.macize("control UP")));
        view.add(new XMenubar.XMenuItem(zoomIn));

	// zoom out
	AbstractAction zoomOut = new AbstractAction("Zoom Out") {
		public void actionPerformed(ActionEvent e) {
		    // decrease by 0.1
		    scale -= 0.1;

		    // set property
		    System.setProperty("corina.grid.scale", Double.toString(scale));

		    // tell 'em -- use Preferences.updateAll()?
		    refreshFromPreferences();

		    // save new pref
		    try {
			Prefs.save();
		    } catch (IOException ioe) {
			// ignore
		    }
		}
	    };
	zoomOut.putValue(Action.MNEMONIC_KEY, new Integer('O'));
	zoomOut.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(XMenubar.macize("control DOWN")));
	view.add(new XMenubar.XMenuItem(zoomOut));

	return new JMenu[] { view, new WindowMenu(this) };
    }

    // HasPreferences
    public void refreshFromPreferences() {
	// re-read scale
	scale = Double.parseDouble(System.getProperty("corina.grid.scale", "1.0"));

	// reset sizes
	output.setRowHeight((int)(Grid.getCellHeight()*scale) + 2);
	for (int i=0; i<output.getColumnCount(); i++)
	    output.getColumnModel().getColumn(i).setPreferredWidth((int)(Grid.getCellWidth()*scale) + 2);

	// redraw?  sure.
	repaint();
    }

    /* prefs: (these are the defaults)
       corina.grid.markSig = true
       corina.grid.booleanOp = and
       corina.grid.useTscore = true
       corina.grid.useTrend = false
       corina.grid.useDscore = false
       corina.grid.useOverlap = true
     */
    private static class MarkCrosses extends JDialog {
	MarkCrosses() {
	    setTitle("Mark Significant Crosses");
	    setResizable(false);

	    JCheckBox markSig;
	    markSig = new JCheckBox("Mark Significant Crosses");
	    markSig.setSelected(Boolean.getBoolean("corina.grid.markSig"));

	    JButton change;
	    change = new JButton("Change Color...");

	    final JRadioButton oneOf, allOf;
	    ButtonGroup group;
	    oneOf = new JRadioButton("If ONE of the following is significant");
	    allOf = new JRadioButton("If ALL of the following are significant");
	    group = new ButtonGroup();
	    group.add(oneOf);
	    group.add(allOf);
	    oneOf.setSelected(true);

	    final JCheckBox tscore, trend, dscore, overlap;
	    tscore = new JCheckBox("T-Score (t > 2.65)");
	    tscore.setSelected(Boolean.getBoolean("corina.grid.useTscore"));
	    trend = new JCheckBox("Trend (tr > 55%)");
	    trend.setSelected(Boolean.getBoolean("corina.grid.useTrend"));
	    dscore = new JCheckBox("D-Score (d > 13.2)");
	    dscore.setSelected(Boolean.getBoolean("corina.grid.useDscore"));
	    overlap = new JCheckBox("Overlap (N > 50)");
	    overlap.setSelected(Boolean.getBoolean("corina.grid.useOverlap"));

	    // disable stuff that should be disabled
	    if (!Boolean.getBoolean("corina.grid.markSig")) {
		oneOf.setEnabled(false);
		allOf.setEnabled(false);
		tscore.setEnabled(false);
		trend.setEnabled(false);
		dscore.setEnabled(false);
		overlap.setEnabled(false);
	    }

	    //
	    // event handling
	    //
	    markSig.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean active = (e.getStateChange() == ItemEvent.SELECTED);
			oneOf.setEnabled(active);
			allOf.setEnabled(active);
			tscore.setEnabled(active);
			trend.setEnabled(active);
			dscore.setEnabled(active);
			overlap.setEnabled(active);
			System.setProperty("corina.grid.markSig", String.valueOf(active));
			// fire something
		    }
		});
	    oneOf.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			if (!selected) return;
			System.setProperty("corina.grid.booleanOp", "or");
			// fire something
		    }
		});
	    allOf.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			if (!selected) return;
			System.setProperty("corina.grid.booleanOp", "and");
			// fire something
		    }
		});
	    tscore.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			System.setProperty("corina.grid.useTscore", String.valueOf(selected));
			// fire something
		    }
		});
	    trend.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			System.setProperty("corina.grid.useTrend", String.valueOf(selected));
			// fire something
		    }
		});
	    dscore.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			System.setProperty("corina.grid.useDscore", String.valueOf(selected));
			// fire something
		    }
		});
	    overlap.addItemListener(new ItemListener() {
		    public void itemStateChanged(ItemEvent e) {
			boolean selected = (e.getStateChange() == ItemEvent.SELECTED);
			System.setProperty("corina.grid.useOverlap", String.valueOf(selected));
			// fire something
		    }
		});

	    // layout
            JPanel indented = new JPanel();
            indented.setLayout(new BoxLayout(indented, BoxLayout.Y_AXIS));
	    indented.add(change);
	    indented.add(Box.createVerticalStrut(12));
	    indented.add(oneOf);
	    indented.add(allOf);
	    indented.add(Box.createVerticalStrut(12));
	    indented.add(tscore);
	    indented.add(trend);
	    indented.add(dscore);
	    indented.add(overlap);

	    JPanel vert = new JPanel(new BorderLayout());
	    vert.add(markSig, BorderLayout.NORTH);
	    vert.add(Box.createHorizontalStrut(40), BorderLayout.WEST);
	    vert.add(indented, BorderLayout.CENTER);

	    getContentPane().add(vert, BorderLayout.CENTER);
	    getContentPane().add(Box.createVerticalStrut(14), BorderLayout.NORTH);
	    getContentPane().add(Box.createVerticalStrut(20), BorderLayout.SOUTH);
	    getContentPane().add(Box.createHorizontalStrut(20), BorderLayout.EAST);
	    getContentPane().add(Box.createHorizontalStrut(20), BorderLayout.WEST);

	    pack();
	    show();
	}
    }

    // class SignificanceRule?  method?

}
