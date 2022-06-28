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
package org.tellervo.desktop.gis;

import java.util.ArrayList;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tellervo.desktop.dictionary.Dictionary;
import org.tellervo.desktop.prefs.Prefs;
import org.tellervo.schema.WSIWmsServer;


/**
 * Table of WMS servers.  The table is made up of two lists: system servers and personal servers.  
 * The system servers are derived from the system dictionary, while the personal are unique to 
 * the current user.
 * 
 * @author pwb48
 *
 */
public class WMSTableModel implements TableModel {

	private final static Logger log = LoggerFactory.getLogger(WMSTableModel.class);

	ArrayList<WSIWmsServer> personalServers = new ArrayList<WSIWmsServer>();
	ArrayList<WSIWmsServer> systemServers = new ArrayList<WSIWmsServer>();
	
	public WMSTableModel()
	{

	}
	
	/**
	 * Set the system servers from the system dictionary
	 */
	@SuppressWarnings("unchecked")
	public void setSystemServers()
	{
		systemServers = Dictionary.getMutableDictionary("wmsServerDictionary");
	}
	
	/**
	 * Set the personal servers to those specified.  Notes this will delete
	 * any existing servers in the table. 
	 * 
	 * @param servers
	 */
	public void setPersonalServers(ArrayList<WSIWmsServer> servers)
	{
		personalServers = servers;
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
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		if(columnIndex==0)
		{
			return "Name";
		}
		else if (columnIndex==1)
		{
			return "Type";
		}
		else
		{
			return "URL";
		}
	}

	@Override
	public int getRowCount() {
		return personalServers.size()+systemServers.size();
	}

	public WSIWmsServer getServerFromRow(int row)
	{
		ArrayList<WSIWmsServer> combinedServers = new ArrayList<WSIWmsServer>();
		combinedServers.addAll(systemServers);
		combinedServers.addAll(personalServers);
		
		if(row>combinedServers.size() || row<0) return null;
		
		try{
		return combinedServers.get(row);
		} catch (Exception e)
		{
			log.error("Trying to get index "+row+" from servers when available servers = "+combinedServers.size());
		}
		
		return null;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		WSIWmsServer server = getServerFromRow(rowIndex);
		
		if(server==null) return null;
		
		if(columnIndex==0)
		{
			return server.getName();
			
		}
		else if (columnIndex==1)
		{
			if(rowIndex<systemServers.size())
			{
				return "System";
			}
			else
			{
				return "Personal";
			}
		}
		else
		{
			return server.getUrl();
		}
	}

	public boolean isRowEditable(int rowIndex)
	{
		WSIWmsServer server = getServerFromRow(rowIndex);
		
		if(server==null) return false;
		
		if(getValueAt(rowIndex,1).equals("Personal"))
		{
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {

		//WSIWmsServer server = getServerFromRow(rowIndex);
		
		/*if(rowIndex<systemServers.size()) return false;
		
		if(columnIndex==1) return false;*/
		
		return false;
		
		
		/*
		for(WSIWmsServer sysser : this.systemServers)
		{
			if(server.equals(sysser))
			{
				return false;
			}
		}
		
		return true;*/
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		WSIWmsServer server = getServerFromRow(rowIndex);
			
		if(server==null) return;
		
		if(columnIndex==0)
		{
			server.setName((String) aValue);
		}
		else
		{
			server.setUrl((String) aValue);
		}

	}
	
	/**
	 * Add a personal server to the table.
	 * 
	 * @param server
	 */
	public void addPersonalServer(WSIWmsServer server)
	{
		this.personalServers.add(server);
		
		
	}
	
	/**
	 * Remove row from table.  This will only remove
	 * personal servers, not system servers.
	 * 
	 * @param row
	 */
	public void removeRow(int row)
	{
		WSIWmsServer server = getServerFromRow(row);
		
		try{
		personalServers.remove(server);
		} catch (Exception e)
		{
			
		}
		
	}

}
