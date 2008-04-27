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

package edu.cornell.dendro.corina.cross;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import edu.cornell.dendro.corina.Build;
import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.gui.FileDialog;
import edu.cornell.dendro.corina.gui.PrintableDocument;
import edu.cornell.dendro.corina.gui.SaveableDocument;
import edu.cornell.dendro.corina.gui.UserCancelledException;
import edu.cornell.dendro.corina.gui.XFrame;
import edu.cornell.dendro.corina.gui.menus.FileMenu;
import edu.cornell.dendro.corina.gui.menus.HelpMenu;
import edu.cornell.dendro.corina.gui.menus.WindowMenu;
import edu.cornell.dendro.corina.prefs.PrefsEvent;
import edu.cornell.dendro.corina.prefs.PrefsListener;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.ui.Alert;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.util.Overwrite;

/*
 TODO:
 -- this class is still used by CanOpener; fix that class to use CrossdateWindow(Grid)
 (WRITEME!), then delete this class.

 -- this class will go away, since grid will be merely a crossdate seq view
 -- refactor this into GridView, a JPanel
 */
public class GridFrame extends XFrame implements SaveableDocument, PrintableDocument, PrefsListener {
	// gui
	private JTable output;

	// data
	private Grid grid = null;

	// saving -- (this seems like it should be higher...)
	private String filename = null;

	// saveabledocument
	public boolean isSaved() { // don't worry about deleting grids, for now
		return true; // fixme
	}

	// saveabledocument -- yes, we can use save as...
	public boolean isNameChangeable() {
		return true;
	};

	/*
	  REFACTOR: make Grid Saveable(?), and move all of this up to XFrame, which
	  i should rename as DocumentFrame(?).  it's the same for samples, grids, graphs, ...
	 */
	public void save() {
		// check filename
		if (filename == null) {
			try {
				filename = FileDialog.showSingle("Save");

				// try up here, try down there.  can these be merged?  (but there's an if-stmt...)

				// check for already-exists
				Overwrite.overwrite(filename); // should return FAILURE -- how?

			} catch (UserCancelledException uce) {
				return; // this should return FAILURE, too -- solution: save() throws UCE
			}
		}

		// save!
		try {
			grid.save(filename);
		} catch (IOException ioe) {
			Alert.error("Error saving", "Error: " + ioe.getMessage());
		}
	}

	public String getDocumentTitle() {
		return I18n.getText("grid") + ": " + filename;
	}

	public void setFilename(String fn) {
		filename = fn;
	}

	public String getFilename() {
		return filename;
	}

	// table model for the grid
	static class GridTableModel extends AbstractTableModel {
		private Grid grid;

		public GridTableModel(Grid grid) {
			this.grid = grid;
		}

		public int getColumnCount() {
			return (grid == null ? 0 : grid.size() + 1);
		}

		public int getRowCount() {
			return (grid == null ? 0 : grid.size() + 1);
		}

		public Object getValueAt(int row, int col) {
			return grid.getCell(row, col);
		}
	}

	// BUG: static!
	private static float scale = Float.parseFloat(App.prefs.getPref("corina.grid.scale", "1.0"));

	// cell renderer
	static class GridRenderer extends JComponent implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			// set myself, return myself
			cell = (Grid.Cell) value;
			return this;
		}

		private Grid.Cell cell;

		@Override
		public void paintComponent(Graphics g) {
			// set font: get original font, and scale it
			Font origFont = (App.prefs.getPref("corina.grid.font") == null ? g.getFont() : Font.decode(App.prefs
					.getPref("corina.grid.font")));
			Font scaledFont = origFont.deriveFont(origFont.getSize() * scale);
			// System.out.println("new font t=" + System.currentTimeMillis()); -- for debugging
			g.setFont(scaledFont);
			// FIXME: new font each time seems even MORE inefficient!
			// better: in refresh() just do component.setFont(...), and i'll pick it up automatically, right?

			// ((Graphics2D) g).scale(scale, scale);
			// FIXME: agh!  just call g2.scale(scale, scale) -- grid cells shouldn't have to
			// scale themselves.  but scale() doesn't scale the text as nicely as i do, so
			// until i figure that out, i'll keep passing in my own scale.

			// call the printing method (REFACTOR: rename method?  it's not just for printing, anymore... -- draw())
			cell.print((Graphics2D) g, 0, 0, getWidth(), getHeight(), scale);
		}
	}

	// PrintableDocument
	public Object getPrinter(PageFormat pf) {
		return grid.makeHardcopy(pf);
	}

	public String getPrintTitle() {
		return I18n.getText("crossdating_grid");
	}

	// c'tor helper
	private void initTable() {
		// make a table out of this grid
		output = new JTable(new GridTableModel(grid));

		// 0 pixels between cells
		output.setIntercellSpacing(new Dimension(0, 0));

		// cell-selection only
		output.setRowSelectionAllowed(false);

		// set cell height/width from Grid
		output.setRowHeight((int) (Grid.getCellHeight() * scale) + 2);
		for (int i = 0; i < output.getColumnCount(); i++) {
			int width = (int) (Grid.getCellWidth() * scale) + 2;
			output.getColumnModel().getColumn(i).setPreferredWidth(width);
		}

		// no top-header
		output.setTableHeader(null);

		// (i don't remember why i need this; do i need this to keep
		// it from being fit-to-width?  or do i need it at all?)
		output.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// renderer -- use same as for printer
		output.setDefaultRenderer(Object.class, new GridRenderer());

		// don't show gridlines
		output.setShowGrid(false);

		// respond to double-clicks
		output.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					// get the (row,col) of the click
					int row = output.rowAtPoint(e.getPoint());
					int col = output.columnAtPoint(e.getPoint());

					// figure out what samples are there
					// (REFACTOR: LoD says this should be in grid:
					// grid.getElement(i)?)
					Element e1 = grid.getElements().get(row - 1);
					Element e2 = grid.getElements().get(col - 1);

					// make a graph
					ElementList list = new ElementList();
					list.add(e1);
					list.add(e2);
					new GraphWindow(list);
				}
			}
		});

		// put the table in a scroller
		JScrollPane scroller = new JScrollPane(output);
		getContentPane().add(scroller, BorderLayout.CENTER);
		// REFACTOR: extract GridPanel, GridFrame; then stuffing the panel into a CrossFrame is trivial.
		// OR: it's just a JTable, right?  would GridComponent (extends JTable) be better?
	}

	// used by elementspanel -- shortcut for new gridframe(new grid(list))
	public GridFrame(ElementList s) {
		grid = new Grid(s);
		grid.run(); // change cursor to WAIT?
		init();
	}

	// used by canopener
	public GridFrame(Grid g) {
		grid = g;
		// move grid.run() to init(), and make run() a no-op if already run?
		init();
	}

	// never used -- could be, but isn't
	// TODO: catch runtime exceptions here, and use Bug
	public GridFrame() {
		try {
			// get args
			ElementList samples = FileDialog.showMulti(I18n.getText("grid"));

			grid = new Grid(samples);
			grid.run(); // change cursor to WAIT?
			init();
		} catch (UserCancelledException uce) {
			dispose();
			return;
		}
	}

	private void init() {
		setTitle(I18n.getText("crossdating_grid") + " - " + Build.VERSION + " " + Build.TIMESTAMP);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		initTable();

		{
			JMenuBar menubar = new JMenuBar();

			menubar.add(new FileMenu(this));
			// menubar.add(edit); // WRITEME -- prefs only?  dummy undo/etc.?
			menubar.add(new GridViewMenu());
			if (App.platform.isMac())
				menubar.add(new WindowMenu(this));
			menubar.add(new HelpMenu());

			setJMenuBar(menubar);
		}

		App.prefs.addPrefsListener(this);

		pack();
		setSize(new Dimension(640, 480));
		show();
	}

	private class GridViewMenu extends JMenu {
		GridViewMenu() {
			super(I18n.getText("view"));

			JMenuItem graph = Builder.makeMenuItem("graph_all");
			graph.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					new GraphWindow(grid.getElements());
				}
			});
			add(graph);

			// ---
			addSeparator();

			// NEED NEW ABSTRACTION: ZOOM
			// -- placard component -- override jscrollpane?  jscrollbar?
			// -- menuitems: zoom in, zoom out, normal/100%, specific values (50, 75, 100, 125, 150, 200, 400?)
			// -- (consistent everywhere!)
			// -- "other..." value (dialog) for other types of user-zooms (like drag-area)
			// --

			// also, the grid should have the option of showing page-breaks (horiz+vert).
			// view -> {show,hide} page breaks.
			// would it be easier to make gridcomponent not-a-jtable, then?

			// zoom in
			JMenuItem zoomIn = Builder.makeMenuItem("zoom_in");
			zoomIn.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// increase by 0.1
					scale += 0.1;

					// set pref
					App.prefs.setPref("corina.grid.scale", Float.toString(scale));
				}
			});
			add(zoomIn);

			// zoom out
			JMenuItem zoomOut = Builder.makeMenuItem("zoom_out");
			zoomOut.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// decrease by 0.1
					scale -= 0.1;

					// set pref
					App.prefs.setPref("corina.grid.scale", Float.toString(scale));
				}
			});
			add(zoomOut);
		}
	}

	// PrefsListener
	public void prefChanged(PrefsEvent e) {
		if (!e.getPref().equals("corina.grid.scale"))
			return;
		// re-read scale
		scale = Float.parseFloat(App.prefs.getPref("corina.grid.scale", "1.0"));

		// reset sizes
		output.setRowHeight((int) (Grid.getCellHeight() * scale) + 2);
		int w = (int) (Grid.getCellWidth() * scale) + 2;
		for (int i = 0; i < output.getColumnCount(); i++) {
			output.getColumnModel().getColumn(i).setPreferredWidth(w);
		}

		// redraw?  sure.
		repaint();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		App.prefs.removePrefsListener(this);
	}
}