
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
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


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="presence" use="required" type="{http://www.tridas.org/1.2.2}presenceAbsence" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "bark")
public class TridasBark
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlAttribute(name = "presence", required = true)
    protected PresenceAbsence presence;

    /**
     * Gets the value of the presence property.
     * 
     * @return
     *     possible object is
     *     {@link PresenceAbsence }
     *     
     */
    public PresenceAbsence getPresence() {
        return presence;
    }

    /**
     * Sets the value of the presence property.
     * 
     * @param value
     *     allowed object is
     *     {@link PresenceAbsence }
     *     
     */
    public void setPresence(PresenceAbsence value) {
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
            PresenceAbsence thePresence;
            thePresence = this.getPresence();
            strategy.appendField(locator, this, "presence", buffer, thePresence);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasBark)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasBark that = ((TridasBark) object);
        {
            PresenceAbsence lhsPresence;
            lhsPresence = this.getPresence();
            PresenceAbsence rhsPresence;
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
            PresenceAbsence thePresence;
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
        if (draftCopy instanceof TridasBark) {
            final TridasBark copy = ((TridasBark) draftCopy);
            if (this.isSetPresence()) {
                PresenceAbsence sourcePresence;
                sourcePresence = this.getPresence();
                PresenceAbsence copyPresence = ((PresenceAbsence) strategy.copy(LocatorUtils.property(locator, "presence", sourcePresence), sourcePresence));
                copy.setPresence(copyPresence);
            } else {
                copy.presence = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasBark();
    }

}
