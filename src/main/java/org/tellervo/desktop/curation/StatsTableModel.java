package org.tellervo.desktop.curation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.tellervo.desktop.dictionary.StatisticsResource;
import org.tellervo.schema.WSIStatistic;
import org.tellervo.desktop.ui.I18n;


public class StatsTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ArrayList<WSIStatistic> stats = new ArrayList<WSIStatistic>();
	
    private Object[] columnNames= {"Item", "Value"};
	
    StatisticsResource res;
    
	public StatsTableModel()
	{
		res = new StatisticsResource();
		pollForStats();
		
	}
	
	public StatsTableModel(StatisticsResource res)
	{
		this.res = res;
		pollForStats();
	}
	
	public void pollForStats()
	{
				
		Map map = res.getStats();
		stats = new ArrayList<WSIStatistic>();
		
		Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        WSIStatistic stat = new WSIStatistic();
	        stat.setKey((String) pairs.getKey());
	        stat.setValue((String) pairs.getValue());
	        stats.add(stat);

	    }
	}
	
	
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return stats.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		WSIStatistic stat = stats.get(row);
		
		if(col==0)
		{
			return I18n.getText("statistic."+stat.getKey().replace(" ", ""));
		}
		else
		{
			return stat.getValue();
		}
	}
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	 public String getColumnName(int col) {
	        return columnNames[col].toString();
	    }

}
