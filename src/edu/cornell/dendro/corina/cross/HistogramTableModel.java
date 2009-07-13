/**
 * 
 */
package edu.cornell.dendro.corina.cross;

 import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.editor.CountRenderer;
import edu.cornell.dendro.corina.gui.SortedHeaderArrowRenderer;
import edu.cornell.dendro.corina.ui.I18n;


/**
 * @author Lucas Madar
 *
 */
public class HistogramTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private CrossdateCollection.Pairing pairing;
	private Class<?> shownCrossdateClass;
	private Cross cross;
	private JTable table;
	private CountRenderer countRenderer;
	private List<HistogramEntry> histogramEntries;
	
	// for sorting
	private SortedHeaderArrowRenderer headerRenderer;
	private HistogramSorter sorter;

	/**
	 * Class to hold histogram entries for sorting
	 * @author Lucas Madar
	 */
	private static class HistogramEntry {
		public HistogramEntry(Histogram histogram, int index) {
			this.range = histogram.getBucketRange(index);
			this.n = histogram.getBucketItems(index);
			this.rangeMin = histogram.getBucketMin(index);
		}
		
		public String range;
		public float rangeMin;
		public int n;
	}

	/**
	 * Class to sort histogram entries
	 * @author Lucas Madar
	 */
	private class HistogramEntryComparator implements Comparator<HistogramEntry> {
		public final static int BUCKET = 1;
		public final static int COUNT = 2;
		
		private final int type;
		
		public HistogramEntryComparator(int col) {
			switch(col) {
			case 0:
				type = BUCKET;
				break;
				
			case 1: 
				type = COUNT;
				break;
				
			default:
				throw new IllegalArgumentException("Can't sort here");
			}
		}

		public int compare(HistogramEntry o1, HistogramEntry o2) {
			switch(type) {
			case BUCKET:
				return (o2.rangeMin < o1.rangeMin) ? 1 : -1;
				
			case COUNT:
				return o2.n - o1.n;
				
			default:
				return 0;
			}
		}
	}
	
	/**
	 * This class handles clicks on table row headers, for sorting
	 * @author Lucas Madar
	 */
	private class HistogramSorter extends MouseAdapter {
		private int lastSortedCol = -1;
		private HistogramTableModel model;
		private JTable table;
		
		public HistogramSorter(HistogramTableModel model, JTable table) {
			this.model = model;
			this.table = table;
		}
		
		public void sort(int col, boolean forceReverse) {
			Collections.sort(model.histogramEntries, new HistogramEntryComparator(col));
			
			if(forceReverse)
				Collections.reverse(model.histogramEntries);
			
			lastSortedCol = col;
			model.headerRenderer.setSortColumn(col);
			model.headerRenderer.setReversed(!forceReverse); // we default to reversed!
			
			// make the table header repaint (for new arrows)
			table.getTableHeader().repaint();
		}
		
		@Override
		public void mouseClicked(MouseEvent me) {
			int col = table.getColumnModel().getColumnIndexAtX(me.getX());
			
			// sanity check
			if(col < 0)
				return;
			
			if(col == lastSortedCol) {
				Collections.reverse(model.histogramEntries);

				// reverse arrow and repaint
				model.headerRenderer.setReversed(!model.headerRenderer.isReversed());
				table.getTableHeader().repaint();
			}
			else {
				sort(col, false);
			}
			
			// notify the table
			model.fireTableDataChanged();			
		}
	}
	
	public HistogramTableModel(JTable table) {
		this.table = table;
		
		countRenderer = new CountRenderer(0);
		
		// a sorter based on us...
		sorter = new HistogramSorter(this, table);
		// a header renderer with no default 
		headerRenderer = new SortedHeaderArrowRenderer(table, null);
		
		table.getTableHeader().addMouseListener(sorter);
		table.getTableHeader().setDefaultRenderer(headerRenderer);
	}

	public void applyFormatting() {
		// center the first two columns
		DefaultTableCellRenderer centerAligner = new DefaultTableCellRenderer();
		centerAligner.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerAligner);
		table.getColumnModel().getColumn(1).setCellRenderer(centerAligner);
		
		// and make the third column our weird custom control
		table.getColumnModel().getColumn(2).setCellRenderer(countRenderer);
		
		// make the first few columns tinier
        int width = table.getWidth();
    	table.getColumnModel().getColumn(0).setPreferredWidth(width / 4);
    	table.getColumnModel().getColumn(1).setPreferredWidth(width / 4);
	}
	
	public void clearCrossdates() {
		this.pairing = null;
		this.cross = null;
		this.histogramEntries = null;
		
		fireTableDataChanged();
	}
	
	public void setCrossdates(CrossdateCollection.Pairing pairing) {
		this.pairing = pairing;
		
		// update the cross (shownCrossdateClass can be null?)
		cross = pairing.getCrossForClass(shownCrossdateClass);

		if(cross != null)
			updateTable();
		
		fireTableDataChanged();
	}
	
	/**
	 * Set the type of crossdate to display (e.g., TScore.class, RScore.class)
	 * @param clazz
	 */
	public void setScoreClass(Class<?> clazz) {
		shownCrossdateClass = clazz;

		// update the cross
		if(pairing != null)  {			
			cross = pairing.getCrossForClass(clazz);
			if(cross != null)
				updateTable();
		}
		
		fireTableDataChanged();
	}

	private void updateTable() {
		// make a histogram of the data
		Histogram histogram = new Histogram(cross);
		
		// populate our hisotgram entries
		histogramEntries = new ArrayList<HistogramEntry>(histogram.getNumberOfBuckets());
		for(int i = 0; i < histogram.getNumberOfBuckets(); i++)
			histogramEntries.add(new HistogramEntry(histogram, i));
		
		// update our countrenderer
		countRenderer.setMax(histogram.getFullestBucket());
		
		// update the column name
		table.getColumnModel().getColumn(0).setHeaderValue(getColumnName(0));
		
		// sort by score (reversed, top scores first)
		sorter.sort(0, true);
	}
	
    @Override
	public String getColumnName(int col) {
        switch (col) {
            case 0: return (cross == null) ?  "Name" : cross.getName();
            case 1: return I18n.getText("quantity");
            case 2: return I18n.getText("histogram");
            default: throw new IllegalArgumentException(); // can't happen
        }
    }

	public Class<?> getColumnClass(int col) {
        switch (col) {
            case 0: return String.class;
            case 1: return Integer.class;
            case 2: return Integer.class;
            default: throw new IllegalArgumentException(); // can't happen
        }
    }
	
	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		if(cross == null || histogramEntries == null)
			return 0;

		return histogramEntries.size();
	}

	public Object getValueAt(int row, int col) {				
		HistogramEntry entry = histogramEntries.get(row);
		
		switch(col) {
		case 0:
			return entry.range;
		case 1:
			return (entry.n == 0) ? null : entry.n;
		case 2:
			return entry.n;
		}
		
		return null;
	}
}
