
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
 *         &lt;element name="addressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="addressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cityOrTown" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="stateProvinceRegion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "addressLine1",
    "addressLine2",
    "cityOrTown",
    "stateProvinceRegion",
    "postalCode",
    "country"
})
@XmlRootElement(name = "address")
public class TridasAddress
    implements Cloneable, CopyTo, Equals, HashCode, ToString
{

    protected String addressLine1;
    protected String addressLine2;
    protected String cityOrTown;
    protected String stateProvinceRegion;
    protected String postalCode;
    protected String country;

    /**
     * Gets the value of the addressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets the value of the addressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine1(String value) {
        this.addressLine1 = value;
    }

    public boolean isSetAddressLine1() {
        return (this.addressLine1 != null);
    }

    /**
     * Gets the value of the addressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets the value of the addressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine2(String value) {
        this.addressLine2 = value;
    }

    public boolean isSetAddressLine2() {
        return (this.addressLine2 != null);
    }

    /**
     * Gets the value of the cityOrTown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityOrTown() {
        return cityOrTown;
    }

    /**
     * Sets the value of the cityOrTown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityOrTown(String value) {
        this.cityOrTown = value;
    }

    public boolean isSetCityOrTown() {
        return (this.cityOrTown!= null);
    }

    /**
     * Gets the value of the stateProvinceRegion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateProvinceRegion() {
        return stateProvinceRegion;
    }

    /**
     * Sets the value of the stateProvinceRegion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateProvinceRegion(String value) {
        this.stateProvinceRegion = value;
    }

    public boolean isSetStateProvinceRegion() {
        return (this.stateProvinceRegion!= null);
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    public boolean isSetPostalCode() {
        return (this.postalCode!= null);
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    public boolean isSetCountry() {
        return (this.country!= null);
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
            String theAddressLine1;
            theAddressLine1 = this.getAddressLine1();
            strategy.appendField(locator, this, "addressLine1", buffer, theAddressLine1);
        }
        {
            String theAddressLine2;
            theAddressLine2 = this.getAddressLine2();
            strategy.appendField(locator, this, "addressLine2", buffer, theAddressLine2);
        }
        {
            String theCityOrTown;
            theCityOrTown = this.getCityOrTown();
            strategy.appendField(locator, this, "cityOrTown", buffer, theCityOrTown);
        }
        {
            String theStateProvinceRegion;
            theStateProvinceRegion = this.getStateProvinceRegion();
            strategy.appendField(locator, this, "stateProvinceRegion", buffer, theStateProvinceRegion);
        }
        {
            String thePostalCode;
            thePostalCode = this.getPostalCode();
            strategy.appendField(locator, this, "postalCode", buffer, thePostalCode);
        }
        {
            String theCountry;
            theCountry = this.getCountry();
            strategy.appendField(locator, this, "country", buffer, theCountry);
        }
        return buffer;
    }

    public boolean equals(ObjectLocator thisLocator, ObjectLocator thatLocator, Object object, EqualsStrategy strategy) {
        if (!(object instanceof TridasAddress)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        final TridasAddress that = ((TridasAddress) object);
        {
            String lhsAddressLine1;
            lhsAddressLine1 = this.getAddressLine1();
            String rhsAddressLine1;
            rhsAddressLine1 = that.getAddressLine1();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "addressLine1", lhsAddressLine1), LocatorUtils.property(thatLocator, "addressLine1", rhsAddressLine1), lhsAddressLine1, rhsAddressLine1)) {
                return false;
            }
        }
        {
            String lhsAddressLine2;
            lhsAddressLine2 = this.getAddressLine2();
            String rhsAddressLine2;
            rhsAddressLine2 = that.getAddressLine2();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "addressLine2", lhsAddressLine2), LocatorUtils.property(thatLocator, "addressLine2", rhsAddressLine2), lhsAddressLine2, rhsAddressLine2)) {
                return false;
            }
        }
        {
            String lhsCityOrTown;
            lhsCityOrTown = this.getCityOrTown();
            String rhsCityOrTown;
            rhsCityOrTown = that.getCityOrTown();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "cityOrTown", lhsCityOrTown), LocatorUtils.property(thatLocator, "cityOrTown", rhsCityOrTown), lhsCityOrTown, rhsCityOrTown)) {
                return false;
            }
        }
        {
            String lhsStateProvinceRegion;
            lhsStateProvinceRegion = this.getStateProvinceRegion();
            String rhsStateProvinceRegion;
            rhsStateProvinceRegion = that.getStateProvinceRegion();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "stateProvinceRegion", lhsStateProvinceRegion), LocatorUtils.property(thatLocator, "stateProvinceRegion", rhsStateProvinceRegion), lhsStateProvinceRegion, rhsStateProvinceRegion)) {
                return false;
            }
        }
        {
            String lhsPostalCode;
            lhsPostalCode = this.getPostalCode();
            String rhsPostalCode;
            rhsPostalCode = that.getPostalCode();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "postalCode", lhsPostalCode), LocatorUtils.property(thatLocator, "postalCode", rhsPostalCode), lhsPostalCode, rhsPostalCode)) {
                return false;
            }
        }
        {
            String lhsCountry;
            lhsCountry = this.getCountry();
            String rhsCountry;
            rhsCountry = that.getCountry();
            if (!strategy.equals(LocatorUtils.property(thisLocator, "country", lhsCountry), LocatorUtils.property(thatLocator, "country", rhsCountry), lhsCountry, rhsCountry)) {
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
            String theAddressLine1;
            theAddressLine1 = this.getAddressLine1();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "addressLine1", theAddressLine1), currentHashCode, theAddressLine1);
        }
        {
            String theAddressLine2;
            theAddressLine2 = this.getAddressLine2();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "addressLine2", theAddressLine2), currentHashCode, theAddressLine2);
        }
        {
            String theCityOrTown;
            theCityOrTown = this.getCityOrTown();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "cityOrTown", theCityOrTown), currentHashCode, theCityOrTown);
        }
        {
            String theStateProvinceRegion;
            theStateProvinceRegion = this.getStateProvinceRegion();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "stateProvinceRegion", theStateProvinceRegion), currentHashCode, theStateProvinceRegion);
        }
        {
            String thePostalCode;
            thePostalCode = this.getPostalCode();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "postalCode", thePostalCode), currentHashCode, thePostalCode);
        }
        {
            String theCountry;
            theCountry = this.getCountry();
            currentHashCode = strategy.hashCode(LocatorUtils.property(locator, "country", theCountry), currentHashCode, theCountry);
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
        if (draftCopy instanceof TridasAddress) {
            final TridasAddress copy = ((TridasAddress) draftCopy);
            if (this.isSetAddressLine1()) {
                String sourceAddressLine1;
                sourceAddressLine1 = this.getAddressLine1();
                String copyAddressLine1 = ((String) strategy.copy(LocatorUtils.property(locator, "addressLine1", sourceAddressLine1), sourceAddressLine1));
                copy.setAddressLine1(copyAddressLine1);
            } else {
                copy.addressLine1 = null;
            }
            if (this.isSetAddressLine2()) {
                String sourceAddressLine2;
                sourceAddressLine2 = this.getAddressLine2();
                String copyAddressLine2 = ((String) strategy.copy(LocatorUtils.property(locator, "addressLine2", sourceAddressLine2), sourceAddressLine2));
                copy.setAddressLine2(copyAddressLine2);
            } else {
                copy.addressLine2 = null;
            }
            if (this.isSetCityOrTown()) {
                String sourceCityOrTown;
                sourceCityOrTown = this.getCityOrTown();
                String copyCityOrTown = ((String) strategy.copy(LocatorUtils.property(locator, "cityOrTown", sourceCityOrTown), sourceCityOrTown));
                copy.setCityOrTown(copyCityOrTown);
            } else {
                copy.cityOrTown = null;
            }
            if (this.isSetStateProvinceRegion()) {
                String sourceStateProvinceRegion;
                sourceStateProvinceRegion = this.getStateProvinceRegion();
                String copyStateProvinceRegion = ((String) strategy.copy(LocatorUtils.property(locator, "stateProvinceRegion", sourceStateProvinceRegion), sourceStateProvinceRegion));
                copy.setStateProvinceRegion(copyStateProvinceRegion);
            } else {
                copy.stateProvinceRegion = null;
            }
            if (this.isSetPostalCode()) {
                String sourcePostalCode;
                sourcePostalCode = this.getPostalCode();
                String copyPostalCode = ((String) strategy.copy(LocatorUtils.property(locator, "postalCode", sourcePostalCode), sourcePostalCode));
                copy.setPostalCode(copyPostalCode);
            } else {
                copy.postalCode = null;
            }
            if (this.isSetCountry()) {
                String sourceCountry;
                sourceCountry = this.getCountry();
                String copyCountry = ((String) strategy.copy(LocatorUtils.property(locator, "country", sourceCountry), sourceCountry));
                copy.setCountry(copyCountry);
            } else {
                copy.country = null;
            }
        }
        return draftCopy;
    }

    public Object createNewInstance() {
        return new TridasAddress();
    }

}
