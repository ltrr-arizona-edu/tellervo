
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="angle" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="azimuth" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
    "angle",
    "azimuth"
})
@XmlRootElement(name = "slope")
public class TridasSlope
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer angle;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @XmlSchemaType(name = "int")
    protected Integer azimuth;

    /**
     * Gets the value of the angle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAngle() {
        return angle;
    }

    /**
     * Sets the value of the angle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAngle(Integer value) {
        this.angle = value;
    }

    public boolean isSetAngle() {
        return (this.angle!= null);
    }

    /**
     * Gets the value of the azimuth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAzimuth() {
        return azimuth;
    }

    /**
     * Sets the value of the azimuth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAzimuth(Integer value) {
        this.azimuth = value;
    }

    public boolean isSetAzimuth() {
        return (this.azimuth!= null);
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
            Integer theAngle;
            theAngle = this.getAngle();
            strategy.appendField(locator, this, "angle", buffer, theAngle);
        }
        {
            Integer theAzimuth;
            theAzimuth = this.getAzimuth();
            strategy.appendField(locator, this, "azimuth", buffer, theAzimuth);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasSlope)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasSlope that = ((TridasSlope) object);
        {
            Integer lhsAngle;
            lhsAngle = this.getAngle();
            Integer rhsAngle;
            rhsAngle = that.getAngle();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "angle", lhsAngle), LocatorUtils.property(thatLocator, "angle", rhsAngle), lhsAngle, rhsAngle)) {
                return false;
            }
        }
        {
            Integer lhsAzimuth;
            lhsAzimuth = this.getAzimuth();
            Integer rhsAzimuth;
            rhsAzimuth = that.getAzimuth();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "azimuth", lhsAzimuth), LocatorUtils.property(thatLocator, "azimuth", rhsAzimuth), lhsAzimuth, rhsAzimuth)) {
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
            Integer theAngle;
            theAngle = this.getAngle();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "angle", theAngle), currentHashCode, theAngle);
        }
        {
            Integer theAzimuth;
            theAzimuth = this.getAzimuth();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "azimuth", theAzimuth), currentHashCode, theAzimuth);
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
        if (draftCopy instanceof TridasSlope) {
            final TridasSlope copy = ((TridasSlope) draftCopy);
            if (this.isSetAngle()) {
                Integer sourceAngle;
                sourceAngle = this.getAngle();
                Integer copyAngle = ((Integer) strategy.copy(LocatorUtils.property(locator, "angle", sourceAngle), sourceAngle));
                copy.setAngle(copyAngle);
            } else {
                copy.angle = null;
            }
            if (this.isSetAzimuth()) {
                Integer sourceAzimuth;
                sourceAzimuth = this.getAzimuth();
                Integer copyAzimuth = ((Integer) strategy.copy(LocatorUtils.property(locator, "azimuth", sourceAzimuth), sourceAzimuth));
                copy.setAzimuth(copyAzimuth);
            } else {
                copy.azimuth = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasSlope();
    }

}
