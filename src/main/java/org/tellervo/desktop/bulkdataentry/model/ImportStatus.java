package org.tellervo.desktop.bulkdataentry.model;

public enum ImportStatus {

	LOCAL("Local only, not imported"),
	IMPORTED("Imported into database"),
	IMPORTED_WITH_LOCAL_EDITS ("In database, but with local unsaved edits");
	
	
	private String strname;
    private ImportStatus(String strname) {
        this.strname = strname;
    }
   
    @Override
    public String toString(){
        return strname;
    } 
}
