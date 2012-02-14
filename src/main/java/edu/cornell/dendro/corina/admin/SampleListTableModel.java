/*******************************************************************************
 * Copyright (C) 2011 Peter Brewer.
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
package edu.cornell.dendro.corina.admin;

import java.awt.Color;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasSample;

import edu.cornell.dendro.corina.core.App;
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
		
		table.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth(App.getLabCodePrefix()+"XXX-XX-XX"));
		table.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth("2009-9999"));
		table.getColumnModel().getColumn(2).setPreferredWidth(fm.stringWidth("Perm. Location"));
		table.getColumnModel().getColumn(3).setPreferredWidth(fm.stringWidth("Curr. Location"));
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
	
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(columnIndex==4) return true;
        return false;
    }
    
    /*
     * Don't need to implement this method unless your table's
     * data can change.
     */
    public void setValueAt(Object value, int row, int col) {

        System.out.println("Setting value at " + row + "," + col
                           + " to " + value
                           + " (an instance of "
                           + value.getClass() + ")");
        

        if(col==4)
        {
        	checkedList.set(row, (Boolean) value);

        }

        fireTableCellUpdated(row, col);


    }

    /**
     * Get the number of samples that have been checked
     * 
     * @return
     */
    public Integer getCheckedCount()
    {
    	Integer count = 0;
    	if(checkedList!=null)
    	{
    		for(Boolean chk : checkedList)
    		{
    			if(chk) count++;
    		}
    	}
    	else
    	{
    		return null;
    	}
    	
    	return count;
    }
    
    /**
     * Get Array of TridasSamples that have not been checked
     * 
     * @return
     */
    public ArrayList<String> getUncheckedSampleNames()
    {
    	ArrayList<String> uncheckedSamples = new ArrayList<String>();
    	
    	for (TridasSample sample : samples)
    	{
    		Boolean isSampleChecked = (Boolean) getColumnValueForSample(sample, 4);
    		if(isSampleChecked==false) 
    		{
    			uncheckedSamples.add((String) getColumnValueForSample(sample,0));
    		}
    	}
    	
    	return uncheckedSamples;
    }
    
    /**
     * Clear all checks against samples
     */
    public void clearChecks()
    {
    	checkedList = new ArrayList<Boolean>();
		if(samples!=null)
		{
			for(int i=0; i<samples.size(); i++)
			{
				checkedList.add(false);
			}
		}
    }
}