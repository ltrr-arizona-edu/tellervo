package org.tellervo.desktop.bulkdataentry.model;

public enum ImportStatus {

	LOCAL("Local only, not imported"),
	IMPORTED("Imported into database"),
	IMPORTED_WITH_LOCAL_EDITS ("In database, but with local unsaved edits");
	
	
	private String longdesc;
    private ImportStatus(String longdesc) {
        this.longdesc = longdesc;
    }
   
    @Override
    public String toString(){
        return longdesc;
    } 
}
