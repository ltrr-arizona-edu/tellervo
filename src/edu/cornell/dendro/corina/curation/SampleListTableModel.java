/**
 * 
 */
package edu.cornell.dendro.corina.curation;

import java.awt.Color;
import java.awt.FontMetrics;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.SampleType;

import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasSample;

public class SampleListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private List<TridasSample> samples;
    private final String[] columnNames = {
            "Name", 
            "Box", 
            "Curation Location", 
            "Current Location"
        };
    
    /**
     * Convenience: set up columns for a JTable that is going to be using this model
     * @param table
     */
    public static void setupColumnWidths(JTable table) {
		FontMetrics fm = table.getFontMetrics(table.getFont());
		
		table.setShowGrid(false);
		table.setShowVerticalLines(true);
		table.setGridColor(Color.lightGray);
		
		table.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth("C-XXX-XX-XX"));
		table.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth("2009-9999"));
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("Curation Location"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Current Location"));
    }
    
	/**
	 * The default: no elements
	 */
	public SampleListTableModel() {
		
		this.samples = null;
		
		
	}

	/**
	 * 
	 * @param elements
	 */
	public SampleListTableModel(List<TridasSample> samples) {
		this.samples = samples;
	}    	
	
	public void setSamples(List<TridasSample> samples) {
		this.samples = samples;
		fireTableDataChanged();
	}
	
	public List<TridasSample> getSamples() {
		return samples;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		if(samples!=null) return samples.size();
		return 0;
	}
	
	public Object getColumnValueForSample(TridasSample s, int columnIndex) {
	
		List<TridasGenericField> gflist = s.getGenericFields();
		
		String boxCode = null;
		String boxCurationLocation = null;
		String boxCurrentLocation = null;
		
		for(TridasGenericField gf : gflist)
		{
			if (gf.getName()=="corina.boxCode") boxCode = gf.getValue();
			if (gf.getName()=="corina.boxCurationLocation") boxCurationLocation = gf.getValue();
			if (gf.getName()=="corina.boxCurrentLocation") boxCurrentLocation = gf.getValue();
		}
		
		
		switch(columnIndex) {
		case 0: 
			return s.getTitle().toString();

		case 1:
			return boxCode;
			
		case 2:
			return boxCurationLocation;

		// taxon
		case 3: {
			return boxCurrentLocation;
		}
		
		default:
			return null;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		TridasSample s = samples.get(rowIndex);
		
		return getColumnValueForSample(s, columnIndex);
	}
	
    public Class<?> getColumnClass(int c) {

    	return String.class;
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public TridasSample getElementAt(int rowIndex) {
		return samples.get(rowIndex);
	}	
}