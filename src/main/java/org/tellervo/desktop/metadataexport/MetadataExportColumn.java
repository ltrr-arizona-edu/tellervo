package org.tellervo.desktop.metadataexport;

import java.io.Serializable;

public class MetadataExportColumn implements Serializable{

	private static final long serialVersionUID = 8467162189123781350L;
	private String title;
	private ExportColumnType type;
	private String value;
	

	public enum ExportColumnType {
		SINGLE, COMPOSITE, CONSTANT		
		
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public ExportColumnType getType() {
		return type;
	}


	public void setType(ExportColumnType type) {
		this.type = type;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}
	
	
	
	
	
}
