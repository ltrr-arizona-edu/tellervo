package edu.cornell.dendro.corina.cross;

import edu.cornell.dendro.corina.Sample;
import edu.cornell.dendro.corina.Element;
// IDEA: what if i got rid of Element?  it's either a sample, or a
// filename.  no, that's bad, i wouldn't be able to load massive
// amounts of metadata, as browser requires...
import edu.cornell.dendro.corina.graph.GraphWindow;
import edu.cornell.dendro.corina.editor.Editor;
import edu.cornell.dendro.corina.ui.Builder;
import edu.cornell.dendro.corina.ui.I18n;
import edu.cornell.dendro.corina.gui.Layout;
import edu.cornell.dendro.corina.index.DecimalRenderer;
import edu.cornell.dendro.corina.util.PopupListener;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.print.Printable;

/*
  a view of a crossdating table.

  made from a sequence.  the fixed samples go in a popup menu at the
  top.  the moving samples go down the rows of the table.

  TODO: everything...
  -- put "moving" label above the table?
  -- extract saveColumnWidths()/restoreColumnWidths()
  -- javadoc
  -- allow only single-row-selection?  i think multiple-row serves no
     purpose here, except to be misleading.  (it *could* be, though,
     if "graph" supported multiples -- nah.)
  -- fixed-popup shouldn't show a scrollbar, unless it really needs it.
     the default of "more than 8 rows" is not "really needs it": it's an
     annoyance to the user.
  -- right-click, "jump to this crossdate": implement
  -- size columns appropriately: first column should be ~50%
  -- don't allow reordering columns
  -- hook up double-click to something ... graph?
     hmm, i think i like "show this crossdate" better.
  -- get "which scores to display" from crossdate
  -- graph button
  -- map button
  -- mark significant scores with hiliting, like grid does, but for
     individual scores.
  -- c'tor shouldn't throw anything
  -- need to expose exporting through public methods
  -- need to expose copy-to-clipboard through public methods
  -- dim map button if no maps available
  -- (better/any) error handling!
  -- allow drops onto the "fixed" popup to add samples
  -- allow drops onto the "moving" table to add samples
  -- manual: crossdating tables, what they are / how to make
  -- manual: crossdating tables, exporting (text, html?), esp.
     how to put a table in a word doc (copy [as text], paste, table ->
     convert -> convert text to table...; confirm "number of columns"
     and "number of rows" are correct, and "separate text at (*) tabs"
     is selected.)
  -- manual: crossdating tables, printing
  -- manual: crossdating tables, saving?
  -- corina.print: "no-page-break" blocks

  -- export as text; html?; via clipboard?  be sure to copy header line, too.
*/
/**
   A panel which displays a crossdating table.

   <p>Since it is a view of a Sequence, it starts out by showing the
   table made by crossdating the first fixed sample against each of
   the moving samples.  The fixed sample is displayed in a popup menu,
   so the user can pick any of the fixed samples to use.</p>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
   @version $Id$
 */
public class TableView extends JPanel {

	private List moving;

	// this could get confusing making tables out of tables, so
	// in this file, let's call JTables "jtable", and Tables "table".
	private JTable jtable;

	// EXTRACT ME!
	private int[] saveColumnWidths() {
		TableColumnModel columns = jtable.getColumnModel();
		int n = columns.getColumnCount();
		int columnWidths[] = new int[n];
		for (int i = 0; i < n; i++)
			columnWidths[i] = columns.getColumn(i).getWidth();
		return columnWidths;
	}

	private void restoreColumnWidths(int columnWidths[]) {
		TableColumnModel columns = jtable.getColumnModel();
		int n = columnWidths.length;
		for (int i = 0; i < n; i++)
			columns.getColumn(i).setPreferredWidth(columnWidths[i]);
	}

	/**
	 Make a printable object for this crossdating table.

	 @return a Printable for this Table
	 */
	public Printable print() {
		return table.print();
	}

	private Table table;

	private JComboBox fixedPopup;

	private List fixedAsList;

	/**
	 Make a new table view for a sequence.

	 @param sequence a sequence containing the fixed and moving
	 samples to display in a table
	 @exception IOException I have no excuse: fix me
	 */
	public TableView(Sequence sequence) throws IOException {
		// put all "fixed" samples in a popup -- EXTRACT METHOD!
		fixedAsList = new ArrayList(sequence.getAllFixed());
		String names[] = new String[fixedAsList.size()];
		for (int i = 0; i < names.length; i++)
			names[i] = new Sample((String) fixedAsList.get(i)).toString();
		fixedPopup = new JComboBox(names);
		fixedPopup.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				// -- figure out what sample to use
				int selection = fixedPopup.getSelectedIndex();
				String fixed = (String) fixedAsList.get(selection);

				try {
					// -- make new Table object
					table = new Table(fixed, moving);

					// -- call table.setModel(t)
					int widths[] = saveColumnWidths();
					jtable.setModel(table);
					initRenderers();
					restoreColumnWidths(widths);
				} catch (IOException ioe) {
					System.out.println("ioe!");
				}
			}
		});

		JPanel top = Layout.flowLayoutL(new JLabel("Fixed:"), fixedPopup);
		top.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		setLayout(new BorderLayout());
		add(top, BorderLayout.NORTH);

		// first fixed one
		String fixed = (String) sequence.getAllFixed().get(0);
		moving = sequence.getAllMoving();

		// make a table.
		table = new Table(fixed, moving);
		jtable = new JTable(table) {
			public void addNotify() {
				super.addNotify();
				System.out.println("width=" + getWidth()); // 0!
				//getColumnModel().getColumn(0).setPreferredWidth(getWidth() / 2);
			}
		};
		// doesn't work: wait for addNotify()? jtable.getColumnModel().getColumn(0).setPreferredWidth(jtable.getWidth() / 2);

		// align by decimal points
		initRenderers();

		JScrollPane scroll = new JScrollPane(jtable);
		add(scroll, BorderLayout.CENTER);

		// popup menu
		jtable.addMouseListener(new PopupListener(new TableViewPopup()));

		// TODO: buttons (graph, map)
		JButton graph = Builder.makeButton("plot");
		graph.setEnabled(false);
		JButton map = Builder.makeButton("map");
		map.setEnabled(false);
		JPanel buttons = Layout.buttonLayout(graph, map, null);
		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		add(buttons, BorderLayout.SOUTH); // FIXME: use Layout for this

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	}

	private void initRenderers() {
		TableColumnModel columns = jtable.getColumnModel();

		// HACK: replace me with: Cross.getFormat(alg)
		String t = new TScore().getFormat();
		String tr = new Trend().getFormat();
		String d = new DScore().getFormat();

		// t, tr, d
		columns.getColumn(1).setCellRenderer(new DecimalRenderer(t));
		columns.getColumn(2).setCellRenderer(new DecimalRenderer(tr));
		columns.getColumn(3).setCellRenderer(new DecimalRenderer(d));

		// overlap
		columns.getColumn(4).setCellRenderer(new DecimalRenderer("000"));
	}

	/**
	 Returns a title suitable for this view.  It'll probably be a
	 localized version of something like "Crossdating Table".

	 @return a title for this view
	 */
	public String toString() {
		return I18n.getText("crossdating_table");
	}

	// popup menu
	private class TableViewPopup extends JPopupMenu {
		TableViewPopup() {
			JMenuItem graph = new JMenuItem("Graph this Crossdate");
			JMenuItem jump = new JMenuItem("Jump to this Crossdate");
			JMenuItem open = new JMenuItem("Open this Sample");

			graph.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					// get fixed
					int i = fixedPopup.getSelectedIndex();
					Element f = new Element((String) fixedAsList.get(i));

					// get moving
					int j = jtable.getSelectedRow();
					Element m = new Element((String) table.getFilenameOfRow(j));

					// make graph
					List list = new ArrayList();
					list.add(f);
					list.add(m);
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
			open.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					try {
						// get moving, make sample, put in editor
						int j = jtable.getSelectedRow();
						Sample s = new Sample((String) table
								.getFilenameOfRow(j));
						new Editor(s);
					} catch (IOException ioe) {
						// FIXME
						System.out.println("ioe!");
					}
				}
			});

			add(graph);
			add(jump);
			addSeparator();
			add(open);
		}
	}
}
