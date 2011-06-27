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
package edu.cornell.dendro.corina.gis;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import edu.cornell.dendro.corina.dictionary.Dictionary;
import edu.cornell.dendro.corina.schema.WSIWmsServer;

public class WMSTableModel implements TableModel {

	ArrayList<WSIWmsServer> servers = new ArrayList<WSIWmsServer>();
	ArrayList<WSIWmsServer> systemServers = new ArrayList<WSIWmsServer>();
	
	@SuppressWarnings("unchecked")
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
