/**
 * 
 */
package edu.cornell.dendro.corina.admin;

import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.tridasv2.LabCode;
import edu.cornell.dendro.corina.tridasv2.LabCodeFormatter;


/**
 * Table model for a list of samples and their curatorial box locations
 * 
 * @author peterbrewer
 */
public class SampleListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	
	private List<TridasSample> samples;
	private ArrayList<Boolean> checkedList;
    private final String[] columnNames = {
            "Name", 
            "Box", 
            "Box Curation Location", 
            "Box Tracking Location",
            "Temporary Checklist"
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
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("Box Curation Location"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Box Tracking Location"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Temporary Checklist"));
    }
    
	/**
	 * The default: no elements
	 */
	public SampleListTableModel() {
		
		setSamples(null);
		
		
	}

	/**
	 * 
	 * @param elements
	 */
	public SampleListTableModel(List<TridasSample> samples) {
		setSamples(samples);
	}    	
	
	public void setSamples(List<TridasSample> samples) {
		this.samples = samples;
		if(samples!=null)
		{
			checkedList = new ArrayList<Boolean>();
			for(int i=0; i<samples.size(); i++)
			{
				checkedList.add(false);
			}
		}
		
		fireTableDataChanged();
	}
	
	public void setIsChecked(Boolean isChecked, int rowIndex)
	{
		if(rowIndex>checkedList.size())
		{
			return;
		}
		else
		{
			checkedList.set(rowIndex, isChecked);
		}
		
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
		String boxTrackingLocation = null;
		String objectCode = null;
		String elementCode = null;
			
		
		for(TridasGenericField gf : gflist)
		{
			if (gf.getName().equals("corina.boxCode")){
				boxCode = gf.getValue().toString();
			}
			if (gf.getName().equals("corina.boxCurationLocation")){
				boxCurationLocation = gf.getValue().toString();
			}
			if (gf.getName().equals("corina.boxTrackingLocation")) {
				boxTrackingLocation = gf.getValue().toString();
			}
			if (gf.getName().equals("corina.objectLabCode")){
				objectCode = gf.getValue().toString();
			}
			if (gf.getName().equals("corina.elementLabCode")){
				elementCode = gf.getValue().toString();
			}
					
		}
		
		
		switch(columnIndex) {
		case 0: 
			LabCode labCode = new LabCode();
			labCode.setElementCode(elementCode);
			labCode.setSampleCode(s.getTitle());
			labCode.appendSiteCode(objectCode);
				
			return LabCodeFormatter.getRadiusPrefixFormatter().format(labCode);

		case 1:
			return boxCode;
			
		case 2:
			return boxCurationLocation;

		case 3: {
			return boxTrackingLocation;
		}
		
		case 4: {
			int rowIndex = samples.indexOf(s);
			return checkedList.get(rowIndex);
		}
		default:
			return null;
		}
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		TridasSample s = samples.get(rowIndex); 
		
		if(columnIndex==4)
		{
			return checkedList.get(rowIndex);
		}
		else
		{
			return getColumnValueForSample(s, columnIndex);
		}
	}
	
    public Class<?> getColumnClass(int c) {
    	if (c==4) return Boolean.class;
    	
    	return String.class;
    }
    
	public String getColumnName(int index) {
		return columnNames[index];
	}
	
	public TridasSample getElementAt(int rowIndex) {
		return samples.get(rowIndex);
	}	
}