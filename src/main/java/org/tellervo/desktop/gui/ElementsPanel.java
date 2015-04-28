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

package org.tellervo.desktop.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.tellervo.desktop.Range;
import org.tellervo.desktop.editor.AbstractEditor;
import org.tellervo.desktop.editor.FullEditor;
import org.tellervo.desktop.metadata.MetadataField;
import org.tellervo.desktop.metadata.MetadataTemplate;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.FileElement;
import org.tellervo.desktop.sample.Sample;
import org.tellervo.desktop.sample.SampleEvent;
import org.tellervo.desktop.sample.SampleListener;
import org.tellervo.desktop.ui.Alert;
import org.tellervo.desktop.util.PopupListener;
import org.tellervo.desktop.util.Sort;


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

/*
 * This is particularly annoying:
 * 
 * The Elements Panel contains a list of Elements.
 * However, it really relies on metadata. So, we need to maintain a list
 * of elements and a hash from Element->BaseSample and load in the beginning.
 * Urgh!
 */

@SuppressWarnings("serial")
public class ElementsPanel extends JPanel implements SampleListener {	
	// data
	private ElementList elements;
	private Map<Element, BaseSample> elementMap;

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

	public void sampleElementsChanged(SampleEvent se) {
		((AbstractTableModel) table.getModel()).fireTableDataChanged();
	}

	// --- SampleListener ----------------------------------------

	// randomness...
	public void update() {
		// new: try and load the new sample's metadata
		// otherwise, we end up with empty stuff in our table
		// when we add new elements.
		
		for(Element e : elements) {
			// if it's already there, don't bother :)
			if(elementMap.containsKey(e))
				continue;
			
			try {
				BaseSample bs = e.loadBasic();
				
				elementMap.put(e, bs);
			} catch (IOException ioe) {
				// don't do anything; it won't show up in the hash
			}
		}
		
		// no no no! YES table data changed, you removed stuff that it POINTS TO! ACK!
		// You must re-initialize the table from scratch, with is oddly just a setView() call...
		// ((AbstractTableModel) table.getModel()).fireTableDataChanged();
		setView(currentView);		
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

		@SuppressWarnings("unchecked")
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
						elements.add(new Element(new FileElement(((File) l.get(i)).getPath())));
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
					Element e = elements.get(i);

					// load it
					Sample s = null;
					try {
						s = e.load();
					} catch (IOException ioe) {
						Alert.error("Error Loading Sample",
								"Can't open this file: " + ioe.getMessage());
						return;
					}

					// open it
					FullEditor editor = FullEditor.getInstance();
	    			editor.addSample(s);
				}
			});
			super.add(open);

			// ---
			addSeparator();

			// Change directory
			JMenuItem changeDir = new JMenuItem("Change directory...");
			changeDir.addActionListener(new AbstractAction() {
				public void actionPerformed(ActionEvent ae) {
					List<Element> flist = new ArrayList<Element>();
					
					// make a list of file elements only
					for(Element e : elements) {
						if(e.getLoader() instanceof FileElement)
							flist.add(e);
					}
					
					if(flist.isEmpty()) {
						Alert.error("Error", "Can only change directories on file elements!");
						return;
					}
					
					// figure out what the base directory is for the samples
					String prefix = ((FileElement) flist.get(0).getLoader()).getFilename();
					for (int i = 1; i < flist.size(); i++) {

						// crop prefix by directories until it really is a prefix
						while (!((FileElement) flist.get(i).getLoader()).getFilename()
								.startsWith(prefix)) {
							int slash = prefix.lastIndexOf(File.separatorChar);
							if (slash == -1) {
								prefix = "";
								break;
							}
							prefix = prefix.substring(0, slash);
						}
					}
					
					// we took off the last file separator; let's see how it works now...
					if (!prefix.endsWith(File.separator))
						prefix += File.separatorChar;					

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
					for (int i = 0; i < flist.size(); i++) {
						FileElement oldEl = (FileElement) elements.get(i).getLoader();
						String newFilename = target
								+ oldEl.getFilename()
										.substring(prefix.length());
						FileElement newEl = new FileElement(newFilename); //, oldEl.isActive());
						elements.get(i).setLoader(newEl);
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
	public ElementsPanel(ElementList el) {
		this(null, el);
	}

	// given: editor
	public ElementsPanel(AbstractEditor e) {
		this(e.getSample(), e.getSample().getElements());
	}

	// given: Sample, and list of Elements (well, this never happens,
	// but it's the common subset.)
	private ElementsPanel(Sample s, ElementList el) {
		// boilerplate
		setLayout(new BorderLayout());

		// data
		if (s == null) {
			elements = el;
		} else {
			this.sample = s;
			if (sample.getElements() == null) {
				// err... this shouldn't happen!!!!
				// should it?
				throw new UnsupportedOperationException("ElementsPanel creation for a sample with no Elements!");
				//sample.setElements(new ArrayList());
			}
			elements = sample.getElements();
		}
		
		// try stealthily loading all of our basic info
		elementMap = new HashMap<Element, BaseSample>();
		for(Element e : elements) {
			try {
				BaseSample bs = e.loadBasic();
				
				elementMap.put(e, bs);
			} catch (IOException ioe) {
				// don't do anything; it won't show up in the hash
			}
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
			@Override
			public void showPopup(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		};
		addMouseListener(popupListener); // for clicking on empty space below the entries
		table.addMouseListener(popupListener); // for clicking on table entries

		// drag-n-drop
		DropLoader dropLoader = new DropLoader();
		@SuppressWarnings("unused")
		DropTarget dt1 = new DropTarget(this, dropLoader);
		@SuppressWarnings("unused")
		DropTarget dt2 = new DropTarget(table, dropLoader);
	}

	public void removeSelectedRows() {
		// save backup -- only a shallow copy (but that's good)
		final ElementList save = new ElementList(elements);
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
			String s = elements.get(rows[i] - deleted).getName();
			System.out.println(s);
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
				ElementList before = save, after;

				@Override
				public void undo() throws CannotUndoException {
					after = new ElementList(elements);
					elements.copyFrom(before);
					update();
				}

				@Override
				public void redo() throws CannotRedoException {
					elements.copyFrom(after);
					update();
				}

				@Override
				public boolean canRedo() {
					return true;
				}

				@Override
				public String getPresentationName() {
					return "Remove"; // (rows.length==1 ?  "Remove Element" : "Remove Elements");
				}
			});
		}

		update();
	}

	private List<MetadataField> fields;

	public final static int VIEW_FILENAMES_MINIMAL = 0;
	public final static int VIEW_FILENAMES = 1;
	public final static int VIEW_STANDARD = 2;
	public final static int VIEW_ALL = 3;

	private int currentView = -1;

	public void setView(int view) {
		switch (view) {
		case VIEW_FILENAMES:
		case VIEW_FILENAMES_MINIMAL:
			fields = new ArrayList<MetadataField>();
			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			break;
			
		case VIEW_STANDARD: {
			// (later, this will use BrowserComponent, so this
			// won't be needed at all)

			fields = new ArrayList<MetadataField>();

			// these are the "preview" fields
			final String PREVIEW_FIELDS[] = new String[] { "unmeas_pre",
					"unmeas_post", "species", "sapwood", "terminal", "quality", };

			// add all fields in |PREVIEW_FIELDS| to |fields|
			Iterator<MetadataField> i = MetadataTemplate.getFields();
			while (i.hasNext()) {
				MetadataField f = (MetadataField) i.next();

				for (int j = 0; j < PREVIEW_FIELDS.length; j++) {
					if (f.getVariable().equals(PREVIEW_FIELDS[j]))
						fields.add(f);
				}
			}

			table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
			break;
		}
		case VIEW_ALL: {
			fields = new ArrayList<MetadataField>();

			// add all fields to |fields|
			Iterator<MetadataField> i = MetadataTemplate.getFields();
			while (i.hasNext()) {
				MetadataField f = (MetadataField) i.next();
				fields.add(f);
			}

			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			break;
		}
		default:
			throw new IllegalArgumentException();
		}
		
		currentView = view;

		table.setModel(new ElementsTableModel(elements, elementMap, fields, currentView));

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
					@Override
					public Component getTableCellEditorComponent(JTable table,
							Object value, boolean isSelected, int row,
							int column) {
						Element e = (Element) value;
						chx.setSelected(elements.isActive(e));
						lab.setText(e.getShortName()); // filename only (not fq)

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
			if (((MetadataField) fields.get(i)).isList()) {
				JComboBox popup = new JComboBox(((MetadataField) fields.get(i)).getValuesArray());
				popup.setEditable(true);
				table.getColumnModel().getColumn(i + 2).setCellEditor(
						new DefaultCellEditor(popup));
			}
	}

	// this method is pretty (was: unbelievably) ugly, thanks to java's lack of closures.  *sigh*
	private int lastSortCol = -1;

	private void addClickToSort() {
		table.getTableHeader().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() != 1)
					return;
				int col = table.getColumnModel().getColumnIndexAtX(e.getX());

				Element first = elements.get(0);
				Element last = elements.get(elements.size() - 1);

				switch (col) {
				case 0: // filename
				{
					// sort by basename, since we don't want to sort by
					// the directory the files are in (which isn't displayed!)
					boolean reverse = (lastSortCol == col && first.getName()
							.compareTo(last.getName()) < 0);
					Sort.sort(elements, "name", reverse);
					break;
				}
				
				case 1: // folder
				{
					// no sorting here!
					break;
				}

				case 2: // range
				{
					Rangesorter sorter = new Rangesorter(elementMap, false);
					boolean reverse = (lastSortCol == col && sorter.compare(first, last) < 0);
					
					sorter.setReverse(reverse);
					Collections.sort(elements, sorter);
					break;
				}

				default: // details
					// unfortunately, i don't think i can use sort.sort() here because i'm not getting a field,
					// i'm calling a method on a field.  i could write another sort() wrapper to take a :key
					// method, like lisp does, and implement it in an anonymous class here, but that's more
					// work than just sorting by hand.  gah.
					String key = ((MetadataField) fields.get(col - 2)).getVariable();
					Metasorter sorter = new Metasorter(key, elementMap, false);
					boolean reverse = (lastSortCol == col && sorter.compare(first, last) < 0);
					
					sorter.setReverse(reverse);
					Collections.sort(elements, sorter);
				}

				lastSortCol = col;
			}
		});
	}

	// (if you see gosling, kick him for me for not giving java closures)
	private static class Metasorter implements Comparator<Element> { // by meta field
		private boolean rev;
		private String field;
		private Map<Element, BaseSample> emap;

		public Metasorter(String field, Map<Element, BaseSample> emap, boolean reverse) {
			rev = reverse;
			this.field = field;
			this.emap = emap;
		}
		
		public void setReverse(boolean reverse) {
			this.rev = reverse;
		}

		@SuppressWarnings("unchecked")
		public int compare(Element o1, Element o2) {
			BaseSample bs1 = emap.get(o1);
			BaseSample bs2 = emap.get(o2);
			Object v1 = (bs1 != null) ? bs1.getMeta(field) : null;
			Object v2 = (bs2 != null) ? bs2.getMeta(field) : null;
			if (v1 == null && v2 != null) // deal with nulls ... ick
				return +1;
			else if (v1 != null && v2 == null)
				return -1;
			else if (v1 == null && v2 == null)
				return 0;
			int x = ((Comparable) v1).compareTo(v2);
			return (rev ? -x : x);
		}
	}

	private static class Rangesorter implements Comparator<Element> { // by meta field
		private boolean rev;
		private Map<Element, BaseSample> emap;

		public Rangesorter(Map<Element, BaseSample> emap, boolean reverse) {
			rev = reverse;
			this.emap = emap;
		}
		
		public void setReverse(boolean reverse) {
			this.rev = reverse;
		}

		@SuppressWarnings("unchecked")
		public int compare(Element o1, Element o2) {
			BaseSample bs1 = emap.get(o1);
			BaseSample bs2 = emap.get(o2);
			Range v1 = (bs1 != null) ? bs1.getRange() : null;
			Range v2 = (bs2 != null) ? bs2.getRange() : null;
			if (v1 == null && v2 != null) // deal with nulls ... ick
				return +1;
			else if (v1 != null && v2 == null)
				return -1;
			else if (v1 == null && v2 == null)
				return 0;
			int x = ((Comparable) v1).compareTo(v2);
			return (rev ? -x : x);
		}
	}

	@Override
	public void sampleDisplayUnitsChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void measurementVariableChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sampleDisplayCalendarChanged(SampleEvent e) {
		// TODO Auto-generated method stub
		
	}
}
