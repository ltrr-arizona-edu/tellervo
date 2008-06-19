package edu.cornell.dendro.corina.site;

import org.jdom.Element;

public class Specimen extends GenericIntermediateObject {
	public Specimen(String id, String name) {
		super(id, name);
	}
	
	private String specimenType;
	private String dateCollected;

	// combos
	private String specimenContinuity;
	private Boolean isSpecimenContinuityVerified;
	private String terminalRing;
	private Boolean isTerminalRingVerified;
	private String pith;
	private Boolean isPithVerified;
	
	// spinners
	private Integer sapwoodCount;
	private Boolean isSapwoodCountVerified;
	private Integer unmeasuredPre;
	private Boolean isUnmeasuredPreVerified;
	private Integer unmeasuredPost;
	private Boolean isUnmeasuredPostVerified;
	
	
	public static Specimen xmlToSpecimen(Element root) {
		String id, name;
		
		id = root.getAttributeValue("id");
		if(id == null) {
			System.out.println("Specimen lacking an id? " + root.toString());
			return null;
		}
		
		name = root.getChildText("name");
		if(name == null) {
			System.out.println("Specimen lacking an name? " + root.toString());
			return null;			
		}
		
		Specimen specimen = new Specimen(id, name);
		
		// TODO: care about everything else!
		
		specimen.setResourceIdentifierFromElement(root);
		
		return specimen;
	}
	
	public Element toXML() {
		Element root = new Element("specimen");
		
		if(!isNew())
			root.setAttribute("id", getID());

		root.addContent(new Element("name").setText(name));
		
		if(specimenType != null)
			root.addContent(new Element("specimenType").setText(specimenType));
		if(dateCollected != null)
			root.addContent(new Element("dateCollected").setText(dateCollected));

		if(specimenContinuity != null) {
			root.addContent(new Element("specimenContinuity").setText(specimenContinuity));
			if(isSpecimenContinuityVerified != null)
				root.addContent(new Element("isSpecimenContinuityVerified").
						setText(isSpecimenContinuityVerified.toString()));
		}
		
		if(terminalRing != null) {
			root.addContent(new Element("terminalRing").setText(terminalRing));
			if(isTerminalRingVerified != null)
				root.addContent(new Element("isTerminalRingVerified").
						setText(isTerminalRingVerified.toString()));
		}

		if(pith != null) {
			root.addContent(new Element("pith").setText(pith));
			if(isPithVerified != null)
				root.addContent(new Element("isPithVerified").
						setText(isPithVerified.toString()));
		}

		if(sapwoodCount != null) {
			root.addContent(new Element("sapwoodCount").setText(sapwoodCount.toString()));
			if(isSapwoodCountVerified != null)
				root.addContent(new Element("isSapwoodCountVerified").
						setText(isSapwoodCountVerified.toString()));
		}

		if(unmeasuredPre != null) {
			root.addContent(new Element("unmeasuredPre").setText(unmeasuredPre.toString()));
			if(isUnmeasuredPreVerified != null)
				root.addContent(new Element("isUnmeasuredPreVerified").
						setText(isUnmeasuredPreVerified.toString()));
		}

		if(unmeasuredPost != null) {
			root.addContent(new Element("unmeasuredPost").setText(unmeasuredPost.toString()));
			if(isUnmeasuredPostVerified != null)
				root.addContent(new Element("isUnmeasuredPostVerified").
						setText(isUnmeasuredPostVerified.toString()));
		}

		return root;
	}

	/**
	 * @return the specimenType
	 */
	public String getSpecimenType() {
		return specimenType;
	}

	/**
	 * @param specimenType the specimenType to set
	 */
	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	/**
	 * @return the dateCollected
	 */
	public String getDateCollected() {
		return dateCollected;
	}

	/**
	 * @param dateCollected the dateCollected to set
	 */
	public void setDateCollected(String dateCollected) {
		this.dateCollected = dateCollected;
	}

	/**
	 * @return the specimenContinuity
	 */
	public String getSpecimenContinuity() {
		return specimenContinuity;
	}

	/**
	 * @param specimenContinuity the specimenContinuity to set
	 */
	public void setSpecimenContinuity(String specimenContinuity) {
		this.specimenContinuity = specimenContinuity;
	}

	/**
	 * @return the isSpecimenContinuityVerified
	 */
	public Boolean getIsSpecimenContinuityVerified() {
		return isSpecimenContinuityVerified;
	}

	/**
	 * @param isSpecimenContinuityVerified the isSpecimenContinuityVerified to set
	 */
	public void setIsSpecimenContinuityVerified(Boolean isSpecimenContinuityVerified) {
		this.isSpecimenContinuityVerified = isSpecimenContinuityVerified;
	}

	/**
	 * @return the terminalRing
	 */
	public String getTerminalRing() {
		return terminalRing;
	}

	/**
	 * @param terminalRing the terminalRing to set
	 */
	public void setTerminalRing(String terminalRing) {
		this.terminalRing = terminalRing;
	}

	/**
	 * @return the isTerminalRingVerified
	 */
	public Boolean getIsTerminalRingVerified() {
		return isTerminalRingVerified;
	}

	/**
	 * @param isTerminalRingVerified the isTerminalRingVerified to set
	 */
	public void setIsTerminalRingVerified(Boolean isTerminalRingVerified) {
		this.isTerminalRingVerified = isTerminalRingVerified;
	}

	/**
	 * @return the pith
	 */
	public String getPith() {
		return pith;
	}

	/**
	 * @param pith the pith to set
	 */
	public void setPith(String pith) {
		this.pith = pith;
	}

	/**
	 * @return the isPithVerified
	 */
	public Boolean getIsPithVerified() {
		return isPithVerified;
	}

	/**
	 * @param isPithVerified the isPithVerified to set
	 */
	public void setIsPithVerified(Boolean isPithVerified) {
		this.isPithVerified = isPithVerified;
	}

	/**
	 * @return the sapwoodCount
	 */
	public Integer getSapwoodCount() {
		return sapwoodCount;
	}

	/**
	 * @param sapwoodCount the sapwoodCount to set
	 */
	public void setSapwoodCount(Integer sapwoodCount) {
		this.sapwoodCount = sapwoodCount;
	}

	/**
	 * @return the isSapwoodCountVerified
	 */
	public Boolean getIsSapwoodCountVerified() {
		return isSapwoodCountVerified;
	}

	/**
	 * @param isSapwoodCountVerified the isSapwoodCountVerified to set
	 */
	public void setIsSapwoodCountVerified(Boolean isSapwoodCountVerified) {
		this.isSapwoodCountVerified = isSapwoodCountVerified;
	}

	/**
	 * @return the unmeasuredPre
	 */
	public Integer getUnmeasuredPre() {
		return unmeasuredPre;
	}

	/**
	 * @param unmeasuredPre the unmeasuredPre to set
	 */
	public void setUnmeasuredPre(Integer unmeasuredPre) {
		this.unmeasuredPre = unmeasuredPre;
	}

	/**
	 * @return the isUnmeasuredPreVerified
	 */
	public Boolean getIsUnmeasuredPreVerified() {
		return isUnmeasuredPreVerified;
	}

	/**
	 * @param isUnmeasuredPreVerified the isUnmeasuredPreVerified to set
	 */
	public void setIsUnmeasuredPreVerified(Boolean isUnmeasuredPreVerified) {
		this.isUnmeasuredPreVerified = isUnmeasuredPreVerified;
	}

	/**
	 * @return the unmeasuredPost
	 */
	public Integer getUnmeasuredPost() {
		return unmeasuredPost;
	}

	/**
	 * @param unmeasuredPost the unmeasuredPost to set
	 */
	public void setUnmeasuredPost(Integer unmeasuredPost) {
		this.unmeasuredPost = unmeasuredPost;
	}

	/**
	 * @return the isUnmeasuredPostVerified
	 */
	public Boolean getIsUnmeasuredPostVerified() {
		return isUnmeasuredPostVerified;
	}

	/**
	 * @param isUnmeasuredPostVerified the isUnmeasuredPostVerified to set
	 */
	public void setIsUnmeasuredPostVerified(Boolean isUnmeasuredPostVerified) {
		this.isUnmeasuredPostVerified = isUnmeasuredPostVerified;
	}

}
