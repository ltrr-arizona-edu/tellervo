
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
 *         &lt;element ref="{http://www.tridas.org/1.2.2}nrOfSapwoodRings" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}lastRingUnderBark" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}missingSapwoodRingsToBark" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}missingSapwoodRingsToBarkFoundation" minOccurs="0"/>
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
    "nrOfSapwoodRings",
    "lastRingUnderBark",
    "missingSapwoodRingsToBark",
    "missingSapwoodRingsToBarkFoundation"
})
@XmlRootElement(name = "sapwood")
public class TridasSapwood
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer nrOfSapwoodRings;
    protected TridasLastRingUnderBark lastRingUnderBark;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer missingSapwoodRingsToBark;
    protected String missingSapwoodRingsToBarkFoundation;
    @XmlAttribute(name = "presence", required = true)
    protected ComplexPresenceAbsence presence;

    /**
     * Gets the value of the nrOfSapwoodRings property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNrOfSapwoodRings() {
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
    public void setNrOfSapwoodRings(Integer value) {
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
    public Integer getMissingSapwoodRingsToBark() {
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
    public void setMissingSapwoodRingsToBark(Integer value) {
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
            Integer theNrOfSapwoodRings;
            theNrOfSapwoodRings = this.getNrOfSapwoodRings();
            strategy.appendField(locator, this, "nrOfSapwoodRings", buffer, theNrOfSapwoodRings);
        }
        {
            TridasLastRingUnderBark theLastRingUnderBark;
            theLastRingUnderBark = this.getLastRingUnderBark();
            strategy.appendField(locator, this, "lastRingUnderBark", buffer, theLastRingUnderBark);
        }
        {
            Integer theMissingSapwoodRingsToBark;
            theMissingSapwoodRingsToBark = this.getMissingSapwoodRingsToBark();
            strategy.appendField(locator, this, "missingSapwoodRingsToBark", buffer, theMissingSapwoodRingsToBark);
        }
        {
            String theMissingSapwoodRingsToBarkFoundation;
            theMissingSapwoodRingsToBarkFoundation = this.getMissingSapwoodRingsToBarkFoundation();
            strategy.appendField(locator, this, "missingSapwoodRingsToBarkFoundation", buffer, theMissingSapwoodRingsToBarkFoundation);
        }
        {
            ComplexPresenceAbsence thePresence;
            thePresence = this.getPresence();
            strategy.appendField(locator, this, "presence", buffer, thePresence);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasSapwood)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasSapwood that = ((TridasSapwood) object);
        {
            Integer lhsNrOfSapwoodRings;
            lhsNrOfSapwoodRings = this.getNrOfSapwoodRings();
            Integer rhsNrOfSapwoodRings;
            rhsNrOfSapwoodRings = that.getNrOfSapwoodRings();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "nrOfSapwoodRings", lhsNrOfSapwoodRings), LocatorUtils.property(thatLocator, "nrOfSapwoodRings", rhsNrOfSapwoodRings), lhsNrOfSapwoodRings, rhsNrOfSapwoodRings)) {
                return false;
            }
        }
        {
            TridasLastRingUnderBark lhsLastRingUnderBark;
            lhsLastRingUnderBark = this.getLastRingUnderBark();
            TridasLastRingUnderBark rhsLastRingUnderBark;
            rhsLastRingUnderBark = that.getLastRingUnderBark();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "lastRingUnderBark", lhsLastRingUnderBark), LocatorUtils.property(thatLocator, "lastRingUnderBark", rhsLastRingUnderBark), lhsLastRingUnderBark, rhsLastRingUnderBark)) {
                return false;
            }
        }
        {
            Integer lhsMissingSapwoodRingsToBark;
            lhsMissingSapwoodRingsToBark = this.getMissingSapwoodRingsToBark();
            Integer rhsMissingSapwoodRingsToBark;
            rhsMissingSapwoodRingsToBark = that.getMissingSapwoodRingsToBark();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "missingSapwoodRingsToBark", lhsMissingSapwoodRingsToBark), LocatorUtils.property(thatLocator, "missingSapwoodRingsToBark", rhsMissingSapwoodRingsToBark), lhsMissingSapwoodRingsToBark, rhsMissingSapwoodRingsToBark)) {
                return false;
            }
        }
        {
            String lhsMissingSapwoodRingsToBarkFoundation;
            lhsMissingSapwoodRingsToBarkFoundation = this.getMissingSapwoodRingsToBarkFoundation();
            String rhsMissingSapwoodRingsToBarkFoundation;
            rhsMissingSapwoodRingsToBarkFoundation = that.getMissingSapwoodRingsToBarkFoundation();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "missingSapwoodRingsToBarkFoundation", lhsMissingSapwoodRingsToBarkFoundation), LocatorUtils.property(thatLocator, "missingSapwoodRingsToBarkFoundation", rhsMissingSapwoodRingsToBarkFoundation), lhsMissingSapwoodRingsToBarkFoundation, rhsMissingSapwoodRingsToBarkFoundation)) {
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
            Integer theNrOfSapwoodRings;
            theNrOfSapwoodRings = this.getNrOfSapwoodRings();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "nrOfSapwoodRings", theNrOfSapwoodRings), currentHashCode, theNrOfSapwoodRings);
        }
        {
            TridasLastRingUnderBark theLastRingUnderBark;
            theLastRingUnderBark = this.getLastRingUnderBark();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "lastRingUnderBark", theLastRingUnderBark), currentHashCode, theLastRingUnderBark);
        }
        {
            Integer theMissingSapwoodRingsToBark;
            theMissingSapwoodRingsToBark = this.getMissingSapwoodRingsToBark();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "missingSapwoodRingsToBark", theMissingSapwoodRingsToBark), currentHashCode, theMissingSapwoodRingsToBark);
        }
        {
            String theMissingSapwoodRingsToBarkFoundation;
            theMissingSapwoodRingsToBarkFoundation = this.getMissingSapwoodRingsToBarkFoundation();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "missingSapwoodRingsToBarkFoundation", theMissingSapwoodRingsToBarkFoundation), currentHashCode, theMissingSapwoodRingsToBarkFoundation);
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
        if (draftCopy instanceof TridasSapwood) {
            final TridasSapwood copy = ((TridasSapwood) draftCopy);
            if (this.isSetNrOfSapwoodRings()) {
                Integer sourceNrOfSapwoodRings;
                sourceNrOfSapwoodRings = this.getNrOfSapwoodRings();
                Integer copyNrOfSapwoodRings = ((Integer) strategy.copy(LocatorUtils.property(locator, "nrOfSapwoodRings", sourceNrOfSapwoodRings), sourceNrOfSapwoodRings));
                copy.setNrOfSapwoodRings(copyNrOfSapwoodRings);
            } else {
                copy.nrOfSapwoodRings = null;
            }
            if (this.isSetLastRingUnderBark()) {
                TridasLastRingUnderBark sourceLastRingUnderBark;
                sourceLastRingUnderBark = this.getLastRingUnderBark();
                TridasLastRingUnderBark copyLastRingUnderBark = ((TridasLastRingUnderBark) strategy.copy(LocatorUtils.property(locator, "lastRingUnderBark", sourceLastRingUnderBark), sourceLastRingUnderBark));
                copy.setLastRingUnderBark(copyLastRingUnderBark);
            } else {
                copy.lastRingUnderBark = null;
            }
            if (this.isSetMissingSapwoodRingsToBark()) {
                Integer sourceMissingSapwoodRingsToBark;
                sourceMissingSapwoodRingsToBark = this.getMissingSapwoodRingsToBark();
                Integer copyMissingSapwoodRingsToBark = ((Integer) strategy.copy(LocatorUtils.property(locator, "missingSapwoodRingsToBark", sourceMissingSapwoodRingsToBark), sourceMissingSapwoodRingsToBark));
                copy.setMissingSapwoodRingsToBark(copyMissingSapwoodRingsToBark);
            } else {
                copy.missingSapwoodRingsToBark = null;
            }
            if (this.isSetMissingSapwoodRingsToBarkFoundation()) {
                String sourceMissingSapwoodRingsToBarkFoundation;
                sourceMissingSapwoodRingsToBarkFoundation = this.getMissingSapwoodRingsToBarkFoundation();
                String copyMissingSapwoodRingsToBarkFoundation = ((String) strategy.copy(LocatorUtils.property(locator, "missingSapwoodRingsToBarkFoundation", sourceMissingSapwoodRingsToBarkFoundation), sourceMissingSapwoodRingsToBarkFoundation));
                copy.setMissingSapwoodRingsToBarkFoundation(copyMissingSapwoodRingsToBarkFoundation);
            } else {
                copy.missingSapwoodRingsToBarkFoundation = null;
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
        return new TridasSapwood();
    }

}
