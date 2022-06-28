package org.tellervo.desktop.curation;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.editor.SeriesDataMatrix;
import org.tellervo.desktop.tridasv2.GenericFieldUtils;
import org.tellervo.desktop.ui.Builder;
import org.tridas.schema.TridasGenericField;
import org.tridas.schema.TridasSample;

import edu.emory.mathcs.backport.java.util.Arrays;


public class SampleListTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private final static Logger log = LoggerFactory.getLogger(SampleListTableModel.class);
    private Object[] columnNames= {"Sample code", "Current curation status", "Sample Status", "Box"};
	private ArrayList<TridasSample> samples = new ArrayList<TridasSample>();

	
	
	public SampleListTableModel()
	{

	}
	
	public SampleListTableModel(ArrayList<TridasSample> samples)
	{
		setSamples(samples);
	}
	
    public Object[] getColumnNames() {
        return this.columnNames;    
    }
	
    public String getColumnName(int column) {
        return (String) columnNames[column];
    }
    
	@Override
	public int getColumnCount() {
		return this.columnNames.length;
	}

	@Override
	public int getRowCount() {
		return samples.size();
	}

	public void clear()
	{
		samples = new ArrayList<TridasSample>();
		this.fireTableDataChanged();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {

		TridasSample sample = getSample(rowIndex);
		if(sample==null) return null;
		
    	TridasGenericField labcodeField = GenericFieldUtils.findField(sample, "tellervo.internal.labcodeText");
    	TridasGenericField curationStatusField = GenericFieldUtils.findField(sample, "tellervo.curationStatus");
    	TridasGenericField sampleStatusField = GenericFieldUtils.findField(sample, "tellervo.sampleStatus");
    	TridasGenericField boxField = GenericFieldUtils.findField(sample, "tellervo.boxCode");
    	
		if(columnIndex==0)
		{
			return (labcodeField != null) ? labcodeField.getValue() : sample.getTitle();	
		}
		else if(columnIndex==1) 
		{
			return (curationStatusField != null) ? curationStatusField.getValue() : "Unknown";
		}	
		else if(columnIndex==2) 
		{
			return (sampleStatusField != null) ? sampleStatusField.getValue() : "Unknown";
		}	
		else if(columnIndex==3) 
		{
			return (boxField != null) ? boxField.getValue() : null;
		}
		else
		{
			return null;
		}
		
	}
	
	public TridasSample getSample(int index)
	{
		try{
			return samples.get(index);
		} catch (IndexOutOfBoundsException e)
		{
			
		}
		return null;
	}
	
	public ArrayList<TridasSample> getTridasSamples()
	{
		return samples;
	}
	
	public void addSample(TridasSample sample)
	{
		for(TridasSample s : samples)
		{
			if(s.getIdentifier().getValue().equals(sample.getIdentifier().getValue()))
			{
				return;
			}
		}
		
		samples.add(sample);
		fireTableDataChanged();
	}
	
	public void setSamples(ArrayList<TridasSample> samples)
	{
		this.samples = samples;
		fireTableDataChanged();
	}
	
	public void removeSample(int index)
	{
		samples.remove(index);
		fireTableDataChanged();
	}

}
