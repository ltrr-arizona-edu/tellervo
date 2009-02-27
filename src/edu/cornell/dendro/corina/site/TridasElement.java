package edu.cornell.dendro.corina.site;

import org.jdom.Element;

import edu.cornell.dendro.corina.core.App;
import edu.cornell.dendro.corina.dictionary.Taxon;

public class TridasElement extends TridasEntityBase {
	public TridasElement(String id, String name) {
		super(id, name);
	}
	
	// these are all stored as strings.
	// verification of input must be done in whatever is setting these!
	private String latitude = null;
	private String longitude = null;
	private String precision = null;
	private String originalTaxonName = null;
	private Taxon validatedTaxon = null;
	private Boolean isLiveTree = null;
		
	public static TridasElement xmlToTree(Element root) {
		String id, name, attr;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Tree lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		if(name == null) {
			System.out.println("Tree lacking an name? " + root.toString());
			return null;			
		}
				
		TridasElement tree = new TridasElement(id, name);

		attr = root.getChildText("latitude");
		tree.setLatitude(attr);

		attr = root.getChildText("longitude");
		tree.setLongitude(attr);

		attr = root.getChildText("precision");
		tree.setPrecision(attr);

		attr = root.getChildText("originalTaxonName");
		tree.setOriginalTaxonName(attr);

		// find the taxon in the dictionary?
		Element taxonElement = root.getChild("validatedTaxon");
		if(taxonElement != null && (id = taxonElement.getAttributeValue("id")) != null) {
			Taxon taxon = (Taxon) App.dictionary.getDictionaryMap("Taxon").get(id);
			tree.setValidatedTaxon(taxon);
		}
		
		tree.setResourceIdentifierFromElement(root);
		
		return tree;
	}
	
	public Element toXML() {
		Element root = new Element("tree");
		
		if(!isNew())
			root.setAttribute("id", getID());

		root.addContent(new Element("name").setText(title));

		if(latitude != null)
			root.addContent(new Element("latitude").setText(latitude));
		if(longitude != null)
			root.addContent(new Element("longitude").setText(longitude));
		if(precision != null)
			root.addContent(new Element("precision").setText(precision));
		if(originalTaxonName != null)
			root.addContent(new Element("originalTaxonName").setText(originalTaxonName));
		
		if(validatedTaxon != null) 
			root.addContent(new Element("validatedTaxon").
					setAttribute("id", validatedTaxon.getInternalRepresentation()).
					setText(validatedTaxon.getValue()));
		
		if(isLiveTree != null)
			root.addContent(new Element("isLiveTree").setText(isLiveTree.toString()));
		
		return root;
	}

	/**
	 * @return the latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * @return the precision
	 */
	public String getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(String precision) {
		this.precision = precision;
	}

	/**
	 * @return the originalTaxonName
	 */
	public String getOriginalTaxonName() {
		return originalTaxonName;
	}

	/**
	 * @param originalTaxonName the originalTaxonName to set
	 */
	public void setOriginalTaxonName(String originalTaxonName) {
		this.originalTaxonName = originalTaxonName;
	}

	/**
	 * @return the validatedTaxon
	 */
	public Taxon getValidatedTaxon() {
		return validatedTaxon;
	}

	/**
	 * @param validatedTaxon the validatedTaxon to set
	 */
	public void setValidatedTaxon(Taxon validatedTaxon) {
		this.validatedTaxon = validatedTaxon;
	}

	/**
	 * @return the isLiveTree
	 */
	public Boolean getIsLiveTree() {
		return isLiveTree;
	}

	/**
	 * @param isLiveTree the isLiveTree to set
	 */
	public void setIsLiveTree(Boolean isLiveTree) {
		this.isLiveTree = isLiveTree;
	}
}
