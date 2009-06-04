/**
 * 
 */
package edu.cornell.dendro.corina.gui.dbbrowse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.tridas.schema.TridasObject;

import edu.cornell.dendro.corina.formats.Metadata;
import edu.cornell.dendro.corina.sample.BaseSample;
import edu.cornell.dendro.corina.sample.Element;
import edu.cornell.dendro.corina.sample.ElementList;
import edu.cornell.dendro.corina.sample.SampleSummary;
import edu.cornell.dendro.corina.tridas.LabCode;

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
		case 0: 
			return bs.hasMeta("title") ? bs.getMeta("title") : "[id: " + bs.getMeta("id") + "]";

		case 1:
			return bs.getSampleType();

		// site description
		case 2: {
			LabCode labcode = bs.getMeta(Metadata.LABCODE, LabCode.class);
			
			if(labcode == null)
				return null;
			
			List<String> titles = labcode.getSiteTitles();

			// no titles?
			if(titles.isEmpty())
				return null;
			
			return titles.get(titles.size() - 1);
		}

		// taxon
		case 3:
			return bs.getMeta(Metadata.SUMMARY_MUTUAL_TAXON);

		// measurement count
		case 4: 
			return bs.getMeta(Metadata.SUMMARY_SUM_CONSTITUENT_COUNT);
		
		// modified date
		case 5: {
			Date date = bs.getMeta(Metadata.MODIFIED_TIMESTAMP, Date.class);
			return date != null ? dateFormat.format(date) : date;
		}

		// start year
		case 6:
			return bs.getRange().getStart();
			
		// end year
		case 7:
			return bs.getRange().getEnd();
		
		// number of readings
		case 8:
			return bs.getRange().span();
		
		// reconciled
		case 9:
			return bs.getMeta(Metadata.RECONCILED);
			
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