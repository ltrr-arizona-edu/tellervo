/**
 * 
 */
package org.tellervo.desktop.gui.dbbrowse;

import java.awt.Color;
import java.awt.FontMetrics;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.core.App;
import org.tellervo.desktop.io.Metadata;
import org.tellervo.desktop.sample.BaseSample;
import org.tellervo.desktop.sample.Element;
import org.tellervo.desktop.sample.ElementList;
import org.tellervo.desktop.sample.SampleType;
import org.tellervo.desktop.ui.I18n;
import org.tridas.interfaces.ITridasSeries;
import org.tridas.schema.TridasDerivedSeries;
import org.tridas.schema.TridasMeasurementSeries;


public class ElementListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private ElementList elements;
    private final String[] columnNames = {
            I18n.getText("dbbrowser.name"), 
            I18n.getText("dbbrowser.version"), 
            I18n.getText("dbbrowser.user"),
            I18n.getText("dbbrowser.type"), 
            I18n.getText("dbbrowser.taxon"), 
            I18n.getText("dbbrowser.hash"), 
            I18n.getText("dbbrowser.modified"), 
            I18n.getText("dbbrowser.begin"), 
            I18n.getText("dbbrowser.end"), 
            I18n.getText("dbbrowser.n"),
            I18n.getText("dbbrowser.rec"),
            "hidden.MostRecentVersion"
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
		
		table.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth(App.getLabCodePrefix()+"-XXX-XX-XX-Xx-Xx"));
		table.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth("Version"));
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("George W Bush"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("DirectX"));
		table.getColumnModel().getColumn(4).setPreferredWidth(fm.stringWidth("Pinus nigra X"));
		table.getColumnModel().getColumn(5).setPreferredWidth(fm.stringWidth("99"));
		table.getColumnModel().getColumn(6).setPreferredWidth(fm.stringWidth("2008-08-08"));
		table.getColumnModel().getColumn(7).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(8).setPreferredWidth(fm.stringWidth("12345"));
		table.getColumnModel().getColumn(9).setPreferredWidth(fm.stringWidth("123"));
		table.getColumnModel().getColumn(10).setPreferredWidth(fm.stringWidth("Rec")); // checkbox?


		}
    
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * The default: no elements
	 */
	public ElementListTableModel() {
		this(new ElementList());
	}

	/**
	 * 
	 * @param elements
	 */
	public ElementListTableModel(ElementList elements) {
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
		
		if(elements!=null)
		{
			return elements.size();
		}
		
		return 0;
	}
	
	public Object getColumnValueForElement(Element e, int columnIndex) {
		BaseSample bs;
		ITridasSeries series;
		
		try {
			bs = e.loadBasic();
			series = bs.getSeries();
		} catch (IOException ioe) {
			return "<ERROR>";
		}
		
		switch(columnIndex) {
		case 0: 
			return bs.hasMeta("title") ? bs.getMeta("title") : "[id: " + bs.getMeta("id") + "]";

		case 1:
			return bs.getMetaString(Metadata.VERSION);
			
		case 2:
			 if (series instanceof TridasMeasurementSeries)
			 {
				 TridasMeasurementSeries ser = (TridasMeasurementSeries) series;
				 return ser.getAnalyst().toString();
			 }
			 else if (series instanceof TridasDerivedSeries)
			 {
				 TridasDerivedSeries ser = (TridasDerivedSeries) series;
				 return ser.getAuthor().toString();
			 }
			 else return null;
			
		// Sample type
		case 3:
			return bs.getSampleType();

		// taxon
		case 4: {
			// sums have special ways of getting taxon info
			if(bs.getSampleType() == SampleType.SUM) {
				Integer nTaxa = bs.getMeta(Metadata.SUMMARY_MUTUAL_TAXON_COUNT, Integer.class);
				String taxon = bs.getMetaString(Metadata.SUMMARY_MUTUAL_TAXON);

				if(nTaxa == null || nTaxa < 2 || taxon == null)
					return taxon;
			
				return nTaxa + " "+I18n.getText("dbbrowser.taxaOf") + " " + taxon;
			}
			
			return bs.meta().getTaxon();
		}
		
		// measurement count
		case 5: 
			return bs.getMeta(Metadata.SUMMARY_SUM_CONSTITUENT_COUNT);
		
		// modified date
		case 6: {
			Date date = bs.getMeta(Metadata.MODIFIED_TIMESTAMP, Date.class);
			return (date != null) ? dateFormat.format(date) : date;
		}

		// start year
		case 7:
			return bs.getRange().getStart();
			
		// end year
		case 8:
			return bs.getRange().getEnd();
		
		// number of readings
		case 9:
			return bs.getRange().span();
		
		// reconciled
		case 10:
			return bs.getMeta(Metadata.RECONCILED);
			
		// hidden column returning true if row is most recent version
		case 11:
			return isMostRecentVersion(bs);
			
		default:
			return null;
		}
	}
	
	/**
	 * Returns true if this baseSample is the most recent version in the table
	 * 
	 * @param thisBS
	 * @return
	 */
	private Boolean isMostRecentVersion(BaseSample thisBS)
	{	
		Boolean mostRecent = true;
		
		for(int i=0; i<getRowCount(); i++ )
		{
			Element matchEl = getElementAt(i);
			BaseSample matchBS;
					
			try {
				matchBS = matchEl.loadBasic();
			} catch (IOException ioe) {
				return false;
			}
			
			// Skip if we're comparing the same row
			if(thisBS.equals(matchBS)) continue;

			// Skip if the name is not the same
			if(!thisBS.getDisplayTitle().equals(matchBS.getDisplayTitle())) continue;
			
			// Check if the current row is newer than the specified row
			if(thisBS.getMeta(Metadata.CREATED_TIMESTAMP, Date.class).before(matchBS.getMeta(Metadata.CREATED_TIMESTAMP, Date.class)))
			{
				mostRecent = false; 
			}
		}
		
		return mostRecent;
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		Element e = elements.get(rowIndex);
		
		return getColumnValueForElement(e, columnIndex);
	}
	
    public Class<?> getColumnClass(int c) {
    	// reconciled is true/false
    	if(c == 10){
    		return Boolean.class;
    	}
    	if(c == 11){
    		return Boolean.class;
    	}
    	else{
    	// everything else is just a string
    		return String.class;
    	}
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public Element getElementAt(int rowIndex) {
		return elements.get(rowIndex);
	}	
}