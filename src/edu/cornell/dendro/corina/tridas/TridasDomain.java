package edu.cornell.dendro.corina.tridas;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A tridas domain is provided for in the tridas identifier tag:
 * 
 *   <tridas:identifier domain="dendro.cornell.edu/dev/">2031</tridas:identifier>
 *   
 * Where the domain attribute is the what we care about.
 * 
 * The client will be responsible for mapping tridas domains to actual
 * protocol schemes. This is very similar to the concept of namespaces in XML.
 * 
 * @author Lucas Madar
 *
 */

public class TridasDomain {
	/**
	 * The domain identifier for this URI
	 */
	private String domainURI;
	
	public static final TridasDomain NO_DOMAIN = new TridasDomain("");
	public static final TridasDomain DEFAULT_DOMAIN = new TridasDomain("_default_");
	
	/**
	 * Use the factory class methods instead
	 */
	private TridasDomain(String domainURI) {
		this.domainURI = domainURI;
	}
	
	/**
	 * Gets a tridas domain object for this URI
	 * 
	 * @param domainURI
	 * @return
	 */
	public static TridasDomain getDomain(String domainURI) {
		TridasDomain domain = domains.get(domainURI);
		
		// already exists? good
		if(domain != null)
			return domain;
		
		// make a new domain
		domain = new TridasDomain(domainURI);
		domains.put(domainURI, domain);
		
		return domain;
	}

	/**
	 * Returns the URI identifier for this domain
	 * Note that this is NOT a valid URI!
	 * @return
	 */
	public String getDomainIdentifier() {
		return domainURI;
	}
	
	/** A set of domain objects */
	private static HashMap<String, TridasDomain> domains;
	
	static {
		domains = new HashMap<String, TridasDomain>(4);
		
		domains.put(NO_DOMAIN.getDomainIdentifier(), NO_DOMAIN);
		domains.put(DEFAULT_DOMAIN.getDomainIdentifier(), NO_DOMAIN);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof TridasDomain) {
			return ((TridasDomain)o).domainURI.equals(domainURI);
		}
		
		return o.toString().equals(domainURI);
	}
}
