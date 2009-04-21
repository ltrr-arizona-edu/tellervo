package edu.cornell.dendro.corina.tridas;

import org.jdom.Element;

import edu.cornell.dendro.corina.util.StringDictionary;
import edu.cornell.dendro.corina.webdbi.CorinaElement;

public class TridasIdentifier {
	private TridasDomain domain;
	private String id;
	private String entityName;
	
	/**
	 * Construct a tridas element from scratch
	 * @param domain
	 * @param id
	 * @param entityName
	 */
	public TridasIdentifier(TridasDomain domain, String id, String entityName) {
		this.domain = domain;
		this.id = id;
		this.entityName = entityName;
	}

	/**
	 * Create a new entity of the specified type
	 * @param entityName
	 * @return
	 */
	public static TridasIdentifier newEntity(String entityName) {
		return new TridasIdentifier(TridasDomain.DEFAULT_DOMAIN, ID_NEW, entityName);
	}
	
	/**
	 * Construct a tridas element from an XML JDOM source
	 * @param identifierElement
	 * @param entityName
	 */
	public TridasIdentifier(Element identifierElement, String entityName) {
		String val;
				
		if(identifierElement == null)
			throw new IllegalArgumentException("Null tridas identifier element");

		// Load the domain
		if((val = identifierElement.getAttributeValue("domain")) == null)
			domain = TridasDomain.NO_DOMAIN;
		else
			domain = TridasDomain.getDomain(val);
		
		// Load the value
		if((val = identifierElement.getText()).length() == 0)
			throw new IllegalArgumentException("Tridas Identifier cannot be empty");
		else
			id = val;
		
		// and just use the provided entity name
		this.entityName = StringDictionary.getEntityName(entityName);
	}
	
	/**
	 * Return the tridas domain
	 * @return
	 */
	public TridasDomain getDomain() {
		return domain;
	}
	
	/**
	 * Return the id for this element
	 * @return
	 */
	public String getID() {
		return id;
	}
	
	/**
	 * Get the TRIDAS name of this entity (eg object, element, sample, etc)
	 * @return
	 */
	public String getEntityName() {
		return entityName;
	}
	
	/**
	 * Returns this identifier as a corina webservice style tag
	 *   <entity type="xxx" id="xxx" />
	 * @return
	 */
	public Element toCorinaEntityTag() {
		// can't do this for new elements
		if(id.equals(ID_NEW))
			throw new IllegalArgumentException("Cannot create request for new, uncommitted element");
		
		CorinaElement e = new CorinaElement("entity");
		
		e.setAttribute("type", entityName);
		e.setAttribute("id", id);
		
		return e;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof TridasIdentifier) {
			TridasIdentifier i = (TridasIdentifier) o;
			
			return domain.equals(i.domain) && 
					entityName.equals(i.entityName) &&
					id.equals(i.id);
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return domain.getDomainIdentifier() + ":" + entityName + ":" + id;
	}
	
	/** An identifier for a new entity */
	public final static String ID_NEW = "::NEW::";
}
