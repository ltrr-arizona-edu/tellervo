package org.tellervo.desktop.metadataexport;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import org.tridas.interfaces.ITridas;

public class MetadataExportTemplate implements Serializable{

	
	private static final long serialVersionUID = 6335764600419541332L;
	private String title;
	private Class<ITridas> forClass;
	private ArrayList<MetadataExportColumn> columns;
	private File filename;
	
	
	
	public MetadataExportTemplate()
	{
		
	}

	
	
	

	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public Class<ITridas> getForClass() {
		return forClass;
	}


	public void setForClass(Class<ITridas> forClass) {
		this.forClass = forClass;
	}


	public File getFilename() {
		return filename;
	}

	public void setFilename(File filename) {
		this.filename = filename;
	}
	
	
	
	
}
