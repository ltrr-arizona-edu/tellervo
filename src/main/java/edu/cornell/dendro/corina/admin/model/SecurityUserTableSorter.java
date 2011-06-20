package edu.cornell.dendro.corina.admin.model;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.table.TableRowSorter;

import edu.cornell.dendro.corina.gui.SortedHeaderArrowRenderer;
import edu.cornell.dendro.corina.schema.WSISecurityUser;

public class SecurityUserTableSorter extends TableRowSorter implements MouseListener {
	private SecurityUserTableModel model;
	private JTable table;
	private SortedHeaderArrowRenderer headerRenderer;
	
	private int lastSortedCol = -1;
	
	public SecurityUserTableSorter(SecurityUserTableModel model, JTable table) {
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

		sortOnColumn(col, false);
	}
	
	/**
	 * Force a re-sort
	 */
	public void reSort() {
		// nothing to sort by :(
		if(lastSortedCol < 0)
			return;
		
		int col = lastSortedCol;
		boolean reverse = headerRenderer.isReversed();
		
		lastSortedCol = -1;
		sortOnColumn(col, reverse);
	}
	
	/**
	 * Force a sort...
	 * @param col the column to sort on
	 * @param reverse force reverse
	 */
	public void sortOnColumn(int col, boolean reverse) {
		if(col == lastSortedCol) {
			Collections.reverse(model.getUsers());
			headerRenderer.setReversed(!headerRenderer.isReversed());
		}
		else {
			Collections.sort(model.getUsers(), new SecurityUserListTableColumnComparator(model, col));
			
			headerRenderer.setSortColumn(col);
			headerRenderer.setReversed(false);
			
			lastSortedCol = col;
			
			if(reverse)
				Collections.reverse(model.getUsers());
		}

		// notify the model and repaint the header
		model.fireTableDataChanged();
		table.getTableHeader().repaint();
		
	}
			
	private class SecurityUserListTableColumnComparator implements Comparator<WSISecurityUser> 
	{
		private int column;
		private SecurityUserTableModel model;
		
		public SecurityUserListTableColumnComparator(SecurityUserTableModel model, int column) {
			this.column = column;
			this.model = model;
		}
		
		private Object getValue(WSISecurityUser usr) {

			
			return model.getColumnValueForUser(usr, column);
		}
		
		@SuppressWarnings("unchecked")
		public int compare(WSISecurityUser e1, WSISecurityUser e2) {
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

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
