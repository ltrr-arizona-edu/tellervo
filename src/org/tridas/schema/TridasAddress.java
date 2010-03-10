
package org.tridas.schema;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
    implements Serializable, CopyTo, Copyable, Equals, HashCode, ToString
{

    private final static long serialVersionUID = 1001L;
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

    public void equals(Object object, EqualsBuilder equalsBuilder) {
        if (!(object instanceof TridasAddress)) {
            equalsBuilder.appendSuper(false);
            return ;
        }
        if (this == object) {
            return ;
        }
        final TridasAddress that = ((TridasAddress) object);
        equalsBuilder.append(this.getAddressLine1(), that.getAddressLine1());
        equalsBuilder.append(this.getAddressLine2(), that.getAddressLine2());
        equalsBuilder.append(this.getCityOrTown(), that.getCityOrTown());
        equalsBuilder.append(this.getStateProvinceRegion(), that.getStateProvinceRegion());
        equalsBuilder.append(this.getPostalCode(), that.getPostalCode());
        equalsBuilder.append(this.getCountry(), that.getCountry());
    }

    public boolean equals(Object object) {
        if (!(object instanceof TridasAddress)) {
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
        hashCodeBuilder.append(this.getAddressLine1());
        hashCodeBuilder.append(this.getAddressLine2());
        hashCodeBuilder.append(this.getCityOrTown());
        hashCodeBuilder.append(this.getStateProvinceRegion());
        hashCodeBuilder.append(this.getPostalCode());
        hashCodeBuilder.append(this.getCountry());
    }

    public int hashCode() {
        final HashCodeBuilder hashCodeBuilder = new JAXBHashCodeBuilder();
        hashCode(hashCodeBuilder);
        return hashCodeBuilder.toHashCode();
    }

    public void toString(ToStringBuilder toStringBuilder) {
        {
            String theAddressLine1;
            theAddressLine1 = this.getAddressLine1();
            toStringBuilder.append("addressLine1", theAddressLine1);
        }
        {
            String theAddressLine2;
            theAddressLine2 = this.getAddressLine2();
            toStringBuilder.append("addressLine2", theAddressLine2);
        }
        {
            String theCityOrTown;
            theCityOrTown = this.getCityOrTown();
            toStringBuilder.append("cityOrTown", theCityOrTown);
        }
        {
            String theStateProvinceRegion;
            theStateProvinceRegion = this.getStateProvinceRegion();
            toStringBuilder.append("stateProvinceRegion", theStateProvinceRegion);
        }
        {
            String thePostalCode;
            thePostalCode = this.getPostalCode();
            toStringBuilder.append("postalCode", thePostalCode);
        }
        {
            String theCountry;
            theCountry = this.getCountry();
            toStringBuilder.append("country", theCountry);
        }
    }

    public String toString() {
        final ToStringBuilder toStringBuilder = new JAXBToStringBuilder(this);
        toString(toStringBuilder);
        return toStringBuilder.toString();
    }

    public Object copyTo(Object target, CopyBuilder copyBuilder) {
        final TridasAddress copy = ((target == null)?((TridasAddress) createCopy()):((TridasAddress) target));
        if (this.isSetAddressLine1()) {
            String sourceAddressLine1;
            sourceAddressLine1 = this.getAddressLine1();
            String copyAddressLine1 = ((String) copyBuilder.copy(sourceAddressLine1));
            copy.setAddressLine1(copyAddressLine1);
        } else {
            copy.addressLine1 = null;
        }
        if (this.isSetAddressLine2()) {
            String sourceAddressLine2;
            sourceAddressLine2 = this.getAddressLine2();
            String copyAddressLine2 = ((String) copyBuilder.copy(sourceAddressLine2));
            copy.setAddressLine2(copyAddressLine2);
        } else {
            copy.addressLine2 = null;
        }
        if (this.isSetCityOrTown()) {
            String sourceCityOrTown;
            sourceCityOrTown = this.getCityOrTown();
            String copyCityOrTown = ((String) copyBuilder.copy(sourceCityOrTown));
            copy.setCityOrTown(copyCityOrTown);
        } else {
            copy.cityOrTown = null;
        }
        if (this.isSetStateProvinceRegion()) {
            String sourceStateProvinceRegion;
            sourceStateProvinceRegion = this.getStateProvinceRegion();
            String copyStateProvinceRegion = ((String) copyBuilder.copy(sourceStateProvinceRegion));
            copy.setStateProvinceRegion(copyStateProvinceRegion);
        } else {
            copy.stateProvinceRegion = null;
        }
        if (this.isSetPostalCode()) {
            String sourcePostalCode;
            sourcePostalCode = this.getPostalCode();
            String copyPostalCode = ((String) copyBuilder.copy(sourcePostalCode));
            copy.setPostalCode(copyPostalCode);
        } else {
            copy.postalCode = null;
        }
        if (this.isSetCountry()) {
            String sourceCountry;
            sourceCountry = this.getCountry();
            String copyCountry = ((String) copyBuilder.copy(sourceCountry));
            copy.setCountry(copyCountry);
        } else {
            copy.country = null;
        }
        return copy;
    }

    public Object copyTo(Object target) {
        final CopyBuilder copyBuilder = new JAXBCopyBuilder();
        return copyTo(target, copyBuilder);
    }

    public Object createCopy() {
        return new TridasAddress();
    }

}
