package edu.cornell.dendro.corina.gis;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSIWmsServer;

public class WMSTableModel implements TableModel {

	ArrayList<WSIWmsServer> servers = new ArrayList<WSIWmsServer>();
	ArrayList<WSIWmsServer> systemServers = new ArrayList<WSIWmsServer>();
	
	public WMSTableModel()
	{
		systemServers = Dictionary.getMutableDictionary("wmsServerDictionary");
		servers.addAll(systemServers);
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(columnIndex==0)
		{
			return "Name";
		}
		else
		{
			return "URL";
		}
	}

	@Override
	public int getRowCount() {
		return servers.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSIWmsServer server = servers.get(rowIndex);
		if(columnIndex==0)
		{
			return server.getName();
			
		}
		else
		{
			return server.getUrl();
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		WSIWmsServer server = servers.get(rowIndex);
		
		for(WSIWmsServer sysser : this.systemServers)
		{
			if(server.equals(sysser))
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		WSIWmsServer server = servers.get(rowIndex);
		
		if(columnIndex==0)
		{
			server.setName((String) aValue);
		}
		else
		{
			server.setUrl((String) aValue);
		}

	}
	
	public void addServer(WSIWmsServer server)
	{
		this.servers.add(server);
		
		
	}

}
