package edu.cornell.dendro.corina.tridas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import org.jdom.Element;

public class TridasObject extends TridasEntityBase implements Comparable {
	public TridasObject(String id, String name, String code) {
		super(id, name);
		this.code = code;
		
		subsites = new ArrayList<Subsite>();
		regions = new HashMap<String, String>();
	}
		
	/** The site's code (e.g., CYL) */
	private String code;
	
	/** A list of any subsites */
	private List<Subsite> subsites;
	
	/*
	 * Region stuff
	 */
	private HashMap<String, String> regions;
	
	/**
	 * Add this site to a particular region
	 * 
	 * @param regionID
	 * @param regionName
	 */
	public void addRegion(String regionID, String regionName) {
		regions.put(regionID, regionName);
	}
	
	/**
	 * Check to see if this site is in a region, by id
	 * 
	 * @param regionID
	 * @return
	 */
	public boolean inRegion(String regionID) {
		return regions.containsKey(regionID);
	}
	
	public String toFullString() {
		if(code.equals(title))
			return title;
		
		return "[" + code + "] " + title;
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
	
	public static TridasObject xmlToSite(Element root) {
		String id, name, code;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Site lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		code = root.getChildText("code");
		if(name == null || code == null) {
			System.out.println("Site lacking a name or code? " + root.toString());
			return null;			
		}
		
		TridasObject site = new TridasObject(id, name, code);
		
		// catch a region tag (or multiple)
		for(Element region : (List<Element>) root.getChildren("region")) {
			if(((id = region.getAttributeValue("id")) != null)) {
				site.addRegion(id, region.getText());
			}			
		}
		
		// now, get any subsites
		Element child = root.getChild("references");
		if(child != null) {
			List<Element> subsites = child.getChildren("subSite");
			
			for(Element ss : subsites) {
				Subsite subsite = Subsite.xmlToSubsite(ss);

				// add our subsite
				if(subsite != null)
					site.addSubsite(subsite);
			}
		}

		// sort the subsites alphabetically
		site.sortSubsites();
		
		// handle our links
		site.setResourceIdentifierFromElement(root);
		
		return site;
	}
	
	public Element toXML() {
		Element root = new Element("site");
		
		if(!isNew())
			root.setAttribute("id", getID());
		
		root.addContent(new Element("name").setText(title));
		root.addContent(new Element("code").setText(code));
		
		// are these necessary?
		// TODO: Regions?
		// TODO: Subsites?
		
		return root;
	}
}
