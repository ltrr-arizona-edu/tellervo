package edu.cornell.dendro.corina.site;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public class Site {
	public Site(String id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
		
		subsites = new ArrayList<Subsite>();
	}
	
	/** The site's internally represented id */
	private String id;

	/** The site's name */
	private String name;
	
	/** The site's code (e.g., CYL) */
	private String code;
	
	/** A list of any subsites */
	private List<Subsite> subsites;
	
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
		
		return "[" + code + "] " + name;
	}
	
	public String getID() {
		return id;
	}
	
	public String getCode() {
		return code;
	}
	
	// add a subsite
	public void addSubsite(Subsite subsite) {
		subsites.add(subsite);
	}
	
	public void sortSubsites() {
		Collections.sort(subsites);
	}
	
	// get the subsite list
	public List<Subsite> getSubsites() {
		return subsites;
	}
	
	@Override
	public boolean equals(Object site) {
		// if it's a site, check ids.
		if(site instanceof Site)
			return (((Site)site).id.equals(this.id));
		
		return super.equals(site);
	}
}
