
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{http://www.tridas.org/1.2}nrOfSapwoodRings" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}lastRingUnderBark" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}missingSapwoodRingsToBark" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2}missingSapwoodRingsToBarkFoundation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="presence" use="required" type="{http://www.tridas.org/1.2}complexPresenceAbsence" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "nrOfSapwoodRings",
    "lastRingUnderBark",
    "missingSapwoodRingsToBark",
    "missingSapwoodRingsToBarkFoundation"
})
@XmlRootElement(name = "sapwood")
public class TridasSapwood
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected String nrOfSapwoodRings;
    protected TridasLastRingUnderBark lastRingUnderBark;
    protected String missingSapwoodRingsToBark;
    protected String missingSapwoodRingsToBarkFoundation;
    @XmlAttribute(required = true)
    protected ComplexPresenceAbsence presence;

    /**
     * Gets the value of the nrOfSapwoodRings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNrOfSapwoodRings() {
        return nrOfSapwoodRings;
    }

    /**
     * Sets the value of the nrOfSapwoodRings property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNrOfSapwoodRings(String value) {
        this.nrOfSapwoodRings = value;
    }

    public boolean isSetNrOfSapwoodRings() {
        return (this.nrOfSapwoodRings!= null);
    }

    /**
     * Gets the value of the lastRingUnderBark property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLastRingUnderBark }
     *     
     */
    public TridasLastRingUnderBark getLastRingUnderBark() {
        return lastRingUnderBark;
    }

    /**
     * Sets the value of the lastRingUnderBark property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLastRingUnderBark }
     *     
     */
    public void setLastRingUnderBark(TridasLastRingUnderBark value) {
        this.lastRingUnderBark = value;
    }

    public boolean isSetLastRingUnderBark() {
        return (this.lastRingUnderBark!= null);
    }

    /**
     * Gets the value of the missingSapwoodRingsToBark property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMissingSapwoodRingsToBark() {
        return missingSapwoodRingsToBark;
    }

    /**
     * Sets the value of the missingSapwoodRingsToBark property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMissingSapwoodRingsToBark(String value) {
        this.missingSapwoodRingsToBark = value;
    }

    public boolean isSetMissingSapwoodRingsToBark() {
        return (this.missingSapwoodRingsToBark!= null);
    }

    /**
     * Gets the value of the missingSapwoodRingsToBarkFoundation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMissingSapwoodRingsToBarkFoundation() {
        return missingSapwoodRingsToBarkFoundation;
    }

    /**
     * Sets the value of the missingSapwoodRingsToBarkFoundation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMissingSapwoodRingsToBarkFoundation(String value) {
        this.missingSapwoodRingsToBarkFoundation = value;
    }

    public boolean isSetMissingSapwoodRingsToBarkFoundation() {
        return (this.missingSapwoodRingsToBarkFoundation!= null);
    }

    /**
     * Gets the value of the presence property.
     * 
     * @return
     *     possible object is
     *     {@link ComplexPresenceAbsence }
     *     
     */
    public ComplexPresenceAbsence getPresence() {
        return presence;
    }

    /**
     * Sets the value of the presence property.
     * 
     * @param value
     *     allowed object is
     *     {@link ComplexPresenceAbsence }
     *     
     */
    public void setPresence(ComplexPresenceAbsence value) {
        this.presence = value;
    }

    public boolean isSetPresence() {
        return (this.presence!= null);
    }

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasSapwood)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasSapwood that = ((TridasSapwood) object);
        equalsBuilder.append(this.getNrOfSapwoodRings(), that.getNrOfSapwoodRings());
        equalsBuilder.append(this.getLastRingUnderBark(), that.getLastRingUnderBark());
        equalsBuilder.append(this.getMissingSapwoodRingsToBark(), that.getMissingSapwoodRingsToBark());
        equalsBuilder.append(this.getMissingSapwoodRingsToBarkFoundation(), that.getMissingSapwoodRingsToBarkFoundation());
        equalsBuilder.append(this.getPresence(), that.getPresence());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasSapwood)) {
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
        hashCodeBuilder.append(this.getNrOfSapwoodRings());
        hashCodeBuilder.append(this.getLastRingUnderBark());
        hashCodeBuilder.append(this.getMissingSapwoodRingsToBark());
        hashCodeBuilder.append(this.getMissingSapwoodRingsToBarkFoundation());
        hashCodeBuilder.append(this.getPresence());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theNrOfSapwoodRings;
            theNrOfSapwoodRings = this.getNrOfSapwoodRings();
            toStringBuilder.append("nrOfSapwoodRings", theNrOfSapwoodRings);
        }
        {
            TridasLastRingUnderBark theLastRingUnderBark;
            theLastRingUnderBark = this.getLastRingUnderBark();
            toStringBuilder.append("lastRingUnderBark", theLastRingUnderBark);
        }
        {
            String theMissingSapwoodRingsToBark;
            theMissingSapwoodRingsToBark = this.getMissingSapwoodRingsToBark();
            toStringBuilder.append("missingSapwoodRingsToBark", theMissingSapwoodRingsToBark);
        }
        {
            String theMissingSapwoodRingsToBarkFoundation;
            theMissingSapwoodRingsToBarkFoundation = this.getMissingSapwoodRingsToBarkFoundation();
            toStringBuilder.append("missingSapwoodRingsToBarkFoundation", theMissingSapwoodRingsToBarkFoundation);
        }
        {
            ComplexPresenceAbsence thePresence;
            thePresence = this.getPresence();
            toStringBuilder.append("presence", thePresence);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasSapwood copy = ((target == null)?((TridasSapwood) createCopy()):((TridasSapwood) target));
        {
            String sourceNrOfSapwoodRings;
            sourceNrOfSapwoodRings = this.getNrOfSapwoodRings();
            String copyNrOfSapwoodRings = ((String) copyBuilder.copy(sourceNrOfSapwoodRings));
            copy.setNrOfSapwoodRings(copyNrOfSapwoodRings);
        }
        {
            TridasLastRingUnderBark sourceLastRingUnderBark;
            sourceLastRingUnderBark = this.getLastRingUnderBark();
            TridasLastRingUnderBark copyLastRingUnderBark = ((TridasLastRingUnderBark) copyBuilder.copy(sourceLastRingUnderBark));
            copy.setLastRingUnderBark(copyLastRingUnderBark);
        }
        {
            String sourceMissingSapwoodRingsToBark;
            sourceMissingSapwoodRingsToBark = this.getMissingSapwoodRingsToBark();
            String copyMissingSapwoodRingsToBark = ((String) copyBuilder.copy(sourceMissingSapwoodRingsToBark));
            copy.setMissingSapwoodRingsToBark(copyMissingSapwoodRingsToBark);
        }
        {
            String sourceMissingSapwoodRingsToBarkFoundation;
            sourceMissingSapwoodRingsToBarkFoundation = this.getMissingSapwoodRingsToBarkFoundation();
            String copyMissingSapwoodRingsToBarkFoundation = ((String) copyBuilder.copy(sourceMissingSapwoodRingsToBarkFoundation));
            copy.setMissingSapwoodRingsToBarkFoundation(copyMissingSapwoodRingsToBarkFoundation);
        }
        {
            ComplexPresenceAbsence sourcePresence;
            sourcePresence = this.getPresence();
            ComplexPresenceAbsence copyPresence = ((ComplexPresenceAbsence) copyBuilder.copy(sourcePresence));
            copy.setPresence(copyPresence);
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasSapwood();
    }

}
