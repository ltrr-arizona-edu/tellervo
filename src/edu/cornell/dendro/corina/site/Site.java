package edu.cornell.dendro.corina.site;

public class Site {
	public Site(String id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;		
	}
	
	/** The site's internally represented id */
	private String id;

	/** The site's name */
	private String name;
	
	/** The site's code (e.g., CYL) */
	private String code;
	
	/*
	 * Region stuff
	 */
	private String regionID;
	private String regionName;
	
	public void setRegion(String regionID, String regionName) {
		this.regionID = regionID;
		this.regionName = regionName;
	}
	
	public boolean inRegion(String regionID) {
		// no region? not in a region :)
		if(this.regionID == null)
			return false;
		
		return this.regionID.equalsIgnoreCase(regionID);
	}
	
	public String toString() {
		if(code.equals(name))
			return name;
		
		return name + "[" + code + "]";
	}
	
	public String getID() {
		return id;
	}
	
	@Override
	public boolean equals(Object site) {
		// if it's a site, check ids.
		if(site instanceof Site)
			return (((Site)site).id.equals(this.id));
		
		return super.equals(site);
	}
}
