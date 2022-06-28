
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.Equals;
import org.jvnet.jaxb2_commons.lang.EqualsStrategy;
import org.jvnet.jaxb2_commons.lang.HashCode;
import org.jvnet.jaxb2_commons.lang.HashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBEqualsStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBHashCodeStrategy;
import org.jvnet.jaxb2_commons.lang.JAXBToStringStrategy;
import org.jvnet.jaxb2_commons.lang.ToString;
import org.jvnet.jaxb2_commons.lang.ToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;
import org.tridas.adapters.IntegerAdapter;


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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}missingHeartwoodRingsToPith" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}missingHeartwoodRingsToPithFoundation" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="presence" use="required" type="{http://www.tridas.org/1.2.2}complexPresenceAbsence" />
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
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer missingHeartwoodRingsToPith;
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
    public Integer getMissingHeartwoodRingsToPith() {
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
    public void setMissingHeartwoodRingsToPith(Integer value) {
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

    public String toString() {
        final ToStringStrategy strategy = JAXBToStringStrategy.INSTANCE;
        final StringBuilder buffer = new StringBuilder();
        append(null, buffer, strategy);
        return buffer.toString();
    }

    public StringBuilder append(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        strategy.appendStart(locator, this, buffer);
        appendFields(locator, buffer, strategy);
        strategy.appendEnd(locator, this, buffer);
        return buffer;
    }

    public StringBuilder appendFields(ObjectLocator locator, StringBuilder buffer, ToStringStrategy strategy) {
        {
            Integer theMissingHeartwoodRingsToPith;
            theMissingHeartwoodRingsToPith = this.getMissingHeartwoodRingsToPith();
            strategy.appendField(locator, this, "missingHeartwoodRingsToPith", buffer, theMissingHeartwoodRingsToPith);
        }
        {
            String theMissingHeartwoodRingsToPithFoundation;
            theMissingHeartwoodRingsToPithFoundation = this.getMissingHeartwoodRingsToPithFoundation();
            strategy.appendField(locator, this, "missingHeartwoodRingsToPithFoundation", buffer, theMissingHeartwoodRingsToPithFoundation);
        }
        {
            ComplexPresenceAbsence thePresence;
            thePresence = this.getPresence();
            strategy.appendField(locator, this, "presence", buffer, thePresence);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasHeartwood)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasHeartwood that = ((TridasHeartwood) object);
        {
            Integer lhsMissingHeartwoodRingsToPith;
            lhsMissingHeartwoodRingsToPith = this.getMissingHeartwoodRingsToPith();
            Integer rhsMissingHeartwoodRingsToPith;
            rhsMissingHeartwoodRingsToPith = that.getMissingHeartwoodRingsToPith();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "missingHeartwoodRingsToPith", lhsMissingHeartwoodRingsToPith), LocatorUtils.property(thatLocator, "missingHeartwoodRingsToPith", rhsMissingHeartwoodRingsToPith), lhsMissingHeartwoodRingsToPith, rhsMissingHeartwoodRingsToPith)) {
                return false;
            }
        }
        {
            String lhsMissingHeartwoodRingsToPithFoundation;
            lhsMissingHeartwoodRingsToPithFoundation = this.getMissingHeartwoodRingsToPithFoundation();
            String rhsMissingHeartwoodRingsToPithFoundation;
            rhsMissingHeartwoodRingsToPithFoundation = that.getMissingHeartwoodRingsToPithFoundation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "missingHeartwoodRingsToPithFoundation", lhsMissingHeartwoodRingsToPithFoundation), LocatorUtils.property(thatLocator, "missingHeartwoodRingsToPithFoundation", rhsMissingHeartwoodRingsToPithFoundation), lhsMissingHeartwoodRingsToPithFoundation, rhsMissingHeartwoodRingsToPithFoundation)) {
                return false;
            }
        }
        {
            ComplexPresenceAbsence lhsPresence;
            lhsPresence = this.getPresence();
            ComplexPresenceAbsence rhsPresence;
            rhsPresence = that.getPresence();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "presence", lhsPresence), LocatorUtils.property(thatLocator, "presence", rhsPresence), lhsPresence, rhsPresence)) {
                return false;
            }
        }
        return true;
    }

    public boolean equals(Object object) {
        final EqualsStrategy strategy = JAXBEqualsStrategy.INSTANCE;
        return equals(null, null, object, strategy);
    }

    public int hashCode(ObjectLocator locator, HashCodeStrategy strategy) {
        int currentHashCode = 1;
        {
            Integer theMissingHeartwoodRingsToPith;
            theMissingHeartwoodRingsToPith = this.getMissingHeartwoodRingsToPith();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "missingHeartwoodRingsToPith", theMissingHeartwoodRingsToPith), currentHashCode, theMissingHeartwoodRingsToPith);
        }
        {
            String theMissingHeartwoodRingsToPithFoundation;
            theMissingHeartwoodRingsToPithFoundation = this.getMissingHeartwoodRingsToPithFoundation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "missingHeartwoodRingsToPithFoundation", theMissingHeartwoodRingsToPithFoundation), currentHashCode, theMissingHeartwoodRingsToPithFoundation);
        }
        {
            ComplexPresenceAbsence thePresence;
            thePresence = this.getPresence();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "presence", thePresence), currentHashCode, thePresence);
        }
        return currentHashCode;
    }

    public int hashCode() {
        final HashCodeStrategy strategy = JAXBHashCodeStrategy.INSTANCE;
        return this.hashCode(null, strategy);
    }

    public Object clone() {
        return copyTo(createNewInstance());
    }

    public Object copyTo(Object target) {
        final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
        return copyTo(null, target, strategy);
    }

    public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
        final Object draftCopy = ((target == null)?createNewInstance():target);
        if (draftCopy instanceof TridasHeartwood) {
            final TridasHeartwood copy = ((TridasHeartwood) draftCopy);
            if (this.isSetMissingHeartwoodRingsToPith()) {
                Integer sourceMissingHeartwoodRingsToPith;
                sourceMissingHeartwoodRingsToPith = this.getMissingHeartwoodRingsToPith();
                Integer copyMissingHeartwoodRingsToPith = ((Integer) strategy.copy(LocatorUtils.property(locator, "missingHeartwoodRingsToPith", sourceMissingHeartwoodRingsToPith), sourceMissingHeartwoodRingsToPith));
                copy.setMissingHeartwoodRingsToPith(copyMissingHeartwoodRingsToPith);
            } else {
                copy.missingHeartwoodRingsToPith = null;
            }
            if (this.isSetMissingHeartwoodRingsToPithFoundation()) {
                String sourceMissingHeartwoodRingsToPithFoundation;
                sourceMissingHeartwoodRingsToPithFoundation = this.getMissingHeartwoodRingsToPithFoundation();
                String copyMissingHeartwoodRingsToPithFoundation = ((String) strategy.copy(LocatorUtils.property(locator, "missingHeartwoodRingsToPithFoundation", sourceMissingHeartwoodRingsToPithFoundation), sourceMissingHeartwoodRingsToPithFoundation));
                copy.setMissingHeartwoodRingsToPithFoundation(copyMissingHeartwoodRingsToPithFoundation);
            } else {
                copy.missingHeartwoodRingsToPithFoundation = null;
            }
            if (this.isSetPresence()) {
                ComplexPresenceAbsence sourcePresence;
                sourcePresence = this.getPresence();
                ComplexPresenceAbsence copyPresence = ((ComplexPresenceAbsence) strategy.copy(LocatorUtils.property(locator, "presence", sourcePresence), sourcePresence));
                copy.setPresence(copyPresence);
            } else {
                copy.presence = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasHeartwood();
    }

}
