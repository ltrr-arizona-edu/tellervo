
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
 *         &lt;element ref="{http://www.tridas.org/1.3}missingHeartwoodRingsToPith" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.3}missingHeartwoodRingsToPithFoundation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="presence" use="required" type="{http://www.tridas.org/1.3}complexPresenceAbsence" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "missingHeartwoodRingsToPith",
    "missingHeartwoodRingsToPithFoundation"
})
@XmlRootElement(name = "heartwood")
public class TridasHeartwood
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
    protected String missingHeartwoodRingsToPith;
    protected String missingHeartwoodRingsToPithFoundation;
    @XmlAttribute(name = "presence", required = true)
    protected ComplexPresenceAbsence presence;

    /**
     * Gets the value of the missingHeartwoodRingsToPith property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMissingHeartwoodRingsToPith() {
        return missingHeartwoodRingsToPith;
    }

    /**
     * Sets the value of the missingHeartwoodRingsToPith property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMissingHeartwoodRingsToPith(String value) {
        this.missingHeartwoodRingsToPith = value;
    }

    public boolean isSetMissingHeartwoodRingsToPith() {
        return (this.missingHeartwoodRingsToPith!= null);
    }

    /**
     * Gets the value of the missingHeartwoodRingsToPithFoundation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMissingHeartwoodRingsToPithFoundation() {
        return missingHeartwoodRingsToPithFoundation;
    }

    /**
     * Sets the value of the missingHeartwoodRingsToPithFoundation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMissingHeartwoodRingsToPithFoundation(String value) {
        this.missingHeartwoodRingsToPithFoundation = value;
    }

    public boolean isSetMissingHeartwoodRingsToPithFoundation() {
        return (this.missingHeartwoodRingsToPithFoundation!= null);
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
        if (!(object instanceof TridasHeartwood)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasHeartwood that = ((TridasHeartwood) object);
        equalsBuilder.append(this.getMissingHeartwoodRingsToPith(), that.getMissingHeartwoodRingsToPith());
        equalsBuilder.append(this.getMissingHeartwoodRingsToPithFoundation(), that.getMissingHeartwoodRingsToPithFoundation());
        equalsBuilder.append(this.getPresence(), that.getPresence());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasHeartwood)) {
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
        hashCodeBuilder.append(this.getMissingHeartwoodRingsToPith());
        hashCodeBuilder.append(this.getMissingHeartwoodRingsToPithFoundation());
        hashCodeBuilder.append(this.getPresence());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theMissingHeartwoodRingsToPith;
            theMissingHeartwoodRingsToPith = this.getMissingHeartwoodRingsToPith();
            toStringBuilder.append("missingHeartwoodRingsToPith", theMissingHeartwoodRingsToPith);
        }
        {
            String theMissingHeartwoodRingsToPithFoundation;
            theMissingHeartwoodRingsToPithFoundation = this.getMissingHeartwoodRingsToPithFoundation();
            toStringBuilder.append("missingHeartwoodRingsToPithFoundation", theMissingHeartwoodRingsToPithFoundation);
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
        final TridasHeartwood copy = ((target == null)?((TridasHeartwood) createCopy()):((TridasHeartwood) target));
        if (this.isSetMissingHeartwoodRingsToPith()) {
            String sourceMissingHeartwoodRingsToPith;
            sourceMissingHeartwoodRingsToPith = this.getMissingHeartwoodRingsToPith();
            String copyMissingHeartwoodRingsToPith = ((String) copyBuilder.copy(sourceMissingHeartwoodRingsToPith));
            copy.setMissingHeartwoodRingsToPith(copyMissingHeartwoodRingsToPith);
        } else {
            copy.missingHeartwoodRingsToPith = null;
        }
        if (this.isSetMissingHeartwoodRingsToPithFoundation()) {
            String sourceMissingHeartwoodRingsToPithFoundation;
            sourceMissingHeartwoodRingsToPithFoundation = this.getMissingHeartwoodRingsToPithFoundation();
            String copyMissingHeartwoodRingsToPithFoundation = ((String) copyBuilder.copy(sourceMissingHeartwoodRingsToPithFoundation));
            copy.setMissingHeartwoodRingsToPithFoundation(copyMissingHeartwoodRingsToPithFoundation);
        } else {
            copy.missingHeartwoodRingsToPithFoundation = null;
        }
        if (this.isSetPresence()) {
            ComplexPresenceAbsence sourcePresence;
            sourcePresence = this.getPresence();
            ComplexPresenceAbsence copyPresence = ((ComplexPresenceAbsence) copyBuilder.copy(sourcePresence));
            copy.setPresence(copyPresence);
        } else {
            copy.presence = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasHeartwood();
    }

}
