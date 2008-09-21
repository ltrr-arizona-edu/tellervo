/**
 * 
 */
package edu.cornell.dendro.corina.cross;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import edu.cornell.dendro.corina.editor.CountRenderer;
import edu.cornell.dendro.corina.ui.I18n;


/**
 * @author Lucas Madar
 *
 */
public class HistogramTableModel extends AbstractTableModel {
	private CrossdateCollection.Pairing pairing;
	private Class<?> shownCrossdateClass;
	private Cross cross;
	private JTable table;
	private Histogram histogram;
	private CountRenderer countRenderer;

	public HistogramTableModel(JTable table) {
		this.table = table;
		
		countRenderer = new CountRenderer(0);
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
		this.histogram = null;
		
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
		histogram = new Histogram(cross);
		
		// update our countrenderer
		countRenderer.setMax(histogram.getFullestBucket());
		
		// update the column name
		table.getColumnModel().getColumn(0).setHeaderValue(getColumnName(0));
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
	
	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public int getRowCount() {
		if(cross == null || histogram == null)
			return 0;
		
		return histogram.getNumberOfBuckets();
	}

	@Override
	public Object getValueAt(int row, int col) {
		int n = histogram.getBucketItems(row);
		
		switch(col) {
		case 0:
			return histogram.getBucketRange(row);
		case 1:
			return (n == 0) ? null : n;
		case 2:
			return n;
		}
		
		return null;
	}
}
