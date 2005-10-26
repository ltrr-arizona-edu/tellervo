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

package corina.gui;

import corina.Sample;
import corina.SampleListener;
import corina.SampleEvent;
import corina.Element;
import corina.MetadataTemplate;
import corina.MetadataTemplate.Field;
import corina.editor.Editor;
import corina.util.Sort;
import corina.util.PopupListener;
import corina.ui.Alert;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Color;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CannotRedoException;

/**
 A JPanel for displaying (and editing) the Elements of a List.

 <h2>Left to do</h2>
 <ul>
 <li>remove me: use the generic Browser components instead!

 <li>finish implementing the popup menu
 <li>make the popup menu separate, so it can be used as a menubar menu, too
 <li>make changes to the List dirty the document
 </ul>

 @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
 @version $Id$
 */
public class ElementsPanel extends JPanel implements SampleListener {

	// data
	private List elements;

	private Sample sample = null;

	// gui
	private JPopupMenu popup;

	private JTable table;

	// --- SampleListener ----------------------------------------
	public void sampleRedated(SampleEvent e) {
	}

	public void sampleDataChanged(SampleEvent e) {
	}

	public void sampleMetadataChanged(SampleEvent e) {
	}

	public void sampleElementsChanged(SampleEvent e) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	// --- SampleListener ----------------------------------------

	// randomness...
	public void update() {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	// --- DropAdder ----------------------------------------
	private class DropLoader implements DropTargetListener {
		public void dragEnter(DropTargetDragEvent event) {
			event.acceptDrag(DnDConstants.ACTION_MOVE);
		}

		public void dragOver(DropTargetDragEvent event) {
		} // do nothing

		public void dragExit(DropTargetEvent event) {
		} // do nothing

		public void dropActionChanged(DropTargetDragEvent event) {
		} // do nothing

		public void drop(DropTargetDropEvent event) {
			try {
				Transferable transferable = event.getTransferable();

				// we accept only filelists
				if (transferable
						.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					event.acceptDrop(DnDConstants.ACTION_MOVE);
					Object o = transferable
							.getTransferData(DataFlavor.javaFileListFlavor);
					List l = (List) o; // a List of Files

					for (int i = 0; i < l.size(); i++) {
						elements.add(new Element(((File) l.get(i)).getPath()));
						// fire update?
					}
					event.getDropTargetContext().dropComplete(true);
				} else {
					event.rejectDrop();
				}
			} catch (IOException ioe) {
				// handle error?
				event.rejectDrop();
			} catch (UnsupportedFlavorException ufe) {
				// handle error?
				event.rejectDrop();
			}
		}
	}

	// --- DropAdder ----------------------------------------

	// --- ContextPopup ----------------------------------------
	private class ContextPopup extends JPopupMenu {
		public ContextPopup() {
			// Open
			JMenuItem open = new JMenuItem("Open");
			open.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent ae) {
					// get selected element
					int i = table.getSelectedRow(); // BUG: if nothing's selected, nothing gets selected, returns -1 here
					Element e = (Element) elements.get(i);

					// load it
					Sample s = null;
					try {
						s = new Sample(e.getFilename());
					} catch (IOException ioe) {
						Alert.error("Error Loading Sample",
								"Can't open this file: " + ioe.getMessage());
						return;
					}

					// open it
					new Editor(s);
				}
			});
			super.add(open);

			// ---
			addSeparator();

			// Change directory
			JMenuItem changeDir = new JMenuItem("Change directory...");
			changeDir.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent ae) {
					// figure out what the base directory is for the samples
					String prefix = ((Element) elements.get(0)).getFilename();
					for (int i = 1; i < elements.size(); i++) {

						// crop prefix by directories until it really is a prefix
						while (!((Element) elements.get(i)).getFilename()
								.startsWith(prefix)) {
							int slash = prefix.lastIndexOf(File.separatorChar);
							if (slash == -1) {
								prefix = "";
								break;
							}
							prefix = prefix.substring(0, slash + 1);
						}
					}

					// ask user what the new directory will be
					String target = (String) JOptionPane.showInputDialog(null,
							"Enter a new directory:", "Choose new directory",
							JOptionPane.QUESTION_MESSAGE, null, /* ??? */
							null, prefix);

					// user aborted
					if (target == null || target.equals(prefix))
						return;

					// make sure target now holds a prefix which ends with File.separator
					if (!target.endsWith(File.separator))
						target += File.separatorChar;

					// change all filenames to the new directory (s/prefix/target/).
					// elements are now immutable, so make a new one from the old name.
					for (int i = 0; i < elements.size(); i++) {
						Element oldEl = (Element) elements.get(i);
						String newFilename = target
								+ oldEl.getFilename()
										.substring(prefix.length());
						Element newEl = new Element(newFilename, oldEl
								.isActive());
						elements.set(i, newEl);
					}

					// fire event so table gets changed
					((AbstractTableModel) table.getModel())
							.fireTableDataChanged();
				}
			});
			super.add(changeDir);
		}
	}

	// --- ContextPopup ----------------------------------------

	// given: list of elements
	public ElementsPanel(List el) {
		this(null, el);
	}

	// given: editor
	public ElementsPanel(Editor e) {
		this(e.getSample(), e.getSample().elements);
	}

	// given: Sample, and list of Elements (well, this never happens,
	// but it's the common subset.)
	private ElementsPanel(Sample s, List el) {
		// boilerplate
		setLayout(new BorderLayout());

		// data
		if (s == null) {
			elements = el;
		} else {
			this.sample = s;
			if (sample.elements == null)
				sample.elements = new ArrayList();
			elements = sample.elements;
		}

		// table
		table = new JTable();
		setView(VIEW_FILENAMES); // initial view
		addClickToSort();
		if (elements != null && elements.size() > 0)
			table.setRowSelectionInterval(0, 0);
		JScrollPane scroller = new JScrollPane(table,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroller, BorderLayout.CENTER);

		// popup, mouselistener for it
		popup = new ContextPopup();
		MouseListener popupListener = new PopupListener() {
			public void showPopup(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		};
		addMouseListener(popupListener); // for clicking on empty space below the entries
		table.addMouseListener(popupListener); // for clicking on table entries

		// drag-n-drop
		DropLoader dropLoader = new DropLoader();
		DropTarget dt1 = new DropTarget(this, dropLoader);
		DropTarget dt2 = new DropTarget(table, dropLoader);
	}

	public void removeSelectedRows() {
		// save backup -- only a shallow copy (but that's good)
		final List save = new ArrayList();
		save.addAll(elements);
		/*
		 DESIGN: saving a before-list and an after-list means the
		 amount of change (=speed/memory) is proportional to the
		 amount of data, not the size of the change, which is what
		 the user expects -- especially since the user will almost
		 always remove one element.

		 now, if i later want to handle the "500 elements, 490 of
		 them removed" case, i can post a second class of edit for
		 that.  but that will happen rarely (or never), so it'll
		 never be worth the effort.
		 */

		final int rows[] = table.getSelectedRows();
		int deleted = 0; // number of rows already deleted
		for (int i = 0; i < rows.length; i++) { // remove those rows
			elements.remove(rows[i] - deleted);
			deleted++;
		}

		// update
		if (sample != null) {
			sample.setModified();
			sample.fireSampleElementsChanged();
			sample.fireSampleMetadataChanged(); // so title gets updated (modified-flag)

			// add undoable edit -- not the most efficient way, but not bad, either.
			sample.postEdit(new AbstractUndoableEdit() {
				List before = save, after;

				public void undo() throws CannotUndoException {
					after = new ArrayList();
					after.addAll(elements);
					elements.clear();
					elements.addAll(before);
					update();
				}

				public void redo() throws CannotRedoException {
					elements.clear();
					elements.addAll(after);
					update();
				}

				public boolean canRedo() {
					return true;
				}

				public String getPresentationName() {
					return "Remove"; // (rows.length==1 ?  "Remove Element" : "Remove Elements");
				}
			});
		}

		System.out.println("updating... -- BROKEN!");
		update();
		// what the fuck is going on here?  why why oh why?
	}

	private List fields;

	public final static int VIEW_FILENAMES = 0;

	public final static int VIEW_STANDARD = 1;

	public final static int VIEW_ALL = 2;

	public void setView(int view) {
		switch (view) {
		case VIEW_FILENAMES:
			fields = new ArrayList();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			break;
		case VIEW_STANDARD: {
			// (later, this will use BrowserComponent, so this
			// won't be needed at all)

			fields = new ArrayList();

			// these are the "preview" fields
			final String PREVIEW_FIELDS[] = new String[] { "unmeas_pre",
					"unmeas_post", "species", "sapwood", "terminal", "quality", };

			// add all fields in |PREVIEW_FIELDS| to |fields|
			Iterator i = MetadataTemplate.getFields();
			while (i.hasNext()) {
				Field f = (Field) i.next();

				for (int j = 0; j < PREVIEW_FIELDS.length; j++) {
					if (f.getVariable().equals(PREVIEW_FIELDS[j]))
						fields.add(f);
				}
			}

			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			break;
		}
		case VIEW_ALL: {
			fields = new ArrayList();

			// add all fields to |fields|
			Iterator i = MetadataTemplate.getFields();
			while (i.hasNext()) {
				Field f = (Field) i.next();
				fields.add(f);
			}

			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			break;
		}
		default:
			throw new IllegalArgumentException();
		}

		table.setModel(new ElementsTableModel(elements, fields));

		// renderer
		table.getColumnModel().getColumn(0).setCellRenderer(
				new ElementsTableModel.FilenameRenderer(table));
		/*
		 table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
		 public Component getTableCellRendererComponent(JTable table, Object value,
		 boolean isSelected, boolean hasFocus,
		 int row, int column) {
		 Component c = super.getTableCellRendererComponent(table, value,
		 isSelected, hasFocus,
		 row, column);
		 Color back = (isSelected ? table.getSelectionBackground() : table.getBackground());

		 if (row % 2 == 0)
		 back = new Color(back.getRed() - 16,
		 back.getGreen() - 16,
		 back.getBlue());
		 c.setBackground(back);
		 return c;
		 }
		 });
		 */

		// editor
		final JPanel panel = new JPanel(new BorderLayout());
		final JCheckBox chx = new JCheckBox();
		final JLabel lab = new JLabel();
		panel.add(chx, BorderLayout.WEST);
		panel.add(lab, BorderLayout.CENTER);
		lab.setFont(table.getFont());
		chx.setForeground(table.getForeground());
		lab.setForeground(table.getForeground());
		chx.setBackground(table.getBackground());
		lab.setBackground(table.getBackground());
		lab.setOpaque(true);
		chx.setOpaque(true);
		table.getColumnModel().getColumn(0).setCellEditor(
				new DefaultCellEditor(chx) {
					public Component getTableCellEditorComponent(JTable table,
							Object value, boolean isSelected, int row,
							int column) {
						Element e = (Element) value;
						chx.setSelected(e.active);
						lab.setText(new File(e.getFilename()).getName()); // filename only (not fq)

						Color fore = (isSelected ? table
								.getSelectionForeground() : table
								.getForeground());
						Color back = (isSelected ? table
								.getSelectionBackground() : table
								.getBackground());

						/*
						 // light-blue-ish
						 if (row % 2 == 0)
						 back = new Color(back.getRed() - 16, back.getGreen() - 16, back.getBlue());
						 */

						chx.setForeground(fore);
						lab.setForeground(fore);
						chx.setBackground(back);
						lab.setBackground(back);

						return panel;
					}
				});

		// set columns: for each column use popup if suggested values present
		for (int i = 0; i < fields.size(); i++)
			if (((Field) fields.get(i)).values != null) {
				JComboBox popup = new JComboBox(((Field) fields.get(i)).values);
				popup.setEditable(true);
				table.getColumnModel().getColumn(i + 2).setCellEditor(
						new DefaultCellEditor(popup));
			}
	}

	// this method is pretty (was: unbelievably) ugly, thanks to java's lack of closures.  *sigh*
	private int lastSortCol = -1;

	private void addClickToSort() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 1)
					return;
				int col = table.getColumnModel().getColumnIndexAtX(e.getX());

				Element first = (Element) elements.get(0);
				Element last = (Element) elements.get(elements.size() - 1);

				switch (col) {
				case 0: // filename
				{
					boolean reverse = (lastSortCol == col && first.filename
							.compareTo(last.filename) < 0);
					Sort.sort(elements, "filename", reverse);
					break;
				}

				case 1: // range
				{
					// IDEA: this reverse is getting pretty popular ... could it be integrated into my sort() wrapper?
					boolean reverse = (lastSortCol == col && first.getRange()
							.compareTo(last.getRange()) < 0);
					Sort.sort(elements, "range", reverse);
					break;
				}

				default: // details
					// unfortunately, i don't think i can use sort.sort() here because i'm not getting a field,
					// i'm calling a method on a field.  i could write another sort() wrapper to take a :key
					// method, like lisp does, and implement it in an anonymous class here, but that's more
					// work than just sorting by hand.  gah.
					String key = ((Field) fields.get(col - 2)).getVariable();
					Comparable v0 = (Comparable) first.details.get(key);
					Comparable vn = (Comparable) last.details.get(key);
					boolean reverse = (lastSortCol == col && v0.compareTo(vn) < 0);
					Collections.sort(elements, new Metasorter(key, reverse));
				}

				lastSortCol = col;
			}
		});
	}

	// (if you see gosling, kick him for me for not giving java closures)
	private static class Metasorter implements Comparator { // by meta field
		private boolean rev;

		private String field;

		public Metasorter(String field, boolean reverse) {
			rev = reverse;
			this.field = field;
		}

		public int compare(Object o1, Object o2) {
			Object v1 = ((Element) o1).details.get(field); // what about null HERE?
			Object v2 = ((Element) o2).details.get(field);
			if (v1 == null && v2 != null) // deal with nulls ... ick
				return +1;
			else if (v1 != null && v2 == null)
				return -1;
			else if (v1 == null && v2 == null)
				return 0;
			int x = ((Comparable) v1).compareTo((Comparable) v2);
			return (rev ? -x : x);
		}
	}
}
