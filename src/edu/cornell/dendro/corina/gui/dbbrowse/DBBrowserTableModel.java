/**
 * 
 */
package edu.cornell.dendro.corina.gui.dbbrowse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.SampleSummary;

public class DBBrowserTableModel extends AbstractTableModel {
	private ElementList elements;
    private final String[] columnNames = {
            "Name", 
            "Type", 
            "Site name", 
            "Taxon", 
            "#", 
            "Modified", 
            "Begin Date", 
            "End Date", 
            "n",
            "Rec"
            //"ID" //useful for debugging
        };
    
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * The default: no elements
	 */
	public DBBrowserTableModel() {
		this(new ElementList());
	}

	/**
	 * 
	 * @param elements
	 */
	public DBBrowserTableModel(ElementList elements) {
		this.elements = elements;
	}    	
	
	public void setElements(ElementList elements) {
		this.elements = elements;
		fireTableDataChanged();
	}
	
	public ElementList getElements() {
		return elements;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return elements.size();
	}
	
	public Object getColumnValueForElement(Element e, int columnIndex) {
		BaseSample bs;
		
		try {
			bs = e.loadBasic();
		} catch (IOException ioe) {
			return "<ERROR>";
		}
		
		switch(columnIndex) {
		case 0: {
			SampleSummary ss = (SampleSummary) bs.getMeta("::summary");

			if(ss != null)
				return ss.getLabCode();
			
			return bs.hasMeta("title") ? bs.getMeta("title") : "[id: " + bs.getMeta("id") + "]";
		}

		case 1:
			return bs.getSampleType();
			
		case 2: {
			SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
			return ss == null ? ss : ss.siteDescription();
		}

		case 3: {
			SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
			return ss == null ? ss : ss.taxonDescription();
		}

		case 4: {
			SampleSummary ss = (SampleSummary) bs.getMeta("::summary");
			return ss == null ? ss : ss.getMeasurementCount();
		}
		
		case 5: {
			Date date = (Date) bs.getMeta("::moddate");
			return date != null ? dateFormat.format(date) : date;
		}

		case 6:
			return bs.getRange().getStart();
			
		case 7:
			return bs.getRange().getEnd();
		
		case 8:
			return bs.getRange().span();
			
		case 9:
			return bs.getMeta("isreconciled");
			
		default:
			return null;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Element e = elements.get(rowIndex);
		
		return getColumnValueForElement(e, columnIndex);
	}
	
    public Class<?> getColumnClass(int c) {
    	// reconciled is true/false
    	if(c == 9)
    		return Boolean.class;
    	
    	// everything else is just a string
    	return String.class;
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public Element getElementAt(int rowIndex) {
		return elements.get(rowIndex);
	}
}