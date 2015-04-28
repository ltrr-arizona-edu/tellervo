package org.tellervo.desktop.io;

import org.tridas.interfaces.ITridas;

public class IdentityItem {





	private ITridas item;
	private String code;
	private boolean dbChecked = false;
	private boolean inDatabase = false;
	
	
	
	public IdentityItem()
	{
			
		
	}
	
	public String toString()
	{
		return this.code;
	}
	
	public ITridas getItem() {
		return item;
	}



	public void setItem(ITridas item) {
		this.item = item;
	}



	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public boolean isDbChecked() {
		return dbChecked;
	}



	public void setDbChecked(boolean dbChecked) {
		this.dbChecked = dbChecked;
		if(dbChecked==false) this.inDatabase = false;
	}



	public boolean isInDatabase() {
		return inDatabase;
	}



	public void setInDatabase(boolean inDatabase) {
		this.inDatabase = inDatabase;
	}
}
