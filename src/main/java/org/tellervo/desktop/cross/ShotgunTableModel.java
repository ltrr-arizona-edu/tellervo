package org.tellervo.desktop.cross;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.sample.Sample;

public class ShotgunTableModel extends AbstractTableModel {

	private final static Logger log = LoggerFactory.getLogger(ShotgunTableModel.class);

	private static final long serialVersionUID = 1L;

	private Cross[][] data;
	private ArrayList<Sample> samples;
	
	public ShotgunTableModel(ArrayList<Sample> samples)
	{
		this.samples = samples;
		data = new Cross[samples.size()][samples.size()];
		
		
	}
	
	public float getMaxValueInTable()
	{
		float highval = 0;
		for(int c=0; c<data.length; c++)
		{
			for(int r=0; r<data.length; r++)
			{
				try{
					Cross value = data[c][r];
					
					if(value==null) continue;
					float val = value.getHighScores().getScores().get(0).score;
					if(val>highval) highval = val;
				} catch (Exception e)
				{
					log.debug("Exception caught ");
				}
			}
		}
		return highval;
	}
	
	@Override
	public String getColumnName(int col)
	{
		
		return samples.get(col).getDisplayTitle();

	}
		
	public Class getColumnClass(int col)
	{
		return Cross.class;
	}
	
	@Override
	public int getColumnCount() {
		return data.length;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		
		try{
			return data[col][row];
		} catch (ArrayIndexOutOfBoundsException e)
		{
			log.debug("Caught AIOOBE");
			return null;
		}
	}
	
	public void setValueAt(int row, int col, Cross value)
	{
		
		try{
			value.getHighScores().sortScoresMostSigFirst();
		} catch (NullPointerException e)
		{
			log.debug("Caught NPE when trying to sort TopScores");
		}
		
		data[col][row] = value;
	}

}
