package edu.cornell.dendro.corina.gui.dbbrowse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTable;

import edu.cornell.dendro.corina.gui.SortedHeaderArrowRenderer;
import edu.cornell.dendro.corina.sample.Element;

public class DBBrowserSorter extends MouseAdapter {
	private DBBrowserTableModel model;
	private JTable table;
	private SortedHeaderArrowRenderer headerRenderer;
	
	private int lastSortedCol = -1;
	
	public DBBrowserSorter(DBBrowserTableModel model, JTable table) {
		this.table = table;
		this.model = model;	
		
		headerRenderer = new SortedHeaderArrowRenderer(table, null);
		table.getTableHeader().setDefaultRenderer(headerRenderer);
	}
	
	public void setUnsorted() {
		lastSortedCol = -1;
		
		// remove any arrow we have lying around
		table.getTableHeader().repaint();
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		int col = table.getColumnModel().getColumnIndexAtX(me.getX());
		
		// sanity check
		if(col < 0)
			return;
		
		if(col == lastSortedCol) {
			Collections.reverse(model.getElements());
			headerRenderer.setReversed(!headerRenderer.isReversed());
		}
		else {
			Collections.sort(model.getElements(), new DBBrowserTableColumnComparator(model, col));
			
			headerRenderer.setSortColumn(col);
			headerRenderer.setReversed(false);
			
			lastSortedCol = col;
		}
		
		// notify the model and repaint the header
		model.fireTableDataChanged();
		table.getTableHeader().repaint();
	}
	
	private class DBBrowserTableColumnComparator implements Comparator<Element> {
		private int column;
		private DBBrowserTableModel model;
		
		public DBBrowserTableColumnComparator(DBBrowserTableModel model, int column) {
			this.column = column;
			this.model = model;
		}
		
		private Object getValue(Element e) {
			return model.getColumnValueForElement(e, column);
		}
		
		public int compare(Element e1, Element e2) {
			Object o1 = getValue(e1);
			Object o2 = getValue(e2);

			// nicely handle nulls
			if(o1 == null && o2 == null)
				return 0;
			if(o1 == null)
				return -1;
			if(o2 == null)
				return +1;
			
			// try and have it compare itself
			if(o1 instanceof Comparable) 
				return ((Comparable) o1).compareTo(o2);
			
			// fallback on lame string compares
			return o1.toString().compareToIgnoreCase(o2.toString());
		}
	}
}
