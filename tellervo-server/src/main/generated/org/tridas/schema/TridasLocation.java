
package org.tridas.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *       &lt;sequence>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}locationGeometry" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}locationType" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}locationPrecision" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}locationComment" minOccurs="0"/>
 *         &lt;element ref="{http://www.tridas.org/1.2.2}address" minOccurs="0"/>
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
    "locationGeometry",
    "locationType",
    "locationPrecision",
    "locationComment",
    "address"
})
@XmlRootElement(name = "location")
public class TridasLocation
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected TridasLocationGeometry locationGeometry;
    protected NormalTridasLocationType locationType;
    protected String locationPrecision;
    protected String locationComment;
    protected TridasAddress address;

    /**
     * Gets the value of the locationGeometry property.
     * 
     * @return
     *     possible object is
     *     {@link TridasLocationGeometry }
     *     
     */
    public TridasLocationGeometry getLocationGeometry() {
        return locationGeometry;
    }

    /**
     * Sets the value of the locationGeometry property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasLocationGeometry }
     *     
     */
    public void setLocationGeometry(TridasLocationGeometry value) {
        this.locationGeometry = value;
    }

    public boolean isSetLocationGeometry() {
        return (this.locationGeometry!= null);
    }

    /**
     * Gets the value of the locationType property.
     * 
     * @return
     *     possible object is
     *     {@link NormalTridasLocationType }
     *     
     */
    public NormalTridasLocationType getLocationType() {
        return locationType;
    }

    /**
     * Sets the value of the locationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link NormalTridasLocationType }
     *     
     */
    public void setLocationType(NormalTridasLocationType value) {
        this.locationType = value;
    }

    public boolean isSetLocationType() {
        return (this.locationType!= null);
    }

    /**
     * Gets the value of the locationPrecision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationPrecision() {
        return locationPrecision;
    }

    /**
     * Sets the value of the locationPrecision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationPrecision(String value) {
        this.locationPrecision = value;
    }

    public boolean isSetLocationPrecision() {
        return (this.locationPrecision!= null);
    }

    /**
     * Gets the value of the locationComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationComment() {
        return locationComment;
    }

    /**
     * Sets the value of the locationComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationComment(String value) {
        this.locationComment = value;
    }

    public boolean isSetLocationComment() {
        return (this.locationComment!= null);
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link TridasAddress }
     *     
     */
    public TridasAddress getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link TridasAddress }
     *     
     */
    public void setAddress(TridasAddress value) {
        this.address = value;
    }

    public boolean isSetAddress() {
        return (this.address!= null);
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
            TridasLocationGeometry theLocationGeometry;
            theLocationGeometry = this.getLocationGeometry();
            strategy.appendField(locator, this, "locationGeometry", buffer, theLocationGeometry);
        }
        {
            NormalTridasLocationType theLocationType;
            theLocationType = this.getLocationType();
            strategy.appendField(locator, this, "locationType", buffer, theLocationType);
        }
        {
            String theLocationPrecision;
            theLocationPrecision = this.getLocationPrecision();
            strategy.appendField(locator, this, "locationPrecision", buffer, theLocationPrecision);
        }
        {
            String theLocationComment;
            theLocationComment = this.getLocationComment();
            strategy.appendField(locator, this, "locationComment", buffer, theLocationComment);
        }
        {
            TridasAddress theAddress;
            theAddress = this.getAddress();
            strategy.appendField(locator, this, "address", buffer, theAddress);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasLocation)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasLocation that = ((TridasLocation) object);
        {
            TridasLocationGeometry lhsLocationGeometry;
            lhsLocationGeometry = this.getLocationGeometry();
            TridasLocationGeometry rhsLocationGeometry;
            rhsLocationGeometry = that.getLocationGeometry();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "locationGeometry", lhsLocationGeometry), LocatorUtils.property(thatLocator, "locationGeometry", rhsLocationGeometry), lhsLocationGeometry, rhsLocationGeometry)) {
                return false;
            }
        }
        {
            NormalTridasLocationType lhsLocationType;
            lhsLocationType = this.getLocationType();
            NormalTridasLocationType rhsLocationType;
            rhsLocationType = that.getLocationType();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "locationType", lhsLocationType), LocatorUtils.property(thatLocator, "locationType", rhsLocationType), lhsLocationType, rhsLocationType)) {
                return false;
            }
        }
        {
            String lhsLocationPrecision;
            lhsLocationPrecision = this.getLocationPrecision();
            String rhsLocationPrecision;
            rhsLocationPrecision = that.getLocationPrecision();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "locationPrecision", lhsLocationPrecision), LocatorUtils.property(thatLocator, "locationPrecision", rhsLocationPrecision), lhsLocationPrecision, rhsLocationPrecision)) {
                return false;
            }
        }
        {
            String lhsLocationComment;
            lhsLocationComment = this.getLocationComment();
            String rhsLocationComment;
            rhsLocationComment = that.getLocationComment();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "locationComment", lhsLocationComment), LocatorUtils.property(thatLocator, "locationComment", rhsLocationComment), lhsLocationComment, rhsLocationComment)) {
                return false;
            }
        }
        {
            TridasAddress lhsAddress;
            lhsAddress = this.getAddress();
            TridasAddress rhsAddress;
            rhsAddress = that.getAddress();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "address", lhsAddress), LocatorUtils.property(thatLocator, "address", rhsAddress), lhsAddress, rhsAddress)) {
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
            TridasLocationGeometry theLocationGeometry;
            theLocationGeometry = this.getLocationGeometry();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "locationGeometry", theLocationGeometry), currentHashCode, theLocationGeometry);
        }
        {
            NormalTridasLocationType theLocationType;
            theLocationType = this.getLocationType();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "locationType", theLocationType), currentHashCode, theLocationType);
        }
        {
            String theLocationPrecision;
            theLocationPrecision = this.getLocationPrecision();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "locationPrecision", theLocationPrecision), currentHashCode, theLocationPrecision);
        }
        {
            String theLocationComment;
            theLocationComment = this.getLocationComment();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "locationComment", theLocationComment), currentHashCode, theLocationComment);
        }
        {
            TridasAddress theAddress;
            theAddress = this.getAddress();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "address", theAddress), currentHashCode, theAddress);
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
        if (draftCopy instanceof TridasLocation) {
            final TridasLocation copy = ((TridasLocation) draftCopy);
            if (this.isSetLocationGeometry()) {
                TridasLocationGeometry sourceLocationGeometry;
                sourceLocationGeometry = this.getLocationGeometry();
                TridasLocationGeometry copyLocationGeometry = ((TridasLocationGeometry) strategy.copy(LocatorUtils.property(locator, "locationGeometry", sourceLocationGeometry), sourceLocationGeometry));
                copy.setLocationGeometry(copyLocationGeometry);
            } else {
                copy.locationGeometry = null;
            }
            if (this.isSetLocationType()) {
                NormalTridasLocationType sourceLocationType;
                sourceLocationType = this.getLocationType();
                NormalTridasLocationType copyLocationType = ((NormalTridasLocationType) strategy.copy(LocatorUtils.property(locator, "locationType", sourceLocationType), sourceLocationType));
                copy.setLocationType(copyLocationType);
            } else {
                copy.locationType = null;
            }
            if (this.isSetLocationPrecision()) {
                String sourceLocationPrecision;
                sourceLocationPrecision = this.getLocationPrecision();
                String copyLocationPrecision = ((String) strategy.copy(LocatorUtils.property(locator, "locationPrecision", sourceLocationPrecision), sourceLocationPrecision));
                copy.setLocationPrecision(copyLocationPrecision);
            } else {
                copy.locationPrecision = null;
            }
            if (this.isSetLocationComment()) {
                String sourceLocationComment;
                sourceLocationComment = this.getLocationComment();
                String copyLocationComment = ((String) strategy.copy(LocatorUtils.property(locator, "locationComment", sourceLocationComment), sourceLocationComment));
                copy.setLocationComment(copyLocationComment);
            } else {
                copy.locationComment = null;
            }
            if (this.isSetAddress()) {
                TridasAddress sourceAddress;
                sourceAddress = this.getAddress();
                TridasAddress copyAddress = ((TridasAddress) strategy.copy(LocatorUtils.property(locator, "address", sourceAddress), sourceAddress));
                copy.setAddress(copyAddress);
            } else {
                copy.address = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasLocation();
    }

}
