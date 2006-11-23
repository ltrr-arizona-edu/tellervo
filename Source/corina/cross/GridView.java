package corina.cross; // FIXME: rename to corina.crossdate

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.MouseAdapter;

import corina.Element;
import corina.Sample;
import corina.core.App;
import corina.editor.Editor;
import corina.graph.GraphWindow;
import corina.gui.Layout;
import corina.ui.I18n;
import corina.util.PopupListener;

public class GridView extends JPanel {

	// TODO:
	// -- javadoc (class, c'tor)
	// -- popup:
	// ---- graph this cross
	// ---- jump to this cross (need CrossdateWindow ref, method there)
	// ------ jumpToCrossdate(String fixed, String moving)?
	// ---- graph this sample
	// ---- open this sample
	// -- PERF: it's really slow ... (why?)
	// -- move zoomer to bottom (why's it broken?)
	// ---- add icons?  label?
	// ---- scale shouldn't be static anywhere!
	// -- graph-all button
	// -- map-all button
	// -- double-click on cell = jump to this crossdate
	// -- default should be "yes, hilite sig scores with color ..."
	// -- delete GridFrame.java (but make sure it's empty)
	// FUTURE:
	// -- (grid.java): a cell is-sig if ANY of its (visible) crosses are sig.
	// -- (grid.java): respect sequence's algorithms
	// -- (grid.java): old way is (seq.fixed+seq.moving);
	//    should it really be fixed on left side, moving on top?
	//    (i.e., assymetric grids)
	// -- exporting?  do i have that?

	public GridView(Sequence sequence) {
		// make a grid from sequence, pass it to constructor...
		this(new Grid(sequence));		
	}
	
	public GridView(Grid thegrid) {
		grid = thegrid;

		setLayout(new BorderLayout());

		initTable();
		add(scroll, BorderLayout.CENTER);

		// where to put?
		recomputeFont();

		// TODO: add icons?  label?
		final JSlider zoomer = new JSlider(50, 150, 100); // use zoom=value/100
		zoomer.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scale = zoomer.getValue() / 100f;
				recomputeFont();
				scroll.revalidate();
				scroll.repaint();

				// update row height / col width, too.
				setCellSizes();
			}
		});

		JButton graphAll = new JButton("Graph All");
		graphAll.setEnabled(false); // WRITEME
		JButton mapAll = new JButton("Map All");
		mapAll.setEnabled(false); // WRITEME

		JPanel buttons = Layout.buttonLayout(graphAll, mapAll, null); // , zoomer);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		add(buttons, BorderLayout.SOUTH);

		add(zoomer, BorderLayout.NORTH); // ???

		// popup
		/*
		JPopupMenu popup = new JPopupMenu();
		popup.add(new JMenuItem("Graph this Crossdate")); // WRITEME: implement
		popup.add(new JMenuItem("Jump to this Crossdate")); // WRITEME: implement
		popup.addSeparator();
		popup.add(new JMenuItem("Graph this Sample")); // WRITEME: implement
		popup.add(new JMenuItem("Open this Sample")); // WRITEME: implement
		*/

		// TODO: no, i like 2 popup menus better.  they are "context"
		// menus, after all.
		
		final GridViewPopup gridViewPopup = new GridViewPopup();
		
		table.addMouseListener(new PopupListener(gridViewPopup) {
		    public void showPopup(MouseEvent e) {
		    	// if table, and this row not selected, select this row
		    	if (e.getSource() instanceof JTable) {
		    	    JTable table = (JTable) e.getSource();
		    	    int row = table.rowAtPoint(e.getPoint());
		    	    int col = table.columnAtPoint(e.getPoint());
		    	    if (row!=-1 && !table.isRowSelected(row))
		    	    	table.setRowSelectionInterval(row, row);
		    	    if (col!=-1 && !table.isColumnSelected(col))
		    	    	table.setColumnSelectionInterval(col, col);
		    	}
		    	
		    	// ok, we know what's selected. 
		    	Grid.Cell cell = grid.getCell(table.getSelectedRow(), table.getSelectedColumn());
		    	
		    	gridViewPopup.disableAll();
		    	
		    	if(cell instanceof Grid.HeaderCell || cell instanceof Grid.HeaderRangeCell) {
		    		if(((Grid.HeaderCell)cell).getFixed() != null)
		    			gridViewPopup.setPopupForSample();
		    	}
		    	else if(cell instanceof Grid.CrossCell) {
		    		
		    		// we can't do this on saved grids, alas :(
		    		if(((Grid.CrossCell)cell).getFixed() != null)
		    			gridViewPopup.setPopupForCross();
		    	}
		    	else
		    		return; // no popups on these weird cells

		    	// show popup
		    	if (gridViewPopup != null)
		    	    gridViewPopup.show(e.getComponent(), e.getX(), e.getY());
		    }
		});

		/*table.addMouseListener(new PopupListener(popup) {
			public void showPopup(MouseEvent e) {
				// WRITEME: select this cell
				// (and make sure super's selection doesn't take over?)

				// WRITEME: dim/undim stuff

				super.showPopup(e);
			}
		});
		*/
	}

	// popup menu
	private class GridViewPopup extends JPopupMenu {
		private JMenuItem graph = new JMenuItem("Graph this Crossdate"); 
		private JMenuItem jump = new JMenuItem("Jump to this Crossdate");
		private JMenuItem graph_sample = new JMenuItem("Graph this Sample");
		private JMenuItem open_sample = new JMenuItem("Open this Sample"); 
		
		public void setPopupForCross() {
			graph.setEnabled(true);
			jump.setEnabled(true);
		}

		public void setPopupForSample() {
			graph.setEnabled(false);
			jump.setEnabled(false);
			graph_sample.setEnabled(true);
			open_sample.setEnabled(true);
		}
		
		public void disableAll() {
			graph.setEnabled(false);
			jump.setEnabled(false);
			graph_sample.setEnabled(false);
			open_sample.setEnabled(false);
		}
		
		public GridViewPopup() {
			
			graph.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// get the cross cell...
					Grid.CrossCell cross = (Grid.CrossCell) grid.getCell(
							table.getSelectedRow(), table.getSelectedColumn());					

					// make graph
					List list = new ArrayList();
					list.add(new Element((String)cross.getFixed().meta.get("filename")));
					list.add(new Element((String)cross.getMoving().meta.get("filename")));
					
					new GraphWindow(list);
				}
			});
			jump.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// TODO: get fixed
					// TODO: get moving (selection)
					// TODO: crossdateWindow.jumpToCrossdate(f,m)?
					// NEED: a ref to the CDW here
					// NEED: that method in CDW
				}
			});
			
			graph_sample.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// get the header cell...
					Grid.HeaderCell header = (Grid.HeaderCell) grid.getCell(
							table.getSelectedRow(), table.getSelectedColumn());					

					// make graph
					new GraphWindow(header.getFixed());
				}
			});
			
			open_sample.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// get the header cell...
					Grid.HeaderCell header = (Grid.HeaderCell) grid.getCell(
							table.getSelectedRow(), table.getSelectedColumn());					

					// get sample, put in editor
					new Editor(header.getFixed());
				}
			});
			
			add(graph);
			add(jump);
			addSeparator();
			add(graph_sample);
			add(open_sample);
		}
	}
	
	private Grid grid;

	private JTable table;

	// HACK!  fixme.
	private float scale = 1.0f;

	// taken directly from GridFrame.java
	private void initTable() {
		// make a table out of this grid
		table = new JTable(new GridTableModel());

		// 0 pixels between cells
		table.setIntercellSpacing(new Dimension(0, 0));

		// cell-selection only
		table.setRowSelectionAllowed(false);
		table.setCellSelectionEnabled(true);

		// set cell height/width from Grid
		setCellSizes();

		// no top-header
		table.setTableHeader(null);

		// (i don't remember why i need this; do i need this to keep
		// it from being fit-to-width?  or do i need it at all?)
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		// renderer -- (same as for printer, coincidentally)
		table.setDefaultRenderer(Object.class, new GridRenderer());

		// don't show gridlines
		table.setShowGrid(false);

		// respond to double-clicks
		// LATER: put in the popup menu first, then
		// figure out which of those ops you want to make double-click.
		// MOVE ME: lift this out of initTable -- it's table, yeah,
		// but it belongs with event stuff.
		/*
		 table.addMouseListener(new MouseAdapter() {
		 public void mouseClicked(MouseEvent e) {
		 if (e.getClickCount() == 2) {
		 // get the (row,col) of the click
		 int row = table.rowAtPoint(e.getPoint());
		 int col = table.columnAtPoint(e.getPoint());

		 // figure out what samples are there
		 // (REFACTOR: LoD says this should be in grid:
		 // grid.getElement(i)?)
		 Element e1 = (Element) grid.getFiles().get(row-1);
		 Element e2 = (Element) grid.getFiles().get(col-1);

		 // make a graph
		 List list = new ArrayList(2);
		 list.add(e1);
		 list.add(e2);
		 new GraphWindow(list);
		 }
		 }
		 });
		 */

		// put the table in a scroller
		scroll = new JScrollPane(table);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	}

	private void setCellSizes() {
		int height = (int) (Grid.getCellHeight() * scale) + 2;
		int width = (int) (Grid.getCellWidth() * scale) + 2;
		table.setRowHeight(height);
		for (int i = 0; i < table.getColumnCount(); i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(width);
	}

	private JScrollPane scroll;

	private Font cellFont;

	private void recomputeFont() {
		String requestedFont = App.prefs.getPref("corina.grid.font");
		Font origFont;
		if (requestedFont == null)
			origFont = new JTable().getFont(); //  Font(); // g.getFont();
		else
			origFont = Font.decode(requestedFont);

		cellFont = origFont.deriveFont(origFont.getSize() * scale);
	}

	// cell renderer
	private class GridRenderer extends JComponent implements TableCellRenderer {
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			// set myself, return myself
			cell = (Grid.Cell) value;
			hilite = isSelected;
			back = table.getSelectionBackground();
			return this;
		}

		private Color back;

		private boolean hilite;

		private Grid.Cell cell;

		public void paintComponent(Graphics g) {
			// set font: get original font, and scale it
			// PERF: new font each time is bad!

			// this cell is selected -> hilite
			if (hilite) {
				g.setColor(back);
				g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
				g.setColor(Color.black); // FIXME: fore
			}

			/*
			 CELL:
			 -- print() should be paint(), or even draw()
			 -- i won't need XML any more.  get rid of that.  (loading still?)
			 -- print() should take graphics, not graphics2d
			 -- take "override background/foreground" args?
			 */

			g.setFont(cellFont);
			cell.print((Graphics2D) g, 0, 0, getWidth(), getHeight(), scale);
		}
	}
	
	// pass our grid format on to our grid cells...
	public void setGridFormat(int gfN) {
		grid.setFormat(gfN);
	}

	// table model for the grid.
	// EXCELLENT: this is great evidence for why Table shouldn't
	// extend AbstractTableModel! -- REFACTOR (over there)
	private class GridTableModel extends AbstractTableModel {
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

	/**
	 Make a printable object for this crossdating grid.

	 @return a Printable for this Grid
	 */
	public Pageable print(PageFormat pageFormat) {
		return grid.makeHardcopy(pageFormat);
	}

	/**
	 Returns a title suitable for this view.  It'll probably be a
	 localized version of something like "Crossdating Grid".

	 @return a title for this view
	 */
	public String toString() {
		return I18n.getText("crossdating_grid");
	}
}
