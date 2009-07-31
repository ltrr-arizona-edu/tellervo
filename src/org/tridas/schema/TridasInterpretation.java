
package org.tridas.schema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Copyable;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.builder.CopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBCopyBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBEqualsBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBHashCodeBuilder;
import org.jvnet.jaxb2_commons.lang.builder.JAXBToStringBuilder;
import org.tridas.annotations.TridasEditProperties;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.3}dating" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}firstYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}datingReference" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}statFoundation" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}sproutYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}deathYear" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}provenance" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dating",
    "firstYear",
    "datingReference",
    "statFoundations",
    "sproutYear",
    "deathYear",
    "provenance"
})
@XmlRootElement(name = "interpretation")
public class TridasInterpretation
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected TridasDating dating;
    @TridasEditProperties(finalType = true, readOnly = true)
    protected Year firstYear;
    protected TridasDatingReference datingReference;
    @XmlElement(name = "statFoundation")
    @TridasEditProperties(readOnly = true)
    protected List<TridasStatFoundation> statFoundations;
    @TridasEditProperties(finalType = true, readOnly = true)
    protected Year sproutYear;
    @TridasEditProperties(finalType = true, readOnly = true)
    protected Year deathYear;
    protected String provenance;

    /**
     * Gets the value of the dating property.
     * 
     * @return
     *     possible object is
     *     {@link TridasDating }
     *     
     */
    public TridasDating getDating() {
        return dating;
    }

    /**
     * Sets the value of the dating property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasDating }
     *     
     */
    public void setDating(TridasDating value) {
        this.dating = value;
    }

    public boolean isSetDating() {
        return (this.dating!= null);
    }

    /**
     * Gets the value of the firstYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getFirstYear() {
        return firstYear;
    }

    /**
     * Sets the value of the firstYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setFirstYear(Year value) {
        this.firstYear = value;
    }

    public boolean isSetFirstYear() {
        return (this.firstYear!= null);
    }

    /**
     * Gets the value of the datingReference property.
     * 
     * @return
     *     possible object is
     *     {@link TridasDatingReference }
     *     
     */
    public TridasDatingReference getDatingReference() {
        return datingReference;
    }

    /**
     * Sets the value of the datingReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasDatingReference }
     *     
     */
    public void setDatingReference(TridasDatingReference value) {
        this.datingReference = value;
    }

    public boolean isSetDatingReference() {
        return (this.datingReference!= null);
    }

    /**
     * Gets the value of the statFoundations property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the statFoundations property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStatFoundations().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TridasStatFoundation }
     * 
     * 
     */
    public List<TridasStatFoundation> getStatFoundations() {
        if (statFoundations == null) {
            statFoundations = new ArrayList<TridasStatFoundation>();
        }
        return this.statFoundations;
    }

    public boolean isSetStatFoundations() {
        return ((this.statFoundations!= null)&&(!this.statFoundations.isEmpty()));
    }

    public void unsetStatFoundations() {
        this.statFoundations = null;
    }

    /**
     * Gets the value of the sproutYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getSproutYear() {
        return sproutYear;
    }

    /**
     * Sets the value of the sproutYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setSproutYear(Year value) {
        this.sproutYear = value;
    }

    public boolean isSetSproutYear() {
        return (this.sproutYear!= null);
    }

    /**
     * Gets the value of the deathYear property.
     * 
     * @return
     *     possible object is
     *     {@link Year }
     *     
     */
    public Year getDeathYear() {
        return deathYear;
    }

    /**
     * Sets the value of the deathYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link Year }
     *     
     */
    public void setDeathYear(Year value) {
        this.deathYear = value;
    }

    public boolean isSetDeathYear() {
        return (this.deathYear!= null);
    }

    /**
     * Gets the value of the provenance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvenance() {
        return provenance;
    }

    /**
     * Sets the value of the provenance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvenance(String value) {
        this.provenance = value;
    }

    public boolean isSetProvenance() {
        return (this.provenance!= null);
    }

    /**
     * Sets the value of the statFoundations property.
     * 
     * @param statFoundations
     *     allowed object is
     *     {@link TridasStatFoundation }
     *     
     */
    public void setStatFoundations(List<TridasStatFoundation> statFoundations) {
        this.statFoundations = statFoundations;
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasInterpretation)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasInterpretation that = ((TridasInterpretation) object);
        equalsBuilder.append(this.getDating(), that.getDating());
        equalsBuilder.append(this.getFirstYear(), that.getFirstYear());
        equalsBuilder.append(this.getDatingReference(), that.getDatingReference());
        equalsBuilder.append(this.getStatFoundations(), that.getStatFoundations());
        equalsBuilder.append(this.getSproutYear(), that.getSproutYear());
        equalsBuilder.append(this.getDeathYear(), that.getDeathYear());
        equalsBuilder.append(this.getProvenance(), that.getProvenance());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasInterpretation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final EqualsBuilder equalsBuilder = new JAXBEqualsBuilder();
        equals(object, equalsBuilder);
        return equalsBuilder.isEquals();
    }

    public void hashCode(HashCodeBuilder hashCodeBuilder) {
        hashCodeBuilder.append(this.getDating());
        hashCodeBuilder.append(this.getFirstYear());
        hashCodeBuilder.append(this.getDatingReference());
        hashCodeBuilder.append(this.getStatFoundations());
        hashCodeBuilder.append(this.getSproutYear());
        hashCodeBuilder.append(this.getDeathYear());
        hashCodeBuilder.append(this.getProvenance());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            TridasDating theDating;
            theDating = this.getDating();
            toStringBuilder.append("dating", theDating);
        }
        {
            Year theFirstYear;
            theFirstYear = this.getFirstYear();
            toStringBuilder.append("firstYear", theFirstYear);
        }
        {
            TridasDatingReference theDatingReference;
            theDatingReference = this.getDatingReference();
            toStringBuilder.append("datingReference", theDatingReference);
        }
        {
            List<TridasStatFoundation> theStatFoundations;
            theStatFoundations = this.getStatFoundations();
            toStringBuilder.append("statFoundations", theStatFoundations);
        }
        {
            Year theSproutYear;
            theSproutYear = this.getSproutYear();
            toStringBuilder.append("sproutYear", theSproutYear);
        }
        {
            Year theDeathYear;
            theDeathYear = this.getDeathYear();
            toStringBuilder.append("deathYear", theDeathYear);
        }
        {
            String theProvenance;
            theProvenance = this.getProvenance();
            toStringBuilder.append("provenance", theProvenance);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasInterpretation copy = ((target == null)?((TridasInterpretation) createCopy()):((TridasInterpretation) target));
        if (this.isSetDating()) {
            TridasDating sourceDating;
            sourceDating = this.getDating();
            TridasDating copyDating = ((TridasDating) copyBuilder.copy(sourceDating));
            copy.setDating(copyDating);
        } else {
            copy.dating = null;
        }
        if (this.isSetFirstYear()) {
            Year sourceFirstYear;
            sourceFirstYear = this.getFirstYear();
            Year copyFirstYear = ((Year) copyBuilder.copy(sourceFirstYear));
            copy.setFirstYear(copyFirstYear);
        } else {
            copy.firstYear = null;
        }
        if (this.isSetDatingReference()) {
            TridasDatingReference sourceDatingReference;
            sourceDatingReference = this.getDatingReference();
            TridasDatingReference copyDatingReference = ((TridasDatingReference) copyBuilder.copy(sourceDatingReference));
            copy.setDatingReference(copyDatingReference);
        } else {
            copy.datingReference = null;
        }
        if (this.isSetStatFoundations()) {
            List<TridasStatFoundation> sourceStatFoundations;
            sourceStatFoundations = this.getStatFoundations();
            @SuppressWarnings("unchecked")
            List<TridasStatFoundation> copyStatFoundations = ((List<TridasStatFoundation> ) copyBuilder.copy(sourceStatFoundations));
            copy.setStatFoundations(copyStatFoundations);
        } else {
            copy.unsetStatFoundations();
        }
        if (this.isSetSproutYear()) {
            Year sourceSproutYear;
            sourceSproutYear = this.getSproutYear();
            Year copySproutYear = ((Year) copyBuilder.copy(sourceSproutYear));
            copy.setSproutYear(copySproutYear);
        } else {
            copy.sproutYear = null;
        }
        if (this.isSetDeathYear()) {
            Year sourceDeathYear;
            sourceDeathYear = this.getDeathYear();
            Year copyDeathYear = ((Year) copyBuilder.copy(sourceDeathYear));
            copy.setDeathYear(copyDeathYear);
        } else {
            copy.deathYear = null;
        }
        if (this.isSetProvenance()) {
            String sourceProvenance;
            sourceProvenance = this.getProvenance();
            String copyProvenance = ((String) copyBuilder.copy(sourceProvenance));
            copy.setProvenance(copyProvenance);
        } else {
            copy.provenance = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasInterpretation();
    }

}
