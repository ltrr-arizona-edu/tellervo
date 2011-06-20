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
