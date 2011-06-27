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
package edu.cornell.dendro.corina.core;

import com.dmurph.mvc.model.HashModel;

public class AppModel extends HashModel {

	private static final long serialVersionUID = 1L;
	public static final String NETWORK_STATUS = "networkStatus";
	
	public enum NetworkStatus{
		OFFLINE,
		ONLINE;
	}
	
	public AppModel()
	{
		registerProperty(AppModel.NETWORK_STATUS, PropertyType.READ_WRITE, NetworkStatus.OFFLINE);
	}
	
	public NetworkStatus getNetworkStatus()
	{
		return (NetworkStatus) getProperty(AppModel.NETWORK_STATUS);
	}
	
	public void setNetworkStatus(NetworkStatus status)
	{
		setProperty(AppModel.NETWORK_STATUS, status);
	}
	
	public boolean isOffline()
	{
		NetworkStatus status = getNetworkStatus();
		
		switch(status)
		{
		case ONLINE:
			return false;
		case OFFLINE:
			return true;
		default:
			return true;
		}
	}
	
	public boolean isLoggedIn()
	{
		NetworkStatus status = getNetworkStatus();
		
		switch(status)
		{
		case ONLINE:
			return true;
		case OFFLINE:
			return false;
		default:
			return false;
		}
	}
	
}
